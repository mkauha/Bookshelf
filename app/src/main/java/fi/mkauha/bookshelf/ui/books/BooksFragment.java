package fi.mkauha.bookshelf.ui.books;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayoutMediator;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentBooksBinding;
import fi.mkauha.bookshelf.ui.adapter.BookCollectionPagerAdapter;
import fi.mkauha.bookshelf.ui.adapter.BookListGridAdapter;
import fi.mkauha.bookshelf.ui.modals.AddBookModalFragment;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

public class BooksFragment extends Fragment  {
    private FragmentBooksBinding binding;
    private BooksViewModel booksViewModel;
    private BookListGridAdapter mAdapter;
    FloatingActionButton fab;
    BottomAppBar bottomAppBar;
    private MaterialToolbar topAppBar;
    private ViewPager2 viewPager;
    private BookCollectionPagerAdapter bookCollectionPagerAdapter;

    // get titles from local storage
    private String[] titles = new String[]{"All Books", "Wishlist", "Study"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("BooksFragment", "onCreateView " + this);

        binding = FragmentBooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        fab = getActivity().findViewById(R.id.fab);
        fab.show();
        fab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_outline_add_24));
        fab.setOnClickListener(view -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AddBookModalFragment fragment = new AddBookModalFragment();
            fragmentTransaction.add(fragment, "BottomSheetFragment");
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        });

        topAppBar = (MaterialToolbar) requireActivity().findViewById(R.id.topAppBar);
        topAppBar.setVisibility(View.GONE);
        topAppBar.setTitle("");
        topAppBar.setNavigationIcon(null);



        bottomAppBar = (BottomAppBar) getActivity().findViewById(R.id.bottom_app_bar);
        bottomAppBar.setHideOnScroll(true);
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_main);
        bottomAppBar.setNavigationIcon(R.drawable.ic_outline_menu_24);
        bottomAppBar.setOnClickListener(view -> {});
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_bottom_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MainActivity", "onOptionsItemSelected " + item);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        NavigationUI.onNavDestinationSelected(item, navController);

        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.pager.setAdapter(new BookCollectionPagerAdapter(this));
        new TabLayoutMediator(binding.booksRecyclerTabs, binding.pager,
                (tab, position) -> tab.setText(titles[position])
        ).attach();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}