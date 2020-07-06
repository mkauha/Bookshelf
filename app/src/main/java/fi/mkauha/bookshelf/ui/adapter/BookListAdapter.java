package fi.mkauha.bookshelf.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.ItemBookGridBinding;
import fi.mkauha.bookshelf.models.Book;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

    class BookViewHolder extends RecyclerView.ViewHolder {
        ItemBookGridBinding binding;

        private BookViewHolder(ItemBookGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private BooksViewModel booksViewModel;
    private final LayoutInflater mInflater;
    private List<Book> mBooks; // Cached copy of words

    public BookListAdapter(Context context, BooksViewModel booksViewModel) {
        this.booksViewModel = booksViewModel;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemBookGridBinding binding = ItemBookGridBinding.inflate(mInflater, parent, false);
        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        if (mBooks != null) {
            Book current = mBooks.get(position);
            holder.binding.itemViewBookBookmarkIcon.setVisibility(View.INVISIBLE);
            holder.binding.itemViewBookTitle.setText(current.getTitle());

            Glide.with(holder.binding.getRoot())
                    .load(current.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.book_cover_placeholder)
                    .into(holder.binding.itemViewBookImage);


            holder.binding.getRoot().setOnClickListener(v -> {
                Log.d("onBindViewHolder", "select: " + current.getImage());
                Context context = holder.binding.getRoot().getContext();
                booksViewModel.select(current);
                NavController navController = Navigation.findNavController((AppCompatActivity)context, R.id.nav_host_fragment);
                navController.navigate(R.id.navigation_bookdetails);
            });

        } else {
            // Covers the case of data not being ready yet.
            holder.binding.itemViewBookTitle.setText("No Book");
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