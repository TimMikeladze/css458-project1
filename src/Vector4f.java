/**
 * @author Tim Mikeladze
 * 
 * 
 */
public class Vector4f {
	
	private float x;
	private float y;
	private float z;
	private float w;
	
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
	}
	
	public float getW() {
		return w;
	}
	
	public Matrix toMatrix() {
		Matrix m = new Matrix(new float[][] { { x }, { y }, { z }, { w } });
		return m;
	}
	
	public Vector4f getNormalizedVector() {
		float magnitude = getMagnitude();
		return new Vector4f(x / magnitude, y / magnitude, z / magnitude, w / magnitude);
	}
	
	public float getMagnitude() {
		return (float) Math.sqrt((x * x) + (y * y) + (z * z) + (w * w));
	}
	
	public Vector4f addVector(Vector4f v) {
		return new Vector4f(x + v.x, y + v.y, z + v.z, w + v.w);
	}
	
	public Vector4f subtractVector(Vector4f v) {
		return addVector(v.getNegated());
	}
	
	public Vector4f getNegated() {
		return new Vector4f(-x, -y, -z, -w);
	}
	
	public float getDotProduct(Vector4f v) {
		return (x * v.x) + (y * v.y) + (z * v.z) + (w * v.w);
	}
	
	public Vector4f getCrossProduct(Vector4f v) {
		return new Vector4f(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x, 0);
		
	}
	
	@Override
	public String toString() {
		return "Point4f [x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
	}
	
}
