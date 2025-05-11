package nazeel.data_types;

/**
 * Represents a system user (tester, admin, or general user).
 * This object holds the user's login credentials and display name.
 * <p>
 * It's commonly used to:
 * - Login during automation
 * - Assert against logged-in user info
 * - Pass around authenticated user context
 */
public class User {

    // -------------------------
    // Fields
    // -------------------------

    private String name;         // Full name of the user (used for assertions or UI display)
    private String username;     // Username used to log into the system
    private String password;     // Login password
    private String accessCode;   // 5-digit access code used for 2FA-style login

    // -------------------------
    // Setters (Fluent-style optional if needed)
    // -------------------------

    /**
     * Sets the full name of the user (e.g., shown in top navbar).
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the username used for login.
     */
    public void setUsername(String name) {
        this.username = name;
    }

    /**
     * Sets the password for login.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the access code used after entering username & password.
     */
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    // -------------------------
    // Getters
    // -------------------------

    /**
     * Returns the full name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the username for login.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the login password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the 2FA-style access code.
     */
    public String getAccessCode() {
        return accessCode;
    }
}
