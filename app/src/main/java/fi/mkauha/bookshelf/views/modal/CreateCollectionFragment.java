package fi.mkauha.bookshelf.views.modal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import fi.mkauha.bookshelf.data.local.model.Collection;
import fi.mkauha.bookshelf.databinding.FragmentModalCollectionBinding;
import fi.mkauha.bookshelf.viewmodel.CollectionsViewModel;

public class CreateCollectionFragment extends BottomSheetDialogFragment {
    private static final String TAG = "CreateCollectionFragmen";
    FragmentModalCollectionBinding binding;
    private CollectionsViewModel viewModel;
    private Collection collection;
    private String collectionName;
    private int collectionId = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentModalCollectionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewModel = new ViewModelProvider(requireActivity()).get(CollectionsViewModel.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            collectionName = bundle.getString("NAME", "");
            collectionId = bundle.getInt("ID", 0);
            if(collectionId != 0) {
                binding.title.setText(collectionName);
                binding.title.requestFocus();
                binding.title.selectAll();
            }
        }

        binding.ok.setOnClickListener(v -> {
            // TODO input validation
            if(collectionId == 0) {
                viewModel.insertCollection(new Collection(binding.title.getText().toString(), 0));
            } else {
                viewModel.findCollectionById(collectionId);
                viewModel.getCollection().observe(requireActivity(),
                        collection -> {
                            this.collection = collection;
                            collection.setTitle(binding.title.getText().toString());
                            viewModel.insertCollection(collection);
                        }
                );
            }

            this.dismiss();
        });

        binding.cancel.setOnClickListener(v -> {
            this.dismiss();
        });
        return root;
    }
}
