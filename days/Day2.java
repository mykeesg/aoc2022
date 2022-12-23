package days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Objects;

public class Day2 extends Base {
    enum Outcome {
        WIN,
        DRAW,
        LOSS;

        static Outcome from(String str) {
            if (Objects.equals(str, "X")) {
                return LOSS;
            }
            if (Objects.equals(str, "Y")) {
                return DRAW;
            }
            if (Objects.equals(str, "Z")) {
                return WIN;
            }
            throw new IllegalArgumentException(str);
        }

        public int score() {
            return switch (this) {
                case WIN -> 6;
                case DRAW -> 3;
                case LOSS -> 0;
            };
        }
    }

    enum ValuePlayed {
        ROCK,
        PAPER,
        SCISSOR;

        public int score() {
            return switch (this) {
                case ROCK -> 1;
                case PAPER -> 2;
                case SCISSOR -> 3;
            };
        }

        Outcome match(ValuePlayed other) {
            if (other == this) {
                return Outcome.DRAW;
            } else if (this.beats(other)) {
                return Outcome.WIN;
            } else {
                return Outcome.LOSS;
            }
        }

        boolean beats(ValuePlayed other) {
            return switch (this) {
                case ROCK -> other == SCISSOR;
                case PAPER -> other == ROCK;
                case SCISSOR -> other == PAPER;
            };
        }

        static ValuePlayed from(String str) {
            if (Objects.equals(str, "A") || Objects.equals(str, "X")) {
                return ROCK;
            }
            if (Objects.equals(str, "B") || Objects.equals(str, "Y")) {
                return PAPER;
            }
            if (Objects.equals(str, "C") || Objects.equals(str, "Z")) {
                return SCISSOR;
            }
            throw new IllegalArgumentException(str);
        }

        public ValuePlayed withOutcome(Outcome outcome) {
            switch (outcome) {
                case DRAW -> {
                    return this;
                }
                case WIN -> {
                    return switch (this) {
                        case ROCK -> PAPER;
                        case PAPER -> SCISSOR;
                        case SCISSOR -> ROCK;
                    };
                }
                case LOSS -> {

                    return switch (this) {
                        case ROCK -> SCISSOR;
                        case PAPER -> ROCK;
                        case SCISSOR -> PAPER;
                    };
                }
            }
            throw new IllegalArgumentException(outcome.name());
        }
    }

    @Override
    public void runFirst() throws Throwable {
        System.out.printf("Total score from 1st round: %1$s%n", solve1());
    }

    @Override
    public void runSecond() throws Throwable {
        System.out.printf("Total score from 2nd round: %1$s%n", solve2());
    }

    private long solve1() throws Throwable {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day2.txt"))) {
            String line;
            long score = 0;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");

                ValuePlayed opponent = ValuePlayed.from(data[0]);
                ValuePlayed mine = ValuePlayed.from(data[1]);

                Outcome outcome = mine.match(opponent);
                score += mine.score() + outcome.score();
            }
            return score;
        }
    }

    private long solve2() throws Throwable {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day2.txt"))) {
            String line;
            long score = 0;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");

                ValuePlayed opponent = ValuePlayed.from(data[0]);
                Outcome outcome = Outcome.from(data[1]);

                ValuePlayed mine = opponent.withOutcome(outcome);
                score += mine.score() + outcome.score();
            }
            return score;
        }
    }
}