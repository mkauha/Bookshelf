package fi.mkauha.bookshelf.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentAddBookModalBinding;

public class AddBookModalFragment extends BottomSheetDialogFragment {

    FragmentAddBookModalBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBookModalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        NavigationView navigationView = root.findViewById(R.id.add_book_modal_navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            NavigationUI.onNavDestinationSelected(item, navController);
            this.dismiss();
            return false;
        });

        return root;
    }
}
