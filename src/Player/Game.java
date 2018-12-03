package Player;
public class Game 
{
	protected int opponentRating;
	protected String opponentName;
	protected char result;
	
	public Game(int o, char c, String nm)
	{
		opponentRating = o;
		opponentName = nm;
		result = c;
	}
		
	public String print()
	{
		return opponentName + ";" + opponentRating + ";" + result;
	}
}