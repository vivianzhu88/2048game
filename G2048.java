package org.cis120.Game2048;

import java.io.*;
import java.util.LinkedList;

public class G2048 {
    private int[][] board = new int[4][4];
    private int score = 0;
    private boolean gameOver = false;
    private LinkedList<int[][]> history = new LinkedList<>();
    private LinkedList<Integer> scoreHistory = new LinkedList<>();

//board constructors
//------------------
    public G2048() {
        resetBoard();
    }

    //for testing
    public G2048(int[][] board) {
        if (board == null) {
            resetBoard();
        } else {
            this.board = board.clone();
        }
    }

//board basics
//------------
    public int[][] getBoard() {
        int [][] newBoard = new int[4][4];
        for (int i = 0; i < this.board.length; i++) {
            newBoard[i] = board[i].clone();
        }

        return newBoard;
    }

    public int getScore() {
        return score;
    }

    public void resetBoard() {
        gameOver = false;
        history.add(board.clone());
        scoreHistory.add(score);
        board = new int[4][4];
        addNumber();
        score = 0;
    }

    public void undoBoard() {
        if (history.size() > 1) {
            board = history.getLast();
            score = scoreHistory.getLast();
            history.removeLast();
            scoreHistory.removeLast();
            gameOver = false;
        }
    }

    public void addNumber() {
        //find the empty spots in the game board
        LinkedList<int[]> hasZero = new LinkedList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == 0) {
                    hasZero.add(new int[] {row, col});
                }
            }
        }

        //place a 2 or 4 in a random empty spot on the game board
        if (hasZero.size() > 0) {
            double r = Math.random();
            int index = (int)(Math.random() * hasZero.size());
            int[] pos = hasZero.get(index);
            if (r < 0.75) {
                board[pos[0]][pos[1]] = 2;
            } else {
                board[pos[0]][pos[1]] = 4;
            }
        }
    }

    public boolean canMove() {
        //check to see if user can make another move
        int[][] temp = board.clone();
        int[] a = shiftDirection("L", temp);
        int[] b = shiftDirection("R", temp);
        int[] c = shiftDirection("U", temp);
        int[] d = shiftDirection("D", temp);
        return (a[1] == 1) || (b[1] == 1) || (c[1] == 1) || (d[1] == 1);
    }

    public int checkVictory() {
        //checks to see if game is over or not
        //1 -> continue game
        //2 -> loss (if all blocks are filled and none are 2048)
        //3 -> win (if one block has 2048)
        boolean hasZero = false;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] >= 2048) {
                    return 3;
                }
                hasZero = hasZero || (board[row][col] == 0);
            }
        }
        if (!hasZero && !canMove()) {
            gameOver = true;
            return 2;
        }
        return 1;
    }

    public void loadGame(String gFile, String sFile) throws IOException {
        //load the game from game history and score history files
        history.clear();
        scoreHistory.clear();

        int[][] board = new int[4][4];
        FileReader f1;
        try {
            f1 = new FileReader(gFile);
        } catch (FileNotFoundException e) {
            return;
        }
        BufferedReader reader1 = new BufferedReader(f1);
        String line = "";
        int row = 0;
        while ((line = reader1.readLine()) != null) {
            String[] cols = line.split(",");
            int col = 0;
            for (String c : cols) {
                board[row][col] = Integer.parseInt(c);
                col++;
            }
            row++;
            if (row == 4) {
                history.add(board.clone());
                board = new int[4][4];
                row = 0;
            }
        }
        this.board = history.getLast();
        history.removeLast();
        reader1.close();

        FileReader f2;
        try {
            f2 = new FileReader(sFile);
        } catch (FileNotFoundException e) {
            return;
        }
        BufferedReader reader2 = new BufferedReader(f2);
        while ((line = reader2.readLine()) != null) {
            String[] scores = line.split(",");
            for (String c : scores) {
                scoreHistory.add(Integer.parseInt(c));
            }
        }
        this.score = scoreHistory.getLast();
        scoreHistory.removeLast();
        reader2.close();
    }

    public void saveGame(String gFile, String sFile) throws IOException {
        //save the game to game history and score history files
        history.add(board);
        scoreHistory.add(score);

        StringBuilder builder1 = new StringBuilder();
        for (int h = 0; h < history.size(); h++) {
            int[][] b = history.get(h);
            for (int row = 0; row < b.length; row++) {
                for (int col = 0; col < b.length; col++) {
                    builder1.append(b[row][col]);
                    if (col < b.length - 1) {
                        builder1.append(",");
                    }
                }
                builder1.append("\n");
            }
        }

        BufferedWriter writer1 = new BufferedWriter(new FileWriter(gFile));
        writer1.write(builder1.toString());//save the string representation of the board
        writer1.close();

        StringBuilder builder2 = new StringBuilder();
        for (int h = 0; h < scoreHistory.size(); h++) {
            builder2.append(scoreHistory.get(h));
            if (h < scoreHistory.size() - 1) {
                builder2.append(",");
            }
        }

        BufferedWriter writer2 = new BufferedWriter(new FileWriter(sFile));
        writer2.write(builder2.toString());//save the string representation of the board
        writer2.close();
    }

