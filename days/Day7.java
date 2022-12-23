package days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Day7 extends Base {
    static class Directory {
        final String name;
        private int size = 0;
        private boolean recalculateSize = true;

        final List<Directory> directories = new ArrayList<>();
        final List<File> files = new ArrayList<>();
        final Directory parentDir;

        Directory(String name, Directory parentDir) {
            this.name = name;
            this.parentDir = Objects.requireNonNullElse(parentDir, this);
        }

        int size() {
            if (recalculateSize) {
                size = 0;
                directories.forEach(dir -> size += dir.size());
                files.forEach(f -> size += f.size);
                recalculateSize = false;
            }
            return size;
        }

        public void addDir(String dirName) {
            directories.add(new Directory(dirName, this));
            recalculateSize = true;
        }

        public void addFile(String fileName, int size) {
            files.add(new File(fileName, size));
            recalculateSize = true;
        }

        @Override
        public String toString() {
            return "- %1$s (dir)".formatted(name);
        }

        @SuppressWarnings("unused")
        void print(int level) {
            directories.forEach(dir -> {
                for (int ii = 0; ii < 2 * level; ++ii) {
                    System.out.print(" ");
                }
                System.out.println(dir);
                dir.print(level + 1);
            });
            files.forEach(file -> {
                for (int ii = 0; ii < 2 * level; ++ii) {
                    System.out.print(" ");
                }
                System.out.println(file);
            });
        }
    }

    record File(String name, int size) {
        @Override
        public String toString() {
            return "- %1$s (file, size=%2$s)".formatted(name, size);
        }
    }

    public Day7() {
        try {
            readInput();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void runFirst() throws Throwable {
        System.out.printf("Sum of directory sizes with max of 100,000: %1$s%n", solveFirst());
    }

    @Override
    void runSecond() throws Throwable {
        System.out.printf("Directory size to be deleted: %1$s%n", solveSecond());
    }

    final Directory root = new Directory("/", null);

    void readInput() throws Throwable {

        root.directories.clear();
        root.files.clear();

        Directory currentDir = root;

        try (BufferedReader reader = new BufferedReader(new FileReader("inputs/day7.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cmd = line.split(" ");
                if ("$".equals(cmd[0])) {
                    String exec = cmd[1];
                    if ("cd".equals(exec)) {
                        String name = cmd[2];
                        if ("..".equals(name)) {
                            currentDir = currentDir.parentDir;
                        } else if ("/".equals(name)) {
                            currentDir = root;
                        } else {
                            currentDir = currentDir.directories.stream().filter(dir -> dir.name.equals(name)).findFirst().orElseThrow();
                        }
                    }
                } else {
                    if ("dir".equals(cmd[0])) {
                        String dirName = cmd[1];
                        currentDir.addDir(dirName);
                    } else {
                        int size = Integer.parseInt(cmd[0]);
                        String fileName = cmd[1];
                        currentDir.addFile(fileName, size);
                    }
                }
            }
        }
    }

    private int solveFirst() {
        AtomicInteger result = new AtomicInteger();
        checkFirst(root, result);
        return result.get();
    }

    private int solveSecond() {
        Map<Directory, Integer> space = new HashMap<>();
        long totalSpace = 70_000_000L;
        long goal = 30_000_000L;
        long availableSpace = totalSpace - root.size();

        checkSecond(root, space);
        Optional<Map.Entry<Directory, Integer>> min = space.entrySet().stream()
                .filter(entry -> availableSpace + entry.getValue() >= goal)
                .min(Map.Entry.comparingByValue());
        if (min.isPresent()) {
            return min.get().getValue();
        } else {
            return 0;
        }
    }

    private void checkFirst(Directory directory, AtomicInteger result) {
        int size = directory.size();
        if (size < 100000L) {
            result.addAndGet(size);
        }
        directory.directories.forEach(entry -> checkFirst(entry, result));
    }

    private void checkSecond(Directory directory, Map<Directory, Integer> result) {
        result.put(directory, directory.size());
        directory.directories.forEach(dir -> checkSecond(dir, result));
    }
}
