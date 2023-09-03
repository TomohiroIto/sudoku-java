package sudoku.solver;

import sudoku.ISudokuGame;

public interface ISudokuSolver
{
	SudokuSolveState Solve(ISudokuGame game, ISudokuGame result);

	int RecCount();
}
