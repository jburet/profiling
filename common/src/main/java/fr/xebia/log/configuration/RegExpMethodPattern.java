package fr.xebia.log.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegExpMethodPattern {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegExpMethodPattern.class);

    private List<Pattern> patterns = new ArrayList<Pattern>();

    public RegExpMethodPattern(List<String> patternString) {
        if (patternString != null) {
            for (String ps : patternString) {
                try {
                    patterns.add(Pattern.compile(ps));
                } catch (Exception e) {
                    LOGGER.warn("Regexp not valid for method pattern : {}", ps);
                }
            }
        }
    }

    public boolean match(String classname, String methodname) {
        for (Pattern p : patterns) {
            if (p.matcher(classname + "/" + methodname).matches()) {
                return true;
            }
        }
        return false;
    }
}
