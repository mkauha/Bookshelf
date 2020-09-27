package fi.mkauha.bookshelf.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.data.remote.model.Record;
import fi.mkauha.bookshelf.databinding.ListItemBookLinearBinding;
import fi.mkauha.bookshelf.viewmodel.SearchViewModel;
import fi.mkauha.bookshelf.views.bookdetails.BookDetailsActivity;

public class BookListLinearAdapter extends RecyclerView.Adapter<BookListLinearAdapter.BookViewHolder> {

    class BookViewHolder extends RecyclerView.ViewHolder {
        ListItemBookLinearBinding binding;

        private BookViewHolder(ListItemBookLinearBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private SearchViewModel searchViewModel;
    private final LayoutInflater mInflater;
    private List<Record> mBooks;
    private static final String TAG = "BookListLinearAdapter";

    public BookListLinearAdapter(Context context, SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemBookLinearBinding binding = ListItemBookLinearBinding.inflate(mInflater, parent, false);
        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        if (mBooks != null) {
            Record book = mBooks.get(position);
            holder.binding.title.setText(book.getTitle());
            holder.binding.author.setText(book.getNonPresenterAuthors().get(0).getName());
            holder.binding.language.setText(book.getLanguages().get(0));
            holder.binding.year.setText(book.getYear());

            String imageURL = "https://sociology.indiana.edu/images/publications/book-cover-placeholder.jpg";
            if(book.getImages().size() > 0) {
                imageURL = "https://api.finna.fi" + book.getImages().get(0);
            }

            Glide.with(holder.binding.getRoot())
                    .load(imageURL)
                    .centerCrop()
                    .placeholder(R.drawable.book_cover_placeholder)
                    .into(holder.binding.image);


            holder.binding.getRoot().setOnClickListener(v -> {
                Context context = holder.binding.getRoot().getContext();
                Intent intent = new Intent(context, BookDetailsActivity.class);
                Log.d(TAG, "RECORD_ID: " + book.getId());
                intent.putExtra("RECORD_ID", book.getId());
                context.startActivity(intent);
            });

        } else {
            // Covers the case of data not being ready yet.
            holder.binding.title.setText(" ");
        }
    }

    public void setBooks(List<Record> books){
        mBooks = books;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mBooks != null)
            return mBooks.size();
        else return 0;
    }
}
