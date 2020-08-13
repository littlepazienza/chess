global.RANDOM = 0;
global.NORMAL = 1;

global.white = true;
global.black = false;

global.BOARD_SIZE = 7;

global.TO_QUEEN = 0;
global.TO_KNIGHT = 1;
global.TO_BISHOP = 2;
global.TO_ROOK = 3;
global.CANCEL = 4;

//BOTTOM LEFT IS A BLACK SQUARE
//SQARE IS RIGHT SIDE UP SO ARRAY POSITIONS ARE [7-x][y]
//0 |1 |2 |3 |4 |5 |6 |7 (0th in array but represents 7th row)
//0 |1 |2 |3 |4 |5 |6 |7 (1st in array but represents 6th row)
//0 |1 |2 |3 |4 |5 |6 |7 (2nd in array but represents 5th row)
//0 |1 |2 |3 |4 |5 |6 |7 (3rd in array but represents 4th row)
//0 |1 |2 |3 |4 |5 |6 |7 (4th in array but represents 3rd row)
//0 |1 |2 |3 |4 |5 |6 |7 (5th in array but represents 2nd row)
//0 |1 |2 |3 |4 |5 |6 |7 (6th in array but represents 1st row)
//0 |1 |2 |3 |4 |5 |6 |7 (7th in array but represents 0th row)

//RANKS ARE THE ROWS
//FILES ARE THE COLUMNS
//A LOCATION VECTOR WILL LOOK LIKE [RANK#, FILE#]

global.newGame = function(board, type, white, black, accepted, isWhitesTurn, id, takenPieces) {
    return new Chess(board, type, white, black, accepted, isWhitesTurn, id, takenPieces);
}

global.newMove = function(fromR, fromF, toR, toF) {
    return new Move(fromR, fromF, toR, toF);
}

global.newMoveStr = function(str) {
    let arr = str.split(',');
    return new Move(arr[0], arr[1], arr[2], arr[3]);
}

global.newQueen = function(isWhite) {
    return new Queen(isWhite);
}

global.newBishop = function(isWhite) {
    return new Bishop(isWhite);
}

global.newRook = function(isWhite) {
    return new Rook(isWhite, false);
}

global.newKnight = function(isWhite) {
    return new Knight(isWhite);
}

class Chess {

    constructor(board, type, white, black, accepted, isWhitesTurn, id, takenPieces) {
        this.type = type;
        this.accepted = accepted;
        // this.moveNumber = moveNumber;
        this.isWhitesTurn = isWhitesTurn;
        this.board = Chess.parseBoard(board);
        this.takenPieces = Chess.parseTakenPieces(takenPieces);
        this.white = white;
        this.black = black;
        this.id = id;
    }

    validMove(move) {
        console.log(`Checking move valid from ${move.fromR}, ${move.fromF} to ${move.toR}, ${move.toF}`)
        return !(move.fromR == move.toR && move.fromF == move.toF) && this.emptyOrOffColor(move) && this.board[move.fromR][move.fromF].validMove(move, this) && this.validCastle(move) && !this.intoCheck(move, this.board[move.fromR][move.fromF].isWhite) && (this.isWhitesTurn == this.board[move.fromR][move.fromF].isWhite);
    }

