package at.tugraz.tc.cyfile.secret;

/**
 * Responsible for prompting the secret from user to input
 */
public interface SecretPrompter {

    /**
     * Prompts secret from the user on the UI
     */
    void promptSecret();
}
