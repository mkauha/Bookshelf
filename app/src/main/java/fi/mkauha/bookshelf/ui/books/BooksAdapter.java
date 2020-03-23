package fi.mkauha.bookshelf.ui.books;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.items.BookItem;
import fi.mkauha.bookshelf.ui.dialogs.EditBookActivity;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private List<BookItem> booksList =  new ArrayList<>();


/*    public BooksAdapter(List<BookItem> booksList) {
        this.booksList = booksList;
    }*/

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BookItem currentBook = booksList.get(position);
        holder.bookTitle.setText(currentBook.getTitle());
        holder.bookAuthor.setText(currentBook.getTitle());
        //holder.bookGenre.setText(currentBook.getTitle());
        Picasso.get()
                .load(currentBook.getImgURL())
                .resize(500, 700)
                .centerCrop()
                .placeholder(R.drawable.temp_cover_1)
                .into(holder.bookImageView);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public void setBooksList(List<BookItem> booksList) {
        this.booksList = booksList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView bookTitle, bookAuthor, bookGenre;
        public ImageView bookImageView;

        public ViewHolder(View view) {
            super(view);
            bookTitle = (TextView) view.findViewById(R.id.book_name);
            bookAuthor = (TextView) view.findViewById(R.id.book_author);
            //bookGenre  = (TextView) view.findViewById(R.id.book_genre);
            bookImageView = (ImageView) view.findViewById(R.id.book_cover);
            view.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                Context context = view.getContext();
                //Toast.makeText(context,"Pos: " + pos,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, EditBookActivity.class);
                intent.putExtra("Action", "EDIT");
                context.startActivity(intent);
            });
        }
        public void bindBookItem(BookItem bookItem) {
            Log.d("BookAdapter", "bindBookItem");
            //bookItem = booksList.get(position);
            bookTitle.setText(bookItem.getTitle());
            bookAuthor.setText(bookItem.getAuthor());
        }
        public interface OnItemClickListener {
            void onItemClick(View view, ViewModel viewModel);
        }
    }
}
