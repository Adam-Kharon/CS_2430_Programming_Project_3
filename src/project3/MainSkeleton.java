package project3;
import java.util.*;

/**
 * CS 2430 — Programming Project 3: Optimal Selection (0/1 Knapsack Problem)
 * 
 * Problem: Select science experiments for a satellite payload up to 700 kg capacity
 * to maximize research rating. Experiments are indivisible (0/1 constraint).
 * 
 * Solution Approaches:
 * - Part 1: Three greedy heuristics (rating, weight, ratio-based)
 * - Part 2: Brute force exhaustive search (2^12 = 4096 subsets)
 * - Part 3: Comparison and output formatting
 * - Part 4: Dynamic programming (extra credit with pseudocode minimum)
 * 
 * @Team_Name Team-2
 * @Team_Members Adam-Sarraj, Danielle-Major, Mustafa-Alrubaye
 * @Course_And_Section CS 2430 / 001
 * @Project_Name Programming Project 3, Optimal selection - Fall 2026
 * @author Danielle-Major
 */
public class MainSkeleton {
    /** Maximum satellite payload capacity in kilograms. */
    static final int CAPACITY = 700;
    
    /** Number of candidate experiments available for selection. */
    static final int N = 12;

    /** 
     * Experiment names. Index i corresponds to weights[i] and ratings[i].
     * All 12 experiments are hard-coded as per project requirements.
     */
    static String[] names = {
        "Cloud Patterns", "Solar Flares", "Solar Power", "Binary Stars",
        "Relativity", "Seed Viability", "Sun Spots", "Mice Tumors",
        "Microgravity Plant Growth", "Micrometeorites", "Cosmic Rays", "Yeast Fermentation"
    };
    
    /**
     * Weight (kg) of each experiment. Index i corresponds to names[i] and ratings[i].
     * Constraint: total weight of selected experiments must not exceed CAPACITY (700 kg).
     */
    static int[] weights = {36, 264, 188, 203, 104, 7, 90, 65, 75, 170, 80, 27};
    
    /**
     * Research rating (quality score) for each experiment. Index i corresponds to names[i] and weights[i].
     * Objective: maximize total rating subject to weight constraint.
     */
    static int[] ratings = {5, 9, 6, 8, 8, 4, 2, 8, 5, 9, 7, 4};

    /**
     * Result encapsulates the output of an algorithm:
     * - chosen: indices of selected experiments (0-based)
     * - count: number of experiments selected
     * - totalWeight: sum of weights of chosen experiments
     * - totalRating: sum of ratings of chosen experiments
     */
    static class Result {
        /** Indices of chosen experiments (references names[], weights[], ratings[]). */
        int[] chosen;
        
        /** Count of chosen experiments. */
        int count;
        
        /** Total weight (kg) of chosen experiments; must satisfy: totalWeight <= CAPACITY. */
        int totalWeight;
        
        /** Total rating of chosen experiments; objective is to maximize this. */
        int totalRating;

        /**
         * Constructs a Result with final values.
         * 
         * @param chosen array of experiment indices selected
         * @param count number of chosen experiments
         * @param totalWeight sum of weights of chosen experiments (kg)
         * @param totalRating sum of ratings of chosen experiments
         */
        Result(int[] chosen, int count, int totalWeight, int totalRating) {
            this.chosen = chosen;
            this.count = count;
            this.totalWeight = totalWeight;
            this.totalRating = totalRating;
        }
    }

    /**
     * Greedy Strategy 1: Highest Rating First
     * 
     * Sort experiments by rating in descending order, then greedily add experiments
     * to the payload as long as total weight does not exceed CAPACITY.
     * 
     * Expected behavior: prioritizes high-value experiments regardless of weight.
     * 
     * @return Result containing chosen experiments, count, total weight, and total rating.
     *         Returns null if no valid selection can be made (should not occur with this data).
     */
    static Result greedyHighestRating() {
        // TODO: sort indices by rating (desc), pick while weight <= CAPACITY
        return null;
    }

    /**
     * Greedy Strategy 2: Lightest First
     * 
     * Sort experiments by weight in ascending order, then greedily add experiments
     * to the payload as long as total weight does not exceed CAPACITY.
     * 
     * Expected behavior: packs lighter experiments first, maximizing quantity over quality.
     * 
     * @return Result containing chosen experiments, count, total weight, and total rating.
     *         Returns null if no valid selection can be made (should not occur with this data).
     */
    static Result greedyLightestFirst() {
        // TODO: sort indices by weight (asc), pick while weight <= CAPACITY
        return null;
    }

