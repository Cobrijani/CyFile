package at.tugraz.tc.cyfile.crypto.impl;

import at.tugraz.tc.cyfile.crypto.Base64;

public class NativeBase64 implements Base64 {
    @Override
    public byte[] decode(String encoded) {
        return android.util.Base64.decode(encoded, android.util.Base64.DEFAULT);
    }

    @Override
    public String encode(byte[] bytes) {
        return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
    }
}
