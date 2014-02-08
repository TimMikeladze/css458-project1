import math.Matrix;
import math.Vector4f;

public class Transformations {
	
	private static final int IDENTITY_MATRIX_SIZE = 4;
	private static Matrix transformationMatrix = Matrix.identity(IDENTITY_MATRIX_SIZE);
	private static Matrix rotationMatrixX = Matrix.identity(IDENTITY_MATRIX_SIZE);
	private static Matrix rotationMatrixY = Matrix.identity(IDENTITY_MATRIX_SIZE);
	private static Matrix projectionMatrix = Matrix.identity(IDENTITY_MATRIX_SIZE);
	private static Matrix lookAtMatrix = Matrix.identity(IDENTITY_MATRIX_SIZE);
	
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
	
	public static void setOrthographicProjection(float left, float right, float bottom, float top, float near, float far) {
		projectionMatrix = Matrix.identity(IDENTITY_MATRIX_SIZE);
		//@formatter:off
		float[][] data = {	{ 2 / (right - left), 0, 0, - (right + left) / (right - left) }, 
							{ 0, 2 / (top-bottom), 0, -(top + bottom) / (top - bottom) }, 
							{ 0, 0, - 2 / (far - near), - (far + near) / (far - near) }, 
							{ 0, 0, 0, 1 } };
		//@formatter:on
		
		try {
			projectionMatrix = new Matrix(data).multiply(projectionMatrix);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setFrustumProjection(float left, float right, float bottom, float top, float near, float far) {
		projectionMatrix = Matrix.identity(IDENTITY_MATRIX_SIZE);
		//@formatter:off
		float[][] data = {	{ 2f * near / (right - left), 0, (right + left) / (right - left), 0 }, 
							{ 0, 2f * near / (top-bottom), (top + bottom) / (top - bottom), 0 }, 
							{ 0, 0, - (far + near) / (far - near), -2f * far * near / (far - near)}, 
							{ 0, 0, -1f, 0} };
		//@formatter:on
		
		try {
			projectionMatrix = new Matrix(data).multiply(projectionMatrix);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		/*
		 *  c[0][0] = 2.0*zNear/(right - left);
		    c[0][2] = (right + left)/(right - left);
		    c[1][1] = 2.0*zNear/(top - bottom);
		    c[1][2] = (top + bottom)/(top - bottom);
		    c[2][2] = -(zFar + zNear)/(zFar - zNear);
		    c[2][3] = -2.0*zFar*zNear/(zFar - zNear);
		    c[3][2] = -1.0;
		 */
	}
	
	public static void setLookAt(float eyeX, float eyeY, float eyeZ, float atX, float atY, float atZ, float upX, float upY, float upZ) {
		lookAtMatrix = Matrix.identity(IDENTITY_MATRIX_SIZE);
		
		Vector4f n = (new Vector4f(eyeX, eyeY, eyeZ, 0).subtractVector(new Vector4f(atX, atY, atZ, 0))).getNormalizedVector();
		Vector4f u = (new Vector4f(upX, upY, upZ, 0).getCrossProduct(n)).getNormalizedVector();
		Vector4f v = (n.getCrossProduct(u)).getNormalizedVector();
		Vector4f t = new Vector4f(0, 0, 0, 1);
		//@formatter:off
		float[][] data = {	{ u.getX(), v.getX(), n.getX(), t.getX() },
							{ u.getY(), v.getY(), n.getY(), t.getY() },
							{ u.getZ(), v.getZ(), n.getZ(), t.getZ() },
							{ u.getW(), v.getW(), n.getW(), t.getW() } };
		//@formatter:on
		Matrix c = new Matrix(data);
		//@formatter:off
		float[][] translate = { { 1, 0, 0, -eyeX }, { 0, 1, 0, -eyeY }, { 0, 0, 1, -eyeZ }, { 0, 0, 0, 1 } };
		//@formatter:on
		Matrix tm = new Matrix(translate);
		try {
			lookAtMatrix = c.multiply(tm)
							.multiply(lookAtMatrix);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Vector4f calculatePoint(Vector4f v) {
		try {
			Matrix m = projectionMatrix.multiply(lookAtMatrix)
										.multiply(rotationMatrixY.multiply(rotationMatrixX.multiply(transformationMatrix.multiply(v.toMatrix()))));
			return new Vector4f(m.getDataAt(0, 0) / m.getDataAt(3, 0), m.getDataAt(1, 0) / m.getDataAt(3, 0), m.getDataAt(2, 0) / m.getDataAt(3, 0),
					m.getDataAt(3, 0) / m.getDataAt(3, 0));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
