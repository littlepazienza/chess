public class Bishop extends Piece
{
	public Bishop(int r, int f, Side c)
	{
		super(f, r, c);
	}

	public boolean validMove(int r, int f, Piece[][] P)
	{
		return false;
	}
}
