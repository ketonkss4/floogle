package com.pmd.floogle.models

import com.pmd.floogle.network.MainService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class Photos(val page: String,
             val photo: List<Photo>)

class Photo(val id: String,
            val title: String,
            val secret: String,
            val server: String,
            val farm: String
            )

class RequestResult(val photos: Photos?)

fun requestPhotos(searchTags:String, page: String) : Observable<RequestResult> {
    return MainService().getPhotosBySearchTags(searchTags, page).subscribeOn(Schedulers.io())
}
