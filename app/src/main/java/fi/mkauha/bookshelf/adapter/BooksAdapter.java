package fi.mkauha.bookshelf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.squareup.picasso.Picasso;

import java.util.Comparator;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.ItemBookGridBinding;
import fi.mkauha.bookshelf.models.BookItem;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

/**
 * Adapter for recyclerview that holds Book items.
 *
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class BooksAdapter extends SortedListAdapter<BookItem> {

    BooksViewModel booksViewModel;

    /**
     * SharedPreferences key.
     */
    public String prefsKey;

    /**
     * Instantiates a new Books adapter.
     *
     * @param context    the context
     * @param comparator the comparator
     */
    public BooksAdapter(Context context, Comparator<BookItem> comparator, BooksViewModel booksViewModel) {
        super(context, BookItem.class, comparator);
        this.booksViewModel = booksViewModel;
    }

    /**
     * Creates viewHolder.
     *
     * @param inflater    the inflater
     * @param parent      the parent
     * @param viewType      the viewType
     */
    @Override
    protected ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        ItemBookGridBinding binding = ItemBookGridBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, prefsKey);
    }

    /**
     * The View holder for recyclerview.
     */
    public class ViewHolder extends SortedListAdapter.ViewHolder<BookItem> {
        /**
         * The binding for layout.
         */
        ItemBookGridBinding binding;

        /**
         * SharedPreferences key.
         */
        String prefsKey;
        /**
         * The Book id.
         */
        int bookID;
        /**
         * The Book image url.
         */
        String bookImageURL;

        /**
         * Instantiates a new View holder.
         *
         * @param binding  the binding
         * @param prefsKey the SharedPreferences key
         */
        ViewHolder(ItemBookGridBinding binding, String prefsKey) {
            super(binding.getRoot());
            this.binding = binding;
            this.prefsKey = prefsKey;
        }

        /**
         * Binds book item to recyclerview.
         *
         * Sets book items title, bookmark and image to individual card views.
         * Sets each view with an onClickListener that when clicked starts detailsActivity in "View" mode with clicked book item data .
         *
         * @param item  the book item that is bind
         */
        @Override
        protected void performBind(@NonNull BookItem item) {
            binding.itemViewBookTitle.setText(item.getTitle());
            binding.itemViewBookBookmarkText.setText(item.getBookmark());

            if(!item.getBookmark().equals("")) {
                binding.itemViewBookBookmarkIcon.setVisibility(View.VISIBLE);
            } else {
                binding.itemViewBookBookmarkIcon.setVisibility(View.INVISIBLE);
            }
            Picasso.get()
                    .load(item.getImgURL())
                    .resize(500, 700)
                    .centerCrop()
                    .placeholder(R.drawable.book_cover_placeholder)
                    .into(binding.itemViewBookImage);


            binding.getRoot().setOnClickListener(v -> {
                Context context = binding.getRoot().getContext();
                booksViewModel.select(item);
                NavController navController = Navigation.findNavController((AppCompatActivity)context, R.id.nav_host_fragment);
                navController.navigate(R.id.navigation_bookdetails);
            });
        }
    }
}
