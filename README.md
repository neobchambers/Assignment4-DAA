# Assignment 4 — Text Analysis: Dynamic Programming and String Algorithms

## 1. Introduction

In this assignment, I implemented and analyzed different dynamic programming and string-processing algorithms using Java.

The main goal was to understand how these algorithms work, compare their theoretical time complexities, test their correctness, and measure their actual execution time.

I implemented the following six algorithms:

**Dynamic Programming:**
- Longest Common Subsequence (LCS)
- Edit Distance

**String Matching:**
- Naive String Matching
- Knuth–Morris–Pratt (KMP)
- Rabin–Karp

**Advanced String Algorithm:**
- Kasai's Longest Common Prefix (LCP) Algorithm

I also performed experiments with different input sizes, recorded execution times and operation counts, and created graphs to visualize the results.

## 2. Project Structure

I organized my project into separate Java classes to make the code easier to understand, test, and maintain.

```text
Assignment4/
│
├── src/
│   ├── Main.java
│   ├── DynamicProgramming.java
│   ├── StringMatching.java
│   ├── SuffixArray.java
│   ├── Kasai.java
│   ├── Tests.java
│   └── Benchmark.java
│
├── results/
│   └── results.csv
│
├── plots/
│   ├── dp_execution_time.png
│   ├── string_matching_execution_time.png
│   └── kasai_execution_time.png
│
└── README.md
```

### Class Responsibilities

| Class | Description |
|---|---|
| Main.java | Runs the tests and benchmarks |
| DynamicProgramming.java | Implements LCS and Edit Distance |
| StringMatching.java | Implements Naive, KMP, and Rabin–Karp |
| SuffixArray.java | Builds a suffix array |
| Kasai.java | Implements Kasai's LCP algorithm |
| Tests.java | Contains correctness tests and edge cases |
| Benchmark.java | Measures execution time and saves results to CSV |

## 3. Implementation Decisions and Assumptions

I used Java as the programming language and organized each algorithm in its own class.

My experimental setup uses the following configuration:

| Setting | Value |
|---|---|
| Programming language | Java |
| JDK version | JDK 17 or newer |
| Random seed | 20261108 |
| Random alphabet | A, B, C, D |
| Benchmark repetitions | 5 |
| Timing method | System.nanoTime() |
| Results format | CSV |

I used a fixed random seed to make the generated input data reproducible.

For the string-matching experiments, I used the same text and pattern when comparing the three algorithms.

I tested two situations:

1. The pattern is present in the text.
2. The pattern is absent from the text.

For the absent-pattern experiment, I used a pattern containing the character `Z`, which does not appear in my randomly generated text.

### Empty Pattern Handling

I decided that an empty pattern should match at every valid position in the text.

For example, if the text is `"abc"`, the result is:

`[0, 1, 2, 3]`

All three string-matching algorithms use this convention.

### Rabin–Karp Collision Handling

Rabin–Karp uses hashing to compare the pattern with text windows.

However, two different strings can sometimes have the same hash value. To avoid incorrect results, I compare the actual characters whenever the hashes match.

### Timing Decisions

I used `System.nanoTime()` to measure algorithm execution time.

I excluded the input generation and file writing from the timed sections.

Each experiment was repeated five times, and I calculated the average execution time.

For Kasai's algorithm, I prepared the suffix array before starting the timer. Therefore, the measured time includes LCP construction but excludes suffix-array construction.

## 4. Algorithm Overview

### 4.1 Longest Common Subsequence (LCS)

LCS finds the length of the longest subsequence shared by two strings.

The characters must appear in the same order, but they do not have to be next to each other.

I implemented LCS using dynamic programming and a two-dimensional table.

If the current characters are equal, I add one to the result from the previous diagonal cell:

`dp[i][j] = dp[i-1][j-1] + 1`

If the characters are different, I take the maximum of the previous row or previous column:

`dp[i][j] = max(dp[i-1][j], dp[i][j-1])`

The first row and column contain zeros because an empty string has no common subsequence with another string.

### 4.2 Edit Distance

Edit Distance calculates the minimum number of operations needed to transform one string into another.

The allowed operations are:

- Insertion
- Deletion
- Substitution

I used a dynamic programming table to store the minimum number of operations for smaller substrings.

For every cell, I compare three possibilities:

- Deletion: `dp[i-1][j] + 1`
- Insertion: `dp[i][j-1] + 1`
- Substitution: `dp[i-1][j-1] + cost`

The cost is zero when the characters are equal and one when they are different.

I choose the minimum of these three values.

### 4.3 Naive String Matching

Naive String Matching checks the pattern at every possible starting position in the text.

It compares the characters one by one.

