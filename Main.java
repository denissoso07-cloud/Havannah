public class Main {
    // couleurs
    public static final String RESET = "\u001B[0m";
    public static final String ROUGE = "\u001B[31m";
    public static final String VERT = "\u001B[32m";
    public static final String JAUNE = "\u001B[33m";
    public static final String VIOLET = "\u001B[35m";

    void main() {
         // 1. Le Menu affiche les options et retourne le choix du joueur :
        //    "ia", "joueur" ou "charger"
        Menu menu = new Menu();
        String choix = menu.afficher();
 
        // 2. On détermine le mode de jeu et si on doit charger une sauvegarde
        boolean charger = choix.equals("charger");
        String mode = charger ? "joueur" : choix; // "charger" → mode joueur par défaut
 
        // 3. On crée la partie et on la lance avec les paramètres du menu
        App app = new App("Havannah", 5);
        app.launch(mode,charger);

    }
}
