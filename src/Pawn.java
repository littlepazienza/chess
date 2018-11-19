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

				if(Math.abs(r-row) <= 2)
					return true;
				else
					return false;
		
			}
			else
			{
			
				if(Math.abs(r-row) == 1)
					return true;
				else
					return false;
			
			}

		}
		else
		{

			if(f == file + 1 && r == row + 1)
			{

				if(P[f][r] != null && P[f][r].color != color)
					return true;
				else
					return false;
		
			}
			else if (f == file - 1 && r == row +1)
			{
			
				if(P[f][r] != null && P[f][r].color != color)
					return true;
				else
					return false;
			}
			else
				return false;
		}
	}

	public boolean attacking(int r, int f, Piece[][] A)
	{
		return validMove(r, f, A) && A[r][f].color != color;
	}

	@Override
	public ImageIcon getImage(boolean onWhite) {
		if(this.color == Piece.Side.WHITE && onWhite)
			return new ImageIcon("res/white_pawn_on_white.png");
		if(this.color == Piece.Side.WHITE && !onWhite)
			return new ImageIcon("res/white_pawn_on_black.png");
		if(this.color == Piece.Side.BLACK && onWhite)
			return new ImageIcon("res/black_pawn_on_white.png");
		else
			return new ImageIcon("res/black_pawn_on_black.png");
	}
}


