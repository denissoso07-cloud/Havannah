import java.util.HashMap;

public class Board {
  HashMap<String, Cell> board = new HashMap<>();
  Integer size;

  Board(Integer size) {
    this.size = size;

    for (int y = size; y >= -size; y--) {
      for (int x = -size; x <= size; x++) {
        if (isValid(x, y)) {
          Cell cell = new Cell(x, y);
          this.board.put((x + "," + y), cell);
        }
      }
    }
  }

  boolean isValid(int x, int y) {
    return Math.abs(x + y) <= size;
  }

  public void show(String name) {
    IO.println(name + ":");
  }
}
