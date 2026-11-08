public class Kasai {
    public static long comparisons;

    public static int[] buildLcp(String text, int[] suffixArray) {
        comparisons = 0;

        int n = text.length();

        if (suffixArray.length != n) {
            throw new IllegalArgumentException(
                    "Suffix array length must equal text length."
            );
        }

        int[] rank = new int[n];
        int[] lcp = new int[n];

        for (int i = 0; i < n; i++) {
            rank[suffixArray[i]] = i;
        }

        int common = 0;

        for (int i = 0; i < n; i++) {
            int r = rank[i];

            if (r == 0) {
                common = 0;
                continue;
            }

            int previousSuffix = suffixArray[r - 1];

            while (i + common < n && previousSuffix + common < n) {
                comparisons++;

                if (text.charAt(i + common)
                        != text.charAt(previousSuffix + common)) {
                    break;
                }

                common++;
            }

            lcp[r] = common;

            if (common > 0) {
                common--;
            }
        }

        return lcp;
    }
}