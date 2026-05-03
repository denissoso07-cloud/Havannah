public class echequier {

   public static void echec() {
    double radius = 20;
    double h = 1.7 * radius;
    double w = 30;              // espacement vertical entre centres
    double startX = 450;         // position horizontale fixe
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
    public static void main(String[] args) {
        echec();
    }
   
}

