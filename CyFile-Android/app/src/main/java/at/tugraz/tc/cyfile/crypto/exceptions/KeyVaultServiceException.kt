package at.tugraz.tc.cyfile.crypto.exceptions


open class KeyVaultServiceException : RuntimeException {

    constructor()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)

}