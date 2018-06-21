package at.tugraz.tc.cyfile.secret;

import android.content.Context;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import static org.mockito.Mockito.mock;

public class HashSecretRepositoryUnitTest {

    private ByteArrayOutputStream createOutputStream() throws IOException {

        return new ByteArrayOutputStream(2048);
    }

    private InputStream createInputStream(HashedSecret secret) throws IOException {
        ByteArrayOutputStream bas = new ByteArrayOutputStream(2048);
        ObjectOutputStream oos = new ObjectOutputStream(bas);
        oos.writeObject(secret);

        byte[] buffer = bas.toByteArray();
        return new ByteArrayInputStream(buffer);
    }

    @Test
    public void testReadSecret() throws IOException {
        HashedSecret secret = new HashedSecret(() -> "111222", new ApacheCodecBase64());
        Context mockContext = mock(Context.class);

        HashSecretRepository repository =
                Mockito.spy(new HashSecretRepository(mockContext, ""
                        , mock(CyFileLogger.class), new ApacheCodecBase64()));

        Mockito.doReturn(createInputStream(secret)).when(repository).getInputStream();
        repository.init();
        Assert.assertEquals(repository.getSecret(), secret);
    }

    @Test
    public void testWriteSecret() throws IOException, ClassNotFoundException {
        HashedSecret oldSecret = new HashedSecret(() -> "332211", new ApacheCodecBase64());
        Secret newSecret = new PinPatternSecret("111222");
        Context mockContext = mock(Context.class);

        HashSecretRepository repository =
                Mockito.spy(new HashSecretRepository(mockContext, ""
                        , mock(CyFileLogger.class), new ApacheCodecBase64()));

        Mockito.doReturn(createInputStream(oldSecret)).when(repository).getInputStream();
        ByteArrayOutputStream outputStream = createOutputStream();
        Mockito.doReturn(outputStream).when(repository).getOutputStream();
        repository.init();

        repository.saveSecret(newSecret);
        byte[] buffer = outputStream.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(buffer);
        ObjectInputStream ois = new ObjectInputStream(is);
        HashedSecret actual = (HashedSecret) ois.readObject();
        Assert.assertEquals(actual, newSecret);
    }

    @Test
    public void testGetInputStream() throws Exception {
        HashedSecret secret = new HashedSecret(() -> "111222", new ApacheCodecBase64());
        Context mockContext = mock(Context.class);

        HashSecretRepository repository =
                Mockito.spy(new HashSecretRepository(mockContext, ""
                        , mock(CyFileLogger.class), new ApacheCodecBase64()));
        Mockito.doReturn(createInputStream(secret)).when(repository).getInputStream();
        repository.init();

        repository.getInputStream();
    }

    @Test
    public void testGetOutputStream() throws Exception {
        HashedSecret secret = new HashedSecret(() -> "111222", new ApacheCodecBase64());
        Context mockContext = mock(Context.class);

        HashSecretRepository repository =
                Mockito.spy(new HashSecretRepository(mockContext, ""
                        , mock(CyFileLogger.class), new ApacheCodecBase64()));

        OutputStream out = createOutputStream();
        Mockito.doReturn(createInputStream(secret)).when(repository).getInputStream();
        Mockito.doReturn(out).when(repository).getOutputStream();
        repository.init();

        OutputStream result = repository.getOutputStream();

        Assert.assertEquals(result, out);
    }
}
