import java.util.ArrayList;

public class App {
  String name;
  Boolean end = false;
  Boolean turn = true; // True: joueur 1, False: joueur 2
  Integer size;
  Integer mapSize;
  Board board;
  Echequier jeu = new Echequier();

   // Score des deux joueurs
    int pointsJoueur1 = 0;
    int pointsJoueur2 = 0;

    int passesConsecutives = 0;

  App(String name, Integer size) {
    this.name = name;
    this.size = size;
    this.mapSize = 2 * size - 1;
    this.board = new Board(size, jeu);

    // on dessine les hexagones
    jeu.echec(board.board, this.size);

  }

  public void launch() {
    // Demande si le joueur joue contre IA ou joueur
    String type = IO.readln("Jouer contre IA (o/n): ");

    if (type.equals("o")) {
      // CONTRE IA (random pour le moment)
      IO.println("Joueur contre IA");
      while (!end) {
        playerPlays();
        IAPlays();
      }

    } else {
      // CONTRE JOUEUR
      IO.println("Joueur contre Joueur");

      board.show(name);
      while (!end) {
        playerPlays();
        // switch le tour du joueur
        this.turn = !this.turn;
      }
      afficherResultat();
    }
  }

  public void IAPlays() {
    Boolean casetrouvee = false;
    while (!casetrouvee) {
      Integer x = (int) (Math.random() * this.mapSize);
      Integer y = (int) (Math.random() * this.mapSize);

      if (board.isValid(x, y) && !board.occupied(x, y)) {
        casetrouvee = true;
        IO.println(Main.VERT + "IA a choisis: " + x + ", " + y + Main.RESET);
        placePion(false, x, y);
      }
    }

  }

  public void playerPlays() {
    int player = this.turn ? 1 : 2;
    int x = -1;
    int y = -1;
    boolean saisieValide = false;

    while (!saisieValide) {

      try {
        // 1. Lecture et conversion
        String inputX = IO.readln("Joueur " + player + " x (0-" + (board.mapSize-1) + ") ou 'pass': ");
        if (inputX.equals("pass")) {
            passesConsecutives++;
        if (passesConsecutives >= 2) {
            IO.println(Main.VIOLET + "Deux passes consécutives. Fin de partie." + Main.RESET);
            end = true;
        afficherResultat();
    }
    return;
}
        passesConsecutives = 0; // remet à zéro si le joueur joue vraiment
        x = Integer.parseInt(inputX);
        y = Integer.parseInt(IO.readln("Joueur " + player + " y (0-" + (board.mapSize - 1) + "): "));

        if (board.isValid(x, y)) {
          saisieValide = true;
        } else {
              IO.println(Main.ROUGE + "Erreur : Coordonnées hors limites ou hors du plateau hexagonal !" + Main.RESET);
        }

      } catch (Exception e) {
        // Si l'utilisateur tape des lettres au lieu de chiffres
        System.out.println(Main.ROUGE + "Erreur : Veuillez entrer un nombre." + Main.RESET);
      }
    }

    // Une fois sorti de la boucle, on place le pion
    placePion(this.turn, x, y);
   // (jouerCase est maintenant appelée depuis placePion, score inclus)
  }

  public void placePion(Boolean turn, Integer x, Integer y) {
    // Modifications
    if (board.occupied(x, y)) {
      IO.println(Main.ROUGE + "Cette case est déjà prise par un joueur!" + Main.RESET);
      playerPlays();
    } else {
      jouerCase(turn, x, y);
    }

  }
   public void jouerCase(boolean tourJoueur1, int x, int y) {
        int joueur = tourJoueur1 ? 1 : 2;
 
        // On place le pion sur la cellule logique ET graphique
        board.board[x][y].clicked(tourJoueur1);
        jeu.placePion(tourJoueur1, x, y);
 
        // On mémorise combien de structures existaient avant ce coup
        int nbStructuresAvant = board.structures.size();
 
        // On vérifie les 3 types de structures
        board.verifierStructures(x, y, joueur);
 
        // On récupère les nouvelles structures formées ce tour
        ArrayList<Structure> nouvelles = new ArrayList<>();
        for (int i = nbStructuresAvant; i < board.structures.size(); i++) {
            nouvelles.add(board.structures.get(i));
            IO.println(Main.JAUNE + "Nouvelle structure : "
                + board.structures.get(i) + Main.RESET);
        }
 
        // 5. Si au moins une nouvelle structure, on gère les déclenchements
        if (!nouvelles.isEmpty()) {
            int points = board.gererDeclenchements(joueur, nouvelles);
            if (points > 0) {
                IO.println(Main.VERT + "Joueur " + joueur
                    + " gagne " + points + " point(s) !" + Main.RESET);
                if (joueur == 1) pointsJoueur1 += points;
                else             pointsJoueur2 += points;
            }
        }
 
        // 6. Affichage du score courant
        IO.println("Score → Joueur 1 : " + pointsJoueur1
            + "  |  Joueur 2 : " + pointsJoueur2);
 
        // Condition de fin : toutes les gemmes récoltées / plateau plein
        if (board.toutesGemmesRecoltees()) {
            IO.println(Main.VIOLET + "Toutes les gemmes ont été récoltées !" + Main.RESET);
            end = true;
        } else if (board.estPlein()) {
        IO.println(Main.ROUGE + "Le plateau est plein ! Fin de la partie." + Main.RESET);
        
        // On affiche toutes les gemmes restantes sur l'interface graphique
        IO.println(Main.JAUNE + "Révélation des gemmes non récoltées..." + Main.RESET);
        jeu.afficherGems(board.getGems()); 
        
        end = true;
    }
      if (end == true) {
        afficherResultat();
    }
    }

      public void afficherResultat() {
        IO.println(Main.VIOLET + "\n=== FIN DE PARTIE ===" + Main.RESET);
        IO.println("Joueur 1 : " + pointsJoueur1 + " point(s)");
        IO.println("Joueur 2 : " + pointsJoueur2 + " point(s)");
 
        if (pointsJoueur1 > pointsJoueur2) {
            IO.println(Main.VERT + "Joueur 1 gagne !" + Main.RESET);
        } else if (pointsJoueur2 > pointsJoueur1) {
            IO.println(Main.VERT + "Joueur 2 gagne !" + Main.RESET);
        } else {
            IO.println(Main.JAUNE + "Égalité !" + Main.RESET);
        }
    }
}
