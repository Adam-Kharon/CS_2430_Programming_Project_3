package project3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

 /**
 * @Team_Name Team-2
 * @Team_Members Adam-Sarraj, Danielle-Major, Mustafa-Alrubaye
 * @Course_And_Section CS 2430 / 001
 * @Project_Name Programming Project 3, Optimal selection - Fall 2026
 * @author Danielle-Major
 *
 * CS 2430 Project 3
 *
 * This program determines the best set of experiments to include
 * in a spacecraft payload. The goal is to maximize the total
 * science rating while keeping the payload at or below 700 kg. 
 * Each experiment can either be included or excluded, but not partially included.
 * This is known as the 0/1 knapsack problem. 
 *
 * The program implements:
 * - Brute Force Search
 * - Greedy Strategies
 * - Dynamic Programming
 *
 * The revised project requirements introduce 3 rules:
 * 1. A solar experiment limit
 *    a. At most two of experiments 2, 3, and 7 can be selected.    
 * 2. A thermal mass credit
 *    b. Selecting both 6 and 12 gives a 7 kg thermal mass credit.
 * 3. An Experiment 11a prerequisite
 *
 * All payloads are evaluated using the same evaluate() method
 * to ensure every algorithm follows the same rules.
 */
 

public class Main {
    // The total weight limit allowed on the spaceship. This limit
    // applies after any thermal credit has been subtracted.
    static final int CAPACITY = 700;

    // These are parallel arrays: the same index describes the same experiment
    // in all four arrays. For example, index 11 is experiment 11a, not ID 12.
    static final String[] ids = {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "11a", "12"
    };
    static final String[] names = {
        "Cloud Patterns", "Solar Flares", "Solar Power", "Binary Stars",
        "Relativity", "Seed Viability", "Sun Spots", "Mice Tumors",
        "Microgravity Plant Growth", "Micrometeorites", "Cosmic Rays (Primary Band)",
        "Cosmic Rays (Secondary Band)", "Yeast Fermentation"
    };
    static final int[] weights = {36, 264, 188, 203, 104, 7, 90, 65, 75, 170, 80, 10, 27};
    static final int[] ratings = {5, 9, 6, 8, 8, 4, 2, 8, 5, 9, 7, 5, 4};
    static final int N = ids.length;

    // Named array indices make the experiments involved in the new rules easier to
    // recognize for the evaluator. These are zero-based indices, not the experiment ID numbers.
    static final int SOLAR_FLARES = 1;
    static final int SOLAR_POWER = 2;
    static final int SEED = 5;
    static final int SUN_SPOTS = 6;
    static final int PRIMARY = 10;
    static final int SECONDARY = 11;
    static final int YEAST = 12;

 /**
 * A mask is a number that keeps track of which experiments are chosen.
 * Each bit in the number matches an index in the experiment arrays:
 *
 *   1 = selected
 *   0 = not selected
 *
 * The bits are read from right to left, starting at index 0, just like
 * array indexes.
 *
 * Example:
 *
 *   array index:  2 1 0
 *   mask bits:    1 0 1
 *
 * In this example, the experiments at indexes 0 and 2 are selected.
 * If those experiments have IDs 1 and 3, then IDs 1 and 3 are chosen.
 *
 * The program uses 13 bits total because there are 13 experiments.
 *
 * The bit(index) method creates a mask with only one bit turned on.
 * It uses the << (left shift) operator to move a 1 to the correct
 * position.
 *
 * Examples:
 *   bit(0) -> 001
 *   bit(1) -> 010
 *   bit(2) -> 100
 */
    static int bit(int index) {
        return 1 << index;
    }

    /**
     * Keeps the selection and calculated totals together so every strategy
     * returns the same kind of result.
     */
    static class Result {
        final int mask;            // The chosen experiments, stored as bits
        final int baselineWeight;  // Mass before the thermal credit
        final int totalWeight;     // Adjusted mass used for the capacity check
        final int totalRating;     // Sum of the selected science ratings
        final boolean feasible;    // True only when the selection follows all rules

