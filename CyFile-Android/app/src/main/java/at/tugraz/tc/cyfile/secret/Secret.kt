package at.tugraz.tc.cyfile.secret

/**
 * Secret that is used to unlock the system
 */
interface Secret {

    /**
     * Secret value represented in string
     *
     * @return value
     */
    val secretValue: String
}
