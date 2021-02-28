import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Similarity {
	/**
	 * Splits the strings between each space and comma, and gives similarity measure
	 * for each word in string
	 * @param first
	 * @param second
	 * @return similarity of the two strings split into words
	 */
	public static double jaccardSimilarity(String first, String second) {
		Set<String> intersection = new HashSet<String>();
		Set<String> union = new HashSet<String>();
		boolean filledUnion = false;

		String[] set1 = first.split(",\\s|,|\\s");
		String[] set2 = second.split(",\\s|,|\\s");
		for(int i = 0; i < set1.length; ++i) {
			union.add(set1[i]);

			for(int j = 0; j < set2.length; ++j) {
				if(!filledUnion) {
					union.add(set2[j]);
				}

				if(set1[i].equals(set2[j])) {
					intersection.add(set1[i]);
				}
			}

			filledUnion = true;
		}

		return (double)intersection.size() / union.size();
	}


	public static double jaccardSimilarity(String[] firstStopped, String[] secondStopped) {
		Set<String> set1 = new HashSet<String>();
		Set<String> set2 = new HashSet<String>();
		for(String s : firstStopped)
			if(s != null)
				set1.add(s);
		for(String s : secondStopped)
			if(s != null)
				set2.add(s);
		Set<String> union = new HashSet<String>(set1);
		union.addAll(set2);
		set1.retainAll(set2);
		return (double)set1.size()/union.size();
	}


	public static double jaccardSimilarity(String[][] firstStopped, String[][] secondStopped) {
		Set<String> set1 = new HashSet<String>();
		Set<String> set2 = new HashSet<String>();
		for(String[] arr : firstStopped) {
			if(arr != null)
			for(String s : arr)
				if(s != null)
					set1.add(s);
		}
		for(String[] arr : secondStopped) {
			if(arr != null)
			for(String s : arr)
				if(s != null)
					set2.add(s);
		}
		Set<String> union = new HashSet<String>(set1);
		union.addAll(set2);
		set1.retainAll(set2);
		return (double)set1.size()/union.size();
	}
}
