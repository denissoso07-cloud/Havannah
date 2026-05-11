import java.util.ArrayList;
import java.util.HashMap;

public class Board {
  HashMap<String, Cell> board = new HashMap<>();
  Integer size;
  ArrayList<Structure> structures = new ArrayList<>(); // liste des structures actives

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
  // permet d'avoir les 6 voisins d'une case hexagonale
  public int[][] getVoisins(int x, int y) {
        return new int[][] {
            {x+1, y}, {x-1, y},   // gauche / droite
            {x, y+1}, {x, y-1},   // haut / bas
            {x+1, y-1}, {x-1, y+1} // diagonales gauche / droite
        };
    }

   // vérifie si 2 cases sont voisines
   public boolean sontVoisines(int x1, int y1, int x2, int y2) {
          int[][] voisins = getVoisins(x1, y1);
    for (int i = 0; i < voisins.length; i++) { //parcours des 6 voisins 
        if (voisins[i][0] == x2 && voisins[i][1] == y2) return true;
    }
    return false;
  }

    public boolean verifierTriangle(int x, int y, int joueur) {
    int[][] voisins = getVoisins(x, y);

    for (int i = 0; i < voisins.length; i++) {
        Cell v1 = board.get(voisins[i][0] + "," + voisins[i][1]);
        if (v1 != null && v1.state == joueur) { // vérifie si v1 est un voisin 

            for (int j = i + 1; j < voisins.length; j++) {
                Cell v2 = board.get(voisins[j][0] + "," + voisins[j][1]);
                if (v2 != null && v2.state == joueur) { // vérifie si v2 est un voisin 

                    //vérifie si v1 et v2 sont voisines
                    if (sontVoisines(voisins[i][0], voisins[i][1], voisins[j][0], voisins[j][1])) {
                        ArrayList<int[]> cases = new ArrayList<>(); // forme une nouvelle structure
                        cases.add(new int[]{x, y});
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
  public void addGems() {
    Integer gemNumber = 0;
    Integer x;
    Integer y;
    while (gemNumber < 10) {
      // Génère un nombre entre -5 et 5 inclus
      x = (int) (Math.random() * this.size) - this.size / 2;
      y = (int) (Math.random() * this.size) - this.size / 2;

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
