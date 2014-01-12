public enum Inputs {
    DIMENSIONS("DIM", 3), RGB("RGB", 4), LINE("LINE", 5), TRIANGLE("TRI", 7);
    
    private String input;
    private int params;
    
    Inputs(String input, int params) {
        this.input = input;
        this.params = params;
    }
    
    public String getInput() {
        return input;
    }
    
    public int getNumberOfParameters() {
        return params;
    }
    
    @Override
    public String toString() {
        return input;
    }
    
    public static Inputs find(String s) {
        Inputs input = null;
        for (Inputs i : Inputs.values()) {
            if (i.getInput()
                 .equalsIgnoreCase(s)) {
                input = i;
                break;
            }
        }
        return input;
    }
}
