import java.util.ArrayList;

public class Board {
  Cell[][] board; // Un tableau 2D au lieu de la HashMap
  int size; // Le nombre d'hexagones sur un côté (ex: 5)
  int mapSize;// La dimension du tableau (2 * size - 1)
  ArrayList<Structure> structures = new ArrayList<>(); // liste des structures actives
  Echequier jeu;

  Board(int size, Echequier jeu) {
    this.size = size;
    this.mapSize = 2 * size - 1; // Pour size 5, mapSize = 9
    this.board = new Cell[mapSize][mapSize];
    this.jeu = jeu;
    for (int x = 0; x < mapSize; x++) {
      for (int y = 0; y < mapSize; y++) {
        if (isValid(x, y)) {
          this.board[x][y] = new Cell(x, y);
        }
      }
    }
    addGems();
  }

  /*
   * Retourne les 6 voisins d'une case hexagonale.
   * Ces directions correspondent à getCoords() dans Echequier :
   * cx = startX + x * 1.5 * radius
   * cy = startY + y * sqrt(3)*radius - x * sqrt(3)*radius/2
   * Les 6 voisins adjacents sont ceux à distance sqrt(3)*radius.
   */
  public int[][] getVoisins(int x, int y) {
    return new int[][] {
        { x + 1, y }, // droite-haut
        { x - 1, y }, // gauche-bas
        { x, y + 1 }, // bas
        { x, y - 1 }, // haut
        { x + 1, y + 1 }, // diagonale bas-droite
        { x - 1, y - 1 } // diagonale haut-gauche
    };
  }

  /**
   * Vérifie si deux cases données sont physiquement adjacentes sur le plateau.
   */
  public boolean sontVoisines(int x1, int y1, int x2, int y2) {
    int[][] voisins = getVoisins(x1, y1);
    for (int i = 0; i < voisins.length; i++) {
      if (voisins[i][0] == x2 && voisins[i][1] == y2)
        return true;
    }
    return false;
  }

  /*
   * Crée la structure triangle si vi et vj sont voisins entre eux.
   * Retourne true si le triangle est formé.
   */
  public boolean formerTriangle(int x, int y, int[] vi, int[] vj, int joueur) {
    if (sontVoisines(vi[0], vi[1], vj[0], vj[1])) {
      ArrayList<int[]> cases = new ArrayList<>();
      cases.add(new int[] { x, y });
      cases.add(vi);
      cases.add(vj);
      structures.add(new Structure("triangle", joueur, cases));
      IO.println(Main.VERT + "Triangle formé !" + Main.RESET);
      return true;
    }
    return false;
  }

