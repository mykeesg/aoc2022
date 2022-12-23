package days;

import util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Day16 extends Base {

    private final Map<String, Pair<Integer, List<String>>> tunnelSystem = new LinkedHashMap<>();
    private List<Integer> valveScores;
    private List<List<Integer>> tunnelNeighbours;
    private List<Integer> scores;
    private int worthyCaves;

    public Day16() {
        try {
            readInput();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private void readInput() throws Throwable {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day16.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                String valve = data[1];
                int flowRate = Integer.parseInt(data[4].substring(5, data[4].length() - 1));
                int tunnelCount = data.length - 9;
                List<String> tunnelsTowards = new ArrayList<>(tunnelCount);
                for (int ii = 0; ii < tunnelCount; ++ii) {
                    String str = data[ii + 9];
                    if (str.endsWith(",")) {
                        str = str.substring(0, str.length() - 1);
                    }
                    tunnelsTowards.add(str);
                }

                tunnelSystem.put(valve, Pair.of(flowRate, tunnelsTowards));
            }
        }
        Map<String, Integer> indices = new LinkedHashMap<>();
        List<String> visitOrder = new ArrayList<>();
        worthyCaves = 0;


        // find where do we start and make it the first in the ORDER we visit the caves
        for (var cave : tunnelSystem.entrySet()) {
            if (cave.getKey().equals("AA")) {
                //start position
                indices.put(cave.getKey(), 0);
                visitOrder.add(cave.getKey());
                worthyCaves++;
                break;
            }
        }

        // any cave that is worthy to be visited will be next in the list
        for (var cave : tunnelSystem.entrySet()) {
            if (cave.getValue().first() > 0) {
                //valve in cave might worthy to be opened
                indices.put(cave.getKey(), visitOrder.size());
                visitOrder.add(cave.getKey());
                worthyCaves++;
            }
        }

        //these caves have 0 flow rate, they are at the end of our lists
        for (var cave : tunnelSystem.entrySet()) {
            if (!indices.containsKey(cave.getKey())) {
                indices.put(cave.getKey(), visitOrder.size());
                visitOrder.add(cave.getKey());
            }
        }

        valveScores = new ArrayList<>(tunnelSystem.size());
        for (int ii = 0; ii < tunnelSystem.size(); ++ii) {
            valveScores.add(tunnelSystem.get(visitOrder.get(ii)).first());
        }

        tunnelNeighbours = new ArrayList<>();
        for (int ii = 0; ii < tunnelSystem.size(); ++ii) {
            List<Integer> tmp = new ArrayList<>();
            for (var tunnelConnection : tunnelSystem.get(visitOrder.get(ii)).second()) {
                tmp.add(indices.get(tunnelConnection));
            }
            tunnelNeighbours.add(tmp);
        }
    }

    private void resetScores() {
        int dynamicSize = (1 << worthyCaves) * tunnelSystem.size() * 31 * 2;
        scores = new ArrayList<>(dynamicSize);
        for (int ii = 0; ii < dynamicSize; ++ii) {
            scores.add(-1);
        }
    }

    //visited caves is a "bit-hacked" set for each index
    private int solve(int position, int visitedCaves, int time, int otherPlayers) {
        if (time == 0) {
            // our time has run out, if there's anyone else, solve for the other player(s) as well
            return otherPlayers > 0 ? solve(0, visitedCaves, 26, otherPlayers - 1) : 0;
        }
        // the dynamic table is a GIGANTIC array containing every possible state, where the index is:

        // - 50 caves -> 50 possible positions to be at
        // - with 15 total valves there's a total of 2^15 possible settings ("subsets") for open valves.
        // - time ticks from 30 -> 31 possible time values
        // - otherPlayers -> each player increases that dimension by 1

        // total possible states: 50 * 2^15 * 31 * players; -> ~51m states per player
        // increasing the number of players above 42 will overflow, so unless Java supports LONG indexing for
        // arrays and Collections, it will not be usable in that case

        // Cheers to Jonathan Paulson for coming up with this: https://www.youtube.com/watch?v=DgqkVDr1WX8

        int dynamicIndex = valveScores.size() * visitedCaves * 31 * 2 + position * 31 * 2 + time * 2 + otherPlayers;
        //this position was solved already
        if (scores.get(dynamicIndex) >= 0) {
            return scores.get(dynamicIndex);
        }
        int score = 0;
        boolean currentNotYetOpened = (visitedCaves & 1 << position) == 0;
        if (currentNotYetOpened && valveScores.get(position) > 0) {
            int newlyVisited = visitedCaves | (1 << position);
            score = Math.max(score, (time - 1) * this.valveScores.get(position) + solve(position, newlyVisited, time - 1, otherPlayers));
        }

        for (int neighbourIndex : tunnelNeighbours.get(position)) {
            score = Math.max(score, solve(neighbourIndex, visitedCaves, time - 1, otherPlayers));
        }

        scores.set(dynamicIndex, score);
        return score;
    }

    @Override
    void runFirst() throws Throwable {
        resetScores();
        int score = solve(0, 0, 30, 0);
        System.err.println("Best score while going alone under 30 minutes: " + score);
    }

    @Override
    void runSecond() throws Throwable {
        resetScores();
        int score = solve(0, 0, 26, 1);
        System.err.println("Best score while going with an elephant under 26 minutes: " + score);
    }
}
