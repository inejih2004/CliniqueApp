package utils;

public class UserSession {
    public static int medecinId;
    public static String medecinNom;
    public static String medecinPrenom;
    public static String medecinSpecialite;
    private static int currentUserId;
    public static void setCurrentUserId(int id) {
        currentUserId = id;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }
}