package at.tugraz.tc.cyfile.crypto.impl

import at.tugraz.tc.cyfile.crypto.Base64

class ApacheCodecBase64 : Base64 {
    override fun decode(encoded: String): ByteArray {
        return org.apache.commons.codec.binary.Base64.decodeBase64(encoded)
    }

    override fun encode(bytes: ByteArray): String {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes)
    }
}
