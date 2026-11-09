```mermaid
classDiagram
    direction LR

    class Main {
        +CAPACITY : int = 700$
        +N : int = 12$
        +names : String[12] #123;12 experiments&#125$
        +weights : int[12] #123;12 experiments&#125$
        +ratings : int[12] #123;12 experiments&#125$
        +greedyHighestRating() Result$
        +greedyLightestFirst() Result$
        +greedyBestRatio() Result$
        +bruteForce() List~Result~$
        +dynamicProgramming() Result$
        +printResult(String, Result) void$
        +printBruteForceResults(List~Result~) void$
        +main(String[]) void$
    }

    class Result {
        <<Static>>
        +chosen : int[]
        +count : int
        +totalWeight : int
        +totalRating : int
    }

    class MainTest {
        +testHighestRatingFirst() void
        +testLightestFirst() void
        +testBestRatioFirst() void
        +testBruteForce() void
        +testDynamicProgramming() void
        +testZeroCapacity() void
        +testItemExceedsCapacity() void
        +testItemEqualsCapacity() void
        +testIdenticalRating() void
        +testIdenticalRatio() void
    }

    Main *-- Result
    MainTest ..> Main : tests
```
