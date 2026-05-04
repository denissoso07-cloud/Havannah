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
    this.board = new Board(5);

    // on dessine les hexagones
    jeu.echec();

    // etape 4 : on recupere les gemmes du board logique
    ArrayList<Cell> gems = this.board.getGems();

    // etape 5 : on les affiche sur le plateau graphique
    jeu.afficherGems(gems);

    // test : pion J1 sur la case j=0, i=0
    double[] c1 = jeu.getCentre(0, 0);
    jeu.PionJ1(c1[0], c1[1]);

    // test : pion J2 sur la case j=2, i=3
    double[] c2 = jeu.getCentre(2, 3);
    jeu.PionJ2(c2[0], c2[1]);

    // test click cell
    Cell test = new Cell(4, 3);
    // premier click
    test.clicked(1);

    // joueur 2 essaye de clicker par dessus
    // ce qui fait une erreur
    test.clicked(2);
  }

  public void launch() {
    // compteur temporaire pour faire 4 moves
    int moves = 0;
    while (!end) {
      board.show(name);
      // showMoves();
      play();
      moves++;
      if (moves > 4) {
        end = true;
      }
    }
  }

  public void play() {
    Integer player;
    if (this.turn)
      player = 1;
    else
      player = 2;

    // exemple d'input
    // NE PAS OUBLIER: Enregistrer les inputs pour les placers dans le tableau.
    Integer x = Integer.valueOf(IO.readln("Joueur " + player + " x: "));
    Integer y = Integer.valueOf(IO.readln("Joueur " + player + " y: "));

    double[] c = jeu.getCentre(x, y);
    if (player == 1) {
      jeu.PionJ1(c[0], c[1]);
    } else {
      jeu.PionJ2(c[0], c[1]);
    }

    this.turn = !this.turn;

  }
}
