package com.pmd.floogle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pmd.floogle.Adapter.PhotoGridAdapter;
import com.pmd.floogle.models.Photo;
import com.pmd.floogle.presenters.PhotoListViewPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.pmd.floogle.SearchActivity.SEARCH_TEXT;

public class PhotoListFragment extends Fragment implements PhotoListViewPresenter.PhotoRequestListener, PhotoGridAdapter.OnPhotoSelectedListener {

    private RecyclerView photoList;
    private PhotoGridAdapter photoGridAdapter;
    private PhotoListViewPresenter presenter;
    private List<Photo> currentList = new ArrayList<>();
    private int currentPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.photo_list, container, false);
        photoList = view.findViewById(R.id.photo_grid);
        photoGridAdapter = new PhotoGridAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        photoList.setLayoutManager(layoutManager);
        photoList.setAdapter(photoGridAdapter);


        photoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (layoutManager.findLastVisibleItemPosition() == photoGridAdapter.getItemCount() - 1) {
                    presenter.requestNextPage();
                }

            }
        });

        getPhotosForDisplay();
        return view;
    }

    /**
     * Request new photo set if list is not already populated or restore previously
     * searched list
     */
    public void getPhotosForDisplay() {
        if (getArguments() != null && currentList.isEmpty()) {
            String searchText = getArguments().getString(SEARCH_TEXT);
            if (searchText != null) {
                presenter.requestNewPhotoSearch(searchText);
            }
        }else {
            addToPhotoList(currentList);
            photoList.scrollToPosition(currentPosition);
        }
    }

    /**
     * Save current list and position on orientation change
     * since setRetainInstance state is true there is no need to
     * actually save a bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        currentList = photoGridAdapter.getList();
        GridLayoutManager layoutManager = (GridLayoutManager) photoList.getLayoutManager();
        currentPosition = layoutManager.findLastVisibleItemPosition();
        super.onSaveInstanceState(outState);
    }

    /**
     * Set new list upon new search and scroll to beginning
     * @param photos request results list
     */
    private void refreshPhotoList(List<Photo> photos) {
        photoGridAdapter.setPhotos(photos);
        photoGridAdapter.notifyDataSetChanged();
        photoList.scrollToPosition(0);
    }

    /**
     * Add photos to current list in adapter
     * @param photos data set to add to current list
     */
    private void addToPhotoList(List<Photo> photos) {
        photoGridAdapter.addPhotos(photos);
        photoGridAdapter.notifyDataSetChanged();
    }

    /**
     * Display toast error when no resutls are found
     */
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

    @Override
    public void onNextPage(List<Photo> photos) {
        addToPhotoList(photos);
    }

    public void setPresenter(PhotoListViewPresenter presenter) {
        this.presenter = presenter;
    }

    public PhotoListViewPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onPhotoSelected(String photoUrl) {
        presenter.displayFullPhotoViewer(photoUrl);
    }
}
