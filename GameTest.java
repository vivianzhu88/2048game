package org.cis120.Game2048;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testCreateGameBoard() {
        G2048 g = new G2048();
        int[][] b = g.getBoard();
        int nonEmptyItems = 0;
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                if (b[i][j] != 0) {
                    nonEmptyItems++;
                }
            }
        }
        assertEquals(nonEmptyItems, 1);
    }

    @Test
    public void testCreateNullGameBoard() {
        G2048 g = new G2048(null);
        int[][] b = g.getBoard();
        int nonEmptyItems = 0;
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                if (b[i][j] != 0) {
                    nonEmptyItems++;
                }
            }
        }
        assertEquals(nonEmptyItems, 1);
    }

    @Test
    public void testCreateNotNullGameBoard() {
        int[][] b = {{2,0,2,2},
            {4,0,4,2},
            {0,0,0,0},
            {2,4,0,2}};
        G2048 g = new G2048(b);
        int [][] b2 = g.getBoard();
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                assertEquals(b2[i][j], b[i][j]);
            }
        }
    }

    @Test
    public void testGetGameBoardObjects() {
        int[][] b = {{2,0,2,2},
            {2,2,2,2},
            {0,2,2,4},
            {2,4,2,2}};
        G2048 g = new G2048(b);
        int [][] newB = g.getBoard();
        assertFalse(newB == b);
    }

    @Test
    public void testGetBeginningScore() {
        G2048 g = new G2048();
        assertEquals(g.getScore(), 0);
    }

    @Test
    public void testScore() {
        int[][] b = {{2,0,2,2},
            {4,0,4,2},
            {0,0,0,0},
            {2,4,0,2}};
        G2048 g = new G2048(b);
        int score = g.combineSlide(b);
        assertEquals(score, 12);
    }

    @Test
    public void testReset() {
        int[][] b = {{2,0,2,2},
            {4,0,4,2},
            {0,0,0,0},
            {2,4,0,2}};
        G2048 g = new G2048(b);
        g.resetBoard();
        int[][] b2 = g.getBoard();
        int nonEmptyItems = 0;
        for (int i = 0; i < b2.length; i++) {
            for (int j = 0; j < b2[i].length; j++) {
                if (b2[i][j] != 0) {
                    nonEmptyItems++;
                }
            }
        }
        assertEquals(nonEmptyItems, 1);
    }

    @Test
    public void testUndoBoard() {
        int[][] b = {{2,0,2,2},
            {4,0,4,2},
            {0,0,0,0},
            {2,4,0,2}};
        G2048 g = new G2048(b);
        g.playTurn("R");
        g.undoBoard();
        int [][] b2 = g.getBoard();
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                assertEquals(b2[i][j], b[i][j]);
            }
        }
    }

    @Test
    public void testAddNumber() {
        int[][] b = {{0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}};
        G2048 g = new G2048(b);
        g.addNumber();

        int[][] b2 = g.getBoard();
        int nonEmptyItems = 0;
        for (int i = 0; i < b2.length; i++) {
            for (int j = 0; j < b2[i].length; j++) {
                if (b2[i][j] != 0) {
                    nonEmptyItems++;
                }
            }
        }
        assertEquals(nonEmptyItems, 1);
    }

    @Test
    public void testCanMoveFalse() {
        int[][] b = {{2,4,2,4},
            {4,2,4,2},
            {2,4,2,4},
            {4,2,4,2}};
        G2048 g = new G2048(b);
        assertFalse(g.canMove());
    }

    @Test
    public void testCanMoveLRUD() {
        //Left and Right
        int[][] b = {{2,8,2,4},
            {4,4,4,2},
            {2,8,2,4},
            {4,2,4,2}};
        G2048 g = new G2048(b);
        assertTrue(g.canMove());

        //Up and Down
        int[][] b2 = {{2,4,2,4},
            {8,4,8,2},
            {2,4,2,4},
            {4,2,4,2}};
        G2048 g2 = new G2048(b2);
        assertTrue(g2.canMove());
    }

    @Test
    public void testCheckVictoryWin() {
        int[][] b = {{2,4,2,4},
            {4,2,4,2},
            {2,2048,2,4},
            {4,2,4,2}};
        G2048 g = new G2048(b);
        int v = g.checkVictory();
        assertEquals(v,3);
    }

    @Test
    public void testCheckVictoryLoss() {
        int[][] b = {{2,4,2,4},
            {4,2,4,2},
            {2,4,2,4},
            {4,2,4,2}};
        G2048 g = new G2048(b);
        int v = g.checkVictory();
        assertEquals(v,2);
    }

    @Test
    public void testCheckVictoryContinue() {
        int[][] b = {{2,4,2,4},
            {4,2,4,2},
            {4,4,2,4},
            {4,2,4,2}};
        G2048 g = new G2048(b);
        int v = g.checkVictory();
        assertEquals(v,1);
    }

    @Test
    public void testLoadGameFileNotFound() {
        int[][] b = {{2,4,2,4},
            {4,2,4,2},
            {4,4,2,4},
            {4,2,4,2}};
        G2048 g = new G2048(b);
        try {
            g.loadGame("nonexistent.txt", "sdjfkls");
        } catch (IOException e) { }

        int [][] b2 = g.getBoard();
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                assertEquals(b2[i][j], b[i][j]);
            }
        }
    }

    @Test
    public void testSaveLoadGameFileFound() {
        int[][] b = {{2,4,2,4},
            {4,2,4,2},
            {4,4,2,4},
            {4,2,4,2}};
        G2048 g1 = new G2048(b);
        try {
            g1.saveGame("gameTest.txt", "scoreTest.txt");
        } catch (IOException e) { }

        G2048 g2 = new G2048();
        try {
            g2.loadGame("gameTest.txt", "scoreTest.txt");
        } catch (IOException e) { }

        int [][] b2 = g2.getBoard();
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                assertEquals(b2[i][j], b[i][j]);
            }
        }
    }

    @Test
    public void testSlideNull() {
        int[][] b = {{2,0,2,2},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}};
        G2048 g = new G2048(b);
        int[] row = g.slide(null);
        assertTrue(Arrays.equals(row, new int[4]));
    }

    @Test
    public void testSlideNotNull() {
        int[][] b = {{2,0,2,2},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}};
        G2048 g = new G2048(b);
        int[] row = g.slide(b[0]);
        int[] expected = {0,2,2,2};
        assertTrue(Arrays.equals(row, expected));
    }

    @Test
    public void testCombineSlideNull() {
        G2048 g = new G2048();
        int expected = g.combineSlide(null);
        assertEquals(expected, 0);
    }

    @Test
    public void testCombineSlideNotNull() {
        int[][] b = {{2,0,2,2},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}};
        G2048 g = new G2048(b);
        int expected = g.combineSlide(b); // values inside b have changed
        assertEquals(expected, 4);

        int[][] bExpected = {{0,0,2,4},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}};
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                assertEquals(b[i][j], bExpected[i][j]);
            }
        }
    }

    @Test
    public void testReflectNull() {
        int[][] b = {{2,0,2,2},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}};
        G2048 g = new G2048(b);
        g.reflect(null); // nothing happens
        int[][] b2 = g.getBoard();
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                assertEquals(b[i][j], b2[i][j]);
            }
        }
    }

    @Test
    public void testReflectNotNull() {
        int[][] b = {{2,0,2,2},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}};
        G2048 g = new G2048(b);
        g.reflect(b);

        int[][] bExpected = {{2,2,0,2},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}};
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                assertEquals(b[i][j], bExpected[i][j]);
            }
        }
    }

    @Test
    public void testRotateCWNull() {
        int[][] b = {{2,0,2,2},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}};
        G2048 g = new G2048(b);
        g.rotateCW(null); // nothing happens
        int[][] b2 = g.getBoard();
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                assertEquals(b[i][j], b2[i][j]);
            }
        }
    }

    @Test
    public void testRotateCWNotNull() {
        int[][] b = {{2,0,2,2},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}};
        G2048 g = new G2048(b);
        g.rotateCW(b);

        int[][] bExpected = {{0,0,0,2},
            {0,0,0,0},
            {0,0,0,2},
            {0,0,0,2}};
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                assertEquals(b[i][j], bExpected[i][j]);
            }
        }
    }

    @Test
    public void testShiftDirectionInvalidInputs() {
        //null board
        G2048 g = new G2048();
        int[] output = g.shiftDirection("L", null);
        assertTrue(Arrays.equals(output, new int[2]));

        //null dir
        int[] output2 = g.shiftDirection(null, g.getBoard());
        assertTrue(Arrays.equals(output2, new int[2]));

        //wrong dir
        int[] output3 = g.shiftDirection("A", g.getBoard());
        assertTrue(Arrays.equals(output3, new int[2]));
    }

    @Test
    public void testShiftDirectionValidInputs() {
        int[][] bRight = {{2,0,2,2},
            {2,2,2,2},
            {0,2,2,4},
            {2,4,2,2}};
        G2048 g1 = new G2048(bRight);
        int[][] aRight = {{0,0,2,4},
            {0,0,4,4},
            {0,0,4,4},
            {0,2,4,4}};
        g1.shiftDirection("R", bRight);
        for (int i = 0; i < aRight.length; i++) {
            for (int j = 0; j < aRight[i].length; j++) {
                assertEquals(bRight[i][j], aRight[i][j]);
            }
        }
    }

    @Test
    public void testPlayTurnInvalidInput() {
        int[][] b = {{2,0,2,2},
            {2,2,2,2},
            {0,2,2,4},
            {2,4,2,2}};
        G2048 g = new G2048(b);
        assertFalse(g.playTurn("A"));
        for (int i = 0; i < b.length; i++) {
            assertTrue(Arrays.equals(g.getBoard()[i], b[i]));
        }
    }

    @Test
    public void testPlayTurnLRUD() {
        int[][] bRight = {{2,0,2,2},
            {2,2,2,2},
            {0,2,2,4},
            {2,4,2,2}};
        G2048 g1 = new G2048(bRight);
        int[][] aRight = {{0,0,2,4},
            {0,0,4,4},
            {0,0,4,4},
            {0,2,4,4}};
        g1.playTurn("R");
        for (int i = 0; i < aRight.length; i++) {
            for (int j = 0; j < aRight[i].length; j++) {
                if (aRight[i][j] != 0) {
                    //need this because playTurn adds a new block to one of the empty spaces
                    assertEquals(g1.getBoard()[i][j], aRight[i][j]);
                }
            }
        }

        int[][] bLeft = {{2,0,2,2},
            {2,2,2,2},
            {0,2,2,4},
            {2,4,2,2}};
        G2048 g2 = new G2048(bLeft);
        int[][] aLeft = {{4,2,0,0},
            {4,4,0,0},
            {4,4,0,0},
            {2,4,4,0}};
        g2.playTurn("L");

        for (int i = 0; i < aLeft.length; i++) {
            for (int j = 0; j < aLeft[i].length; j++) {
                if (aLeft[i][j] != 0) {
                    //need this because playTurn adds a new block to one of the empty spaces
                    assertEquals(g2.getBoard()[i][j], aLeft[i][j]);
                }
            }
        }

        int[][] bUp = {{2,0,2,2},
            {2,2,2,2},
            {0,2,2,4},
            {2,4,2,2}};
        G2048 g3 = new G2048(bUp);
        int[][] aUp = {{4,4,4,4},
            {2,4,4,4},
            {0,0,0,2},
            {0,0,0,0}};
        g3.playTurn("U");
        for (int i = 0; i < aUp.length; i++) {
            for (int j = 0; j < aUp[i].length; j++) {
                if (aUp[i][j] != 0) {
                    //need this because playTurn adds a new block to one of the empty spaces
                    assertEquals(g3.getBoard()[i][j], aUp[i][j]);
                }
            }
        }

        int[][] bDown = {{2,0,2,2},
            {2,2,2,2},
            {0,2,2,4},
            {2,4,2,2}};
        G2048 g4 = new G2048(bDown);
        int[][] aDown = {{0,0,0,0},
            {0,0,0,4},
            {2,4,4,4},
            {4,4,4,2}};
        g4.playTurn("D");
        for (int i = 0; i < aDown.length; i++) {
            for (int j = 0; j < aDown[i].length; j++) {
                if (aDown[i][j] != 0) {
                    //need this because playTurn adds a new block to one of the empty spaces
                    assertEquals(g4.getBoard()[i][j], aDown[i][j]);
                }
            }
        }
    }


}
