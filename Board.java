import java.util.ArrayList;
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
    addGems();
  }

  public void addGems() {
    Integer gemNumber = 0;
    Integer x;
    Integer y;
    while (gemNumber < 10) {
      // Génère un nombre entre -5 et 5 inclus
      x = (int) (Math.random() * 10) - 5;
      y = (int) (Math.random() * 10) - 5;

      if (isValid(x, y) && isNotGem(x, y)) {
        gemNumber++;
        // On garde ta logique pour le type de gemme (1 ou 2)
        this.board.get(x + "," + y).setGem((int) (Math.random() * 2) + 1);
      }
    }
  }

  // on recupere toutes les cellules qui ont une gemme
  public ArrayList<Cell> getGems() {
    ArrayList<Cell> gems = new ArrayList<>();
    for (Cell cell : board.values()) {
      if (cell.gem != 0) {
        gems.add(cell);
      }
    }
    return gems;
  }

  boolean isNotGem(int x, int y) {
    return this.board.get(x + "," + y).gem == 0;
  }

  boolean isValid(int x, int y) {
    return Math.abs(x + y) <= size;
  }

  public void show(String name) {
    IO.println(Main.VIOLET + name + ":" + Main.RESET);
  }
}