  /*
   * TRIANGLE : 3 cases du même joueur toutes voisines entre elles.
   * On cherche 2 voisins v1 et v2 du joueur qui sont aussi voisins entre eux.
   */
  public boolean verifierTriangle(int x, int y, int joueur) {
    int[][] voisins = getVoisins(x, y);
    for (int i = 0; i < voisins.length; i++) {
      if (isValid(voisins[i][0], voisins[i][1])) {
        Cell v1 = board[voisins[i][0]][voisins[i][1]];
        if (v1 != null && v1.state == joueur) {// vérifie si v1 est un voisin
          for (int j = i + 1; j < voisins.length; j++) {
            if (isValid(voisins[j][0], voisins[j][1])) {
              Cell v2 = board[voisins[j][0]][voisins[j][1]];
              if (v2 != null && v2.state == joueur) {// vérifie si v2 est un voisin
                if (formerTriangle(x, y, voisins[i], voisins[j], joueur))
                  return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  /*
   * Cherche des cases alignées dans une direction (dx, dy).
   * S'arrête dès qu'une case est vide, ennemie, ou hors du plateau.
   */
  public void chercherDansDirection(ArrayList<int[]> liste, int x, int y, int dx, int dy, int joueur) {
    for (int step = 1; step <= 4; step++) {
      int nx = x + dx * step;
      int ny = y + dy * step;
      if (isValid(nx, ny)) {
        Cell c = board[nx][ny];
        if (c != null && c.state == joueur) {
          liste.add(new int[] { nx, ny });
        } else {
          break;
        }
      } else {
        break;
      }
    }
  }

  /*
   * LIGNE : 5 cases alignées du même joueur sur un des 3 axes :
   * Axe 1 : Vertical (0,+1) <-> (0,-1)
   * Axe 2 : Horizontal (+1,0) <-> (-1,0)
   * Axe 3 : Diagonale (+1,+1) <-> (-1,-1)
   */
  public boolean verifierLigne(int x, int y, int joueur) {
    // Axe 1 : Vertical
    ArrayList<int[]> ligneV = new ArrayList<>();
    ligneV.add(new int[] { x, y });
    chercherDansDirection(ligneV, x, y, 0, 1, joueur);
    chercherDansDirection(ligneV, x, y, 0, -1, joueur);
    if (ligneV.size() >= 5) {
      structures.add(new Structure("ligne", joueur, ligneV));
      IO.println(Main.VERT + "Ligne verticale formée !" + Main.RESET);
      return true;
    }
    // Axe 2 : Horizontal
    ArrayList<int[]> ligneH = new ArrayList<>();
    ligneH.add(new int[] { x, y });
    chercherDansDirection(ligneH, x, y, 1, 0, joueur);
    chercherDansDirection(ligneH, x, y, -1, 0, joueur);
    if (ligneH.size() >= 5) {
      structures.add(new Structure("ligne", joueur, ligneH));
      IO.println(Main.VERT + "Ligne horizontale formée !" + Main.RESET);
      return true;
    }
    // Axe 3 : Diagonale bas-droite <-> haut-gauche
    ArrayList<int[]> ligneD = new ArrayList<>();
    ligneD.add(new int[] { x, y });
    chercherDansDirection(ligneD, x, y, 1, 1, joueur);
    chercherDansDirection(ligneD, x, y, -1, -1, joueur);
    if (ligneD.size() >= 5) {
      structures.add(new Structure("ligne", joueur, ligneD));
      IO.println(Main.VERT + "Ligne diagonale formée !" + Main.RESET);
      return true;
    }
    return false;
  }

  /**
   * Définit les coordonnées des 6 cases adjacentes dans une grille hexagonale
   * basée sur le système de coordonnées utilisé par l'Echequier.
   */
  public boolean estCentreEtoile(int cx, int cy, int joueur) {
    int[][] voisins = getVoisins(cx, cy);
    int count = 0;
    for (int i = 0; i < voisins.length; i++) {
      if (isValid(voisins[i][0], voisins[i][1])) {
        Cell c = board[voisins[i][0]][voisins[i][1]];
        if (c != null && c.state == joueur)
          count++;
      }
    }
    return count == 6;
  }

  /*
   * Crée la structure étoile autour du centre (cx, cy).
   * cases[0] = le centre, cases[1..6] = les 6 pions du joueur.
   */
  public boolean formerEtoile(int cx, int cy, int joueur) {
    if (isValid(cx, cy) && estCentreEtoile(cx, cy, joueur)) {
      ArrayList<int[]> cases = new ArrayList<>();
      cases.add(new int[] { cx, cy }); // centre (index 0)
      int[][] v = getVoisins(cx, cy);
      for (int j = 0; j < v.length; j++) {
        cases.add(v[j]);
      }
      structures.add(new Structure("etoile", joueur, cases));
      IO.println(Main.VERT + "Étoile formée autour de (" + cx + "," + cy + ") !" + Main.RESET);
      return true;
    }
    return false;
  }

  /*
   * ÉTOILE : 6 pions du joueur autour d'une case centrale.
   * Quand on place en (x,y), on vérifie si un voisin est maintenant
   * entouré de 6 pions du joueur.
   */
  public boolean verifierEtoile(int x, int y, int joueur) {
    int[][] voisins = getVoisins(x, y);
    for (int i = 0; i < voisins.length; i++) {
      if (formerEtoile(voisins[i][0], voisins[i][1], joueur))
        return true;
    }
    return false;
  }

  // Point d'entrée : appelé après chaque coup
  public void verifierStructures(int x, int y, int joueur) {
    verifierTriangle(x, y, joueur);
    verifierLigne(x, y, joueur);
    verifierEtoile(x, y, joueur);
  }

  // ══════════════════════════════════════
  // DÉCLENCHEMENT (règles 9, 10, 13)
  // ══════════════════════════════════════

  /*
   * Vérifie si deux structures sont reliées :
   * elles partagent une case, ou deux de leurs cases sont voisines.
   */
  public boolean sontReliees(Structure s1, Structure s2) {
    for (int i = 0; i < s1.cases.size(); i++) {
      for (int j = 0; j < s2.cases.size(); j++) {
        int[] c1 = s1.cases.get(i);
        int[] c2 = s2.cases.get(j);
        if (c1[0] == c2[0] && c1[1] == c2[1])
          return true;
        if (sontVoisines(c1[0], c1[1], c2[0], c2[1]))
          return true;
      }
    }
    return false;
  }

  /*
   * Réaction en chaîne (règle 10) :
   * Déclenche une structure puis propage aux structures reliées.
   * Fonction récursive : elle s'appelle elle-même.
   */
  public void declencherChaine(Structure depart, int joueur) {
    if (!depart.declenchee) { // règle 13 : déclenchée qu'une seule fois
      depart.declenchee = true;
      IO.println(Main.JAUNE + "Structure déclenchée : " + depart + Main.RESET);
      for (int i = 0; i < structures.size(); i++) {
        Structure autre = structures.get(i);
        // on regarde parmi toutes les structures
        if (autre != depart && autre.joueur == joueur && !autre.declenchee) {
          if (sontReliees(depart, autre)) {
            declencherChaine(autre, joueur); // appel récursif
          }
        }
      }
    }
  }

  /*
   * Gère les déclenchements (règle 9) :
   * CAS A : plusieurs structures ce tour -> toutes déclenchées
   * CAS B : une seule structure reliée à une autre du joueur -> déclenchée
   */
  public int gererDeclenchements(int joueur, ArrayList<Structure> nouvelles) {
    if (nouvelles.size() >= 2) { // CAS A
      for (int i = 0; i < nouvelles.size(); i++) {
        declencherChaine(nouvelles.get(i), joueur);
      }
    } else if (nouvelles.size() == 1) { // CAS B
      Structure nouvelle = nouvelles.get(0);
      for (int j = 0; j < structures.size(); j++) {
        Structure autre = structures.get(j);
        if (autre != nouvelle && autre.joueur == joueur && sontReliees(nouvelle, autre)) {
          declencherChaine(nouvelle, joueur);
          break;
        }
      }
    }
    // Collecter et attribuer les points avec la règle du double (règle 12)
    ArrayList<int[]> casesRevelees = collecterCasesRevelees(nouvelles);
    return attribuerPoints(casesRevelees);
  }

  // ══════════════════════════════════════
  // GEMMES (règles 11, 12)
  // ══════════════════════════════════════

  /*
   * Retourne les cases à révéler pour une structure déclenchée (règle 11) :
   * triangle -> case adjacente aléatoire
   * ligne -> toutes les cases adjacentes à la ligne
   * étoile -> la case centrale (index 0)
   */
  public ArrayList<int[]> getCasesRevelees(Structure s) {
    ArrayList<int[]> cases = new ArrayList<>();
    if (s.type.equals("triangle")) {
      cases.add(s.cases.get((int) (Math.random() * s.cases.size())));
    } else if (s.type.equals("etoile")) {
      cases.add(s.cases.get(0)); // index 0 = centre
    } else if (s.type.equals("ligne")) {
      for (int i = 0; i < s.cases.size(); i++) {
        int[][] voisins = getVoisins(s.cases.get(i)[0], s.cases.get(i)[1]);
        for (int j = 0; j < voisins.length; j++) {
          cases.add(voisins[j]);
        }
      }
    }
    return cases;
  }

  /*
   * Collecte toutes les cases à révéler pour toutes les structures déclenchées.
   * Les doublons sont gardés intentionnellement pour la règle 12.
   */
  public ArrayList<int[]> collecterCasesRevelees(ArrayList<Structure> nouvelles) {
    ArrayList<int[]> toutes = new ArrayList<>();
    for (int i = 0; i < nouvelles.size(); i++) {
      if (nouvelles.get(i).declenchee) {
        ArrayList<int[]> cases = getCasesRevelees(nouvelles.get(i));
        for (int j = 0; j < cases.size(); j++) {
          toutes.add(cases.get(j));
        }
      }
    }
    return toutes;
  }

  /*
   * Compte combien de fois la case (x,y) apparaît dans la liste.
   * Utilisé pour la règle 12 (gemme compte double si révélée 2 fois).
   */
  public int compterOccurrences(ArrayList<int[]> liste, int x, int y) {
    int count = 0;
    for (int i = 0; i < liste.size(); i++) {
      if (liste.get(i)[0] == x && liste.get(i)[1] == y)
        count++;
    }
    return count;
  }

  /*
   * Attribue les points pour toutes les cases révélées.
   * Règle 12 : si une case est révélée plusieurs fois, la gemme compte double.
   * Exemple : gem=2, révélée 2 fois → 2*2 = 4 points.
   */
  public int attribuerPoints(ArrayList<int[]> casesRevelees) {
    int total = 0;
    for (int i = 0; i < casesRevelees.size(); i++) {
      int x = casesRevelees.get(i)[0];
      int y = casesRevelees.get(i)[1];
      if (isValid(x, y) && board[x][y] != null && board[x][y].gem != 0) {
        int nbFois = compterOccurrences(casesRevelees, x, y);
        int points = board[x][y].gem * nbFois;
        IO.println(
            Main.JAUNE + "Gemme (" + x + "," + y + ") révélée " + nbFois + "x → +" + points + " pts" + Main.RESET);
        jeu.dessinerGem(x, y, board[x][y].gem);
        board[x][y].gem = 0; // la gemme est prise
        total += points;
      }
    }
    return total;
  }

  // ══════════════════════════════════════
  // CONDITIONS DE FIN (règle 14)
  // ══════════════════════════════════════

  public boolean toutesGemmesRecoltees() {
    return getGems().isEmpty();
  }

  public boolean estPlein() {
    for (int x = 0; x < mapSize; x++) {
      for (int y = 0; y < mapSize; y++) {
        if (isValid(x, y) && board[x][y].state == 0)
          return false;
      }
    }
    return true;
  }

  // ══════════════════════════════════════
  // UTILITAIRES
  // ══════════════════════════════════════

  boolean occupied(int x, int y) {
    return board[x][y].state != 0;
  }

  boolean isValid(int x, int y) {
    if (x < 0 || x >= mapSize || y < 0 || y >= mapSize)
      return false;
    int center = size - 1;
    int q = x - center;
    int r = y - center;
    return Math.abs(q) < size && Math.abs(r) < size && Math.abs(q - r) < size;
  }

  public void addGems() {
    int gemNumber = 0;
    while (gemNumber < 10) {
      int x = (int) (Math.random() * mapSize);
      int y = (int) (Math.random() * mapSize);
      if (isValid(x, y) && board[x][y] != null && isNotGem(x, y)) {
        board[x][y].setGem((int) (Math.random() * 2) + 1);
        gemNumber++;
      }
    }
  }

  /**
   * Parcourt l'intégralité du tableau 2D pour identifier les cellules contenant
   * une gemme.
   * 
   * @return Une liste d'objets Cell contenant toutes les gemmes encore présentes
   *         sur le plateau.
   */
  public ArrayList<Cell> getGems() {
    ArrayList<Cell> gems = new ArrayList<>();
    for (int x = 0; x < mapSize; x++) {
      for (int y = 0; y < mapSize; y++) {
        if (board[x][y] != null && board[x][y].gem != 0)
          gems.add(board[x][y]);
      }
    }
    return gems;
  }

  /**
   * Vérifie si une cellule spécifique (x, y) est dépourvue de gemme.
   * 
   * @return true si la case ne contient aucune gemme (valeur 0), false sinon.
   */
  boolean isNotGem(int x, int y) {
    return board[x][y].gem == 0;
  }

  /**
   * Affiche le nom de la partie ou du plateau dans la console.
   */
  public void show(String name) {
    System.out.println(Main.VIOLET + name + ":" + Main.RESET);
  }
}