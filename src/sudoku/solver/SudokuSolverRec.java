package sudoku.solver;

import java.util.ArrayList;
import java.util.List;

import sudoku.ISudokuGame;
import sudoku.SudokuBlockState;

public class SudokuSolverRec implements ISudokuSolver
{
	private int recCnt = 0;

	public int RecCount()
	{
		return recCnt;
	}

	public SudokuSolveState Solve(ISudokuGame game, ISudokuGame result)
	{
		recCnt = 0;

		if (game.CheckStatus() == SudokuBlockState.Illegal)
		{
			return SudokuSolveState.Illegal;
		}

		ISudokuGame testGame = game.Copy();

		SudokuCellsStatas status = new SudokuCellsStatas();
		status.setStatas(testGame);

		status.FillUnique();

		if (status.getGame().CheckStatus() == SudokuBlockState.Complete)
		{
			result.Copy(status.getGame());
			return SudokuSolveState.Solvable;
		}

		if (!status.IsValid())
		{
			return SudokuSolveState.NoResult;
		}

		List<ISudokuGame> results = findPossibleList(status);

		if (results.size() == 1)
		{
			result.Copy(results.get(0));
			return SudokuSolveState.Solvable;
		}
		else if (results.size() == 0)
		{
			return SudokuSolveState.NoResult;
		}
		else
		{
			return SudokuSolveState.HasMultipleResults;
		}
	}

	private List<ISudokuGame> findPossibleList(SudokuCellsStatas status)
	{
		recCnt++;

		List<ISudokuGame> list = new ArrayList<ISudokuGame>();

		_numparams param = new _numparams();
		findLess(status, param);

		List<Integer> resultList = createList(param.listnum);
		for (Integer testValue : resultList)
		{
			SudokuCellsStatas statusTest = status.Copy();
			statusTest.getGame().SetNumber(param.row, param.column, testValue);
			statusTest.buildStatus();

			statusTest.FillUnique();

			SudokuBlockState blockState = statusTest.getGame().CheckStatus();
			if (statusTest.IsValid() && blockState == SudokuBlockState.Building)
			{
				list.addAll(findPossibleList(statusTest));

				if (list.size() > 1)
					break;
			}
			else if (blockState == SudokuBlockState.Complete)
			{
				list.add(statusTest.getGame());

				if (list.size() > 1)
					break;
			}
		}

		return list;
	}

	private int findLess(SudokuCellsStatas game, _numparams param)
	{
		param.row = 0;
		param.column = 0;
		param.listnum = 0;
		int minList = 9;

		for (int row = 0; row < 9; row++)
		{
			for (int column = 0; column < 9; column++)
			{
				if (game.getGame().GetNumber(row, column) != 0)
					continue;

				int tmpList = game.CountPossibleList(row, column);
				if (tmpList > 0 && tmpList < minList)
				{
					param.row = row;
					param.column = column;

					int listValue = game.GetPossibleList(row, column);
					param.listnum = listValue;
					minList = tmpList;
				}
			}
		}

		return 0;
	}

	private List<Integer> createList(int nList)
	{
		List<Integer> rList = new ArrayList<Integer>();

		for (int val = 1; val <= 9; val++)
		{
			if (SudokuCellsStatas.IsIncluded(nList, val))
			{
				rList.add(val);
			}
		}

		return rList;
	}

	class _numparams
	{
		int row = 0;
		int column = 0;
		int listnum = 0;
	}
}
