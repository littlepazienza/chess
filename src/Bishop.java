import javax.swing.ImageIcon;

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
	
	public boolean attacking(int r, int f, Piece[][] A)
	{
		return validMove(r, f, A) && A[r][f].color != color;
	}
	
	public ImageIcon getImage(boolean onWhite) {
		if(this.color == Piece.Side.WHITE && onWhite)
			return new ImageIcon("res/white_bishop_on_white.png");
		if(this.color == Piece.Side.WHITE && !onWhite)
			return new ImageIcon("res/white_bishop_on_black.png");
		if(this.color == Piece.Side.BLACK && onWhite)
			return new ImageIcon("res/black_bishop_on_white.png");
		else
			return new ImageIcon("res/black_bishop_on_black.png");
	}
	
	public int value() {return 3;}
}
