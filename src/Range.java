public class Range {
	
	private int min;
	private int max;
	private float min2;
	private float max2;
	
	public Range(int mi, int ma, float ni, float na) {
		min = mi;
		max = ma;
		min2 = ni;
		max2 = na;
	}
	
	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}
	
	public float getMin2() {
		return min2;
	}
	
	public float getMax2() {
		return max2;
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
	public void setMin2(float min2) {
		this.min2 = min2;
	}
	
	public void setMax2(float max2) {
		this.max2 = max2;
	}
	
}