        Result(int mask, int baselineWeight, int totalWeight, int totalRating, boolean feasible) {
            this.mask = mask;
            this.baselineWeight = baselineWeight;
            this.totalWeight = totalWeight;
            this.totalRating = totalRating;
            this.feasible = feasible;
        }
    }

    /**
     * Evaluate one complete candidate selection.
     *
     * First add the baseline masses and ratings of all selected experiments.
     * Then apply the thermal credit, if earned, and check the capacity, solar
     * limit, and 11a prerequisite. Keeping these checks in one method means
     * the greedy, brute-force, and DP approaches use the same rules!s
     */
    static Result evaluate(int mask) {
        // A valid mask only uses the 13 bits belonging to our experiments.
        // Shifting right by N places leaves zero if there are no extra bits.
        if (mask < 0 || (mask >>> N) != 0) {
            throw new IllegalArgumentException("Invalid experiment mask: " + mask);
        }
        int baseline = 0;
        int rating = 0;
        for (int i = 0; i < N; i++) {
            // & compares the selected bits in mask with the single bit for i.
            // The result is nonzero if that experiment's bit is turned on.
            // Example: binary 101 & 100 = 100, so index 2 is selected.
            if ((mask & bit(i)) != 0) {
                baseline += weights[i];
                rating += ratings[i];
            }
        }
        int weight = baseline;
        // The credit applies once only when both 6 and 12 are present.
        // Their science ratings are still counted in full.
        if ((mask & bit(SEED)) != 0 && (mask & bit(YEAST)) != 0) {
            weight -= 7;
        }
        // | combines the three solar bits. Then & keeps only those bits from
        // the candidate mask, so bitCount can count the solar selections.
        int solar = mask & (bit(SOLAR_FLARES) | bit(SOLAR_POWER) | bit(SUN_SPOTS));
        // The prerequisite is met when 11a is absent or 11 is present.
        boolean prerequisite = (mask & bit(SECONDARY)) == 0 || (mask & bit(PRIMARY)) != 0;
        boolean feasible = weight <= CAPACITY && Integer.bitCount(solar) <= 2 && prerequisite;
        return new Result(mask, baseline, weight, rating, feasible);
    }

    /**
     * Shared selection loop for all three greedy strategies.
     *
     * The comparator decides the order. The method tries each experiment in
     * that order and keeps it only if the entire new manifest is feasible.
     * It does not remove earlier choices to make room for a later one, so a
     * greedy result is not guaranteed to be the best possible result.
     *
     * If 11a appears before 11, wait until 11 is actually added, then try
     * adding 11a immediately. No space is reserved while 11a is waiting; if
     * 11 is skipped or 11a no longer fits, 11a is not selected.
     */
    static Result greedy(Comparator<Integer> ranking) {
        // Sort indices instead of moving the data in the parallel arrays.
        Integer[] order = new Integer[N];
        for (int i = 0; i < N; i++) {
            order[i] = i;
        }
        // Original array order is the final tie-break for repeatable results.
        Arrays.sort(order, ranking.thenComparingInt(i -> i));
        // Zero has no bits turned on, so the starting selection is empty.
        int mask = 0;
        boolean secondaryDeferred = false;
        for (int index : order) {
            if (index == SECONDARY && (mask & bit(PRIMARY)) == 0) {
                secondaryDeferred = true;
                continue;
            }
            // | makes a tentative mask with this experiment added, without
            // turning off the bits for experiments already selected.
            // Example: binary 001 | 100 = 101 (indices 0 and 2 selected).
            int candidate = mask | bit(index);
            if (evaluate(candidate).feasible) {
                mask = candidate;
            }
            // Only try a deferred 11a after its required experiment is selected.
            if (index == PRIMARY && secondaryDeferred && (mask & bit(PRIMARY)) != 0) {
                candidate = mask | bit(SECONDARY);
                if (evaluate(candidate).feasible) {
                    mask = candidate;
                }
                secondaryDeferred = false;
            }
        }
        return evaluate(mask);
    }

    /**
     * Greedy 1: rank by science rating from highest to lowest.
     * For equal ratings, try the lighter baseline mass first.
     */
    static Result greedyHighestRating() {
        return greedy(Comparator.<Integer>comparingInt(i -> ratings[i]).reversed()
            .thenComparingInt(i -> weights[i]));
    }

