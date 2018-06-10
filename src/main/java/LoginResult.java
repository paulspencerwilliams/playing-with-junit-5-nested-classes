public class LoginResult {
    public enum Status {SUCCESS, BAD_CREDENTIALS;}

    private final Status status;

    private final String username;

    public LoginResult(Status status, String username) {
        this.status = status;
        this.username = username;
    }

    public LoginResult(Status status) {
        this.status = status;
        this.username = null;
    }

    public Status getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }
}
