package sudoku.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sudoku.ISudokuGame;
import sudoku.SudokuGame;
import sudoku.solver.ISudokuSolver;
import sudoku.solver.SudokuSolveState;

public class SudokuBuilderV2014_01 implements ISudokuBuilder
{
	private ISudokuGame objSudokuGameParam = new SudokuGame();
	private Random rand = new Random();

	public ISudokuGame CreateGame(ISudokuSolver solver)
	{
		ISudokuGame game = new SudokuGame();

		gameInit(game);
		mixNumbers(game);

		if (tryCreate(game, solver))
		{
			return game;
		}

		throw new IndexOutOfBoundsException("erro creating game");
	}

	private void gameInit(ISudokuGame game)
	{
		int lB = rand.nextInt(10);

		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				int tgtNum = BuilderConst.boardList[lB][r + c * 9];
				game.SetNumber(r, c, tgtNum);
			}
		}
	}

	private void mixNumbers(ISudokuGame game)
	{
		for (int i = 0; i < 25; i++)
		{
			int b = i % 3;

			int r1 = b * 3 + (rand.nextInt(3));
			int r2 = b * 3 + (rand.nextInt(3));

			if (r1 == r2)
				continue;

			if (rand.nextInt(2) == 0)
				exchangeRows(game, r1, r2);
			if (rand.nextInt(2) == 0)
				exchangeColumns(game, r1, r2);
		}
	}

	private void exchangeRows(ISudokuGame game, int r1, int r2)
	{
		if (r1 / 3 != r2 / 3)
			throw new IndexOutOfBoundsException("illegal call");

		for (int c = 0; c < 9; c++)
		{
			int tmpa = game.GetNumber(r1, c);
			game.SetNumber(r1, c, game.GetNumber(r2, c));
			game.SetNumber(r2, c, tmpa);
		}
	}

	private void exchangeColumns(ISudokuGame game, int c1, int c2)
	{
		if (c1 / 3 != c2 / 3)
			throw new IndexOutOfBoundsException("illegal call");

		for (int r = 0; r < 9; r++)
		{
			int tmpa = game.GetNumber(r, c1);
			game.SetNumber(r, c1, game.GetNumber(r, c2));
			game.SetNumber(r, c2, tmpa);
		}
	}

	private Boolean tryCreate(ISudokuGame game, ISudokuSolver solver)
	{
		int a = 0;
		while (a++ < 50)
		{
			for (int rb = 0; rb < 3; rb++)
			{
				for (int cb = 0; cb < 3; cb++)
				{
					Boolean f = checkLoop(game, solver, rb, cb);
					if (!f)
						return true;

					if (game.getRestNum() > 53)
					{
						return lastPadding(game, solver);
					}
				}
			}
		}

		return true;
	}

	private Boolean lastPadding(ISudokuGame game, ISudokuSolver solver)
	{
		List<Integer[]> hintList = new ArrayList<Integer[]>();

		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				int v = game.GetNumber(r, c);
				if (v != 0)
				{
					Integer[] rc = new Integer[] { r, c };
					hintList.add(rc);
				}
			}
		}

		List<ISudokuGame> gameList = new ArrayList<ISudokuGame>();
		List<Integer> gameListRec = new ArrayList<Integer>();
		_params param = new _params();
		param.maxIndex = 0;
		param.maxRec = 0;
		buildSudokuTree(game, hintList, solver, gameList, gameListRec, param);

		if (gameList.size() == 0)
		{
			return true;
		}
		else
		{
			game.ClearGame(gameList.get(param.maxIndex));
			return true;
		}
	}

	private Boolean checkLoop(ISudokuGame game, ISudokuSolver solver, int rb,
			int cb)
	{
		Boolean f = true;

		for (int i = 0; i < 5; i++)
		{
			int ro = rand.nextInt(3);
			int co = rand.nextInt(3);

			int r = rb * 3 + ro;
			int c = cb * 3 + co;

			int numTemp = game.GetNumber(r, c);
			if (numTemp == 0)
				return true;

			int numTemp2 = game.GetNumber(8 - r, 8 - c);

			game.SetNumber(r, c, 0);
			game.SetNumber(8 - r, 8 - c, 0);

			f = gameFinished(game, solver);

			if (f)
			{
				return true;
			}
			else
			{
				game.SetNumber(r, c, numTemp);
				game.SetNumber(8 - r, 8 - c, numTemp2);
			}
		}

		return false;
	}

	private Boolean buildSudokuTree(ISudokuGame game, List<Integer[]> hintList,
			ISudokuSolver solver, List<ISudokuGame> gameList,
			List<Integer> gameListRec, _params param)
	{
		int solveCount = 0;

		for (int i = hintList.size() - 1; i >= 0; i--)
		{
			int rX = hintList.get(i)[0];
			int rY = hintList.get(i)[1];

			int numTemp = game.GetNumber(rX, rY);

			game.SetNumber(rX, rY, 0);

			SudokuSolveState slvStateT = solver.Solve(game, objSudokuGameParam);

			if (slvStateT == SudokuSolveState.Solvable)
			{
				solveCount++;
			}
			else
			{
				hintList.remove(i);
			}

			game.SetNumber(rX, rY, numTemp);
		}

		if (solveCount == 0)
		{
			gameList.add(game);
			solver.Solve(game, objSudokuGameParam);
			gameListRec.add(solver.RecCount());

			if (param.maxRec < solver.RecCount())
			{
				param.maxRec = solver.RecCount();
				param.maxIndex = gameListRec.size() - 1;
			}

			return true;
		}

		System.out.println("buildSudokuTree 検索終了、残: " + hintList.size());

		int NUM_TEST_COUNT = 1;
		List<Integer> hintLoop = new ArrayList<Integer>();
		if (hintList.size() <= NUM_TEST_COUNT)
		{
			for (int l = 0; l < hintList.size(); l++)
				hintLoop.add(l);
		}
		else
		{
			for (int l = 0; l < NUM_TEST_COUNT; l++)
			{
				int rdIndex = rand.nextInt(hintList.size());
				if (!hintLoop.contains(rdIndex))
					hintLoop.add(rdIndex);
			}
		}

		for (int i = 0; i < hintLoop.size(); i++)
		{
			int hintIndex = hintLoop.get(i);
			int rX = hintList.get(hintIndex)[0];
			int rY = hintList.get(hintIndex)[1];

			int numTemp = game.GetNumber(rX, rY);

			game.SetNumber(rX, rY, 0);

			ISudokuGame game2 = game.Copy();
			List<Integer[]> hintList2 = new ArrayList<Integer[]>(hintList);
			hintList2.remove(hintIndex);
			buildSudokuTree(game2, hintList2, solver, gameList, gameListRec,
					param);

			game.SetNumber(rX, rY, numTemp);
		}

		return true;
	}

	private Boolean gameFinished(ISudokuGame game, ISudokuSolver solver)
	{
		SudokuSolveState slvStateT = solver.Solve(game, objSudokuGameParam);
		return (slvStateT == SudokuSolveState.Solvable);
	}

	class _params
	{
		Integer maxIndex;
		Integer maxRec;
	}
}
