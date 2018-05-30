package at.tugraz.tc.cyfile.crypto.impl;

import at.tugraz.tc.cyfile.crypto.Base64;

public class ApacheCodecBase64 implements Base64 {
    @Override
    public byte[] decode(String encoded) {
        return org.apache.commons.codec.binary.Base64.decodeBase64(encoded);
    }

    @Override
    public String encode(byte[] bytes) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }
}
