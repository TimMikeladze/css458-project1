public class Point4f {

    private float x;
    private float y;
    private float z;
    private float w;

    public Point4f(float x, float y, float z, float w) {
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
        Matrix m =  new Matrix(new float[][] { { x }, { y }, { z }, { w } });
        return m;
    }

    @Override
    public String toString() {
        return "Point4f [x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
    }


}
