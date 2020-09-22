package fi.mkauha.bookshelf.views.collections;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentCollectionsBinding;
import fi.mkauha.bookshelf.views.adapter.CollectionListLinearAdapter;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionsFragment extends Fragment {
    private FragmentCollectionsBinding binding;
    private CollectionListLinearAdapter mCollectionsAdapter;
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

        collections.add(this.getResources().getString(R.string.collection_all_books));
        collections.add(this.getResources().getString(R.string.collection_wishlist));
        collections.add(this.getResources().getString(R.string.collection_study));

        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.hide();

        mCollectionsAdapter = new CollectionListLinearAdapter(getContext());
        binding.collectionRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.collectionRecyclerview.setHasFixedSize(true);
        binding.collectionRecyclerview.setAdapter(mCollectionsAdapter);
        mCollectionsAdapter.setCollections(collections);
        Log.d(TAG, "mCollectionsAdapter "+ mCollectionsAdapter.getItemCount());

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.navigation_books);
            }
        });

        return root;
    }
}