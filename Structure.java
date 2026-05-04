public class Structure {
    /*
     * type : "triangle", "ligne", "etoile"
     * joueur : 1 ou 2
     * active : devient true quand la structure est complétée
     */
    String type;
    int joueur;
    Boolean active;

    Structure(String type,int joueur ) {
        this.type = type;
        this.joueur = joueur;
        this.active = true; // complétée = active immédiatement
    }

    public String toString() {
        return "Structure " + type + " du joueur " + joueur + " (active: " + active + ")";
    }
}