package at.tugraz.tc.cyfile.ui;

import android.os.Bundle;
import android.widget.TextView;

import at.tugraz.tc.cyfile.R;

public class TextDisplayActivity extends BaseActivity {

    public static final String ABOUT = "ABOUT";
    public static final String HELP = "HELP";
    public static final String LICENSES = "LICENSES";
    public static final String CONTACT = "CONTACT";

    private static String aboutString;
    private static String helpString;
    private static String licensesString;
    private static String contactString;

    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_display);
        aboutString = getString(R.string.about_string);
        helpString = getString(R.string.help_string);
        licensesString = getString(R.string.licenses_string);
        contactString = getString(R.string.contact_string);
        content = findViewById(R.id.text_content);
        setText();
    }

    private void setText() {
        String content_id = getIntent().getStringExtra(SettingsActivity.CONTENT_ID);
        switch (content_id) {
            case ABOUT:
                content.setText(aboutString);
                break;
            case HELP:
                content.setText(helpString);
                break;
            case LICENSES:
                content.setText(licensesString);
                break;
            case CONTACT:
                content.setText(contactString);
                break;
            default:
                content.setText("");
                break;
        }
    }
}
