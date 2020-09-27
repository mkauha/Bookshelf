package fi.mkauha.bookshelf.views.collections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentCollectionsBinding;
import fi.mkauha.bookshelf.viewmodel.CollectionsViewModel;
import fi.mkauha.bookshelf.views.adapter.CollectionListLinearAdapter;
import fi.mkauha.bookshelf.views.modal.CreateCollectionFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionsFragment extends Fragment {
    private FragmentCollectionsBinding binding;
    private CollectionListLinearAdapter mCollectionsAdapter;
    private CollectionsViewModel viewModel;
    private List<String> collections = new ArrayList<>();

    public CollectionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CollectionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectionsFragment newInstance(String param1, String param2) {
        CollectionsFragment fragment = new CollectionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCollectionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(requireActivity()).get(CollectionsViewModel.class);

        collections.add(this.getResources().getString(R.string.collection_all_books));
        collections.add(this.getResources().getString(R.string.collection_wishlist));
        collections.add(this.getResources().getString(R.string.collection_study));

        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.hide();

        mCollectionsAdapter = new CollectionListLinearAdapter(getContext(), viewModel);
        binding.collectionRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.collectionRecyclerview.setHasFixedSize(true);
        binding.collectionRecyclerview.setAdapter(mCollectionsAdapter);

        viewModel.getAllCollections().observe(requireActivity(),
                list -> {
                    if(list != null) {
                        mCollectionsAdapter.setCollections(list);
                    }
                }
        );

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.popBackStack();
            }
        });

        binding.addCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int collectionId = -1;
                Bundle bundle = new Bundle();
                bundle.putInt("ID", collectionId);
                CreateCollectionFragment createCollectionFragment = new CreateCollectionFragment();
                createCollectionFragment.show(requireActivity().getSupportFragmentManager(), createCollectionFragment.getTag());
            }
        });

        return root;
    }
}