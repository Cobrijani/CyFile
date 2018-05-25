package at.tugraz.tc.cyfile.secret.impl;



import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import at.tugraz.tc.cyfile.crypto.Base64;
import at.tugraz.tc.cyfile.secret.Secret;

public class HashedSecret extends AbstractSecret implements Serializable {
    private final String secretValue;
    private final String pepper;
    private final Base64 encoder;

    public static final int PEPPER_LENGTH = 8;
    private static final String SALT = "F52FBD32B2B3B86FF88EF";

    public HashedSecret(Secret secret, Base64 encoder) {
        this.encoder = encoder;
        pepper = getPepper();
        this.secretValue = getHashForSecret(secret, pepper);
    }

    private String getHashForSecret(Secret secret, String pepper) {
        String secretValue = secret.getSecretValue() + SALT + pepper;
        return getSHADigest(secretValue);
    }

    private String getPepper() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[PEPPER_LENGTH];
        random.nextBytes(bytes);
        return this.encoder.encode(bytes);
    }

    private String getSHADigest(String secretValue) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }

        md.update(secretValue.getBytes());
        byte[] digest = md.digest();
        return this.encoder.encode(digest);
    }

    @Override
    public String getSecretValue() {
        return this.secretValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof  HashedSecret) {
            return ((HashedSecret) o).getSecretValue().equals(this.getSecretValue());
        }

        if (o instanceof Secret) {
            return getHashForSecret((Secret) o, this.pepper).equals(this.getSecretValue());
        }
        return false;
    }
}
