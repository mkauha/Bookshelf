package fi.mkauha.bookshelf.views.info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentInfoBinding;


public class InfoFragment extends Fragment {

    FragmentInfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void onClickLogo(View view) { }

    public void onClickGithub(View view) {
        String url = getResources().getString(R.string.app_github_URL);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void onClickAPI(View view) {
        String url = getResources().getString(R.string.credits_data_sources_kirkanta_URL);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void onClickPicasso(View view) {
        String url = getResources().getString(R.string.credits_licenses_picasso_URL);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void onClickMapBox(View view) {
        String url = getResources().getString(R.string.credits_licenses_mapbox_URL);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void onClickSortedList(View view) {
        String url = getResources().getString(R.string.credits_licenses_credits_licenses_sortedlist_URL);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}