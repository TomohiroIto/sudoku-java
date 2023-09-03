package sudoku.solver;

import sudoku.ISudokuGame;

public class SudokuCellsStatas
{

	private int[][] cellStates = new int[9][9];

	private ISudokuGame game;

	public ISudokuGame getGame()
	{
		return game;
	}

	public int CountPossibleList(int row, int column)
	{
		return Integer.bitCount(cellStates[row][column]);
	}

	public int GetPossibleList(int row, int column)
	{
		return cellStates[row][column];
	}

	public void setStatas(ISudokuGame game)
	{
		this.game = game.Copy();
		buildStatus();
	}

	public SudokuCellsStatas Copy()
	{
		SudokuCellsStatas stat = new SudokuCellsStatas();
		stat.game = this.game.Copy();

		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				stat.cellStates[r][c] = this.cellStates[r][c];
			}
		}

		return stat;
	}

	public Boolean IsValid()
	{
		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				if (game.GetNumber(r, c) == 0 && cellStates[r][c] <= 0)
				{
					return false;
				}
			}
		}

		return true;
	}

	public static Boolean IsIncluded(int numList, int numVal)
	{
		return (numList & (1 << numVal)) != 0;
	}

	public int FillUnique()
	{
		int count = 0;

		while (true)
		{
			int c_s = fillSimple();
			int c_f = fillBlock();

			if (c_s + c_f <= 0)
				break;
		}

		return count;
	}

	private int getFirstInt(int stateVal)
	{
		for (int i = 1; i <= 9; i++)
		{
			if (IsIncluded(stateVal, i))
			{
				return i;
			}
		}

		throw new IndexOutOfBoundsException("error");
	}

	private int fillSimple()
	{
		int count = 0;
		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				if (CountPossibleList(r, c) == 1)
				{
					game.SetNumber(r, c, getFirstInt(cellStates[r][c]));
					count++;
				}
			}
		}

		buildStatus();
		return count;
	}

	private int fillBlock()
	{
		int count = 0;

		// row
		for (int r = 0; r < 9; r++)
		{
			count += checkRow(r);
		}

		// column
		for (int c = 0; c < 9; c++)
		{
			count += checkColumn(c);
		}

		// block
		for (int r = 0; r < 3; r++)
		{
			for (int c = 0; c < 3; c++)
			{
				count += checkBlock(r, c);
			}
		}

		buildStatus();
		return count;
	}

	private int checkRow(int row)
	{
		int uniqueCnt = 0;

		for (int val = 1; val <= 9; val++)
		{
			int numCnt = 0;
			int findCol = -1;
			for (int c = 0; c < 9; c++)
			{
				if (IsIncluded(cellStates[row][c], val))
				{
					numCnt++;
					if (numCnt == 1)
					{
						findCol = c;
					}
					else
					{
						break;
					}
				}
			}

			if (numCnt == 1)
			{
				game.SetNumber(row, findCol, val);
				uniqueCnt++;
			}
		}

		return uniqueCnt;
	}

	private int checkColumn(int column)
	{
		int uniqueCnt = 0;

		for (int val = 1; val <= 9; val++)
		{
			int numCnt = 0;
			int findRow = -1;
			for (int r = 0; r < 9; r++)
			{
				if (IsIncluded(cellStates[r][column], val))
				{
					numCnt++;
					if (numCnt == 1)
					{
						findRow = r;
					}
					else
					{
						break;
					}
				}
			}

			if (numCnt == 1)
			{
				game.SetNumber(findRow, column, val);
				uniqueCnt++;
			}
		}

		return uniqueCnt;
	}

	private int checkBlock(int row, int column)
	{
		int uniqueCnt = 0;

		for (int val = 1; val <= 9; val++)
		{
			int numCnt = 0;
			int findCol = -1;
			int findRow = -1;
			LABEL: for (int c = 0; c < 3; c++)
			{
				for (int r = 0; r < 3; r++)
				{
					if (IsIncluded(cellStates[row * 3 + r][column * 3 + c], val))
					{
						numCnt++;
						if (numCnt == 1)
						{
							findCol = c;
							findRow = r;
						}
						else
						{
							break LABEL;
						}

					}
				}
			}

			if (numCnt == 1)
			{
				game.SetNumber(row * 3 + findRow, column * 3 + findCol, val);
				uniqueCnt++;
			}

		}

		return uniqueCnt;
	}

	public void buildStatus()
	{
		for (int r = 0; r < 9; r++)
		{
			for (int c = 0; c < 9; c++)
			{
				cellStates[r][c] = findPossible(game, r, c);
			}
		}
	}

	private int findPossible(ISudokuGame game, int row, int column)
	{
		int intGame = game.GetNumber(row, column);
		if (intGame != 0)
		{
			return 0;
		}

		int ret = 0;

		// row
		for (int r = 0; r < 9; r++)
		{
			int h = game.GetNumber(r, column);
			if (h != 0)
				ret |= (1 << h);
		}

		// column
		for (int c = 0; c < 9; c++)
		{
			int h = game.GetNumber(row, c);
			if (h != 0)
				ret |= (1 << h);
		}

		// block
		int rb = (row / 3) * 3;
		int cb = (column / 3) * 3;
		for (int r = rb; r < rb + 3; r++)
		{
			for (int c = cb; c < cb + 3; c++)
			{
				int h = game.GetNumber(r, c);
				if (h != 0)
					ret |= (1 << h);
			}
		}

		int realRet = ((1 << 10) - 2) - ret;

		return realRet;
	}

	public static int BitCnt(int val)
	{
		val = (val & 0x55555555) + ((val >> 1) & 0x55555555);
		val = (val & 0x33333333) + ((val >> 2) & 0x33333333);
		val = (val & 0x0f0f0f0f) + ((val >> 4) & 0x0f0f0f0f);
		val = (val & 0x00ff00ff) + ((val >> 8) & 0x00ff00ff);
		val = (val & 0x0000ffff) + ((val >> 16) & 0x0000ffff);

		return (int) val;
	}
}
