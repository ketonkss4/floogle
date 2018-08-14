package com.pmd.floogle.models

import com.pmd.floogle.network.MainService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class Photos(val page: String,
             val photos: List<Photo>)

class Photo(val id: String,
            val title: String)

fun requestPhotos(searchTags:String, page: String) : Observable<Photos> {
    return MainService().getPhotosBySearchTags(searchTags, page).subscribeOn(Schedulers.io())
}