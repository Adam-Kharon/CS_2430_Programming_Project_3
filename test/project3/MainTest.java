package project3;

import org.junit.jupiter.api.Test;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for OptimalSelection
 * @Team_Name Team-2
 * @Team_Members Adam-Sarraj, Danielle-Major, Mustafa-Alrubaye
 * @Course_And_Section CS 2430 / 001
 * @Project_Name Programming Project 3, Optimal selection - Fall 2026
 * @author Adam-Sarraj
 */

public class MainTest {

    // Core Algorithm Tests

    @Test
    public void testHighestRatingFirst() {
        Main.Result result = Main.greedyHighestRating();
        assertTrue(result.feasible);
        assertEquals("2, 5, 6, 8, 10, 11, 11a", Main.selectedIds(result.mask));
        assertEquals(700, result.baselineWeight);
        assertEquals(700, result.totalWeight);
        assertEquals(50, result.totalRating);
    }

    @Test
    public void testLightestFirst() {
        Main.Result result = Main.greedyLightestFirst();
        assertTrue(result.feasible);
        assertEquals("1, 5, 6, 7, 8, 9, 10, 11, 11a, 12", Main.selectedIds(result.mask));
        assertEquals(664, result.baselineWeight);
        assertEquals(657, result.totalWeight);
        assertEquals(57, result.totalRating);
    }

    @Test
    public void testBestRatioFirst() {
        Main.Result result = Main.greedyBestRatio();
        assertTrue(result.feasible);
        assertEquals("1, 5, 6, 7, 8, 9, 10, 11, 11a, 12", Main.selectedIds(result.mask));
        assertEquals(664, result.baselineWeight);
        assertEquals(657, result.totalWeight);
        assertEquals(57, result.totalRating);
    }

    @Test
    public void testBruteForce() {
        List<Main.Result> topThree = Main.bruteForce();
        assertEquals(3, topThree.size());

        Main.Result optimal = topThree.get(0);
        assertTrue(optimal.feasible);
        assertEquals("1, 4, 5, 6, 8, 10, 11, 11a, 12", Main.selectedIds(optimal.mask));
        assertEquals(702, optimal.baselineWeight);
        assertEquals(695, optimal.totalWeight);
        assertEquals(58, optimal.totalRating);
        assertEquals(57, topThree.get(1).totalRating);
        assertEquals(56, topThree.get(2).totalRating);
    }

    @Test
    public void testDynamicProgramming() {
        Main.Result dp = Main.dynamicProgramming();
        Main.Result bruteOptimal = Main.bruteForce().get(0);
        assertTrue(dp.feasible);
        assertEquals("1, 4, 5, 6, 8, 10, 11, 11a, 12", Main.selectedIds(dp.mask));
        assertEquals(702, dp.baselineWeight);
        assertEquals(695, dp.totalWeight);
        assertEquals(58, dp.totalRating);
        assertEquals(bruteOptimal.totalRating, dp.totalRating);
    }

    // Edge Case Tests

    @Test
    public void testEmptySelection() {
        Main.Result empty = Main.evaluate(0);
        assertTrue(empty.feasible);
        assertEquals(0, empty.baselineWeight);
        assertEquals(0, empty.totalWeight);
        assertEquals(0, empty.totalRating);
        assertEquals("", Main.selectedIds(empty.mask));
    }

    @Test
    public void testPayloadExceedsCapacity() {
        int overweightMask = Main.bit(1) | Main.bit(2) | Main.bit(3) | Main.bit(9);
        Main.Result result = Main.evaluate(overweightMask);
        assertEquals(825, result.totalWeight);
        assertFalse(result.feasible);
    }

    @Test
    public void testPayloadEqualsCapacity() {
        Main.Result result = Main.greedyHighestRating();
        assertEquals(Main.CAPACITY, result.totalWeight);
        assertTrue(result.feasible);
    }

    @Test
    public void testIdenticalRating() {
        Main.Result result = Main.greedyHighestRating();
        assertTrue((result.mask & Main.bit(9)) != 0);
        assertTrue((result.mask & Main.bit(1)) != 0);
    }

    @Test
    public void testIdenticalRatio() {
        Comparator<Integer> tiedRatio = (a, b) -> Integer.compare(Main.weights[a], Main.weights[b]);
        Main.Result firstRun = Main.greedy(tiedRatio);
        Main.Result secondRun = Main.greedy(tiedRatio);
        assertEquals(firstRun.mask, secondRun.mask);
        assertEquals(firstRun.totalRating, secondRun.totalRating);
        assertTrue(firstRun.feasible);
    }

    // Change Order Tests

    @Test
    public void testSolarInstrumentLimit() {
        int twoSolar = Main.bit(Main.SOLAR_FLARES) | Main.bit(Main.SOLAR_POWER);
        assertTrue(Main.evaluate(twoSolar).feasible);

        int threeSolar = twoSolar | Main.bit(Main.SUN_SPOTS);
        Main.Result rejected = Main.evaluate(threeSolar);
        assertEquals(542, rejected.totalWeight);
        assertFalse(rejected.feasible);
    }

    @Test
    public void testThermalMassCredit() {
        int mask = Main.bit(0) | Main.bit(1) | Main.bit(3)
                 | Main.bit(Main.SEED) | Main.bit(9) | Main.bit(Main.YEAST);
        Main.Result result = Main.evaluate(mask);
        assertEquals(707, result.baselineWeight);
        assertEquals(700, result.totalWeight);
        assertEquals(39, result.totalRating);
        assertTrue(result.feasible);
    }

    @Test
    public void testExperiment11aPrerequisite() {
        Main.Result secondaryOnly = Main.evaluate(Main.bit(Main.SECONDARY));
        assertFalse(secondaryOnly.feasible);

        Main.Result primaryOnly = Main.evaluate(Main.bit(Main.PRIMARY));
        assertTrue(primaryOnly.feasible);

        Main.Result both = Main.evaluate(Main.bit(Main.PRIMARY) | Main.bit(Main.SECONDARY));
        assertTrue(both.feasible);
        assertEquals(90, both.totalWeight);
        assertEquals(12, both.totalRating);
    }

    @Test
    public void testGreedyDeferred11a() {
        Main.Result result = Main.greedyLightestFirst();
        assertTrue((result.mask & Main.bit(Main.PRIMARY)) != 0);
        assertTrue((result.mask & Main.bit(Main.SECONDARY)) != 0);
    }
}
