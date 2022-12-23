import days.*;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            // System.out.println("Usage: Main <day_number>");
            new Day23().run();
            return;
        }
        switch (Integer.parseInt(args[0])) {
//            case 1 -> new Day1().run();
//            case 2 -> new Day2().run();
//            case 3 -> new Day3().run();
//            case 4 -> new Day4().run();
//            case 5 -> new Day5().run();
//            case 6 -> new Day6().run();
//            case 7 -> new Day7().run();
//            case 8 -> new Day8().run();
//            case 9 -> new Day9().run();
//            case 10 -> new Day10().run();
//            case 11 -> new Day11().run();
//            case 12 -> new Day12().run();
//            case 13 -> new Day13().run();
//            case 14 -> new Day14().run();
//            case 15 -> new Day15().run();
//            case 16 -> new Day16().run();
//            case 17 -> new Day17().run();
//            case 18 -> new Day18().run();
//            case 19 -> new Day19().run();
//            case 20 -> new Day20().run();
//            case 21 -> new Day21().run();
//            case 22 -> new Day22().run();
            case 23 -> new Day23().run();
            default -> System.out.println("Solution not yet defined.");
        }
    }
}
