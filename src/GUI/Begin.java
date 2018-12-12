package GUI;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.sun.awt.AWTUtilities;

import sun.applet.Main;

public class Begin extends JFrame{

	public Begin()
	{	
		setUndecorated(true);
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
		
		getContentPane().add(pnl);
		
	}
	
	public static void main(String[] args) throws InterruptedException, IOException, JSchException, SftpException {
		try {
		Begin b = new Begin();
		b.setVisible(true);
		b.setSize(600, 450);
		b.setLocationRelativeTo(null);
		
		for(float i = 0; i < 1; i+=.01)
		{
			AWTUtilities.setWindowOpacity(b, i);
			Thread.sleep(20);
		}
		
		Thread.sleep(2000);
		
		for(float i = 0; i < 1; i+=.01)
		{
			AWTUtilities.setWindowOpacity(b, 1 - i);
			Thread.sleep(20);
		}
		
		b.dispose();
		
		Menu m = new Menu();
		m.setVisible(true);
		m.setSize(600, 700);
		m.setBackground(new Color(135, 67, 67));		
		m.setLocationRelativeTo(null);
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
		}
	}
	
	private static Icon resizeIcon(Icon icon, int resizedWidth, int resizedHeight) {
	    Image img = ((ImageIcon) icon).getImage();  
	    Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
	    return new ImageIcon(resizedImage);
	}
}
