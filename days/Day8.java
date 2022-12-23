package days;

import util.Vector2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class Day8 extends Base {

    private final static int SIZE = 99;
    private final int[][] trees = new int[SIZE][SIZE];

    public Day8() {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day8.txt"))) {
            String line;
            for (int ii = 0; ii < SIZE; ++ii) {
                line = reader.readLine();
                for (int jj = 0; jj < SIZE; ++jj) {
                    trees[ii][jj] = line.charAt(jj) - '0';
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    void runFirst() throws Throwable {
        Set<Vector2D> coords = new HashSet<>();

        for (int ii = 1; ii < SIZE - 1; ++ii) {
            int maxLeft = trees[ii][0];
            int maxRight = trees[ii][SIZE - 1];
            int maxTop = trees[0][ii];
            int maxBottom = trees[SIZE - 1][ii];

            for (int jj = 1; jj < SIZE - 1; ++jj) {
                int currentLeft = trees[ii][jj];
                if (currentLeft > maxLeft) {
                    coords.add(new Vector2D(ii, jj));
                    maxLeft = currentLeft;
                }
                int currentRight = trees[ii][SIZE - jj - 1];
                if (currentRight > maxRight) {
                    coords.add(new Vector2D(ii, SIZE - jj - 1));
                    maxRight = currentRight;
                }
                int currentTop = trees[jj][ii];
                if (currentTop > maxTop) {
                    coords.add(new Vector2D(jj, ii));
                    maxTop = currentTop;
                }
                int currentBottom = trees[SIZE - jj - 1][ii];
                if (currentBottom > maxBottom) {
                    coords.add(new Vector2D(SIZE - jj - 1, ii));
                    maxBottom = currentBottom;
                }
            }
        }
        int count = coords.size() + (4 * (SIZE - 1));
        System.out.println("Total number of trees that are visible: " + count);
    }

    @Override
    void runSecond() throws Throwable {
        int score = 0;
        for (int ii = 1; ii < SIZE - 1; ++ii) {
            for (int jj = 1; jj < SIZE - 1; ++jj) {

                int currentItem = trees[ii][jj];

                int currentScore = 1;

                int cnt = 0;
                // go right
                for (int kk = jj + 1; kk < SIZE; ++kk) {
                    ++cnt;
                    if (trees[ii][kk] >= currentItem) {
                        break;
                    }
                }
                currentScore *= cnt;

                cnt = 0;
                // go left
                for (int kk = jj - 1; kk >= 0; --kk) {
                    ++cnt;
                    if (trees[ii][kk] >= currentItem) {
                        break;
                    }
                }
                currentScore *= cnt;

                cnt = 0;
                // go down
                for (int kk = ii + 1; kk < SIZE; ++kk) {
                    ++cnt;
                    if (trees[kk][jj] >= currentItem) {
                        break;
                    }
                }
                currentScore *= cnt;

                cnt = 0;
                // go up
                for (int kk = ii - 1; kk >= 0; --kk) {
                    ++cnt;
                    if (trees[kk][jj] >= currentItem) {
                        break;
                    }
                }
                currentScore *= cnt;

                if (score < currentScore) {
                    score = currentScore;
                }
            }
        }
        System.out.println("Best spot possible for any tree: " + score);
    }
}