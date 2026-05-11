import java.util.ArrayList;

public class Board {
  // Un tableau 2D au lieu de la HashMap
  Cell[][] board;
  int size; // Le nombre d'hexagones sur un côté (ex: 5)
  int mapSize; // La dimension du tableau (2 * size - 1)
  ArrayList<Structure> structures = new ArrayList<>(); // liste des structures actives
  Echequier jeu;

  Board(int size,Echequier jeu) {
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

  // permet d'avoir les 6 voisins d'une case hexagonale
  public int[][] getVoisins(int x, int y) {
    return new int[][] {
        { x + 1, y }, { x - 1, y }, // droite / gauche
        { x, y + 1 }, { x, y - 1 }, // bas / haut
        { x + 1, y - 1 }, { x - 1, y + 1 } // diagonales haut-droit / bas-gauche
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
      if (!isValid(voisins[i][0], voisins[i][1])) continue;
      Cell v1 = board[voisins[i][0]][voisins[i][1]];
      if (v1 != null && v1.state == joueur) { // vérifie si v1 est un voisin

        for (int j = i + 1; j < voisins.length; j++) {
          if (!isValid(voisins[j][0], voisins[j][1])) continue;
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

public void chercherDansDirection(ArrayList<int[]> liste, int x, int y, int dx, int dy, int joueur) {
    for (int step = 1; step <= 4; step++) {
        int nx = x + (dx * step);
        int ny = y + (dy * step);
        
        if (isValid(nx, ny)) {
            Cell c = board[nx][ny];
            if (c != null && c.state == joueur) {
                liste.add(new int[]{nx, ny});
            } else {
                break; // On a trouvé une case vide ou un ennemi, la ligne s'arrête
            }
        } else {
            break; // On est sorti du plateau
        }
    }
}

public boolean verifierLigne(int x, int y, int joueur) {
    
    //AXE VERTICAL (Haut <-> Bas)
    ArrayList<int[]> ligneV = new ArrayList<>();
    ligneV.add(new int[]{x, y});
    chercherDansDirection(ligneV, x, y, 0, 1, joueur);  // Chercher en bas
    chercherDansDirection(ligneV, x, y, 0, -1, joueur); // Chercher en haut
    if (ligneV.size() >= 5) {
        structures.add(new Structure("ligne", joueur, ligneV));
        return true;
    }

    //AXE DIAGONAL
    ArrayList<int[]> ligneD = new ArrayList<>();
    ligneD.add(new int[]{x, y});
    chercherDansDirection(ligneD, x, y, 1, -1, joueur); // Chercher Diagonale haut-droite
    chercherDansDirection(ligneD, x, y, -1, 1, joueur); // Chercher Diagonale bas-gauche
    chercherDansDirection(ligneD, x, y, -1, -1, joueur); // Chercher Diagonale haut-gauche
    chercherDansDirection(ligneD, x, y, 1, 1, joueur); // Chercher Diagonale bas-droit
    if (ligneD.size() >= 5) {
        structures.add(new Structure("ligne", joueur, ligneD));
        return true;
    }

    return false;
}


// verifierEtoile
public boolean verifierEtoile(int x, int y, int joueur) {
    int[][] voisins = getVoisins(x, y);
    ArrayList<int[]> cases = new ArrayList<>();
    cases.add(new int[]{x, y});
    for (int i = 0; i < voisins.length; i++) {
        if (!isValid(voisins[i][0], voisins[i][1])) continue;
        Cell c = board[voisins[i][0]][voisins[i][1]];
        if (c != null && c.state == joueur) {
          cases.add(voisins[i]);}
    }
    if (cases.size() == 7) {
        structures.add(new Structure("etoile", joueur, cases));
        return true;
    }
    return false;

} 

public void verifierStructures(int x, int y, int joueur) {
    verifierTriangle(x, y, joueur);
    verifierLigne(x, y, joueur);
    verifierEtoile(x, y, joueur);
}

public int gererDeclenchements(int joueur, ArrayList<Structure> nouvellesStructures) {
    // CONDITION A : Plusieurs structures ce tour-ci -> On déclenche tout le groupe
    if (nouvellesStructures.size() >= 2) {
        for (Structure s : nouvellesStructures) {
            declencherChaine(s, joueur);
        }
    } 
    // CONDITION B : Une seule structure ce tour -> On regarde si elle touche une ANCIENNE structure active
    else if (nouvellesStructures.size() == 1) {
        Structure nouvelle = nouvellesStructures.get(0);
        for (Structure existante : structures) {
            if (existante != nouvelle && existante.joueur == joueur && sontReliees(nouvelle, existante)) {
                // Si l'existante est active, la nouvelle se déclenche et propage
                declencherChaine(nouvelle, joueur);
                break;
            }
        }
    }

    // on comptabilise les points uniquement des nouvelles structures declenchees
    int pointsGagnes = 0;
    for (int i = 0; i < nouvellesStructures.size(); i++) {
        Structure s = nouvellesStructures.get(i);
        if (s.declenchee) {
            pointsGagnes += revelerGemmes(s);
        }
    }
    return pointsGagnes;
}

public int revelerGemmes(Structure s) {
    int points = 0;

    if (s.type.equals("triangle")) {
        // une case adjacente choisie aleatoirement
        int[] c = s.cases.get((int)(Math.random() * s.cases.size()));
        points += prendrGemme(c[0], c[1]);

    } else if (s.type.equals("ligne")) {
        // toutes les cases adjacentes a la ligne
        for (int i = 0; i < s.cases.size(); i++) {
            int[][] voisins = getVoisins(s.cases.get(i)[0], s.cases.get(i)[1]);
            for (int j = 0; j < voisins.length; j++) {
                points += prendrGemme(voisins[j][0], voisins[j][1]);
            }
        }

    } else if (s.type.equals("etoile")) {
        // uniquement la case centrale (index 0)
        int[] centre = s.cases.get(0);
        points += prendrGemme(centre[0], centre[1]);
    }

    return points;
}

// prend la gemme d'une case et retourne ses points
public int prendrGemme(int x, int y) {
    if (!isValid(x, y)) return 0;
    Cell cell = board[x][y];
    if (cell == null || cell.gem == 0) return 0;

    int points = cell.gem; // 1 ou 2 selon la rarete
    IO.println(Main.JAUNE + "Gemme revelee en (" + x + "," + y + ") → +" + points + " pts" + Main.RESET);

    jeu.dessinerGem(x, y, points);

    cell.gem = 0; // la gemme est prise
    return points;
}
    /*
     * Vérifie si deux structures sont "reliées" :
     * elles partagent une case, ou deux de leurs cases sont voisines.
     */
    private boolean sontReliees(Structure s1, Structure s2) {
        for (int[] c1 : s1.cases) {
            for (int[] c2 : s2.cases) {
                // Cases identiques
                if (c1[0] == c2[0] && c1[1] == c2[1]) return true;
                // Cases voisines
                if (sontVoisines(c1[0], c1[1], c2[0], c2[1])) return true;
            }
        }
        return false;
    }
 
    /*
     * Réaction en chaîne : déclenche une structure,
     * puis propage le déclenchement à toutes les structures reliées du même joueur
     * qui ne sont pas encore déclenchées.
     */
    private void declencherChaine(Structure depart, int joueur) {
        if (depart.declenchee) return; // déjà déclenchée, on arrête
 
        depart.declenchee = true;
        IO.println(Main.JAUNE + "Structure déclenchée : " + depart + Main.RESET);
 
        // Propagation : on cherche toutes les structures reliées non encore déclenchées
        for (Structure autre : structures) {
            if (autre.joueur != joueur) continue;
            if (autre.declenchee) continue;
            if (autre == depart) continue;
 
            if (sontReliees(depart, autre)) {
                declencherChaine(autre, joueur); // appel récursif = réaction en chaîne
            }
        }
    }

    public boolean toutesGemmesRecoltees() {
        return getGems().isEmpty();
    }

    public boolean estPlein() {
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                if (isValid(x, y) && board[x][y].state == 0) {
                    return false; // Il reste au moins une case vide
                }
            }
        }
        return true; // Toutes les cases valides sont occupées
    }
  boolean occupied(int x, int y) {
    return board[x][y].state != 0;
  }

  boolean isValid(int x, int y) {
    // 1. Sécurité : Vérifier que x et y ne sont pas hors du tableau
    // (IndexOutOfBounds)
    if (x < 0 || x >= mapSize || y < 0 || y >= mapSize) {
      return false;
    }

    // 2. Géométrie : Formule de l'hexagone centrée
    int center = size - 1;
    int q = x - center;
    int r = y - center;

    // On vérifie les 3 axes de coordonnées axiales
    return Math.abs(q) < size &&
        Math.abs(r) < size &&
        Math.abs(q - r) < size;
  }

  public void addGems() {
    int gemNumber = 0;
    int x;
    int y;
    while (gemNumber < 10) {
      x = (int) (Math.random() * mapSize);
      y = (int) (Math.random() * mapSize);

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
    for (int x = 0; x < mapSize; x++) {
      for (int y = 0; y < mapSize; y++) {
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