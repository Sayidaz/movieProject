package utils;

public class TitleNormalizer {

    public static String normalize(String title) {
        if (title == null) {
            return "";
        }
        return title.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", "");
    }

    public static boolean areTitlesMatching(String scannedTitle, String dbTitle) {
        String normScanned = normalize(scannedTitle);
        String normDb = normalize(dbTitle);

        if (normDb.contains(normScanned) || normScanned.contains(normDb)) {
            return true;
        }

        int distance = levenshteinDistance(normScanned, normDb);
        int threshold = Math.max(normScanned.length(), normDb.length()) / 4;
        return distance <= threshold;
    }

    public static int levenshteinDistance(String s, String t) {
        int m = s.length(), n = t.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = s.charAt(i - 1) == t.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(dp[i - 1][j] + 1,
                        Math.min(dp[i][j - 1] + 1,
                                dp[i - 1][j - 1] + cost));
            }
        }
        return dp[m][n];
    }
}

