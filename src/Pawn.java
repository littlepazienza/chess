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
}


