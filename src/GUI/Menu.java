package GUI;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

import Player.Chat;
import Player.Game;
import Player.LiveGame;
import Player.Player;
import sun.applet.Main;
import sun.management.counter.perf.PerfLongArrayCounter;

public class Menu extends JFrame {

	protected final static String HOSTNAME = "known_hosts";
	protected final String GAMEDATA = "gamedata";
	protected final String GAMEREQ = "gamerequests";
	public final static String ERRORREP = "errorlog";
	protected final static String PLAYERDATA = "playerdata";
	protected final String CHATDATA = "chat";
	JTextField p1, p2;
	static ArrayList<Player> playerList;
	static ArrayList<LiveGame> gameList;
	static ArrayList<Chat> chatlist;
	protected Player currentPlayer;
	protected JPanel mainPnl;
	protected JPanel rankPnl;
	protected JPanel chatPnl;
	protected JPanel leadPnl;
	protected JPanel playPnl;
	protected JPanel srcBar;
	protected static ChannelSftp channel;
	protected Menu m = this;
	protected boolean inUse;
	protected Chat currentChat = null;

	protected MenuButton rankPnlButton, playPnlButton, chatPnlButton, leadPnlButton;

	public Menu() throws IOException, JSchException, SftpException, InterruptedException, ExecutionException {

		playerList = new ArrayList<Player>();
		gameList = new ArrayList<LiveGame>();
		chatlist = new ArrayList<Chat>();

		this.setTitle("Chess");
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				try {
					writeFile();
					writeGames();
				} catch (Exception e1) {
					catchHandle(
							e1.getMessage() + " writing games while closing menu by " + currentPlayer.name + " @ "
									+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()),
							m);
				}
			}
		});

		channel = initializeChannel();

		playerList = readFile();

		currentPlayer = login();

		gameList = readGame(channel);

		readGameRequests();

		Collections.sort(playerList);
		Collections.reverse(playerList);

		final ExecutorService executorService = Executors.newFixedThreadPool(16);
		final Future<String> res1 = executorService.submit(new Notification());
		final Future<String> res2 = executorService.submit(new GameTime());
		String obj1 = res1.get();
		String obj2 = res2.get();
	}

	class Notification implements Callable<String> {

		@Override
		public String call() throws Exception {
			boolean keepGoing = true;
			while (keepGoing) {
				try {
					ArrayList<LiveGame> gml = readGame(initializeChannel());

					for (LiveGame g : gml) {
						if (g.currentTurn.equals(currentPlayer.name) && !g.notified) {
							m.displayTray("YOUR MOVE vs " + g.getOpponent(currentPlayer.name).name);
							g.notified = true;
							m.writeGames();
						}
					}
				} catch (Exception e1) {
					catchHandle(
							e1.getMessage() + " grabbing turn notifications for " + currentPlayer.name + " @ "
									+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()),
							m);
				}

				/*
				try {
					ArrayList<Chat> c = readChat(initializeChannel());

					System.out.println(c.get(0).chatlist.size());
					for (int i = 0; i < c.size();i++) {
						if (c.get(i).includesPlayer(currentPlayer))
							c.get(i).notify(m, currentPlayer);
					}
					m.writeChats();
					
					System.out.println(c.get(0).chatlist.size());
					
				} catch (Exception e1) {
					catchHandle(
							e1.getMessage() + " grabbing chat notifications for " + currentPlayer.name + " @ "
									+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()),
							m);
					e1.printStackTrace();
				}
				*/
				Thread.sleep(100000);
			}
			return "";
		}

	}

	class GameTime implements Callable<String> {

		@Override
		public String call() throws Exception {
			try {
				m.update();
			} catch (Exception e1) {
				// catchHandle(e1.getMessage() + " running initial update for " +
				// currentPlayer.name + " @ "
				// + new
				// SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()),
				// m);
				e1.printStackTrace();
			}
			return "";
		}

	}

	public void catchHandle(String msg, Menu m) {
		try {
			ChannelSftp c = initializeChannel();

			JOptionPane.showMessageDialog(null, "There was an error, this will be reported and fixed ASAP");
			m.update();

			c.get("/home/chess/" + Menu.ERRORREP, Menu.ERRORREP);

			BufferedWriter buffy = new BufferedWriter(new FileWriter(new File(Menu.ERRORREP)));
			buffy.flush();
			buffy.append(msg);

			c.put(Menu.ERRORREP, "/home/chess/" + Menu.ERRORREP);

			c.disconnect();
		} catch (Exception x) {
			JOptionPane.showMessageDialog(null, "There was an error in the error reporting.... #@$%");
			System.exit(0);
		}

	}

	public void update() throws IOException, JSchException, SftpException {
		playerList = collidePlayers(playerList, readFile());
		gameList = collideGames(gameList, readGame(channel));
		chatlist = collideChats(chatlist, readChat(channel));
		System.out.println(chatlist.get(0).chatlist.size());

		currentPlayer = matchPlayer(currentPlayer.name);

		setVisible(true);
		setSize(600, 700);
		setLocationRelativeTo(null);

		rankPnl = new JPanel();
		chatPnl = new JPanel();
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
		playPnlButton.setBackground(new Color(77, 65, 65));
		playPnlButton.setForeground(new Color(135, 67, 67));
		playPnlButton.setBounds(0, 0, srcBar.getWidth() / 5, srcBar.getHeight());
		playPnlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deselectAll();
				setInvisible();
				if (!playPnlButton.currentlySelected) {
					playPnl.setVisible(true);
					playPnlButton.currentlySelected = true;
				}
				repaint();
			}
		});
		srcBar.add(playPnlButton);

		rankPnlButton = new MenuButton("Stats", MenuButton.RANK);
		rankPnlButton.setBackground(new Color(77, 65, 65));
		rankPnlButton.setForeground(new Color(135, 67, 67));
		rankPnlButton.setBounds(getWidth() / 5, 0, srcBar.getWidth() / 5, srcBar.getHeight());
		rankPnlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deselectAll();
				setInvisible();
				if (!rankPnlButton.currentlySelected) {
					rankPnl.setVisible(true);
					rankPnlButton.currentlySelected = true;
				}
				repaint();
			}
		});
		srcBar.add(rankPnlButton);

		leadPnlButton = new MenuButton("Leaderboard", MenuButton.LEADERBOARD);
		leadPnlButton.setBackground(new Color(77, 65, 65));
		leadPnlButton.setForeground(new Color(135, 67, 67));
		leadPnlButton.setBounds(2 * getWidth() / 5, 0, srcBar.getWidth() / 5, srcBar.getHeight());
		leadPnlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deselectAll();
				setInvisible();
				if (!leadPnlButton.currentlySelected) {
					leadPnl.setVisible(true);
					leadPnlButton.currentlySelected = true;
				}
				repaint();
			}
		});
		srcBar.add(leadPnlButton);

		chatPnlButton = new MenuButton("Chat", MenuButton.CHAT);
		chatPnlButton.setBackground(new Color(77, 65, 65));
		chatPnlButton.setForeground(new Color(135, 67, 67));
		chatPnlButton.setBounds(3 * getWidth() / 5, 0, srcBar.getWidth() / 5, srcBar.getHeight());
		chatPnlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deselectAll();
				setInvisible();
				if (!chatPnlButton.currentlySelected) {
					chatPnl.setVisible(true);
					chatPnlButton.currentlySelected = true;
				}
				repaint();
			}
		});
		srcBar.add(chatPnlButton);

		JButton exitButton = new JButton("Exit");
		exitButton.setBackground(Color.red);
		exitButton.setForeground(Color.black);
		exitButton.setBounds(4 * getWidth() / 5, 0, srcBar.getWidth() / 5 - 20, srcBar.getHeight());
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					writeGames();
				} catch (Exception e1) {
					catchHandle(
							e1.getMessage() + " creating the exit button by " + currentPlayer.name + " @ "
									+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()),
							m);
				}
				System.exit(0);
			}
		});
		srcBar.add(exitButton);

		srcBar.setBackground(new Color(135, 67, 67));

		/**
		 * SRC BAR DONE
		 */
		/**
		 * PLAY PANEL
		 */

		playPnl.setLayout(null);

		JButton refresh = new JButton("Refresh");
		refresh.setBounds(425, 500, 100, 50);
		refresh.setBorder(BorderFactory.createBevelBorder(1));
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deselectAll();
				setInvisible();
				try {
					dispose();
					update();
				} catch (Exception e1) {
					catchHandle(
							e1.getMessage() + " creating the refresh button by " + currentPlayer.name + " @ "
									+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()),
							m);
				}
				playPnl.setVisible(true);
				playPnlButton.currentlySelected = true;
			}
		});
		refresh.setContentAreaFilled(false);
		playPnl.add(refresh);

		JButton play = new JButton("2 Player");
		play.setBounds(75, 100, 125, 50);
		play.setBorder(BorderFactory.createSoftBevelBorder(1));
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String plr = JOptionPane.showInputDialog("Who would you like to challenge?");
				Player plr2 = null;
				for (Player tempPlayer : playerList) {
					if (tempPlayer.name.equals(plr)) {
						plr2 = tempPlayer;
						break;
					}
				}
				if (plr2 == null) {
					JOptionPane.showMessageDialog(null, "Player does not exist!");
				} else if (JOptionPane.showInputDialog("Enter password for " + plr2.name + ": ")
						.equals(plr2.password) == false) {
					JOptionPane.showMessageDialog(null, "Wrong Password");
				} else {
					try {
						deselectAll();
						setInvisible();
						PlayLocal p = new PlayLocal(currentPlayer, plr2, m);
						p.setVisible(true);
						p.setSize(1500, 1000);
						p.setLocationRelativeTo(null);
					} catch (Exception e1) {
						catchHandle(e1.getMessage() + " challenging a 2nd player by " + currentPlayer.name + " against "
								+ plr2.name + " @ "
								+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()), m);
					}
					dispose();
				}
			}
		});
		play.setOpaque(false);
		play.setContentAreaFilled(false);

		JButton playG = new JButton("Play With Guest");
		playG.setBounds(225, 100, 125, 50);
		playG.setBorder(BorderFactory.createSoftBevelBorder(1));
		playG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PlayGuest p;
					deselectAll();
					setInvisible();
					p = new PlayGuest(currentPlayer, m);
					p.setVisible(true);
					p.setSize(1500, 1000);
					p.setLocationRelativeTo(null);
				} catch (Exception e1) {
					catchHandle(
							e1.getMessage() + " playing with quest by " + currentPlayer.name + " @ "
									+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()),
							m);
				}
				dispose();
			}
		});
		playG.setOpaque(false);
		playG.setContentAreaFilled(false);

		JButton playR = new JButton("Play with Friend");
		playR.setBounds(375, 100, 125, 50);
		playR.setBorder(BorderFactory.createSoftBevelBorder(1));
		playR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String plr = JOptionPane.showInputDialog("Who would you like to challenge?");
				Player plr2 = null;
				for (Player tempPlayer : playerList) {
					if (tempPlayer.name.equals(plr)) {
						plr2 = tempPlayer;
						break;
					}
				}

				if (plr2 == null) {
					JOptionPane.showMessageDialog(null, "Player does not exist");

				} else {
					LiveGame g = new LiveGame(currentPlayer, plr2, 0, plr2.name, "REQ", currentPlayer,
							"" + new Random().nextLong());
					try {
						writeGameRequest(g);
					} catch (Exception e1) {
						catchHandle(e1.getMessage() + " writing a new game request by " + currentPlayer.name
								+ " against " + plr2.name + " @ "
								+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()), m);
					}
				}
			}
		});
		playR.setOpaque(false);
		playR.setContentAreaFilled(false);

		JPanel gamesPnl = new JPanel();
		gamesPnl.setBackground(new Color(135, 67, 67));

		int i = 0;
		for (LiveGame g : gameList) {
			if (g.black.equals(currentPlayer) || g.white.equals(currentPlayer)) {
				JButton b = new JButton("vs " + g.getOpponent(currentPlayer.name).name);
				b.setOpaque(true);
				b.setBackground(Color.GRAY);
				b.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							PlayRemote p = new PlayRemote(currentPlayer, g, m);
							p.setSize(1500, 1000);
							p.setVisible(true);
							p.setLocationRelativeTo(null);
						} catch (Exception e1) {
							catchHandle(e1.getMessage() + " painting a live game for " + currentPlayer.name
									+ " against " + g.getOpponent(currentPlayer.name).name + " @ "
									+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()),
									m);
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
		JScrollPane gamesPane = new JScrollPane(gamesPnl, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		if (i > 1)
			gamesPane.setBounds(190, 200, 200, 400);
		else
			gamesPane.setBounds(190, 200, 200, 220);

		UIManager.getLookAndFeelDefaults().put("ScrollBar.thumb", Color.blue);

		playPnl.add(play);
		playPnl.add(playG);
		playPnl.add(playR);
		playPnl.setBackground(new Color(135, 67, 67));
		playPnl.add(gamesPane);

		/**
		 * PLAY PANEL DONE
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
		 * DONE LEADERBOARD PANEL
		 */

		/**
		 * STATS PANEL
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

		/**
		 * Chat panel
		 */

		chatPnl.setBackground(new Color(135, 67, 67));
		chatPnl.setLayout(null);

		JPanel chatPeople = new JPanel();
		JPanel chat = new JPanel();

		chatPeople.setBackground(new Color(135, 67, 67));
		chat.setBackground(new Color(135, 67, 67));
		chat.setBounds(200, 100, 350, 550);
		chatPeople.setBounds(50, 100, 150, 500);

		i = 0;
		for (Chat c : chatlist) {
			chat.setSize(400, c.chatlist.size());
			System.out.println(c.chatlist.size());
			if (c.includesPlayer(currentPlayer)) {
				JButton b = new JButton(c.getOther(currentPlayer));
				b.setBackground(new Color(77, 65, 65));
				b.setForeground(new Color(135, 67, 67));
				b.setBounds(0, i * 50, chatPeople.getWidth(), 50);
				b.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						chat.removeAll();
						c.addToPanel(chat, currentPlayer);
						chat.repaint();
						chat.setPreferredSize(new Dimension(350, c.chatlist.size() * 100));
						currentChat = c;
					}
				});
				i++;

				chatPeople.add(b);
			}
		}

		chatPeople.setLayout(new GridLayout(0, 1, 0, 0));
		chatPeople.setPreferredSize(new Dimension(150, chatlist.size() * 50));
		JScrollPane chatPplScroll = new JScrollPane(chatPeople, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		chat.setLayout(new GridLayout(0, 1, 0, 10));
		JScrollPane chatScroll = new JScrollPane(chat, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatScroll.setBounds(0, 0, 350, 450);
		chatPplScroll.setBounds(0, 0, 150, 500);

		if (i < 10)
			chatPplScroll.setSize(150, i * 60);

		if (chat.getPreferredSize().getHeight() < 450)
			chatScroll.getSize(new Dimension(350, (int) chat.getPreferredSize().getHeight()));

		JPanel cp = new JPanel();
		cp.setLayout(null);
		cp.setBackground(new Color(135, 67, 67));
		cp.setBorder(BorderFactory.createBevelBorder(1));
		cp.add(chatPplScroll);
		cp.setBounds(50, 100, 150, 500);

		JPanel c = new JPanel();
		c.setLayout(null);
		c.setBackground(new Color(135, 67, 67));
		c.setBorder(BorderFactory.createBevelBorder(1));
		c.add(chatScroll);
		c.setBounds(200, 100, 350, 450);

		chatPnl.add(cp);
		chatPnl.add(c);

		JTextField chatTxt = new JTextField("");
		chatTxt.setBounds(200, 550, 250, 50);
		chatPnl.add(chatTxt);

		JButton send = new JButton("Send");
		send.setBounds(450, 550, 100, 50);
		send.setBackground(new Color(77, 65, 65));
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentChat != null) {
					currentChat.addMessage(chatTxt.getText().trim() == "" ? "-" : chatTxt.getText(), currentPlayer,
							new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()), false);
					try {
						writeChats();
					} catch (Exception e1) {
						catchHandle(e1.getMessage() + " sending a message by " + currentPlayer.name + " @ "
								+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()), m);
					}
					currentChat.addToPanel(chat, currentPlayer);
					chat.repaint();
				} else
					JOptionPane.showMessageDialog(m, "You must have a chat selected to send a message!");
			}
		});
		chatPnl.add(send);

		setInvisible();
		getContentPane().setBackground(new Color(135, 67, 67));
		getContentPane().add(leadPnl);
		getContentPane().add(rankPnl);
		getContentPane().add(srcBar);
		getContentPane().add(playPnl);
		getContentPane().add(chatPnl);

		leadPnl.setBounds(0, 50, 600, 600);
		playPnl.setBounds(0, 50, 600, 600);
		rankPnl.setBounds(0, 50, 600, 600);
		chatPnl.setBounds(0, 50, 600, 600);
		srcBar.setBounds(0, 0, 600, 50);

		repaint();
	}

	public void displayTray(String msg) throws AWTException, MalformedURLException {
		// Obtain only one instance of the SystemTray object
		SystemTray tray = SystemTray.getSystemTray();

		// If the icon is a file
		Image image = Toolkit.getDefaultToolkit().createImage(Main.class.getResource("/white_bishop_on_black.png"));
		// Alternative (if the icon is on the classpath):
		// Image image =
		// Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

		TrayIcon trayIcon = new TrayIcon(image, "Chess");
		// Let the system resize the image if needed
		trayIcon.setImageAutoSize(true);
		// Set tooltip text for the tray icon
		trayIcon.setToolTip("System tray icon demo");
		tray.add(trayIcon);

		trayIcon.displayMessage("Chess", msg, MessageType.INFO);
	}

	public ChannelSftp initializeChannel() throws JSchException {
		String user = "chess";
		String host = "66.175.216.86";
		String password = "chessisfun";

		JSch jsch = new JSch();
		jsch.setKnownHosts(HOSTNAME);
		Session session = jsch.getSession(user, host);
		session.setPassword(password);
		session.connect();

		ChannelSftp chn = (ChannelSftp) session.openChannel("sftp");
		chn.connect();
		return chn;
	}

	private Player login() throws IOException, JSchException, SftpException {
		JTextField username = new JTextField();
		JTextField password = new JPasswordField();

		Object[] userMsg = { "Username:", username, };

		int option = JOptionPane.showConfirmDialog(null, userMsg, "Login", JOptionPane.OK_CANCEL_OPTION);

		boolean valid = true;
		for (char c : username.getText().toCharArray()) {
			if (Character.isWhitespace(c)) {
				valid = false;
			}
		}

		if (!valid) {
			JOptionPane.showMessageDialog(this, "Invalid Username");
			System.exit(0);
		}

		Player p = matchPlayer(username.getText());

		if (option == JOptionPane.OK_OPTION) {
			if (p.password == null) {
				Object[] pswrdMsg = { "Create a password:", password, };
				int option2 = JOptionPane.showConfirmDialog(null, pswrdMsg, "New User", JOptionPane.OK_CANCEL_OPTION);

				if (option2 == JOptionPane.OK_OPTION) {
					p.password = password.getText();
					writeFile();
					return p;
				} else {
					System.exit(0);
				}
			}

			Object[] pswrdMsg = { "Password:", password, };

			int option2 = JOptionPane.showConfirmDialog(null, pswrdMsg, "Login", JOptionPane.OK_CANCEL_OPTION);

			if (option2 == JOptionPane.OK_OPTION) {
				if (!password.getText().equals(p.password)) {
					JOptionPane.showMessageDialog(this, "Login Failed");
					System.exit(0);
				}
				p.password = password.getText();
				return p;
			} else {
				System.exit(0);
			}

		} else {
			JOptionPane.showMessageDialog(this, "Login Cancelled");
			System.exit(0);
		}

		return null;
	}

	private static Icon resizeIcon(Icon icon, int resizedWidth, int resizedHeight) {
		Image img = ((ImageIcon) icon).getImage();
		Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImage);
	}

	public static ArrayList<Player> readFile() throws IOException, JSchException, SftpException {
		ArrayList<Player> temp = new ArrayList<Player>();

		channel.get("/home/chess/playerdata", PLAYERDATA);

		Scanner scan = new Scanner(new File(PLAYERDATA));

		while (scan.hasNextLine())

		{
			String nmAndp = scan.nextLine();
			String[] nmAndps = nmAndp.split(";");
			Player p = new Player(nmAndps[0], nmAndps[1], false);
			String gm = scan.nextLine();
			while (!gm.equals("--")) {
				String[] theGm = gm.split(";");
				p.add(new Game(Integer.parseInt(theGm[1]), theGm[2].charAt(0), theGm[0]));
				gm = scan.nextLine();
			}
			temp.add(p);
		}
		scan.close();
		return temp;
	}

	public ArrayList<LiveGame> readGame(ChannelSftp c) throws JSchException, SftpException, IOException {
		ArrayList<LiveGame> templist = new ArrayList<LiveGame>();

		c.get("/home/chess/gamedata", GAMEDATA);

		Scanner scan = new Scanner(new File(GAMEDATA));

		while (scan.hasNextLine()) {
			String[] names = scan.nextLine().split(";");
			if (!scan.hasNext()) {
				scan.close();
				return templist;
			}
			String id = scan.nextLine();
			String nxt = scan.nextLine();

			int turnNum = Integer.parseInt(scan.nextLine());
			String turn = scan.nextLine();
			LiveGame g = new LiveGame(matchPlayer(names[0]), matchPlayer(names[1]), turnNum, turn, "ACPT",
					currentPlayer, id);
			Scanner line = new Scanner(scan.nextLine());
			line.useDelimiter(",");
			while (line.hasNext()) {
				g.addMove(line.next());
			}
			line.close();
			boolean notified = scan.nextLine().equals("true");
			g.notified = notified;
			templist.add(g);
			scan.nextLine();
		}
		scan.close();
		return templist;
	}

	public ArrayList<Chat> readChat(ChannelSftp chnl) throws SftpException, FileNotFoundException {
		ArrayList<Chat> temp = new ArrayList<Chat>();

		chnl.get("/home/chess/" + CHATDATA, CHATDATA);

		Scanner s = new Scanner(new File(CHATDATA));

		while (s.hasNext()) {
			String nms = s.nextLine();
			if ("".equals(nms))
				break;

			String[] names = nms.split(";");
			Player p1 = matchPlayer(names[0]);
			Player p2 = matchPlayer(names[1]);
			Chat c = new Chat(p1, p2);

			String nxtline = s.nextLine();
			while (!nxtline.equals("--")) {
				String[] msg = new String[4];
				int x = 0;
				String str = "";
				for (char chr : nxtline.toCharArray()) {
					if (chr == ';') {
						msg[x] = str;
						x++;
						str = "";
					} else {
						str += chr;
					}
				}
				msg[x] = str;
				Player sender = matchPlayer(msg[0]);
				c.addMessage(msg[2], sender, msg[1], msg[3].equals("true")?true:false);
				nxtline = s.nextLine();
			}
			temp.add(c);
		}
		return temp;
	}

	public void readGameRequests() throws SftpException, IOException {
		channel.get("/home/chess/" + GAMEREQ, GAMEREQ);

		Scanner scan = new Scanner(new File(GAMEREQ));

		ArrayList<LiveGame> ignore = new ArrayList<LiveGame>();

		while (scan.hasNext()) {
			String[] names = scan.nextLine().split(";");
			if (!scan.hasNext())
				return;
			String id = scan.nextLine();
			scan.nextLine();
			if (names[1].equals(currentPlayer.name)) {
				String[] opt = { "accept", "decline" };
				if (JOptionPane.showOptionDialog(this, "New game request from " + names[0], "Game Request",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opt, opt[0]) == 0) {
					LiveGame g = null;
					if (new Random().nextBoolean()) {
						g = new LiveGame(matchPlayer(names[0]), currentPlayer, 0, currentPlayer.name, "ACPT",
								currentPlayer, id);
					} else {
						g = new LiveGame(currentPlayer, matchPlayer(names[0]), 0, names[0], "ACPT", currentPlayer, id);
					}
					gameList.add(g);
				}
			} else {
				LiveGame g = new LiveGame(matchPlayer(names[0]), matchPlayer(names[1]), 0, names[1], "REQ",
						currentPlayer, id);
				ignore.add(g);
			}
			scan.next();
		}
		scan.close();

		BufferedWriter buffy = new BufferedWriter(new FileWriter(new File(GAMEREQ)));
		buffy.flush();

		for (LiveGame g : ignore) {
			buffy.write(g.printGame());
		}
		buffy.close();
		channel.put(GAMEREQ, "/home/chess/" + GAMEREQ);
	}

	public void writeGameRequest(LiveGame g) throws SftpException, IOException {
		BufferedWriter buffy = new BufferedWriter(new FileWriter(new File(GAMEREQ)));
		buffy.flush();

		buffy.append(g.printGame());

		buffy.close();
		channel.put(GAMEREQ, "/home/chess/" + GAMEREQ);
	}

	public ArrayList<LiveGame> collideGames(ArrayList<LiveGame> one, ArrayList<LiveGame> other) {
		ArrayList<LiveGame> temp = new ArrayList<>();

		for (LiveGame g : one)
			for (LiveGame h : other)
				if (g.equals(h)) {
					temp.add(g.mostUpdated(h));
					break;
				}

		one.removeAll(temp);
		other.removeAll(temp);

		for (LiveGame g : one)
			temp.add(g);

		for (LiveGame g : other)
			temp.add(g);

		return temp;
	}

	public ArrayList<Chat> collideChats(ArrayList<Chat> one, ArrayList<Chat> two) {
		ArrayList<Chat> temp = new ArrayList<Chat>();

		for (Chat c : one)
			for (Chat x : two)
				if (c.equals(x)) {
					temp.add(c.mostUpdated(x));
					break;
				}
		one.removeAll(temp);
		two.removeAll(temp);

		for (Chat c : one)
			temp.add(c);

		for (Chat c : two)
			temp.add(c);

		return temp;
	}

	public static ArrayList<Player> collidePlayers(ArrayList<Player> one, ArrayList<Player> other) {
		ArrayList<Player> temp = new ArrayList<>();

		for (Player p : one)
			for (Player j : other)
				if (p.equals(j)) {
					temp.add(p);
					break;
				}

		one.removeAll(temp);
		other.removeAll(temp);

		for (Player p : one)
			temp.add(p);

		for (Player p : other)
			temp.add(p);

		return temp;
	}

	public LiveGame matchGame(Player p, Player q) {
		for (LiveGame g : gameList) {
			if (g.black.name.equals(p.name) && g.white.name.equals(q.name))
				return g;
			else if (g.black.name.equals(q.name) && g.white.name.equals(p.name))
				return g;
		}
		return null;
	}

	public static void writeFile() throws IOException, JSchException, SftpException {
		playerList = collidePlayers(playerList, readFile());
		BufferedWriter buffy = new BufferedWriter(new FileWriter(new File(PLAYERDATA)));
		buffy.flush();
		for (Player p : playerList) {
			buffy.write(p.print());
		}
		buffy.close();

		channel.put(PLAYERDATA, "/home/chess/playerdata");
	}

	public void writeGames() throws JSchException, SftpException, IOException {
		gameList = collideGames(gameList, readGame(channel));
		BufferedWriter buffy = new BufferedWriter(new FileWriter(new File(GAMEDATA)));

		buffy.flush();
		for (LiveGame g : gameList) {
			buffy.write(g.printGame());
		}
		buffy.close();
		channel.put(GAMEDATA, "/home/chess/gamedata");
	}

	public void writeChats() throws SftpException, IOException {
		chatlist = collideChats(chatlist, readChat(channel));
		BufferedWriter buffy = new BufferedWriter(new FileWriter(new File(CHATDATA)));

		buffy.flush();
		for (Chat c : chatlist) {
			buffy.write(c.print());
		}
		buffy.close();
		channel.put(CHATDATA, "/home/chess/" + CHATDATA);
	}

	public Player matchPlayer(String str) {
		for (Player p : playerList) {
			if (p.name.equals(str)) {
				return p;
			}
		}
		Player newPlr = new Player(str);
		playerList.add(newPlr);
		return newPlr;
	}

	private void setInvisible() {
		playPnl.setVisible(false);
		chatPnl.setVisible(false);
		rankPnl.setVisible(false);
		leadPnl.setVisible(false);
	}

	private void deselectAll() {
		leadPnlButton.currentlySelected = false;
		playPnlButton.currentlySelected = false;
		rankPnlButton.currentlySelected = false;
		chatPnlButton.currentlySelected = false;
	}

	public String getLeaderBoardNames() {
		String O = "";
		for (int i = 0; i < 10 && i < playerList.size(); i++) {
			O += playerList.get(i).name + "\n";
		}
		return O;

	}

	public String getLeaderBoardRanks() {
		String O = "";
		for (int i = 0; i < 10 && i < playerList.size(); i++) {
			O += playerList.get(i).rating() + "\n";
		}
		return O;

	}

	public String getLeaderBoardRecords() {
		String O = "";
		for (int i = 0; i < 10 && i < playerList.size(); i++) {
			O += playerList.get(i).totalWins() + "/" + playerList.get(i).totalLosses() + "/"
					+ playerList.get(i).totalDraws() + "\n";
		}
		return O;

	}

	class MenuButton extends JButton {
		protected boolean currentlySelected;
		protected int decision;
		protected static final int RANK = 0;
		protected static final int PLAY = 1;
		protected static final int LEADERBOARD = 2;
		protected static final int CHAT = 3;

		public MenuButton(String s, int d) {
			super(s);
			decision = d;
			currentlySelected = false;
		}

		public MenuButton(Icon i, int d) {
			super(i);
			decision = d;
			currentlySelected = false;
		}
	}

}
