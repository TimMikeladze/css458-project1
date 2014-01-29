public class Transformations {
	
	private static final int IDENTITY_MATRIX_SIZE = 4;
	private static Matrix transformationMatrix;
	private static Matrix rotationMatrixX = Matrix.identity(IDENTITY_MATRIX_SIZE);
	private static Matrix rotationMatrixY = Matrix.identity(IDENTITY_MATRIX_SIZE);
	
	public static void init() {
		transformationMatrix = Matrix.identity(IDENTITY_MATRIX_SIZE);
	}
	
	public static Matrix getTranformationMatrix() {
		return transformationMatrix;
	}
	
	public static void setRotationMatrixX(Matrix m) {
		rotationMatrixX = new Matrix(m.getData());
	}
	
	public static void setRotationMatrixY(Matrix m) {
		rotationMatrixY = new Matrix(m.getData());
		
	}
	
	public static void translate(float dx, float dy, float dz) {
		//@formatter:off
		float[][] data = { { 1, 0, 0, dx }, { 0, 1, 0, dy }, { 0, 0, 1, dz }, { 0, 0, 0, 1 } };
		//@formatter:on
		
		try {
			transformationMatrix = new Matrix(data).multiply(transformationMatrix);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void rotateX(float deg) {
		deg = (float) Math.toRadians(deg);
		//@formatter:off
		float[][] data = { { 1, 0, 0, 0 }, { 0, (float) Math.cos(deg), (float) -Math.sin(deg), 0 },
				{ 0, (float) Math.sin(deg), (float) Math.cos(deg), 0 }, { 0, 0, 0, 1 } };
		//@formatter:on
		try {
			transformationMatrix = new Matrix(data).multiply(transformationMatrix);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void rotateY(float deg) {
		deg = (float) Math.toRadians(deg);
		//@formatter:off
		float[][] data = { { (float) Math.cos(deg), 0, (float) Math.sin(deg), 0 }, { 0, 1, 0, 0 },
				{ (float) -Math.sin(deg), 0, (float) Math.cos(deg), 0 }, { 0, 0, 0, 1 } };
		//@formatter:on
		try {
			transformationMatrix = new Matrix(data).multiply(transformationMatrix);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void rotateZ(float deg) {
		deg = (float) Math.toRadians(deg);
		//@formatter:off
		float[][] data = { { (float) Math.cos(deg), (float) -Math.sin(deg), 0, 0 }, { (float) Math.sin(deg), (float) Math.cos(deg), 0, 0 },
				{ 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		//@formatter:on
		try {
			transformationMatrix = new Matrix(data).multiply(transformationMatrix);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void scale(float sx, float sy, float sz) {
		//@formatter:off
		float[][] data = { { sx, 0, 0, 0 }, { 0, sy, 0, 0 }, { 0, 0, sz, 0 }, { 0, 0, 0, 1 } };
		//@formatter:on
		
		try {
			transformationMatrix = new Matrix(data).multiply(transformationMatrix);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Point3f calculatePoint(Point3f p) {
		try {
			Matrix m = rotationMatrixY.multiply(rotationMatrixX.multiply(transformationMatrix.multiply(p.toMatrix())));
			return new Point3f(m.getDataAt(0, 0), m.getDataAt(1, 0), m.getDataAt(2, 0));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
