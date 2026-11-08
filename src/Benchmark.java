import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class Benchmark {
    private static final long SEED = 20261108L;
    private static final int RUNS = 5;
    private static final String ALPHABET = "ABCD";

    public static void run() throws IOException {
        Random random = new Random(SEED);

        Path output = Path.of("results", "results.csv");
        Files.createDirectories(output.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            writer.write(
                    "algorithm,input_size,test_case,average_time_ns,operations"
            );
            writer.newLine();

            int[] dpSizes = {100, 500, 1000};

            for (int size : dpSizes) {
                String a = randomString(size, random);
                String b = randomString(size, random);

                long time = averageTime(
                        () -> DynamicProgramming.lcs(a, b)
                );

                writeRow(
                        writer, "LCS", size, "random",
                        time, DynamicProgramming.operations
                );

                time = averageTime(
                        () -> DynamicProgramming.editDistance(a, b)
                );

                writeRow(
                        writer, "EditDistance", size, "random",
                        time, DynamicProgramming.operations
                );
            }

            int[] textSizes = {10000, 50000, 100000};
            int patternLength = 10;

            for (int size : textSizes) {
                String text = randomString(size, random);

                String present = text.substring(
                        size / 2,
                        size / 2 + patternLength
                );

                String absent = "Z".repeat(patternLength);

                benchmarkMatcher(
                        writer, "Naive", text, present, size, "present"
                );

                benchmarkMatcher(
                        writer, "KMP", text, present, size, "present"
                );

                benchmarkMatcher(
                        writer, "RabinKarp", text, present, size, "present"
                );

                benchmarkMatcher(
                        writer, "Naive", text, absent, size, "absent"
                );

                benchmarkMatcher(
                        writer, "KMP", text, absent, size, "absent"
                );

                benchmarkMatcher(
                        writer, "RabinKarp", text, absent, size, "absent"
                );
            }

            int[] kasaiSizes = {1000, 5000, 10000};

            for (int size : kasaiSizes) {
                String text = randomString(size, random);
                int[] suffixArray = SuffixArray.build(text);

                long time = averageTime(
                        () -> Kasai.buildLcp(text, suffixArray)
                );

                writeRow(
                        writer, "Kasai", size, "random",
                        time, Kasai.comparisons
                );
            }
        }

        System.out.println("Benchmark results saved to " + output);
    }

    private static void benchmarkMatcher(
            BufferedWriter writer,
            String algorithm,
            String text,
            String pattern,
            int size,
            String testCase
    ) throws IOException {

        Runnable task;

        if (algorithm.equals("Naive")) {
            task = () -> StringMatching.naive(text, pattern);
        } else if (algorithm.equals("KMP")) {
            task = () -> StringMatching.kmp(text, pattern);
        } else {
            task = () -> StringMatching.rabinKarp(text, pattern);
        }

        long time = averageTime(task);

        writeRow(
                writer, algorithm, size, testCase,
                time, StringMatching.comparisons
        );
    }

    private static long averageTime(Runnable task) {
        long total = 0;

        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();

            task.run();

            total += System.nanoTime() - start;
        }

        return total / RUNS;
    }

    private static String randomString(int length, Random random) {
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            result.append(
                    ALPHABET.charAt(random.nextInt(ALPHABET.length()))
            );
        }

        return result.toString();
    }

    private static void writeRow(
            BufferedWriter writer,
            String algorithm,
            int size,
            String testCase,
            long time,
            long operations
    ) throws IOException {

        writer.write(
                algorithm + "," +
                        size + "," +
                        testCase + "," +
                        time + "," +
                        operations
        );

        writer.newLine();
    }
}