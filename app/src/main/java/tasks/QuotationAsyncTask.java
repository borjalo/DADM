package tasks;

import android.os.AsyncTask;

import com.example.quotationshake.QuotationActivity;

import java.lang.ref.WeakReference;

import quotes.Quotation;

public class QuotationAsyncTask extends AsyncTask<Void, Void, Quotation> {

    private WeakReference<QuotationActivity> quotationActivityWeakReference;

    public QuotationAsyncTask(QuotationActivity quotationActivity) {
        quotationActivityWeakReference = new WeakReference<>(quotationActivity);
    }

    @Override
    protected Quotation doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        if (quotationActivityWeakReference != null) {
            quotationActivityWeakReference.get().showProgressBar();
        }
    }

    @Override
    protected void onPostExecute(Quotation quotation) {
        quotation = new Quotation("Work smarter, not harder", "Borjita");

        if(quotationActivityWeakReference != null){
            quotationActivityWeakReference.get().quotationGet(quotation);
        }
    }
}
