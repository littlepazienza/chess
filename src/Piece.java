import java.awt.Image;

public abstract class Piece
{
			protected char file; //vertical coordinate of piece
			protected int row; //horizontal coordinate of piece
			protected boolean captured;

			public Piece(char file, int r)
			{
				this.file = file;
				this.row = row;
				captured = false;
			}

			public void capture(){captured = true;}

			public void move(char file, int row)
			{
				this.file = file;
				this.row = row;
			}

			//true if this piece can move to (f, r)
			public abstract boolean validMove(char f, int r);

			public abstract Image getImage();

}
