public class RetrievalResult {
    private final Status status;
    private final String username;
    private final String passwordHash;

    public RetrievalResult(Status status, String username, String passwordHash) {
        this.status = status;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public RetrievalResult(Status status) {
        this.status = status;
        this.username = null;
        this.passwordHash = null;
    }

    public Status getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public enum Status {SUCCESS, INVALID_USERNAME;};
}
