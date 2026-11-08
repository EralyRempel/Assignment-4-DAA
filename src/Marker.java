import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border length" line.
     */
    public static int[] prefixFunction(String p) {
         int m = p.length();
        int[] pi = new int[m];
        for (int i = 1, len = 0; i < m; ) {
            if (p.charAt(i) == p.charAt(len)) {
                pi[i++] = ++len;
            } else if (len != 0) {
                len = pi[len - 1];
            } else {
                pi[i++] = 0;
            }
        }
        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA" in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */
    public static int[] findAll(String text, String p) {
        int n = text.length(), m = p.length();
        if (n < m || m == 0) return new int[0];
        int[] pi = prefixFunction(p);
        List<Integer> res = new ArrayList<>();

        for (int i = 0, j = 0; i < n; ) {
            if (text.charAt(i) == p.charAt(j)) {
                i++; j++;
            }
            if (j == m) {
                res.add(i - j);
                j = pi[j - 1];
            } else if (i < n && text.charAt(i) != p.charAt(j)) {
                if (j != 0) j = pi[j - 1];
                else i++;
            }
        }
        return res.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA, GTAC; only ACGT is in b, so the answer is 1.
     */
    public static int sharedWords(String a, String b, int k) {
        // TODO Mission 4
        return -1;
    }
}
