import java.util.ArrayList;
import java.util.List;

public class StringMatching {
    public static long comparisons;

    public static List<Integer> naive(String text, String pattern) {
        comparisons = 0;
        List<Integer> result = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                result.add(i);
            }
            return result;
        }

        if (m > n) {
            return result;
        }

        for (int i = 0; i <= n - m; i++) {
            int j = 0;

            while (j < m) {
                comparisons++;

                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }

                j++;
            }

            if (j == m) {
                result.add(i);
            }
        }

        return result;
    }

    private static int[] buildLps(String pattern) {
        int[] lps = new int[pattern.length()];
        int length = 0;
        int i = 1;

        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else if (length > 0) {
                length = lps[length - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }

        return lps;
    }

    public static List<Integer> kmp(String text, String pattern) {
        comparisons = 0;
        List<Integer> result = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                result.add(i);
            }
            return result;
        }

        if (m > n) {
            return result;
        }

        int[] lps = buildLps(pattern);
        int i = 0;
        int j = 0;

        while (i < n) {
            comparisons++;

            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;

                if (j == m) {
                    result.add(i - j);
                    j = lps[j - 1];
                }
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }

        return result;
    }

    public static List<Integer> rabinKarp(String text, String pattern) {
        comparisons = 0;
        List<Integer> result = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                result.add(i);
            }
            return result;
        }

        if (m > n) {
            return result;
        }

        final long base = 256;
        final long mod = 1_000_000_007L;

        long highestPower = 1;

        for (int i = 1; i < m; i++) {
            highestPower = (highestPower * base) % mod;
        }

        long patternHash = 0;
        long windowHash = 0;

        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * base + pattern.charAt(i)) % mod;
            windowHash = (windowHash * base + text.charAt(i)) % mod;
        }

        for (int i = 0; i <= n - m; i++) {
            if (patternHash == windowHash) {
                int j = 0;

                while (j < m) {
                    comparisons++;

                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        break;
                    }

                    j++;
                }

                if (j == m) {
                    result.add(i);
                }
            }

            if (i < n - m) {
                windowHash = (
                        windowHash - text.charAt(i) * highestPower % mod + mod
                ) % mod;

                windowHash = (
                        windowHash * base + text.charAt(i + m)
                ) % mod;
            }
        }

        return result;
    }
}