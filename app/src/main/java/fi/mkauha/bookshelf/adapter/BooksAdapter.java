package fi.mkauha.bookshelf.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.squareup.picasso.Picasso;

import java.util.Comparator;
import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.ItemBookGridBinding;
import fi.mkauha.bookshelf.models.BookItem;
import fi.mkauha.bookshelf.ui.details.DetailsActivity;

public class BooksAdapter extends SortedListAdapter<BookItem> {

    public String prefsKey;

    public BooksAdapter(Context context, Comparator<BookItem> comparator) {
        super(context, BookItem.class, comparator);
    }

    @Override
    protected ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        ItemBookGridBinding binding = ItemBookGridBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, prefsKey);
    }

    public class ViewHolder extends SortedListAdapter.ViewHolder<BookItem> {
        ItemBookGridBinding binding;

        String prefsKey;
        int bookID;
        String bookImageURL;

        ViewHolder(ItemBookGridBinding binding, String prefsKey) {
            super(binding.getRoot());
            this.binding = binding;
            this.prefsKey = prefsKey;
    }

        @Override
        protected void performBind(@NonNull BookItem item) {
            bookID = item.getBookID();
            bookImageURL = item.getImgURL();

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

            // TODO fix prefsKey
            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                Context context = binding.getRoot().getContext();
                //Toast.makeText(context,"Pos: " + position,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("Action", "VIEW");
                intent.putExtra("ViewModel_Key", prefsKey);
                intent.putExtra("ID", bookID);
                intent.putExtra("Position", position);
                Log.d("BookAdapter", "onClick position " + position);
                context.startActivity(intent);
            });
        }
    }
}
