package com.pmd.floogle.presenters;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.pmd.floogle.R;

import java.util.ArrayList;
import java.util.List;

import static com.pmd.floogle.Util.AutoCompleteSuggestionsUtilKt.getAutoCompleteSuggestions;
import static com.pmd.floogle.Util.AutoCompleteSuggestionsUtilKt.saveAutoCompleteSuggestion;

public class SearchViewPresenter {
    private final OnSearchActionListener searchActionListener;
    private List<String> suggestions = new ArrayList<>();
    private final Context context;

    public interface OnSearchActionListener {
        void onSearchRequested(String searchText);
    }

    public SearchViewPresenter(Context context, OnSearchActionListener searchActionListener) {
        this.searchActionListener = searchActionListener;
        this.context = context.getApplicationContext();
    }

    public void setUpAutocompleteTextView(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setImeActionLabel(context.getString(R.string.search_btn_label), KeyEvent.KEYCODE_ENTER);
        setAutoCompleteSuggestions(autoCompleteTextView);
        setEnterButtonListener(autoCompleteTextView);
        setSelectionClickListener(autoCompleteTextView);
    }

    private void setSelectionClickListener(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setOnItemClickListener((adapterView, view, pos, l) -> {
                    String searchText = adapterView.getItemAtPosition(pos).toString();
                    dismissKeyboard(autoCompleteTextView);
                    performSearchRequest(searchText);
                }
        );
    }

    private void setAutoCompleteSuggestions(AutoCompleteTextView autoCompleteTextView) {
        suggestions = getAutoCompleteSuggestions(context);
        if (!suggestions.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                    android.R.layout.select_dialog_item,
                    suggestions);
            autoCompleteTextView.setAdapter(adapter);
        }
    }

    private void setEnterButtonListener(AutoCompleteTextView searchInputView) {
        searchInputView.setOnEditorActionListener((textView, keyCode, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String searchText = textView.getText().toString();
                dismissKeyboard(searchInputView);
                performSearchRequest(searchText);
                return true;
            }
            return false;
        });
    }

    public void performSearchRequest(String searchText) {
        saveAutoCompleteSuggestion(createNewSuggestionList(searchText.trim()), context);
        searchActionListener.onSearchRequested(searchText.trim());
    }

    private String createNewSuggestionList(String searchText) {
        StringBuilder suggestionStringListBuilder = new StringBuilder();
        Boolean isDuplicate = false;
        for (String suggestion : suggestions) {
            if (searchText.equalsIgnoreCase(suggestion)) isDuplicate = true;
            suggestionStringListBuilder.append(",").append(suggestion);
        }
        if (!isDuplicate) suggestionStringListBuilder.append(",").append(searchText);
        return suggestionStringListBuilder.toString();
    }

    private void dismissKeyboard(EditText editText) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}


