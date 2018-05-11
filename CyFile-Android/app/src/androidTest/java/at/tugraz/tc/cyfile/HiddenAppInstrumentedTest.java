package at.tugraz.tc.cyfile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.v4.content.LocalBroadcastManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.Executor;

import at.tugraz.tc.cyfile.async.AsyncModule;
import at.tugraz.tc.cyfile.crypto.KeyVaultService;
import at.tugraz.tc.cyfile.injection.ApplicationComponent;
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent;
import at.tugraz.tc.cyfile.note.NoteModule;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.secret.AppHidingModule;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.SecretModule;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.secret.impl.DefaultAppHidingModule;
import at.tugraz.tc.cyfile.ui.CallReceiverActivity;
import at.tugraz.tc.cyfile.ui.ListNoteActivity;

import static android.content.Intent.ACTION_NEW_OUTGOING_CALL;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class HiddenAppInstrumentedTest extends BaseInstrumentedTest {
    @Mock
    private NoteService noteService;

    @Mock
    private SecretManager secretManager;

    @Mock
    private KeyVaultService keyVaultService;

    @Mock
    private SecretPrompter secretPrompter;

    private AppHidingModule appHidingModule;

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class, true, false);

    @Rule
    public IntentsTestRule<ListNoteActivity> listActivityRule =
            new IntentsTestRule<>(ListNoteActivity.class, true, false);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.CALL_PHONE);

    @Test
    public void callNumberToLaunchApp() {
        //TODO fix tests
        CallReceiverActivity cra = spy(new CallReceiverActivity());
        LocalBroadcastManager.getInstance(app.getApplicationContext())
                .registerReceiver(cra, new IntentFilter(ACTION_NEW_OUTGOING_CALL));
        Intents.init();
        String phoneNumber = "12";
        Intent intent = new Intent(Intent.ACTION_NEW_OUTGOING_CALL,
                Uri.parse("tel:" + phoneNumber));
        cra.onReceive(app.getApplicationContext(), intent);
        app.getApplicationContext().startActivity(intent);
        intended(hasComponent(ListNoteActivity.class.getName()));
    }

    @Test
    public void preventNormalLaunchOfMainActivity() {
        hideApp();
        try {
            mActivityRule.launchActivity(new Intent());
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(), "Could not launch activity");
            return;
        }
        fail();
    }

    private void hideApp() {
        Context context = app.getApplicationContext();
        appHidingModule.hideApp(context);
    }

    @Test
    public void reenableNormalLaunchOfMainActivity() {
        Context context = app.getApplicationContext();
        //we hide it first to make sure reenabling works
        hideApp();
        appHidingModule.displayApp(context);
        mActivityRule.launchActivity(new Intent());
    }

    @Before
    public void init() {
        appHidingModule = new DefaultAppHidingModule();
        ApplicationComponent applicationComponent
                = DaggerApplicationComponent.builder()
                .noteModule(new NoteModule(noteService))
                .asyncModule(new AsyncModule(mock(Executor.class)))
                .secretModule(new SecretModule(secretManager, secretPrompter, keyVaultService, appHidingModule))
                .appModule(mock(AppModule.class))
                .build();

        app.setComponent(applicationComponent);

        when(secretManager.verify(any()))
                .thenReturn(true);
    }

}