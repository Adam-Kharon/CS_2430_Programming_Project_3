# CS 2430 Project 3 – Optimal Payload Selection: Design Notes

## Problem
Select a subset of 12 science experiments for a satellite so that:
- Total weight is <= **700 kg** (`CAPACITY`)
- Total rating is **maximized**
- Each experiment is taken whole or not at all (0/1 knapsack)

---

## Program Structure

Everything lives in one file, `Main.java`, as static members of `Main`. There are no separate classes for experiments or strategies.

### Constants and data (parallel arrays, index `i` = experiment `i`)
- `CAPACITY : int = 700`
- `N : int = 12`
- `names : String[]`
- `weights : int[]` (kg)
- `ratings : int[]` (1-10)

### Nested class `Result`
- `chosen : int[]` – indices of selected experiments (length `N`; only the first `count` entries are valid)
- `count : int` – number of selected experiments
- `totalWeight : int`
- `totalRating : int`
- Used by all five algorithms, including each brute-force solution.

### Methods (all `static`)
| Method | Returns | Role |
|--------|---------|------|
| `greedyHighestRating()` | `Result` | Greedy strategy 1 |
| `greedyLightestFirst()` | `Result` | Greedy strategy 2 |
| `greedyBestRatio()` | `Result` | Greedy strategy 3 |
| `bruteForce()` | `List<Result>` | Exhaustive search, top 3 subsets |
| `dynamicProgramming()` | `Result` | DP with backtracking (extra credit) |
| `printResult(String, Result)` | `void` | Print one result |
| `printBruteForceResults(List<Result>)` | `void` | Print the top 3 |
| `main(String[])` | `void` | Run everything and print the comparison |

The methods take no parameters and read the static arrays directly.

---

## Algorithms

### Greedy (three variants)
All three build an index array, sort it, then walk it in order. An experiment is added only if it still fits; if it doesn't, it is skipped and the walk continues.

| Strategy | Primary sort key | Tie-break |
|----------|------------------|-----------|
| Highest Rating First | rating, descending | weight, ascending |
| Lightest First | weight, ascending | rating, descending |
| Best Ratio First | `rating / weight`, descending | weight, ascending |

- Time: O(n log n). Space: O(n).
- Not guaranteed optimal.

### Brute Force
- Loop a bitmask from 0 to 2^N - 1. Each value encodes exactly one subset.
- Compute weight and rating for each subset and keep it if weight <= `CAPACITY`.
- Sort valid subsets by rating (descending), ties by weight (ascending), and return the top 3. The first entry is the optimal.
- Time: O(2^n · n) for enumeration, plus sorting the valid subsets. Space: O(2^n) for the stored valid subsets.
- Optimal.

### Dynamic Programming (extra credit)
- Table `dp[N+1][CAPACITY+1]`, where `dp[i][w]` is the best rating using the first `i` items with limit `w`.
- Recurrence: `dp[i][w] = max(dp[i-1][w], ratings[i-1] + dp[i-1][w - weights[i-1]])`, with the second option allowed only if `weights[i-1] <= w`.
- Backtrack from `dp[N][CAPACITY]` to recover the chosen items.
- Time: O(N × CAPACITY). Space: O(N × CAPACITY).
- Optimal.

### Complexity summary
| Strategy | Time | Space | Optimal? |
|----------|------|-------|----------|
| Highest Rating | O(n log n) | O(n) | No |
| Lightest First | O(n log n) | O(n) | No |
| Ratio First | O(n log n) | O(n) | No |
| Brute Force | O(2^n · n) | O(2^n) | Yes |
| Dynamic Programming | O(n × W) | O(n × W) | Yes |

### Detailed Complexity Comparison: Dynamic Programming vs. Brute Force

For this project (n=12, CAPACITY=700):

| Aspect | Brute Force | Dynamic Programming |
|--------|------------|---------------------|
| **Time Complexity** | O(2^n · n) = O(2^12 · 12) = O(49,152) operations | O(n · CAPACITY) = O(12 · 700) = O(8,400) operations |
| **Space Complexity** | O(2^n) = O(4,096) subsets stored in memory | O(n · CAPACITY) = O(8,400) DP table cells |
| **Approach** | Enumerate all 4,096 subsets, filter feasible ones | Build DP table incrementally, backtrack for solution |
| **Optimality** | ✅ Guaranteed optimal | ✅ Guaranteed optimal |
| **Speedup Factor** | Baseline | **49,152 / 8,400 ≈ 5.85× faster** |

**Practical Viability:**
- **Brute Force:** Feasible for n ≤ 20 (≈1 million subsets); infeasible for n > 25 (>33 million subsets)
- **Dynamic Programming:** Practical for any n; scales efficiently to thousands of items

**Scalability Example (Hypothetical n=100, CAPACITY=10,000):**
- **Brute Force:** O(2^100 · 100) ≈ 10^32 operations — **IMPOSSIBLE** in reasonable time
- **Dynamic Programming:** O(100 · 10,000) = 1,000,000 operations — **EASILY SOLVABLE** in milliseconds

**Key Trade-off:**
- Brute Force: Simpler implementation, exhaustive but exponential
- Dynamic Programming: More complex implementation, polynomial and much faster despite equal optimality

---

## Assumptions
1. Weights and ratings are positive integers.
2. Items are indivisible.
3. Capacity is a strict limit (<= 700 kg).
4. Ties are broken by the secondary keys above, so results are reproducible.
5. The total rating is the plain sum of individual ratings (no interaction effects).

## Invariants
1. **Feasibility:** every returned selection has `totalWeight <= CAPACITY`.
2. **Greedy:** items are considered once, in sorted order, and never removed. A skipped item does not stop later, lighter items from being taken.
3. **Brute force:** each bitmask maps to exactly one unique subset, so all 4096 subsets are examined.
4. **Optimality:** brute force and DP must return the same maximum rating.

---

## Testing Notes
- All strategies return feasible results.
- Brute-force best rating >= every greedy rating.
- DP rating == brute-force best rating.
- Edge cases: capacity 0 (nothing selected), a single item, all items fitting, and ties in the sort keys.

## Output Format
Each strategy prints its name, total weight (and % of capacity), total rating, and a list of selected experiments. `main` then prints a comparison of the greedy results against the brute-force optimal.

## References
- 0/1 Knapsack DP: https://www.geeksforgeeks.org/java/java-program-for-dynamic-programming-set-10-0-1-knapsack-problem/
- Brute force bitmask pattern: https://www.finalroundai.com/articles/knapsack-problem
- Fractional Knapsack (adapted for 0/1): https://www.geeksforgeeks.org/dsa/fractional-knapsack-problem/
