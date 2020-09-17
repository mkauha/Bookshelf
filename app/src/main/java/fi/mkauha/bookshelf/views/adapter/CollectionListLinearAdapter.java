package fi.mkauha.bookshelf.views.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fi.mkauha.bookshelf.databinding.ListItemCollectionBinding;

public class CollectionListLinearAdapter extends RecyclerView.Adapter<CollectionListLinearAdapter.CollectionViewHolder> {

    class CollectionViewHolder extends RecyclerView.ViewHolder {
        ListItemCollectionBinding binding;

        private CollectionViewHolder(ListItemCollectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private final LayoutInflater mInflater;
    private List<String> mCollections;
    private static final String TAG = "CollectionList";

    public CollectionListLinearAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemCollectionBinding binding = ListItemCollectionBinding.inflate(mInflater, parent, false);
        return new CollectionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CollectionViewHolder holder, int position) {
        if (mCollections != null) {
            String collection = mCollections.get(position);
            String pos = "" + position;
            holder.binding.collectionItem.setText(collection);

            holder.binding.getRoot().setOnClickListener(v -> {
                Log.d(TAG, "Collection: " + position);
            });

        } else {
            // Covers the case of data not being ready yet.
            holder.binding.collectionItem.setText(" ");
        }
    }

    public void setCollections(List<String> collections){
        mCollections = collections;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCollections != null)
            return mCollections.size();
        else return 0;
    }
}
