package com.pmd.floogle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pmd.floogle.Adapter.PhotoGridAdapter;
import com.pmd.floogle.models.Photo;
import com.pmd.floogle.models.Photos;
import com.pmd.floogle.models.PhotosKt;
import com.pmd.floogle.models.RequestResult;
import com.pmd.floogle.presenters.PhotoListViewPresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.pmd.floogle.SearchActivity.SEARCH_TEXT;

public class PhotoListFragment extends Fragment implements PhotoListViewPresenter.PhotoRequestListener, PhotoGridAdapter.OnPhotoSelectedListener {

    private RecyclerView photoList;
    private PhotoGridAdapter photoGridAdapter;
    private PhotoListViewPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_list, container, false);
        photoList = view.findViewById(R.id.photo_grid);
        photoGridAdapter = new PhotoGridAdapter(this);
        photoList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        photoList.setAdapter(photoGridAdapter);
        if (getArguments() != null) {
            String searchText = getArguments().getString(SEARCH_TEXT);
            if (searchText != null) {
                presenter.requestPhotos(searchText);
            }
        }
        return view;
    }


    private void refreshPhotoList(List<Photo> photos) {
        photoGridAdapter.setPhotos(photos);
        photoGridAdapter.notifyDataSetChanged();
        photoList.scrollToPosition(0);
    }

    private void displayEmptyResultsNotice() {
        Toast.makeText(getContext(), "No Results Found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPhotoRequestComplete(List<Photo> photos) {
        refreshPhotoList(photos);
    }

    @Override
    public void onEmptyPhotoResponse() {
        displayEmptyResultsNotice();
    }

    public void setPresenter(PhotoListViewPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onPhotoSelected(String photoUrl) {
        presenter.displayFullPhotoViewer(photoUrl);
    }
}
