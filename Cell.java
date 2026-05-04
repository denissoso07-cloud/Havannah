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
  /*
   * Rareté d'une gem, 1 ou 2
   */
  Integer gem;

  Cell(Integer x, Integer y) {
    this.state = 0;
    this.gem = 0;
    this.x = x;
    this.y = y;
  }

  public void setGem(Integer gem) {
    this.gem = gem;
  }

  public void clicked(Integer joueur) {
    if (this.state == 0) {
      this.state = joueur;
    } else {
      IO.println(Main.ROUGE + "Cette case est deja prise: " + this.toString() + Main.RESET);
    }
  }

  public String toString() {
    return "Pos: (" + this.x + "," + this.y + "), State: " + this.state + ", Gem: "
        + (this.gem == null ? "-" : this.gem);
  }
}
