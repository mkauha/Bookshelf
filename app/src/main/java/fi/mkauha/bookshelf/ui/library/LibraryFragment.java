package fi.mkauha.bookshelf.ui.library;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fi.mkauha.bookshelf.databinding.FragmentLibraryBinding;

public class LibraryFragment  extends Fragment {
    private FragmentLibraryBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LibraryFragment", "onCreateView " + this);
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

}
