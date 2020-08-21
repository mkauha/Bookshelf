package fi.mkauha.bookshelf.ui.books;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fi.mkauha.bookshelf.databinding.FragmentBooksRecyclerviewBinding;
import fi.mkauha.bookshelf.ui.adapter.BookListGridAdapter;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

public class BookTabFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private FragmentBooksRecyclerviewBinding binding;
    private BooksViewModel booksViewModel;
    private BookListGridAdapter mAdapter;
    private FloatingActionButton fab;
    private static final String ARG_COUNT = "param1";
    private Integer counter;

    public static BookTabFragment newInstance(Integer counter) {
        BookTabFragment fragment = new BookTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBooksRecyclerviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.booksRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        binding.booksRecyclerView.setHasFixedSize(true);
/*        binding.booksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });*/

        booksViewModel = new ViewModelProvider(requireActivity()).get(BooksViewModel.class);
        booksViewModel.select(null);

        mAdapter = new BookListGridAdapter(getContext(), booksViewModel);
        binding.booksRecyclerView.setAdapter(mAdapter);

        booksViewModel.getAllBooks().observe(requireActivity(),

                list -> {
                    mAdapter.setBooks(list);
                }
        );

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            counter = getArguments().getInt(ARG_COUNT);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
/*        ((TextView) view.findViewById(android.R.id.text1))
                .setText(Integer.toString(args.getInt(ARG_OBJECT)));*/
    }
}
