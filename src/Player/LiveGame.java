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

	public boolean whiteTurn;
	public int turnNumber;
	protected ArrayList<String> movelist;
	public String plr1, plr2;
	
	public LiveGame(String plr1, String plr2, int turn, boolean whiteTurn)
	{
		movelist = new ArrayList<>();
		this.whiteTurn = whiteTurn;
		this.turnNumber = turn;
		this.plr1 = plr1;
		this.plr2 = plr2;
	}
	
	public void addMove(String mv)
	{
		movelist.add(mv);
	}
	
	public GameBoard toGameBoard()
	{
		GameBoard g = new GameBoard();
		for(String s:movelist)
		{
			if(s.charAt(0) == 'E')
			{
				g.board[(int)s.charAt(1)][(int)s.charAt(2)] = null;
			}
			
			Piece.Side c;
			if(s.charAt(0) == 'w')
			{
				c = Piece.Side.WHITE;
			}
			else
			{
				c = Piece.Side.BLACK;
			}
			
			Piece p = null;
			if(s.charAt(1) == 'p')
			{
				p = new Pawn((int)s.charAt(2), (int)s.charAt(3), c);
			}
			if(s.charAt(1) == 'k')
			{
				p = new King((int)s.charAt(2), (int)s.charAt(3), c);
			}
			if(s.charAt(1) == 'n')
			{
				p = new Knight((int)s.charAt(2), (int)s.charAt(3), c);
			}
			if(s.charAt(1) == 'r')
			{
				p = new Rook((int)s.charAt(2), (int)s.charAt(3), c);
			}
			if(s.charAt(1) == 'b')
			{
				p = new Bishop((int)s.charAt(2), (int)s.charAt(3), c);
			}
			if(s.charAt(1) == 'q')
			{
				p = new Queen((int)s.charAt(2), (int)s.charAt(3), c);
			}
			
			g.board[Integer.parseInt(""+s.charAt(2))][Integer.parseInt(""+s.charAt(3))] = p;
		}
		
		return g;
	}
}
