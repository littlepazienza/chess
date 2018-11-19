import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import BreezySwing.*;
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
	public Play()
	{
		g = new GameBoard();
		g.GameFill();
		whiteTurn = true;
		selectedR = -1;
		selectedF = -1;
		buttons = new tileButton[8][8];
		
		JPanel frame = new JPanel();
		frame.setLayout(null);
	
		for(int i = 1 ; i <= 8;i++)
			for(int j = 0; j < 8;j++)
			{
				JButton button = new tileButton("" + files[j] + i, i-1, j);
				button.addActionListener(this);
				button.setBounds((40 + 50*j), (440 - 50*i), 50, 50);
				frame.add(button);		
				buttons[i-1][j] = button;
			}
		
		getContentPane().add(frame, 0);
		frame.setSize(600, 600);
		frame.setVisible(true);
		
	}
		
	public static void main(String[] args)
	{
		Play p = new Play();
		p.setVisible(true);
		p.setSize(505, 530);
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
				update();
				selectedR = -1;
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Invalid Move");
				selectedR = -1;
			}
			
		}
			
	}
	
	private void update()
	{
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8;j++)
			{
				
			}
	}
	
	class tileButton extends JButton
	{
		protected int file, row;
		public tileButton(String txt, int r, int f) 
		{
			super(txt);
			this.row = r;
			this.file = f;
		}
	}
}