    validCastle(move) {
        if(this.board[move.fromR][move.fromF] instanceof King) {
            if(this.board[move.fromR][move.fromF].isCastle(move, this.board)) {
                if(this.inCheck(this.board[move.fromR][move.fromF].isWhite)) {
                    return false;
                }
                else {
                    for(let i = 1; i <= Math.abs(move.toR-move.fromR);i++) {
                        if(this.intoCheck(new Move(move.fromR, move.fromF, (move.fromR>move.toR?move.fromR-i:move.fromR+i), move.toF), this.board[move.fromR][move.fromF].isWhite)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    // fastForward(moves) {
    //     for(let i = 0; i < moves.length;i++)
    //     {
    //         this.makeMove(moves[i]);
    //     }
    // }

    getAdvantage() {
        let adv = 0;
        for(let i = 0; i < this.takenPieces.length;i++)
        {
            if(this.takenPieces[i].isWhite)
            {
                adv += this.takenPieces[i].val
            }
            else
            {
                adv -= this.takenPieces[i].val
            }
        }
        return adv;
    }

    isQueening(move) {
        return this.board[move.fromR][move.fromF] instanceof Pawn && this.board[move.fromR][move.fromF].isQueening(move);
    }

    isCastling(move) {
        return this.board[move.fromR][move.fromF] instanceof King && this.board[move.fromR][move.fromF].isCastle(move, this.board);
    }

    makeMove(move, queeningChoice) {
        console.log('moving piece');
        if(this.board[move.fromR][move.fromF] instanceof King && this.board[move.fromR][move.fromF].isCastle(move, this.board))
        {
            this.board[move.fromR][(move.toF==2?3:5)] = this.board[move.fromR][(move.toF==2?0:7)];
            this.board[move.fromR][(move.toF==2?0:7)].notMoved = false;
            this.board[move.fromR][(move.toF==2?0:7)] = null;
        }

        if(this.board[move.fromR][move.fromF] instanceof Pawn && this.board[move.fromR][move.fromF].isQueening(move))
        {
            if(queeningChoice == TO_QUEEN) {this.board[move.fromR][move.fromF] = new Queen(this.board[move.fromR][move.fromF].isWhite);}
            else if(queeningChoice == TO_BISHOP) {this.board[move.fromR][move.fromF] = new Bishop(this.board[move.fromR][move.fromF].isWhite);}
            else if(queeningChoice == TO_KNIGHT) {this.board[move.fromR][move.fromF] = new Knight(this.board[move.fromR][move.fromF].isWhite);}
            else if(queeningChoice == TO_ROOK) {this.board[move.fromR][move.fromF] = new Rook(this.board[move.fromR][move.fromF].isWhite);}
            else {
                return false;
            }
        }

        this.board[move.toR][move.toF] = this.board[move.fromR][move.fromF];
        this.board[move.fromR][move.fromF].notMoved = false;
        this.board[move.fromR][move.fromF] = null;
        return true;
    }

    emptyOrOffColor(move) {
         return this.board[move.toR][move.toF] == null || this.board[move.fromR][move.fromF].isWhite != this.board[move.toR][move.toF].isWhite;
    }

    intoCheck(move, isWhite) {
        let temp = this.board[move.toR][move.toF];
        this.board[move.toR][move.toF] = this.board[move.fromR][move.fromF];
        this.board[move.fromR][move.fromF] = null;

        if(this.inCheck(isWhite)) {
            this.board[move.fromR][move.fromF] = this.board[move.toR][move.toF];
            this.board[move.toR][move.toF] = temp;
            return true;
        }
        this.board[move.fromR][move.fromF] = this.board[move.toR][move.toF];
        this.board[move.toR][move.toF] = temp;
        return false;
    }

    validMoves(isWhite) {
        let allValidMoves = [];
        for(let i = 0; i <= BOARD_SIZE;i++) {
            for(let j = 0; j <= BOARD_SIZE;j++) {
                if(this.board[i][j] != null && this.board[i][j].isWhite == isWhite) {
                    allValidMoves = allValidMoves.concat(this.board[i][j].validMoves(i, j, this));
                }
            }
        }
        return allValidMoves;
    }

    inCheck(isWhite) {
        let kingPos = this.findKing(isWhite);
        for(let i = 0; i <= BOARD_SIZE;i++) {
            for(let j = 0; j <= BOARD_SIZE;j++) {
                if(this.board[i][j] != null && this.board[i][j].isWhite != isWhite) {
                    if(this.board[i][j].validMove(new Move(i, j, kingPos[0], kingPos[1]), this)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    isCheckMate(isWhite) {
        for(let i = 0; i <= BOARD_SIZE;i++) {
            for(let j = 0; j <= BOARD_SIZE;j++) {
                if(this.board[i][j] != null && this.board[i][j].isWhite == isWhite) {
                    if(this.board[i][j].validMoves(new Move(i, j), this).length > 0) {
                        console.log(`this piece has validmoves: ${i}, ${j}`)
                        console.log(this.board[i][j].validMoves(new Move(i, j), this));
                        return false;
                    }
                }
            }
        }
        return true;
    }

    findKing(isWhite) {
        for(let i = 0; i <= BOARD_SIZE; i++) {
            for(let j = 0; j <= BOARD_SIZE;j++) {
                if(this.board[i][j] instanceof King && this.board[i][j].isWhite == isWhite) {
                    return [i, j];
                }
            }
        }
        return [-1,-1];
    }

    static parseTakenPieces(takenPieceDTO)
    {
        let arr = [];
        for(let i = 0; i < takenPieceDTO.length;i++)
        {
            arr.push( Chess.parseTile(takenPieceDTO[i]));
        }
        return arr;
    }

    static parseBoard(boardDTO) {
        let brd = [];
        for(let i = 0; i < boardDTO.length;i++)
        {
            let tmp = [];
            for(let j = 0; j < boardDTO[i].length;j++)
            {
                tmp.push(Chess.parseTile(boardDTO[i][j]));
            }
            brd.push(tmp);
        }
        return brd;
    }

    static parseTile(tile)
    {
        if(tile == null) {return null;}

        switch (tile.charAt(1)) {
            case 'b': return new Bishop(tile.charAt(0) == 'w');
            case 'r': return new Rook(tile.charAt(0) == 'w', true);
            case 'q': return new Queen(tile.charAt(0) == 'w');
            case 'n': return new Knight(tile.charAt(0) == 'w');
            case 'p': return new Pawn(tile.charAt(0) == 'w');
            case 'k': return new King(tile.charAt(0) == 'w', true);
            default:  return null;
        }
    }

    // static randomBoard() {
    //     return [z
    //         [this.randomPiece(black), this.randomPiece(black), this.randomPiece(black), this.randomPiece(black), new King(black, true), this.randomPiece(black), this.randomPiece(black), this.randomPiece(black)],
    //         [this.randomPiece(black), this.randomPiece(black), this.randomPiece(black), this.randomPiece(black), this.randomPiece(black), this.randomPiece(black), this.randomPiece(black), this.randomPiece(black)],
    //         [null, null, null, null, null, null, null, null],
    //         [null, null, null, null, null, null, null, null],
    //         [null, null, null, null, null, null, null, null],
    //         [null, null, null, null, null, null, null, null],
    //         [this.randomPiece(white), this.randomPiece(white), this.randomPiece(white), this.randomPiece(white), this.randomPiece(white), this.randomPiece(white), this.randomPiece(white), this.randomPiece(white)],
    //         [this.randomPiece(white), this.randomPiece(white), this.randomPiece(white), this.randomPiece(white), new King(white, true), this.randomPiece(white), this.randomPiece(white), this.randomPiece(white)]
    //     ];
    // }
    //
    // static normalBoard() {
    //     return [
    //         [new Rook(black, true), new Knight(black), new Bishop(black), new Queen(black), new King(black, true), new Bishop(black), new Knight(black), new Rook(black, true)],
    //         [new Pawn(black), new Pawn(black), new Pawn(black), new Pawn(black), new Pawn(black), new Pawn(black), new Pawn(black), new Pawn(black)],
    //         [null, null, null, null, null, null, null, null],
    //         [null, null, null, null, null, null, null, null],
    //         [null, null, null, null, null, null, null, null],
    //         [null, null, null, null, null, null, null, null],
    //         [new Pawn(white), new Pawn(white), new Pawn(white), new Pawn(white), new Pawn(white), new Pawn(white), new Pawn(white), new Pawn(white)],
    //         [new Rook(white, true), new Knight(white), new Bishop(white), new Queen(white), new King(white, true), new Bishop(white), new Knight(white), new Rook(white, true)]
    //     ];
    // }

    // static randomPiece(isWhite) {
    //     let min = 1;
    //     let max = 6;
    //     let num = Math.floor(Math.random() * (+max - +min)) + +min;
    //     console.log(`Random number is ${num}`);
    //
    //     switch (num)
    //     {
    //         case 1: return new Pawn(isWhite);
    //         case 2: return new Bishop(isWhite);
    //         case 3: return new Rook(isWhite, true);
    //         case 4: return new Queen(isWhite);
    //         case 5: return new Knight(isWhite);
    //         default: return null;
    //     }
    // }
}

class Piece {

    constructor(isWhite) {
        this.isWhite = isWhite;
    }

}

class Bishop extends Piece {

    static abbr = 'b';

    constructor(isWhite) {
        super(isWhite);
        this.val = 3;
    }

    getImg() {
        return `../img/chessicons/chessicons/${this.isWhite?'w':'b'}b.svg`
    }

    validMove(move) {
        if(Math.abs(move.toR-move.fromR) != Math.abs(move.toF-move.fromF))
        {
            return false;
        }
        for(let i = 1; i < Math.abs(move.fromR-move.toR);i++){
            if(game.board[move.fromR + (move.fromR-move.toR >0 ?-i:+i)][move.fromF + (move.fromF-move.toF>0?-i:+i)] != null)
            {
                return false;
            }
        }
        return true;
    }

    validMoves(move, game) {
        let validMoves = [];

        //diagonal up left

        let i = 1;
        while (move.fromR + i <= BOARD_SIZE && move.fromF + i <= BOARD_SIZE)
        {
            if(game.board[move.fromR+i][move.fromF+i] != null) {
                break;
            }

            if(!game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+i, move.fromF+i), this.isWhite))
            {
                validMoves.push([move.fromR+i, move.fromF+i]);
            }
            i++;
        }

        i = 1;
        while (move.fromR + i <= BOARD_SIZE && move.fromF - i >= 0)
        {
            if(game.board[move.fromR+i][move.fromF-i] != null) {
                break;
            }

            if(!game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+i, move.fromF-i), this.isWhite))
            {
                validMoves.push([move.fromR+i, move.fromF-i]);
            }
            i++;
        }

        i = 1;
        while (move.fromR - i >= 0 && move.fromF + i <= BOARD_SIZE)
        {
            if(game.board[move.fromR-i][move.fromF+i] != null) {
                break;
            }

            if(!game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-i, move.fromF+i), this.isWhite))
            {
                validMoves.push([move.fromR-i, move.fromF+i]);
            }
            i++;
        }

        i = 1;
        while (move.fromR - i >= 0 && move.fromF - i >= 0)
        {
            if(game.board[move.fromR-i][move.fromF-i] != null) {
                break;
            }

            if(!game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-i, move.fromF-i), this.isWhite))
            {
                validMoves.push([move.fromR-i, move.fromF-i]);
            }
            i++;
        }

        return validMoves;
    }
}

class Rook extends Piece {

    static abbr = 'r';

    constructor(isWhite, notMoved) {
        super(isWhite);
        this.notMoved = notMoved;
        this.val = 5;
    }

    getImg() {
        return `../img/chessicons/chessicons/${this.isWhite?'w':'b'}r.svg`
    }

    validMove(move, game) {
        if(move.fromR != move.toR && move.fromF != move.toF) {
            return false;
        }
        let dist = (move.fromR == move.toR? Math.abs(move.fromF-move.toF):Math.abs(move.fromR-move.toR));
        for(let i = 1; i < dist;i++)
        {
            let r = move.fromR == move.toR? move.fromR : (move.fromR > move.toR?move.fromR - i:move.fromR + i);
            let f = move.fromF == move.toF? move.fromF : (move.fromF > move.toF?move.fromF - i:move.fromF + i);

            if(game.board[r][f] != null)
            {
                return false;
            }
        }
        return true;
    }

    validMoves(move, game) {
        let validMoves = [];

        let i = 1;
        while(move.fromR+i <= BOARD_SIZE) {
            if(game.board[move.fromR+i][move.fromF] != null) {
                break;
            }

            if(!game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+i, move.fromF), this.isWhite))
            {
                validMoves.push([move.fromR+i,move.fromF]);
            }
            i++;
        }

        i = 1;
        while(move.fromF+i <= BOARD_SIZE) {
            if(game.board[move.fromR][move.fromF+i] != null) {
                break;
            }

            if(!game.intoCheck(new Move(move.fromR, move.fromF, move.fromR, move.fromF+i), this.isWhite))
            {
                validMoves.push([move.fromR,move.fromF+i]);
            }
            i++;
        }

        i = 1;
        while(move.fromR-i >= 0) {
            if(game.board[move.fromR-i][move.fromF] != null) {
                break;
            }

            if(!game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-i, move.fromF), this.isWhite))
            {
                validMoves.push([move.fromR-i,move.fromF]);
            }
            i++;
        }

