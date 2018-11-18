public class Pawn extends Piece
{
	public Pawn(int r, int f, Side c)
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


