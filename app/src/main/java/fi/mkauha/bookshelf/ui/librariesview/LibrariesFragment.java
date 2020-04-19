package fi.mkauha.bookshelf.ui.librariesview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.mapboxsdk.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.DialogLibraryInfoBinding;
import fi.mkauha.bookshelf.databinding.FragmentLibrariesBinding;
import fi.mkauha.bookshelf.models.Library;


public class LibrariesFragment extends Fragment {
    FragmentLibrariesBinding binding;
    DialogLibraryInfoBinding dialogBinding;
    private MapView mapView;
    Location selectedLocation;
    SymbolManager symbolManager;


    List<Library> libraryList = new ArrayList<>();
    private List<Symbol> symbols = new ArrayList<>();
    List<SymbolOptions> options = new ArrayList<>();

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";

    double initialLongitude = 23.8491393;
    double initialLatitude = 61.450702;

    private Gson gson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLibrariesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(new Style.Builder().fromUri(getString(R.string.mapbox_custom_style)), style -> {
            mapboxMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(initialLatitude, initialLongitude), 10
                )
            );

            addLibrarySymbolImageToStyle(style);

            symbolManager = new SymbolManager(mapView, mapboxMap, style);
            symbolManager.addClickListener(symbol -> {
                if(symbol.getData() != null) {
                    openLibraryInfoDialog(symbol.getData());
                }
            });

            symbolManager.setIconAllowOverlap(true);
            symbolManager.setIconIgnorePlacement(true);

            UiSettings uiSettings = mapboxMap.getUiSettings();
            uiSettings.setCompassEnabled(false);
            uiSettings.setRotateGesturesEnabled(false);
            uiSettings.setLogoGravity(Gravity.TOP);
            uiSettings.setAttributionGravity(Gravity.TOP);
        }));

        Activity activity = getActivity();
        if(activity != null) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("LibrariesService"));

            Intent myIntent = new Intent(getActivity(), LibrariesService.class);
            myIntent.putExtra("city", "tampere");
            getActivity().startService(myIntent);
        }

        return root;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        String libraryData;

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("libraryData")) {
                libraryData = intent.getStringExtra("libraryData");
                updateLibraryList(libraryData);
            }
        }
    };

    private void updateLibraryList(String libraryData) {
        Gson gson = new Gson();
        JsonObject dataJson = gson.fromJson(libraryData, JsonObject.class);
        JsonArray librariesJson = dataJson.getAsJsonObject().get("items").getAsJsonArray();
        JsonObject coordinatesObj = null;
        JsonObject addressObj = null;
        JsonArray schedulesObj = null;
        boolean isOpen = false;

        for(JsonElement library : librariesJson) {
            JsonElement address = library.getAsJsonObject().get("address");
            JsonElement schedules = library.getAsJsonObject().get("schedules");
            JsonElement coordinates = library.getAsJsonObject().get("coordinates");

            if(!address.isJsonNull()) {
                addressObj = library.getAsJsonObject().get("address").getAsJsonObject();
            }
            if(!schedules.isJsonNull()) {
                schedulesObj = library.getAsJsonObject().get("schedules").getAsJsonArray();
            }
            if(!coordinates.isJsonNull()) {
                coordinatesObj = library.getAsJsonObject().get("coordinates").getAsJsonObject();
            }

            if(schedulesObj != null) {
                for(JsonElement key : schedulesObj) {
                    if(key.toString().equals("closed")) {
                        isOpen = key.getAsBoolean();
                    }
                }
            }

            long id = library.getAsJsonObject().get("id").getAsLong();
            String name = library.getAsJsonObject().get("name").getAsString();
            String street = addressObj.getAsJsonObject().get("street").getAsString();
            String zip = addressObj.getAsJsonObject().get("zipcode").getAsString();

            double latitude = coordinatesObj.getAsJsonObject().get("lat").getAsDouble();
            double longitude = coordinatesObj.getAsJsonObject().get("lon").getAsDouble();

            this.libraryList.add(new Library(id, name, "", street, zip, isOpen, "", latitude, longitude));
        }
        updateMarkers();
    }

    private void updateMarkers() {
        Activity activity = getActivity();
        if(activity != null) {
            for (Library lib : libraryList) {
                JsonObject dataJson = gson.fromJson(gson.toJson(lib), JsonObject.class);
                options.add(new SymbolOptions()
                        .withLatLng(new LatLng(lib.getLatitude(), lib.getLongitude()))
                        .withIconImage(ICON_ID)
                        .withIconColor(ColorUtils.colorToRgbaString(getResources().getColor(R.color.colorPrimary)))
                        .withData(dataJson)
                        .withIconSize(1.1f)
                        .withTextField(lib.getName())
                        .withTextSize(10f)
                        .withTextOffset(new Float[]{0f, 2.1f})
                );
            }
            symbols = symbolManager.create(options);
        }
    }


    private void openLibraryInfoDialog(JsonElement data) {
        Log.d("LibrariesFragment", "" + data);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater factory = LayoutInflater.from(getActivity());
        View dialogLayout = factory.inflate(R.layout.dialog_library_info, null);
        alertDialogBuilder.setView(dialogLayout);
        alertDialogBuilder.setTitle(data.getAsJsonObject().get("name").getAsString());
        alertDialogBuilder.setPositiveButton("OK",
                (arg0, arg1) -> {

                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        String fullAddress= data.getAsJsonObject().get("address").getAsString() + ", " + data.getAsJsonObject().get("zip").getAsString();
        boolean isOpen = data.getAsJsonObject().get("isOpen").getAsBoolean();
        TextView addressView = dialogLayout.findViewById(R.id.dialog_library_address);
        TextView openView = dialogLayout.findViewById(R.id.dialog_library_open);
        addressView.setText(fullAddress);

        if(isOpen) {
            openView.setText(getString(R.string.library_open));
            openView.setTextColor(Color.GREEN);
        } else {
            openView.setText(getString(R.string.library_closed));
            openView.setTextColor(Color.RED);
        }

        alertDialog.show();
    }

    private void addLibrarySymbolImageToStyle(Style style) {
        style.addImage(ICON_ID, BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_library_point)),true);
    }



    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

}