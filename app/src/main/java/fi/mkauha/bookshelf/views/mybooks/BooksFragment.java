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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.data.local.model.Collection;
import fi.mkauha.bookshelf.databinding.FragmentBooksBinding;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;
import fi.mkauha.bookshelf.viewmodel.CollectionsViewModel;
import fi.mkauha.bookshelf.views.adapter.BookCollectionPagerAdapter;
import fi.mkauha.bookshelf.views.adapter.CollectionListLinearAdapter;
import fi.mkauha.bookshelf.views.modal.AddBookModalFragment;

import static android.content.ContentValues.TAG;

public class BooksFragment extends Fragment  {
    private FragmentBooksBinding binding;
    private BooksViewModel booksViewModel;
    private CollectionsViewModel collectionsViewModel;
    FloatingActionButton fab;
    BottomAppBar bottomAppBar;
    private MaterialToolbar topAppBar;

    private CollectionListLinearAdapter mCollectionsAdapter;
    // TODO get titles from local storage
    private List<Collection> collections = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView " + this);

        binding = FragmentBooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        booksViewModel = new ViewModelProvider(requireActivity()).get(BooksViewModel.class);
        collectionsViewModel = new ViewModelProvider(requireActivity()).get(CollectionsViewModel.class);

        mCollectionsAdapter = new CollectionListLinearAdapter(getContext(), collectionsViewModel);
        binding.includeCollections.collectionRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.includeCollections.collectionRecyclerview.setHasFixedSize(true);
        binding.booksRecyclerTabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        BookCollectionPagerAdapter bookCollectionPagerAdapter = new BookCollectionPagerAdapter(this);
        binding.pager.setAdapter(bookCollectionPagerAdapter);
        booksViewModel.getAllCollections().observe(requireActivity(),
                list -> {
                    this.collections = list;
                    bookCollectionPagerAdapter.setItemCount(this.collections.size());
                    mCollectionsAdapter.setCollections(this.collections);
                    TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.booksRecyclerTabs, binding.pager,
                            (tab, position) -> tab.setText(collections.get(position).getTitle())
                    );
                    tabLayoutMediator.attach();
                    Log.d(TAG, "mCollectionsAdapter "+ mCollectionsAdapter.getItemCount());
                }
        );

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
                navController.navigate(R.id.collections);
            }
        });

        bottomAppBar = (BottomAppBar) getActivity().findViewById(R.id.bottom_app_bar);
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
        binding.includeCollections.collectionRecyclerview.setAdapter(mCollectionsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
        //booksViewModel.getAllCollections().removeObservers(this);
    }
}