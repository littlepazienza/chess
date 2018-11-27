package Game;
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


	public String getImage(boolean onWhite) {
		if(this.color == Piece.Side.WHITE)
		{		
			if(selected)
				return "/white_king_selected.png";
			else if(onWhite)
				return  "/white_king_on_white.png";
			else
				return  "/white_king_on_black.png";
		}
		else
		{
			if(selected)
				return "/black_king_selected.png";
			else if(onWhite)
				return  "/black_king_on_white.png";
			else
				return  "/black_king_on_black.png";

		}
	}	

	public int value() {return 0;}

}
