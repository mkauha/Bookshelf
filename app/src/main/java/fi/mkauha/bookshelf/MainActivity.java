package fi.mkauha.bookshelf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import fi.mkauha.bookshelf.ui.bottomnav.BottomNavigationFragment;
import fi.mkauha.bookshelf.ui.details.DetailsActivity;

/**
 * MainActivity that initializes Navigation UI with bottom navigation bar and three fragments.
 *
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomAppBar bottomAppBar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bottomAppBar);
        FragmentManager fragmentManager = getSupportFragmentManager();

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "bottomAppBar");
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                BottomNavigationFragment fragment = new BottomNavigationFragment();
                fragmentTransaction.add(fragment, "BottomSheetFragment");
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bottom_main_menu, menu);
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
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("Action", "ADD");
            intent.putExtra("ViewModel_Key", "my_books");
            startActivity(intent);
    }

}