If all characters match, the algorithm saves the starting index.

This algorithm is easy to understand and implement, but it can repeat many comparisons.

### 4.4 Knuth–Morris–Pratt (KMP)

KMP improves string matching by using an LPS array.

LPS means Longest Proper Prefix which is also a Suffix.

I first calculate the LPS array for the pattern. When a mismatch occurs, the algorithm uses the LPS values to avoid checking characters that have already been matched.

This reduces unnecessary comparisons and allows KMP to search in linear time.

### 4.5 Rabin–Karp

Rabin–Karp uses hashing to search for a pattern.

First, it calculates the hash of the pattern and the first text window.

Then it uses a rolling hash to calculate the next window's hash efficiently.

If the hashes match, the algorithm checks the actual characters to confirm the match.

This algorithm is usually efficient, but hash collisions can cause additional comparisons.

### 4.6 Kasai's Algorithm

Kasai's algorithm constructs an LCP array using a string and its suffix array.

LCP means Longest Common Prefix.

The algorithm compares neighboring suffixes in suffix-array order and stores the length of their common prefixes.

I used an inverse-rank array to find the position of each suffix in the suffix array.

Kasai's algorithm reuses the common-prefix length from the previous iteration, reducing unnecessary comparisons.

It can construct the LCP array in linear time when the suffix array is already available.

## 5. Complexity Analysis

In this section, I compare the theoretical time and auxiliary space complexity of all six required algorithms.

Let:
- `n` represent the length of the text or first string.
- `m` represent the length of the pattern or second string.

| Algorithm | Main operation | Best-case time | Worst-case time | Auxiliary space |
|---|---|---|---|---|
| LCS | Dynamic programming table | Θ(nm) | Θ(nm) | Θ(nm) |
| Edit Distance | Dynamic programming table | Θ(nm) | Θ(nm) | Θ(nm) |
| Naive Matching | Character-by-character comparison | O(n) | O(nm) | O(1) |
| KMP | LPS preprocessing and pattern searching | Θ(n + m) | Θ(n + m) | Θ(m) |
| Rabin–Karp | Rolling hash and character verification | Θ(n + m) | O(nm) | O(1) |
| Kasai | Reusing previous LCP values | Θ(n) | Θ(n) | Θ(n) |

The space complexities exclude the storage needed for the returned occurrence indices.

### Complexity Explanation

**LCS:** I fill a two-dimensional table with approximately `n × m` cells. Each cell is calculated once, so the time and space complexity are Θ(nm).

**Edit Distance:** I also fill a two-dimensional table. Each cell requires a constant number of operations, giving Θ(nm) time and space.

**Naive Matching:** The algorithm checks every possible starting position. In the best case, it fails on the first character at each position, resulting in O(n) time. In the worst case, it compares up to `m` characters at every position, resulting in O(nm).

**KMP:** Building the LPS array takes O(m) time, and searching takes O(n) time. The total time complexity is Θ(n + m). The LPS array requires Θ(m) space.

**Rabin–Karp:** Rolling-hash calculation takes linear time overall. When hash collisions are uncommon, the expected time is Θ(n + m). In the worst case, character verification can result in O(nm) time.

**Kasai:** Kasai's algorithm reuses previously calculated prefix information. Each character is processed a limited number of times, giving Θ(n) time and Θ(n) auxiliary space.

## 6. Testing Strategy and Results

I created a separate `Tests.java` class to check the correctness of the algorithms.

I tested normal cases and edge cases.

### 6.1 LCS Tests

| Test case | Input | Expected result |
|---|---|---|
| Identical strings | `"ABC"`, `"ABC"` | 3 |
| Different strings | `"ABC"`, `"XYZ"` | 0 |
| Empty string | `""`, `"ABC"` | 0 |

### 6.2 Edit Distance Tests

| Test case | Input | Expected result |
|---|---|---|
| Identical strings | `"cat"`, `"cat"` | 0 |
| Empty string | `""`, `"abc"` | 3 |
| Substitution | `"cat"`, `"cut"` | 1 |
| Insertion | `"cat"`, `"cats"` | 1 |
| Deletion | `"cats"`, `"cat"` | 1 |

### 6.3 String Matching Tests

I tested all three string-matching algorithms with the same inputs and checked that they returned the expected occurrence indices.

| Test case | Text | Pattern | Expected indices |
|---|---|---|---|
| Multiple occurrences | `"abcabc"` | `"abc"` | `[0, 3]` |
| Middle occurrence | `"xxabcxx"` | `"abc"` | `[2]` |
| End occurrence | `"xxabc"` | `"abc"` | `[2]` |
| Pattern absent | `"abcdef"` | `"xyz"` | `[]` |
| Pattern longer than text | `"abc"` | `"abcd"` | `[]` |
| Empty pattern | `"abc"` | `""` | `[0, 1, 2, 3]` |

