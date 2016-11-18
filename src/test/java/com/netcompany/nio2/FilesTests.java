package com.netcompany.nio2;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.*;

/**
 * Created by matm on 02-11-2016.
 */
public class FilesTests {

    @Test
    public void testPathsExist() throws Exception {
        Path path = Paths.get("src");
        out.println(Files.exists(path));
    }

    @Test
    public void testIsSameFile() throws IOException {
        Path relativePath = Paths.get("/test/aters/", "resources", "bla");
        Path notNormalizedPath = Paths.get("/test/../test/aters/", "resources", "bla");

        //Q: Is the result the same for below executions?
      out.println(Files.isSameFile(relativePath, notNormalizedPath));
//        out.println(Files.isSameFile(relativePath, notNormalizedPath.normalize()));

        // Q: How to work with symbolic links?
//        Path pathToTestTXT = Paths.get("test.txt");
//        Path pathToTestShortcut = Paths.get("shortcutToTest.txt");
//        Files.isSameFile(pathToTestTXT, pathToTestShortcut);
    }


    @Test
    public void testCreateDirectories() throws Exception {
        Path nonExistentDirectories = Paths.get("dir1", "dir2");
        out.println("Directory: " + nonExistentDirectories + " exists: " + Files.exists(nonExistentDirectories));
        Files.createDirectories(nonExistentDirectories);
        out.println("Directory: " + nonExistentDirectories + " exists: " + Files.exists(nonExistentDirectories));
    }

    @Test
    public void testCopyDirectory() throws IOException {
        Path currentWorkingDir = Paths.get(".");
        Path copyOfCurrentWorkingDir = Paths.get("coppyOfWorkingDir");
        Path path = Files.copy(currentWorkingDir, copyOfCurrentWorkingDir, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        out.println(path);
    }

    @Test
    public void testCopyWithStreams() throws Exception {
        try (InputStream is = new FileInputStream("test.txt");
             OutputStream out = new FileOutputStream("target.txt")) {
// Copy stream data to file
            Files.copy(is, Paths.get("wolf.txt"), StandardCopyOption.REPLACE_EXISTING);
// Copy file data to stream
            Files.copy(Paths.get("clown.txt"), out);
        }
    }

    @Test
    public void testMove() throws Exception {
//        Files.move(Paths.get("test.txt"), Paths.get("NewTest.txt"));
        Files.move(Paths.get("test.txt"), Paths.get("NewTest.txt"), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void testDelete() throws Exception {
        Path dirPath = Paths.get("dir1", "dir2");
//        Files.delete(dirPath);
        out.println("File existed:" + Files.deleteIfExists(dirPath));
    }

    @Test
    public void testBuferredReader() throws Exception {
        Path path = Paths.get("gorilla.txt");
        try (BufferedReader reader = Files.newBufferedReader(path,
                Charset.forName("UTF-8"))) {
// Read from the stream
            String currentLine = null;
            while((currentLine = reader.readLine()) != null)
                System.out.println(currentLine);
        } catch (IOException e) {
// Handle file I/O exception...
        }
    }

    @Test
    public void testBuferredWriter() throws Exception {
        Path path = Paths.get("gorilla.txt");
        List<String> data = new ArrayList();
        try (BufferedWriter writer = Files.newBufferedWriter(path,
                Charset.forName("UTF-8"), StandardOpenOption.APPEND)) {
            writer.write("Hello World");
            writer.newLine();
            writer.write("My dear yellow submarine!");
            writer.newLine();
        } catch (IOException e) {
// Handle file I/O exception...
        } finally {
        }

    }

    @Test
    public void testReadAllLines() throws Exception {
        Path path = Paths.get("gorilla.txt");
            final List<String> lines = Files.readAllLines(path,Charset.forName("UTF-8"));
        Files.readAllLines(path);
        Charset.defaultCharset();
            for(String line: lines) {
                System.out.println(line);
            }

    }
}
