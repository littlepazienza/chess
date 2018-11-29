package Game;
import javax.swing.ImageIcon;

public class King extends Piece
{
	protected boolean moved;
	public King(int r, int f, Side c)
	{
		super(f, r, c);
		moved = false;
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

		//castling case
		if(color == Piece.Side.WHITE)
		{
			if(r == row && row == 0 && !moved && A[r][f] instanceof Rook)
			{
				if(f < file)
				{
					for(int i = 0; i < file - f;i++)
						if(A[row][file - i] != null)
							return false;
					return true;
				}
				else
				{
					for(int i = 1; i < f - file;i++)
						if(A[row][file + i] != null)
							return false;
					return true;
				}
			}
		}
		else
		{
			if(r == row && row == 7 && !moved && A[r][f] instanceof Rook)
			{
				if(f < file)
				{
					for(int i = 0; i < file - f;i++)
						if(A[row][file - i] != null)
							return false;
					return true;
				}
				else
				{
					for(int i = 0; i < f - file;i++)
						if(A[row][file + i] != null)
							return false;
					return true;
				}
			}
		}
		
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
