package fi.mkauha.bookshelf.views.mybooks;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentBooksBinding;
import fi.mkauha.bookshelf.views.adapter.BookCollectionPagerAdapter;
import fi.mkauha.bookshelf.views.adapter.CollectionListLinearAdapter;
import fi.mkauha.bookshelf.views.modal.AddBookModalFragment;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

import static android.content.ContentValues.TAG;

public class BooksFragment extends Fragment  {
    private FragmentBooksBinding binding;
    private BooksViewModel booksViewModel;
    FloatingActionButton fab;
    BottomAppBar bottomAppBar;
    private MaterialToolbar topAppBar;

    private CollectionListLinearAdapter mCollectionsAdapter;
    // TODO get titles from local storage
    private List<String> collections = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView " + this);

        binding = FragmentBooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        collections.add(this.getResources().getString(R.string.collection_all_books));
        collections.add(this.getResources().getString(R.string.collection_wishlist));
        collections.add(this.getResources().getString(R.string.collection_study));

        mCollectionsAdapter = new CollectionListLinearAdapter(getContext());
        binding.includeCollections.collectionRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.includeCollections.collectionRecyclerview.setHasFixedSize(true);
        binding.includeCollections.collectionRecyclerview.setAdapter(mCollectionsAdapter);
        mCollectionsAdapter.setCollections(collections);
        Log.d(TAG, "mCollectionsAdapter "+ mCollectionsAdapter.getItemCount());


        fab = requireActivity().findViewById(R.id.fab);
        fab.show();
        fab.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_outline_add_24));
        fab.setOnClickListener(view -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
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

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.navigation_collections);
                if(binding.includeCollections.collections.getVisibility() == View.VISIBLE) {
/*                    binding.pager.setVisibility(View.VISIBLE);
                    binding.includeCollections.collections.setVisibility(View.GONE);
                    binding.booksRecyclerTabs.setVisibility(View.VISIBLE);
                    binding.edit.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_outline_keyboard_arrow_down_24, null));*/


                } else {
/*                    //binding.pager.setVisibility(View.GONE);
                    binding.includeCollections.collections.setVisibility(View.VISIBLE);
                    binding.booksRecyclerTabs.setVisibility(View.INVISIBLE);
                    binding.edit.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_outline_keyboard_arrow_up_24, null));*/
                }
            }
        });

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
                (tab, position) -> tab.setText(collections.get(position))
        ).attach();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}