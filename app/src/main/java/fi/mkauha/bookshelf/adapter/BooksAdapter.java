package fi.mkauha.bookshelf.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.ItemBookGridBinding;
import fi.mkauha.bookshelf.model.BookItem;
import fi.mkauha.bookshelf.ui.details.DetailsActivity;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    BooksViewModel booksViewModel;
    View view;
    private List<BookItem> booksList =  new ArrayList<>();

    public BooksAdapter(BooksViewModel booksViewModel) {
        this.booksViewModel = booksViewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_grid, parent, false);
        ItemBookGridBinding binding = ItemBookGridBinding.bind(view);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.prefsKey = booksViewModel.getCurrentKey();

        BookItem currentBook = booksList.get(position);
        holder.bookID = currentBook.getBookID();
        holder.bookImageURL = currentBook.getImgURL();

        holder.binding.itemViewBookTitle.setText(currentBook.getTitle());
        holder.binding.itemViewBookBookmarkText.setText(currentBook.getBookmark());

        if(!currentBook.getBookmark().equals("")) {
            holder.binding.itemViewBookBookmarkIcon.setVisibility(View.VISIBLE);
        } else {
            holder.binding.itemViewBookBookmarkIcon.setVisibility(View.INVISIBLE);
        }
        Picasso.get()
                .load(currentBook.getImgURL())
                .resize(500, 700)
                .centerCrop()
                .placeholder(R.drawable.book_cover_placeholder)
                .into(holder.binding.itemViewBookImage);
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public void setBooksList(List<BookItem> booksList) {
        this.booksList = booksList;
        //notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ItemBookGridBinding binding;

        String prefsKey;
        int bookID;
        String bookImageURL;

        ViewHolder(ItemBookGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

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
