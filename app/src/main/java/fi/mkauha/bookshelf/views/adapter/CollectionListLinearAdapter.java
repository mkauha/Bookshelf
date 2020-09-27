package fi.mkauha.bookshelf.views.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fi.mkauha.bookshelf.data.local.model.Collection;
import fi.mkauha.bookshelf.databinding.ListItemCollectionBinding;
import fi.mkauha.bookshelf.viewmodel.CollectionsViewModel;
import fi.mkauha.bookshelf.views.modal.CreateCollectionFragment;

public class CollectionListLinearAdapter extends RecyclerView.Adapter<CollectionListLinearAdapter.CollectionViewHolder> {

    class CollectionViewHolder extends RecyclerView.ViewHolder {
        ListItemCollectionBinding binding;

        private CollectionViewHolder(ListItemCollectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private CollectionsViewModel viewModel;
    private final LayoutInflater mInflater;
    private List<Collection> mCollections;
    private Context context;
    private static final String TAG = "CollectionList";

    public CollectionListLinearAdapter(Context context, CollectionsViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.viewModel = viewModel;
    }

    @Override
    public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemCollectionBinding binding = ListItemCollectionBinding.inflate(mInflater, parent, false);
        return new CollectionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CollectionViewHolder holder, int position) {
        if (mCollections != null) {
            final Collection collection = mCollections.get(position);
            String pos = "" + position;
            Drawable defaultBackground = holder.binding.collectionItem.getBackground();
            holder.binding.collectionItem.setBackgroundResource(android.R.color.transparent);
            holder.binding.collectionItem.setText(collection.getTitle());
            holder.binding.collectionItem.setSelectAllOnFocus(true);
/*            holder.binding.getRoot().setOnClickListener(v -> {
                Log.d(TAG, "Collection: " + position);
            });*/

            holder.binding.remove.setOnClickListener(v -> {
                viewModel.deleteCollection(collection);
            });

            holder.binding.edit.setOnClickListener(v -> {
                String collectionName = "";
                int collectionId = 0;
                if(collection.getTitle() != null) {
                    collectionName = collection.getTitle();
                    collectionId = collection.getUid();
                }
                FragmentActivity activity = (FragmentActivity) this.context;
                CreateCollectionFragment createCollectionFragment = new CreateCollectionFragment();
                Bundle bundle = new Bundle();
                bundle.putString("NAME", collectionName);
                bundle.putInt("ID", collectionId);
                createCollectionFragment.setArguments(bundle);
                createCollectionFragment.show(activity.getSupportFragmentManager(), createCollectionFragment.getTag());
            });


        } else {
            // Covers the case of data not being ready yet.
            holder.binding.collectionItem.setText(" ");
        }
    }

    public void setCollections(List<Collection> collections){
        mCollections = collections;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCollections != null)
            return mCollections.size();
        else return 0;
    }

    private void setEditable(CollectionViewHolder holder, Boolean isEditable, Drawable defaultBackground) {
        holder.binding.collectionItem.setEnabled(isEditable);
        holder.binding.collectionItem.setFocusable(isEditable);
        holder.binding.collectionItem.setFocusableInTouchMode(isEditable);
        holder.binding.collectionItem.setCursorVisible(isEditable);

        if(isEditable) {
            holder.binding.confirm.setVisibility(View.VISIBLE);
            holder.binding.edit.setVisibility(View.INVISIBLE);
            holder.binding.remove.setVisibility(View.INVISIBLE);
            holder.binding.collectionItem.setBackground(defaultBackground);
            holder.binding.collectionItem.clearFocus();
            holder.binding.collectionItem.requestFocus();
            holder.binding.collectionItem.selectAll();
            holder.binding.collectionItem.setSelection(holder.binding.collectionItem.length());

            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(holder.binding.collectionItem, InputMethodManager.SHOW_IMPLICIT);
        } else {
            holder.binding.confirm.setVisibility(View.INVISIBLE);
            holder.binding.edit.setVisibility(View.VISIBLE);
            holder.binding.remove.setVisibility(View.VISIBLE);
            holder.binding.collectionItem.setBackground(defaultBackground);
        }
    }
}
