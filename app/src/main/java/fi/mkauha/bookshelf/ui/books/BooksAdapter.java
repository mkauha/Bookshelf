package fi.mkauha.bookshelf.ui.books;

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
        holder.bookID = currentBook.getBookID();
        holder.bookTitle.setText(currentBook.getTitle());
        holder.bookAuthor.setText(currentBook.getAuthor());
        //holder.bookGenre.setText(currentBook.getTitle());
        holder.bookImageURL = currentBook.getImgURL();
        holder.bookBookMarkText.setText(currentBook.getBookmark());
        if(!currentBook.getBookmark().equals("")) {
            holder.bookBookMarkIcon.setVisibility(View.VISIBLE);
        } else {
            holder.bookBookMarkIcon.setVisibility(View.INVISIBLE);
        }
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

    void setBooksList(List<BookItem> booksList) {
        Log.d("BookAdapter", "setBooksList " + booksList.size());
        this.booksList = booksList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        int bookID;
        public TextView bookTitle, bookAuthor, bookGenre, bookBookMarkText;
        public ImageView bookImageView, bookBookMarkIcon;
        public String bookImageURL;
        int pos = getAdapterPosition();

        ViewHolder(View view) {
            super(view);
            bookTitle = (TextView) view.findViewById(R.id.itemView_book_name);
            bookAuthor = (TextView) view.findViewById(R.id.itemView_book_author);
            bookImageView = (ImageView) view.findViewById(R.id.itemView_book_image);
            bookBookMarkIcon = (ImageView) view.findViewById(R.id.itemView_book_bookmark_icon);
            bookBookMarkText = (TextView) view.findViewById(R.id.itemView_book_bookmark_text);
            //bookGenre  = (TextView) view.findViewById(R.id.book_genre);
            view.setOnClickListener(v -> {
                Context context = view.getContext();
                Toast.makeText(context,"Pos: " + pos,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, EditBookActivity.class);
                intent.putExtra("Action", "EDIT");
                intent.putExtra("ID", bookID);
                intent.putExtra("Position", pos);
                Log.d("BookAdapter", "onClick position " + pos);
                context.startActivity(intent);
            });
        }
    }
}
