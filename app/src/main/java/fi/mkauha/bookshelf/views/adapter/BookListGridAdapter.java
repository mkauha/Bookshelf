package fi.mkauha.bookshelf.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.ListItemBookGridBinding;
import fi.mkauha.bookshelf.data.local.model.Book;
import fi.mkauha.bookshelf.views.bookdetails.BookDetailsActivity;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

public class BookListGridAdapter extends RecyclerView.Adapter<BookListGridAdapter.BookViewHolder> {

    class BookViewHolder extends RecyclerView.ViewHolder {
        ListItemBookGridBinding binding;

        private BookViewHolder(ListItemBookGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private BooksViewModel booksViewModel;
    private final LayoutInflater mInflater;
    private List<Book> mBooks; // Cached copy of books

    public BookListGridAdapter(Context context, BooksViewModel booksViewModel) {
        this.booksViewModel = booksViewModel;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemBookGridBinding binding = ListItemBookGridBinding.inflate(mInflater, parent, false);
        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        if (mBooks != null) {
            Book book = mBooks.get(position);
            holder.binding.bookmarkIcon.setVisibility(View.INVISIBLE);
            holder.binding.bookmarkText.setVisibility(View.INVISIBLE);
            holder.binding.title.setText(book.getTitle());

            Glide.with(holder.binding.getRoot())
                    .load(book.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.book_cover_placeholder)
                    .into(holder.binding.image);


            holder.binding.getRoot().setOnClickListener(v -> {
                Context context = holder.binding.getRoot().getContext();
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("BOOK_UID", book.getUid());
                context.startActivity(intent);
            });

        } else {
            // TODO Covers the case of data not being ready yet.
            holder.binding.title.setText("Unknown");
        }
    }

    public void setBooks(List<Book> books){
        mBooks = books;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mBooks != null)
            return mBooks.size();
        else return 0;
    }
}