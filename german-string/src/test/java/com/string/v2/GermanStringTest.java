package com.string.v2;

import com.string.german.string.v2.GermanString;
import org.junit.jupiter.api.Test;

public class GermanStringTest {

    private static final String[] testCaseShortStrings = {
            "TNTT",
            "TNT",
            "TNT",
            "TNT",
            "TNT",
            "TNT",
            "TNT",
            "TNT",
            "TNT",
            "TNT",
    };

    @Test
    public void testGermanStringEqualsPerformanceWithString() {

        long start = System.nanoTime();
        for (String testCase: testCaseShortStrings) {
            for (String compare: testCaseShortStrings) {
                boolean ignored = testCase.equals(compare);
            }
        }
        long end = System.nanoTime();
        System.out.println("100 short string comparisons run time:: " + (end - start));

        GermanString[] testCaseShortGermanStrings = new GermanString[testCaseShortStrings.length];
        int i = 0;
        for (String s: testCaseShortStrings) {
            testCaseShortGermanStrings[i++] = new GermanString(s);
        }

        start = System.nanoTime();
        for (GermanString testCase: testCaseShortGermanStrings) {
            for (GermanString compare: testCaseShortGermanStrings) {
                boolean ignored = testCase.equals(compare);
            }
        }
        end = System.nanoTime();
        System.out.println("100 short german string comparisons run time:: " + (end - start));
    }
}
