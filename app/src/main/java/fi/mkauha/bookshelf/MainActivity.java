package fi.mkauha.bookshelf;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fi.mkauha.bookshelf.databinding.ActivityMainBinding;
import fi.mkauha.bookshelf.databinding.FragmentSearchBinding;
import fi.mkauha.bookshelf.ui.modals.MainNavigationModalFragment;
import fi.mkauha.bookshelf.ui.modals.AddBookModalFragment;

/**
 * MainActivity that initializes Navigation UI with bottom navigation bar and three fragments.
 *
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    ActivityMainBinding binding;
    BottomAppBar bottomAppBar;
    private MaterialToolbar topAppBar;
    FloatingActionButton fab;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        bottomAppBar = binding.bottomAppBar;
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_main);
        bottomAppBar.setNavigationIcon(R.drawable.ic_outline_menu_24);

        // TODO Change interaction in different fragments
        // bottomAppBar.setOnClickListener(view -> openNavigationDrawer());
        bottomAppBar.setNavigationOnClickListener(v ->  openNavigationDrawer());
        bottomAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                    navController.navigate(R.id.navigation_search);
                    break;
                case R.id.navigation_collection:
                    Log.d(TAG, "Collection");
                    break;
            }
            return false;
        });

        topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);

        topAppBar.setVisibility(View.GONE);

        fab = findViewById(R.id.fab);
        fab.setImageDrawable(getDrawable(R.drawable.ic_outline_add_24));
        fab.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_add_book);
        });

    }



    public void openNavigationDrawer() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainNavigationModalFragment fragment = new MainNavigationModalFragment();
        fragmentTransaction.add(fragment, "BottomSheetFragment");
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_bottom_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MainActivity", "onOptionsItemSelected " + item);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.onNavDestinationSelected(item, navController);

        return true;
    }

}
