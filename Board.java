import java.util.ArrayList;

public class Board {
  // Un tableau 2D au lieu de la HashMap
  Cell[][] board;
  int size; // Le nombre d'hexagones sur un côté (ex: 5)
  int mapSize; // La dimension du tableau (2 * size - 1)
  ArrayList<Structure> structures = new ArrayList<>(); // liste des structures actives

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

  // permet d'avoir les 6 voisins d'une case hexagonale
  public int[][] getVoisins(int x, int y) {
    return new int[][] {
        { x + 1, y }, { x - 1, y }, // gauche / droite
        { x, y + 1 }, { x, y - 1 }, // haut / bas
        { x + 1, y - 1 }, { x - 1, y + 1 } // diagonales gauche / droite
    };
  }

  // vérifie si 2 cases sont voisines
  public boolean sontVoisines(int x1, int y1, int x2, int y2) {
    int[][] voisins = getVoisins(x1, y1);
    for (int i = 0; i < voisins.length; i++) { // parcours des 6 voisins
      if (voisins[i][0] == x2 && voisins[i][1] == y2)
        return true;
    }
    return false;
  }

  public boolean verifierTriangle(int x, int y, int joueur) {
    int[][] voisins = getVoisins(x, y);

    for (int i = 0; i < voisins.length; i++) {
      Cell v1 = board[voisins[i][0]][voisins[i][1]];
      if (v1 != null && v1.state == joueur) { // vérifie si v1 est un voisin

        for (int j = i + 1; j < voisins.length; j++) {
          Cell v2 = board[voisins[j][0]][voisins[j][1]];
          if (v2 != null && v2.state == joueur) { // vérifie si v2 est un voisin

            // vérifie si v1 et v2 sont voisines
            if (sontVoisines(voisins[i][0], voisins[i][1], voisins[j][0], voisins[j][1])) {
              ArrayList<int[]> cases = new ArrayList<>(); // forme une nouvelle structure
              cases.add(new int[] { x, y });
              cases.add(voisins[i]);
              cases.add(voisins[j]);
              structures.add(new Structure("triangle", joueur, cases));
              return true;
            }
          }
        }
      }
    }
    return false;
  }

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