package Game;

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
			for(int i = 1; i < f-file ;i++)
			{
				if(P[r][file+i] != null)
					return false;
			}
			return true;
		}
		else if(r == row && f != file && f < file)
		{
			for(int i = 1; i < file - f;i++)
			{
				if(P[r][file-i] != null)
					return false;
			}
			return true;
		}	
		else if(r != row && f == file && r > row)
		{
			for(int i = 1; i < r-row;i++)
			{
				if(P[row + i][f] != null)
					return false;
			}
			return true;
		}
		else if(r != row && f == file && r < row)
		{
			for(int i = 1; i < row - r;i++)
			{
				if(P[row - i][f] != null)
					return false;
			}
			return true;
		}
		else
			return false;
	}

	public String getImage(boolean onWhite) {
		if(this.color == Piece.Side.WHITE)
		{		
			if(selected)
				return "/white_rook_selected.png";
			else if(onWhite)
				return  "/white_rook_on_white.png";
			else
				return  "/white_rook_on_black.png";
		}
		else
		{
			if(selected)
				return "/black_rook_selected.png";
			else if(onWhite)
				return  "/black_rook_on_white.png";
			else
				return  "/black_rook_on_black.png";

		}
	}
	
	public int value() {return 5;}
}
