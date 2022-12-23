package days;

public abstract class Base implements Runnable {
    @Override
    public void run() {
        try {
            runFirst();
            runSecond();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    abstract void runFirst() throws Throwable;

    abstract void runSecond() throws Throwable;
}
