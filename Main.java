import java.util.ArrayList;

public class Main {
    // couleurs
    public static final String RESET = "\u001B[0m";
    public static final String ROUGE = "\u001B[31m";
    public static final String VERT = "\u001B[32m";
    public static final String JAUNE = "\u001B[33m";
    public static final String VIOLET = "\u001B[35m";

    void main() {
        App app = new App("Havannah", 5);
        app.launch();

        // on cree le plateau logique
        // c'est lui qui place les gemmes aleatoirement
        Board board = new Board(10);

        // on cree le plateau graphique
        echequier jeu = new echequier();

        // on dessine les hexagones
        jeu.echec();

        // etape 4 : on recupere les gemmes du board logique
        ArrayList<Cell> gems = board.getGems();

        // etape 5 : on les affiche sur le plateau graphique
        jeu.afficherGems(gems);

        // test  : pion J1 sur la case j=0, i=0
        double[] c1 = jeu.getCentre(0, 0);
        jeu.PionJ1(c1[0], c1[1]);

        // test  : pion J2 sur la case j=2, i=3
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
}
