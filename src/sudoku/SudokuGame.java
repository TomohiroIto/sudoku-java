package sudoku;

public class SudokuGame implements ISudokuGame
{
	private int[][] board = new int[9][9];
	private int restNum = 0;

	public int getRestNum()
	{
		return restNum;
	}

	private static final int[][][] BD_GROUP = {
			{ { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 0 }, { 1, 1 }, { 1, 2 },
					{ 2, 0 }, { 2, 1 }, { 2, 2 } },
			{ { 0, 3 }, { 0, 4 }, { 0, 5 }, { 1, 3 }, { 1, 4 }, { 1, 5 },
					{ 2, 3 }, { 2, 4 }, { 2, 5 } },
			{ { 0, 6 }, { 0, 7 }, { 0, 8 }, { 1, 6 }, { 1, 7 }, { 1, 8 },
					{ 2, 6 }, { 2, 7 }, { 2, 8 } },
			{ { 3, 0 }, { 3, 1 }, { 3, 2 }, { 4, 0 }, { 4, 1 }, { 4, 2 },
					{ 5, 0 }, { 5, 1 }, { 5, 2 } },
			{ { 3, 3 }, { 3, 4 }, { 3, 5 }, { 4, 3 }, { 4, 4 }, { 4, 5 },
					{ 5, 3 }, { 5, 4 }, { 5, 5 } },
			{ { 3, 6 }, { 3, 7 }, { 3, 8 }, { 4, 6 }, { 4, 7 }, { 4, 8 },
					{ 5, 6 }, { 5, 7 }, { 5, 8 } },
			{ { 6, 0 }, { 6, 1 }, { 6, 2 }, { 7, 0 }, { 7, 1 }, { 7, 2 },
					{ 8, 0 }, { 8, 1 }, { 8, 2 } },
			{ { 6, 3 }, { 6, 4 }, { 6, 5 }, { 7, 3 }, { 7, 4 }, { 7, 5 },
					{ 8, 3 }, { 8, 4 }, { 8, 5 } },
			{ { 6, 6 }, { 6, 7 }, { 6, 8 }, { 7, 6 }, { 7, 7 }, { 7, 8 },
					{ 8, 6 }, { 8, 7 }, { 8, 8 } } };

	// / <summary>
	// / constructor
	// / </summary>
	public SudokuGame()
	{
		Init();
	}

	// / <summary>
	// / Clear this game with the given one.
	// / </summary>
	// / <param name="game"></param>
	public void ClearGame(ISudokuGame game)
	{
		for (int r = 0; r < 9; r++)
			for (int c = 0; c < 9; c++)
				board[r][c] = game.GetNumber(r, c);

		this.restNum = game.getRestNum();
	}

	// / <summary>
	// / Initialize this object
	// / </summary>
	public void Init()
	{
		restNum = 81;

		for (int r = 0; r < 9; r++)
			for (int c = 0; c < 9; c++)
				board[r][c] = 0;
	}

	// / <summary>
	// / tostring
	// / </summary>
	// / <returns></returns>
	public String ToString()
	{
		String ret = "";

		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				ret += ((board[r][c] == 0) ? "" : Integer.toString(board[r][c]))
						+ "\t";
			}

