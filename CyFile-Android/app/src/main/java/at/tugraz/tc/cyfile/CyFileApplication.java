package at.tugraz.tc.cyfile;

import android.app.Application;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Context;

import com.blankj.utilcode.util.Utils;

import at.tugraz.tc.cyfile.crypto.CryptoService;
import at.tugraz.tc.cyfile.crypto.DummyKeyVaultService;
import at.tugraz.tc.cyfile.crypto.AESCryptoService;
import at.tugraz.tc.cyfile.crypto.KeyVaultService;
import at.tugraz.tc.cyfile.crypto.NoOpCryptoService;
import at.tugraz.tc.cyfile.crypto.PrefixCryptoService;
import at.tugraz.tc.cyfile.injection.ApplicationComponent;
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent;
import at.tugraz.tc.cyfile.note.NoteModule;
import at.tugraz.tc.cyfile.note.NoteRepository;
import at.tugraz.tc.cyfile.note.impl.FileNoteRepository;
import at.tugraz.tc.cyfile.note.impl.SecureNoteService;
import at.tugraz.tc.cyfile.secret.SecretModule;
import at.tugraz.tc.cyfile.secret.SecretRepository;
import at.tugraz.tc.cyfile.secret.impl.InMemorySecretRepository;
import at.tugraz.tc.cyfile.secret.impl.OnApplicationForegroundSecretPrompter;
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret;
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecretPrompter;
import at.tugraz.tc.cyfile.secret.impl.SecretManagerImpl;
import at.tugraz.tc.cyfile.secret.impl.SimplePinPatternSecretVerifier;

/**
 * Application extended class
 * Created by cobri on 3/21/2018.
 */

public class CyFileApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }

    public static CyFileApplication get(Context context) {
        return (CyFileApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            SecretRepository secretRepository = new InMemorySecretRepository(new PinPatternSecret("111222"));
            OnApplicationForegroundSecretPrompter prompter = new OnApplicationForegroundSecretPrompter(new PinPatternSecretPrompter(this));

            KeyVaultService keyVaultService = new DummyKeyVaultService();
            CryptoService cryptoService = new PrefixCryptoService();

            NoteRepository repository = new FileNoteRepository(this, null);

            //add this prompter to the lifecycle so we which states
            ProcessLifecycleOwner.get().getLifecycle().addObserver(prompter);

            SecretModule secretModule = new SecretModule(
                    new SecretManagerImpl(
                            new SimplePinPatternSecretVerifier(secretRepository),
                            secretRepository),
                    prompter,
                    keyVaultService
            );
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .appModule(new AppModule(this))
                    .noteModule(new NoteModule(new SecureNoteService(repository, cryptoService)))
                    .secretModule(secretModule)
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.mApplicationComponent = applicationComponent;
    }
}
