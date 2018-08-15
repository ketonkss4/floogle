package com.pmd.floogle;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.pmd.floogle.presenters.SearchViewPresenter;

public class SearchActivity extends AppCompatActivity implements SearchViewPresenter.OnSearchActionListener {
    public static final String SEARCH_TEXT = "SEARCH_TEXT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);
        AutoCompleteTextView searchInputView = findViewById(R.id.searchInputView);
        View searchBtn = findViewById(R.id.search_button);
        SearchViewPresenter searchViewPresenter = new SearchViewPresenter(this, this);
        searchViewPresenter.setUpAutocompleteTextView(searchInputView);
        searchBtn.setOnClickListener(view -> {
            String searchText = searchInputView.getText().toString();
            searchViewPresenter.performSearchRequest(searchText
            );
        });
    }

    private void startSearchResultsActivity(String searchText) {
        Intent intent = new Intent(this, PhotoResultsViewActivity.class);
        intent.putExtra(SEARCH_TEXT, searchText);
        startActivity(intent);
    }

    @Override
    public void onSearchRequested(String searchText) {
        startSearchResultsActivity(searchText);
    }
}
