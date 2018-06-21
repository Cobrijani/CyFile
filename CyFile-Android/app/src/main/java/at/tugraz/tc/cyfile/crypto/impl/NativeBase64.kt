package at.tugraz.tc.cyfile.crypto.impl

import at.tugraz.tc.cyfile.crypto.Base64

class NativeBase64 : Base64 {
    override fun decode(encoded: String): ByteArray {
        return android.util.Base64.decode(encoded, android.util.Base64.DEFAULT)
    }

    override fun encode(bytes: ByteArray): String {
        return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
    }
}
