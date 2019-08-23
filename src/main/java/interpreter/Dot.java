package interpreter;

/**
 * Dot wrapper object and enums
 * 
 * @author jchen3652
 *
 */
public class Dot {
	String set;
	int counts;

	side side;
	double LRValue;
	leftToRightModifier LRMod;
	int yardline;
	double FBValue;

	frontToBackModifier FBMod;
	hash hash;

	/**
	 * 
	 * @param set
	 * @param counts
	 * @param side
	 * @param LRValue
	 * @param LRMod
	 * @param yardline
	 * @param FBValue
	 * @param FBMod
	 * @param hash
	 */
	public Dot(String set, int counts, side side, double LRValue, leftToRightModifier LRMod, int yardline,
			double FBValue, frontToBackModifier FBMod, hash hash) {

		this.set = set;
		this.counts = counts;
		this.side = side;

		this.LRValue = LRValue;
		this.LRMod = LRMod;
		this.yardline = yardline;

		this.FBValue = FBValue;
		this.FBMod = FBMod;
		this.hash = hash;

	}

	public String toString() {
		return set + " " + counts + " " + side + " " + LRValue + " " + LRMod + " " + yardline + " " + FBValue + " "
				+ FBMod + " " + hash;
	}
	
	public String shortHand() {
		return "Counts: " + counts + " Side " + side + " " + LRValue + " " + LRMod + " " + yardline + " " + FBValue + " "
				+ FBMod + " " + hash;
	}
	
	private String locationString() {
		return side + " " + LRValue + " " + LRMod + " " + yardline + " " + FBValue + " "
				+ FBMod + " " + hash;
	}

//	public boolean equals(Dot other) {
//		return other.getXYCoords() == this.locationString();
//	}

	/**
	 * Field position calculation
	 * 
	 * @return double array of x and y, in steps from the lower left corner of the
	 *         field
	 */
	public double[] getXYCoords() {
		double x = this.side.equals(interpreter.side.ONE) ? 0 : 100 * 3 * 12 / 22.5;
		double y = this.hash.value;

		x += side.multiplier * yardline * 3 * 12 / 22.5; // Correct yardline
		x += side.multiplier * LRMod.multiplier * LRValue; // Correct modification

		y += FBValue * FBMod.multiplier;

		return new double[] { 3600 / 22.5 - x, 1920 / 22.5 - y };
	}
	
	/**
	 * Returns just counts
	 * 
	 * @return number of counts to get to set
	 */
	public int getCounts() {
		return this.counts;
	}
	
	
	/**
	 * Returns just the set number
	 * 
	 * @return set number
	 */
	public String getSetNumber() {
		return this.set;
	}
 }


enum leftToRightModifier {
	INSIDE(1), OUTSIDE(-1);

	public final int multiplier;

	private leftToRightModifier(int multiplier) {
		this.multiplier = multiplier;
	}
}

enum frontToBackModifier {
	FRONT(-1), BEHIND(1);

	public final int multiplier;

	private frontToBackModifier(int multiplier) {
		this.multiplier = multiplier;
	}
}

enum side {
	ONE(1), TWO(-1);

	public final int multiplier;

	private side(int multiplier) {
		this.multiplier = multiplier;
	}
}

enum hash {
	FRONT_SIDELINE(0), FRONT_HASH(640 / 22.5), BACK_HASH(1280 / 22.5), BACK_SIDELINE(1920 / 22.5);

	public final double value;

	private hash(double value) {
		this.value = value;
	}
}
