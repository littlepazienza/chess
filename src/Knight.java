public class Knight extends Piece
{
	public Knight(int r, int f, Side c)
	{
		super(f, r, c);
	}

	public boolean validMove(int r, int f)
	{
		return false;
	}
}
