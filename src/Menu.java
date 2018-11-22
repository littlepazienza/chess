import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Menu extends JFrame implements ActionListener{
	
	JButton play, exit;
	JTextField p1, p2;
	
	public Menu()
	{
		JPanel pnl = new JPanel();
		pnl.setLayout(null);
		pnl.setVisible(true);
		
		play = new JButton("Play");
		play.setBounds(25, 60, 100, 40);
		play.addActionListener(this);
		exit = new JButton("Exit");
		exit.setBounds(125, 60, 100, 40);
		exit.addActionListener(this);
		p1 = new JTextField("Player 1");
		p1.setBounds(25, 10, 100, 20);
		p1.setSelectionStart(0);
		p1.setSelectionEnd(p1.getText().length());
		p2 = new JTextField("Player 2");
		p2.setBounds(125, 10, 100, 20);
		p2.setSelectionStart(0);
		p2.setSelectionEnd(p1.getText().length());
		
		pnl.add(play);
		pnl.add(exit);
		pnl.add(p1);
		pnl.add(p2);
		
		getContentPane().add(pnl);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(((JButton)arg0.getSource()).getText().equals("Play"))
		{	
			String[] args = {p1.getText(), p2.getText()};
			Play.main(args);
			this.dispose();
		}
		else
			System.exit(FRAMEBITS);
	}
	
	public static void main(String[] args)
	{
		Menu m = new Menu();
		m.setVisible(true);
		m.setSize(280, 200);
		
	}

}
	