package at.tugraz.tc.cyfile.secret;

/**
 * Class that checks if the secret is correct
 */
public interface SecretVerifier {

    /**
     * Check if the secret passed is correct or not
     *
     * @param secret secret to be checked
     * @return true if correct, false otherwise
     */
    boolean verify(Secret secret);
}
