package Player;
import java.util.ArrayList;

public class Player implements Comparable{

	public String name, password;
	protected ArrayList<Game> games;
	public boolean guest;
	
	public Player(String nm, String pswrd, boolean g)
	{
		games = new ArrayList<Game>();
		name = nm;
		password = pswrd;
		guest = g;
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
		return (opponentRatingSum() + 400 * (totalWins() -totalLosses()))/(games.size() - totalDraws());
	}
	
	public int opponentRatingSum()
	{
		int sum=0;
		for(Game g:games)
		{
			sum += (g.result != 'D'?g.opponentRating:0);
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

	public int totalDraws()
	{
		int count=0;
		for(Game g:games)
		{
			if(g.result == 'D')
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

	public String[] last5Games()
	{
		String[] args = {"", "", ""};
		for(int i = games.size() - 1; i >= 0 && i >= games.size() - 5;i--)
		{
			args[0]+=games.get(i).opponentRating + "\n";
			args[1]+=games.get(i).opponentName + "\n";
			args[2]+=(games.get(i).result == 'W'?"Win":games.get(i).result =='L'?"Loss":"Draw") + "\n"; 
		}
		return args;
	}
	
	@Override
	public int compareTo(Object arg0) {
		return this.rating() - ((Player) arg0).rating();
	}
	
	@Override
	public boolean equals(Object o)
	{
		return this.name.equals(((Player)o).name);
	}
}
