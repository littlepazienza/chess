package GUI;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
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
	
	public static ChannelSftp initializeChannel() throws JSchException {
		String user = "chess";
		String host = "66.175.216.86";
		String password = "chessisfun";

		JSch jsch = new JSch();
		jsch.setKnownHosts(Menu.HOSTNAME);
		Session session = jsch.getSession(user, host);
		session.setPassword(password);
		session.connect();

		ChannelSftp chn = (ChannelSftp) session.openChannel("sftp");
		chn.connect();
		return chn;
	}
	
	public static void catchHandle(String msg)
	{
		try {
			ChannelSftp c = initializeChannel();
			
			JOptionPane.showMessageDialog(null, "There was an error, this will be reported and fixed ASAP");
			
			c.get("/home/chess/" + Menu.ERRORREP, Menu.ERRORREP);
			
			BufferedWriter buffy = new BufferedWriter(new FileWriter(new File(Menu.ERRORREP)));
			buffy.flush();
			buffy.append(msg);
			
			c.put(Menu.ERRORREP, "/home/chess/" + Menu.ERRORREP);
			
			c.disconnect();
		}catch (Exception x) {
			JOptionPane.showMessageDialog(null, "There was an error in the error reporting.... #@$%");
			System.exit(0);
		}
	}
	
	public static void main(String[] args) throws InterruptedException, IOException, JSchException, SftpException, ExecutionException {
		Menu m = null;
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
			
			m = new Menu();
			m.setUndecorated(true);
			m.setVisible(true);
			m.setSize(600, 700);
			m.setBackground(new Color(135, 67, 67));		
			m.setLocationRelativeTo(null);
		}catch(Exception e)
		{
			catchHandle(e.getMessage() + " \n\n by intial loading sequence "  + " @ " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
		}
	}
	
	private static Icon resizeIcon(Icon icon, int resizedWidth, int resizedHeight) {
	    Image img = ((ImageIcon) icon).getImage();  
	    Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
	    return new ImageIcon(resizedImage);
	}
}