// functions that have to do with moving
//--------------------------------------
    public int[] slide(int[] row) {
        // slide items to the right
        // 2 2 0 2 -> 0 2 2 2
        if (row == null) {
            return new int[4];
        }
        LinkedList<Integer> slidedRow = new LinkedList<>();
        for (int r = 0; r < row.length; r++) {
            if (row[r] != 0) {
                slidedRow.add(row[r]);
            }
        }
        while (slidedRow.size() < row.length) {
            slidedRow.addFirst(0);
        }
        for (int r = 0; r < row.length; r++) {
            row[r] = slidedRow.get(r);
        }
        return row;
    }

    public int combineSlide(int[][] board) {
        if (board == null) {
            return 0;
        }
        int points = 0;
        for (int r1 = 0; r1 < board.length; r1++) {
            int[] row = board[r1];
            // slide items to right
            // 2 0 2 2 -> 0 2 2 2
            row = slide(row);

            // combine items
            // 0 2 2 2 -> 0 2 0 4
            for (int r2 = row.length - 1; r2 > 0; r2--) {
                int a = row[r2];
                int b = row[r2 - 1];
                if (a == b) {
                    points += a + b;
                    row[r2] = a + b;
                    row[r2 - 1] = 0;
                }
            }
            // slide items to right again
            // 0 2 0 4 -> 0 0 2 4
            row = slide(row);
            board[r1] = row;
        }
        return points;
    }

    public void reflect(int[][] board) {
        //reverse items in each row
        //0 2 0 2 -> 2 0 2 0
        //0 2 4 0 -> 0 4 2 0
        //0 2 2 2 -> 2 2 2 0
        //0 2 2 4 -> 4 2 2 0
        if (board == null) {
            return;
        }
        for (int row = 0; row < board.length; row++) {
            int[] newRow = new int[4];
            for (int col = board[row].length - 1; col >= 0; col--) {
                newRow[newRow.length - 1 - col] = board[row][col];
            }
            board[row] = newRow;
        }
    }

    public void rotateCW(int[][] board) {
        //rotate rows clockwise
        //0 2 0 2 -> 0 0 0 0
        //0 2 4 0 -> 2 2 2 2
        //0 2 2 2 -> 2 2 4 0
        //0 2 2 4 -> 4 2 0 2
        if (board == null) {
            return;
        }
        int[][] tempBoard = board.clone();
        for (int row = 0; row < tempBoard.length; row++) {
            int[] newRow = new int[4];
            for (int col = tempBoard[row].length - 1; col >= 0; col--) {
                newRow[col] = tempBoard[newRow.length - 1 - col][row];
            }
            board[row] = newRow;
        }
    }

    public int[] shiftDirection(String dir, int[][] board) {
        //shift direction of game board and check if anything changed
        if (board == null) {
            return new int[2];
        }

        int[][] prevBoard = getBoard();
        boolean hasShifted = false;
        int points = 0;

        if (dir == "L") {
            reflect(board);
            points += combineSlide(board);
            reflect(board);
        } else if (dir == "R") {
            points += combineSlide(board);
        } else if (dir == "U") {
            rotateCW(board);
            points += combineSlide(board);
            rotateCW(board);
            rotateCW(board);
            rotateCW(board);
        } else if (dir == "D") {
            rotateCW(board);
            rotateCW(board);
            rotateCW(board);
            points += combineSlide(board);
            rotateCW(board);
        } else {
            return new int[2];
        }

        //if boxes shifted, then update the history
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                hasShifted = hasShifted || (prevBoard[i][j] != board[i][j]);
            }
        }

        int[] myReturn = new int[2];
        myReturn[0] = points;
        if (hasShifted) {
            myReturn[1] = 1;
        } else {
            myReturn[1] = 0;
        }
        return myReturn;
    }

    public boolean playTurn(String dir) {
        if ((!(dir.equals("L") || dir.equals("R") || dir.equals("U") || dir.equals("D")))
                || gameOver) {
            return false;
        }

        int[][] prevBoard = getBoard();
        int prevScore = score;

        int[] pointsAndShifted = shiftDirection(dir, board);
        score += pointsAndShifted[0];
        int hasShifted = pointsAndShifted[1]; // 0 -> has not shifted ;; 1 -> has shifted

        if (checkVictory() == 2) {
            System.out.println("Congrats, you won!");
        }

        if (hasShifted == 1) {
            history.add(prevBoard);
            scoreHistory.add(prevScore);
            addNumber();
        }
        return true;
    }

//for playing the game without GUI stuff
//--------------------------------------
    public void printGameState() {
        System.out.println("\n\nPoints " + scoreHistory.getLast() + ":\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 3) {
                    System.out.print(" | ");
                }
            }
            if (i < 3) {
                System.out.println("\n---------------");
            }
        }
    }

    public static void main(String[] args) {
        G2048 g = new G2048();
        g.printGameState();
        g.playTurn("U");
        g.printGameState();
        g.playTurn("D");
        g.printGameState();
        g.playTurn("R");
        g.printGameState();
        g.playTurn("L");
        g.printGameState();
        g.playTurn("U");
        g.printGameState();
        g.playTurn("D");
        g.printGameState();
        g.playTurn("R");
        g.printGameState();
        g.playTurn("L");
        g.printGameState();
    }
}
