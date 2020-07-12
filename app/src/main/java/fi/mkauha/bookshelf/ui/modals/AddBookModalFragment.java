package fi.mkauha.bookshelf.ui.modals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentModalAddBookBinding;

public class AddBookModalFragment extends BottomSheetDialogFragment {

    FragmentModalAddBookBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentModalAddBookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        binding.addBookSheetIcIsbn.setOnClickListener(v -> {
            // TODO add navigation link
            this.dismiss();
        });

        binding.addBookSheetIcSearch.setOnClickListener(v -> {
            navController.navigate(R.id.navigation_search);
            this.dismiss();
        });

        binding.addBookSheetIcManual.setOnClickListener(v -> {
            navController.navigate(R.id.navigation_create_book);
            this.dismiss();
        });

        return root;
    }
}
