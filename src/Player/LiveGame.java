package Player;

import java.util.ArrayList;

import Game.Bishop;
import Game.GameBoard;
import Game.King;
import Game.Knight;
import Game.Pawn;
import Game.Piece;
import Game.Queen;
import Game.Rook;

public class LiveGame {

	public String currentTurn;
	public int turnNumber;
	protected ArrayList<String> movelist;
	public Player black, white;
	public String gameStatus;
	public Player currentPlayer;
	
	public LiveGame(Player black, Player white, int turn, String whoseTurn, String gameStatus, Player currentPlayer)
	{
		movelist = new ArrayList<>();
		this.currentTurn = whoseTurn;
		this.turnNumber = turn;
		this.black = black;
		this.white = white;
		this.gameStatus =gameStatus;
		this.currentPlayer = currentPlayer;
	}
	
	public void addMove(String mv)
	{
		movelist.add(mv);
	}
	
	public void passTurn()
	{
		if(currentTurn.equals(white.name))
			currentTurn = black.name;
		else
			currentTurn = white.name;
	}
	
	public Player getOpponent(String otherName)
	{
		if(black.name.equals(otherName))
			return white;
		else
			return black;
	}

	
	public GameBoard toGameBoard()
	{
		GameBoard g = new GameBoard();
		g.GameFill();
		
		for(String s:movelist)
		{
			g.board[Integer.parseInt(""+s.charAt(0))][Integer.parseInt(""+s.charAt(1))].setSelected();
			g.makeMove(Integer.parseInt(""+s.charAt(0)), Integer.parseInt(""+s.charAt(1)), Integer.parseInt(""+s.charAt(2)), Integer.parseInt(""+s.charAt(3)));
		}
		
		return g;
	}
	
	public String printGame()
	{
		String O = "";
		O+=black.name + ";" + white.name  + "\n";
		if(gameStatus.equals("REQ"))
			return O + gameStatus + "\n--\n";
		else
		{	
			O+=gameStatus + "\n";
			O+=turnNumber + "\n";
			O+=currentTurn + "\n";
			for(String s: movelist)
				O+= s + ",";
			return O.substring(0, O.length()) + "\n--\n";
		}
	}
}