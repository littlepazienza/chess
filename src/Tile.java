public class Tile
{
	protected Piece piece;

	public Tile()
	{
		piece = null;
	}

	public Tile(Piece p)
	{
		this.piece = p;
	}

	public boolean validMove(int r, int f)
	{
		return piece.validMove(r, f);
	}

	public void evaluateMove(Tile t)
	{
			
	}
}
