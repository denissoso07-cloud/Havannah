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

    this.board.put("0,0", new Cell(0, 0));
  }

  boolean isValid(int x, int y) {
    return Math.abs(x + y) <= size;
  }

  public void show(String name) {
    IO.println(name + ":");

    for (int y = size; y >= -size; y--) {

      int minX = Math.max(-size, -y - size);
      int maxX = Math.min(size, -y + size);

      // Décalage correct
      IO.print(" ".repeat(size - (maxX - minX + 1) / 2));

      for (int x = minX; x <= maxX; x++) {
        IO.print(". ");
      }

      IO.println();
    }
  }
}
