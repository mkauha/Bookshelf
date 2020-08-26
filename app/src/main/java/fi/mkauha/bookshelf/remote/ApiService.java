package fi.mkauha.bookshelf.remote;

import fi.mkauha.bookshelf.network.BookResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    // "lookfor={query}&type=Title&sort=relevance%2Cid%20asc&page=1&limit=20&prettyPrint=false&lng=fi"
    // https://api.finna.fi/api/v1/search?lookfor=Dune&type=Title&field[]=cleanIsbn&field[]=title&field[]=authors&field[]=genres&field[]=year&field[]=images&field[]=summary&field[]=languages&sort=relevance%2Cid%20asc&page=1&limit=20&prettyPrint=false&lng=fi
    @GET("search?")
    public Call<BookResponse> getResults(
            @Query("lookfor") String lookfor,
            @Query("type") String type,
            @Query("filter[]") String[] filters,
            @Query("field[]") String[] fields

    );


}
