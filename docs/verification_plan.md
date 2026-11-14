# **Verification Plan & Test Results**

## **1\. Verification Plan**

### Outlined Plan

* Correctness checks/tests to run
  * Unit tests for core functions.  
  * Golden-file tests: compare outputs to expected results on provided sample inputs.  
  * Negative tests: invalid inputs produce clear error messages.

* Edge cases to verify
  * Empty/near-empty input (minimum size).  
  * Maximum size input within assignment constraints.  
  * Duplicate or conflicting entries (as applicable to the project spec).

* Expected vs. actual outputs (where applicable)
  * Document expected outputs for at least 3 representative test cases in docs/test-results.md.  
  * Include screenshots or terminal output snippets if tooling is minimal.

* Performance checks (if required by the project)
  * Run on increasing input sizes and record runtime (basic timing).  
  * Confirm no unnecessary O(n^2) loops on hot paths where avoidable.

### Conceptualization

JUnit 5 tests were used to verify the correctness of MainSkeleton.java.

* Algorithm Correctness: Verifies that each strategy satisfies capacity constraint and produces expected ratings for dataset.
* Edge Cases: Evaluates conditions such as zero capacity, a single items exceeding capacity, and a single item matching capacity.
* Tie Breaking: Ensures deterministic behavior when handling items with equivalent values.

## **2\. Tests**

| Test Case | Input/Condition | Expected Result |
| ----- | ----- | ----- |
| Highest Rating First | N = 13, CAPACITY = 700 | Selected: 2, 5, 6, 8, 10, 11, 11a <br> Baseline / Adjusted Weight: 700kg / 700kg <br> Total Rating: 50 |
| Lightest First | N = 13, CAPACITY = 700 | Selected: 1, 5, 6, 7, 8, 9, 10, 11, 11a, 12 <br> Baseline / Adjusted Weight: 664kg / 657kg (7kg credit) <br> Total Rating: 57 |
| Best Ratio First | N = 13, CAPACITY = 700 | Selected: 1, 5, 6, 7, 8, 9, 10, 11, 11a, 12 <br> Baseline / Adjusted Weight: 664kg / 657kg (7kg credit) <br> Total Rating: 57 |
| Brute Force | N = 13, CAPACITY = 700 | Optimal Subset: 1, 4, 5, 6, 8, 10, 11, 11a, 12 <br> Baseline / Adjusted Weight: 702kg / 695kg (7kg credit) <br> Total Rating: 58 |
| Dynamic Programming | N = 13, CAPACITY = 700 | Optimal Subset: 1, 4, 5, 6, 8, 10, 11, 11a, 12 <br> Baseline / Adjusted Weight: 702kg / 695kg (7kg credit) <br> Total Rating: 58 |
| Empty Selection | mask = 0 | baslineWeight = 0, totalWeight = 0, totalRating = 0, feasible = true, empty ID String |
| Payload Exceeds Capacity | mask = {2, 3, 4, 10} (825kg > 700kg) | feasible = false |
| Payload Equals Capacity | mask = {2, 5, 6, 8, 10, 11, 11a} (700kg) | totalWeight = 700, feasible = true |
| Identical Rating | Tied rating 9 <br> (ID 2: 264kg vs ID 10: 170kg) | Lighter baseline mass (ID 10) deterministically prioritized |
| Identical Ratio | Tied ratio comparator | Deterministic selection via lighter mass than array index order |
| Solar Instrument Limit | {2, 3} vs {2, 3, 7} (542 kg) | {2, 3} accepted (feasible = true) <br> {2, 3, 7} rejected (feasible = false) |
| Thermal Mass Credit | {1, 2, 4, 6, 10, 12} | baselineWeight = 707kg, totalWeight = 700kg, feasible = true |
| Exp. 11a Prerequisite | {11a} vs {11, 11a} | {11a} rejected (feasible = false) <br> {11, 11a} accepted (feasible = true) |
| Greedy Deferred 11a | greedyLightestFirst() encounters 11a before 11 | 11a temporarily skipped and added after 11 is selected |

## **3\. Test Results**

TODO
