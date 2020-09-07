package fi.mkauha.bookshelf.data.remote;

import fi.mkauha.bookshelf.data.remote.model.BookResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    // "lookfor={query}&type=Title&sort=relevance%2Cid%20asc&page=1&limit=20&prettyPrint=false&lng=fi"
    // https://api.finna.fi/api/v1/search?lookfor=Dune&type=Title&field[]=cleanIsbn&field[]=title&field[]=authors&field[]=genres&field[]=year&field[]=images&field[]=summary&field[]=languages&sort=relevance%2Cid%20asc&page=1&limit=20&prettyPrint=false&lng=fi
    @GET("search?")
    public Call<BookResponse> getAllRecords (
            @Query("lookfor") String lookfor,
            @Query("type") String type,
            @Query("filter[]") String[] filters,
            @Query("field[]") String[] fields
    );

    // https://api.finna.fi/api/v1/record?id=satakirjastot.398814&prettyPrint=false&lng=fi
    @GET("record?")
    public Call<BookResponse> getRecordById (
            @Query("id") String id,
            @Query("filter[]") String[] filters,
            @Query("field[]") String[] fields
    );


}
