public class Queen extends Piece
{
	public Queen(int r, int f, Side c)
	{
		super(f, r, c);
	}

	public boolean validMove(int r, int f)
	{
		return false;
	}
}
