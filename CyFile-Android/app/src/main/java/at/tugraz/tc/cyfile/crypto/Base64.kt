package at.tugraz.tc.cyfile.crypto

import java.io.Serializable


interface Base64 : Serializable {
    fun decode(encoded: String): ByteArray
    fun encode(bytes: ByteArray): String
}