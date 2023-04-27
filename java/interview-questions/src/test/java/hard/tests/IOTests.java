package hard.tests;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * IO tests
 * https://jenkov.com/tutorials/java-io/index.html
 * https://www.javatpoint.com/java-io
 * https://www.tutorialspoint.com/java/java_files_io.htm
 * https://www.baeldung.com/java-io-vs-nio
 * https://itsobes.ru/JavaSobes/chem-otlichaetsia-blokiruiushchee-chtenie-ot-neblokiruiushchego/
 */
public class IOTests {

    private static final String FILE_NAME = "example.txt";

    @Test
    public void blockingIoWriteAndRead() throws IOException {
        String text = "Hello, World!";

        // write
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME)) {
            byte[] bytes = text.getBytes();
            // wait
            fos.write(bytes);
            System.out.println("File has been written.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            // wait
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Files.delete(Path.of(FILE_NAME));
    }

    /**
     * https://jenkov.com/tutorials/java-nio/index.html
     * https://russianblogs.com/article/9386822493/
     */
    @Test
    public void nioWriteAndRead() throws IOException {
        Path filePath = Paths.get(FILE_NAME);
        String text = "Hello, World!";

        // write
        ByteBuffer buffer = ByteBuffer.wrap(text.getBytes());
        try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            while (buffer.hasRemaining()) {
                fileChannel.write(buffer);
            }
            System.out.println("File has been written.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read
        try {
            String content = Files.readString(filePath);
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Files.delete(filePath);
    }
}
