package tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.quotationshake.QuotationActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.example.quotationshake.R;
import com.google.gson.Gson;

import quotes.Quotation;

public class QuotationAsyncTask extends AsyncTask<String, Void, Quotation> {



    private WeakReference<QuotationActivity> quotationActivityWeakReference;

    public QuotationAsyncTask(QuotationActivity quotationActivity) {
        quotationActivityWeakReference = new WeakReference<>(quotationActivity);
    }

    @Override
    protected Quotation doInBackground(String... strings) {
        Quotation quotation = null;
        String postParameters = "";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("api.forismatic.com");
        builder.appendPath("api");
        builder.appendPath("1.0");
        builder.appendPath("");
        Log.d("LANGUAGE", strings[0]);

        String language = quotationActivityWeakReference.get().getResources().getString(R.string.english);
        String post = quotationActivityWeakReference.get().getResources().getString(R.string.post_text);
        String get = quotationActivityWeakReference.get().getResources().getString(R.string.get_text);

        if (strings[1].equals(get)) {
            builder.appendQueryParameter("method", "getQuote");
            builder.appendQueryParameter("format", "json");
            if(strings[0].equals(language)) {
                builder.appendQueryParameter("lang", "en");
            } else {
                builder.appendQueryParameter("lang", "ru");
            }
        } else {
            postParameters = "method=getQuote&format=json&lang=";

            if (strings[0].equals(language)) {
                postParameters += "en";
            } else postParameters += "ru";
        }


        try {
            URL url = new URL(builder.build().toString());
            Log.d("URL", url.toString());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(strings[1]);
            connection.setDoInput(true);

            if(strings[1].equals(post)){
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(postParameters);
                writer.flush();
                writer.close();
            }
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                Gson gson = new Gson();
                quotation = gson.fromJson(bufferedReader,Quotation.class);
                bufferedReader.close();
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return quotation;
    }

    @Override
    protected void onPreExecute() {
        if (quotationActivityWeakReference != null) {
            quotationActivityWeakReference.get().showProgressBar();
        }
    }

    @Override
    protected void onPostExecute(Quotation quotation) {
        if(quotationActivityWeakReference != null){
            quotationActivityWeakReference.get().quotationGet(quotation);
        }
    }
}
