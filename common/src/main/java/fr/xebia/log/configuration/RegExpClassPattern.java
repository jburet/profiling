package fr.xebia.log.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 */
public class RegExpClassPattern {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegExpClassPattern.class);

    private List<Pattern> patterns = new ArrayList<Pattern>();

    public RegExpClassPattern(List<String> patternString) {
        if (patternString != null) {
            for (String ps : patternString) {
                try {
                    patterns.add(Pattern.compile(ps));
                } catch (Exception e) {
                    LOGGER.warn("Regex^p not valid for class pattenrn : {}", ps);
                }
            }
        }
    }

    /**
     * Return true if classname match one of stored pattern
     * @param classname
     * @return
     */
    public boolean isClassNameMatch(String classname) {
        for (Pattern p : patterns) {
            if (p.matcher(classname).matches()) {
                return true;
            }
        }
        return false;
    }
}
