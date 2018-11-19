import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import BreezySwing.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Play extends JFrame implements ActionListener
{
	char[] files = {'a', 'b', 'c', 'd', 'e','f', 'g','h'};
	protected JButton[][] buttons;
	protected GameBoard g;
	boolean whiteTurn;
	protected int selectedR, selectedF;
	JPanel frame = new JPanel();
	
	public Play()
	{
		g = new GameBoard();
		g.GameFill();
		whiteTurn = true;
		selectedR = -1;
		selectedF = -1;
		buttons = new tileButton[8][8];
		
		update();
		
		frame.setLayout(null);
		
		getContentPane().add(frame, 0);
		frame.setSize(600, 600);
		frame.setVisible(true);
		
	}
		
	public static void main(String[] args)
	{
		Play p = new Play();
		p.setVisible(true);
		p.setSize(800, 800);
	}

	public void actionPerformed(ActionEvent e) {
		tileButton b = (tileButton) e.getSource();
		
		if(selectedR == -1)
		{
			selectedR = b.row;
			selectedF = b.file;
		}
		else
		{
			if(g.makeMove(selectedR, selectedF, b.row, b.file))
			{
				selectedR = -1;
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Invalid Move");
				selectedR = -1;
			}
			
		}
		update();
	}
	
	private void update()
	{
		frame.removeAll();
		boolean white = true;
		for(int i = 0 ; i < 8;i++)
		{	
			if(white)
				white = false;
			else
				white = true;
			
			for(int j = 0; j < 8;j++)
			{				
				JButton button;
				if(g.board[i][j] != null)
					button = new tileButton(g.board[i][j].getImage(white), i, j);
				else
				{
					if(white)
						button = new tileButton(new ImageIcon("res/white.png"), i, j);
					else
						button = new tileButton(new ImageIcon("res/black.png"), i, j);
				}
				button.setBounds((40 + 75*j), (600 - 75*i), 75, 75);
				button.setIcon(resizeIcon(button.getIcon(), button.getWidth(), button.getHeight()));
				button.addActionListener(this);
				frame.add(button);	
				
				buttons[i][j] = button;
				
				if(white)
					white =false;
				else
					white = true;
			}
			frame.repaint();
		}
	}
	
	private static Icon resizeIcon(Icon icon, int resizedWidth, int resizedHeight) {
	    Image img = ((ImageIcon) icon).getImage();  
	    Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
	    return new ImageIcon(resizedImage);
	}
	
	class tileButton extends JButton
	{
		protected int file, row;
		public tileButton(ImageIcon txt, int r, int f) 
		{
			super(txt);
			this.row = r;
			this.file = f;
		}
		
	}
}
