public class Rook extends Piece
{
	public Rook(int r, int f, Side c)
	{
		super(f, r, c);
	}

	public boolean validMove(int r, int f, Piece[][] P)
	{
		return false;
	}

	public boolean attacking(int r, int f, Piece[][] A)
	{
		return false;
	}
}
