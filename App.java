import java.util.ArrayList;

public class App {
  String name;
  Boolean end = false;
  Boolean turn = true; // True: joueur 1, False: joueur 2
  ArrayList<Integer> moves = new ArrayList<>();
  Integer size;
  Board board;

  App(String name, Integer size) {
    this.name = name;
    this.board = new Board(5);
  }

  public void launch() {
    // compteur temporaire pour faire 4 moves
    int moves = 0;
    while (!end) {
      board.show(name);
      // showMoves();
      play();
      moves++;
      if (moves > 4)
        end = true;
    }
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

  }
}
