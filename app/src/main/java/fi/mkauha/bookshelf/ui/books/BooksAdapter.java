package fi.mkauha.bookshelf.ui.books;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.items.BookItem;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    private List<BookItem> moviesList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
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
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BooksAdapter(List<BookItem> moviesList) {
        this.moviesList = moviesList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BooksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Resources res = holder.itemView.getContext().getResources();
        BookItem bookItem = moviesList.get(position);
        holder.bookTitle.setText(bookItem.getName());
        holder.bookAuthor.setText(bookItem.getAuthor());

        //TODO correct placeholder images
        Picasso.get()
                .load(bookItem.getImageID())
                .resize(500, 700)
                .centerCrop()
                .placeholder(R.drawable.temp_cover_1)
                .into(holder.imageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
