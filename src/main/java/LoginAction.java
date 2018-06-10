import java.security.NoSuchAlgorithmException;

public class LoginAction {
    private final Retriever stubbedRetriever;
    private final Hasher hasher;

    public LoginAction(Retriever stubbedRetriever, Hasher hasher) {

        this.stubbedRetriever = stubbedRetriever;
        this.hasher = hasher;
    }

    public LoginResult execute(String username, String password) throws NoSuchAlgorithmException {
        RetrievalResult retrievedResult = stubbedRetriever.retrieve(username);
        if (retrievedResult.getStatus() == RetrievalResult.Status.SUCCESS) {
            if (retrievedResult.getPasswordHash().equals(hasher.hash(password))) {
                return new LoginResult(LoginResult.Status.SUCCESS, username);
            } else {
                return new LoginResult(LoginResult.Status.BAD_CREDENTIALS);
            }
        }
        return new LoginResult(LoginResult.Status.BAD_CREDENTIALS);
    }
}
