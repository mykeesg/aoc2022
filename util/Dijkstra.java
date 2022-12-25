package util;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class Dijkstra<T> implements Runnable {
    private final Deque<T> path = new LinkedList<>();

    final Collection<T> allNodes;
    final T start;
    final Predicate<T> endGoal;
    final BiFunction<T, T, Long> edgeCost;
    final Function<T, Collection<T>> neighbours;

    public Dijkstra(Collection<T> allNodes,
                    T start,
                    Predicate<T> endGoal,
                    BiFunction<T, T, Long> edgeCost,
                    Function<T, Collection<T>> neighbours) {
        this.allNodes = allNodes;
        this.start = start;
        this.endGoal = endGoal;
        this.edgeCost = edgeCost;
        this.neighbours = neighbours;
    }

    @Override
    public void run() {
        path.clear();

        final Map<T, T> parentNodes = new HashMap<>();
        final Map<T, Long> distances = new HashMap<>();

        PriorityQueue<T> unvisited = new PriorityQueue<>(Comparator.comparing(distances::get));
        allNodes.forEach(node -> {
            distances.put(node, Long.MAX_VALUE);
            parentNodes.put(node, null);
            unvisited.add(node);
        });

        distances.put(start, 0L);
        //has no built-in recalculation method for priority
        unvisited.remove(start);
        unvisited.add(start);

        T end = null;

        while (!unvisited.isEmpty()) {
            T current = unvisited.remove();

            if (endGoal.test(current)) {
                end = current;
                break;
            }

            for (T neighbour : neighbours.apply(current)) {
                if (unvisited.contains(neighbour)) {
                    long cost = distances.get(current) + edgeCost.apply(current, neighbour);
                    if (cost < distances.get(neighbour)) {

                        distances.put(neighbour, cost);
                        parentNodes.put(neighbour, current);

                        //has no built-in recalculation method for priority
                        unvisited.remove(neighbour);
                        unvisited.add(neighbour);
                    }
                }
            }
        }

        if (end != null) {
            T current = end;
            while ((current = parentNodes.get(current)) != null) {
                path.addLast(current);
            }
        }
    }

    public boolean hasPath() {
        return !path.isEmpty();
    }

    public Deque<T> getPath() {
        return path;
    }
}
