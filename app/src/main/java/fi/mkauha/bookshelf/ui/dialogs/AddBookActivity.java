package fi.mkauha.bookshelf.ui.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.items.BookItem;

public class AddBookActivity extends AppCompatActivity {

    private String title = "-";
    private String author = "-";
    private String genre = "-";
    private String imgURL = "-";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.add_book);
        setContentView(R.layout.activity_add_book);
    }

    public void onClickAdd(View view) {
        EditText etTitle = findViewById(R.id.addDialog_bookTitle);
        EditText etAuthor = findViewById(R.id.addDialog_bookAuthor);
        EditText etGenre = findViewById(R.id.addDialog_bookGenre);
        EditText etImageURL = findViewById(R.id.addDialog_bookImgURL);

        title = etTitle.getText().toString();
        author = etAuthor.getText().toString();
        genre = etGenre.getText().toString();
        imgURL = etImageURL.getText().toString();



        // TODO unique id generation
        // TODO don't allow empty title and author
          if(imgURL == null || imgURL.equals("")) {
              imgURL = "placeholder";
          }
        BookItem bookItem = new BookItem(1, title, author, genre, imgURL);

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        Intent intentUpdate = new Intent("AddBookActivity");
        Bundle bundle = new Bundle();
        bundle.putSerializable("BOOK_ITEM", bookItem);
        intentUpdate.putExtras(bundle);
        manager.sendBroadcast(intentUpdate);

        finish();
    }
}

