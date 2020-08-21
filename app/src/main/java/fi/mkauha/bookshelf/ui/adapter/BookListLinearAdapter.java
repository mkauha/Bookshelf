package fi.mkauha.bookshelf.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.ListItemBookGridBinding;
import fi.mkauha.bookshelf.databinding.ListItemBookLinearBinding;
import fi.mkauha.bookshelf.models.Book;
import fi.mkauha.bookshelf.ui.books.BookDetailsActivity;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

public class BookListLinearAdapter extends RecyclerView.Adapter<BookListLinearAdapter.BookViewHolder> {

    class BookViewHolder extends RecyclerView.ViewHolder {
        ListItemBookLinearBinding binding;

        private BookViewHolder(ListItemBookLinearBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private BooksViewModel booksViewModel;
    private final LayoutInflater mInflater;
    private List<Book> mBooks; // Cached copy of words

    public BookListLinearAdapter(Context context, BooksViewModel booksViewModel) {
        this.booksViewModel = booksViewModel;
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
            Book book = mBooks.get(position);
            holder.binding.title.setText(book.getTitle());
            holder.binding.author.setText(book.getAuthor());
            holder.binding.language.setText(book.getLanguages());
            holder.binding.year.setText(book.getYear());

            Glide.with(holder.binding.getRoot())
                    .load(book.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.book_cover_placeholder)
                    .into(holder.binding.image);


            holder.binding.getRoot().setOnClickListener(v -> {
                Log.d("onBindViewHolder", "select: " + book.getImage());
                Context context = holder.binding.getRoot().getContext();
                booksViewModel.select(book);
/*                NavController navController = Navigation.findNavController((AppCompatActivity)context, R.id.nav_host_fragment);
                navController.navigate(R.id.navigation_book_details);*/
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("CURRENT_BOOK", (Parcelable) book);
                context.startActivity(intent);
            });

        } else {
            // Covers the case of data not being ready yet.
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
