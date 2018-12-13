package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ScrollPaneUI;
import javax.swing.text.Document;

import org.apache.commons.io.FileUtils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import Player.Game;
import Player.LiveGame;
import Player.Player;
import sun.applet.Main;
import sun.management.counter.perf.PerfLongArrayCounter;

public class Menu extends JFrame implements ActionListener{

	protected final String HOSTNAME = "known_hosts";
	protected final String GAMEDATA = "gamedata";
	protected final static String PLAYERDATA = "playerdata";
	JTextField p1, p2;
	static ArrayList<Player> playerList;
	static ArrayList<LiveGame> gameList;
	protected Player currentPlayer;
	protected JPanel mainPnl;
	protected JPanel rankPnl;
	protected JPanel newsPnl;
	protected JPanel leadPnl;
	protected JPanel playPnl;
	protected JPanel srcBar;
	protected static ChannelSftp channel;
	
	protected MenuButton rankPnlButton, playPnlButton, newsPnlButton, leadPnlButton;
	
	public Menu() throws IOException, JSchException, SftpException
	{
		super();
		
		this.addWindowListener(new WindowAdapter()
			{
	            public void windowClosing(WindowEvent e)
	            {
	               try {
					writeFile();
					writeGames();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (JSchException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SftpException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            }
	        });
		   
		initializeChannel();
		playerList = new ArrayList<Player>();
		gameList = new ArrayList<LiveGame>();
		readFile();
		
		currentPlayer = login();
		
		readGame();
		
		Collections.sort(playerList);
		Collections.reverse(playerList);
				
		setVisible(true);
		setSize(600, 700);
		setLocationRelativeTo(null);
		
		rankPnl = new JPanel();
		newsPnl = new JPanel();
		leadPnl = new JPanel();
		playPnl = new JPanel();
		srcBar = new JPanel();
		
		
		/**
		 * SRC BAR
		 */
		
		srcBar.setLayout(null);
		srcBar.setVisible(true);
		srcBar.setSize(getWidth(), 50);
		
		playPnlButton = new MenuButton("Play", MenuButton.PLAY);
		playPnlButton.currentlySelected = true;
		playPnlButton.setBounds(0, 0, srcBar.getWidth() / 4, srcBar.getHeight());
		playPnlButton.addActionListener(this);
		srcBar.add(playPnlButton);
		
		rankPnlButton = new MenuButton("Stats", MenuButton.RANK);
		rankPnlButton.setBounds(getWidth()/4, 0, srcBar.getWidth() / 4, srcBar.getHeight());
		rankPnlButton.addActionListener(this);
		srcBar.add(rankPnlButton);

		
		leadPnlButton = new MenuButton("Leaderboard", MenuButton.LEADERBOARD);
		leadPnlButton.setBounds(2*getWidth()/4, 0, srcBar.getWidth() / 4, srcBar.getHeight());
		leadPnlButton.addActionListener(this);
		srcBar.add(leadPnlButton);

		
		newsPnlButton = new MenuButton("News", MenuButton.NEWS);
		newsPnlButton.setBounds(3*getWidth()/4, 0, srcBar.getWidth() / 4, srcBar.getHeight());
		newsPnlButton.addActionListener(this);
		srcBar.add(newsPnlButton);
		
		srcBar.setBackground(new Color(135, 67, 67));		

		/**
		 * SRC BAR DONE
		 */
		/**
		 * PLAY PANEL
		 */
		
		playPnl.setLayout(null);
		
		JButton play = new JButton("2 Player (Ranked)");
		play.setBounds(75, 100, 125, 50);
		play.setBorder(BorderFactory.createSoftBevelBorder(1));
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Player plr2 = login();
				try {
					PlayLocal p = new PlayLocal(currentPlayer, plr2);
					p.setVisible(true);
					p.setSize(1500, 1000);
					p.setLocationRelativeTo(null);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (JSchException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SftpException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}
		});
		play.setOpaque(false);
		play.setContentAreaFilled(false);
		
		JButton playG = new JButton("2 Player");
		playG.setBounds(225, 100, 125, 50);
		playG.setBorder(BorderFactory.createSoftBevelBorder(1));
		playG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Player plr2 = new Player("Guest", "", true);
				try {
					PlayLocal p;
					try {
						p = new PlayLocal(currentPlayer, plr2);
						p.setVisible(true);
						p.setSize(1500, 1000);
						p.setLocationRelativeTo(null);
					} catch (JSchException | SftpException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				dispose();
			}
		});
		playG.setOpaque(false);
		playG.setContentAreaFilled(false);
		
		JButton playR = new JButton("Play with Friend");
		playR.setBounds(325, 100, 125, 50);
		playR.setBorder(BorderFactory.createSoftBevelBorder(1));
		playR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String plr = JOptionPane.showInputDialog("Who would you like to challenge?");
				Player plr2 =null;
				for(Player tempPlayer:playerList)
				{
					if(tempPlayer.name.equals(plr))
					{
						plr2 = tempPlayer;
						break;
					}
				}
				
				if(plr2 == null)
				{
					JOptionPane.showMessageDialog(null, "Player does not exist");
					
				}
				else
				{
					gameList.add(new LiveGame(currentPlayer, plr2, 0, plr2.name, "REQ", currentPlayer));
				}
			}
		});
		playR.setOpaque(false);
		playR.setContentAreaFilled(false);
		
		JPanel gamesPnl = new JPanel();
		gamesPnl.setBackground(new Color(135, 67, 67));
		
		int i=0;
		for(LiveGame g:gameList)
		{
			if(g.black.equals(currentPlayer) || g.white.equals(currentPlayer))
			{
				JButton b = new JButton("vs " + g.getOpponent(currentPlayer.name).name);
				b.setOpaque(true);
				b.setBackground(Color.GRAY);
				b.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							PlayRemote p = new PlayRemote(currentPlayer, g);
							p.setSize(1500, 1000);
							p.setVisible(true);
							p.setLocationRelativeTo(null);
						} catch (IOException | JSchException | SftpException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				b.setBorder(BorderFactory.createBevelBorder(1));
				i++;
				gamesPnl.add(b, i, 0);
			}
		}
		gamesPnl.setLayout(new GridLayout(0, 1, 30, 30));
	    gamesPnl.setBorder(LineBorder.createBlackLineBorder());
	    gamesPnl.setPreferredSize(new Dimension(200, i * 200));
		JScrollPane gamesPane = new JScrollPane(gamesPnl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		if(i > 1)
			gamesPane.setBounds(190, 200, 200, 400);
		else
			gamesPane.setBounds(190, 200, 200, 220);

		UIManager.getLookAndFeelDefaults().put( "ScrollBar.thumb", Color.blue );	
		
		playPnl.add(play);
		playPnl.add(playG);
		playPnl.add(playR);
		playPnl.setBackground(new Color(135, 67, 67));	
		playPnl.add(gamesPane);
		
		/**
		 *  PLAY PANEL DONE
		 */
		
		/**
		 * LEADERBOARD PANEL
		 */
		
		leadPnl.setLayout(null);
		
		JLabel leadLbl = new JLabel("Top 10 Players!");
		leadLbl.setBounds(200, 50, 400, 50);
		leadLbl.setFont(new Font(leadLbl.getName(), 0, 24));
		leadLbl.setForeground(Color.WHITE);
		leadPnl.add(leadLbl);
		
		JLabel ratingLbl = new JLabel("Rating");
		ratingLbl.setBounds(125, 120, 50, 30);
		ratingLbl.setForeground(Color.WHITE);
		leadPnl.add(ratingLbl);
		
		JLabel nameLbl = new JLabel("Username");
		nameLbl.setBounds(175, 120, 200, 30);
		nameLbl.setForeground(Color.WHITE);
		leadPnl.add(nameLbl);
		
		JLabel resultLblW = new JLabel("W");
		resultLblW.setBounds(385, 120, 15, 30);
		resultLblW.setForeground(Color.GREEN);
		leadPnl.add(resultLblW);
		
		JLabel resultLblSlash = new JLabel("/");
		resultLblSlash.setBounds(400, 120, 15, 30);
		resultLblSlash.setForeground(Color.WHITE);
		leadPnl.add(resultLblSlash);
		
		JLabel resultLblL = new JLabel("L");
		resultLblL.setBounds(406, 120, 15, 30);
		resultLblL.setForeground(Color.RED);
		leadPnl.add(resultLblL);
		
		JLabel resultLblSlash2 = new JLabel("/");
		resultLblSlash2.setBounds(415, 120, 15, 30);
		resultLblSlash2.setForeground(Color.WHITE);
		leadPnl.add(resultLblSlash2);
		
		JLabel resultLabelD = new JLabel("D");
		resultLabelD.setBounds(421, 120, 15, 30);
		resultLabelD.setForeground(Color.GRAY);
		leadPnl.add(resultLabelD);

		JTextArea names = new JTextArea(getLeaderBoardNames());
		names.setBounds(175, 150, 200, 200);
		names.setBorder(BorderFactory.createBevelBorder(1));
		names.setFont(new Font(names.getName(), 3, 18));
		names.setForeground(Color.WHITE);
		names.setBackground(new Color(135, 67, 67));	
		names.setEditable(false);
		leadPnl.add(names);
		
		JTextArea ratings = new JTextArea(getLeaderBoardRanks());
		ratings.setBounds(125, 150, 50, 200);
		ratings.setBorder(BorderFactory.createBevelBorder(1));
		ratings.setFont(new Font(ratings.getName(), 3, 18));
		ratings.setForeground(Color.WHITE);
		ratings.setBackground(new Color(135, 67, 67));	
		ratings.setEditable(false);
		leadPnl.add(ratings);
		
		JTextArea record = new JTextArea(getLeaderBoardRecords());
		record.setBounds(375, 150, 75, 200);
		record.setBorder(BorderFactory.createBevelBorder(1));
		record.setFont(new Font(record.getName(), 3, 18));
		record.setForeground(Color.WHITE);
		record.setBackground(new Color(135, 67, 67));	
		record.setEditable(false);
		leadPnl.add(record);
		
		leadPnl.setBackground(new Color(135, 67, 67));	

		
		/**
		 *  DONE LEADERBOARD PANEL
		 */
		
		/**
		 *  STATS PANEL
		 */
		
		rankPnl.setLayout(null);
		
		JLabel rankNmLbl = new JLabel(currentPlayer.name);
		rankNmLbl.setBounds(50, 50, 200, 100);
		rankNmLbl.setFont(new Font(rankNmLbl.getName(), 0, 40));
		rankNmLbl.setForeground(Color.gray);
		rankPnl.add(rankNmLbl);
		
		JLabel currentPlayerRankLabel = new JLabel("" + currentPlayer.rating());
		currentPlayerRankLabel.setBounds(200, 150, 200, 100);
		currentPlayerRankLabel.setFont(new Font(currentPlayerRankLabel.getName(), 2, 64));
		currentPlayerRankLabel.setForeground(Color.WHITE);
		rankPnl.add(currentPlayerRankLabel);
		
		JLabel last5lbl = new JLabel("Last 5 Games");
		last5lbl.setBounds(220, 250, 400, 30);
		last5lbl.setFont(new Font(last5lbl.getName(), 3, 20));
		last5lbl.setBackground(new Color(135, 67, 67));
		last5lbl.setForeground(Color.WHITE);
		rankPnl.add(last5lbl);
		
		String[] args = currentPlayer.last5Games();
		JTextArea last5R = new JTextArea(args[0]);
		last5R.setBorder(BorderFactory.createBevelBorder(1));
		last5R.setEditable(true);
		last5R.setBounds(130, 300, 50, 200);
		last5R.setBackground(new Color(135, 67, 67));
		last5R.setForeground(Color.WHITE);
		last5R.setFont(new Font(last5R.getName(), 0, 16));
		rankPnl.add(last5R);
		
		JTextArea last5N = new JTextArea(args[1]);
		last5N.setBorder(BorderFactory.createBevelBorder(1));
		last5N.setEditable(true);
		last5N.setBounds(180, 300, 150, 200);
		last5N.setBackground(new Color(135, 67, 67));
		last5N.setForeground(Color.WHITE);
		last5N.setFont(new Font(last5N.getName(), 0, 16));
		rankPnl.add(last5N);
		rankPnl.setBackground(new Color(135, 67, 67));
		
		JTextArea last5W = new JTextArea(args[2]);
		last5W.setBorder(BorderFactory.createBevelBorder(1));
		last5W.setEditable(true);
		last5W.setBounds(330, 300, 100, 200);
		last5W.setBackground(new Color(135, 67, 67));
		last5W.setForeground(Color.WHITE);
		last5W.setFont(new Font(last5W.getName(), 0, 16));
		rankPnl.add(last5W);
		rankPnl.setBackground(new Color(135, 67, 67));
		
		/**
		 * STATS PANEL DONE
		 */
		
		setInvisible();
		getContentPane().setBackground(new Color(135, 67, 67));		
		getContentPane().add(leadPnl);
		getContentPane().add(rankPnl);
		getContentPane().add(srcBar);
		getContentPane().add(playPnl);

		leadPnl.setBounds(0, 50, 600, 600);
		playPnl.setBounds(0, 50, 600, 600);
		rankPnl.setBounds(0, 50, 600, 600);
		srcBar.setBounds(0, 0, 600, 50);
	}
	
	public void initializeChannel() throws JSchException
	{
		String user = "chess";
		String host = "66.175.216.86";
		String password = "chessisfun";
		
		JSch jsch = new JSch();
		jsch.setKnownHosts(HOSTNAME);
		Session session = jsch.getSession(user, host);
		session.setPassword(password);
		session.connect();

		channel = (ChannelSftp) session.openChannel("sftp");
		channel.connect();	
	}
	
	private Player login()
	{
		JTextField username = new JTextField();
		JTextField password = new JPasswordField();
		
		Object[] userMsg = {
		    "Username:", username,
		};

		int option = JOptionPane.showConfirmDialog(null, userMsg, "Login", JOptionPane.OK_CANCEL_OPTION);
		
		boolean valid = true;
		for (char c : username.getText().toCharArray()) {
		    if (Character.isWhitespace(c)) {
		        valid = false;
		     }
		}
		    
		if(!valid)
		{
			JOptionPane.showMessageDialog(this, "Invalid Username");
			System.exit(0);
		}
		    
		Player p = matchPlayer(username.getText());
	
		if (option == JOptionPane.OK_OPTION) {
			if(p.password == null)
			{
					Object[] pswrdMsg = {
					    "Create a password:", password,
					};
				int option2 = JOptionPane.showConfirmDialog(null, pswrdMsg, "New User", JOptionPane.OK_CANCEL_OPTION);
				
				if(option2 == JOptionPane.OK_OPTION)
				{
					p.password = password.getText();
					return p;
				}
				else
				{
					System.exit(0);
				}
			}
			
			Object[] pswrdMsg = {
				    "Password:", password,
				};
			
			int option2 = JOptionPane.showConfirmDialog(null, pswrdMsg, "Login", JOptionPane.OK_CANCEL_OPTION);

			if(option2 == JOptionPane.OK_OPTION)
			{
				if(!password.getText().equals(p.password))
				{
					JOptionPane.showMessageDialog(this, "Login Failed");
					System.exit(0);
				}
				p.password = password.getText();
				return p;
			}
			else
			{
				System.exit(0);
			}
			
		}
		else 
		{
			JOptionPane.showMessageDialog(this, "Login Cancelled");
			System.exit(0);
		}
		
		return null;
	}

	private static Icon resizeIcon(Icon icon, int resizedWidth, int resizedHeight) {
	    Image img = ((ImageIcon) icon).getImage();  
	    Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
	    return new ImageIcon(resizedImage);
	}
	
	public void readFile() throws IOException, JSchException, SftpException
	{
		channel.get("/home/chess/playerdata", PLAYERDATA);
		
		Scanner scan = new Scanner(new File(PLAYERDATA));
		
		while(scan.hasNextLine())
			
		{
			String nmAndp = scan.nextLine();
			String[] nmAndps = nmAndp.split(";");
			Player p = new Player(nmAndps[0], nmAndps[1], false);
			String gm = scan.nextLine();
			while(!gm.equals("--"))
			{
				String[] theGm = gm.split(";");
				p.add(new Game(Integer.parseInt(theGm[1]), theGm[2].charAt(0), theGm[0]));
				gm = scan.nextLine();
			}
			playerList.add(p);
		}
		scan.close();
	}
	
	public void readGame() throws JSchException, SftpException, IOException
	{
		channel.get("/home/chess/gamedata", GAMEDATA);
				
		Scanner scan = new Scanner(new File(GAMEDATA));
		
		while(scan.hasNextLine())
		{
			String[] names = scan.nextLine().split(";");
			String nxt = scan.nextLine();
			if(nxt.equals("REQ"))
			{
				if(names[1].equals(currentPlayer.name))
				{
					String[] opt = {"accept", "decline"};
					if(JOptionPane.showOptionDialog(this, "New game request from " + names[0], "Game Request", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opt, opt[0]) == 0)
					{
						LiveGame g = new LiveGame(matchPlayer(names[0]), currentPlayer, 0, currentPlayer.name, "ACPT", currentPlayer);
						gameList.add(g);
					}
				}
				else
				{
					LiveGame g = new LiveGame(matchPlayer(names[0]), matchPlayer(names[1]), 0, names[1], "REQ", currentPlayer);
				}
				scan.nextLine();
			}
			else
			{
				int turnNum = Integer.parseInt(scan.nextLine());
				String turn = scan.nextLine();
				LiveGame g = new LiveGame(matchPlayer(names[0]), matchPlayer(names[1]), turnNum, turn, "ACPT", currentPlayer);
				Scanner line = new Scanner(scan.nextLine());
				line.useDelimiter(",");
				while(line.hasNext())
				{
					g.addMove(line.next());
				}
				line.close();
				scan.nextLine();
				gameList.add(g);
			}
		}
		scan.close();
	}
	
	public LiveGame matchGame(Player p, Player q)
	{
		for(LiveGame g:gameList)
		{
			if(g.black.name.equals(p.name) && g.white.name.equals(q.name))
				return g;
			else if(g.black.name.equals(q.name) && g.white.name.equals(p.name))
				return g;
		}
		return null;
	}
	
	public static void writeFile() throws IOException, JSchException, SftpException
	{
		BufferedWriter buffy = new BufferedWriter(new FileWriter(new File(PLAYERDATA)));
		buffy.flush();
		for(Player p:playerList)
		{
			buffy.write(p.print());
		}
		buffy.close();

		channel.put(PLAYERDATA, "/home/chess/playerdata");
	}
	
	public void writeGames() throws JSchException, SftpException, IOException
	{
		BufferedWriter buffy = new BufferedWriter(new FileWriter(new File(GAMEDATA)));

		buffy.flush();
		for(LiveGame g:gameList)
		{
			buffy.write(g.printGame());
		}
		buffy.close();
		channel.put(GAMEDATA, "/home/chess/gamedata");
	}
	
	public Player matchPlayer(String str)
	{
		for(Player p:playerList)
		{
			if(p.name.equals(str))
			{
				return p;
			}
		}
		Player newPlr = new Player(str);
		playerList.add(newPlr);
		return newPlr;
	}

	public void actionPerformed(ActionEvent arg0) {
		MenuButton b = (MenuButton) arg0.getSource();
		
		if(b.decision == MenuButton.RANK)
		{				
			deselectAll();
			setInvisible();
			if(!b.currentlySelected)
			{	
				rankPnl.setVisible(true);
				rankPnlButton.currentlySelected = true;
			}
		}
		else if(b.decision == MenuButton.PLAY)
		{
			deselectAll();
			setInvisible();
			if(!b.currentlySelected)
			{	
				playPnl.setVisible(true);
				playPnlButton.currentlySelected = true;
			}
		}	
		else if(b.decision == MenuButton.LEADERBOARD)
		{
			deselectAll();
			setInvisible();
			if(!b.currentlySelected)
			{	
				leadPnl.setVisible(true);
				leadPnlButton.currentlySelected = true;
			}
		}	
		else if(b.decision == MenuButton.NEWS)
		{
			deselectAll();
			setInvisible();
			if(!b.currentlySelected)
			{	
				newsPnl.setVisible(true);
				newsPnlButton.currentlySelected = true;
			}
		}	
		repaint();
	}
	
	private void setInvisible()
	{
		playPnl.setVisible(false);
		newsPnl.setVisible(false);
		rankPnl.setVisible(false);
		leadPnl.setVisible(false);

	}
	
	private void deselectAll()
	{
		leadPnlButton.currentlySelected = false;
		playPnlButton.currentlySelected = false;
		rankPnlButton.currentlySelected = false;
		newsPnlButton.currentlySelected = false;
	}
	
	public String getLeaderBoardNames()
	{	
		String O = "";
		for(int i = 0; i < 10 && i < playerList.size();i++)
		{
			O+=playerList.get(i).name + "\n";
		}
		return O;
		
	}
	
	public String getLeaderBoardRanks()
	{	
		String O = "";
		for(int i = 0; i < 10 && i < playerList.size();i++)
		{
			O+=playerList.get(i).rating() + "\n";
		}
		return O;
		
	}
	
	public String getLeaderBoardRecords()
	{	
		String O = "";
		for(int i = 0; i < 10 && i < playerList.size();i++)
		{
			O+=playerList.get(i).totalWins() + "/" + playerList.get(i).totalLosses() + "/" + playerList.get(i).totalDraws() + "\n";
		}
		return O;
		
	}
	
	class MenuButton extends JButton
	{
		protected boolean currentlySelected;
		protected int decision;
		protected static final int RANK = 0;
		protected static final int PLAY = 1;
		protected static final int LEADERBOARD = 2;
		protected static final int NEWS = 3;
		
		public MenuButton(String s, int d)
		{
			super(s);
			decision=d;
			currentlySelected = false;
		}
		
		public MenuButton(Icon i, int d)
		{
			super(i);
			decision=d;
			currentlySelected = false;
		}
	}
		
}
	