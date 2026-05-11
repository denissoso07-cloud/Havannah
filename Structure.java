import java.util.ArrayList;

public class Structure {
    /*
     * type : "triangle", "ligne", "etoile"
     * joueur : 1 ou 2
     * active : devient true quand la structure est complétée
     * declenchee : true quand elle a déjà revelé ses gemmes
     * liste des cases qui forme une structure 
     */
    String type;
    int joueur;
    Boolean active;
    boolean declenchee;
    ArrayList<int[]> cases;

    Structure(String type,int joueur,ArrayList<int[]> cases ) {
        this.type = type;
        this.joueur = joueur;
        this.active = true; // complétée = active immédiatement
        this.declenchee = false;  // pas encore declenchee
        this.cases = cases;
    }

    public String toString() {
         return "Structure " + type + " du joueur " + joueur
             + " | active=" + active + " | declenchee=" + declenchee;
    }
}