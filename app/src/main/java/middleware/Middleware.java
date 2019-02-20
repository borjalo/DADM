package middleware;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.quotationshake.R;

import java.util.List;

import quotes.Quotation;

public class Middleware extends ArrayAdapter {
    private int resource;

    public Middleware(Context context, int resource, List<Quotation> quotes) {
        super(context, resource, quotes);
        this.resource = resource;
    }

    private class ViewHolder {
        private TextView tvAuthor;
        private TextView tvQuote;

        public ViewHolder(TextView tvQuote, TextView tvAuthor) {
            this.tvAuthor = tvAuthor;
            this.tvQuote = tvQuote;
        }

        public TextView getAuthor() {
            return tvAuthor;
        }

        public void setAuthor(TextView tvAuthor) {
            this.tvAuthor = tvAuthor;
        }

        public TextView getQuote() {
            return tvQuote;
        }

        public void setQuote(TextView tvQuote) {
            this.tvQuote = tvQuote;
        }
    }

    @Override
    public View getView(int pos, View v, ViewGroup vg) {
        if (v == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            v = (inflater.inflate(this.resource, null));

            ViewHolder viewHolder = new ViewHolder((TextView) v.findViewById(R.id.quote), (TextView) v.findViewById(R.id.author));

            v.setTag(viewHolder);
        }

        ViewHolder vh = (ViewHolder) v.getTag();
        TextView tvAuthor = vh.getAuthor();
        TextView tvQuote = vh.getQuote();

        Quotation quotation = (Quotation) getItem(pos);
        tvAuthor.setText(quotation.getQuoteAuthor());
        tvQuote.setText(quotation.getQuoteText());

        return v;
    }
}