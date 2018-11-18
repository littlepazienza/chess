import java.util.ArrayList;

public class GameBoard
{

	protected Piece[][] board = new Piece[8][8];
	protected ArrayList<Piece> white, black;
	
	public GameBoard()
	{
		board = new Piece[8][8];
		white = new ArrayList<>();
		black = new ArrayList<>();

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

	}

	public boolean makeMove(int fromR, int fromF, int toR, int toF)
	{
		if(board[fromR][fromF] == null)
			return false;

		if(!board[fromR][fromF].validMove(toR, toF))
			return false;
		
		if(kingInCheck(fromR, fromF, toR, toF))
			return false;
		
		return true;		
	}

	private boolean kingInCheck(int fromR, int fromF, int toR, int toF)
	{
		return false;	
	}
	

}
