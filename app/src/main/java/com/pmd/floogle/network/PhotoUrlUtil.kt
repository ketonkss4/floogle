package com.pmd.floogle.network

import android.util.Log


const val FLICKR_DOMAIN_PREFIX = "https://farm"
const val FLICKR_DOMAIN = ".staticflickr.com"
const val JPG = ".jpg"
const val LARGE_SQUARE_THUMBNAIL_SIZE = "n"
const val LARGE_SIZE = "b"


fun generateFlickrPhotoThumbnailUrl(id: String, farmId: String, serverId: String, secret: String): String {
    val url = "$FLICKR_DOMAIN_PREFIX$farmId$FLICKR_DOMAIN/$serverId/${id}_${secret}_$LARGE_SQUARE_THUMBNAIL_SIZE$JPG"
    Log.v("PhotoUrlUtil :", "Url = $url ")
    return url
}

fun generateFlickrPhotoUrl(id: String, farmId: String, serverId: String, secret: String): String {
    return "$FLICKR_DOMAIN_PREFIX$farmId$FLICKR_DOMAIN/$serverId/${id}_${secret}_$LARGE_SIZE$JPG"
}