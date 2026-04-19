public class Cell {
  /*
   * State represente l'état de la cellule
   * 0: vide,
   * 1: occupé par joueur 1
   * 2: occupé par joueur 2
   */
  Integer state;
  Integer x;
  Integer y;

  Cell(Integer x, Integer y) {
    this.state = 0;
    this.x = x;
    this.y = y;
  }

  public void clicked(Integer joueur) {

  }

  public String toString() {
    return this.state.toString();
  }
}
