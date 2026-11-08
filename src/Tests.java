import java.util.Arrays;
import java.util.List;

public class Tests {
    public static void runAll() {
        testLcs();
        testEditDistance();
        testStringMatching();
        testKasai();

        System.out.println("All tests passed.");
    }

    private static void testLcs() {
        check(
                DynamicProgramming.lcs("ABC", "ABC") == 3,
                "LCS identical"
        );

        check(
                DynamicProgramming.lcs("ABC", "XYZ") == 0,
                "LCS different"
        );

        check(
                DynamicProgramming.lcs("", "ABC") == 0,
                "LCS empty"
        );
    }

    private static void testEditDistance() {
        check(
                DynamicProgramming.editDistance("cat", "cat") == 0,
                "Edit identical"
        );

        check(
                DynamicProgramming.editDistance("", "abc") == 3,
                "Edit empty"
        );

        check(
                DynamicProgramming.editDistance("cat", "cut") == 1,
                "Edit substitution"
        );

        check(
                DynamicProgramming.editDistance("cat", "cats") == 1,
                "Edit insertion"
        );

        check(
                DynamicProgramming.editDistance("cats", "cat") == 1,
                "Edit deletion"
        );
    }

    private static void testStringMatching() {
        checkMatchers(
                "abcabc", "abc", List.of(0, 3), "multiple occurrences"
        );

        checkMatchers(
                "xxabcxx", "abc", List.of(2), "middle"
        );

        checkMatchers(
                "xxabc", "abc", List.of(2), "end"
        );

        checkMatchers(
                "abcdef", "xyz", List.of(), "absent"
        );

        checkMatchers(
                "abc", "abcd", List.of(), "pattern longer than text"
        );

        checkMatchers(
                "abc", "", List.of(0, 1, 2, 3), "empty pattern"
        );
    }

    private static void checkMatchers(
            String text,
            String pattern,
            List<Integer> expected,
            String testName
    ) {
        check(
                StringMatching.naive(text, pattern).equals(expected),
                "Naive " + testName
        );

        check(
                StringMatching.kmp(text, pattern).equals(expected),
                "KMP " + testName
        );

        check(
                StringMatching.rabinKarp(text, pattern).equals(expected),
                "Rabin-Karp " + testName
        );
    }

    private static void testKasai() {
        int[] suffixArray = {5, 3, 1, 0, 4, 2};
        int[] expectedLcp = {0, 1, 3, 0, 0, 2};

        check(
                Arrays.equals(
                        Kasai.buildLcp("banana", suffixArray),
                        expectedLcp
                ),
                "Kasai banana"
        );
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Test failed: " + message);
        }
    }
}