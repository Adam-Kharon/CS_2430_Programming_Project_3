# Project 3 Change Order

## Purpose

The purpose of this change order is to update Project 3 with the revised
payload manifest and the three new mission constraints.

The overall goal of the project did not change.

The objective is still to select a group of experiments that maximizes
the total science rating while staying within the 700 kg payload limit.

Each experiment can only be chosen once.

---

# What Changed

The revised mission data introduced:

1. A new experiment (11a)
2. A solar instrument restriction
3. A thermal mass credit
4. A prerequisite requirement for experiment 11a

Because of these changes, both the brute-force and greedy algorithms
had to be updated.

---

# New Experiment

A new experiment was added to the payload manifest.

| ID | Experiment | Mass (kg) | Rating |
|----|------------|-----------|---------|
| 11a | Cosmic Rays (Secondary Band) | 10 | 5 |

This increases the total number of experiments from 12 to 13.

Because each experiment can either be selected or not selected, the
number of possible payload combinations increases from:

4096

to

8192

possible combinations.

Calculation:

2^13 = 8192

---

# Rule 1: Solar Instrument Limit

The mission can include at most two of the following experiments:

- Experiment 2 (Solar Flares)
- Experiment 3 (Solar Power)
- Experiment 7 (Sun Spots)

If all three are selected together, the manifest is rejected.

### Valid Examples

{2,3}

{2,7}

{3,7}

### Invalid Example

{2,3,7}

Even if the weight is under 700 kg, the payload is not allowed.

---

# Rule 2: Thermal Mass Credit

A thermal mass credit is applied when these two experiments are selected
together:

- Experiment 6 (Seed Viability)
- Experiment 12 (Yeast Fermentation)

Normally:

Seed Viability = 7 kg

Yeast Fermentation = 27 kg

Combined weight:

34 kg

When both are included, a 7 kg credit is applied.

Adjusted weight:

34 - 7 = 27 kg

The credit must be applied before checking the payload capacity.

This means a payload that weighs 707 kg may still become valid if the
credit reduces the adjusted weight to 700 kg.

---

# Rule 3: Experiment 11a Prerequisite

Experiment 11a depends on Experiment 11.

Allowed:

{11}

Allowed:

{11,11a}

Not Allowed:

{11a}

Experiment 11a cannot fly by itself.

When both experiments are selected:

Mass:

80 + 10 = 90 kg

Science Rating:

7 + 5 = 12

---

# Program Design Changes

## Shared Evaluate Method

A shared evaluation method is used for all algorithms.

The evaluate() method calculates:

- Total science rating
- Baseline mass
- Adjusted mass
- Feasibility

The method also checks all three revised rules.

Using one evaluation method ensures that brute force, greedy, and dynamic
programming all follow the same mission constraints.

---

# Brute Force Changes

The brute-force solution now evaluates all possible combinations of the
13 experiments.

Total combinations checked:

8192

For each candidate:

1. Calculate mass
2. Calculate science rating
3. Apply thermal credit
4. Check solar experiment limit
5. Check 11a prerequisite
6. Verify adjusted mass is 700 kg or less

Only valid payloads are kept.

After all candidates are evaluated, the results are sorted by science
rating.

The highest rated valid solution is considered the optimal payload.

---

# Greedy Changes

Three greedy strategies are still used.

## Highest Rating First

Experiments are sorted by science rating from highest to lowest.

The algorithm attempts to add the highest value experiments first.

---

## Lightest First

Experiments are sorted by mass from lowest to highest.

The algorithm attempts to fit as many experiments as possible.

---

## Best Rating-To-Mass Ratio

Experiments are sorted by:

rating / mass

The algorithm prioritizes experiments that provide the most science
value per kilogram.

---

# Handling Experiment 11a During Greedy Selection

Experiment 11a creates a special case.

If the greedy algorithm encounters 11a before selecting Experiment 11,
11a would normally fail the prerequisite rule.

Instead of immediately rejecting it forever, the algorithm:

1. Temporarily skips 11a
2. Continues processing the remaining experiments
3. Rechecks 11a if Experiment 11 is later selected

This allows valid combinations containing both experiments to still be
considered.

---

# Rule Tests

## Test 1: Solar Instrument Limit

Payload:

{2,3,7}

Expected Result:

Reject

Reason:

All three solar experiments are selected.

Actual Result:

Reject

---

## Test 2: Experiment 11a Prerequisite

Payload:

{11a}

Expected Result:

Reject

Reason:

Experiment 11 is not included.

Actual Result:

Reject

---

## Test 3: Thermal Mass Credit

Payload:

{1,2,4,6,10,12}

Baseline Mass:

707 kg

Expected Adjusted Mass:

700 kg

Expected Result:

Accept

Actual Result:

Accept

---

# Impact of the Change Order

The new constraints reduce the number of feasible payloads.

Some payloads that would normally fit within the mass limit are now
rejected because of the solar instrument restriction or the 11a
dependency requirement.

The thermal credit introduces additional valid payloads that would
otherwise exceed the payload capacity.

The brute-force algorithm still guarantees the optimal solution because
every possible payload configuration is evaluated.

The greedy algorithms provide approximate solutions that depend on the
selected ranking strategy.
