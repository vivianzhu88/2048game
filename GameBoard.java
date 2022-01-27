package org.cis120.Game2048;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;


public class GameBoard extends JPanel {
    private G2048 game; // model for the game
    private JLabel status; // current status text
    private JLabel score; // current score
    private boolean playing = true; // is the game gunning

    // Game constants
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;

    public GameBoard(JLabel status, JLabel score) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        game = new G2048(); // initializes model for the game
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (playing) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        game.playTurn("U");
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        game.playTurn("D");
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        game.playTurn("R");
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        game.playTurn("L");
                    }
                    updateStatus();
                    repaint();
                }
            }
        });

        this.status = status; // initializes the status JLabel
        this.score = score; // initializes the score JLabel
    }

    public String multipleLines(String s) {
        return "<html>" + s.replaceAll("\n", "<br>");
    }

    public void guide() {
        //gives the user instructions on how to play
        final JFrame frame = new JFrame("How to Play");
        frame.setLocation(0, 0);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.NORTH);

        String s =
                "Hi! Welcome to 2048 :)\n\n" +
                "- 'Load' button lets you load the last saved game and history\n" +
                "- 'Save' button lets you save your current game and history\n" +
                "- 'Reset' button lets you restart the game\n" +
                "- 'Undo' button lets you undo your last move\n\n" +
                "Here is how you play:\n" +
                "- You can use your up, down, right, and left arrow keys " +
                "to move all the blocks in the board in that direction\n" +
                "- Combine like blocks (ex. 2 and 2 -> 4)\n" +
                "- The points you gain equal to the value of the block you create\n" +
                "- To win, you have to reach the 2048 block\n" +
                "- You lose when the board is full and you cannot move anymore";

        final JLabel help = new JLabel();
        help.setText(multipleLines(s));
        status_panel.add(help);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void reset() {
        //resets game to initial state
        game.resetBoard();
        playing = true;
        repaint();
        status.setText(" ");
        score.setText("Score: 0");

        //makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void undo() {
        //undoes the last move
        game.undoBoard();
        playing = true;
        repaint();
        status.setText(" ");
        score.setText("Score: " + game.getScore());
        requestFocusInWindow();
    }

    public void save() {
        try {
            game.saveGame("files/gameHistory.txt", "files/scoreHistory.txt");
            status.setText("Game saved. ");
        } catch (IOException e) {
            status.setText("Game not saved. ");
        }
        requestFocusInWindow();
    }

    public void load() {
        try {
            game.loadGame("files/gameHistory.txt", "files/scoreHistory.txt");
            status.setText("Game loaded. ");
            score.setText("Score: " + game.getScore());
            playing = true;
            repaint();
        } catch (IOException e) {
            status.setText("Game not loaded. ");
        }
        requestFocusInWindow();
    }

    private void updateStatus() {
        //update status to reflect win or loss
        int winner = game.checkVictory();
        score.setText("Score: " + game.getScore());
        if (winner == 3) {
            status.setText("You Won!!! ");
        } else if (winner == 2) {
            status.setText("You Lost!!! ");
            playing = false;
        } else if (winner == 1) {
            status.setText(" ");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int[][] board = game.getBoard();

        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2F);
        g.setFont(newFont);

        // Draws board
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                int state = board[row][col];

                //draw block
                String block = "";
                if (state != 0) {
                    block = String.valueOf(state);
                    int red = 255 - (int)(Math.log(state) / Math.log(2)) * 20;
                    if (red < 0) {
                        red = 0;
                    }
                    Color c = new Color(red, 255, 255);
                    g.setColor(c);
                    g.fillRect(100 * row, 100 * col, 100, 100);
                }

                //draw grid and numbers
                g.setColor(Color.black);
                g.drawRect(100 * row, 100 * col, 100, 100);
                FontMetrics metrics = g.getFontMetrics(newFont);
                int x = 100 * row + (100 - metrics.stringWidth(block)) / 2;
                int y = 100 * col + ((100 - metrics.getHeight()) / 2) + metrics.getAscent();
                g.drawString(block, x, y);
            }
        }

    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
