package dp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class StringDPTest {

    // Helper: verifica que 'sub' es subsecuencia de 's'
    private boolean isSubsequence(String sub, String s) {
        int idx = 0;
        for (int i = 0; i < s.length() && idx < sub.length(); i++)
            if (s.charAt(i) == sub.charAt(idx)) idx++;
        return idx == sub.length();
    }

    // (a) Cadenas vacías
    @Test
    void testEmptyStrings() {
        assertEquals(0, StringDP.lcsLength("", "abc"));
        assertEquals(0, StringDP.lcsLength("abc", ""));
        assertEquals(0, StringDP.lcsLength("", ""));
        assertEquals("", StringDP.lcsString("", "abc"));
        assertEquals("", StringDP.lcsString("abc", ""));
        assertEquals(0, StringDP.lcsMemOpt("", "abc"));
        assertEquals(0, StringDP.lcsMemOpt("abc", ""));
    }

    // (b) Cadenas idénticas
    @Test
    void testIdenticalStrings() {
        String s = "helloworld";
        assertEquals(s.length(), StringDP.lcsLength(s, s));
        assertEquals(s, StringDP.lcsString(s, s));
        assertEquals(s.length(), StringDP.lcsMemOpt(s, s));
    }

    // (c) Sin caracteres en común
    @Test
    void testNoCommonChars() {
        assertEquals(0, StringDP.lcsLength("aaaa", "bbbb"));
        assertEquals("", StringDP.lcsString("aaaa", "bbbb"));
        assertEquals(0, StringDP.lcsMemOpt("aaaa", "bbbb"));
    }

    // (d) lcsString retorna subsecuencia válida de ambas cadenas
    @Test
    void testLcsStringIsValidSubsequence() {
        String[][] pairs = {
            {"ABCBDAB", "BDCABA"},
            {"algorithm", "altruistic"},
            {"dynamic", "programming"},
            {"abcdef", "ace"}
        };
        for (String[] p : pairs) {
            String lcs = StringDP.lcsString(p[0], p[1]);
            assertTrue(isSubsequence(lcs, p[0]),
                "LCS '" + lcs + "' no es subsecuencia de '" + p[0] + "'");
            assertTrue(isSubsequence(lcs, p[1]),
                "LCS '" + lcs + "' no es subsecuencia de '" + p[1] + "'");
            assertEquals(StringDP.lcsLength(p[0], p[1]), lcs.length(),
                "Longitud del LCS no coincide con lcsLength");
        }
    }

    // (e) lcsMemOpt == lcsLength para 5 pares con longitudes 10-200
    @Test
    void testMemOptMatchesFullDp() {
        String[][] pairs = {
            {"abcdefghij", "acegikmoqs"},
            {"thequickbrownfox", "quickbrownfoxjumps"},
            {"dynamicprogrammingisuseful", "greedyalgorithmisfast"},
            {"aabbccddeeffgghhiijjkkllmmnnooppqqrrssttuuvvwwxxyyzz",
             "zyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba"},
            {"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrs",
             "zyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba"}
        };
        for (String[] p : pairs) {
            int full = StringDP.lcsLength(p[0], p[1]);
            int opt  = StringDP.lcsMemOpt(p[0], p[1]);
            assertEquals(full, opt,
                "lcsMemOpt difiere de lcsLength para X.len=" + p[0].length() + " Y.len=" + p[1].length());
        }
    }

    // --- editDistance ---

    // (a) editDistance("", X) == X.length()
    @Test
    void testEditDistanceEmpty() {
        assertEquals(3, StringDP.editDistance("", "abc"));
        assertEquals(5, StringDP.editDistance("hello", ""));
        assertEquals(0, StringDP.editDistance("", ""));
    }

    // (b) editDistance(X, X) == 0
    @Test
    void testEditDistanceIdentical() {
        assertEquals(0, StringDP.editDistance("kitten", "kitten"));
        assertEquals(0, StringDP.editDistance("abcdefghij", "abcdefghij"));
    }

    // (c) editDistance("kitten", "sitting") == 3
    @Test
    void testEditDistanceKittenSitting() {
        assertEquals(3, StringDP.editDistance("kitten", "sitting"));
    }

    // (d) operaciones de align consistentes con editDistance
    @Test
    void testAlignConsistentWithEditDistance() {
        String[][] pairs = {
            {"kitten", "sitting"},
            {"algorithm", "altruistic"},
            {"", "hello"},
            {"abc", "abc"},
            {"abcdef", "azced"}
        };
        for (String[] p : pairs) {
            int dist = StringDP.editDistance(p[0], p[1]);
            List<StringDP.Op> ops = StringDP.align(p[0], p[1]);
            long cost = ops.stream().filter(o ->
                o == StringDP.Op.SUBSTITUTE ||
                o == StringDP.Op.INSERT ||
                o == StringDP.Op.DELETE).count();
            assertEquals(dist, cost,
                "align cost != editDistance para X='" + p[0] + "' Y='" + p[1] + "'");
        }
    }

    // (e) editDistanceMemOpt == editDistance para 5 pares longitud 20-300
    @Test
    void testEditDistanceMemOptMatchesFull() {
        String[][] pairs = {
            {"abcdefghijklmnopqrst", "tsrqponmlkjihgfedcba"},                      // len 20
            {"dynamicprogrammingisusefulalgorithm", "greedyalgorithmisveryfastindeed"}, // len ~35-31
            {"aabbccddeeffgghhiijjkkllmmnnooppqqrrssttuuvvwwxxyyzz" +
             "aabbccddeeffgghhiijjkkllmmnnooppqqrrssttuuvvwwxxyyzz",
             "zyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba" +
             "zyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba"},               // len 104
            {"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
             "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
             "abcdefghijklmnopqrstuvwxyzab",
             "zyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba" +
             "zyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba" +
             "zyxwvutsrqponmlkjihgfedcba"},                                          // len ~132-130
            {"abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
             "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
             "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
             "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
             "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" +
             "abcdefghijklmnopqrstuvwxyzabcd",
             "zyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba" +
             "zyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba" +
             "zyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba" +
             "zyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba" +
             "zyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba" +
             "zyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba"}               // len ~290-286
        };
        for (String[] p : pairs) {
            int full = StringDP.editDistance(p[0], p[1]);
            int opt  = StringDP.editDistanceMemOpt(p[0], p[1]);
            assertEquals(full, opt,
                "editDistanceMemOpt difiere para X.len=" + p[0].length() + " Y.len=" + p[1].length());
        }
    }
}