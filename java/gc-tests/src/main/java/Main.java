public class Main {
    public static void main(String[] args) throws InterruptedException {
        long size = 100_000_000;
        int processors = 1;
        Thread.sleep(2000);
        new Application(size, processors).run();

        while (true) {

        }
    }
}
