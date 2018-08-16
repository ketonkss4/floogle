package com.pmd.floogle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.pmd.floogle.presenters.PhotoListViewPresenter;
import com.pmd.floogle.presenters.SearchViewPresenter;
import com.squareup.picasso.Picasso;

import static com.pmd.floogle.SearchActivity.SEARCH_TEXT;

/**
 * This activity houses both a search feature and a fragment with list of results
 */
public class PhotoResultsViewActivity extends AppCompatActivity implements SearchViewPresenter.OnSearchActionListener, PhotoListViewPresenter.PhotoViewerController {

    private PhotoListViewPresenter photoListViewPresenter;
    private View photoViewer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_results);
        AutoCompleteTextView searchBox = findViewById(R.id.searchBox);
        View upButton = findViewById(R.id.upButton);
        upButton.setOnClickListener(view -> onBackPressed());

        photoViewer = findViewById(R.id.photo_viewer_container);
        photoViewer.setOnClickListener(view -> view.setVisibility(View.GONE));

        SearchViewPresenter searchViewPresenter = new SearchViewPresenter(this, this);
        searchViewPresenter.setUpAutocompleteTextView(searchBox);

        if (getIntent().hasExtra(SEARCH_TEXT)) {
            populateSearchViewWithCurrentSearch(searchBox);
        }
        loadResultsFragment();

    }

    /**
     * Loads PhotoListFragment to display list of photos based on search text
     */
    private void loadResultsFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_TEXT, getIntent().getStringExtra(SEARCH_TEXT));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (currentFragment == null) {
            PhotoListFragment fragment = new PhotoListFragment();
            photoListViewPresenter = new PhotoListViewPresenter(this);
            photoListViewPresenter.setListener(fragment);
            fragment.setPresenter(photoListViewPresenter);
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.fragmentContainer, fragment).commit();
        } else {
            photoListViewPresenter = ((PhotoListFragment) currentFragment).getPresenter();
        }
    }

    /**
     * Adds searched text from previous screen to search box
     *
     * @param searchBox view for additional user searches
     */
    private void populateSearchViewWithCurrentSearch(AutoCompleteTextView searchBox) {
        if (getIntent().hasExtra(SEARCH_TEXT)) {
            String searchText = getIntent().getStringExtra(SEARCH_TEXT);
            searchBox.setText(searchText);
            searchBox.dismissDropDown();
        }
    }

    /**
     * Invoked when new search is made via the search box view
     * in the PhotoResultsViewActivity
     *
     * @param searchText the target search text
     */
    @Override
    public void onSearchRequested(String searchText) {
        photoListViewPresenter.requestNewPhotoSearch(searchText);
    }

    /**
     * Invoked when user selects on item in the photo list grid
     *
     * @param photoUrl url of the selected image
     */
    @Override
    public void displayFullPhotoViewer(String photoUrl) {
        ImageView photoImageView = findViewById(R.id.imageView);
        Picasso.with(this)
                .load(photoUrl)
                .into(photoImageView);
        photoViewer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        photoListViewPresenter.cleanUpSubscriptions();
        super.onDestroy();
    }
}
