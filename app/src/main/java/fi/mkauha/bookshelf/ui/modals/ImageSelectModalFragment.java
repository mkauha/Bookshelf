package fi.mkauha.bookshelf.ui.modals;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentModalImageSelectBinding;
import fi.mkauha.bookshelf.viewmodel.ImageSelectViewModel;

public class ImageSelectModalFragment extends BottomSheetDialogFragment {
    private static final String TAG = "ImageSelectModal";

    FragmentModalImageSelectBinding binding;
    ImageSelectViewModel imageSelectViewModel;
    public static final int PICK_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private String image;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentModalImageSelectBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageSelectViewModel = new ViewModelProvider(requireActivity()).get(ImageSelectViewModel.class);

        binding.imageSelectIcFile.setOnClickListener(v -> {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE);
        });

        binding.imageSelectIcCamera.setOnClickListener(v -> {
            Log.d(TAG, "Camera");
/*            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
        });

        binding.imageSelectIcLink.setOnClickListener(v -> {
            Log.d(TAG, "Link");
        });

        return root;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if(data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                this.image = selectedImageUri.toString();
                Log.d(TAG, "image selected: " + image);
                imageSelectViewModel.selectImageFile(this.image);
                this.dismiss();
            }
        }


        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Log.d(TAG, "photo taken: " + photo);
            this.image = photo.toString();
            imageSelectViewModel.selectImageFile(this.image);
            this.dismiss();
        }

    }
}

