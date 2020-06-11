package fi.mkauha.bookshelf.ui.bottomnav;
import android.os.Bundle;
import android.util.Log;
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
import fi.mkauha.bookshelf.databinding.FragmentBottomsheetBinding;

public class BottomNavigationFragment extends BottomSheetDialogFragment {

    FragmentBottomsheetBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomsheetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        NavigationView navigationView = root.findViewById(R.id.bottom_sheet_navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            NavigationUI.onNavDestinationSelected(item, navController);
            this.dismiss();
            return false;
        });

        return root;
    }

}