package at.tugraz.tc.cyfile;


import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Executor;

import at.tugraz.tc.cyfile.async.AsyncModule;
import at.tugraz.tc.cyfile.crypto.KeyVaultService;
import at.tugraz.tc.cyfile.hiding.HidingComponent;
import at.tugraz.tc.cyfile.hiding.HidingModule;
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent;
import at.tugraz.tc.cyfile.logging.CyFileLogger;
import at.tugraz.tc.cyfile.logging.impl.NoOpLogger;
import at.tugraz.tc.cyfile.note.NoteModule;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.note.impl.FileNoteRepository;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.SecretModule;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.settings.SettingsModule;
import at.tugraz.tc.cyfile.settings.UserSettings;
import at.tugraz.tc.cyfile.settings.UserSettingsComponent;
import at.tugraz.tc.cyfile.settings.impl.UserSettingsComponentImpl;
import at.tugraz.tc.cyfile.ui.CallReceiver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FileNoteRepository}
 * Created by mmalte on 25.04.18.
 */

@RunWith(MockitoJUnitRunner.class)
public class CallReceiverUnitTest extends BaseInstrumentedTest {

    private UserSettings userSettings = new UserSettings(false, "");

    @Before
    public void init() {
        UserSettingsComponent userSettingsComponent = spy(new UserSettingsComponentImpl(app));
        when(userSettingsComponent.getUserSettings()).thenReturn(userSettings);

        app = (CyFileApplication)
                InstrumentationRegistry
                        .getInstrumentation()
                        .getTargetContext()
                        .getApplicationContext();
        app.setComponent(DaggerApplicationComponent.builder()
                .appModule(new AppModule(app, new NoOpLogger()))
                .noteModule(new NoteModule(mock(NoteService.class)))
                .asyncModule(new AsyncModule(mock(Executor.class)))
                .hidingModule(new HidingModule(mock(HidingComponent.class)))
                .settingsModule(new SettingsModule(userSettingsComponent))
                .secretModule(new SecretModule(mock(SecretManager.class), mock(SecretPrompter.class), mock(KeyVaultService.class))).build());

    }

    @Test
    public void testStealthModeEnabledCorrectNumber() {
        String number = "1234";
        userSettings.setMagicPhoneNumber(number);
        userSettings.setStealthMode(true);
        CallReceiver receiver = new CallReceiver();
        Intent intent = new Intent("android.intent.action.NEW_OUTGOING_CALL");
        intent.putExtra("android.intent.extra.PHONE_NUMBER", number);
        receiver.onReceive(app, intent);

    }
}


