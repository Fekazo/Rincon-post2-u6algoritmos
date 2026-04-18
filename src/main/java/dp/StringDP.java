package dp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringDP {

    /** LCS completo — O(n*m) tiempo y espacio */
    public static int lcsLength(String X, String Y) {
        int m = X.length(), n = Y.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = X.charAt(i - 1) == Y.charAt(j - 1)
                        ? dp[i - 1][j - 1] + 1
                        : Math.max(dp[i - 1][j], dp[i][j - 1]);
        return dp[m][n];
    }

    /** Reconstruye la cadena LCS por backtracking */
    public static String lcsString(String X, String Y) {
        int m = X.length(), n = Y.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = X.charAt(i - 1) == Y.charAt(j - 1)
                        ? dp[i - 1][j - 1] + 1 : Math.max(dp[i - 1][j], dp[i][j - 1]);
        StringBuilder sb = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (X.charAt(i - 1) == Y.charAt(j - 1)) { sb.append(X.charAt(i - 1)); i--; j--; }
            else if (dp[i - 1][j] > dp[i][j - 1]) i--;
            else j--;
        }
        return sb.reverse().toString();
    }

    /** LCS longitud con O(min(n,m)) espacio — dos filas rotatorias */
    public static int lcsMemOpt(String X, String Y) {
        if (X.length() < Y.length()) { String t = X; X = Y; Y = t; } // Y es la menor
        int m = X.length(), n = Y.length();
        int[] prev = new int[n + 1], curr = new int[n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++)
                curr[j] = X.charAt(i - 1) == Y.charAt(j - 1)
                        ? prev[j - 1] + 1 : Math.max(prev[j], curr[j - 1]);
            int[] tmp = prev; prev = curr; curr = tmp;
            java.util.Arrays.fill(curr, 0);
        }
        return prev[n];
    }

    /** Edit Distance — O(n*m) tiempo y espacio */
    public static int editDistance(String X, String Y) {
        int m = X.length(), n = Y.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++) {
                if (X.charAt(i - 1) == Y.charAt(j - 1)) dp[i][j] = dp[i - 1][j - 1];
                else dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                        Math.min(dp[i - 1][j], dp[i][j - 1]));
            }
        return dp[m][n];
    }

    /** Edit Distance con O(min(n,m)) espacio — dos filas rotatorias */
    public static int editDistanceMemOpt(String X, String Y) {
        if (X.length() < Y.length()) { String t = X; X = Y; Y = t; } // Y es la menor
        int m = X.length(), n = Y.length();
        int[] prev = new int[n + 1], curr = new int[n + 1];
        for (int j = 0; j <= n; j++) prev[j] = j;
        for (int i = 1; i <= m; i++) {
            curr[0] = i;
            for (int j = 1; j <= n; j++) {
                if (X.charAt(i - 1) == Y.charAt(j - 1)) curr[j] = prev[j - 1];
                else curr[j] = 1 + Math.min(prev[j - 1], Math.min(prev[j], curr[j - 1]));
            }
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[n];
    }

    public enum Op { MATCH, SUBSTITUTE, INSERT, DELETE }

    /** Reconstruye la secuencia de operaciones del alineamiento óptimo */
    public static List<Op> align(String X, String Y) {
        int m = X.length(), n = Y.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++) {
                if (X.charAt(i - 1) == Y.charAt(j - 1)) dp[i][j] = dp[i - 1][j - 1];
                else dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                        Math.min(dp[i - 1][j], dp[i][j - 1]));
            }
        // Backtracking
        List<Op> ops = new ArrayList<>();
        int i = m, j = n;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && X.charAt(i - 1) == Y.charAt(j - 1)) {
                ops.add(Op.MATCH); i--; j--;
            } else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                ops.add(Op.SUBSTITUTE); i--; j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                ops.add(Op.DELETE); i--;
            } else {
                ops.add(Op.INSERT); j--;
            }
        }
        Collections.reverse(ops);
        return ops;
    }
}