package at.tugraz.tc.cyfile.secret;

/**
 * Repository that manages saving and retrieving {@link Secret} of the application
 */
public interface SecretRepository {

    /**
     * Retrieve the secret from persistence
     *
     * @return secret
     */
    Secret getSecret();

    /**
     * Save the secret to persistence
     *
     * @param secret secret
     * @return true if secret is saved, false otherwise
     */
    boolean saveSecret(Secret secret);
}
