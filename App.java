import java.util.ArrayList;

public class App {
  String name;
  Boolean end = false;
  Boolean turn = true; // True: joueur 1, False: joueur 2
  Integer size;
  Integer mapSize;
  Board board;
  Echequier jeu = new Echequier();

  App(String name, Integer size) {
    this.name = name;
    this.size = size;
    this.mapSize = 2 * size - 1;
    this.board = new Board(size);

    // on dessine les hexagones
    jeu.echec(board.board, this.size);

    // etape 4 : on recupere les gemmes du board logique
    ArrayList<Cell> gems = this.board.getGems();

    // etape 5 : on les affiche sur le plateau graphique
    jeu.afficherGems(gems);
  }

  public void launch() {
    // Demande si le joueur joue contre IA ou joueur
    String type = IO.readln("Jouer contre IA (o/n): ");

    if (type.equals("o")) {
      // CONTRE IA (random pour le moment)
      IO.println("Joueur contre IA");
      while (!end) {
        playerPlays();
        IAPlays();
      }

    } else {
      // CONTRE JOUEUR
      IO.println("Joueur contre Joueur");

      board.show(name);
      while (!end) {
        playerPlays();
        // switch le tour du joueur
        this.turn = !this.turn;
      }
    }
  }

  public void IAPlays() {
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

  public void playerPlays() {
    int player = this.turn ? 1 : 2;
    int x = -1;
    int y = -1;
    boolean saisieValide = false;

    while (!saisieValide) {
      try {
        // 1. Lecture et conversion
        x = Integer.parseInt(IO.readln("Joueur " + player + " x (0-" + (board.mapSize - 1) + "): "));
        y = Integer.parseInt(IO.readln("Joueur " + player + " y (0-" + (board.mapSize - 1) + "): "));

        if (board.isValid(x, y)) {
          saisieValide = true;
        } else {
          System.out
              .println(Main.ROUGE + "Erreur : Coordonnées hors limites ou hors du plateau hexagonal !" + Main.RESET);
        }

      } catch (Exception e) {
        // Si l'utilisateur tape des lettres au lieu de chiffres
        System.out.println(Main.ROUGE + "Erreur : Veuillez entrer un nombre." + Main.RESET);
      }
    }

    // Une fois sorti de la boucle, on place le pion
    placePion(this.turn, x, y);

    // N'oublie pas de mettre à jour l'état de la cellule dans ton objet board !
    board.board[x][y].state = player;
  }

  public void placePion(Boolean turn, Integer x, Integer y) {
    // Modifications
    if (board.occupied(x, y)) {
      IO.println(Main.ROUGE + "Cette case est déjà prise par un joueur!" + Main.RESET);
      playerPlays();
    } else {
      board.board[x][y].clicked(turn);
      // Affichage
      jeu.placePion(turn, x, y);
    }

  }
}