    /**
     * Greedy 2: rank by baseline mass from lightest to heaviest.
     * For equal masses, try the higher rating first.
     */
    static Result greedyLightestFirst() {
        return greedy(Comparator.<Integer>comparingInt(i -> weights[i])
            .thenComparing(Comparator.<Integer>comparingInt(i -> ratings[i]).reversed()));
    }

    /**
     * Greedy 3: rank by rating divided by baseline mass, highest first.
     * For equal ratios, try the lighter baseline mass first.
     */
    static Result greedyBestRatio() {
        return greedy((a, b) -> {
            // Cross-multiplication compares the ratios without rounding.
            // A long is used for the products to avoid int multiplication overflow.
            int comparison = Long.compare((long) ratings[b] * weights[a],
                                          (long) ratings[a] * weights[b]);
            return comparison != 0 ? comparison : Integer.compare(weights[a], weights[b]);
        });
    }

    /**
     * Brute force: check every possible selection and return the top three.
     *
     * Each of the 13 bits can be on or off, so there are 2^13 = 8,192
     * candidates. This count includes candidates that break the rules. Unlike
     * greedy, brute force does not commit to an early choice, so the first
     * feasible result after sorting is guaranteed to have the best rating.
     */
    static List<Result> bruteForce() {
        List<Result> valid = new ArrayList<>();
        // 1 << N is 2^13, or 8192. Counting from 0 to 8191 visits every
        // possible pattern of 13 bits, including invalid selections.
        for (int mask = 0; mask < (1 << N); mask++) {
            Result result = evaluate(mask);
            if (result.feasible) {
                valid.add(result);
            }
        }
        // Highest rating comes first. Break rating ties using the lighter
        // adjusted mass, then the mask value for a consistent order.
        valid.sort(Comparator.<Result>comparingInt(result -> result.totalRating).reversed()
            .thenComparingInt(result -> result.totalWeight)
            .thenComparingInt(result -> result.mask));
        // Return up to three results, even if fewer than three were feasible.
        return new ArrayList<>(valid.subList(0, Math.min(3, valid.size())));
    }

    /**
     * Extra credit: dynamic programming with groups of related experiments.
     *
     * A normal knapsack table treats experiments as independent, which would
     * miss the new solar limit, thermal credit, and prerequisite. Instead,
     * each group below lists its allowed combinations. The table chooses one
     * combination from each group and finds the best rating for each capacity.
     *
     * For example, the solar group lists singles and pairs but not all three.
     * The seed/yeast group includes their combined choice so evaluate() can
     * apply the credit. The cosmic-ray group allows 11 alone or both 11 and
     * 11a, but not 11a alone. Unrelated experiments have their own groups.
     */
    static Result dynamicProgramming() {
        // Each row lists the masks that can be chosen from that group.
        // Zero means no experiments from that group are selected.
        int[][] groups = {
            {0, bit(0)},
            {0, bit(SOLAR_FLARES), bit(SOLAR_POWER), bit(SUN_SPOTS),
                bit(SOLAR_FLARES) | bit(SOLAR_POWER), bit(SOLAR_FLARES) | bit(SUN_SPOTS),
                bit(SOLAR_POWER) | bit(SUN_SPOTS)},
            {0, bit(3)}, {0, bit(4)},
            {0, bit(SEED), bit(YEAST), bit(SEED) | bit(YEAST)},
            {0, bit(7)}, {0, bit(8)}, {0, bit(9)},
            {0, bit(PRIMARY), bit(PRIMARY) | bit(SECONDARY)}
        };
        // dp[group][capacity] is the best rating using the first group groups
        // with that much capacity. choices records how to rebuild the answer.
        int[][] dp = new int[groups.length + 1][CAPACITY + 1];
        int[][] choices = new int[groups.length + 1][CAPACITY + 1];
        for (int group = 1; group <= groups.length; group++) {
            for (int capacity = 0; capacity <= CAPACITY; capacity++) {
                for (int choice : groups[group - 1]) {
                    Result part = evaluate(choice);
                    if (part.totalWeight <= capacity) {
                        // Combine this choice with the best earlier groups
                        // that fit in the remaining capacity.
                        int value = dp[group - 1][capacity - part.totalWeight] + part.totalRating;
                        if (value > dp[group][capacity]) {
                            dp[group][capacity] = value;
                            choices[group][capacity] = choice;
                        }
                    }
                }
            }
        }
        // Work backward through the recorded choices. Each choice is a mask,
        // and |= combines its selected bits with the ones already collected.
        int capacity = CAPACITY;
        int mask = 0;
        for (int group = groups.length; group > 0; group--) {
            int choice = choices[group][capacity];
            mask |= choice;
            capacity -= evaluate(choice).totalWeight;
        }
        // Check the reconstructed result rather than assuming it is correct.
        Result result = evaluate(mask);
        if (!result.feasible || result.totalRating != dp[groups.length][CAPACITY]) {
            throw new IllegalStateException("Dynamic programming returned an invalid result");
        }
        return result;
    }

