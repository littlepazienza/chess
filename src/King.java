public class King extends Piece
{
	
	public King(int r, int f, Side c)
	{
		super(f, r, c);
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

		return valid;
	}

	public boolean attacking(int r, int f, Piece[][] A)
	{
		return validMove(r, f, A) && A[r][f].color != color;
	}


}
