package fi.mkauha.bookshelf.ui.librariesview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
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
import fi.mkauha.bookshelf.databinding.FragmentLibrariesBinding;
import fi.mkauha.bookshelf.models.Consortium;
import fi.mkauha.bookshelf.models.Library;
import fi.mkauha.bookshelf.viewmodel.LibrariesViewModel;


public class LibrariesFragment extends Fragment {
    private FragmentLibrariesBinding binding;
    private LibrariesViewModel viewModel;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private SymbolManager symbolManager;

    private List<Library> libraryList = new ArrayList<>();
    private List<Consortium> consortiumList = new ArrayList<>();
    private List<String> consortiumNamesList = new ArrayList<>();
    private List<Symbol> symbols = new ArrayList<>();
    private List<SymbolOptions> options = new ArrayList<>();

    private static final String ICON_ID = "LIBRARY_ICON";

    private Consortium currentConsortium;
    private Gson gson;

    private double cameraLatitude;
    private double cameraLongitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLibrariesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);
        viewModel = new ViewModelProvider(this).get(LibrariesViewModel.class);
        currentConsortium = viewModel.getConsortium().getValue();
        cameraLatitude = viewModel.getLatitude().getValue();
        cameraLongitude = viewModel.getLongitude().getValue();

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(new Style.Builder().fromUri(getString(R.string.mapbox_custom_style)), style -> {
            this.mapboxMap = mapboxMap;
            mapboxMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(cameraLatitude, cameraLongitude), 10
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
            myIntent.putExtra("consortium_list", "");
            myIntent.putExtra("consortium", currentConsortium.getId());
            getActivity().startService(myIntent);
        }

        return root;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        String libraryData;
        String consortiumData;

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("consortiumData")) {
                consortiumData = intent.getStringExtra("consortiumData");
                updateConsortiumList(consortiumData);
            }
            if(intent.hasExtra("libraryData")) {
                libraryData = intent.getStringExtra("libraryData");
                updateLibraryList(libraryData);
            }
        }
    };
    private void updateConsortiumList(String consortiumData) {
        Gson gson = new Gson();
        JsonObject dataJson = gson.fromJson(consortiumData, JsonObject.class);
        JsonArray librariesJson = dataJson.getAsJsonObject().get("items").getAsJsonArray();

        for(JsonElement library : librariesJson) {
            long id = library.getAsJsonObject().get("id").getAsLong();
            String name = library.getAsJsonObject().get("name").getAsString();

            this.consortiumList.add(new Consortium(id, name));
            this.consortiumNamesList.add(name);
        }

    }
    private void updateLibraryList(String libraryData) {
        this.libraryList.clear();

        Gson gson = new Gson();
        JsonObject dataJson = gson.fromJson(libraryData, JsonObject.class);
        JsonArray librariesJson = dataJson.getAsJsonObject().get("items").getAsJsonArray();
        JsonObject coordinatesObj = null;
        JsonObject addressObj = null;
        JsonArray schedulesObj = null;
        long id = -1;
        String name = "";
        String street = "";
        String zip = "";
        boolean isOpen = false;
        boolean isMainLibrary = false;
        double latitude = 0;
        double longitude = 0;

        for(JsonElement library : librariesJson) {
            JsonObject rootObject = library.getAsJsonObject();
            boolean isValid = true;
            JsonElement address = rootObject.get("address");
            JsonElement schedules = rootObject.get("schedules");
            JsonElement coordinates = rootObject.get("coordinates");
            JsonElement mainLibrary = rootObject.get("mainLibrary");

            if(!address.isJsonNull()) {
                addressObj = rootObject.get("address").getAsJsonObject();
            } else {
                isValid = false;
            }

            if(!schedules.isJsonNull()) {
                schedulesObj = rootObject.get("schedules").getAsJsonArray();
            } else {
                isValid = false;
            }

            if(!coordinates.isJsonNull()) {
                coordinatesObj = rootObject.get("coordinates").getAsJsonObject();
            } else {
                isValid = false;
            }

            if(!mainLibrary.isJsonNull()) {
                isMainLibrary = mainLibrary.getAsBoolean();
            }

            if(schedulesObj != null) {
                for(JsonElement key : schedulesObj) {
                    if(key.toString().equals("closed")) {
                        isOpen = key.getAsBoolean();
                    }
                }
            }

            if(isValid) {
                if(!library.getAsJsonObject().get("id").isJsonNull()) {
                    id = rootObject.get("id").getAsLong();
                }
                if(!library.getAsJsonObject().get("name").isJsonNull()) {
                    name = rootObject.get("name").getAsString();
                }
                if(!addressObj.getAsJsonObject().get("street").isJsonNull()) {
                    street = addressObj.getAsJsonObject().get("street").getAsString();
                }
                if(!addressObj.getAsJsonObject().get("zipcode").isJsonNull()) {
                    zip = addressObj.getAsJsonObject().get("zipcode").getAsString();
                }
                if(!coordinatesObj.getAsJsonObject().isJsonNull()) {
                    latitude = coordinatesObj.getAsJsonObject().get("lat").getAsDouble();
                    longitude = coordinatesObj.getAsJsonObject().get("lon").getAsDouble();
                }

                this.libraryList.add(new Library(id, name, "", street, zip, isOpen, "", latitude, longitude, isMainLibrary));
            }

        }
        updateMarkers();
    }

    private void updateMarkers() {
        Activity activity = getActivity();
        if(activity != null) {
            options.clear();
            symbolManager.delete(symbols);
            for (Library lib : libraryList) {
                if(lib.isMainLibrary()) {
                    mapboxMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lib.getLatitude(), lib.getLongitude()), 10
                            )
                    );
                }
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

    private void openChooseConsortiumDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(getResources().getString(R.string.choose_city));
        alertDialogBuilder.setItems(consortiumNamesList.toArray(new CharSequence[consortiumNamesList.size()]), (dialog, which) -> {
            viewModel.setConsortium(consortiumList.get(which));
            currentConsortium = consortiumList.get(which);
            Intent myIntent = new Intent(getActivity(), LibrariesService.class);
            myIntent.putExtra("consortium", currentConsortium.getId());
            getActivity().startService(myIntent);

        });
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.button_cancel),
                (arg0, arg1) -> {

                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void addLibrarySymbolImageToStyle(Style style) {
        style.addImage(ICON_ID, BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_library_point)),true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem m1 = menu.findItem(R.id.change_city);
        m1.setEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.change_city) {
            openChooseConsortiumDialog();
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_main_menu, menu);
        final MenuItem changeCityItem = menu.findItem(R.id.change_city);
        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        final MenuItem addItem = menu.findItem(R.id.add_book);

        searchItem.setVisible(false);
        addItem.setVisible(false);
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