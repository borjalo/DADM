package com.example.quotationshake;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import databases.QuotationDB;
import databases.QuotationSQLiteOpenHelper;
import middleware.Middleware;
import quotes.Quotation;
import tasks.FavouriteAsyncTask;

public class FavouriteActivity extends AppCompatActivity {

    ListView lv;
    Middleware quotationArrayAdapter;

    QuotationSQLiteOpenHelper helper = QuotationSQLiteOpenHelper.getInstance(this);
    String databaseAccessMethod;
    Quotation quotation;
    List<Quotation> quotations;
    boolean databaseMethodAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        databaseAccessMethod = preferences.getString("list_preference_database","Room");
        quotations = new ArrayList<Quotation>();

        /*switch (databaseAccessMethod) {
            case "Room":
                quotationArrayAdapter = new Middleware(this, R.layout.quotation_list_row, QuotationDB.getInstance(FavouriteActivity.this).quotationDAO().getAllQuotation());
                break;
            case "SQLiteOpenHelper":
                quotationArrayAdapter = new Middleware(this, R.layout.quotation_list_row, helper.getQuotationsFromDatabase());
                break;
        }*/

        lv = findViewById(R.id.favouriteList);
        lv.setAdapter(quotationArrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Quotation quote = (Quotation) adapterView.getItemAtPosition(i);
                String author = quote.getQuoteAuthor();
                if (author == "" || author == null) {
                    Toast errorToast = Toast.makeText(FavouriteActivity.this, "Error al intentar obtener la información del autor", Toast.LENGTH_LONG);
                    errorToast.show();
                } else {
                    searchInfo(view, author);
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int i, long l) {
                AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(FavouriteActivity.this);
                confirmationDialog.setMessage(R.string.dialog_message);
                confirmationDialog.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int pos) {
                        quotation = (Quotation) quotationArrayAdapter.getItem(i);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                switch (databaseAccessMethod){
                                    case "Room":
                                        QuotationDB.getInstance(FavouriteActivity.this).quotationDAO().deleteQuotation(quotation);
                                        break;
                                    case "SQLiteOpenHelper":
                                        helper.deleteQuotationFromDatabase(quotation.getQuoteText());
                                        break;
                                }
                            }
                        }).start();

                        quotationArrayAdapter.remove(quotation);
                    }
                });

                confirmationDialog.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                confirmationDialog.create().show();
                return true;
            }
        });

        FavouriteAsyncTask favouriteAsyncTask = new FavouriteAsyncTask(this);
        favouriteAsyncTask.execute(databaseMethodAsync);
    }

    public void fillAdapter(List<Quotation> quotations){
        switch (databaseAccessMethod){
            case "Room":
                quotationArrayAdapter = new Middleware(this, R.layout.quotation_list_row, quotations);
                break;
            case "SQLiteOpenHelper":
                quotationArrayAdapter = new Middleware(this, R.layout.quotation_list_row, quotations);
                break;
        }

        lv.setAdapter(quotationArrayAdapter);
    }

    public void searchInfo(View v, String author) {
        String url = "https://en.wikipedia.org/wiki/Special:Search?search=";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url + author));
        startActivity(i);
    }

    public ArrayList<Quotation> getMockQuotations() {
        ArrayList<Quotation> mockListQuotation = new ArrayList<>();
        Quotation mockQuotation = new Quotation("If you want to impress someone put him on your black list", "Johnnie Walker");
        for (int i = 0; i < 16; i++) {
            mockListQuotation.add(mockQuotation);
        }

        Quotation nullQuotation = new Quotation("This is a null quotation", "");
        mockListQuotation.add(nullQuotation);

        return mockListQuotation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menufavourites, menu);
        if(quotationArrayAdapter.isEmpty()) menu.findItem(R.id.deleteAll).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAll:
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(FavouriteActivity.this);
                deleteDialog.setMessage(R.string.clearalldialog);

                deleteDialog.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        quotationArrayAdapter.clear();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                switch (databaseAccessMethod){
                                    case "Room":
                                        QuotationDB.getInstance(FavouriteActivity.this).quotationDAO().deleteAllQuotations();
                                        break;
                                    case "SQLiteOpenHelper":
                                        helper.deleteAll();
                                        break;
                                }
                            }
                        }).start();


                        supportInvalidateOptionsMenu();
                    }
                });

                deleteDialog.setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                deleteDialog.create().show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
