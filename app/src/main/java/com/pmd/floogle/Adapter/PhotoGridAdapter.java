package com.pmd.floogle.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pmd.floogle.R;
import com.pmd.floogle.models.Photo;
import com.pmd.floogle.network.PhotoUrlUtilKt;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.ViewHolder> {

    private List<Photo> photos = new ArrayList<>();
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
    private OnPhotoSelectedListener listener;

    public interface OnPhotoSelectedListener{
        void onPhotoSelected(String photoUrl);
    }

    public PhotoGridAdapter(OnPhotoSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_card, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo photo = photos.get(position);

        //Render image using Picasso library
        if (!TextUtils.isEmpty(photo.getId())) {
            loadPhotoIntoView(holder.itemView.getContext(),
                    holder.photo,
                    PhotoUrlUtilKt.generateFlickrPhotoThumbnailUrl(photo.getId(),
                            photo.getFarm(),
                            photo.getServer(),
                            photo.getSecret()));

            holder.title.setText(photo.getTitle());

            holder.view.setOnClickListener(view -> listener.onPhotoSelected(PhotoUrlUtilKt.generateFlickrPhotoUrl(photo.getId(),
                    photo.getFarm(),
                    photo.getServer(),
                    photo.getSecret())));
        }

    }

    private void loadPhotoIntoView(Context context, ImageView imageView, String photoUrl) {
        Picasso.with(context)
                .load(photoUrl)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return (photos != null) ? photos.size() : 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView photo;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView.findViewById(R.id.card_view);
            photo = itemView.findViewById(R.id.card_image);
            title = itemView.findViewById(R.id.card_title);
        }

    }
}
