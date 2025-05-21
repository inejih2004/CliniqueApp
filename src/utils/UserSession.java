package utils;

public class UserSession {
    public static int medecinId;
    public static String medecinNom;
    public static String medecinPrenom;
    public static String medecinSpecialite;

    // Optional: Add a method to reset session if needed
    public static void clear() {
        medecinId = 1;
        medecinNom = "Baba";
        medecinPrenom = "calu";
        medecinSpecialite ="Cardiologie";
    }
}
