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
			for(int i = file; i < f ;i++)
			{
				if(P[r][i] != null)
					 return false;
			}
			return true;
		}
		else if(r == row && f != file && f < file)
		{
			for(int i = file; i > f;i--)
			{
				if(P[r][i] != null)
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
}
