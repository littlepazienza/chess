package Game;
import javax.swing.ImageIcon;

import Game.Piece.Side;

public class Bishop extends Piece
{
	public Bishop(int r, int f, Side c)
	{
		super(f, r, c);
	}

	public boolean validMove(int r, int f, Piece[][] P)
	{
		if(Math.abs(r -row) == Math.abs(f - file) && r > row && f > file)
		{
			for(int i = 1; i < r -row;i++)
		 	{
		    if(P[row + i][file + i] != null)
				  return false;
			}
			return true;
		}
		else if(Math.abs(r - row) == Math.abs(f - file) && r < row && f < file)
		{
			for(int i = 1; i < f - file;i++)
		  {
		    if(P[row - i][file - i] != null)
		      return false;
		  }
		  return true;
		}
		else if(Math.abs(r - row) == Math.abs(f - file) && r < row && f > file)
		{
		  for(int i = 1; i < f - file;i++)
		  {
		    if(P[row - i][file + i] != null)
		       return false;
		  }
		  return true;
		}
		else if(Math.abs(r - row) == Math.abs(f - file) && r > row && f < file)
		{
		  for(int i = 1; i < r-row;i++)
		  {
			  if(P[row + i][file - i] != null)
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
				return "/white_bishop_selected.png";
			else if(onWhite)
				return  "/white_bishop_on_white.png";
			else
				return  "/white_bishop_on_black.png";
		}
		else
		{
			if(selected)
				return "/black_bishop_selected.png";
			else if(onWhite)
				return  "/black_bishop_on_white.png";
			else
				return  "/black_bishop_on_black.png";

		}
	}	
	
	public int value() {return 3;}
}
