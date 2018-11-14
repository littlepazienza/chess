public abstract class Piece
{
			protected char file; //vertical coordinate of piece
			protected int row; //horizontal coordinate of piece
			protected boolean captured;

			public Piece(String file, int r)
			{
				this.file = file;
				this.row = row;
				captured = false;
			}

			public void capture(){captured = true;}

			public void move(String file, int row)
			{
				this.file = file;
				this.row = row;
			}

			//true if this piece can move to (f, r)
			public boolean validMove(String f, int r);

			public Image getImage();

}