        i = 1;
        while(move.fromF-i >= 0) {
            if(game.board[move.fromR][move.fromF-i] != null) {
                break;
            }

            if(!game.intoCheck(new Move(move.fromR, move.fromF, move.fromR, move.fromF-i), this.isWhite))
            {
                validMoves.push([move.fromR, move.fromF - i]);
            }
            i++;
        }

        return validMoves;
    }
}

class Knight extends Piece {

    static abbr = 'n';


    constructor(isWhite) {
        super(isWhite);
        this.val = 3;
    }

    getImg() {
        return `../img/chessicons/chessicons/${this.isWhite?'w':'b'}n.svg`
    }

    validMove(move, game){
        return (Math.abs(move.fromR-move.toR) == 2 && Math.abs(move.fromF-move.toF) == 1) || (Math.abs(move.fromR-move.toR) == 1 && Math.abs(move.fromF-move.toF) == 2);
    }

    validMoves(move, game) {
        let validMoves = [];

        if(move.fromR+2 <= BOARD_SIZE && move.fromF+1 <= BOARD_SIZE && (game.board[move.fromR+2][move.fromF+1] == null || game.board[move.fromR+2][move.fromF+1].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+2, move.fromF+1), this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+2, move.fromF+1), this.isWhite)) {validMoves.push([move.fromR+2, move.fromF+1]);}
        if(move.fromR+2 <= BOARD_SIZE && move.fromF-1 >= 0 &&          (game.board[move.fromR+2][move.fromF-1] == null || game.board[move.fromR+2][move.fromF-1].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+2, move.fromF-1), this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+2, move.fromF-1), this.isWhite)) {validMoves.push([move.fromR+2, move.fromF-1]);}
        if(move.fromR-2 >= 0 && move.fromF+1 <= BOARD_SIZE &&          (game.board[move.fromR-2][move.fromF+1] == null || game.board[move.fromR-2][move.fromF+1].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-2, move.fromF+1), this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-2, move.fromF+1), this.isWhite)) {validMoves.push([move.fromR-2, move.fromF+1]);}
        if(move.fromR-2 >= 0 && move.fromF-1 >= 0 &&                   (game.board[move.fromR-2][move.fromF-1] == null || game.board[move.fromR-2][move.fromF-1].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-2, move.fromF-1), this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-2, move.fromF-1), this.isWhite)) {validMoves.push([move.fromR-2, move.fromF-1]);}
        if(move.fromR+1 <= BOARD_SIZE && move.fromF+2 <= BOARD_SIZE && (game.board[move.fromR+1][move.fromF+2] == null || game.board[move.fromR+1][move.fromF+2].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+1, move.fromF+2), this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+1, move.fromF+2), this.isWhite)) {validMoves.push([move.fromR+1, move.fromF+2]);}
        if(move.fromR+1 <= BOARD_SIZE && move.fromF-2 >= 0 &&          (game.board[move.fromR+1][move.fromF-2] == null || game.board[move.fromR+1][move.fromF-2].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+1, move.fromF-2), this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+1, move.fromF-2), this.isWhite)) {validMoves.push([move.fromR+1, move.fromF-2]);}
        if(move.fromR-1 >= 0 && move.fromF+2 <= BOARD_SIZE &&          (game.board[move.fromR-1][move.fromF+2] == null || game.board[move.fromR-1][move.fromF+2].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-1, move.fromF+2), this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-1, move.fromF+2), this.isWhite)) {validMoves.push([move.fromR-1, move.fromF+2]);}
        if(move.fromR-1 >= 0 && move.fromF-2 >= 0 &&                   (game.board[move.fromR-1][move.fromF-2] == null || game.board[move.fromR-1][move.fromF-2].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-1, move.fromF-1), this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-1, move.fromF-2), this.isWhite)) {validMoves.push([move.fromR-1, move.fromF-2]);}

        return validMoves;
    }
}

