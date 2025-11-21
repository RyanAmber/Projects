package Chess;

import java.util.*;

public class ChessPlayer {
    private int type;
    public ChessPlayer(int type){
        this.type=type;
    }
    public String[] getMove(ChessBoard board, char team, Map<String, Integer> boardStates,Scanner s){
        String[] move=new String[2];
        if (type==1){  
            System.out.print("Enter move (e.g., g1 f3): ");
            move[0] = s.next();
            move[1] = s.next();
        }else if (type==2){
            System.out.println("Current board score: " + score(board.copyBoard(), team, board.halfmoveClock, new double[]{0.0,1.0,3.0,3.0,5.0,9.0,0.02},boardStates));
            List<List<Integer>> allMoves = board.getAllLegalMoves(team);
            Map<List<Integer>,Double> scores=new HashMap<List<Integer>,Double>();
            for (List<Integer> m : allMoves) {
                int startrow=m.get(0);
                int startcol=m.get(1);
                int endrow=m.get(2);
                int endcol=m.get(3);
                ChessPiece[][] testboard=board.copyBoard();
            int moves=board.halfmoveClock;
                if (testboard[endrow][endcol]!=null||testboard[startrow][startcol].getType().equals("P")){
                    moves=0;
                }else{
                    moves++;
                }
                testboard[endrow][endcol] = testboard[startrow][startcol];
                testboard[startrow][startcol] = null;
                double[] weights=getWeights();//AI adjust
                System.out.println("Testing move: " + (char)('a' + startcol) + (8 - startrow) + " to " + (char)('a' + endcol) + (8 - endrow));
                scores.put(m,score(testboard,team,moves,weights,boardStates));
            }
            //System.out.println("All moves listed");
            double minimaxscore=team=='w'?Double.NEGATIVE_INFINITY:Double.POSITIVE_INFINITY;
            List<List<Integer>> bestMoves=new ArrayList<List<Integer>>();
            for (Map.Entry<List<Integer>, Double> entry : scores.entrySet()) {
                int fromRow = entry.getKey().get(0);
                int fromCol = entry.getKey().get(1);
                int toRow = entry.getKey().get(2);
                int toCol = entry.getKey().get(3);
                String fromNotation = "" + (char)('a' + fromCol) + (8 - fromRow);
                String toNotation = "" + (char)('a' + toCol) + (8 - toRow);
                System.out.println(minimaxscore + " " + entry.getValue() + " " + fromNotation + " " + toNotation);
                if (team=='w'?entry.getValue()>minimaxscore:entry.getValue()<minimaxscore){
                    minimaxscore=entry.getValue();
                    bestMoves.clear();
                    bestMoves.add(entry.getKey());
                }else if(entry.getValue()==minimaxscore){
                    bestMoves.add(entry.getKey());
                }
            }
            List<Integer> moveIndices = bestMoves.get(new Random().nextInt(bestMoves.size()));
            move[0] = "" + (char)('a' + moveIndices.get(1)) + (8 - moveIndices.get(0));
            move[1] = "" + (char)('a' + moveIndices.get(3)) + (8 - moveIndices.get(2));
        }else if(type==3){
            List<List<Integer>> allMoves = board.getAllLegalMoves(team);
            if (allMoves.size() > 0) {
                Random rand = new Random();
                List<Integer> moveIndices = allMoves.get(rand.nextInt(allMoves.size()));
                move[0] = "" + (char)('a' + moveIndices.get(1)) + (8 - moveIndices.get(0));
                move[1] = "" + (char)('a' + moveIndices.get(3)) + (8 - moveIndices.get(2));
            }
        }else if(type==4){
            ChessBoard tempBoard = new ChessBoard();
            tempBoard.setupBoard(board.getBoard());
            ChessBoardNode rootNode = new ChessBoardNode(tempBoard, team, boardStates, new ArrayList<>());
            rootNode.getAllNextMoves();
            double bestScore = team == 'w' ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            List<ChessBoardNode> bestNodes = new ArrayList<>();
            double eps = 2e-6;//Slight randomness
            for (ChessBoardNode child : rootNode.getNextMoves()) {
                double moveScore = child.getScoreAtDepth(0);
                if (team == 'w') {
                    if (moveScore > bestScore + eps) {
                        bestScore = moveScore;
                        bestNodes.clear();
                        bestNodes.add(child);
                    } else if (Math.abs(moveScore - bestScore) <= eps) {
                        bestNodes.add(child);
                    }
                } else {
                    if (moveScore < bestScore - eps) {
                        bestScore = moveScore;
                        bestNodes.clear();
                        bestNodes.add(child);
                    } else if (Math.abs(moveScore - bestScore) <= eps) {
                        bestNodes.add(child);
                    }
                }
            }
            if (!bestNodes.isEmpty()) {
                //System.out.println("What do I do?");
                ChessBoardNode chosen = bestNodes.get(new Random().nextInt(bestNodes.size()));
                List<Integer> moveIndices = chosen.getMove();
                if (moveIndices != null && moveIndices.size() >= 4) {
                    move[0] = "" + (char)('a' + moveIndices.get(1)) + (8 - moveIndices.get(0));
                    move[1] = "" + (char)('a' + moveIndices.get(3)) + (8 - moveIndices.get(2));
                }
            }

        }
        System.out.println("Chosen move: " + move[0] + " to " + move[1]);
        return move;
    }
    public double[] getWeights(){
        return new double[]{0.0,1.0,3.0,3.0,5.0,9.0,0.02};//AI adjust
    }
    public double score(ChessPiece[][] board, char team,int moves,double[] weights,Map<String, Integer> boardStates){
        ChessBoard b=new ChessBoard();
        b.setupBoard(board);
        double score=0;
        score+=pieceValues(board,team,weights)*2;//AI adjust
        double wmodifier=0.0;
        double bmodifier=0.0;
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (board[i][j]!=null){
                    if (board[i][j].getColor()=='w'){
                        if(b.isSquareAttacked(i, j, 'b')){
                            ChessPiece temp=board[i][j];
                            int atkval=b.minSquareAttacked(i,j,'b');
                            int numatkers=b.numSquareAttacked(i,j,'b');
                            int numdefenders=b.numSquareDefended(i,j,'w');
                            if(atkval>0&&(numdefenders>=numatkers)){
                                switch (temp.getType()){
                                    case "P": wmodifier-=Math.max(0,1.0-atkval); break;
                                    case "N": wmodifier-=Math.max(0,3.0-atkval); break;
                                    case "B": wmodifier-=Math.max(0,3.0-atkval); break;
                                    case "R": wmodifier-=Math.max(0,5.0-atkval); break;
                                    case "Q": wmodifier-=Math.max(0,9.0-atkval); break;
                                }
                            }else if(atkval>0){
                                switch (temp.getType()){
                                    case "P": wmodifier-=1.0; break;//AI adjust
                                    case "N": wmodifier-=3.0; break;//AI adjust
                                    case "B": wmodifier-=3.0; break;//AI adjust
                                    case "R": wmodifier-=5.0; break;//AI adjust
                                    case "Q": wmodifier-=9.0; break;//AI adjust
                                }
                            }
                        }
                    }else if(board[i][j].getColor()=='b'){
                        ChessPiece temp=board[i][j];
                        if(b.isSquareAttacked(i, j, 'w')){
                            int atkval=b.minSquareAttacked(i,j,'w');
                            if(atkval>0&&b.isSquareDefended(i,j,'b')){
                                switch (temp.getType()){
                                    case "P": bmodifier+=Math.max(0,1.0-atkval); break;
                                    case "N": bmodifier+=Math.max(0,3.0-atkval); break;
                                    case "B": bmodifier+=Math.max(0,3.0-atkval); break;
                                    case "R": bmodifier+=Math.max(0,5.0-atkval); break;
                                    case "Q": bmodifier+=Math.max(0,9.0-atkval); break;
                                }
                            }else if(atkval>0){
                                switch (temp.getType()){
                                    case "P": bmodifier+=1.0; break;//AI adjust
                                    case "N": bmodifier+=3.0; break;//AI adjust
                                    case "B": bmodifier+=3.0; break;//AI adjust
                                    case "R": bmodifier+=5.0; break;//AI adjust
                                    case "Q": bmodifier+=9.0; break;//AI adjust
                                }
                            }
                        }
                    }
                }
            }
        }
        if(team=='w'){
            score+=wmodifier*2;
            score+=bmodifier*0.2;
        }else{
            score+=bmodifier*2;
            score+=wmodifier*0.2;
        }
        score+=0.3*kingSafety(board,'w');//AI adjust
        score-=0.3*kingSafety(board,'b');//AI adjust
        score+=0.5*rookFiles(board,'w');//AI adjust
        score-=0.5*rookFiles(board,'b');//AI adjust
        score+=1.2*pawnProgress(board,'w');//AI adjust
        score-=1.2*pawnProgress(board,'b');//AI adjust
        score+=0.4*activePieces(board,'w');//AI adjust
        score-=0.4*activePieces(board,'b');//AI adjust
        List<List<Integer>> allMoves = b.getAllLegalMoves('w');
        for (List<Integer> move : allMoves) {
            score+=weights[6];
            if(move.size()>0)
            move.get(0);
        }
        allMoves = b.getAllLegalMoves('b');
        for (List<Integer> move : allMoves) {
            score-=weights[6];
            if(move.size()>0)
            move.get(0);
        }
        if(boardStates.containsKey(b.toString())&&team=='w'){
            score-=2.0*boardStates.get(b.toString());
        }else if(boardStates.containsKey(b.toString())&&team=='b'){
            score+=2.0*boardStates.get(b.toString());
        }
        if (b.isInCheck('w')){
            score-=0.3;//AI adjust
        }else if(b.isInCheck('b')){
            score+=0.3;//AI adjust
        }
        if (b.isInStalemate('w')){
            score=0;
        }
        if(b.isInStalemate('b')){
            score=0;
        }
        if (b.isInsufficientMaterial()){
            score=0;
        }
        if(moves>=98){
            score=0;
        }
        if (b.isInCheckmate('w')){
            score-=200000;
        }else if(b.isInCheckmate('b')){
            score+=200000;
        }
        if(b.onlyQueen('w')){
            board=b.getBoard();
            score+=100*queenSolve(board,'w');
            if(b.isInStalemate('b')){
                score-=500;
            }
        }else if(b.onlyQueen('b')){
            board=b.getBoard();
            score-=100*queenSolve(board,'b');
            if(b.isInStalemate('w')){
                score+=500;
            }
        }
        /*if(b.onlyPawn('w')){
            board=b.getBoard();
            score+=100*pawnSolve(board,'w');
            score+=500;
        }
        if(b.onlyPawn('b')){
            board=b.getBoard();
            score-=100*pawnSolve(board,'b');
            score-=500;
        } */
        return Math.round(score*1000.0)/2000.0;
    }
    public int queenSolve(ChessPiece[][] board, char team){
        
        int qX=-1;
        int qY=-1;
        int kX=-1;
        int kY=-1;
        int oX=-1;
        int oY=-1;
        int score=0;
        for (int r=0;r<8;r++){
            for (int c=0;c<8;c++){
                if (board[r][c]!=null){
                    if ((board[r][c].getType().equals("Q"))&&board[r][c].getColor()==team){
                        qY=r;
                        qX=c;
                    }
                    if ((board[r][c].getType().equals("K"))&&board[r][c].getColor()==team){
                        kY=r;
                        kX=c;
                    }
                    if ((board[r][c].getType().equals("K"))&&board[r][c].getColor()!=team){
                        oY=r;
                        oX=c;
                    }
                }
            }
        }
        int distY=qY-oY;
        int distX=oX-qX;
        if(distY==1&&distX==2){
            score+=5;
        }
        if(qY==1&&qX==4&&oY==0&&oX>6){
            score+=10;
            score-=Math.abs(2-kY);
            score-=Math.abs(6-kX);
        }
        score-=Math.abs(oY-qY);
        score-=Math.abs(oX-qX);
        if (Math.abs(oY-qY)<=1&&Math.abs(oX-qX)<=1){
            score-=13;
        }
        return score;
    }
    public double pawnSolve(ChessPiece[][] board, char team){
        int pX=-1;
        int pY=-1;
        int kX=-1;
        int kY=-1;
        double score=0;
        for (int r=0;r<8;r++){
            for (int c=0;c<8;c++){
                if (board[r][c]!=null){
                    if ((board[r][c].getType().equals("P"))&&board[r][c].getColor()==team){
                        pY=r;
                        pX=c;
                    }
                    if ((board[r][c].getType().equals("K"))&&board[r][c].getColor()==team){
                        kY=r;
                        kX=c;
                    }
                }
            }
        }
        if (team=='b'){
            score+=pY*2;
            score-=Math.abs(kX - pX)*2.5;
            score-=Math.abs(kY - pY)*2.5;
            if (pY==7){
                score+=20;
            }
        }else{
            score+=(7 - pY)*2;
            score-=Math.abs(kX - pX)*2.5;
            score-=Math.abs(kY - pY)*2.5;
            if (pY==0){
                score+=20;
            }
        }
        return score;
    }
    public double pieceValues(ChessPiece[][] board, char team, double weights[]){
        double score=0;
        for (int r=0;r<8;r++){
            for (int c=0;c<8;c++){
                if (board[r][c]!=null){
                    double val=0;
                    switch (board[r][c].getType()){
                        case "P": val=weights[1]; break;
                        case "N": val=weights[2]; break;
                        case "B": val=weights[3]; break;
                        case "R": val=weights[4]; break;
                        case "Q": val=weights[5]; break;
                    }
                    if (board[r][c].getColor()=='w'){
                        score+=val;
                    }else{
                        score-=val;
                    }
                }
            }
        }        
        return score;
    }
    public static double kingSafety(ChessPiece[][] board, char team){
        ChessBoard b=new ChessBoard();
        b.setupBoard(board);
        double safety=0;
        int kingRow=-1;
        int kingCol=-1;
        for (int r=0;r<8;r++){
            for (int c=0;c<8;c++){
                if (board[r][c]!=null&&(board[r][c].getType().equals("K")&&board[r][c].getColor()==team)){
                    kingRow=r;
                    kingCol=c;
                }
            }
        }
        if (kingRow!=-1&&kingCol!=-1){
            int[][] directions={{-1,0},{1,0},{0,-1},{0,1},{-1,-1},{-1,1},{1,-1},{1,1}};
            for (int[] dir:directions){
                int newRow=kingRow+dir[0];
                int newCol=kingCol+dir[1];
                if (newRow>=0&&newRow<8&&newCol>=0&&newCol<8){
                    if (board[newRow][newCol]==null||board[newRow][newCol].getColor()==team){
                        safety+=0.5;//AI adjust
                    }else if (board[newRow][newCol].getColor()!=team){
                        safety-=1;//AI adjust
                    }else if(b.isSquareAttacked(newRow,newCol,team=='w'?'b':'w')){
                        safety-=1;//AI adjust
                    }
                }else{
                    safety+=0.5;//AI adjust
                }
            }
        }
        return safety;
    }
    public static double rookFiles(ChessPiece[][] board, char team){
        double openFiles=0;
        for (int c=0;c<8;c++){
            boolean hasRook=false;
            boolean isOpen=true;
            for (int r=0;r<8;r++){
                if (board[r][c]!=null){
                    if (board[r][c].getType().equals("R")&&board[r][c].getColor()==team){
                        hasRook=true;
                    }else if (board[r][c].getType().equals("P")&&board[r][c].getColor()==team){
                        isOpen=false;
                    }
                }
            }
            if (hasRook&&isOpen){
                openFiles+=0.3;//AI adjust
            }
        }
        return openFiles;
    }
    public static double pawnProgress(ChessPiece[][] board, char team){
        double progress=0;
        for (int c=0;c<8;c++){
            for (int r=0;r<8;r++){
                if (board[r][c]!=null&&(board[r][c].getType().equals("P"))&&board[r][c].getColor()==team){
                    if (team=='b'){
                        progress+=r/10.0;//AI adjust
                    }else{
                        progress+=(7.0-r)/10.0;//AI adjust
                    }
                    if(r==7||r==0){
                        progress+=2;//AI adjust
                    }
                }
            }
        }
        return progress*0.9;//AI adjust
    }
    public static double activePieces(ChessPiece[][] board, char team){
        double active=0;
        ChessBoard b=new ChessBoard();
        b.setupBoard(board);
        for (int c=0;c<8;c++){
            if(team=='w')
            if(board[7][c]!=null&&(board[7][c].getColor()==team)&&(board[7][c].getType().equals("B")||board[7][c].getType().equals("N"))){
                active-=1;//AI adjust
            }
            else
            if(board[0][c]!=null&&(board[0][c].getColor()==team)&&(board[0][c].getType().equals("B")||board[0][c].getType().equals("N"))){
                active-=1;//AI adjust
            }
        }
        return active;
    }
}
