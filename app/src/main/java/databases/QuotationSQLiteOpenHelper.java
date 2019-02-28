package databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import quotes.Quotation;

public class QuotationSQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static QuotationSQLiteOpenHelper instance;

    private QuotationSQLiteOpenHelper(Context context){
        super(context,"quotation_database",null,1);
    }

    public static synchronized QuotationSQLiteOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuotationSQLiteOpenHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE quotation_table (id INTEGER PRIMARY KEY AUTOINCREMENT, quote TEXT NOT NULL, author TEXT, UNIQUE(quote));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<Quotation> getQuotationsFromDatabase(){
        List<Quotation> result = new ArrayList<>();
        Quotation quotation;

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query("quotation_table", new String[]{"quote","author"}, null, null,null, null, null);
        while (cursor.moveToNext()) {
            quotation = new Quotation(cursor.getString(0), cursor.getString(1));
            result.add(quotation);
        }
        cursor.close();
        database.close();
        return result;
    }

    public boolean isInDatabase(String quotation) {
        boolean is = false;
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.query("quotation_table", null, "quote=?", new String[]{quotation}, null, null, null, null);
        if (c.getCount() > 0) {
            is = true;
        }
        c.close();
        database.close();
        return is;
    }

    public void addQuotationToDatabase(Quotation quotation) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues quote = new ContentValues();
        quote.put("quote", quotation.getQuoteText());
        quote.put("author", quotation.getQuoteAuthor());
        database.insert("quotation_table", null, quote);

        database.close();
    }

    public void deleteAll() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete("quotation_table", null, null);
        database.close();
    }

    public void deleteQuotationFromDatabase(String quotation) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete("quotation_table", "quote=?", new String[] {quotation});
        database.close();
    }
}
