package fr.xebia.log.configuration;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class RegExpClassPatternTest {

    @Test
    public void when_null_pattern_then_no_class_match() {
        RegExpClassPattern pattern = new RegExpClassPattern(null);
        assertTrue(!pattern.isClassNameMatch("fr/xebia/log/agent/MainAgent"));
    }

    @Test
    public void when_empty_pattern_then_no_class_match() {
        List<String> xebiaP = new ArrayList<String>();
        RegExpClassPattern pattern = new RegExpClassPattern(xebiaP);
        assertTrue(!pattern.isClassNameMatch("fr/xebia/log/agent/MainAgent"));
    }

    @Test
    public void when_valid_xebia_pattern_then_xebia_class_match() {
        List<String> xebiaP = new ArrayList<String>();
        xebiaP.add("fr/xebia/log/.*");
        RegExpClassPattern pattern = new RegExpClassPattern(xebiaP);
        assertTrue(pattern.isClassNameMatch("fr/xebia/log/agent/MainAgent"));
    }

    @Test
    public void when_valid_xebia_pattern_then_java_class_not_match() {
        List<String> xebiaP = new ArrayList<String>();
        xebiaP.add("fr/xebia/log/.*");
        RegExpClassPattern pattern = new RegExpClassPattern(xebiaP);
        assertTrue(!pattern.isClassNameMatch("java/util/List"));
    }

    @Test
    public void when_valid_xebia_and_java_pattern_then_java_and_xebia_class_match() {
        List<String> xebiaP = new ArrayList<String>();
        xebiaP.add("fr/xebia/log/.*");
        xebiaP.add("java/util/.*");
        RegExpClassPattern pattern = new RegExpClassPattern(xebiaP);
        assertTrue(pattern.isClassNameMatch("java/util/List"));
        assertTrue(pattern.isClassNameMatch("fr/xebia/log/agent/MainAgent"));
    }

}
