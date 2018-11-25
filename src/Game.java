public class Game 
{
	protected int opponentRating;
	protected char result;
	
	public Game(int o, char c)
	{
		opponentRating = o;
		result = c;
	}
		
	public String print()
	{
		return opponentRating + ";" + result;
	}
}