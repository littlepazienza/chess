public class Queen extends Piece
{
	public Queen(int r, int f, Side c)
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
		else if(r != row && f == file && r > row)
		{
			for(int i = row; i < r;i++)
			{
				if(P[i][f] != null)
					return false;
			}
			return true;
		}
		else if(r != row && f == file && r < row)
		{
			for(int i = row; i > r;i--)
			{
				if(P[i][f] != null)
					return false;
			}
			return true;
		}
		else if(Math.abs(r -row) == Math.abs(f - file) && r > row && f > file)
		{
			for(int i = row; i < r;i++)
			{
				if(P[i][i] != null)
					return false;
			}
			return true;
		}
		else if(Math.abs(r - row) == Math.abs(f - file) && r < row && f < file)
		{
			for(int i = row; i > r;i--)
			{
				if(P[i][i] != null)
					return false;
			}
			return true;
		}
		else if(Math.abs(r - row) == Math.abs(f - file) && r < row && f > file)
		{
			for(int i = file; i < f;i++)
			{
				if(P[1-i][i] != null)
					return false;
			}
			return true;
		}
		else if(Math.abs(r - row) == Math.abs(f - file) && r > row && f < file)
		{
			for(int i = row; i < r;i++)
			{
				if(P[i][1-i] != null)
					return false;
			}
			return true;
		}
		else
			return false;
	}

	public boolean attacking(int r, int f, Piece[][] P)
	{
		return false;
	}
}
