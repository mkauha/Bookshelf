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

    BottomAppBar bottomAppBar;
    private MaterialToolbar topAppBar;
    FloatingActionButton fab;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomAppBar = (BottomAppBar) findViewById(R.id.bottom_app_bar);

        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_main);
        bottomAppBar.setNavigationIcon(R.drawable.ic_outline_menu_24);
        bottomAppBar.setFabAnimationMode(BottomAppBar.FAB_ANIMATION_MODE_SLIDE);
        // TODO Change interaction in different fragments
        // bottomAppBar.setOnClickListener(view -> openNavigationDrawer());
        bottomAppBar.setNavigationOnClickListener(v ->  openNavigationDrawer());

        topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);
        Log.d(TAG, "topAppBar: " + topAppBar);


        topAppBar.setVisibility(View.GONE);

        fab = findViewById(R.id.fab);
        fab.setImageDrawable(getDrawable(R.drawable.ic_outline_add_24));
        fab.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AddBookModalFragment fragment = new AddBookModalFragment();
            fragmentTransaction.add(fragment, "BottomSheetFragment");
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        });

    }



    public void openNavigationDrawer() {
        Log.d("MainActivity", "bottomAppBar");

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


    public void onClickAdd(View view) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddBookModalFragment fragment = new AddBookModalFragment();
        fragmentTransaction.add(fragment, "BottomSheetFragment");
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

/*            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("Action", "ADD");
            intent.putExtra("ViewModel_Key", "my_books");
            startActivity(intent);*/
    }

    public MaterialToolbar getTopAppBar() {
        return topAppBar;
    }
}
