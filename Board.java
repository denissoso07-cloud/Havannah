import java.util.ArrayList;

public class Board {
  // Un tableau 2D au lieu de la HashMap
  Cell[][] board;
  int size; // Le nombre d'hexagones sur un côté (ex: 5)
  int mapSize; // La dimension du tableau (2 * size - 1)

  Board(int size) {
    this.size = size;
    this.mapSize = 2 * size - 1; // Pour size 5, mapSize = 9
    this.board = new Cell[mapSize][mapSize];

    for (int x = 0; x < mapSize; x++) {
      for (int y = 0; y < mapSize; y++) {
        if (isValid(x, y)) {
          this.board[x][y] = new Cell(x, y);
        }
      }
    }
    addGems();
  }

  // La formule magique pour couper les coins du losange et faire un hexagone
  boolean isValid(int x, int y) {
    int center = size - 1;
    // On vérifie la distance par rapport au centre sur les 3 axes
    return Math.abs(x - center) < size &&
        Math.abs(y - center) < size &&
        Math.abs((x - center) - (y - center)) < size;
  }

  public void addGems() {
    int gemNumber = 0;
    int x;
    int y;
    while (gemNumber < 10) {
      x = (int) (Math.random() * (size + 1));
      y = (int) (Math.random() * (size + 1));

      // Accès direct avec le tableau [x][y]
      if (isValid(x, y) && this.board[x][y] != null && isNotGem(x, y)) {
        gemNumber++;
        this.board[x][y].setGem((int) (Math.random() * 2) + 1);
      }
    }
  }

  public ArrayList<Cell> getGems() {
    ArrayList<Cell> gems = new ArrayList<>();
    // Double boucle pour parcourir le tableau 2D
    for (int x = 0; x <= size; x++) {
      for (int y = 0; y <= size; y++) {
        Cell cell = board[x][y];
        if (cell != null && cell.gem != 0) {
          gems.add(cell);
        }
      }
    }
    return gems;
  }

  boolean isNotGem(int x, int y) {
    return this.board[x][y].gem == 0;
  }

  public void show(String name) {
    System.out.println(Main.VIOLET + name + ":" + Main.RESET);
  }
}