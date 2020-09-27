package fi.mkauha.bookshelf.views.search;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentSearchBinding;
import fi.mkauha.bookshelf.viewmodel.SearchViewModel;
import fi.mkauha.bookshelf.views.adapter.BookListLinearAdapter;

public class SearchFragment  extends Fragment implements OnKeyboardVisibilityListener {
    private static final String TAG = "SearchFragment";


    private FragmentSearchBinding binding;
    BottomAppBar bottomAppBar;
    private MaterialToolbar topAppBar;
    FloatingActionButton fab;
    private SearchViewModel searchViewModel;
    private BookListLinearAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setKeyboardVisibilityListener(this);

        bottomAppBar = requireActivity().findViewById(R.id.bottom_app_bar);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_search);

        fab = requireActivity().findViewById(R.id.fab);
        fab.hide();

        binding.toolbar.setNavigationOnClickListener(v -> {
            closeKeyboard();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.my_books);
        });

        binding.booksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.booksRecyclerView.setHasFixedSize(true);
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        searchViewModel.init();

        mAdapter = new BookListLinearAdapter(getContext(), searchViewModel);
        binding.booksRecyclerView.setAdapter(mAdapter);

        binding.searchTextfield.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });

        binding.searchTextfield.setOnEditorActionListener((textView, i, keyEvent) -> {
            Log.d(TAG, "query: " + textView.getText().toString());
            searchViewModel.searchAllRecords(textView.getText().toString());
            mAdapter.setBooks(searchViewModel.getSearchResults().getValue());
            closeKeyboard();
            return true;
        });

        searchViewModel.getSearchResults().observe(requireActivity(),
                list -> {
                    Log.d(TAG, "mAdapter.setBooks " + list);
                    mAdapter.setBooks(list);
                }
        );


        return root;
    }

    public void closeKeyboard(){
        try {
            InputMethodManager editTextInput = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            editTextInput.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            Log.e("AndroidView", "closeKeyboard: "+e);
        }
    }

    private void setKeyboardVisibilityListener(final OnKeyboardVisibilityListener onKeyboardVisibilityListener) {
        final View parentView = ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {
                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
                parentView.getWindowVisibleDisplayFrame(rect);
                int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == alreadyOpen) {
                    Log.i("Keyboard state", "Ignoring global layout change...");
                    return;
                }
                alreadyOpen = isShown;
                onKeyboardVisibilityListener.onVisibilityChanged(isShown);
            }
        });
    }


    @Override
    public void onVisibilityChanged(boolean visible) {
        if(visible) {
            binding.toolbar.setVisibility(View.INVISIBLE);
        } else {
            binding.toolbar.setVisibility(View.VISIBLE);
        }
    }
}
