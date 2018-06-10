import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginActionWithMatchingUsernameTest {

    @Nested
    class WithMatchingUsername {
        private Retriever stubbedRetriever;

        @BeforeEach
        void stubbedRetrieverReturnsMatchingUser() {
            stubbedRetriever = mock(Retriever.class);

            when(stubbedRetriever.retrieve("123")).thenReturn(new RetrievalResult(RetrievalResult.Status.SUCCESS, "123", "retrievedHash"));
        }

        @Nested
        class AndNoSuchAlgorithm {

            @Test
            void executeThrowsException() throws NoSuchAlgorithmException {
                Hasher stubbedHasher = mock(Hasher.class);
                LoginAction sut = new LoginAction(stubbedRetriever, stubbedHasher);

                when(stubbedHasher.hash(any())).thenThrow(NoSuchAlgorithmException.class);

                Assertions.assertThrows(NoSuchAlgorithmException.class, () -> sut.execute("123", "abc"));
            }
        }

        @Nested
        class AndMatchingPassword {
            private Hasher stubbedHasher;
            private LoginAction sut;
            private LoginResult result;

            @BeforeEach
            void executeWithMatchingPassword() throws NoSuchAlgorithmException {
                stubbedHasher = mock(Hasher.class);
                sut = new LoginAction(stubbedRetriever, stubbedHasher);

                when(stubbedHasher.hash("abc")).thenReturn("retrievedHash");

                result = sut.execute("123", "abc");
            }

            @Test
            void itReturnsLoginSuccess() throws NoSuchAlgorithmException {
                assertThat(result.getStatus(), equalTo(LoginResult.Status.SUCCESS));
            }

            @Test
            void itReturnsUsername() throws NoSuchAlgorithmException {
                assertThat(result.getUsername(), equalTo("123"));
            }
        }

        @Nested
        class AndMismatchingPassword {
            private Hasher hasher;
            private LoginAction sut;
            private LoginResult result;

            @BeforeEach
            void executeWithMismatchedPassword() throws NoSuchAlgorithmException {
                hasher = mock(Hasher.class);
                sut = new LoginAction(stubbedRetriever, hasher);

                when(hasher.hash("abc")).thenReturn("notRetrievedHash");

                result = sut.execute("123", "abc");
            }

            @Test
            void itReturnsBadCredentials() throws NoSuchAlgorithmException {
                assertThat(result.getStatus(), equalTo(LoginResult.Status.BAD_CREDENTIALS));
            }

            @Test
            void itReturnsNullUsername() throws NoSuchAlgorithmException {
                assertThat(result.getUsername(), nullValue());
            }
        }
    }

    @Nested
    class WithNonMatchingUsername {
        private Retriever stubbedRetriever;
        private LoginAction sut;
        private LoginResult result;

        @BeforeEach
        void executeWithNonMatchingUsername() throws NoSuchAlgorithmException {
            stubbedRetriever = mock(Retriever.class);
            sut = new LoginAction(stubbedRetriever, null);

            when(stubbedRetriever.retrieve("123")).thenReturn(new RetrievalResult(RetrievalResult.Status.INVALID_USERNAME));

            result = sut.execute("123", "abc");
        }

        @Test
        void itReturnsBadCredentials() throws NoSuchAlgorithmException {
            assertThat(result.getStatus(), equalTo(LoginResult.Status.BAD_CREDENTIALS));
        }

        @Test
        void itReturnsNullUsername() throws NoSuchAlgorithmException {
            assertThat(result.getUsername(), nullValue());
        }
    }
}
