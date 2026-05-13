import java.util.ArrayList;

public class Structure {
  String type;
  int joueur;
  Boolean active;
  boolean declenchee;
  ArrayList<int[]> cases;

  /**
   * Constructeur de la structure.
   * Initialise une forme (triangle, ligne, étoile) complétée par un joueur.
   */
  Structure(String type, int joueur, ArrayList<int[]> cases) {
    this.type = type;
    this.joueur = joueur;
    this.active = true; // La structure est opérationnelle dès sa création
    this.declenchee = false; // Elle n'a pas encore activé ses pouvoirs/gemmes
    this.cases = cases;
  }

  /**
   * Retourne une description textuelle de la structure.
   * Utile pour le débogage dans la console.
   */
  public String toString() {
    return "Structure " + type + " du joueur " + joueur
        + " | active=" + active + " | declenchee=" + declenchee;
  }
}