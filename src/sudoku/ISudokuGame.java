package sudoku;

public interface ISudokuGame
{

	int getRestNum();

	// / <summary>
	// / Clear this game with the given one.
	// / </summary>
	// / <param name="game"></param>
	void ClearGame(ISudokuGame game);

	// / <summary>
	// / Initialize this object
	// / </summary>
	void Init();

	// / <summary>
	// / set a number to the cell.
	// / </summary>
	// / <param name="r"></param>
	// / <param name="c"></param>
	// / <param name="num"></param>
	void SetNumber(int r, int c, int num);

	// / <summary>
	// / get the number of the cell.
	// / </summary>
	// / <param name="r"></param>
	// / <param name="c"></param>
	// / <returns></returns>
	int GetNumber(int r, int c);

	// / <summary>
	// / check game status
	// / </summary>
	// / <returns></returns>
	SudokuBlockState CheckStatus();

	// / <summary>
	// / copy object
	// / </summary>
	// / <returns></returns>
	ISudokuGame Copy();

	// / <summary>
	// / copy object
	// / </summary>
	// / <returns></returns>
	void Copy(ISudokuGame game);

}
