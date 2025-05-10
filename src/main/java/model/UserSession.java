package model;

public class UserSession {
    private static UserSession instance;
    private String username;

    // Private constructor to enforce the Singleton pattern
    private UserSession() {}

    // Get the single instance of UserSession
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Set the logged-in username
    public void setUsername(String username) {
        this.username = username;
    }

    // Get the logged-in username
    public String getUsername() {
        return username;
    }

    // Clear the session (e.g., on logout)
    public void clearSession() {
        username = null;
    }
}