 /**
 * Creates a comma-separated String of the IDs for all experiments
 * selected in the mask.
 *
 * The method checks each bit in the mask. If the bit is 1, the
 * experiment at that index is selected, so its ID is added to the
 * list. The selected IDs are then joined together and returned as
 * one String.
 */
    static String selectedIds(int mask) {
        List<String> selected = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if ((mask & bit(i)) != 0) {
                selected.add(ids[i]);
            }
        }
        return String.join(", ", selected);
    }

    /**
     * Print one strategy's selection and totals. Individual experiment masses
     * are baseline values, the adjusted total is shown separately.
     */
    static void printResult(String title, Result result) {
        System.out.println("\n" + title);
        System.out.printf("  IDs: %s%n", selectedIds(result.mask));
        System.out.printf("  Baseline mass: %d kg; adjusted mass: %d kg / %d kg%n",
            result.baselineWeight, result.totalWeight, CAPACITY);
        System.out.printf("  Total science rating: %d%n", result.totalRating);
        for (int i = 0; i < N; i++) {
            if ((result.mask & bit(i)) != 0) {
                System.out.printf("    %-3s %-32s %3d kg  rating %d%n",
                    ids[i], names[i], weights[i], ratings[i]);
            }
        }
    }

    /** Show how many rating points a strategy is below the brute-force best. */
    static void printComparison(String name, Result result, int optimal) {
        System.out.printf("  %-24s %2d points; gap %d; %s optimum%n",
            name, result.totalRating, optimal - result.totalRating,
            result.totalRating == optimal ? "matches" : "below");
    }

    /**
     * Run all five approaches, print their results, and compare their ratings.
     * Brute force is the reference because it checks every possible selection.
     */
    public static void main(String[] args) {
        Result highest = greedyHighestRating();
        Result lightest = greedyLightestFirst();
        Result ratio = greedyBestRatio();
        List<Result> brute = bruteForce();
        Result dp = dynamicProgramming();

        System.out.println("CS 2430 Project 3: Revised Payload Manifest");
        printResult("Greedy: highest rating first", highest);
        printResult("Greedy: lightest first", lightest);
        printResult("Greedy: best rating-to-baseline-mass ratio", ratio);
        System.out.printf("%nBrute force examined %d binary selections.%n", 1 << N);
        for (int i = 0; i < brute.size(); i++) {
            printResult("Brute force rank " + (i + 1) + (i == 0 ? " (optimal)" : ""), brute.get(i));
        }
        printResult("Dynamic programming", dp);
        // The DP result should agree with the exhaustive search on the best
        // rating, even if it happens to choose a different tied selection.
        if (dp.totalRating != brute.get(0).totalRating) {
            throw new IllegalStateException("Dynamic programming disagrees with brute force");
        }
        int optimal = brute.get(0).totalRating;
        System.out.println("\nComparison with brute force:");
        printComparison("Highest rating", highest, optimal);
        printComparison("Lightest first", lightest, optimal);
        printComparison("Best ratio", ratio, optimal);
        printComparison("Dynamic programming", dp, optimal);
    }
}
