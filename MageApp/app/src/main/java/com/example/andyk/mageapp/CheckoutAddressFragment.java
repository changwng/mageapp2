package com.example.andyk.mageapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.andyk.mageapp.form.Form;
import com.example.andyk.mageapp.form.FormField;
import com.example.andyk.mageapp.form.FormFieldValue;
import com.example.andyk.mageapp.helper.Helper;
import com.example.andyk.mageapp.xmlconnect.ResponseMessage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutAddressFragment extends Fragment {

    protected static final String TAG = "CheckoutAddressFragment";
    protected static final String STATE_FORM = "addressForm";
    protected Form mForm;
    protected LinearLayout mAddressForm;
    protected Button mBtSave;
    protected Set<String> mFormValueRelations = new HashSet<>();
    protected Map<String, String> mPostData;
    protected ResponseMessage mFormSaveRespMsg;
    protected boolean mIsFormValid;

    public CheckoutAddressFragment() {
        // Required empty public constructor
    }

    /**
     * todo - make form related classes as parcellable so we can store the mAddressForm object as the bundle item
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_FORM, mForm);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mForm = savedInstanceState.getParcelable(STATE_FORM);
            updateUI();
        }
    }

    protected void updateUI() {
        List<FormField> fields = mForm.getFields();
        int numFields = fields.size();
        for (int i = 0; i < numFields; i++) {
            FormField field = fields.get(i);
            switch (field.getType()) {
                case FormField.TYPE_CHECKBOX:
                    renderCheckbox(field);
                    break;
                case FormField.TYPE_SELECT:
                    renderSpinner(field);
                    break;
                case FormField.TYPE_TEXT:
                    renderEditText(field);
                    break;
            }
        }
    }

    protected void renderCheckbox(FormField field) {
        LinearLayout container = new LinearLayout(getContext());
        CheckBox cbox = new CheckBox(getContext());
        cbox.setTag(field);
        int viewId = this.getViewIdByField(field);
        cbox.setId(viewId);
        TextView label = new TextView(getContext());
        label.setText(field.getLabel());
        container.addView(cbox);
        container.addView(label);
        mAddressForm.addView(container);
    }

    protected void renderEditText(FormField field) {
        EditText view = new EditText(getContext());
        view.setHint(field.getLabel());
        view.setTag(field);
        int viewId = this.getViewIdByField(field);
        view.setId(viewId);
        mAddressForm.addView(view);
    }

    protected void renderSpinner(FormField field) {
        Spinner view = new Spinner(getContext());
        view.setTag(field);
        int viewId = this.getViewIdByField(field);
        view.setId(viewId);
        List<FormFieldValue> vals = field.getValues();
        final ArrayAdapter adapter = new DropDownAdapter(getContext(), vals);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        view.setAdapter(adapter);
        view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FormFieldValue item = (FormFieldValue) parent.getItemAtPosition(position);
                String relation = item.getRelation();
                if (relation == null) return;
                int numChildren = mAddressForm.getChildCount();
                for (int i = 0; i < numChildren; i++) {
                    View elem = mAddressForm.getChildAt(i);
                    if (elem.getTag() != null) {
                        FormField field = (FormField) elem.getTag();
                        String fieldId = field.getId();
                        if (mFormValueRelations.contains(fieldId)) {
                            elem.setVisibility(View.GONE);
                        }
                        if (relation.equals(fieldId)) {
                            elem.setVisibility(View.VISIBLE);
                            if (elem instanceof Spinner) {
                                DropDownAdapter adapter =
                                        new DropDownAdapter(getContext(), item.getItems());
                                ((Spinner) elem).setAdapter(adapter);
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAddressForm.addView(view);
    }

    protected void prepareFormPostData() {
        int cnt = mAddressForm.getChildCount();
        int i = 0;
        while ((i < cnt) && mIsFormValid) {
            View view = mAddressForm.getChildAt(i);
            FormField field = (FormField) view.getTag();
            if (field != null) {
                String key, val;
                switch (field.getType()) {
                    case FormField.TYPE_CHECKBOX:
                        CheckBox cbox = (CheckBox) view;
                        key = field.getName();
                        val = cbox.isChecked() ? "1" : "0";
                        this.validateField(field, val);
                        mPostData.put(key, val);
                        break;
                    case FormField.TYPE_SELECT:
                        Spinner dropdown = (Spinner) view;
                        key = field.getName();
                        FormFieldValue item = (FormFieldValue) dropdown.getSelectedItem();
                        if (item != null) {
                            val = item.getValue();
                            this.validateField(field, val);
                            mPostData.put(key, val);
                        }
                        break;
                    case FormField.TYPE_TEXT:
                        EditText etxt = (EditText) view;
                        key = field.getName();
                        val = etxt.getText().toString();
                        this.validateField(field, val);
                        mPostData.put(key, val);
                        break;
                }
            }
            i++;
        }
    }

    protected void validateField(FormField field, String val) {
        if (field.isRequred() && TextUtils.isEmpty(val)) {
            String msg = "Please enter " + field.getLabel().toLowerCase();
            this.openAlertDialog(msg);
            mIsFormValid = false;
        } else {
            mIsFormValid = true;
        }
    }

    protected void openAlertDialog(String msg) {
        Resources res = this.getResources();
        String title = res.getString(R.string.alert);
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private class DropDownAdapter extends ArrayAdapter<FormFieldValue> {
        protected List<FormFieldValue> mItems;

        public DropDownAdapter(Context context, List<FormFieldValue> items) {
            super(context, 0, items);
            mItems = items;
        }

        @Override
        public
        @NonNull
        View getView(int position, @Nullable View convertView,
                     @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.spinner_item, null);
            }
            TextView tvLabel = convertView.findViewById(R.id.spinner_label);
            FormFieldValue item = mItems.get(position);
            String label = item.getLabel();
            tvLabel.setText(label);
            String relation = item.getRelation();
            mFormValueRelations.add(relation);
            return convertView;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView,
                                    @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.spinner_dropdown_item, parent, false);
            }
            TextView tvLabel = convertView.findViewById(R.id.spinner_label);
            String label = mItems.get(position).getLabel();
            tvLabel.setText(label);
            return convertView;
        }
    }

    protected int getViewIdByField(FormField field) {
        switch (field.getId()) {
            case "firstname":
                return R.id.firstname;
            case "lastname":
                return R.id.lastname;
            case "company":
                return R.id.company;
            case "email":
                return R.id.email;
            case "street":
                return R.id.street;
            case "street_2":
                return R.id.street_2;
            case "city":
                return R.id.city;
            case "country_id":
                return R.id.country_id;
            case "region":
                return R.id.region;
            case "region_id":
                return R.id.region_id;
            case "postcode":
                return R.id.postcode;
            case "telephone":
                return R.id.telephone;
            case "fax":
                return R.id.fax;
            case "save_in_address_book":
                return R.id.save_in_address_book;
            default:
                return Helper.generateViewId();
        }
    }
}
