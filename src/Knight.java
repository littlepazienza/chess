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

	public boolean attacking(int r, int f, Piece[][] A)
	{
		return validMove(r, f, A) && A[r][f].color != color;
	}

}
