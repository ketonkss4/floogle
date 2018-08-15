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

    private PhotoRequestListener listener;
    private PhotoViewerController photoViewerController;

    public interface PhotoRequestListener {
        void onPhotoRequestComplete(List<Photo> photo);
        void onEmptyPhotoResponse();
    }

    public interface PhotoViewerController {
        void displayFullPhotoViewer(String photoUrl);
    }
    public PhotoListViewPresenter(PhotoRequestListener listener, PhotoViewerController photoViewerController) {
        this.listener = listener;
        this.photoViewerController = photoViewerController;
    }

    public void requestPhotos(String searchText) {
        Observable<RequestResult> photosObservable = PhotosKt.requestPhotos(searchText, "1");
        Disposable subscription = photosObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            Photos photos = result.getPhotos();
                            if (photos != null) {
                                listener.onPhotoRequestComplete(photos.getPhoto());
                            }else {
                                listener.onEmptyPhotoResponse();
                            }
                        }
                );
    }

    public void displayFullPhotoViewer(String photoUrl) {
        photoViewerController.displayFullPhotoViewer(photoUrl);
    }

}
