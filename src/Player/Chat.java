package Player;

import java.util.List;
import java.awt.AWTException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jcraft.jsch.SftpException;

import GUI.Menu;

public class Chat {
	
	
	public ArrayList<Message> chatlist;
	protected Player[] members;
	
	public Chat(Player p1, Player p2)
	{
		chatlist = new ArrayList<Message>();
		members = new Player[]{p1, p2};
	}
	
	public void addMessage(String txt, Player from, String time, boolean notified)
	{
		chatlist.add(new Message(from, txt, time, notified));
		Collections.sort(chatlist);
	}
	
	public void addMessage(Message m)
	{
		chatlist.add(m);
	}
	
	public void addToPanel(JPanel p, Player current)
	{
		p.setSize(300, chatlist.size() * 50);
		
		int i = 0;
		int size = 0;
		for(int c = 0; i < chatlist.size();i++)
		{
			Message m = chatlist.get(c);
			JLabel l = new JLabel();
			if(m.sender.equals(current))
			{
				l.setText(m.printRight());
				l.setHorizontalAlignment(SwingConstants.RIGHT);
			}
			else
			{
				l.setText(m.printLeft());
				l.setHorizontalAlignment(SwingConstants.LEFT);
			}
			l.setBounds(0, size, 350, (int) (i* l.getPreferredSize().getHeight() > 100 ? 100 : l.getPreferredSize().getHeight()));
			p.add(l);
			i++;
			size+=l.getPreferredSize().getHeight();
		}
	}
	
	public String print()
	{
		String O ="";
		O+= members[0].name + ";" + members[1].name + "\n";
		for(Message m :chatlist)
		{
			O+= m.write() + "\n";
		}
		return O + "--\n";
	}
	
	@Override
	public boolean equals(Object o)
	{
		return (members[0].equals(((Chat) o).members[0]) || members[0].equals(((Chat) o).members[1])) && 
				members[1].equals(((Chat) o).members[0]) || members[1].equals(((Chat) o).members[1]);
			
	}
	
	public String getOther(Player p)
	{
		return p.name.equals(members[0].name) ? members[1].name : members[0].name;
	}
	
	public boolean includesPlayer(Player p)
	{
		return p.name.equals(members[0].name) || p.name.equals(members[1].name);
	}
	
	public Chat mostUpdated(Chat c)
	{
		if(chatlist.size() < c.chatlist.size())
			return c;
		else if(chatlist.size() > c.chatlist.size())
			return this;
		else
			return collideChats(this.chatlist, c.chatlist);
	}
	
	public Chat collideChats(ArrayList<Message> one, ArrayList<Message> two)
	{
		Chat c = new Chat(this.members[0], this.members[1]);
		
		for(Message m : one)
			for(Message n: two)
				if(m.equals(n))
					c.addMessage(n);
		
		one.removeAll(c.chatlist);
		two.removeAll(c.chatlist);
		
		for(Message m:one)
			c.addMessage(m);
		
		for(Message n:two)
			c.addMessage(n);
		
		return c;
	}
	
	public void notify(Menu m, Player crnt) throws AWTException, SftpException, IOException
	{
		for(Message msg : chatlist)
		{
			if(!msg.sender.equals(crnt) && !msg.notified)
			{
				m.displayTray("NEW MESSAGE from " + msg.sender.name);
				msg.notified = true;
			}
		}
		m.writeChats();
	}
	
	class Message implements Comparable
	{
		protected Player sender;
		protected String message;
		protected Date time;
		protected boolean notified;
		
		public Message(Player s, String m, String t, boolean n)
		{
			sender = s;
			message = m;
			notified = n;
			try {
				time = toDate(t);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		private Date toDate(String s) throws ParseException
		{
			return new SimpleDateFormat("yyyyMMdd_HHmmss").parse(s);
		}
		
		public int compareTo(Object m) {
			return this.time.compareTo(((Message)m).time);
		}
		
		@Override
		public boolean equals(Object o)
		{
			return this.message.equals(((Message) o).message) && this.time.equals(((Message) o).time);
		}
		
		public String printRight()
		{
			return message + " - " + time.toString();
		}
		
		public String printLeft()
		{
			return time.toString() + " - " + message;
		}
		
		public String write()
		{
			return sender.name + ";" + new SimpleDateFormat("yyyMMdd_HHmmss").format(time) + ";" + message + ";" + notified;
		}
	}

}
