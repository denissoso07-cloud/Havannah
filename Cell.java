public class Cell {
  Integer state;
  Integer x;
  Integer y;

  Cell(Integer x, Integer y) {
    this.state = 0;
    this.x = x;
    this.y = y;
  }

  public String toString() {
    return this.state.toString();
  }
}
