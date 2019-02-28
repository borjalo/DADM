package tasks;


import android.os.AsyncTask;

import com.example.quotationshake.FavouriteActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import databases.QuotationDB;
import databases.QuotationSQLiteOpenHelper;
import quotes.Quotation;

public class FavouriteAsyncTask extends AsyncTask<Boolean,Void, List<Quotation>> {
    private WeakReference<FavouriteActivity> favouriteActivityWeakReference;

    public FavouriteAsyncTask(FavouriteActivity activity){
        this.favouriteActivityWeakReference = new WeakReference(activity);
    }

    @Override
    protected List<Quotation> doInBackground(Boolean... booleans) {
        List<Quotation> quotations = new ArrayList<>();

        if(favouriteActivityWeakReference != null) {
            QuotationSQLiteOpenHelper helper = QuotationSQLiteOpenHelper.getInstance(favouriteActivityWeakReference.get().getApplicationContext());

            int count = booleans.length;

            for(int i = 0; i < count; i++) {
                if (booleans[i]) {
                    quotations = QuotationDB.getInstance(favouriteActivityWeakReference.get().getApplicationContext()).quotationDAO().getAllQuotation();
                } else {
                    quotations = helper.getInstance(favouriteActivityWeakReference.get().getApplicationContext()).getQuotationsFromDatabase();
                }
            }
        }

        return  quotations;
    }

    @Override
    protected void onPostExecute(List<Quotation> quotations) {
        if (favouriteActivityWeakReference != null) {
            favouriteActivityWeakReference.get().fillAdapter(quotations);
        }
    }
}