I also verified that Naive, KMP, and Rabin–Karp return the same lists of indices.

### 6.4 Kasai Test

For Kasai's algorithm, I used the string `"banana"` with a manually verified suffix array.

| Position | Suffix starting index | Suffix |
|---|---:|---|
| 0 | 5 | a |
| 1 | 3 | ana |
| 2 | 1 | anana |
| 3 | 0 | banana |
| 4 | 4 | na |
| 5 | 2 | nana |

Expected suffix array:

`[5, 3, 1, 0, 4, 2]`

Expected LCP array:

`[0, 1, 3, 0, 0, 2]`

I compared the generated LCP array with this expected result.

### Testing Result

I ran the test suite before executing the benchmarks.

The console confirmed:

```text
All tests passed.
```

This means all the implemented test cases passed successfully.

## 7. Experimental Setup and Methodology

I performed experiments to compare the execution time and operation counts of the algorithms.

### 7.1 Input Sizes

I used the following input sizes:

| Algorithm | Input sizes |
|---|---|
| LCS | 100, 500, 1,000 |
| Edit Distance | 100, 500, 1,000 |
| String Matching | 10,000, 50,000, 100,000 |
| Kasai | 1,000, 5,000, 10,000 |

For LCS and Edit Distance, I generated two strings of equal length.

For string matching, I tested both present and absent patterns.

For every string-matching experiment, I used the same text and pattern for all three algorithms.

### 7.2 Timing Methodology

I used `System.nanoTime()` to measure execution time.

Each experiment was repeated five times.

I calculated the average execution time using:

`Average Time = Total Time of 5 Runs / 5`

All measurements are recorded in nanoseconds.

I generated the input data before starting the timer. I also excluded file writing and console output from the timed sections.

### 7.3 Operation Counts

I recorded operation counts to help understand the amount of work performed by each algorithm.

The counters represent:

- LCS: number of DP cells filled.
- Edit Distance: number of DP cells filled.
- Naive: character comparisons.
- KMP: character comparisons in the search loop.
- Rabin–Karp: character comparisons during hash-match verification.
- Kasai: character comparisons during LCP construction.

The Rabin–Karp counter does not include rolling-hash arithmetic operations, so its operation count is not directly equivalent to the other comparison counters.

## 8. Experimental Results

I saved the benchmark results in `results/results.csv`.

The following tables show the execution times and operation counts from my experiments.

### 8.1 LCS and Edit Distance

| Algorithm | Input size | Average time (ns) | Operations |
|---|---:|---:|---:|
| LCS | 100 | 1,218,957 | 10,000 |
| LCS | 500 | 5,074,562 | 250,000 |
| LCS | 1,000 | 1,704,242 | 1,000,000 |
| Edit Distance | 100 | 1,558,145 | 10,000 |
| Edit Distance | 500 | 5,068,729 | 250,000 |
| Edit Distance | 1,000 | 4,532,680 | 1,000,000 |

### 8.2 String Matching — Present Pattern

| Algorithm | Text length | Average time (ns) | Operations |
|---|---:|---:|---:|
| Naive | 10,000 | 969,218 | 13,374 |
| KMP | 10,000 | 852,832 | 12,485 |
| Rabin-Karp | 10,000 | 983,806 | 10 |
| Naive | 50,000 | 1,180,938 | 66,637 |
| KMP | 50,000 | 958,366 | 62,498 |
| Rabin-Karp | 50,000 | 908,444 | 10 |
| Naive | 100,000 | 615,242 | 133,326 |
| KMP | 100,000 | 691,953 | 124,518 |
| Rabin-Karp | 100,000 | 1,370,085 | 20 |

### 8.3 String Matching — Absent Pattern

| Algorithm | Text length | Average time (ns) | Operations |
|---|---:|---:|---:|
| Naive | 10,000 | 162,165 | 9,991 |
| KMP | 10,000 | 163,299 | 10,000 |
| Rabin-Karp | 10,000 | 550,390 | 0 |
| Naive | 50,000 | 236,294 | 49,991 |
| KMP | 50,000 | 225,610 | 50,000 |
| Rabin-Karp | 50,000 | 812,889 | 0 |
| Naive | 100,000 | 293,813 | 99,991 |
| KMP | 100,000 | 624,537 | 100,000 |
| Rabin-Karp | 100,000 | 2,705,851 | 0 |

### 8.4 Kasai's Algorithm

