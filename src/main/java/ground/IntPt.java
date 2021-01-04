package ground;

public class IntPt {
	public IntPt(int x, int y) {
		this.x = x;
		this.y = y;
	}

	IntPt(double x, double y) {
		this.x = (int) x;
		this.y = (int) y;
	}

	public int x = 0;
	public int y = 0;
}
