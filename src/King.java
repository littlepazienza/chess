import java.awt.Image;

public class King extends Piece
{
	protected Image img;

	public King(char f, int r, Image img)
	{
		super(f, r);
		this.img = img;
	}
	
}
