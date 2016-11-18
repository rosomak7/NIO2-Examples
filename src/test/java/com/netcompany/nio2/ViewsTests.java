package com.netcompany.nio2;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;

import static java.lang.System.*;

/**
 * Created by matm on 03-11-2016.
 */
public class ViewsTests {

    @Test
    public void testBasicFileAttributes() throws Exception {
        Path path = Paths.get("target.txt");
        BasicFileAttributes data = Files.readAttributes(path, PosixFileAttributes.class);

        out.println("For file:" + path);
        out.println("Is path a directory? "+data.isDirectory());
        out.println("Is path a regular file? "+data.isRegularFile());
        out.println("Is path a symbolic link? "+data.isSymbolicLink());
        out.println("Path not a file, directory, nor symbolic link? "+  data.isOther());
        out.println("Size (in bytes): "+data.size());
        out.println("Creation date/time: " + data.creationTime());
        out.println("Last modified date/time: "+ data.lastModifiedTime());
        out.println("Last accessed date/time: "+ data.lastAccessTime());
        out.println("Unique file identifier (if available): "+ data.fileKey());

    }

    @Test
    public void testBasicFileAttributeView() throws Exception {
        Path path = Paths.get("target.txt");
        BasicFileAttributeView view = Files.getFileAttributeView(path,BasicFileAttributeView.class);
        //Still we can access Attributes
        BasicFileAttributes data = view.readAttributes();
        out.println("Last modified date/time: "+ data.lastModifiedTime());
        FileTime lastModifiedTime = FileTime.fromMillis(
                data.lastModifiedTime().toMillis()+60_000);
        view.setTimes(lastModifiedTime,null,null);

        //Q: Are outcomes from below printlines the same?
        out.println("Last modified date/time using Attributes: "+ data.lastModifiedTime());
        out.println("Last modified date/time using Files: "+ Files.getLastModifiedTime(path));

    }
}
