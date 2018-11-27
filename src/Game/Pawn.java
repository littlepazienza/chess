package Game;
import javax.swing.ImageIcon;

public class Pawn extends Piece
{
	public Pawn(int r, int f, Side c)
	{
		super(f, r, c);
	}

	public boolean validMove(int r, int f, Piece[][] P)
	{
		if(f == file)
		{

			if(row == 1 || row == 6)
			{
				if(Math.abs(r-row) <= 2 && P[r][f] == null  && P[r-1][f] == null && color == Piece.Side.WHITE)
					return true;
				else if(Math.abs(r-row) <= 2 && P[r][f] == null  && P[r-1][f] == null && color == Piece.Side.BLACK)
					return true;
				else if(Math.abs(r-row) == 1 && r > row && color == Piece.Side.WHITE && P[r][f] == null)
					return true;
				else if(Math.abs(r-row) == 1 && r < row && color == Piece.Side.BLACK && P[r][f] == null)
					return true;
				else
					return false;
			}
			else
			{
			
				if(Math.abs(r-row) == 1 && r > row && color == Piece.Side.WHITE && P[r][f] == null)
					return true;
				else if(Math.abs(r-row) == 1 && r < row && color == Piece.Side.BLACK && P[r][f] == null)
					return true;
				else
					return false;
			
			}

		}
		else
		{

			if(f == file + 1 && r == row + 1)
			{

				if(P[r][f] != null && P[r][f].color != color && color == Piece.Side.WHITE)
					return true;
				else
					return false;
		
			}
			else if (f == file - 1 && r == row +1)
			{
			
				if(P[r][f] != null && P[r][f].color != color && color == Piece.Side.WHITE)
					return true;
				else
					return false;
			}
			else if (f == file + 1 && r == row - 1)
			{
				if(P[r][f] != null && P[r][f].color != color && color == Piece.Side.BLACK)
					return true;
				else
					return false;
			}
			else if(f == file - 1 && r == row - 1)
			{
				if(P[r][f] != null && P[r][f].color != color && color == Piece.Side.BLACK)
					return true;
				else
					return false;
			}else
				return false;
		}
	}


	public String getImage(boolean onWhite) {
		if(this.color == Piece.Side.WHITE)
		{		
			if(selected)
				return "/white_pawn_selected.png";
			else if(onWhite)
				return  "/white_pawn_on_white.png";
			else
				return  "/white_pawn_on_black.png";
		}
		else
		{
			if(selected)
				return "/black_pawn_selected.png";
			else if(onWhite)
				return  "/black_pawn_on_white.png";
			else
				return  "/black_pawn_on_black.png";

		}
	}
	
	public int value() {return 1;}
}


