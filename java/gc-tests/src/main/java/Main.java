public class Main {
    public static void main(String[] args) {
        long size = 1_000_000;
        int pushers = 8;

        new Application(size, pushers).run();
    }
}
