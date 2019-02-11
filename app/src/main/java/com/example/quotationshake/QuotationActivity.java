package com.example.quotationshake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class QuotationActivity extends AppCompatActivity {
    String username;
    TextView tv_quote;
    TextView tv_author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);
        tv_quote = findViewById(R.id.textView3);
        tv_author = findViewById(R.id.textView2);

        username = "Nameless One";
        String user = tv_quote.getText().toString();
        String newUser = user.replace("%1s", username);
        tv_quote.setText(newUser);
    }

    public void refresh(View view) {
        tv_quote.setText(R.string.quotation);
        tv_author.setText(R.string.author);
    }
}
