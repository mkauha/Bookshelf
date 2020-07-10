package fi.mkauha.bookshelf.ui.modals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import fi.mkauha.bookshelf.databinding.FragmentModalImageSelectBinding;

public class ImageSelectModalFragment extends BottomSheetDialogFragment {

    FragmentModalImageSelectBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentModalImageSelectBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.imageSelectIcFile.setOnClickListener(v -> {

            this.dismiss();
        });

        binding.imageSelectIcCamera.setOnClickListener(v -> {

            this.dismiss();
        });

        binding.imageSelectIcLink.setOnClickListener(v -> {

            this.dismiss();
        });

        return root;
    }
}

