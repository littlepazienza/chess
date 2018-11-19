import java.util.ArrayList;

public class GameBoard
{

	protected Piece[][] board = new Piece[8][8];
	protected ArrayList<Piece> captured;
	
	
	public GameBoard()
	{
		board = new Piece[8][8];
		captured = new ArrayList<>();
	}
	
	public void GameFill()
	{
		//fill white
		board[0][0] = new Rook(0, 0, Piece.Side.WHITE);
		board[0][1] = new Knight(0, 1, Piece.Side.WHITE);
		board[0][2] = new Bishop(0, 2, Piece.Side.WHITE);
		board[0][3] = new Queen(0, 3, Piece.Side.WHITE);
		board[0][4] = new King(0, 4, Piece.Side.WHITE);
		board[0][5] = new Bishop(0, 5, Piece.Side.WHITE);
		board[0][6] = new Knight(0, 6, Piece.Side.WHITE);
		board[0][7] = new Rook(0, 7, Piece.Side.WHITE);
		board[1][0] = new Pawn(1, 0, Piece.Side.WHITE);
		board[1][1] = new Pawn(1, 1, Piece.Side.WHITE);
		board[1][2] = new Pawn(1, 2, Piece.Side.WHITE);
		board[1][3] = new Pawn(1, 3, Piece.Side.WHITE);
		board[1][4] = new Pawn(1, 4, Piece.Side.WHITE);
		board[1][5] = new Pawn(1, 5, Piece.Side.WHITE);
		board[1][6] = new Pawn(1, 6, Piece.Side.WHITE);
		board[1][7] = new Pawn(1, 7, Piece.Side.WHITE);
		
		//fill black
		board[7][7] = new Rook(7, 7, Piece.Side.BLACK);
		board[7][6] = new Knight(7, 6, Piece.Side.BLACK);
		board[7][5] = new Bishop(7, 5, Piece.Side.BLACK);
		board[7][4] = new Queen(7, 4, Piece.Side.BLACK);
		board[7][3] = new King(7, 3, Piece.Side.BLACK);
		board[7][2] = new Bishop(7, 2, Piece.Side.BLACK);
		board[7][1] = new Knight(7, 1, Piece.Side.BLACK);
		board[7][0] = new Rook(7, 0, Piece.Side.BLACK);
		board[6][7] = new Pawn(6, 7, Piece.Side.BLACK);
		board[6][6] = new Pawn(6, 6, Piece.Side.BLACK);
		board[6][5] = new Pawn(6, 5, Piece.Side.BLACK);
		board[6][4] = new Pawn(6, 4, Piece.Side.BLACK);
		board[6][3] = new Pawn(6, 3, Piece.Side.BLACK);
		board[6][2] = new Pawn(6, 2, Piece.Side.BLACK);
		board[6][1] = new Pawn(6, 1, Piece.Side.BLACK);
		board[6][0] = new Pawn(6, 0, Piece.Side.BLACK);

		for(int i = 2; i < 6;i++)
			for(int j = 0; j < 8;j++)
				board[i][j] = null;
	}

	public boolean makeMove(int fromR, int fromF, int toR, int toF)
	{
		if(board[fromR][fromF] == null)
			return false;

		if(!board[fromR][fromF].validMove(toR, toF, board))
			return false;
		
		if(board[toR][toF] != null)
		{

			if(board[toR][toF].color == board[fromR][fromF].color)
			{
				return false;
			}
			else
			{

				if(inCheckIfMoveMade(fromR, fromF, toR, toF))
				{
					return false;
				}
				else
				{
					captured.add(board[fromR][fromF]);
					board[toR][toF] = board[fromR][fromF];
					board[toR][toF].setCoord(toR, toF);
					board[fromR][fromF] = null;
					return true;
				}

			}
		}
		else
		{
			if(inCheckIfMoveMade(fromR, fromF, toR, toF))
				return false;
			else
			{
				captured.add(board[fromR][fromF]);
				board[toR][toF] = board[fromR][fromF];
				board[toR][toF].setCoord(toR, toF);
				board[fromR][fromF] = null;
				return true;
			}
		}		
	}

	private boolean inCheckIfMoveMade(int fromR, int fromF, int toR, int toF)
	{
		Piece[][] temp = new Piece[8][8];
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				temp[i][j] = board[i][j];

		temp[toR][toF] = temp[fromR][fromF];
		temp[toR][toF].setCoord(toR, toF);
		temp[fromR][fromF] = null;

		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8;j++)
				if(temp[i][j] != null && temp[i][j].attacking(rowOfKing(temp[i][j].color, temp),
																fileOfKing(temp[i][j].color, temp), temp))			
					return true;

		return false;
	}
	
	private int rowOfKing(Piece.Side c, Piece[][] A)
	{
		for(int i = 0; i < 8;i++)
			for(int j = 0; j < 8;j++)
				if(A[i][j] instanceof King && A[i][j].color == c)
					return i;
		
		return -1;
	}

	private int fileOfKing(Piece.Side c, Piece[][] A)
	{
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8;j++)
				if(A[i][j] instanceof King && A[i][j].color == c)
					return j;

		return -1;
	}

	public void pawnPromotion()
	{
		for(int i = 0;  i < 8;i++)
		{
			if(board[7][i] instanceof Pawn)
				board[7][i] = new Queen(7, i, board[7][i].color);
			if(board[0][i] instanceof Pawn)
				board[0][i] = new Queen(0, i, board[0][i].color);
		}		
	}

	public int advantage()
	{
		return -1;
	}
	
	public boolean checkMate()
	{
		return false;
	}

}
