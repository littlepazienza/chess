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
	public String plr1, plr2;
	public String gameStatus;
	public Player currentPlayer;
	
	public LiveGame(String plr1, String plr2, int turn, String whoseTurn, String gameStatus, Player currentPlayer)
	{
		movelist = new ArrayList<>();
		this.currentTurn = whoseTurn;
		this.turnNumber = turn;
		this.plr1 = plr1;
		this.plr2 = plr2;
		this.gameStatus =gameStatus;
		this.currentPlayer = currentPlayer;
	}
	
	public void addMove(String mv)
	{
		movelist.add(mv);
	}
	
	public String getOpponent(String otherName)
	{
		if(plr1.equals(otherName))
			return plr2;
		else
			return plr1;
	}
	
	public GameBoard toGameBoard()
	{
		GameBoard g = new GameBoard();
		g.GameFill();
		
		for(String s:movelist)
		{
			g.makeMove(Integer.parseInt(""+s.charAt(0)), Integer.parseInt(""+s.charAt(1)), Integer.parseInt(""+s.charAt(2)), Integer.parseInt(""+s.charAt(3)));
		}
		
		return g;
	}
	
	public String printGame()
	{
		String O = "";
		O+=plr1 + ";" + plr2  + "\n";
		if(gameStatus.equals("REQ"))
			return O + gameStatus + "--\n";
		else
		{	
			O+=gameStatus + "\n";
			O+=turnNumber + "\n";
			O+=currentTurn + "\n";
			for(String s: movelist)
				O+= s + ",";
			return O.substring(0, O.length()) + "--\n";
		}
	}
}
