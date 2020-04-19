package fi.mkauha.bookshelf.ui.librariesview;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LibrariesService extends IntentService {

    String city;
    URL url;
    HttpURLConnection urlConnection;

    public LibrariesService(String name) {
        super(name);
    }

    public LibrariesService() {
        super("def");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent.hasExtra("city")) {
            this.city = (String) intent.getStringExtra("city");

            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("https://api.kirjastot.fi/v4/library?city.name=");
            urlBuilder.append(this.city);
            urlBuilder.append("&with=schedules&limit=23");
            String url = urlBuilder.toString();
            String data = getJSON(url, 10000);
            Log.d("LibrariesService", "data: " + data);

            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
            Intent intentUpdate = new Intent("LibrariesService");
            intentUpdate.putExtra("libraryData", data);
            manager.sendBroadcast(intentUpdate);

        }
    }
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
