package at.tugraz.tc.cyfile.crypto.exceptions;

public class InvalidPassPhraseException extends KeyVaultServiceException {

    public InvalidPassPhraseException() {
    }

    public InvalidPassPhraseException(String message) {
        super(message);
    }

    public InvalidPassPhraseException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPassPhraseException(Throwable cause) {
        super(cause);
    }

}