class Pawn extends Piece {

    static abbr = 'p';

    constructor(isWhite) {
        super(isWhite);
        this.val = 1;
    }

    getImg() {
        return `../img/chessicons/chessicons/${this.isWhite?'w':'b'}p.svg`
    }

    validMove(move, game) {
        if ( ((move.fromF == move.toF) && ((this.isWhite && move.fromR - move.toR == 1 && game.board[move.toR][move.toF] == null) || (this.isWhite && move.fromR == 6 && move.fromR - move.toR == 2 && game.board[5][move.fromF] == null && game.board[move.toR][move.toF] == null))) || ((move.fromF == move.toF) && ((!this.isWhite && move.toR - move.fromR == 1 && game.board[move.toR][move.toF] == null) || (!this.isWhite && move.fromR == 1 && move.toR - move.fromR == 2 && game.board[2][move.fromF] == null && game.board[move.toR][move.toF] == null)) ) ) {
            return true;
        }

        if ((Math.abs(move.fromF - move.toF) == 1 && this.isWhite && move.fromR - move.toR == 1 && game.board[move.toR][move.toF] != null) || (Math.abs(move.fromF - move.toF) == 1 && !this.isWhite && move.toR - move.fromR == 1 && game.board[move.toR][move.toF] != null)) {
            return true;
        }

        //not yet done: en passant

        return false;
    }

