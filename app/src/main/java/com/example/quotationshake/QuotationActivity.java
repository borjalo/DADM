package com.example.quotationshake;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import databases.QuotationDB;
import databases.QuotationSQLiteOpenHelper;
import quotes.Quotation;
import tasks.QuotationAsyncTask;

public class QuotationActivity extends AppCompatActivity {
    String username;
    TextView tv_quote;
    TextView tv_author;

    boolean visible;

    QuotationSQLiteOpenHelper helper = QuotationSQLiteOpenHelper.getInstance(this);
    Quotation quotation;
    Menu m;

    String databaseAccessMethod;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);
        tv_quote = findViewById(R.id.textView3);
        tv_author = findViewById(R.id.textView2);
        visible = false;

        progressBar = findViewById(R.id.progressBar);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (savedInstanceState == null) {
            username = preferences.getString("name", "Nameless One");
            databaseAccessMethod = preferences.getString("list_preference_database", "");
            Log.d("1. method", databaseAccessMethod);
            String user = tv_quote.getText().toString();
            String newUser = user.replace("%1s", username);
            tv_quote.setText(newUser);
        } else {
            tv_quote.setText(savedInstanceState.getString("tv_quote"));
            tv_author.setText(savedInstanceState.getString("tv_author"));

            databaseAccessMethod = savedInstanceState.getString("list_preference_database");
            Log.d("1. method", databaseAccessMethod);
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
                favQuotation(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refresh() {
        QuotationAsyncTask asyncTask = new QuotationAsyncTask(QuotationActivity.this);
        asyncTask.execute();
        /*tv_quote.setText(getResources().getString(R.string.quotation).replace("%1$d", " " + quantityOfQuotes));
        tv_author.setText(getResources().getString(R.string.author).replace("%1$d", " " + quantityOfQuotes));*/

        m.findItem(R.id.favquote).setVisible(true);


    }

    public void favQuotation(MenuItem item) {
        item.setVisible(false);
        quotation = new Quotation(tv_quote.getText().toString(), tv_author.getText().toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (databaseAccessMethod){
                    case "Room":
                        QuotationDB.getInstance(QuotationActivity.this).quotationDAO().addQuotation(quotation);
                        break;
                    case "SQLiteOpenHelper":
                        helper.addQuotationToDatabase(quotation);
                        break;
                }
            }
        }).start();
    }

    public void showProgressBar() {
        tv_author.setVisibility(View.INVISIBLE);
        tv_quote.setVisibility(View.INVISIBLE);

        m.findItem(R.id.favquote).setVisible(false);
        m.findItem(R.id.newquote).setVisible(false);

        progressBar.setVisibility(View.VISIBLE);
    }

    public void quotationGet(Quotation quotation) {
        tv_author.setVisibility(View.VISIBLE);
        tv_author.setText(quotation.getQuoteAuthor());
        tv_quote.setVisibility(View.VISIBLE);
        tv_quote.setText(quotation.getQuoteText());

        m.findItem(R.id.newquote).setVisible(true);

        progressBar.setVisibility(View.INVISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                switch(databaseAccessMethod){
                    case "Room":
                        visible = QuotationDB.getInstance(QuotationActivity.this).quotationDAO().getQuotation(tv_quote.getText().toString()) == null;
                        break;
                    case "SQLiteOpenHelper":
                        visible = !helper.isInDatabase(tv_quote.getText().toString());
                        break;
                }
            }
        }).start();


    }

    /*@Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("tv_quote", tv_quote.getText().toString());
        savedInstanceState.putString("tv_author", tv_author.getText().toString());
        savedInstanceState.putInt("quantity", quantityOfQuotes);
        savedInstanceState.putBoolean("visible",visible);

        savedInstanceState.putString("list_preference_database", databaseAccessMethod);

    }*/
}
