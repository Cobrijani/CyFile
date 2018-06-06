package at.tugraz.tc.cyfile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import at.tugraz.tc.cyfile.MainActivity;
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

    private void setText()
    {
        String content_id = getIntent().getStringExtra(SettingsActivity.CONTENT_ID);
        if (content_id.equals(ABOUT)) {
            content.setText(aboutString);
        }
        else if(content_id.equals(HELP))
        {
            content.setText(helpString);
        }
        else if(content_id.equals(LICENSES))
        {
            content.setText(licensesString);
        }
        else if(content_id.equals(CONTACT))
        {
            content.setText(contactString);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(TextDisplayActivity.this, SettingsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

}
