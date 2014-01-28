
public class Matrix {

    private int rows;
    private int columns;
    private float[][] data;

    public Matrix(float[][] data) {
        rows = data.length;
        columns = data[0].length;
        this.data = new float[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.data[i][j] = data[i][j];
            }
        }
    }

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        data = new float[rows][columns];
    }

    public static Matrix identity(int size) {
        Matrix result = new Matrix(size, size);
        for (int i = 0; i < size; i++) {
            result.data[i][i] = 1;
        }
        return result;
    }

    public Matrix multiply(Matrix matrix) throws Exception {
        if (this.rows != this.columns) {
            throw new Exception("Matrix dimensions don't match");
        }

        Matrix result = new Matrix(this.rows, matrix.columns);
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.columns; j++) {
                for (int k = 0; k < this.columns; k++) {
                    result.data[i][j] += (this.data[i][k] * matrix.data[k][j]);
                }
            }
        }
        return result;
    }

    public void setValue(float value, int row, int column) {
        data[row][column] = value;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public float[][] getData() {
        return data;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                s += data[i][j] + "\t";
            }
            s += "\n";
        }
        return s;
    }


}