    validMoves(move, game) {
        let validMoves = [];
        if (this.isWhite && game.board[move.fromR - 1][move.fromF] == null && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-1, move.fromF), this.isWhite))
        {
            validMoves.push([move.fromR-1, move.fromF]);
        }

        if(!this.isWhite && game.board[move.fromR+1][move.fromF] == null && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+1, move.fromF), this.isWhite))
        {
            validMoves.push([move.fromR+1, move.fromF]);
        }

        if (this.isWhite && move.fromR == 6 && game.board[move.fromR - 1][move.fromF] == null && game.board[move.fromR-2][move.fromF] == null && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-2, move.fromF), this.isWhite))
        {
            validMoves.push([move.fromR-2, move.fromF]);
        }

        if (!this.isWhite && move.fromR == 1 && game.board[move.fromR + 1][move.fromF] == null && game.board[move.fromR+2][move.fromF] == null && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+2, move.fromF), this.isWhite))
        {
            validMoves.push([move.fromR+2, move.fromF]);
        }

        if(this.isWhite && game.board[move.fromR-1][move.fromF+1] != null && game.board[move.fromR-1][move.fromF+1].isWhite != this.isWhite && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-1, move.fromF+1), this.isWhite))
        {
            validMoves.push([move.fromR-1, move.fromF+1]);
        }

        if(this.isWhite && game.board[move.fromR-1][move.fromF-1] != null && game.board[move.fromR-1][move.fromF-1].isWhite != this.isWhite && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-1, move.fromF-1), this.isWhite))
        {
            validMoves.push([move.fromR-1, move.fromF-1]);
        }

        if(!this.isWhite && game.board[move.fromR+1][move.fromF+1] != null && game.board[move.fromR+1][move.fromF+1].isWhite != this.isWhite && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+1, move.fromF+1), this.isWhite))
        {
            validMoves.push([move.fromR+1, move.fromF+1]);
        }

        if(!this.isWhite && game.board[move.fromR+1][move.fromF-1] != null && game.board[move.fromR+1][move.fromF-1].isWhite != this.isWhite && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+1, move.fromF-1), this.isWhite))
        {
            validMoves.push([move.fromR+1, move.fromF-1]);
        }

        return validMoves;
    }

    isQueening(move) {
        return move.toR == 7 || move.toR == 0;
    }
}

