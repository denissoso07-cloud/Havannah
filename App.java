import java.util.ArrayList;

public class App {
  String name;
  Boolean end = false;
  Boolean turn = true; // True: joueur 1, False: joueur 2
  ArrayList<Integer> moves = new ArrayList<>();

  App(String name) {
    this.name = name;
  }

  public void launch() {
    while (!end) {
      showGame();
      showMoves();
      play();
    }
  }

  public void showGame() {
    IO.println(this.name + ": ");
    IO.println("<Affichage du tableau>");
  }

  public void showMoves() {
    IO.println("Moves: " + this.moves.toString());
  }

  public void play() {
    Integer player;
    if (this.turn)
      player = 1;
    else
      player = 2;

    // exemple d'input
    Integer cell = Integer.valueOf(IO.readln("Joueur " + player + ": "));
    // exemple d'output
    this.moves.add(cell);
    this.turn = !this.turn;
    // this.end = true;
  }
}
