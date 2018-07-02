package at.tugraz.tc.cyfile.secret

/**
 * Responsible for prompting the secret from user to input
 */
interface SecretPrompter {

    /**
     * Prompts secret from the user on the UI
     */
    fun promptSecret()
}
