public abstract class Piece
{
	protected int file, row;
	
	public Piece(int f, int r)
	{
		this.file = f;
		this.row = r;
	}

	public boolean validMove(int f, int r);

}
