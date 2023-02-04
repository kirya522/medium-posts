public class Main {
    public static void main(String[] args) {
        long size = 10_000_000;
        int pushers = 20;

        new Application(size, pushers).run();
    }
}
