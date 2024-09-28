package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    private Move move;

    @BeforeEach
    void setUp() {
        // Initialize the Move object before each test
        move = new Move(3, 1); // Example: column 3, rotation 1
    }

    @Test
    void testSetSimulatedBoard() {
        int[][] simulatedBoard = {
                {0, 1, 0},
                {1, 0, 1},
                {0, 0, 0}
        };

        move.setSimulatedBoard(simulatedBoard);
        assertArrayEquals(simulatedBoard, move.getSimulatedBoard(), "Simulated board should be set and retrieved correctly.");
    }

    @Test
    void testSimulatedBoardWithEmptyArray() {
        // Test setting the simulated board with an empty array
        int[][] emptyBoard = new int[0][0];
        move.setSimulatedBoard(emptyBoard);
        assertArrayEquals(emptyBoard, move.getSimulatedBoard(), "Simulated board should be set to empty array correctly.");
    }
}
