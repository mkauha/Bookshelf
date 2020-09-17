package fi.mkauha.bookshelf.views.modal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import fi.mkauha.bookshelf.views.createbook.CreateBookActivity;

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
            Intent intent = new Intent(requireActivity(), CreateBookActivity.class);
            intent.putExtra("CURRENT_BOOK", (Parcelable) null);
            this.startActivity(intent);
            this.dismiss();
        });

        return root;
    }
}
