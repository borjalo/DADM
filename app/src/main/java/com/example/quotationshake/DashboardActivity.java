package com.example.quotationshake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void launchActivity(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.getQuotationsButton:
                intent = new Intent(DashboardActivity.this, QuotationActivity.class);
                startActivity(intent);
                break;
            case R.id.favouriteQuotationsButton:
                intent = new Intent(DashboardActivity.this, FavouriteActivity.class);
                startActivity(intent);
                break;
            case R.id.settingsButton:
                intent = new Intent(DashboardActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.aboutButton:
                intent = new Intent(DashboardActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
        }
    }
}
