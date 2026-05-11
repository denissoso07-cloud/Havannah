import java.util.ArrayList;

public class Echequier {
    double radius = 20;
    // Math.sqrt(3) * radius est la distance exacte entre deux centres d'hexagones
    double h = Math.sqrt(3) * radius;
    double w = 1.5 * radius; // Les colonnes se chevauchent horizontalement

    double startX = 100;
    double startY = 200;
    int size; // Un Havannah de taille 5 a des coordonnées de -4 à 4

    public void echec(Cell[][] board, int size) {
        this.size = size;
        // On parcourt tout le tableau 2D (0 à 10 si size est 10)
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] != null) {
                    double[] coords = getCoords(x, y);
                    Hexagone box = new Hexagone(coords[0], coords[1], radius);
                    box.draw();
                }
            }
        }
    }

    public double[] getCoords(int x, int y) {
        // 1. Calcul de X : les colonnes sont espacées de 1.5 * radius
        double cx = startX + x * w;

        // 2. Calcul de Y : On ajoute un décalage vertical (x * h / 2)
        // pour créer la structure en nid d'abeille
        double cy = startY + (y * h) - (x * h / 2);

        return new double[] { cx, cy };
    }

    // Place un pion sur une case spécifique (x, y) du tableau
    public void placePion(Boolean turn, int x, int y) {
        double[] coord = getCoords(x, y);
        double cx = coord[0];
        double cy = coord[1];

        // On définit les couleurs pour chaque joueur
        Color couleurJoueur = turn ? Color.BLUE : Color.RED;

        // Pour simuler une épaisseur de contour, on dessine 3 ellipses
        // légèrement décalées ou de tailles différentes.
        for (double i = 0; i < 1.5; i += 0.5) {
            // On réduit légèrement la taille à chaque itération pour "remplir" le trait
            double currentSize = radius - i;
            double offset = currentSize / 2;

            Ellipse pion = new Ellipse(cx - offset, cy - offset, currentSize, currentSize);
            pion.setColor(couleurJoueur);
            pion.draw();
        }
    }

    // Dessine toutes les gemmes
    public void afficherGems(ArrayList<Cell> gems) {
        for (Cell cell : gems) {
            dessinerGem(cell.x, cell.y, cell.gem);
        }
    }

    public void dessinerGem(int x, int y, int type) {
        double[] coord = getCoords(x, y);
        Hexagone gem = new Hexagone(coord[0], coord[1], radius / 2);

        if (type == 1) {
            gem.setColor(Color.YELLOW);
        } else {
            gem.setColor(Color.GREEN);
        }
        gem.fill();
    }

}