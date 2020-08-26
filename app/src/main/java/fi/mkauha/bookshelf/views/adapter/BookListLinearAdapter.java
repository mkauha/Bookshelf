package fi.mkauha.bookshelf.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fi.mkauha.bookshelf.databinding.ListItemBookLinearBinding;
import fi.mkauha.bookshelf.data.remote.model.Record;
import fi.mkauha.bookshelf.views.bookdetails.BookDetailsActivity;
import fi.mkauha.bookshelf.viewmodel.SearchViewModel;

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

/*            Glide.with(holder.binding.getRoot())
                    .load(book.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.book_cover_placeholder)
                    .into(holder.binding.image);*/


            holder.binding.getRoot().setOnClickListener(v -> {
                Context context = holder.binding.getRoot().getContext();
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("BOOK_URL", book.getId());
                context.startActivity(intent);
            });

        } else {
            // Covers the case of data not being ready yet.
            holder.binding.title.setText("Unknown");
        }
    }

    public void setBooks(List<Record> books){
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
