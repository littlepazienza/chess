package GUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Player.Game;
import Player.Player;
import sun.applet.Main;

public class Menu extends JFrame{

	JButton play, exit;
	JTextField p1, p2;
	static ArrayList<Player> playerList;
	
	public Menu() throws FileNotFoundException
	{
		playerList = new ArrayList<Player>();
		readFile();
		JPanel pnl = new JPanel();
		pnl.setLayout(null);
		pnl.setVisible(true);
		
		JLabel chess = new JLabel(new ImageIcon(Main.class.getResource("/chess.png")));
		chess.setBounds(40, 100, 500, 250);
		chess.setIcon(resizeIcon(chess.getIcon(), chess.getWidth(), chess.getHeight()));
		pnl.add(chess);
		
		JLabel by = new JLabel(new ImageIcon(Main.class.getResource("/by.png")));
		by.setBounds(200, 300, 150, 50);
		by.setIcon(resizeIcon(by.getIcon(), by.getWidth(), by.getHeight()));
		pnl.add(by);
		
		play = new JButton(new ImageIcon(Main.class.getResource("/play.png")));
		play.setBounds(100, 500, 150, 100);
		play.setIcon(resizeIcon(play.getIcon(), play.getWidth(), play.getHeight()));
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Player plr1 = matchPlayer(p1.getText());
				Player plr2 = matchPlayer(p2.getText());
				try {
					new Play(plr1, plr2);
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
		play.setBorderPainted(false);
		
		exit = new JButton(new ImageIcon(Main.class.getResource("/exit.png")));
		exit.setBounds(300, 500, 150, 100);
		exit.setIcon(resizeIcon(exit.getIcon(), exit.getWidth(), exit.getHeight()));
		exit.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {dispose();}
		});
		
		exit.setOpaque(false);
		exit.setContentAreaFilled(false);
		exit.setBorderPainted(false);
		
		p1 = new JTextField("Player 1");
		p1.setBounds(100, 450, 150, 35);
		p1.setFont(new Font(p1.getName(), 0, 16));
		p1.setForeground(Color.WHITE);
		p1.setSelectionStart(0);
		p1.setSelectionEnd(p1.getText().length());
		p1.setOpaque(false);
		
		p2 = new JTextField("Player 2");
		p2.setBounds(300, 450, 150, 35);
		p2.setFont(new Font(p2.getName(), 0, 16));
		p2.setForeground(Color.WHITE);
		p2.setSelectionStart(0);
		p2.setSelectionEnd(p1.getText().length());
		p2.setOpaque(false);

		
		pnl.add(play);
		pnl.add(exit);
		pnl.add(p1);
		pnl.add(p2);
		
		pnl.setBackground(new Color(135, 67, 67));
		getContentPane().add(pnl);
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		Menu m = new Menu();
		m.setVisible(true);
		m.setSize(600, 700);
		
	}
	
	private static Icon resizeIcon(Icon icon, int resizedWidth, int resizedHeight) {
	    Image img = ((ImageIcon) icon).getImage();  
	    Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
	    return new ImageIcon(resizedImage);
	}
	
	public void readFile() throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File("data"));
		while(scan.hasNextLine())
		{
			String nm = scan.nextLine();
			Player p = new Player(nm);
			String gm = scan.nextLine();
			while(!gm.equals("--"))
			{
				String[] theGm = gm.split(";");
				p.add(new Game(Integer.parseInt(theGm[0]), theGm[1].charAt(0)));
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
	}
	
	public Player matchPlayer(String str)
	{
		for(Player p:playerList)
		{
			if(p.name.equals(str))
			{
				String[] options = {"Yes", "No"};
				int n = JOptionPane.showOptionDialog(
						this,
						"Player " + str + " already exists, would you like to overwrite?",
						"Player Choice",
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, 
						null, 
						options,
						options[0]);
				if(n == 0)
				{
					playerList.remove(p);
				}
				else
				{
					return p;
				}
			}
		}
		Player newPlr = new Player(str);
		playerList.add(newPlr);
		return newPlr;
	}

}
	