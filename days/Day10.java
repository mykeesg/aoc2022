package days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Set;

public class Day10 extends Base {

    private final Set<Integer> goals = Set.of(20, 60, 100, 140, 180, 220);

    private final static int SCREEN_WIDTH = 40;
    private final static int SCREEN_HEIGHT = 6;
    private final boolean[] screen = new boolean[SCREEN_WIDTH * SCREEN_HEIGHT];
    private int cycle = 0;
    private int eax = 1;
    private int signalStrength = 0;

    public Day10() {
        try {
            solve();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void runFirst() throws Throwable {
        System.out.printf("Sum of signal strengths: %1$s%n", signalStrength);
    }

    @Override
    public void runSecond() throws Throwable {
        for (int i = 0; i < SCREEN_HEIGHT; i++) {
            for (int j = 0; j < SCREEN_WIDTH; j++) {
//                System.out.print(screen[i * SCREEN_WIDTH + j] ? "█" : "░");
//                System.out.print(screen[i * SCREEN_WIDTH + j] ? "█" : " ");
                System.out.print(screen[i * SCREEN_WIDTH + j] ? "⬜" : "⬛");
            }
            System.out.println();
        }
    }

    private void nextCycle() {
        ++cycle;
        if (goals.contains(cycle)) {
            this.signalStrength += cycle * eax;
        }
    }

    private void checkScreen() {
        if (Math.abs(cycle % SCREEN_WIDTH - eax) <= 1) {
            screen[cycle] = true;
        }
    }

    private void solve() throws Throwable {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day10.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                checkScreen();
                nextCycle();
                if (line.startsWith("addx")) {
                    long val = Long.parseLong(line.split(" ")[1]);
                    checkScreen();
                    nextCycle();
                    eax += val;
                }
            }
        }
    }
}