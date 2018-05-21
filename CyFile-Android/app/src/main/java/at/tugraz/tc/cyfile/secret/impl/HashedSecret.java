package at.tugraz.tc.cyfile.secret.impl;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import at.tugraz.tc.cyfile.secret.Secret;

public class HashedSecret implements Secret {
    private final String secretValue;

    public HashedSecret(Secret secret) {
        secretValue = getSHADigest(secret);
    }

    private String getSHADigest(Secret secret) {
        //TODO we should definitely add salt

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }

        md.update(secret.getSecretValue().getBytes());
        byte[] digest = md.digest();
        return Base64.encodeBase64String(digest);
    }

    @Override
    public String getSecretValue() {
        return this.secretValue;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof HashedSecret
                && ((HashedSecret) obj).getSecretValue() == this.getSecretValue();
    }
}
