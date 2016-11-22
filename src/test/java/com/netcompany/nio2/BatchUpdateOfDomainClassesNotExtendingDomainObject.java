package com.netcompany.nio2;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

/**
 * Real work issue: Update all classes in domain package and its sub packages which do not extend DomainObject
 * What was problem: DomainObject uses apache Commons library (reflection) for equals and hashCode
 * All classes which do not extend it cause problems during comparision etc.
 *
 * Created by matm on 21-11-2016.
 *
 * After update: Update of ~300 classes take ~2-3 seconds
 */
public class BatchUpdateOfDomainClassesNotExtendingDomainObject {

    private static final int INSERT_IMPORT_LINE = 3;
    private final String NOT_EXTENDING_REGEX = "public class ((?!extends).)*$";
    private static final String NOVEMBER_2016_BRANCH = "C:\\source\\TDC0002\\Stabilisation_November2016_BusinessCore\\business-core-common\\src\\main\\java\\dk\\tdc\\businesscore\\domain";
    private static final String JANUARY_2017_BRANCH = "C:\\source\\TDC0002\\Stabilisation_January2017-1_BusinessCore\\business-core-common\\src\\main\\java\\dk\\tdc\\businesscore\\domain";
    private final String ONE_FILE = "C:\\source\\TDC0002\\Stabilisation_January2017-1_BusinessCore\\business-core-common\\src\\main\\java\\dk\\tdc\\businesscore\\domain\\administrator\\AdministratorApplication.java";
    private final String IMPORT_DOMAIN_OBJECT = "import dk.tdc.businesscore.domain.DomainObject;";

    @Test
    public void printStatisticsRegardingClassesNotExtending() throws Exception {
        Path path = Paths.get(JANUARY_2017_BRANCH);
//        Path path = Paths.get(NOVEMBER_2016_BRANCH);
        Stream<Path> s = Files.walk(path).filter(p -> p.toString().endsWith(".java"));
//        s.forEach(p -> printClassNameNotExtending(p));
        Stream<Path> filteredClasses = s.filter(p -> filterOnlyNotExtendedClasses(p));
//        System.out.println("Number of classes not extending anything:" + filteredClasses.count());
        filteredClasses.forEach(System.out::println);
    }

    @Test
    public void makeAllDomainClassesExtendDomainObject() throws Exception {
        Path path = Paths.get(JANUARY_2017_BRANCH);
//        Path path = Paths.get(NOVEMBER_2016_BRANCH);
        Stream<Path> s = Files.walk(path).filter(p -> p.toString().endsWith(".java"));
        s.forEach(p -> printClassNameNotExtending(p));
//        Stream<Path> filteredClasses = s.filter(p -> filterOnlyNotExtendedClasses(p));
//        System.out.println("Number of classes not extending anything:" + filteredClasses.count());
//        filteredClasses.forEach(p -> makeClassExtendDomainObject(p));
    }

    boolean filterOnlyNotExtendedClasses(Path p) {
        final String allPublicClasses = "public class";
        try {
            return Files.lines(p).filter(s -> s.matches(NOT_EXTENDING_REGEX)).count() > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    void printClassNameNotExtending(Path p) {
        final String allPublicClasses = "public class";
        try {
            Files.lines(p).filter(s -> s.matches(NOT_EXTENDING_REGEX)).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateOneFile() throws Exception {
        Path path = Paths.get(ONE_FILE);
        makeClassExtendDomainObject(path);
    }

    public void makeClassExtendDomainObject(Path path){
        int lineNumber = 1;
        List<String> lines = null;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path,
                Charset.defaultCharset(), StandardOpenOption.TRUNCATE_EXISTING)) {
            for(String line : lines){
                if(lineNumber++ == INSERT_IMPORT_LINE){
                    writer.write(IMPORT_DOMAIN_OBJECT);
                    writer.newLine();
                }
                writer.write(updateClassNameLine(line));
                writer.newLine();
            }
        } catch (IOException e) {
// Handle file I/O exception...
        } finally {
        }

    }

    boolean isImportLine(String line){
        return line.contains("import");
    }

    @Test
    public void testUpdateClassName() throws Exception {
        String apnInfo = "public class ApnInfo {";
        String apnInfoExtends = "public class ApnInfo extends{";
        Assert.assertEquals("public class ApnInfo extends DomainObject {", updateClassNameLine(apnInfo));
        Assert.assertEquals(apnInfoExtends, updateClassNameLine(apnInfoExtends));
    }

    private String updateClassNameLine(String line){
        if(!line.matches(NOT_EXTENDING_REGEX)){
            return line;
        }
        return line.replace("{", "extends DomainObject {");
    }
}