    /**
     * Greedy Strategy 3: Best Rating-to-Weight Ratio First
     * 
     * Compute rating/weight ratio for each experiment, sort by ratio in descending order,
     * then greedily add experiments to the payload as long as total weight does not exceed CAPACITY.
     * 
     * Expected behavior: balances quality and efficiency, selecting "best value" experiments.
     * Note: This often produces near-optimal results for knapsack-like problems.
     * 
     * @return Result containing chosen experiments, count, total weight, and total rating.
     *         Returns null if no valid selection can be made (should not occur with this data).
     */
    static Result greedyBestRatio() {
        // TODO: sort indices by ratio (desc), pick while weight <= CAPACITY
        return null;
    }

    /**
     * Brute Force / Exhaustive Search: Find Optimal Selection(s)
     * 
     * Enumerate all 2^N = 4096 possible subsets of experiments using a bitmask approach.
     * For each subset, compute total weight and rating. Track the maximum feasible rating
     * (weight <= CAPACITY) and identify all subsets achieving that maximum (handle ties).
     * 
     * Expected behavior: returns the true optimal solution(s) to compare greedy strategies against.
     * This is computationally feasible for N=12 but impractical for larger datasets.
     * 
     * @return List of Result objects representing all optimal subsets (typically 1, but may be > 1 if tied).
     *         Returns empty list if no feasible subset exists (should not occur with this data).
     */
    static List<Result> bruteForce() {
        // TODO: loop mask 0..(1<<N)-1, track best feasible rating and ties
        return new ArrayList<>();
    }

    /**
     * Dynamic Programming Solution: Optimal Selection (Extra Credit)
     * 
     * Build a 0/1 knapsack DP table: dp[i][w] = max rating achievable using first i experiments
     * and at most w kg of weight. Backtrack from dp[N][CAPACITY] to recover the chosen experiments.
     * 
     * DP Pseudocode:
     *   1. Initialize dp[0..N][0..CAPACITY] with zeros.
     *   2. For each experiment i from 1 to N:
     *        For each weight w from 0 to CAPACITY:
     *          if weights[i-1] <= w:
     *            dp[i][w] = max(dp[i-1][w], dp[i-1][w-weights[i-1]] + ratings[i-1])
     *          else:
     *            dp[i][w] = dp[i-1][w]
     *   3. Backtrack from dp[N][CAPACITY] to determine which experiments were chosen.
     * 
     * Time complexity: O(N * CAPACITY) = O(12 * 700) = O(8400) — much better than brute force O(2^12).
     * Space complexity: O(N * CAPACITY) or O(CAPACITY) if using space optimization.
     * 
     * @return Result containing optimal experiments chosen by DP, total weight, and total rating.
     *         Returns null if implementation is not attempted (pseudocode-only submission).
     */
    static Result dynamicProgramming() {
        // TODO: build dp[N+1][CAPACITY+1], then backtrack
        return null;
    }

    /**
     * Print a single algorithm result.
     * 
     * Formats and displays:
     * - Strategy name
     * - List of chosen experiment names (and possibly indices)
     * - Total weight (kg)
     * - Total rating
     * 
     * Example output:
     *   Highest Rating First:
     *     Chosen: Solar Flares, Binary Stars, Micrometeorites, ... (6 experiments)
     *     Total Weight: 699 kg
     *     Total Rating: 50
     * 
     * @param strategyName name of the algorithm (e.g., "Highest Rating First")
     * @param result the Result object to print
     */
    static void printResult(String strategyName, Result result) {
        // TODO: print strategy name, chosen experiment names, weight, rating
    }

    /**
     * Print all optimal subsets found by brute force.
     * 
     * For each result in the list, print:
     * - Subset number (1st optimal, 2nd optimal, etc.)
     * - Chosen experiment names
     * - Total weight and rating
     * 
     * Example output:
     *   1st Optimal Subset:
     *     Chosen: Solar Flares, Binary Stars, ... (5 experiments)
     *     Total Weight: 700 kg
     *     Total Rating: 50
     *   2nd Optimal Subset (tied):
     *     Chosen: Micrometeorites, Relativity, ... (4 experiments)
     *     Total Weight: 674 kg
     *     Total Rating: 50
     * 
     * @param results list of Result objects from bruteForce(); may contain multiple optimal subsets
     */
    static void printBruteForceResults(List<Result> results) {
        // TODO: print every optimal subset found
    }

    /**
     * Main entry point: orchestrate all algorithm runs and output.
     * 
     * Execution flow:
     * 1. Run Part 1 (greedy strategies) and print results.
     * 2. Run Part 2 (brute force) and print all optimal subsets.
     * 3. Print Part 3 comparison summary: which greedy strategies matched optimal?
     * 4. Optionally run Part 4 (dynamic programming) if implemented.
     * 
     * Expected output: all results formatted clearly for the project report.
     * 
     * @param args command-line arguments (not used in this project)
     */
    public static void main(String[] args) {
        // TODO: run each strategy and print results
    }
}
