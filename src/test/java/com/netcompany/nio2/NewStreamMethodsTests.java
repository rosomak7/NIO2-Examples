package com.netcompany.nio2;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by matm on 03-11-2016.
 */
public class NewStreamMethodsTests {
    @Test
    public void testWalk() throws Exception {
        Path path = Paths.get(".");
        Files.walk(path).filter(p -> p.toString().endsWith(".java")).forEach(System.out::println);

        //FileVisitOption is enum, like LinkOption, not interface.
        Files.walk(path, 100, FileVisitOption.FOLLOW_LINKS);
    }

    @Test
    public void testFind() throws Exception {
        long dateFilter = 1420070400000l;
        Path path = Paths.get(".");
        //biPredicate takes path and second is basic file attribute
        Stream<Path> stream = Files.find(path, 10,
                (p,a) -> p.toString().endsWith(".java")
                        && a.lastModifiedTime().toMillis()>dateFilter);
        stream.forEach(System.out::println);
    }

    @Test
    public void testListDirContentOneLevelDeep() throws Exception {
        Path path = Paths.get(".");
        Files.list(path).filter(p -> !Files.isDirectory(p)).map(p -> p.toAbsolutePath()).forEach(System.out::println);
    }

    @Test
    public void testPrintingFileContents() throws Exception {
        Path path = Paths.get("target.txt");
//        Path path = Paths.get(".");
        Files.lines(path).forEach(System.out::println);

    }

    @Test
    public void testFilteredContentsSearch() throws Exception {
//        Path path = Paths.get("sharks.log");
        Path path = Paths.get("businesscore.log");

//            System.out.println(
                    Files.lines(path)
                    .filter(s -> s.contains("DEBUG"))
                    .map(s -> s).forEach(System.out::println);
//                            collect(Collectors.toList()));

    }
}
