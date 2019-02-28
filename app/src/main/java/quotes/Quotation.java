package quotes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "quotation_table", indices = {@Index(value = {"quote"}, unique = true)})
public class Quotation {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="quote")
    @NonNull
    private String quoteText;

    @ColumnInfo(name="author")
    private String quoteAuthor;

    public Quotation() {}

    public Quotation(String quoteText, String quoteAuthor) {
        this.quoteText = quoteText;
        this.quoteAuthor = quoteAuthor;
    }

    public void setQuoteAuthor(String quoteAuthor) {
        this.quoteAuthor = quoteAuthor;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public String getQuoteAuthor() {
        return quoteAuthor;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
