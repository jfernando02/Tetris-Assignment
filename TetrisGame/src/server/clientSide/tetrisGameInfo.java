package server.clientSide;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class tetrisGameInfo {
    private int width;
    private int height;
    private String cells;
    private String currentShape;
    private String nextShape;

    private static ObjectMapper objectMapper = new ObjectMapper();  // For be able to read strings as Arrays later

    // Constructor for game info message layout
    public tetrisGameInfo(int width, int height, String cells, String currentShape, String nextShape) {
        this.width = width;
        this.height = height;
        this.cells = cells;
        this.currentShape = currentShape;
        this.nextShape = nextShape;
    }

    // Getters
    public int getWidth() {return width;}

    public int getHeight() {return height;}

    public String getCells() {return cells;}

    public String getCurrentShape() {return currentShape;}

    public String getNextShape() {return nextShape;}

    public int[][] parseCells() throws JsonProcessingException {
        return objectMapper.readValue(cells, int[][].class);
    }

    public int[][] parseCurrentShape() throws JsonProcessingException {
        return objectMapper.readValue(currentShape, int[][].class);
    }

    @Override
    public String toString() {
        return "tetrisGameInfo{" +
                "width=" + width +
                ", height=" + height +
                ", cells='" + cells + '\'' +
                ", currentShape='" + currentShape + '\'' +
                ", nextShape='" + nextShape + '\'' +
                '}';
    }
}
