package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import Game.Bishop;
import Game.GameBoard;
import Game.Knight;
import Game.Pawn;
import Game.Piece;
import Game.Queen;
import Game.Rook;
import Player.Game;
import Player.Player;
import sun.applet.Main;

public class Play extends JFrame implements ActionListener {
	protected ImageIcon[] options = { new ImageIcon(Main.class.getResource("/white_queen_on_white.png")),
			new ImageIcon(Main.class.getResource("/white_knight_on_white.png")),
			new ImageIcon(Main.class.getResource("/white_bishop_on_white.png")),
			new ImageIcon(Main.class.getResource("/white_rook_on_white.png")) };

	protected int turnNum;
	protected boolean wasAValidMove;
	protected char[] files = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
	protected String[] yon = { "Yes", "No" };
	protected JButton[][] buttons;
	protected GameBoard g;
	boolean whiteTurn;
	protected int selectedR, selectedF;
	JPanel frame = new JPanel();
	protected Player p1, p2;
	protected String moves;

	public Play(Player p, Player q) throws IOException {
		p1 = p;
		p2 = q;
		setVisible(true);
		setSize(1500, 1000);

		g = new GameBoard();
		g.GameFill();
		whiteTurn = true;
		selectedR = -1;
		selectedF = -1;
		buttons = new tileButton[8][8];
		turnNum = 1;
		wasAValidMove = false;
		moves = "";

		update(p, q);

		frame.setLayout(null);
		frame.setBackground(new Color(135, 67, 67));
		getContentPane().setBackground(new Color(30, 206, 219));
		getContentPane().add(frame, 0);
		frame.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		wasAValidMove = false;
		tileButton b = (tileButton) e.getSource();
		Piece[][] temp = g.getTempOfBoard();
		if(b.row == selectedR && b.file == selectedF)
		{
			g.board[b.row][b.file].setSelected();
			selectedR = -1;
		}
		else if (selectedR == -1) {
			if (whiteTurn && g.board[b.row][b.file] != null && g.board[b.row][b.file].color != Piece.Side.WHITE) {
				JOptionPane.showMessageDialog(null, "White's turn");
			} else if (!whiteTurn && g.board[b.row][b.file] != null
					&& g.board[b.row][b.file].color != Piece.Side.BLACK) {
				JOptionPane.showMessageDialog(null, "Black's turn");
			} else if(g.board[b.row][b.file] != null){
				selectedR = b.row;
				selectedF = b.file;
				g.board[b.row][b.file].setSelected();
			}
		} else {
			if (g.fakeMove(selectedR, selectedF, b.row, b.file, g.getTempOfBoard())) {
				wasAValidMove = true;
				if (JOptionPane.showOptionDialog(this, "Are you sure?", "Confirm Move", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, yon, yon[1]) == 0
						&& g.makeMove(selectedR, selectedF, b.row, b.file)) {
					int selectedRTemp = selectedR;
					selectedR = -1;
					if (whiteTurn) {
						moves += turnNum + ". ";
						whiteTurn = false;
					} else {
						whiteTurn = true;
						turnNum++;
					}
					moves += g.moveNotation(selectedRTemp, selectedF, b.row, b.file, temp) + " ";
				}
			} else if (!wasAValidMove){
				JOptionPane.showMessageDialog(null, "Invalid Move");
				g.board[selectedR][selectedF].setSelected();
				selectedR = -1;
			} else
			{
				g.board[selectedR][selectedF].setSelected();
				selectedR = -1;
			}
		}
		
		try {
			update(p1, p2);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void update(Player p, Player q) throws IOException {
		frame.removeAll();

		Pawn pwn = g.pawnPromotion();
		if (pwn != null) {
			char f = files[pwn.file];
			int n = JOptionPane.showOptionDialog(this, "Choose which piece to upgrade the " + f + " pawn to...",
					f + " Pawn Promotion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
					options[0]);
			if (n == 0 || JOptionPane.CANCEL_OPTION == n)
				g.board[pwn.row][pwn.file] = new Queen(pwn.row, pwn.file, pwn.color);
			else if (n == 1)
				g.board[pwn.row][pwn.file] = new Knight(pwn.row, pwn.file, pwn.color);
			else if (n == 2)
				g.board[pwn.row][pwn.file] = new Bishop(pwn.row, pwn.file, pwn.color);
			else if (n == 3)
				g.board[pwn.row][pwn.file] = new Rook(pwn.row, pwn.file, pwn.color);
		}

		if (whiteTurn) {
			// white side

			boolean white = true;
			for (int i = 0; i < 8; i++) {
				if (white)
					white = false;
				else
					white = true;

				for (int j = 0; j < 8; j++) {
					JButton button;
					if (g.board[i][j] != null) {
						button = new tileButton(new ImageIcon(Main.class.getResource(g.board[i][j].getImage(white))), i,
								j);
					} else {
						if (white)
							button = new tileButton(new ImageIcon(Main.class.getResource("/white.png")), i, j);
						else
							button = new tileButton(new ImageIcon(Main.class.getResource("/black.png")), i, j);
					}
					button.setBounds((180 + 75 * j), (670 - 75 * i), 75, 75);
					button.setIcon(resizeIcon(button.getIcon(), button.getWidth(), button.getHeight()));
					button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
					button.addActionListener(this);
					frame.add(button);

					buttons[i][j] = button;

					if (white)
						white = false;
					else
						white = true;
				}
			}

		}
		// black side
		else {
			boolean white = false;
			for (int i = 0; i < 8; i++) {
				if (white)
					white = false;
				else
					white = true;

				for (int j = 0; j < 8; j++) {
					JButton button;
					if (g.board[7 - i][7 - j] != null) {
						button = new tileButton(
								new ImageIcon(Main.class.getResource(g.board[7 - i][7 - j].getImage(white))), 7 - i,
								7 - j);
					} else {
						if (white)
							button = new tileButton(new ImageIcon(Main.class.getResource("/white.png")), 7 - i, 7 - j);
						else
							button = new tileButton(new ImageIcon(Main.class.getResource("/black.png")), 7 - i, 7 - j);
					}
					button.setBounds((180 + 75 * j), (670 - 75 * i), 75, 75);
					button.setIcon(resizeIcon(button.getIcon(), button.getWidth(), button.getHeight()));
					button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
					button.addActionListener(this);
					frame.add(button);

					buttons[i][j] = button;

					if (white)
						white = false;
					else
						white = true;
				}
			}
		}
		// whose turn is it
		JLabel turn = new JLabel((whiteTurn ? p.name + "'s Turn" : q.name + "'s Turn"));
		turn.setBounds(40, 400, 200, 100);
		turn.setFont(new Font(turn.getName(), Font.PLAIN, 18));
		turn.setForeground(Color.WHITE);

		// configure advantage
		int w = g.whiteSum();
		int b = g.blackSum();
		JLabel adv;

		if (w > b) {
			adv = new JLabel("+" + (w - b));
			adv.setBounds(200, 775, 100, 100);
			adv.setFont(new Font(adv.getName(), Font.PLAIN, 18));
			adv.setForeground(Color.WHITE);
			frame.add(adv);
		} else if (w < b) {
			adv = new JLabel("+" + (b - w));
			adv.setBounds(200, 25, 100, 100);
			adv.setFont(new Font(adv.getName(), Font.PLAIN, 18));
			adv.setForeground(Color.WHITE);
			frame.add(adv);
		}

		// pieces captured
		w = 0;
		b = 0;
		JLabel lbl;
		for (Piece pie : g.captured) {
			if (pie.color == Piece.Side.WHITE) {
				lbl = new JLabel(new ImageIcon(Main.class.getResource(pie.getImage(false))));
				lbl.setBounds(225 + 25 * w, 50, 50, 50);
				w++;
				frame.add(lbl);
			} else {
				lbl = new JLabel(new ImageIcon(Main.class.getResource(pie.getImage(false))));
				lbl.setBounds(225 + 25 * b, 800, 50, 50);
				b++;
				frame.add(lbl);
			}

		}

		// text area for moves
		JTextArea moveList = new JTextArea(moves);
		moveList.setBackground(Color.WHITE);
		moveList.setEditable(false);
		moveList.setLineWrap(true);
		moveList.setFont(new Font(moveList.getName(), 0, 16));
		moveList.setBounds(900, 200, 500, 500);
		frame.add(moveList);

		// teams
		JButton whiteBtn = new JButton(p.name);
		whiteBtn.setBackground(new Color(135, 67, 67));
		whiteBtn.setBounds(75, 775, 100, 100);
		whiteBtn.setForeground(Color.BLACK);
		whiteBtn.setBackground(Color.WHITE);
		whiteBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		whiteBtn.setEnabled(false);
		frame.add(whiteBtn);

		JLabel whiteRating = new JLabel("" + p.rating());
		whiteRating.setOpaque(false);
		whiteRating.setBounds(75, 750, 100, 30);
		whiteRating.setFont(new Font(whiteRating.getName(), 0, 12));
		whiteRating.setForeground(Color.WHITE);
		frame.add(whiteRating);

		JButton blackBtn = new JButton(q.name);
		blackBtn.setBackground(new Color(135, 67, 67));
		blackBtn.setBounds(75, 30, 100, 100);
		blackBtn.setForeground(Color.WHITE);
		blackBtn.setBackground(Color.BLACK);
		blackBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		blackBtn.setEnabled(false);
		frame.add(blackBtn);

		JLabel blackRating = new JLabel("" + q.rating());
		blackRating.setOpaque(false);
		blackRating.setBounds(75, 5, 100, 30);
		blackRating.setFont(new Font(blackRating.getName(), 0, 12));
		blackRating.setForeground(Color.WHITE);
		frame.add(blackRating);

		frame.add(turn);
		frame.repaint();

		// check for check mate
		String[] winnerBox = { "Play Again!", "Nah, I'm Good. Thanks though, I really appreciate the offer." };
		int endgm = g.endGameStatus();
		if (endgm == g.BLACK_WIN) {
			endgm = JOptionPane.showOptionDialog(this, "Black Wins!!!", "Winner is...", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, winnerBox, winnerBox[0]);
			if (endgm == 0) {
				int r = p1.rating();
				p1.add(new Game(p2.rating(), 'L'));
				p2.add(new Game(r, 'W'));
				Menu.writeFile();
				Menu.main(null);
				dispose();
			} else {
				int r = p1.rating();
				p1.add(new Game(p2.rating(), 'L'));
				p2.add(new Game(r, 'W'));
				Menu.writeFile();
				dispose();
			}
		} else if (endgm == g.WHITE_WIN) {
			endgm = JOptionPane.showOptionDialog(this, "White Wins!!!", "Winner is...", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, winnerBox, winnerBox[0]);
			if (endgm == 0) {
				int r = p1.rating();
				p1.add(new Game(p2.rating(), 'W'));
				p2.add(new Game(r, 'L'));
				Menu.writeFile();
				Menu.main(null);
				dispose();
			} else {
				int r = p1.rating();
				p1.add(new Game(p2.rating(), 'W'));
				p2.add(new Game(r, 'L'));
				Menu.writeFile();
				dispose();
			}
		}
	}

	private static Icon resizeIcon(Icon icon, int resizedWidth, int resizedHeight) {
		Image img = ((ImageIcon) icon).getImage();
		Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImage);
	}

	class tileButton extends JButton {
		protected int file, row;

		public tileButton(ImageIcon txt, int r, int f) {
			super(txt);
			this.row = r;
			this.file = f;
		}

	}
}
