package tech.kirya522.mem;

public class ThreadLocalLeak {
    // https://www.baeldung.com/mdc-in-log4j-2-logback#mdc-and-thread-pools
    private static final ThreadLocal<byte[]> TL =
            new ThreadLocal<>();

    public static void leak() {
        TL.set(new byte[5 * 1024 * 1024]);
    }
}
