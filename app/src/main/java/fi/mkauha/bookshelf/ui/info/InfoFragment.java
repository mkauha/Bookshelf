package fi.mkauha.bookshelf.ui.info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentInfoBinding;


/**
 * Activity that displays credits and licences.
 *
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class InfoFragment extends Fragment {

    /**
     * The layout binding.
     */
    FragmentInfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    /**
     * On click logo.
     *
     * @param view the view
     */
    public void onClickLogo(View view) { }

    /**
     * On click github.
     *
     * @param view the view
     */
    public void onClickGithub(View view) {
        String url = getResources().getString(R.string.app_github_URL);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    /**
     * On click api.
     *
     * @param view the view
     */
    public void onClickAPI(View view) {
        String url = getResources().getString(R.string.credits_data_sources_kirkanta_URL);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    /**
     * On click picasso.
     *
     * @param view the view
     */
    public void onClickPicasso(View view) {
        String url = getResources().getString(R.string.credits_licenses_picasso_URL);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    /**
     * On click map box.
     *
     * @param view the view
     */
    public void onClickMapBox(View view) {
        String url = getResources().getString(R.string.credits_licenses_mapbox_URL);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    /**
     * On click SortedListAdapter.
     *
     * @param view the view
     */
    public void onClickSortedList(View view) {
        String url = getResources().getString(R.string.credits_licenses_credits_licenses_sortedlist_URL);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}