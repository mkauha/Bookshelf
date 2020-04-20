package fi.mkauha.bookshelf.ui.librariesview;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Service to fetch library and consortium data from API.
 *
 * Uses free Kirkanta API v4 https://api.kirjastot.fi/ to fetch Finnish library data.
 *
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class LibrariesService extends IntentService {

    /**
     * The Consortium id.
     */
    long consortiumId;

    /**
     * Instantiates a new Libraries service.
     *
     * @param name the name
     */
    public LibrariesService(String name) {
        super(name);
    }

    /**
     * Instantiates a new Libraries service.
     */
    public LibrariesService() {
        super("def");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * Fetches data when receiving an intent.
     *
     * Can fetch every Finnish library consortium from https://api.kirjastot.fi/v4/consortium?limit=50
     * Can fetch every library in a single consortium from "https://api.kirjastot.fi/v4/library?consortium=2090&with=schedules&limit=100" by changing consortium ID.
     * Send that data to activities in JSON string format.
     *
     * @param intent the intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent.hasExtra("consortium_list")) {

            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("https://api.kirjastot.fi/v4/consortium?limit=50");
            String url = urlBuilder.toString();
            String data = getJSON(url, 10000);

            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
            Intent intentUpdate = new Intent("LibrariesService");
            intentUpdate.putExtra("consortiumData", data);
            manager.sendBroadcast(intentUpdate);

        }
        if(intent.hasExtra("consortium")) {
            long defId = 2090;
            this.consortiumId = intent.getLongExtra("consortium", defId);

            StringBuilder urlBuilder = new StringBuilder();
            //urlBuilder.append("https://api.kirjastot.fi/v4/library?city.name=");
            urlBuilder.append("https://api.kirjastot.fi/v4/library?consortium=");
            urlBuilder.append(this.consortiumId);
            urlBuilder.append("&with=schedules&limit=100");
            String url = urlBuilder.toString();
            String data = getJSON(url, 10000);
            Log.d("LibrariesService", data);

            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
            Intent intentUpdate = new Intent("LibrariesService");
            intentUpdate.putExtra("libraryData", data);
            manager.sendBroadcast(intentUpdate);
        }
    }

    /**
     * Creates HttpURLConnection to given URL and returns fetched data as String.
     *
     * Sends a GET request to API and reads received data.
     *
     * @param url     the url to fetch data
     * @param timeout the timeout for connection
     * @return fetched data
     */
    public String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();

                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Log.d("getJSON", "" + ex.getMessage());
        } catch (IOException ex) {
            Log.d("getJSON", "" + ex.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Log.d("getJSON", "" + ex.getMessage());
                }
            }
        }
        return null;
    }
}
