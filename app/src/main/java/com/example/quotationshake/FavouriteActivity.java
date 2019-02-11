package com.example.quotationshake;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
    }

    public void searchInfo(View v) {
        String authorName = "Albert Einstein";
        String url = "https://en.wikipedia.org/wiki/Special:Search?search=";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url + authorName));
        startActivity(i);
    }
}
