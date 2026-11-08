import java.util.Arrays;

public class SuffixArray {
    public static int[] build(String text) {
        int n = text.length();

        if (n == 0) {
            return new int[0];
        }

        Integer[] suffixes = new Integer[n];
        int[] rank = new int[n];
        int[] nextRank = new int[n];

        for (int i = 0; i < n; i++) {
            suffixes[i] = i;
            rank[i] = text.charAt(i);
        }

        for (int step = 1; step < n; step *= 2) {
            final int currentStep = step;
            final int[] currentRank = rank;

            Arrays.sort(suffixes, (a, b) -> {
                if (currentRank[a] != currentRank[b]) {
                    return Integer.compare(currentRank[a], currentRank[b]);
                }

                int ra = a + currentStep < n
                        ? currentRank[a + currentStep] : -1;

                int rb = b + currentStep < n
                        ? currentRank[b + currentStep] : -1;

                return Integer.compare(ra, rb);
            });

            nextRank[suffixes[0]] = 0;

            for (int i = 1; i < n; i++) {
                int a = suffixes[i - 1];
                int b = suffixes[i];

                boolean different = rank[a] != rank[b];

                int aSecond = a + step < n ? rank[a + step] : -1;
                int bSecond = b + step < n ? rank[b + step] : -1;

                if (aSecond != bSecond) {
                    different = true;
                }

                nextRank[b] = nextRank[a] + (different ? 1 : 0);
            }

            int[] temp = rank;
            rank = nextRank;
            nextRank = temp;

            if (rank[suffixes[n - 1]] == n - 1) {
                break;
            }

            if (step > n / 2) {
                break;
            }
        }

        int[] result = new int[n];

        for (int i = 0; i < n; i++) {
            result[i] = suffixes[i];
        }

        return result;
    }
}