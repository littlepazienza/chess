package Game;
import javax.swing.ImageIcon;

public class Knight extends Piece
{
	public Knight(int r, int f, Side c)
	{
		super(f, r, c);
	}

	public boolean validMove(int r, int f, Piece[][] P)
	{
		if(r == row + 2 && f == file + 1)
			return true;
		else if(r == row - 2 && f == file + 1)
			return true;
		else if(r == row + 2 && f == file -1)
			return true;
		else if(r == row - 2 && f == file - 1)
			return true;
		else if(r == row + 1 && f == file + 2)
			return true;
		else if(r == row + 1 && f == file - 2)
			return true;
		else if(r == row - 1 && f == file + 2)
			return true;
		else if(r == row - 1 && f == file - 2)
			return true;
		else
			return false;
	}


	
	public String getImage(boolean onWhite) {
		if(this.color == Piece.Side.WHITE)
		{		
			if(selected)
				return "/white_knight_selected.png";
			else if(onWhite)
				return  "/white_knight_on_white.png";
			else
				return  "/white_knight_on_black.png";
		}
		else
		{
			if(selected)
				return "/black_knight_selected.png";
			else if(onWhite)
				return  "/black_knight_on_white.png";
			else
				return  "/black_knight_on_black.png";

		}
	}	

	public int value() {return 3;}
}
