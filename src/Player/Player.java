package Player;
import java.util.ArrayList;

public class Player implements Comparable{

	public String name, password;
	protected ArrayList<Game> games;
	
	public Player(String nm, String pswrd)
	{
		games = new ArrayList<Game>();
		name = nm;
		password = pswrd;
	}
	
	public Player(String nm)
	{
		games = new ArrayList<Game>();
		name = nm;
		password = null;
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
	
	public int opponentRatingSum()
	{
		int sum=0;
		for(Game g:games)
		{
			sum += g.opponentRating;
		}
		return sum;
	}
	
	public int totalWins()
	{
		int count=0;
		for(Game g:games)
		{
			if(g.result == 'W')
				count++;
		}
		return count;
	}

	public int totalLosses()
	{
		int count=0;
		for(Game g:games)
		{
			if(g.result == 'L')
				count++;
		}
		return count;
	}
	
	public String print()
	{
		String O = name + ";" + password + "\n";
		for(Game g:games)
		{
			O += g.print() + "\n";
		}
		return O + "--\n";
	}

	public String last5Games()
	{
		String O = "";
		for(int i = games.size() - 1; i >= 0 && i > games.size() - 5;i--)
		{
			O+= games.get(i).opponentRating + "\t" + games.get(i).opponentName + "\t" + (games.get(i).result == 'W'?"Win":games.get(i).result =='L'?"Loss":"Draw"); 
		}
		return O;
	}
	
	@Override
	public int compareTo(Object arg0) {
		return this.rating() - ((Player) arg0).rating();
	}
}
