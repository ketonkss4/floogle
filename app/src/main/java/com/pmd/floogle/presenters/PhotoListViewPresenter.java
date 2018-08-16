package com.pmd.floogle.presenters;

import com.pmd.floogle.models.Photo;
import com.pmd.floogle.models.Photos;
import com.pmd.floogle.models.PhotosKt;
import com.pmd.floogle.models.RequestResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class PhotoListViewPresenter {

    private static final int INITIAL_PAGE = 1;
    private PhotoRequestListener listener;
    private PhotoViewerController photoViewerController;
    private String currentSearchText;
    private Disposable subscription;
    private int currentPage = 1;

    public interface PhotoRequestListener {

        void onPhotoRequestComplete(List<Photo> photo);

        void onEmptyPhotoResponse();

        void onNextPage(List<Photo> photo);
    }

    public interface PhotoViewerController {

        void displayFullPhotoViewer(String photoUrl);
    }

    public PhotoListViewPresenter(PhotoViewerController photoViewerController) {
        this.photoViewerController = photoViewerController;
    }

    public void setListener(PhotoRequestListener listener) {
        this.listener = listener;
    }

    /**
     * Request next page of current search
     */
    public void requestNextPage() {
        if (currentSearchText != null && !currentSearchText.isEmpty()) {
            requestPhotoSearchByPage(currentSearchText, ++currentPage);
        }
    }

    /**
     * Request new search on first page
     * @param searchText user search text
     */
    public void requestNewPhotoSearch(String searchText) {
        currentSearchText = searchText;
        requestPhotoSearchByPage(searchText, INITIAL_PAGE);
    }

    /**
     * Asynchronously Request a photo search based on user search text and target page
     * @param searchText user search text
     * @param page targeted page
     */
    private void requestPhotoSearchByPage(String searchText, int page) {
        currentPage = page;
        Observable<RequestResult> photosObservable = PhotosKt.requestPhotos(searchText, String.valueOf(page));
        subscription = photosObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            Photos photos = result.getPhotos();
                            if (photos != null) {
                                if (page == INITIAL_PAGE) {
                                    listener.onPhotoRequestComplete(photos.getPhoto());
                                } else {
                                    listener.onNextPage(photos.getPhoto());
                                }
                            } else {
                                listener.onEmptyPhotoResponse();
                            }
                        }
                );
    }

    public void displayFullPhotoViewer(String photoUrl) {
        photoViewerController.displayFullPhotoViewer(photoUrl);
    }

    public void cleanUpSubscriptions() {
        if (subscription != null) subscription.dispose();
    }

}
