package com.pmd.floogle.Util

import android.content.Context
import android.content.Context.MODE_PRIVATE

const val AUTOCOMPLETE_PREFS = "AUTOCOMPLETE_PREFS"
const val SUGGESTIONS_KEY = "SUGGESTIONS_KEY"

fun getAutoCompleteSuggestions(context: Context): List<String> {
    val applicationContext = context.applicationContext
    val suggestions = applicationContext
            .getSharedPreferences(AUTOCOMPLETE_PREFS, MODE_PRIVATE)
            .getString(SUGGESTIONS_KEY, "")
    return suggestions.split(",")
}

fun saveAutoCompleteSuggestion(searchText: String, context: Context) {
    val applicationContext = context.applicationContext
    applicationContext.getSharedPreferences(AUTOCOMPLETE_PREFS, MODE_PRIVATE)
            .edit()
            .putString(SUGGESTIONS_KEY, searchText)
            .apply()
}
