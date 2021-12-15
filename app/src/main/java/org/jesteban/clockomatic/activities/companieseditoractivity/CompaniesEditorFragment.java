package org.jesteban.clockomatic.activities.companieseditoractivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.helpers.UndoMessage;
import org.jesteban.clockomatic.model.Company;
import org.jesteban.clockomatic.model.UndoAction;

// A good tutorial to listview: https://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/

// In future will be great that UI seems gmail
// https://www.androidhive.info/2017/02/android-creating-gmail-like-inbox-using-recyclerview/

public class CompaniesEditorFragment extends Fragment implements CompaniesEditorContract.View {
    ListView listView = null;
    CompaniesEditorContract.Presenter presenter = null;
    CompaniesListAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.companies_editor_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.list_companies_editor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.clickOnListItem(position);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.clickOnListItem(position);
                return true;
            }
        });
        adapter = new CompaniesListAdapter();
        listView.setAdapter(adapter);
        FloatingActionButton floating = (FloatingActionButton) view.findViewById(R.id.add_new_company_button);
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clickOnAddCompany();
            }
        });
        getActivity().setResult(Activity.RESULT_OK);
        return view;
    }

    @Override
    public void showMessage(String text) {
        Snackbar.make(this.getView(), text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void showUndoMessage(final UndoAction undoAction) {
        UndoMessage.showUndoMessage(undoAction,getView(),getResources());
    }
    private int getColorForCompany(Company c){
        if (c.isEnabled())  return c.getColor();
                else{
                    return (c.getColor() & 0xff8f8f8f);
        }
    }
    @Override
    public void showEditDialog(final int position, int titleId,Company company, Boolean isAdd, Boolean canRemove) {
        final Dialog dialog = new Dialog(getActivity());
        final Company companyEditor = company;
        dialog.setContentView(R.layout.companies_editor_edit_dialog);
        dialog.setTitle(titleId);
        EditText name = (EditText) dialog.findViewById(R.id.edit_text_company_name);
        EditText desc = (EditText) dialog.findViewById(R.id.edit_text_company_desc);
        final Button colorButton = (Button) dialog.findViewById(R.id.edit_text_company_color);
        if (isAdd) {
            name.setHint(companyEditor.getName());
            name.setText("");
            desc.setHint(companyEditor.getDescription());
            desc.setText("");
        } else {
            name.setText(companyEditor.getName());
            desc.setText(companyEditor.getDescription());
        }
        RelativeLayout mainLayout = (RelativeLayout) dialog.findViewById(R.id.dialog_edit_company_main_layout);
        final int currentColor = getColorForCompany(companyEditor);
        colorButton.setBackgroundColor(currentColor);
        colorButton.setTag(currentColor);


        ImageButton removeButton = (ImageButton) dialog.findViewById(R.id.button_edit_company_remove);
        removeButton.setImageResource(R.drawable.remove);
        if (canRemove) removeButton.setVisibility(View.VISIBLE);
        else removeButton.setVisibility(View.GONE);

        Button okButton = (Button) dialog.findViewById(R.id.button_edit_company_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) dialog.findViewById(R.id.edit_text_company_name);
                EditText desc = (EditText) dialog.findViewById(R.id.edit_text_company_desc);
                Button colorButton = (Button) dialog.findViewById(R.id.edit_text_company_color);
                companyEditor.setName(name.getText().toString());
                companyEditor.setDescription(desc.getText().toString());
                companyEditor.setColor((int)colorButton.getTag());
                if (presenter.clickOnEditDialogOk(position, companyEditor)) {
                    dialog.dismiss();
                }
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clickOnEditDialogRemoveCompany(position);
                dialog.dismiss();
            }
        });

        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(getActivity())
                        .setTitle("Choose color")
                        .initialColor(currentColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {

                                colorButton.setBackgroundColor(selectedColor);
                                colorButton.setTag(selectedColor);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }

        });



        dialog.show();

    }

    @Override
    public void showError(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(R.string.error_title);
        alertDialogBuilder.setMessage(msg);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void setPresenter(CompaniesEditorContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public CompaniesEditorContract.Presenter getPresenter() {
        return this.presenter;
    }

    @Override
    public void refreshCompaniesList() {
        adapter.notifyDataSetChanged();
    }

    public class CompaniesListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return presenter.getCompaniesSize();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                view = inflater.inflate(R.layout.companies_editor_list_row, null);
            }
            Company c = presenter.getCompany(position);
            TextView companyName = (TextView) view.findViewById(R.id.company_name);
            TextView companyDescription = (TextView) view.findViewById(R.id.company_description);
            TextView numEntries = (TextView) view.findViewById(R.id.num_entries);
            long num = presenter.getCompanyNumEntries(position);
            numEntries.setText(Long.toString(num));
            CheckBox checkEnable = (CheckBox) view.findViewById(R.id.check_box_enable);
            checkEnable.setTag(position);
            checkEnable.setChecked(c.isEnabled());
            checkEnable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox check = (CheckBox) v;
                    presenter.clickOnDisableCompany((int) check.getTag(), check.isChecked());
                }
            });

            ImageButton editButton = (ImageButton) view.findViewById(R.id.edit_button);
            editButton.setTag(position);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton editButton = (ImageButton) v;
                    presenter.clickOnListItem((int) editButton.getTag());
                }
            });
            companyName.setText(c.getName());
            companyDescription.setText(c.getDescription());
            view.setBackgroundColor(getColorForCompany(c));
            return view;
        }
    }
}
