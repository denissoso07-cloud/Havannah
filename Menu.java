/**
 * Menu graphique affiché avant le début de la partie.
 * Utilise uniquement la librairie graphique fournie (Rectangle, Text, Ellipse).
 * La sélection se fait via IO.readln car la librairie n'a pas de gestion de clic.
 */
public class Menu {

    // Position et taille du menu
    double startX = 500;
    double startY = 80;
    double largeur = 220;
    double hauteur = 50;

    /**
     * Dessine un bouton avec un fond coloré et un texte centré.
     */
    public void dessinerBouton(double x, double y, double w, double h, String label, Color couleur) {
        // fond du bouton
        Rectangle fond = new Rectangle(x, y, w, h);
        fond.setColor(couleur);
        fond.fill();

        // contour du bouton
        Rectangle contour = new Rectangle(x, y, w, h);
        contour.setColor(Color.BLACK);
        contour.draw();

        // texte du bouton
        Text texte = new Text(x + 20, y + 15, label);
        texte.setColor(Color.BLACK);
        texte.draw();
    }

    /**
     * Dessine le titre du jeu en haut du menu.
     */
    public void dessinerTitre() {
        // fond du titre
        Rectangle fondTitre = new Rectangle(startX - 10, startY - 10, largeur + 20, 60);
        fondTitre.setColor(Color.DARK_GRAY);
        fondTitre.fill();

        // texte du titre
        Text titre = new Text(startX + 50, startY + 10, "=== HAVANNAH ===");
        titre.setColor(Color.YELLOW);
        titre.draw();
    }

    /**
     * Affiche le menu complet et retourne le choix du joueur :
     *   "ia"     → joueur contre IA
     *   "joueur" → joueur contre joueur
     */
    public String afficher() {
        dessinerTitre();

        // bouton 1 : contre IA
        dessinerBouton(startX, startY + 80, largeur, hauteur, "1 - Jouer contre l'IA", Color.LIGHT_GRAY);

        // bouton 2 : contre un joueur
        dessinerBouton(startX, startY + 150, largeur, hauteur, "2 - Joueur vs Joueur", Color.LIGHT_GRAY);

        // bouton 3 : charger une sauvegarde
        dessinerBouton(startX, startY + 220, largeur, hauteur, "3 - Charger sauvegarde", Color.CYAN);

        // separateur
        Rectangle sep = new Rectangle(startX, startY + 300, largeur, 2);
        sep.setColor(Color.GRAY);
        sep.fill();

        // instruction
        Text instruction = new Text(startX, startY + 315, "Tapez 1, 2 ou 3 dans la console");
        instruction.setColor(Color.DARK_GRAY);
        instruction.draw();

        // lecture du choix dans la console
        String choix = IO.readln("Votre choix (1/2/3) : ");

        if (choix.equals("1")) {
            // on met le bouton IA en vert pour confirmer le choix
            dessinerBouton(startX, startY + 80, largeur, hauteur, "1 - Jouer contre l'IA [OK]", Color.GREEN);
            return "ia";
        } else if (choix.equals("3")) {
            dessinerBouton(startX, startY + 220, largeur, hauteur, "3 - Charger sauvegarde [OK]", Color.GREEN);
            return "charger";
        } else {
            // par défaut : joueur vs joueur
            dessinerBouton(startX, startY + 150, largeur, hauteur, "2 - Joueur vs Joueur [OK]", Color.GREEN);
            return "joueur";
        }
    }
}