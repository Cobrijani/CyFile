package at.tugraz.tc.cyfile.secret.impl;

import android.content.Context;

import org.apache.commons.codec.binary.Base64;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import at.tugraz.tc.cyfile.logging.CyFileLogger;
import at.tugraz.tc.cyfile.secret.Secret;
import at.tugraz.tc.cyfile.secret.SecretRepository;
import at.tugraz.tc.cyfile.secret.SecretVerifier;

public class HashPinPatternSecretVerifier implements SecretVerifier {
    private final SecretRepository repository;

    public HashPinPatternSecretVerifier(SecretRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean verify(Secret secret) {
        return repository.getSecret().equals(secret);
    }
}
