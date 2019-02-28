package databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import quotes.Quotation;

@Database(version = 1, entities = {Quotation.class})
public abstract class QuotationDB extends RoomDatabase {
    public abstract QuotationDAO quotationDAO();

    private static QuotationDB quotationDB;

    public static synchronized QuotationDB getInstance(Context context) {
        if (quotationDB == null) {
            quotationDB = Room
                    .databaseBuilder(context, QuotationDB.class, "quotation_database")
                    .build();
        }
        return quotationDB;
    }
}
