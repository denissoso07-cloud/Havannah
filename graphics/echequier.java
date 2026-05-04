import java.util.ArrayList;

public class Echequier {
    double radius = 20;
    double h = 1.7 * radius; // hauteur
    double w = 30; // espacement vertical entre centres
    double startX = 450; // position horizontale
    double startY = 170; // point de départ en Y
    double maxCols = 9;

    public void echec() {

        for (int j = 0; j < maxCols; j++) {
            double cx = startX + j * w;
            double offsetY;

            if (j < maxCols / 2) {
                offsetY = -(j * h / 2); // monte progressivement
            } else {
                offsetY = -((maxCols - 1 - j) * h / 2); // redescend progressivement
            }

            double nbHex;
            if (j < maxCols / 2) {
                nbHex = 5 + j; // augmente
            } else {
                nbHex = 5 + (maxCols - 1 - j); // diminue
            }

            for (int i = 0; i < nbHex; i++) {
                double cy = startY + offsetY + i * h; // i qui avance verticalement
                Hexagone box = new Hexagone(cx, cy, radius);
                box.draw();
            }
        }
    }

    public void PionJ1(double cx, double cy) {
        // cx, cy = centre de l'hexagone
        // on décale de -radius pour avoir le coin haut-gauche de l'ellipse
        Ellipse j1 = new Ellipse(cx - radius, cy - radius, 20, 20);
        j1.setColor(Color.BLACK);
        j1.fill();

    }

    public void PionJ2(double cx, double cy) {
        Ellipse j2 = new Ellipse(cx - radius, cy - radius, 20, 20);
        j2.draw();

    }

    // dessine une gemme sur une case (j, i) du plateau
    // type 1 = gemme simple (jaune), type 2 = gemme rare (verte)
    public void dessinerGem(int j, int i, int type) {

        // on recupere le centre de la case (j, i)
        double[] centre = getCentreGems(j, i);
        double cx = centre[0];
        double cy = centre[1];

        // on dessine un petit hexagone colore
        Hexagone gem = new Hexagone(cx, cy, radius / 2);

        if (type == 1) {
            gem.setColor(Color.YELLOW); // gemme simple = jaune
        } else {
            gem.setColor(Color.GREEN); // gemme rare = vert
        }

        gem.fill();
    }

    // parcourt la liste des gemmes du Board et les affiche
    public void afficherGems(ArrayList<Cell> gems) {

        for (int k = 0; k < gems.size(); k++) {
            Cell cell = gems.get(k);

            // conversion : coordonnees logiques (x,y) → indices graphiques (j,i)
            // le plateau logique va de -size a +size
            // le plateau graphique commence a 0
            // donc on decale de maxCols/2 pour centrer
            int j = cell.x + (int) (maxCols / 2);
            int i = cell.y + (int) (maxCols / 2);

            dessinerGem(j, i, cell.gem);
        }
    }

    public double[] getCentre(int j, int i) {
        double cx = startX + j * w + 10;
        double offsetY;
        if (j < maxCols / 2) {
            offsetY = -(j * h / 2);
        } else {
            offsetY = -((maxCols - 1 - j) * h / 2);
        }
        double cy = startY + offsetY + i * h;
        cy += 10;
        return new double[] { cx, cy };
    }

    public double[] getCentreGems(int j, int i) {
        double cx = startX + j * w;
        double offsetY;
        if (j < maxCols / 2) {
            offsetY = -(j * h / 2);
        } else {
            offsetY = -((maxCols - 1 - j) * h / 2);
        }
        double cy = startY + offsetY + i * h;
        return new double[] { cx, cy };
    }

    public static void main(String[] args) {

    }
}
