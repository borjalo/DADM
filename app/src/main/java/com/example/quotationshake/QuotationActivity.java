package com.example.quotationshake;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class QuotationActivity extends AppCompatActivity {
    String username;
    TextView tv_quote;
    TextView tv_author;

    boolean visible;
    int quantityOfQuotes = 0;

    Menu m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);
        tv_quote = findViewById(R.id.textView3);
        tv_author = findViewById(R.id.textView2);
        visible = false;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (savedInstanceState == null) {
            username = preferences.getString("name", "Nameless One");
            String user = tv_quote.getText().toString();
            String newUser = user.replace("%1s", username);
            tv_quote.setText(newUser);
        } else {
            tv_quote.setText(savedInstanceState.getString("tv_quote"));
            tv_author.setText(savedInstanceState.getString("tv_author"));

            quantityOfQuotes = savedInstanceState.getInt("quantityOfQuotes");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principalmenu, menu);
        menu.findItem(R.id.favquote).setVisible(visible);
        m = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newquote:
                refresh();
                return super.onOptionsItemSelected(item);
            case R.id.favquote:
                visible = false;
                item.setVisible(visible);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refresh() {
        tv_quote.setText(getResources().getString(R.string.quotation).replace("%1$d", " " + quantityOfQuotes));
        tv_author.setText(getResources().getString(R.string.author).replace("%1$d", " " + quantityOfQuotes));
        quantityOfQuotes++;
        Log.d("quote", tv_quote.getText().toString());
        Log.d("author", tv_author.getText().toString());
        Log.d("cantidad", " " + quantityOfQuotes);
        visible = true;
        m.findItem(R.id.favquote).setVisible(visible);
    }
}