class King extends Piece {

    static abbr = 'k';


    constructor(isWhite, notMoved) {
        super(isWhite);
        this.notMoved = notMoved;
        this.val = 0;
    }

    getImg() {
        return `../img/chessicons/chessicons/${this.isWhite?'w':'b'}k.svg`
    }

    validMove(move, game) {
        if( Math.abs(move.fromR-move.toR) <= 1 && Math.abs(move.fromF-move.toF) <= 1) {
            return true;
        }

        return this.isCastle(move, game.board);
    }

    isCastle(move, board) {
        if(this.isWhite && board[move.fromR][move.fromF].notMoved && move.fromR == move.toR && move.fromR == 7 && (move.toF == 2 && board[7][0] != null && board[7][1] == null && board[7][2] == null && board[7][3] == null && board[7][0] instanceof Rook && board[7][0].notMoved) ||  (move.toF == 6 && board[7][7] != null && board[7][5] == null && board[7][6] == null && board[7][7] instanceof Rook && board[7][7].notMoved)) {
            return true;
        }

        if(!this.isWhite && board[move.fromR][move.fromF].notMoved && move.fromR == move.toR && move.fromR == 0 && (move.toF == 2 && board[0][0] != null && board[0][1] == null && board[0][2] == null && board[0][3] == null && board[0][0] instanceof Rook && board[0][0].notMoved) ||  (move.toF == 6 && board[0][7] != null && board[0][5] == null && board[0][6] == null && board[0][7] instanceof Rook && board[0][7].notMoved)) {
            return true;
        }

        return false;
    }

