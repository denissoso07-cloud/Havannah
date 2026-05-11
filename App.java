import java.util.ArrayList;

public class App {
  String name;
  Boolean end = false;
  Boolean turn = true; // True: joueur 1, False: joueur 2
  Integer size;
  Board board;
  Echequier jeu = new Echequier();

  App(String name, Integer size) {
    this.name = name;
    this.size = size;
    this.board = new Board(size);

    // on dessine les hexagones
    jeu.echec(board.board, this.size);

    // etape 4 : on recupere les gemmes du board logique
    ArrayList<Cell> gems = this.board.getGems();

    // etape 5 : on les affiche sur le plateau graphique
    jeu.afficherGems(gems);

    jeu.placePion(true, 0, 0);

    jeu.placePion(false, 2, 3);
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
      Integer x = (int) (Math.random() * this.size);
      Integer y = (int) (Math.random() * this.size);

      if (board.isValid(x, y)) {
        casetrouvee = true;
        IO.println("IA a choisis: " + x + ", " + y);
        affichePion(false, x, y);
      }
    }

  }

  public void playerPlays() {
    Integer player;
    if (this.turn)
      player = 1;
    else {
      player = 2;
    }

    // exemple d'input
    // NE PAS OUBLIER: Enregistrer les inputs pour les placers dans le tableau.
    Integer x = Integer.valueOf(IO.readln("Joueur " + player + " x: "));
    Integer y = Integer.valueOf(IO.readln("Joueur " + player + " y: "));

    affichePion(this.turn, x, y);
  }

  public void affichePion(Boolean turn, Integer x, Integer y) {
    // double[] c = jeu.getCoords(x, y);
    jeu.placePion(turn, x, y);
  }
}
