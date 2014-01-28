public class Transformations {

    private static final int IDENTITY_MATRIX_SIZE = 4;
    private static Matrix transformationMatrix;

    public static void init() {
        transformationMatrix = Matrix.identity(IDENTITY_MATRIX_SIZE);
    }

    public static void flush() {
        transformationMatrix = null;
    }

    public static Matrix getTranformationMatrix() {
        return transformationMatrix;
    }

    public static void translation(float dx, float dy, float dz) {
        //@formatter:off
        float[][] data = {
                {1, 0, 0, dx},
                {0, 1, 0, dy},
                {0, 0, 1, dz},
                {0, 0, 0, 1}};
         //@formatter:on

        try {
            transformationMatrix = transformationMatrix.multiply(new Matrix(data));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rotateX(float deg) {
        //@formatter:off
        float[][] data = {
                {1, 0, 0, 0},
                {0, (float) Math.cos(deg), (float) -Math.sin(deg), 0},
                {0, (float) Math.sin(deg), (float) Math.cos(deg), 0},
                {0, 0, 0, 1}};
         //@formatter:on
        try {
            transformationMatrix = transformationMatrix.multiply(new Matrix(data));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rotateY(float deg) {
        //@formatter:off
        float[][] data = {
                {(float) Math.cos(deg), 0, (float) Math.sin(deg), 0},
                {0, 1, 0, 0},
                {(float) - Math.sin(deg), 0, (float) Math.cos(deg), 0},
                {0, 0, 0, 1}};
         //@formatter:on
        try {
            transformationMatrix = transformationMatrix.multiply(new Matrix(data));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rotateZ(float deg) {
        //@formatter:off
        float[][] data = {
                {(float) Math.cos(deg), (float) -Math.sin(deg), 0, 0},
                {(float) Math.sin(deg), (float) Math.cos(deg), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
         //@formatter:on
        try {
            transformationMatrix = transformationMatrix.multiply(new Matrix(data));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scale(float sx, float sy, float sz) {
        //@formatter:off
        float[][] data = {
                {sx, 0, 0, 0},
                {0, sy, 0, 0},
                {0, 0, sz, 0},
                {0, 0, 0, 1}};
         //@formatter:on

        try {
            transformationMatrix = transformationMatrix.multiply(new Matrix(data));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Transformations.init();
        Transformations.rotateX(90);
        Transformations.translation(3, 4, 5);
        System.out.println(Transformations.getTranformationMatrix());
    }
}
