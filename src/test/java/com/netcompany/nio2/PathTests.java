package com.netcompany.nio2;

import org.junit.Test;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.ProviderNotFoundException;
import java.nio.file.spi.FileSystemProvider;
import java.util.Iterator;
import java.util.jar.Pack200;

import static java.lang.System.*;

/**
 * Created by matm on 06-10-2016.
 */
public class PathTests {

    @Test//(expected = InvalidPathException.class)
    public void testInvalidPathException() throws Exception {
        Path invalidPath = Paths.get("/c/d:/");
        Path pathByFileSystem = FileSystems.getDefault().getPath("c:\\sdf/asd");

    }

    // Excercise: How to add http FileSystemProvider
    @Test//(expected = ProviderNotFoundException.class)
    public void testGetFileSystem() throws Exception {
        out.println(FileSystemProvider.installedProviders());
        FileSystem remote = FileSystems.getFileSystem(new URI("http:///www.selikoff.net"));
        out.println(remote);
        Path path4 = Paths.get(new URI("http://www.wiley.com"));
        URI uri4 = path4.toUri();
    }


    @Test
    public void testGetByURI() throws Exception {
        Path path = Paths.get("C:", "sroce");//artificial
        URI uri = new URI("file:///c:/ssource");
        Path uriPath = Paths.get(uri);
        printPathProperties(path);
        printPathProperties(uriPath);

    }

    /**
     *  Paths returned by pathIterator are relative!!! compare println of absoluteDirecoty and relativeSub
     * @throws Exception
     */
    @Test
    public void viewPathAndIterateSubDirectories() throws Exception {
        Path absoluteDirectory = Paths.get("C:", "test", "resources", "bla");
        Iterator<Path> pathIterator = absoluteDirectory.iterator();
        int i = 0;
        for(Path p: absoluteDirectory){

        }
        out.println("For path: " + absoluteDirectory);
        while(pathIterator.hasNext()){
            Path relativeSub = pathIterator.next();
            out.println("Element "+ i++ +" is: " +relativeSub);
            printPathProperties(relativeSub); // Q: Is it absolute? What is root? What is root for relative path?
        }
        printSubdirectories(absoluteDirectory); // Q: Is the order of printing the same as for iterator?
        printByGetParent(absoluteDirectory);
    }

    @Test
    public void testForRelativePath() throws Exception {
        Path relativePath = Paths.get("q:/test/aters/", "resources/test", "bla");//artificial
        Path relativePath2 = Paths.get("test/aters", "resources", "bla"); // Q: what is absolute path? the difference between these two is only first slash!!!
        printPathProperties(relativePath);
        printPathProperties(relativePath2);
//        printPathProperties(Paths.get("."));
    }

    @Test
    public void testForSubpath() throws Exception {
        Path relativePath = Paths.get("\\test/aters/", "resources", "bla");//artificial
        printPathProperties(relativePath);

        Path subpath = relativePath.subpath(0,3);
        printPathProperties(subpath);

        //Q: What happens when executing below lines?
        Path subpath4 = relativePath.subpath(0,5);
//        Path subpath3 = relativePath.subpath(1,1);
    }

    @Test
    public void testRelativize() throws Exception {
        Path path1 = Paths.get("fish.txt");
//        Path path2 = Paths.get("birds.txt");
//        out.println(path1.relativize(path2));
//        out.println(path2.relativize(path1));

        Path path3 = Paths.get("E:\\habitat");
        Path path4 = Paths.get("E:\\sanctuary\\raven");
        out.println(path3.relativize(path4));
        out.println(path4.relativize(path3));

        //Q: what happens when relative is relativized with absolute or two absolutes with different root?
        Path path5 = Paths.get("c:/adsf");
        out.println(path3.relativize(path1));
//        out.println(path3.relativize(path5));
    }

    @Test
    public void testResolve() throws Exception {
        final Path path1 = Paths.get("/cats/../panther");
        final Path path2 = Paths.get("food");
//        out.println(path1.resolve(path2));

        //Q: What happens when Unix specific absolute is appended?
        final Path path4 = Paths.get("tiger", "cage");
//        printPathProperties(path4);
//
        final Path path3 = Paths.get("/turkey/food");
//        out.println(path3.resolve(path4));

        //Q: What happens when absolute path is appended to this?
        final Path realAbsolute = Paths.get("c:/asdf");
        out.println(path3.resolve(realAbsolute));
    }

    @Test
    public void testNormalize() throws Exception {
        Path path3 = Paths.get("E:\\data");
        Path path4 = Paths.get("E:\\user\\home");
        Path relativePath = path3.relativize(path4);
        out.println("Relativized path: " + relativePath);
        System.out.println(path3.resolve(relativePath));
//        System.out.println(path3.resolve(relativePath).normalize());
    }

    @Test
    public void testToRealPath() throws Exception {
        Path path = Paths.get("srck");
        out.println(path.toRealPath());

        out.println(Paths.get(".").toAbsolutePath());
    }

    private void printPathProperties(Path path) {
        out.println("For path: " + path);
        out.println("GetFileName: " + path.getFileName());
        out.println("isAbsolute: " + path.isAbsolute());
        out.println("toAbsolutePath: " + path.toAbsolutePath());
        out.println("getRoot: " + path.getRoot());
        out.println("getNameCount: " + path.getNameCount());
        out.println("getFileName: " + path.getFileName());
        out.println();
    }

    private void printSubdirectories(Path path){
        for(int i=0; i<path.getNameCount(); i++) {
            System.out.println(" Element "+i+" is: "+path.getName(i));
        }
    }

    private static void printByGetParent(Path path) {
        Path currentParent = path;
        out.println("Printing by getParent for path: " + path);
        while ((currentParent = currentParent.getParent()) != null) {
            System.out.println(" Current parent is: " + currentParent);
        }
    }

    @Test
    public void testEquals() throws Exception {
        Path absoluteDirectory = Paths.get("C:", "test", "resources", "bla");
//        out.println(absoluteDirectory.equals(absoluteDirectory.toAbsolutePath()));

        Path relativePath = Paths.get("/test/aters/", "resources", "bla");
        Path notNormalizedPath = Paths.get("/test/../test/aters/", "resources", "bla");
//        out.println(relativePath.equals(notNormalizedPath));
        out.println(relativePath.toAbsolutePath().equals(notNormalizedPath.normalize()));
        out.println(relativePath.toAbsolutePath());
        out.println(notNormalizedPath.normalize());
    }
}
