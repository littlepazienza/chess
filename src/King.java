import javax.swing.ImageIcon;

public class King extends Piece
{
	
	public King(int r, int f, Side c)
	{
		super(f, r, c);
	}

	public boolean validMove(int r, int f, Piece[][] A)
	{
		boolean valid = false;
		if(r == row + 1 && f == file + 1)
			valid = true;
		if(r == row -1 && f == file - 1)
			valid = true;
		if(r == row + 1 && f == file - 1)
			valid = true;
		if(r == row -1 && f == file + 1)
			valid = true;
		if(r == row && f == file + 1)
			valid = true;
		if(r == row && f == file - 1)
			valid = true;
		if(r == row + 1 && f == file)
			valid = true;
		if(r == row - 1 && f == file)
			valid = true;

		return valid;
	}

	public boolean attacking(int r, int f, Piece[][] A)
	{
		return validMove(r, f, A) && A[r][f].color != color;
	}
	
	public ImageIcon getImage(boolean onWhite) {
		if(this.color == Piece.Side.WHITE && onWhite)
			return new ImageIcon("res/white_king_on_white.png");
		if(this.color == Piece.Side.WHITE && !onWhite)
			return new ImageIcon("res/white_king_on_black.png");
		if(this.color == Piece.Side.BLACK && onWhite)
			return new ImageIcon("res/black_king_on_white.png");
		else
			return new ImageIcon("res/black_king_on_black.png");
	}


}
