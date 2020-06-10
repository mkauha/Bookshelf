package fi.mkauha.bookshelf.ui.bottomnav;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentBottomsheetBinding;

public class BottomNavigationFragment extends BottomSheetDialogFragment {

    FragmentBottomsheetBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottomsheet, container, false);
    }

}