# Project Scope: Programming Part

## Problem

A satellite can carry **up to 700 kg** of science experiments. There are 12 candidate experiments, each with a weight (kg) and a research rating. Select experiments so total weight is <= 700 kg and total rating is as high as possible. Experiments are indivisible (0/1 knapsack).

### Data

| ID | Experiment | Weight (kg) | Rating |
|----|------------|-------------|--------|
| 1 | Cloud Patterns | 36 | 5 |
| 2 | Solar Flares | 264 | 9 |
| 3 | Solar Power | 188 | 6 |
| 4 | Binary Stars | 203 | 8 |
| 5 | Relativity | 104 | 8 |
| 6 | Seed Viability | 7 | 4 |
| 7 | Sun Spots | 90 | 2 |
| 8 | Mice Tumors | 65 | 8 |
| 9 | Microgravity Plant Growth | 75 | 5 |
| 10 | Micrometeorites | 170 | 9 |
| 11 | Cosmic Rays | 80 | 7 |
| 12 | Yeast Fermentation | 27 | 4 |

## Programming Requirements (4 Parts)

### Part 1: Three Greedy Strategies

Load/store the 12 experiments (hard-coded in a list, array, struct or similar is fine). Implement three separate functions/methods, each building a payload without exceeding 700 kg:

- **Highest Rating First**
  - Select experiments by highest rating without exceeding 700 kg.
  - Output: chosen experiments, total weight, total rating.
  - Give-and-Take: This strategy maximizes individual prestige by grabbing the highest-rated experiments first. The downside is severe capacity fragmentation: after packing just four heavy 
    items, the remaining $10\text{ kg}$ capacity is too small to fit any other valuable payloads, forcing the satellite to fly half-empty relative to potential scientific yield.
- **Lightest First**
  - Select experiments by lightest weight without exceeding 700 kg.
  - **Output:** chosen experiments, total weight, total rating.
  - **Give-and-Take:** By prioritizing lightweight items, this strategy successfully packs a high volume of experiments without wasting cargo capacity (leaving only $46\text{ kg}$ unused). 
    However, it blindly favors small items regardless of their scientific value, meaning high-value heavier items are discarded.
- **Best Rating-to-Weight Ratio First**
  - Compute `score = rating / weight` for each experiment.
  - Select experiments by highest ratio while total weight <= 700 kg.
  - **Output:** chosen experiments, total weight, total rating.
  - **Give-and-Take:** Calculating the efficiency ($ext{rating} / 	ext{weight}$) bridges the gap between value and space utilization. In this specific dataset, the ratio heuristic behaves 
    identically to the weight-based approach because the highest-efficiency items also happen to be relatively lightweight. While it beats rating-first, it still misses the absolute optimal combination.
  - *Why greedy strategies may miss the optimal subset:*
    Greedy algorithms make irrevocable local choices at each step based on a single heuristic (such as weight or ratio) without looking ahead. When the algorithm packs items sequentially, taking a high-ratio
    item early can permanently consume capacity or lock out a complementary cluster of items that, when combined, yield a higher aggregate rating. The optimal subset requires a holistic tradeoff—such as 
    including the heavier Binary Stars ($203\text{ kg}$, rating $8$)—which a greedy algorithm rejects because its individual ratio or weight profile does not immediately align with local sorting rules.
    Clearly display results for all three strategies.

### Part 2: Brute Force / Exhaustive Search

With 12 items there are 2^12 = 4096 possible subsets. The code must:

- Generate all 4096 subsets.
- Compute total weight and total rating for each subset.
- Identify and display the **three highest-rated valid subsets** (total weight <= 700 kg), clearly indicating which one is optimal.
- Output chosen experiments, total weight and total rating for each.
- Give-and-Take: By exhaustively evaluating all $4,096$ possible combinations ($2^{12}$), this approach guarantees the absolute mathematical optimum. The "give" is absolute certainty and peak scientific return; 
  the "take" is exponential time complexity ($O(2^n)$), making it impossible to scale if the candidate pool grows beyond a few dozen items.

This gives the true best solution to compare against the greedy strategies.

### Part 3: Comparison Summary Output

Add output summarizing:

- Results of all 3 greedy strategies (Part 1)
- Result of the brute-force optimal (Part 2)
- A brief printed summary of which strategies matched the optimal and which did not

Make the output easy to read and copy into the report.

### Part 4: Dynamic Programming (Extra Credit Option)

For small sets brute force is feasible. For larger datasets, dynamic programming (DP) can give optimal (or near-optimal) solutions efficiently.

- **Not implementing DP:** include **pseudocode comments** in the source describing how a DP solution would work. This is required even without attempting extra credit.
- **Extra credit (+2 pts):** implement DP in code and include:
  - The DP table or final result (as appropriate)
  - The chosen subset
  - A short comparison of runtime and code complexity vs. brute force


