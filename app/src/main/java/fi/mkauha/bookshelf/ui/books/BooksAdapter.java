package fi.mkauha.bookshelf.ui.books;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.items.BookItem;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    private List<BookItem> booksList;

    public BooksAdapter(List<BookItem> booksList) {
        this.booksList = booksList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BooksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(BooksAdapter.MyViewHolder holder, int position) {
        holder.bindBookItem(booksList.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return booksList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView bookTitle;
        public TextView bookAuthor;
        public ImageView imageView;

        public MyViewHolder(View v) {
            super(v);
            bookTitle = (TextView) v.findViewById(R.id.book_name);
            bookAuthor = (TextView) v.findViewById(R.id.book_author);
            imageView = (ImageView) v.findViewById(R.id.book_cover);
        }
        public void bindBookItem(BookItem bookItem) {
            Log.d("BookAdapter", "bindBookItem");
            //bookItem = booksList.get(position);
            bookTitle.setText(bookItem.getName());
            bookAuthor.setText(bookItem.getAuthor());

            //TODO correct placeholder images
            Picasso.get()
                    .load(bookItem.getImgURL())
                    .resize(500, 700)
                    .centerCrop()
                    .placeholder(R.drawable.temp_cover_1)
                    .into(imageView);
        }

    }

}