			ret += "\r\n";
		}

		return ret;
	}

	// / <summary>
	// / set a number to the cell.
	// / </summary>
	// / <param name="r"></param>
	// / <param name="c"></param>
	// / <param name="num"></param>
	public void SetNumber(int r, int c, int num)
	{
		checkIndex(r);
		checkIndex(c);
		checkNumber(num);

		// check cell status
		if (board[r][c] == 0)
		{
			if (num == 0)
			{
				// do nothing
			}
			else
			{
				// fill a blank cell
				restNum--;
			}
		}
		else
		{
			if (num == 0)
			{
				// make the cell blank
				restNum++;
			}
			else
			{
				// do nothing
			}
		}

		board[r][c] = num;
	}

	// / <summary>
	// / get the number of the cell.
	// / </summary>
	// / <param name="r"></param>
	// / <param name="c"></param>
	// / <returns></returns>
	public int GetNumber(int r, int c)
	{
		checkIndex(r);
		checkIndex(c);

		return board[r][c];
	}

	// / <summary>
	// / check game status
	// / </summary>
	// / <returns></returns>
	public SudokuBlockState CheckStatus()
	{
		SudokuBlockState state = SudokuBlockState.Complete;

		for (int r = 0; r < 9; r++)
		{
			SudokuBlockState stateR = checkRow(r);
			if (stateR == SudokuBlockState.Illegal)
				return SudokuBlockState.Illegal;
			if (stateR == SudokuBlockState.Building)
				state = SudokuBlockState.Building;
		}

		for (int c = 0; c < 9; c++)
		{
			SudokuBlockState stateC = checkColumn(c);
			if (stateC == SudokuBlockState.Illegal)
				return SudokuBlockState.Illegal;
			if (stateC == SudokuBlockState.Building)
				state = SudokuBlockState.Building;
		}

		for (int i = 0; i < 9; i++)
		{
			SudokuBlockState stateC = checkBlock(i);
			if (stateC == SudokuBlockState.Illegal)
				return SudokuBlockState.Illegal;
			if (stateC == SudokuBlockState.Building)
				state = SudokuBlockState.Building;
		}

		return state;
	}

	// / <summary>
	// / copy object
	// / </summary>
	// / <returns></returns>
	public ISudokuGame Copy()
	{
		SudokuGame game = new SudokuGame();

		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				game.SetNumber(r, c, this.board[r][c]);
			}
		}
		game.restNum = this.restNum;

		return game;
	}

	public void Copy(ISudokuGame game)
	{
		for (int r = 0; r < 9; r++)
			for (int c = 0; c < 9; c++)
				this.SetNumber(r, c, game.GetNumber(r, c));
	}

	// / <summary>
	// / check row
	// / </summary>
	// / <param name="r"></param>
	// / <returns></returns>
	private SudokuBlockState checkRow(int r)
	{
		checkIndex(r);

		SudokuBlockState ret = SudokuBlockState.Complete;
		int check = 0;
		for (int c = 0; c < 9; c++)
		{
			int num = board[r][c];

			if (num != 0)
			{
				if ((check & (1 << num)) != 0)
					return SudokuBlockState.Illegal;
				check |= (1 << num);
			}
			else
			{
				ret = SudokuBlockState.Building;
			}
		}

		return ret;
	}

	// / <summary>
	// / check column
	// / </summary>
	// / <param name="c"></param>
	// / <returns></returns>
	private SudokuBlockState checkColumn(int c)
	{
		checkIndex(c);

		SudokuBlockState ret = SudokuBlockState.Complete;
		int check = 0;
		for (int r = 0; r < 9; r++)
		{
			int num = board[r][c];

			if (num != 0)
			{
				if ((check & (1 << num)) != 0)
					return SudokuBlockState.Illegal;
				check |= (1 << num);
			}
			else
			{
				ret = SudokuBlockState.Building;
			}
		}

		return ret;
	}

	// / <summary>
	// / check a block
	// / </summary>
	// / <param name="c"></param>
	// / <returns></returns>
	private SudokuBlockState checkBlock(int i)
	{
		SudokuBlockState ret = SudokuBlockState.Complete;

		int check = 0;
		for (int b = 0; b < 9; b++)
		{
			int num = board[BD_GROUP[i][b][0]][BD_GROUP[i][b][1]];

			if (num != 0)
			{
				if ((check & (1 << num)) != 0)
					return SudokuBlockState.Illegal;
				check |= (1 << num);
			}
			else
			{
				ret = SudokuBlockState.Building;
			}
		}

		return ret;
	}

	// / <summary>
	// / check a block
	// / </summary>
	// / <param name="rb"></param>
	// / <param name="cb"></param>
	// / <returns></returns>
	private SudokuBlockState checkBlock(int rb, int cb)
	{
		checkBlockIndex(rb);
		checkBlockIndex(cb);

		SudokuBlockState ret = SudokuBlockState.Complete;

		int check = 0;
		for (int r = 0; r < 3; r++)
		{
			for (int c = 0; c < 3; c++)
			{
				int num = board[rb * 3 + r][cb * 3 + c];

				if (num != 0)
				{
					if ((check & (1 << num)) != 0)
						return SudokuBlockState.Illegal;
					check |= (1 << num);
				}
				else
				{
					ret = SudokuBlockState.Building;
				}
			}
		}

		return ret;
	}

	// / <summary>
	// / varidate block index
	// / </summary>
	// / <param name="block"></param>
	private void checkBlockIndex(int block)
	{
		if (block < 0 || block >= 3)
			throw new IndexOutOfBoundsException("block index error");
	}

	// / <summary>
	// / varidate index
	// / </summary>
	// / <param name="index"></param>
	private void checkIndex(int index)
	{
		if (index < 0 || index >= 9)
			throw new IndexOutOfBoundsException("index error");
	}

	// / <summary>
	// / number check
	// / </summary>
	// / <param name="number"></param>
	private void checkNumber(int number)
	{
		if (number < 0 || number > 9)
			throw new IndexOutOfBoundsException("cell number error");
	}
}