    validMoves(move, game) {
        let validMoves = [];

        //DIAGONALS
        if(move.fromR+1 <= BOARD_SIZE && move.fromF+1 <= BOARD_SIZE && (game.board[move.fromR+1][move.fromF+1] == null || game.board[move.fromR+1][move.fromF+1].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+1, move.fromF+1), this.isWhite)) {validMoves.push([move.fromR+1, move.fromF+1])}
        if(move.fromR+1 <= BOARD_SIZE && move.fromF-1 >= 0          && (game.board[move.fromR+1][move.fromF-1] == null || game.board[move.fromR+1][move.fromF-1].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+1, move.fromF-1), this.isWhite)) {validMoves.push([move.fromR+1, move.fromF-1])}
        if(move.fromR-1 >= 0 && move.fromF+1 <= BOARD_SIZE          && (game.board[move.fromR-1][move.fromF+1] == null || game.board[move.fromR-1][move.fromF+1].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-1, move.fromF+1), this.isWhite)) {validMoves.push([move.fromR-1, move.fromF+1])}
        if(move.fromR-1 >= 0 && move.fromF-1 >= 0                   && (game.board[move.fromR-1][move.fromF-1] == null || game.board[move.fromR-1][move.fromF-1].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-1, move.fromF-1), this.isWhite)) {validMoves.push([move.fromR-1, move.fromF-1])}

        //sides (up down)
        if(move.fromR+1 <= BOARD_SIZE && (game.board[move.fromR+1][move.fromF] == null || game.board[move.fromR+1][move.fromF].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR+1, move.fromF, this.isWhite))) {validMoves.push([move.fromR+1, move.fromF])}
        if(move.fromR-1 >= 0          && (game.board[move.fromR-1][move.fromF] == null || game.board[move.fromR-1][move.fromF].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR-1, move.fromF, this.isWhite))) {validMoves.push([move.fromR-1, move.fromF])}

        //sides (left right)
        if(move.fromF+1 <= BOARD_SIZE && (game.board[move.fromR][move.fromF+1] == null || game.board[move.fromR][move.fromF+1].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR, move.fromF+1), this.isWhite)) {validMoves.push([move.fromR, move.fromF+1])}
        if(move.fromF-1 >= 0          && (game.board[move.fromR][move.fromF-1] == null || game.board[move.fromR][move.fromF-1].isWhite != this.isWhite) && !game.intoCheck(new Move(move.fromR, move.fromF, move.fromR, move.fromF-1), this.isWhite)) {validMoves.push([move.fromR, move.fromF-1])}

        if(this.isWhite && game.board[move.fromR][move.fromF].notMoved && move.fromR == 7 && game.board[7][0] != null && game.board[7][1] == null && game.board[7][2] == null && game.board[7][3] == null && game.board[7][0] instanceof Rook && game.board[7][0].notMoved  && !game.intoCheck(new Move(move.fromR, move.fromF, 7, 2), this.isWhite)) {validMoves.push([7,2])}
        if(this.isWhite && game.board[move.fromR][move.fromF].notMoved && move.fromR == 7 && game.board[7][7] != null && game.board[7][5] == null && game.board[7][6] == null && game.board[7][7] instanceof Rook && game.board[7][7].notMoved  && !game.intoCheck(new Move(move.fromR, move.fromF, 7, 6), this.isWhite)) {validMoves.push([7, 6])}

        if(!this.isWhite && game.board[move.fromR][move.fromF].notMoved && move.fromR == 0 && game.board[0][0] != null && game.board[0][1] == null && game.board[0][2] == null && game.board[0][3] == null && game.board[0][0] instanceof Rook && game.board[0][0].notMoved && !game.intoCheck(new Move(move.fromR, move.fromF, 0, 2), this.isWhite)) {validMoves.push([0,2])}
        if(!this.isWhite && game.board[move.fromR][move.fromF].notMoved && move.fromR == 0 && game.board[0][7] != null && game.board[0][5] == null && game.board[0][6] == null && game.board[0][7] instanceof Rook && game.board[0][7].notMoved  && !game.intoCheck(new Move(move.fromR, move.fromF, 0, 6), this.isWhite)) {validMoves.push([0, 6])}

        return validMoves;
    }


}

class Queen extends Piece {

    static abbr = 'q';

    constructor(isWhite) {
        super(isWhite);
        this.val = 9;
    }

    getImg() {
        return `../img/chessicons/chessicons/${this.isWhite?'w':'b'}q.svg`
    }

    validMove(move, game) {
        return new Bishop(this.isWhite).validMove(move, game) || new Rook(this.isWhite).validMove(move, game);
    }

    validMoves(move, game) {
        return new Bishop(this.isWhite).validMoves(move, game).concat(new Rook(this.isWhite).validMoves(move, game));
    }
}

class Move{

    constructor(fromR, fromF, toR, toF) {
        this.fromR = fromR;
        this.fromF = fromF;
        this.toR = toR;
        this.toF =toF;
    }

    toString() {
        return `${this.fromR},${this.fromF},${this.toR},${this.toF}`;
    }

}


