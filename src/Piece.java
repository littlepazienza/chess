public abstract class Piece
{
	protected int file, row;
	protected Side color;
	
	public Piece(int f, int r, Side c)
	{
		this.file = f;
		this.row = r;
		this.color = c;
	}

	public abstract boolean validMove(int f, int r);


	public enum Side
	{
		WHITE, BLACK
	}
}
