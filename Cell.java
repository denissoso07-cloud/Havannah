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

  public void clicked(Boolean turn) {
    if (this.state == 0) {
      this.state = turn ? 1 : 2;
    } else {
      IO.println(Main.ROUGE + "Erreur: La case est déjà prise (Cell.clicked)" + Main.RESET);
    }
  }

  public String toString() {
    return "Pos: (" + this.x + "," + this.y + "), State: " + this.state + ", Gem: "
        + (this.gem == null ? "-" : this.gem);
  }
}
