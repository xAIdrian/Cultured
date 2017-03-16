package com.androidtitan.culturedapp.common.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.util.AttributeSet;


/**
 * Created by Adrian Mohnacs on 3/1/17.
 */

public class ContinentPriorityPreference extends ListPreference implements ContinentPreferenceAdapter.RowInteractionInterface {

    private Context context = null;

    CharSequence[] entries;
    CharSequence[] entryValues;
    private ContinentPreferenceAdapter listAdapter;

    public ContinentPriorityPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
    }

    public ContinentPriorityPreference(Context context) {
        super(context);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setPositiveButton(null, null);

        entries = getEntries();
        entryValues = getEntryValues();

        if(entries == null || entryValues == null || entries.length != entryValues.length) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array which are both the same length");
        }

        listAdapter = new ContinentPreferenceAdapter(this, context, entries, entryValues);

        builder.setAdapter(listAdapter, (dialog, which) -> {

        });
    }


/*

    private ListAdapter adapter() {
        return new ArrayAdapter(getContext(), android.R.layout.select_dialog_singlechoice);
    }

    private CharSequence[] entries() {
        //todo: action to provide entry data in char sequence array for list
        return null;
    }

    private CharSequence[] entryValues() {
        //todo: action to provide value data for list
        return null;
    }
*/

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        super.onSetInitialValue(restoreValue, defaultValue);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return super.onGetDefaultValue(a, index);
    }

    @Override
    public void onRowClick(int position) {
        getDialog().dismiss();
    }
}
