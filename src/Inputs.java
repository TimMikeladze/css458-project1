/**
 * @author Tim Mikeladze
 * 
 * 
 */
public enum Inputs {
	DIMENSIONS("DIM", 3), RGB("RGB", 4), LINE("LINE", 7), TRIANGLE("TRI", 10), LOAD_IDENTITY_MATRIX("LOAD_IDENTITY_MATRIX", 1), TRANSLATE(
			"TRANSLATE", 4), SCALE("SCALE", 4), ROTATEX("ROTATEX", 2), ROTATEY("ROTATEY", 2), ROTATEZ("ROTATEZ", 2), WIREFRAME_CUBE("WIREFRAME_CUBE",
			1), SOLID_CUBE("SOLID_CUBE", 1), LOOKAT("LOOKAT", 10), ORTHOGRAPHIC("ORTHO", 7), FRUSTUM("FRUSTUM", 7);
	
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
