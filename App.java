import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
  String name;
  Boolean end = false;
  Boolean turn = true; // True: joueur 1, False: joueur 2
  Integer size;
  Integer mapSize;
  Board board;
  Echequier jeu = new Echequier();
  Cell lastMove;

  // Score des deux joueurs
  int pointsJoueur1 = 0;
  int pointsJoueur2 = 0;

  int passesConsecutives = 0;

  /**
   * Constructeur : Initialise les dimensions, le plateau logique et
   * lance le dessin initial de la grille graphique.
   */
  App(String name, Integer size) {
    this.name = name;
    this.size = size;
    this.mapSize = 2 * size - 1;
    this.board = new Board(size, jeu);

    // on dessine les hexagones
    jeu.echec(board.board, this.size);

  }

  /**
   * Boucle principale du jeu.
   * Propose le choix du mode (IA ou Humain) et gère l'alternance des tours
   * jusqu'à ce que la condition 'end' soit vraie.
   */
  public void launch(String mode, boolean charger) {
    // 1. Gestion du chargement
    if (charger && !loadGame("sauvegarde.txt")) {
      IO.println("Aucune sauvegarde valide trouvée. Nouvelle partie...");
    }

    IO.println("Mode : Joueur contre " + (mode.equals("ia") ? "IA" : "Joueur"));

     while (!end) {
      playerPlays();

      if (!end && mode.equals("ia")) {
          IAPlays(lastMove);
        } else {
          this.turn = !this.turn;
        }
      }
    // 3. Nettoyage final
    supprimerSauvegarde("sauvegarde.txt");
  }

  public void supprimerSauvegarde(String filename) {
    try {
      // On convertit le nom du fichier en "Path" et on supprime
      Files.deleteIfExists(Paths.get(filename));
      IO.println("Fichier de sauvegarde nettoyé.");
    } catch (Exception e) {
      // On ne bloque pas le jeu si la suppression échoue (ex: fichier ouvert
      // ailleurs)
      IO.println("Note : Impossible de supprimer le fichier de sauvegarde.");
    }
  }

  public void saveGame(String filename) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
      // On sauvegarde d'abord les infos globales
      writer.println(pointsJoueur1 + " " + pointsJoueur2 + " " + (turn ? "1" : "2"));

      // On parcourt le plateau
      for (int x = 0; x < mapSize; x++) {
        for (int y = 0; y < mapSize; y++) {
          if (board.isValid(x, y)) {
            Cell c = board.board[x][y];
            // On enregistre : x y state gem
            writer.println(x + " " + y + " " + c.state + " " + c.gem);
          }
        }
      }
      IO.println("Partie sauvegardée dans " + filename);
    } catch (IOException e) {
      IO.println("Erreur lors de la sauvegarde : " + e.getMessage());
    }
  }

  public boolean loadGame(String filename) {
    File file = new File(filename);
    if (!file.exists())
      return false;

    try (Scanner scanner = new Scanner(file)) {
      if (!scanner.hasNext())
        return false;

      // 1. Charger les scores et le tour
      this.pointsJoueur1 = scanner.nextInt();
      this.pointsJoueur2 = scanner.nextInt();
      this.turn = (scanner.nextInt() == 1);

      // 2. Charger les cellules
      while (scanner.hasNextInt()) {
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        int state = scanner.nextInt();
        int gem = scanner.nextInt();

        if (board.isValid(x, y)) {
          board.board[x][y].state = state;
          board.board[x][y].gem = gem;

          // Mettre à jour le visuel si un pion était là
          if (state != 0) {
            jeu.placePion(state == 1, x, y);
          }
        }
      }
      IO.println("Ancienne partie chargée !");
      return true;
    } catch (Exception e) {
      IO.println("Erreur de lecture : " + e.getMessage());
      return false;
    }
  }

  /**
   * Logique de l'Intelligence Artificielle (niveau simple).
   * Choisit des coordonnées au hasard jusqu'à trouver une case valide et libre,
   * puis place son pion.
   */
  public void IAPlays(Cell cell) {
    ArrayList<int[]> casesValide = new ArrayList<int[]>();
    // recuperer les voisins valides
    for (int[] voisin : board.getVoisins(cell.x, cell.y)) {
      if (board.isValid(voisin[0], voisin[1]) && !board.occupied(voisin[0], voisin[1])) {
        casesValide.add(voisin);
      }
    }

    // Choisir une case random parmis les voisins
    if (casesValide.size() > 0) {
      int choix = (int) (Math.random() * casesValide.size());
      IO.println(choix + "taille: " + casesValide.size());
      int[] caseChoisie = casesValide.get(choix);
      IO.println(Main.VERT + "IA a choisis: " + caseChoisie[0] + ", " + caseChoisie[1] + Main.RESET);
      placePion(false, caseChoisie[0], caseChoisie[1]);
      return;
    }
    // Aucun voisins -> choix random
    chooseRandom();

  }

  /*
   * Choisis une case random
   */
  private void chooseRandom() {

    Boolean casetrouvee = false;
    while (!casetrouvee) {
      Integer x = (int) (Math.random() * this.mapSize);
      Integer y = (int) (Math.random() * this.mapSize);

      if (board.isValid(x, y) && !board.occupied(x, y)) {
        casetrouvee = true;
        IO.println(Main.VERT + "IA a choisis: " + x + ", " + y + Main.RESET);
        placePion(false, x, y);

      }
    }
    
  }


  /**
   * Gère l'interaction avec le joueur humain.
   * 1. Demande les coordonnées (x, y) ou le mot-clé 'pass'.
   * 2. Gère l'abandon si deux joueurs passent leur tour à la suite.
   * 3. Sécurise la saisie contre les erreurs (lettres, hors-plateau).
   */
  public void playerPlays() {
    int player = this.turn ? 1 : 2;
    int x = -1;
    int y = -1;
    boolean saisieValide = false;

    while (!saisieValide) {

      try {
        // 1. Lecture et conversion
        String inputX = IO.readln("Joueur " + player + " x (0-" + (board.mapSize - 1) + ") ou 'pass': ");
        if (inputX.equals("pass")) {
          passesConsecutives++;
          if (passesConsecutives >= 2) {
            IO.println(Main.VIOLET + "Deux passes consécutives. Fin de partie." + Main.RESET);
            end = true;
            afficherResultat();
          }
          return;
        }
        passesConsecutives = 0; // remet à zéro si le joueur joue vraiment
        x = Integer.parseInt(inputX);
        y = Integer.parseInt(IO.readln("Joueur " + player + " y (0-" + (board.mapSize - 1) + "): "));

        if (board.isValid(x, y)) {
          saisieValide = true;
        } else {
          IO.println(Main.ROUGE + "Erreur : Coordonnées hors limites ou hors du plateau hexagonal !" + Main.RESET);
        }

      } catch (Exception e) {
        // Si l'utilisateur tape des lettres au lieu de chiffres
        System.out.println(Main.ROUGE + "Erreur : Veuillez entrer un nombre." + Main.RESET);
      }
    }

    // Une fois sorti de la boucle, on place le pion
    placePion(this.turn, x, y);
    // (jouerCase est maintenant appelée depuis placePion, score inclus)
  }

  /**
   * Vérifie la disponibilité d'une case.
   * Si occupée, relance la saisie du joueur. Sinon, procède au placement.
   */
  public void placePion(Boolean turn, Integer x, Integer y) {
    // Modifications
    if (board.occupied(x, y)) {
      IO.println(Main.ROUGE + "Cette case est déjà prise par un joueur!" + Main.RESET);
      playerPlays();
    } else {
      jouerCase(turn, x, y);
    }

  }

  /**
   * 1. Met à jour l'état de la case (logique et visuel).
   * 2. Déclenche la recherche de nouvelles structures (Ligne, Triangle, Étoile).
   * 3. Active les réactions en chaîne et calcule les points via les gemmes.
   * 4. Met à jour les scores et vérifie les conditions de victoire (gemmes
   * épuisées ou plateau plein).
   */
  public void jouerCase(boolean tourJoueur1, int x, int y) {
    int joueur = tourJoueur1 ? 1 : 2;

    // Recuperer le dernier du joueur move pour l'ia
    lastMove = board.board[x][y];

    // On place le pion sur la cellule logique ET graphique
    board.board[x][y].clicked(tourJoueur1);
    jeu.placePion(tourJoueur1, x, y);

    // On mémorise combien de structures existaient avant ce coup
    int nbStructuresAvant = board.structures.size();

    // On vérifie les 3 types de structures
    board.verifierStructures(x, y, joueur);

    // On récupère les nouvelles structures formées ce tour
    ArrayList<Structure> nouvelles = new ArrayList<>();
    for (int i = nbStructuresAvant; i < board.structures.size(); i++) {
      nouvelles.add(board.structures.get(i));
      IO.println(Main.JAUNE + "Nouvelle structure : "
          + board.structures.get(i) + Main.RESET);
    }

    // Si au moins une nouvelle structure, on gère les déclenchements
    nouvelleStructureDetectee(joueur, nouvelles);

    // Affichage du score courant
    IO.println("Score -> Joueur 1 : " + pointsJoueur1
        + "  |  Joueur 2 : " + pointsJoueur2);

    // Sauvegarde a chaque coups, apres calculs des scores:
    saveGame("sauvegarde.txt");
    // Condition de fin : toutes les gemmes récoltées / plateau plein
    conditionDeFin();
  }

  private void nouvelleStructureDetectee(int joueur, ArrayList<Structure> nouvelles) {
    if (!nouvelles.isEmpty()) {
      int points = board.gererDeclenchements(joueur, nouvelles);
      if (points > 0) {
        IO.println(Main.VERT + "Joueur " + joueur
            + " gagne " + points + " point(s) !" + Main.RESET);
        if (joueur == 1) {
          pointsJoueur1 += points;
        } else {
          pointsJoueur2 += points;
        }
        ;
      }
    }
  }

  private void conditionDeFin() {
    if (board.toutesGemmesRecoltees()) {
      IO.println(Main.VIOLET + "Toutes les gemmes ont été récoltées !" + Main.RESET);
      end = true;
    } else if (board.estPlein()) {
      IO.println(Main.ROUGE + "Le plateau est plein ! Fin de la partie." + Main.RESET);

      // On affiche toutes les gemmes restantes sur l'interface graphique
      IO.println(Main.JAUNE + "Révélation des gemmes non récoltées..." + Main.RESET);
      jeu.afficherGems(board.getGems());

      end = true;
    }
    if (end == true) {
      afficherResultat();
    }
  }

  /**
   * Affiche le bilan final dans la console : scores et annonce du vainqueur.
   */
  public void afficherResultat() {
    IO.println(Main.VIOLET + "\n=== FIN DE PARTIE ===" + Main.RESET);
    IO.println("Joueur 1 : " + pointsJoueur1 + " point(s)");
    IO.println("Joueur 2 : " + pointsJoueur2 + " point(s)");

    if (pointsJoueur1 > pointsJoueur2) {
      IO.println(Main.VERT + "Joueur 1 gagne !" + Main.RESET);
    } else if (pointsJoueur2 > pointsJoueur1) {
      IO.println(Main.VERT + "Joueur 2 gagne !" + Main.RESET);
    } else {
      IO.println(Main.JAUNE + "Égalité !" + Main.RESET);
    }
  }
}
