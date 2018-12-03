package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Player.Game;
import Player.Player;
import sun.applet.Main;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Menu extends JFrame implements ActionListener{

	JButton play, exit;
	JTextField p1, p2;
	static ArrayList<Player> playerList;
	protected Player currentPlayer;
	protected JPanel mainPnl;
	protected JPanel rankPnl;
	protected JPanel newsPnl;
	protected JPanel leadPnl;
	protected JPanel playPnl;
	protected JPanel srcBar;
	
	protected MenuButton rankPnlButton, playPnlButton, newsPnlButton, leadPnlButton;
	
	public Menu() throws IOException
	{
		super();
		
		this.addWindowListener(new WindowAdapter()
			{
	            public void windowClosing(WindowEvent e)
	            {
	               try {
					writeFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	            }
	        });
		   
		playerList = new ArrayList<Player>();
		readFile();
		
		currentPlayer = login();
		
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
		
		
		JLabel chess = new JLabel(new ImageIcon(Main.class.getResource("/chess.png")));
		chess.setBounds(40, 100, 500, 250);
		chess.setIcon(resizeIcon(chess.getIcon(), chess.getWidth(), chess.getHeight()));
		playPnl.add(chess);
		
		JLabel by = new JLabel(new ImageIcon(Main.class.getResource("/by.png")));
		by.setBounds(200, 300, 150, 50);
		by.setIcon(resizeIcon(by.getIcon(), by.getWidth(), by.getHeight()));
		playPnl.add(by);
		
		play = new JButton("Play with Friend");
		play.setBounds(100, 500, 150, 100);
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Player plr2 = login();
				try {
					Play p = new Play(currentPlayer, plr2);
					p.setVisible(true);
					p.setSize(1500, 1000);
					p.setLocationRelativeTo(null);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				dispose();
			}
		});
		
		play.setOpaque(false);
		play.setContentAreaFilled(false);
		
		p1 = new JTextField(currentPlayer.name);
		p1.setEditable(false);
		p1.setBounds(100, 450, 150, 35);
		p1.setFont(new Font(p1.getName(), 0, 16));
		p1.setForeground(Color.WHITE);
		p1.setOpaque(false);
		
		p2 = new JTextField("Player 2");
		p2.setBounds(300, 450, 150, 35);
		p2.setFont(new Font(p2.getName(), 0, 16));
		p2.setForeground(Color.WHITE);
		p2.setSelectionStart(0);
		p2.setSelectionEnd(p1.getText().length());
		p2.setOpaque(false);

		
		playPnl.add(play);
		playPnl.add(p1);
		playPnl.add(p2);
		
		playPnl.setBackground(new Color(135, 67, 67));		
		
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
		nameLbl.setBounds(200, 120, 200, 30);
		nameLbl.setForeground(Color.WHITE);
		leadPnl.add(nameLbl);
		
		JLabel resultLblW = new JLabel("W");
		resultLblW.setBounds(400, 120, 15, 30);
		resultLblW.setForeground(Color.GREEN);
		leadPnl.add(resultLblW);
		
		JLabel resultLblSlash = new JLabel("/");
		resultLblSlash.setBounds(415, 120, 15, 30);
		resultLblSlash.setForeground(Color.WHITE);
		leadPnl.add(resultLblSlash);
		
		JLabel resultLblL = new JLabel("L");
		resultLblL.setBounds(421, 120, 15, 30);
		resultLblL.setForeground(Color.RED);
		leadPnl.add(resultLblL);
		
		JLabel resultLblSlash2 = new JLabel("/");
		resultLblSlash2.setBounds(430, 120, 15, 30);
		resultLblSlash2.setForeground(Color.WHITE);
		leadPnl.add(resultLblSlash2);
		
		JLabel resultLabelD = new JLabel("D");
		resultLabelD.setBounds(436, 120, 15, 30);
		resultLabelD.setForeground(Color.GRAY);
		leadPnl.add(resultLabelD);

		JTextArea names = new JTextArea(getLeaderBoardNames());
		names.setBounds(200, 150, 200, 200);
		names.setFont(new Font(names.getName(), 3, 18));
		names.setForeground(Color.WHITE);
		names.setBackground(new Color(135, 67, 67));	
		names.setEditable(false);
		leadPnl.add(names);
		
		JTextArea ratings = new JTextArea(getLeaderBoardRanks());
		ratings.setBounds(125, 150, 50, 200);
		ratings.setFont(new Font(ratings.getName(), 3, 18));
		ratings.setForeground(Color.WHITE);
		ratings.setBackground(new Color(135, 67, 67));	
		ratings.setEditable(false);
		leadPnl.add(ratings);
		
		JTextArea record = new JTextArea(getLeaderBoardRecords());
		record.setBounds(400, 150, 200, 200);
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
		
		JLabel currentPlayerRankLabel = new JLabel("" + currentPlayer.rating());
		currentPlayerRankLabel.setBounds(200, 50, 200, 100);
		currentPlayerRankLabel.setFont(new Font(currentPlayerRankLabel.getName(), 2, 64));
		currentPlayerRankLabel.setForeground(Color.WHITE);
		rankPnl.add(currentPlayerRankLabel);
		
		JLabel last5lbl = new JLabel("Last 5 Games");
		last5lbl.setBounds(200, 270, 400, 30);
		last5lbl.setFont(new Font(last5lbl.getName(), 3, 20));
		last5lbl.setBackground(new Color(135, 67, 67));
		last5lbl.setForeground(Color.WHITE);
		rankPnl.add(last5lbl);
		
		JTextArea last5 = new JTextArea(currentPlayer.last5Games());
		last5.setEditable(true);
		last5.setBounds(150, 330, 400, 300);
		last5.setBackground(new Color(135, 67, 67));
		last5.setForeground(Color.WHITE);
		last5.setFont(new Font(last5.getName(), 0, 16));
		rankPnl.add(last5);
		
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
	
	private Player login()
	{
		JTextField username = new JTextField();
		JTextField password = new JPasswordField();
		
		Object[] userMsg = {
		    "Username:", username,
		};

		int option = JOptionPane.showConfirmDialog(null, userMsg, "Login", JOptionPane.OK_CANCEL_OPTION);
		
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
	
	public void readFile() throws IOException
	{
		String source = "\\\\10.233.148.78\\C$\\Users\\benpa\\Desktop\\Loyola\\fall18\\CS\\workspace\\play\\game\\data";
		String target = "data";

		try 
		{
		Files.copy(Paths.get(source),Paths.get(target),
		StandardCopyOption.REPLACE_EXISTING);
		}
		catch(Exception e) {}
		
		Scanner scan = new Scanner(new File("data"));
		while(scan.hasNextLine())
			
		{
			String[] nmAndps = scan.nextLine().split(";");
			Player p = new Player(nmAndps[0], nmAndps[1]);
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
	
	public static void writeFile() throws IOException
	{
		BufferedWriter buffy = new BufferedWriter(new FileWriter(new File("data")));
		buffy.flush();
		for(Player p:playerList)
		{
			buffy.write(p.print());
		}
		buffy.close();
		
		String source = "\\\\10.233.148.78\\C$\\Users\\benpa\\Desktop\\Loyola\\fall18\\CS\\workspace\\play\\game\\data";
		String target = "data";

		try 
		{
		Files.copy(Paths.get(target),Paths.get(source),
		StandardCopyOption.REPLACE_EXISTING);
		}
		catch(Exception e) {}		
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
			O+=playerList.get(i).totalWins() + "/" + playerList.get(i).totalLosses() + "/" + "0\n";
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
	