| Algorithm | Input size | Average time (ns) | Operations |
|---|---:|---:|---:|
| Kasai | 1,000 | 205,969 | 1,990 |
| Kasai | 5,000 | 789,827 | 9,989 |
| Kasai | 10,000 | 471,225 | 19,988 |

## 9. Graphical Results

I created the graphs manually and included them in the `plots` folder of my project.

I used the benchmark results from `results.csv` to create the graphs. 

For all execution-time graphs:
- The X-axis represents the input size or text length.
- The Y-axis represents average execution time in nanoseconds.
- Each line represents an algorithm.

## 10. Discussion of Results

### 10.1 Dynamic Programming Algorithms

LCS and Edit Distance both have theoretical time complexity Θ(nm).

My operation counts confirm that both algorithms process 10,000 cells for input size 100, 250,000 cells for size 500, and 1,000,000 cells for size 1,000.

This demonstrates the expected quadratic growth when both input strings have equal lengths.

However, the measured execution times do not increase perfectly with the input size. For example, the LCS measurement for size 1,000 is lower than the measurement for size 500.

This can happen because of Java Virtual Machine optimization, garbage collection, operating-system scheduling, and other runtime conditions.

### 10.2 String Matching Algorithms

Naive String Matching is simple, but it can perform many repeated character comparisons.

KMP uses the LPS array to avoid unnecessary comparisons and has linear theoretical time complexity.

Rabin–Karp uses rolling hashes to compare text windows. Its expected time complexity is linear, but its performance can depend on hash verification and the input data.

My measurements show that no algorithm is always the fastest in every experiment.

The present-pattern and absent-pattern tests also produce different results because the algorithms process the pattern differently depending on whether it occurs in the text.

In particular, my absent pattern contains `Z`, which is not in the text alphabet. This makes it a useful test for checking how quickly the algorithms reject a pattern that cannot occur.

### 10.3 Kasai's Algorithm

Kasai's algorithm has theoretical time complexity Θ(n).

The operation counts increase from 1,990 to 9,989 and then to 19,988 as the input size increases.

This supports the expected linear growth in character comparisons.

The measured execution times do not increase perfectly proportionally, which can be explained by runtime conditions and measurement variability.

### 10.4 Theoretical Complexity vs Actual Execution Time

Big-O notation describes how the amount of work grows as input size increases.

Actual execution time also depends on the computer, memory allocation, JVM optimization, garbage collection, and implementation details.

Therefore, an algorithm with a better theoretical complexity is not necessarily faster for every small input.

I used both execution-time measurements and operation counts to understand the performance of the algorithms.

The benchmark uses five repetitions but does not include a separate JVM warm-up phase, so the measurements should be treated as experimental observations rather than perfectly stable performance values.

## 11. Conclusion

In this assignment, I implemented six algorithms related to dynamic programming and string processing.

I learned how LCS and Edit Distance solve problems using dynamic programming tables. Both algorithms have quadratic time and space complexity.

I also compared three string-matching algorithms.

Naive String Matching is easy to implement but can perform many repeated comparisons. KMP uses an LPS array to avoid unnecessary work and guarantees linear search time. Rabin–Karp uses rolling hashes and can be efficient on average, although hash collisions can lead to additional verification work.

Finally, I implemented Kasai's algorithm and verified its LCP output using a manually checked suffix array.

The experiments helped me understand the difference between theoretical complexity and actual execution time.

The main conclusions are:

- Dynamic programming is useful for solving problems with overlapping subproblems, but it can require significant time and memory.
- KMP improves string matching by avoiding repeated comparisons.
- Rabin–Karp provides a hash-based alternative to direct character comparison.
- Naive matching is simple but can be inefficient for larger inputs.
- Kasai's algorithm constructs an LCP array in linear time when the suffix array is already available.

Overall, this assignment helped me understand how to implement, test, analyze, and compare different algorithms in Java.

## 12. How to Compile and Run

I used JDK 17 or newer to compile and run my project.

To compile the Java files, I used:

```bash
javac -d out src/*.java
```

To run the program:

```bash
java -cp out Main
```

The program first runs the correctness tests and then starts the benchmarks.

Expected console output:

```text
All tests passed.
Benchmark results saved to results/results.csv
Assignment 4 completed.
```

The benchmark results are saved automatically in the `results` folder.

The graphs are included in the `plots` folder and were created manually using the experimental data.

## 13. Submission Checklist

My project contains:

- Java source code organized into separate classes.
- Implementations of all six required algorithms.
- Correctness tests and edge-case tests.
- Experimental results saved in `results.csv`.
- Execution-time graphs in the `plots` folder.
- A complete README report.
- Instructions for compiling and running the project.

I also organized the project using separate classes and meaningful file names to make it easier to understand and maintain.# Assignment4-DAA