package com.netcompany.nio2;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.out;

/**
 * Created by matm on 02-11-2016.
 */
public class FileAttributesTests {
    @Test
    public void testIsDirRegularLink() throws Exception {
//        isDirRegularLink(Paths.get("shortcutToTest.txt"));
        final Path target = Paths.get("target.txt");
        isDirRegularLink(target);
        printOtherAttributes(target);
    }

    @Test
    public void testLastModifiedTimeAndIOwner() throws Exception {
        Path path = Paths.get("target.txtt");
        out.println("For file: " + path);
        out.println(Files.getLastModifiedTime(path));
        out.println(Files.getOwner(path));
    }

    private void isDirRegularLink(Path path) throws IOException {
        out.println("For path: " + path);
        out.println("isDirectory:" + Files.isDirectory(path));
        out.println("isRegularFile:" + Files.isRegularFile(path));
        out.println("isSymbolicLink:" + Files.isSymbolicLink(path));
    }

    private  void printOtherAttributes(Path path) throws IOException {
        out.println("For path:" + path);
        out.println("isHidden:" + Files.isHidden(path));
        out.println("isExecutable:" + Files.isExecutable(path));
        out.println("Size:" + Files.size(path));

        // Compare LinkOption to OpenOption and CopyOption!!!
        // OpenOption and CopyOption are interfaces.
        out.println(Files.getOwner(path, LinkOption.NOFOLLOW_LINKS));
    }
}
