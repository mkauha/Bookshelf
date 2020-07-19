package fi.mkauha.bookshelf.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentSearchBinding;

public class SearchFragment  extends Fragment {
    private FragmentSearchBinding binding;
    BottomAppBar bottomAppBar;
    private MaterialToolbar topAppBar;
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("SearchFragment", "onCreateView " + this);
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bottomAppBar = requireActivity().findViewById(R.id.bottom_app_bar);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_search);

        fab = requireActivity().findViewById(R.id.fab);
        fab.hide();

        binding.toolbar.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_books);
        });

        return root;
    }

}
