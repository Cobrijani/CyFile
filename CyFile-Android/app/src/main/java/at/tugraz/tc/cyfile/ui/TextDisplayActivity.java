package at.tugraz.tc.cyfile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import at.tugraz.tc.cyfile.MainActivity;
import at.tugraz.tc.cyfile.R;

public class TextDisplayActivity extends BaseActivity {

    public static final String ABOUT = "ABOUT";
    public static final String HELP = "HELP";

    private static String aboutString;
    private static String helpString;

    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_display);
        aboutString = getString(R.string.about_string);
        helpString = getString(R.string.help_string);
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
    }
}
