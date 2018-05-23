package at.tugraz.tc.cyfile.secret.impl;


import android.util.Base64;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import at.tugraz.tc.cyfile.secret.Secret;

public class HashedSecret extends AbstractSecret implements Serializable {
    private final String secretValue;
    private final String pepper;

    public static final int PEPPER_LENGTH = 8;
    private static final String SALT = "F52FBD32B2B3B86FF88EF";

    public HashedSecret(Secret secret) {
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
        return encodeBase64(bytes);
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
        return encodeBase64(digest);
    }

    public String encodeBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
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
