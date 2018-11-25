import java.util.ArrayList;

public class Player {

	protected String name;
	protected ArrayList<Game> games;
	
	public Player(String nm)
	{
		games = new ArrayList<Game>();
		name = nm;
	}
	
	public void add(Game g)
	{
		games.add(g);
	}
	
	public int rating()
	{
		if(games.size() == 0)
			return 1200;
		return (opponentRatingSum() + 400 * (totalWins() -totalLosses()))/games.size();
	}
	
	public int ratingOneGameBack()
	{
		if(games.size() == 1)
			return 1200;
		return (opponentRatingSumOneGameBack() + 400 * (totalWinsOneGameBack() - totalLossesOneGameBack())) /(games.size()-1);
	}
	
	private int opponentRatingSum()
	{
		int sum=0;
		for(Game g:games)
		{
			sum += g.opponentRating;
		}
		return sum;
	}
	
	private int opponentRatingSumOneGameBack()
	{
		int sum=0;
		for(int i = 0; i < games.size()-1;i++)
		{
			sum+= games.get(i).opponentRating;
		}
		return sum;
	}
	
	private int totalWins()
	{
		int count=0;
		for(Game g:games)
		{
			if(g.result == 'W')
				count++;
		}
		return count;
	}
	
	private int totalWinsOneGameBack()
	{
		int count = 0;
		for(int i = 0;i < games.size()-1;i++)
		{
			if(games.get(i).result == 'W')
				count++;
		}
		return count;
	}
	
	private int totalLosses()
	{
		int count=0;
		for(Game g:games)
		{
			if(g.result == 'L')
				count++;
		}
		return count;
	}
	
	private int totalLossesOneGameBack()
	{
		int count = 0;
		for(int i =0;i<games.size()-1;i++)
		{
			if(games.get(i).result == 'L')
				count++;
		}
		return count;
	}
	
	public String print()
	{
		String O = name + "\n";
		for(Game g:games)
		{
			O += g.print() + "\n";
		}
		return O + "--\n";
	}
}
