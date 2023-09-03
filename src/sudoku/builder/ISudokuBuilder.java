package sudoku.builder;

import sudoku.ISudokuGame;
import sudoku.solver.ISudokuSolver;

public interface ISudokuBuilder
{
	ISudokuGame CreateGame(ISudokuSolver solver);
}
