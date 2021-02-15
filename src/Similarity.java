import java.util.HashSet;
import java.util.Set;

public class Similarity {
	public static double jaccardSimilarity(String first, String second) {
		Set<String> intersection = new HashSet<String>();
		Set<String> union = new HashSet<String>();
		boolean filledUnion = false;

		String[] set1 = first.split(",| ");
		String[] set2 = second.split(",| ");
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
}
