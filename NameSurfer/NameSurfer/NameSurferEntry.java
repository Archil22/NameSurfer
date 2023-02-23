/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

public class NameSurferEntry implements NameSurferConstants {
	private final String name;
	private final int[] rateArr = new int[NDECADES];

/* Constructor: NameSurferEntry(line) */
/**
 * Creates a new NameSurferEntry from a data line as it appears
 * in the data file.  Each line begins with the name, which is
 * followed by integers giving the rank of that name for each
 * decade.
 */
	public NameSurferEntry(String line) {
		String[] info = line.split(" ");
		name = info[0];
		for(int i=1;i<info.length;i++){
			rateArr[i-1]=Integer.parseInt(info[i]);
		}
	}

/* Method: getName() */
/**
 * Returns the name associated with this entry.
 */
	public String getName() {
		// You need to turn this stub into a real implementation //
		return name;
	}

/* Method: getRank(decade) */
/**
 * Returns the rank associated with an entry for a particular
 * decade.  The decade value is an integer indicating how many
 * decades have passed since the first year in the database,
 * which is given by the constant START_DECADE.  If a name does
 * not appear in a decade, the rank value is 0.
 */
	public int getRank(int decade) {
		switch(decade){
			case 1900: return rateArr[0];
			case 1910: return rateArr[1];
			case 1920: return rateArr[2];
			case 1930: return rateArr[3];
			case 1940: return rateArr[4];
			case 1950: return rateArr[5];
			case 1960: return rateArr[6];
			case 1970: return rateArr[7];
			case 1980: return rateArr[8];
			case 1990: return rateArr[9];
			case 2000: return rateArr[10];
		}
		return 0;
	}

/* Method: toString() */
/**
 * Returns a string that makes it easy to see the value of a
 * NameSurferEntry.
 */
	public String toString() {
		String result="";
		String rateStr=("["+rateArr[0]);
		for (int i=1;i<rateArr.length;i++){
			rateStr+=(" "+rateArr[i]);
		}
		rateStr+="]";
		result+=(name+" "+rateStr);
		return result;
	}
}

