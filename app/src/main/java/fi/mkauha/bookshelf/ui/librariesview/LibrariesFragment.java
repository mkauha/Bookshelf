package fi.mkauha.bookshelf.ui.librariesview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
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

import static com.mapbox.geojson.FeatureCollection.fromFeatures;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class LibrariesFragment extends Fragment {
    FragmentLibrariesBinding binding;
    DialogLibraryInfoBinding dialogBinding;
    private MapView mapView;
    Location selectedLocation;

    List<Library> libraryList = new ArrayList<>();
    private List<Symbol> symbols = new ArrayList<>();

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


        List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(23.8491393, 	61.450702)));
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(23.7340802, 61.4704433)));
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(23.872232, 	61.4702285)));

        initializeLibraryList();

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(new Style.Builder().fromUri(getString(R.string.mapbox_custom_style)), style -> {
            mapboxMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(initialLatitude, initialLongitude), 10
                )
            );

            addLibrarySymbolImageToStyle(style);

            SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);
            symbolManager.addClickListener(symbol -> {
                openLibraryInfoDialog(symbol.getData());
                Log.d("LibrariesFragment", "symbol:  " + symbol);
            });

            symbolManager.setIconAllowOverlap(true);
            symbolManager.setIconIgnorePlacement(true);

            // Add symbol at specified lat/lon
            List<SymbolOptions> options = new ArrayList<>();
            for(Library lib : libraryList) {
                JsonObject dataJson = gson.fromJson(gson.toJson(lib), JsonObject.class);
                options.add( new SymbolOptions()
                        .withLatLng(new LatLng(lib.getLatitude(),lib.getLongitude()))
                        .withIconImage(ICON_ID)
                        .withIconColor(ColorUtils.colorToRgbaString(getResources().getColor(R.color.colorPrimary)))
                        .withData(dataJson)
                        .withIconSize(1.1f)
                        .withTextField(lib.getName())
                        .withTextSize(10f)
                        .withTextOffset(new Float[] {0f, 2.1f})
                );
            }

           symbols = symbolManager.create(options);

            UiSettings uiSettings = mapboxMap.getUiSettings();
            uiSettings.setCompassEnabled(false);
            uiSettings.setRotateGesturesEnabled(false);
            uiSettings.setLogoGravity(Gravity.TOP);
            uiSettings.setAttributionGravity(Gravity.TOP);
        }));

        return root;
    }

    public void initializeLibraryList() {
        this.libraryList.add(new Library(1, "Hervannan kirjasto", "Piki", "Insinöörinkatu 38","33720", false, "", 61.450702, 23.8491393));
        this.libraryList.add(new Library(2, 	"Härmälän kirjasto", "Piki", "Nuolialantie 47", "33900", false, "", 61.4704433, 23.7340802));
        this.libraryList.add(new Library(3, "Kaukajärven kirjasto", "Piki", "Käätykatu 6","33710", false, "", 61.4702285, 23.872232));
        this.libraryList.add(new Library(4, "Koivistonkylän kirjasto", "Piki", "Lehvänkatu 9","33820", false, "", 61.470319, 	23.796789));
    }

    public void openLibraryInfoDialog(JsonElement data) {
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
        } else {
            openView.setText(getString(R.string.library_closed));
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