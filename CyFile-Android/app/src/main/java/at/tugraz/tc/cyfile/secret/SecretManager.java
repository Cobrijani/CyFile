package at.tugraz.tc.cyfile.secret;

public interface SecretManager {

    boolean secretIsSet();

    /**
     * Verify that the secret is correct
     *
     * @param secret secret to be checked
     * @return true if valid, false otherwise
     */
    boolean verify(Secret secret);


    /**
     * Set the secret.
     * <p>
     * Can only be set if the secret is not already set
     *
     * @param secret secret to be set
     * @return true if new secret is set, false otherwise
     */
    boolean setSecret(Secret secret);
}
