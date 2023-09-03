package sudoku.solver;

/// <summary>
/// Sudoku game status
/// </summary>
public enum SudokuSolveState
{
	Solvable,
	HasMultipleResults,
	NoResult,
	Illegal
}
