import javax.swing.ImageIcon;

public class Rook extends Piece
{
	public Rook(int r, int f, Side c)
	{
		super(f, r, c);
	}

	public boolean validMove(int r, int f, Piece[][] P)
	{
		if(r == row && f != file && f > file)
		{
			for(int i = file; i < f ;i++)
			{
				if(P[r][i] != null)
					 return false;
			}
			return true;
		}
		else if(r == row && f != file && f < file)
		{
			for(int i = file; i > f;i--)
			{
				if(P[r][i] != null)
					return false;
			}
			return true;
		}
		else
			return false;
	}

	public boolean attacking(int r, int f, Piece[][] A)
	{
		return validMove(r, f, A) && A[r][f].color != color;
	}
	
	public ImageIcon getImage(boolean onWhite) {
		if(this.color == Piece.Side.WHITE && onWhite)
			return new ImageIcon("res/white_rook_on_white.png");
		if(this.color == Piece.Side.WHITE && !onWhite)
			return new ImageIcon("res/white_rook_on_black.png");
		if(this.color == Piece.Side.BLACK && onWhite)
			return new ImageIcon("res/black_rook_on_white.png");
		else
			return new ImageIcon("res/black_rook_on_black.png");
	}
}
