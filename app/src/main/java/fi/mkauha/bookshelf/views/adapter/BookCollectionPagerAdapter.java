package fi.mkauha.bookshelf.views.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import fi.mkauha.bookshelf.views.mybooks.BookTabFragment;

public class BookCollectionPagerAdapter extends FragmentStateAdapter {
    public BookCollectionPagerAdapter(Fragment fragment) {
        super(fragment);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        Fragment fragment = BookTabFragment.newInstance(position);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}

