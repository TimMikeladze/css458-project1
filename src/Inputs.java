
public enum Inputs {
    DIMENSIONS("DIM"), RGB("RGB"), LINE("LINE"), TRIANGLE("TRI");

    private String input;

    Inputs(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    @Override
    public String toString() {
        return input;
    }
}
