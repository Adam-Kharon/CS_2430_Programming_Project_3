# **Verification Plan & Test Results**

## **1\. Verification Plan**

JUnit 5 tests were used to verify the correctness of MainSkeleton.java.

### Tests Covered

* Algorithm Correctness: Verifies that each strategy satisfies capacity constraint and produces expected ratings for dataset.
* Edge Cases: Evaluates conditions such as zero capacity, a single items exceeding capacity, and a single item matching capacity.
* Tie Breaking: Ensures deterministic behavior when handling items with equivalent values. 

## **2\. Edge Case Tests**

| Test Case | Input/Condition | Expected Result |
| ----- | ----- | ----- |
| Highest Rating First | N = 12, CAPACITY = 700 | Selected: Solar Flares, Micrometeorites, Binary Stars, Cloud Patterns, Seed Viability <br> Total Weight: 680 <br> Total Rating: 35 |
| Lightest First | N = 12, CAPACITY = 700 | Selected: Seed Viability, Yeast Fermentation, Cloud Patterns, Mice Tumors, Microgravity Plant Growth, Cosmic Rays, Sun Spots, Relativity, Micrometeorites <br> Total Weight: 654 <br> Total Rating: 52 |
| Best Ratio First | N = 12, CAPACITY = 700 | Selected: Seed Viability, Yeast Fermentation, Cloud Patterns, Mice Tumors, Cosmic Rays, Relativity, Microgravity Plant Growth, Micrometeorites, Sun Spots <br> Total Weight: 654 <br> Total Rating: 52 |
| Brute Force | N = 12, CAPACITY = 700 | Optimal Subset: Cloud Patterns, Binary Stars, Relativity, Seed Viability, Mice Tumors, Micrometeorites, Cosmic Rays, Yeast Fermentation <br> Total Weight: 692 <br> Total Rating: 53 |
| Dynamic Programming | N = 12, CAPACITY = 700 | Optimal Subset: Cloud Patterns, Binary Stars, Relativity, Seed Viability, Mice Tumors, Micrometeorites, Cosmic Rays, Yeast Fermentation <br> Total Weight: 692 <br> Total Rating: 53 |
| Zero Capcity | CAPACITY = 0 | count = 0, totalWeight = 0, totalRating = 0, empty array |
| Item Exceeds Capacity | Item Weight > CAPACITY | Item is skipped and valid result is returned |
| Item Equals Capacity | Item Weight = CAPACITY | Single item is selected and returned |
| Identical Rating | More than one item with identical rating | Deterministic item selection without OutOfBoundsException |
| Identical Ratio | More than one item with identical ratio | Deterministic item selection without OutOfBoundsException |

## **3\. Test Results**

TODO
