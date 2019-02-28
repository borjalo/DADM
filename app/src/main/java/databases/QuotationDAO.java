package databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import quotes.Quotation;

@Dao
public interface QuotationDAO {

    @Insert
    void addQuotation(Quotation quotation);

    @Delete
    void deleteQuotation(Quotation quotation);

    @Query("SELECT * FROM quotation_table")
    List<Quotation> getAllQuotation();

    @Query("SELECT * FROM quotation_table WHERE quote = :quotationText")
    Quotation getQuotation(String quotationText);

    @Query("DELETE FROM quotation_table")
    void deleteAllQuotations();
}
