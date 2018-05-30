package at.tugraz.tc.cyfile.crypto;

import java.io.Serializable;

public interface Base64 extends Serializable {
    byte[] decode(String encoded);
    String encode(byte[] bytes);
}
