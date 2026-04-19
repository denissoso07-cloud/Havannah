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

        // test click cell
        Cell test = new Cell(4, 3);
        // premier click
        test.clicked(1);

        // joueur 2 essaye de clicker par dessus
        // ce qui fait une erreur
        test.clicked(2);
    }
}
