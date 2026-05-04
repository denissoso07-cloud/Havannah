
public class echequier {

   public static void echec() {
    double radius = 20;
    double h = 1.7 * radius;    // hauteur
    double w = 30;              // espacement vertical entre centres
    double startX = 450;         // position horizontale 
    double startY = 170;      // point de départ en Y
    double maxCols = 15;    

    for(int j = 0;j< maxCols;j++){
        double cx = startX + j * w;
        double offsetY;

        if (j < maxCols / 2) {
            offsetY = -(j * h / 2);        // monte progressivement
        } else {
            offsetY = -((maxCols - 1 - j) * h / 2); // redescend progressivement
        }

        double nbHex;
        if (j < maxCols / 2) {
            nbHex = 8 + j;        // augmente
        } else {
            nbHex = 8 + (maxCols - 1 - j); // diminue
        }

        for (int i = 0; i < nbHex; i++) {
        double cy = startY + offsetY + i * h; // i qui avance verticalement 
        Hexagone box = new Hexagone(cx, cy, radius);
        box.draw();
    }
}
}
 public static void PionJ1(double cx, double cy, double radius) {
    // cx, cy = centre de l'hexagone
    // on décale de -radius pour avoir le coin haut-gauche de l'ellipse
    Ellipse j1 = new Ellipse(cx - radius, cy - radius, radius * 2, radius * 2);
    j1.setColor(Color.BLACK);
    j1.fill();
   
}

public static void PionJ2(double cx, double cy, double radius) {
    Ellipse j2 = new Ellipse(cx - radius, cy - radius, radius * 2, radius * 2);
    j2.setColor(Color.GRAY);
    j2.fill();
   
}

  public static void gem(){
        Hexagone gems = new Hexagone(45, 45, 10);
        gems.setColor(Color.GREEN);
        gems.fill();

    }
public static double[] getCentre(int j, int i) {
    double radius = 20;
    double h = 1.7 * radius;
    double w = 30;
    double startX = 450;
    double startY = 170;
    double maxCols = 15;

    double cx = startX + j * w;
    double offsetY;
    if (j < maxCols / 2) {
        offsetY = -(j * h / 2);
    } else {
        offsetY = -((maxCols - 1 - j) * h / 2);
    }
    double cy = startY + offsetY + i * h;

    return new double[]{cx, cy};
}

public static void main(String[] args) {
    double[] centre = getCentre(1, 1);
  
    PionJ1(centre[0], centre[1], 20);
    echec();
}
}

