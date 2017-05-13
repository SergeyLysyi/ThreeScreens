package sergeylysyi.notes.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import sergeylysyi.notes.MainActivity;
import sergeylysyi.notes.R;

public class DialogInvoker {

    private final Context context;

    public DialogInvoker(Activity context) {
        this.context = context;
    }

    public void sortDialog(MainActivity.NoteSortOrder orderPreference, MainActivity.NoteSortField fieldPreference,
                           final ResultListener listener) {
        final MainActivity.NoteSortOrder[] noteSortOrder = new MainActivity.NoteSortOrder[1];
        final MainActivity.NoteSortField[] noteSortField = new MainActivity.NoteSortField[1];
        noteSortOrder[0] = orderPreference;
        noteSortField[0] = fieldPreference;
        Dialog d = new AlertDialog.Builder(context, 0)
                .setTitle(R.string.dialog_sort_title)
                .setView(R.layout.sort_layout)
                .setNegativeButton(R.string.dialog_negative_button, null)
                .setPositiveButton(R.string.dialog_sort_positive_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SortDialogResult result = new SortDialogResult();
                                result.order = noteSortOrder[0];
                                result.field = noteSortField[0];
                                listener.onSortDialogResult(result);
                            }
                        }
                )
                .create();
        d.show();
        final RadioGroup order = ((RadioGroup) d.findViewById(R.id.sort_order_rg));
        ((RadioButton) order.getChildAt(noteSortOrder[0].ordinal())).setChecked(true);
        order.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.ascending:
                        noteSortOrder[0] = MainActivity.NoteSortOrder.ascending;
                        break;
                    case R.id.descending:
                        noteSortOrder[0] = MainActivity.NoteSortOrder.descending;
                        break;
                }
            }
        });
        final RadioGroup column = ((RadioGroup) d.findViewById(R.id.sort_column_rg));
        ((RadioButton) column.getChildAt(noteSortField[0].ordinal())).setChecked(true);
        column.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (column.getCheckedRadioButtonId()) {
                    case R.id.sort_by_title:
                        noteSortField[0] = MainActivity.NoteSortField.title;
                        break;
                    case R.id.sort_by_created:
                        noteSortField[0] = MainActivity.NoteSortField.created;
                        break;
                    case R.id.sort_by_edited:
                        noteSortField[0] = MainActivity.NoteSortField.edited;
                        break;
                    case R.id.sort_by_Viewed:
                        noteSortField[0] = MainActivity.NoteSortField.viewed;
                        break;
                }
            }
        });
    }

    public void searchDialog(final ResultListener listener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.search_layout, null);
        final Dialog d = new AlertDialog.Builder(context, 0)
                .setTitle(R.string.dialog_search_title)
                .setView(v)
                .setNegativeButton(R.string.dialog_negative_button, null)
                .setPositiveButton(R.string.dialog_search_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String patternTitle = ((EditText) v.findViewById(R.id.search_field_title))
                                .getText()
                                .toString();
                        String patternDescription = ((EditText) v.findViewById(R.id.search_field_description))
                                .getText()
                                .toString();
                        if (patternTitle.length() + patternDescription.length() > 0) {
                            SearchDialogResult result = new SearchDialogResult();
                            result.title = patternTitle;
                            result.description = patternDescription;
                            listener.onSearchDialogResult(result);
                        }
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        listener.onSortCancel();
                    }
                })
                .create();
        d.show();
    }

    public void filterDialog(final ResultListener listener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.filter_layout, null);
        final MainActivity.NoteDateField[] dateField = new MainActivity.NoteDateField[1];
        final GregorianCalendar[] after = new GregorianCalendar[1];
        final GregorianCalendar[] before = new GregorianCalendar[1];
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        dateField[0] = MainActivity.NoteDateField.created;
        after[0] = null;
        before[0] = null;
        final Dialog d = new AlertDialog.Builder(context, 0)
                .setTitle(R.string.dialog_filter_title)
                .setView(v)
                .setNegativeButton(R.string.dialog_negative_button, null)
                .setPositiveButton(R.string.dialog_filter_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FilterDialogResult result = new FilterDialogResult();
                        result.dateField = dateField[0];
                        result.after = after[0];
                        result.before = before[0];
                        listener.onFilterDialogResult(result);
                    }
                })
                .create();
        d.show();
        final TextView tv = (TextView) v.findViewById(R.id.column_filter);
        final String[] fields = new String[]{"Created", "Edited", "Viewed"};
        tv.setText(fields[dateField[0].ordinal()]);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context, 0)
                        .setTitle(R.string.dialog_pick_time_title)
                        .setItems(fields, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dateField[0] = MainActivity.NoteDateField.values()[which];
                                tv.setText(fields[which]);
                            }
                        })
                        .setNegativeButton(R.string.dialog_negative_button, null)
                        .create()
                        .show();
            }
        });

        v.findViewById(R.id.time_after_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimePicker tp = new TimePicker(context);
                //TODO:set hour, minute
                final TextView tv = (TextView) v;
                new AlertDialog.Builder(context, 0)
                        .setTitle(R.string.dialog_pick_time_title)
                        .setView(tp)
                        .setNegativeButton(R.string.dialog_negative_button, null)
                        .setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (after[0] == null) {
                                    after[0] = new GregorianCalendar();
                                }
                                after[0].set(GregorianCalendar.HOUR, tp.getCurrentHour());
                                after[0].set(GregorianCalendar.MINUTE, tp.getCurrentMinute());
                                tv.setText(timeFormat.format(after[0].getTime()));
                            }
                        })
                        .create()
                        .show();
            }
        });
        v.findViewById(R.id.date_after_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePicker dp = new DatePicker(context);
                //TODO: set date
                final TextView tv = (TextView) v;
                new AlertDialog.Builder(context, 0)
                        .setTitle(R.string.dialog_pick_date)
                        .setView(dp)
                        .setNegativeButton(R.string.dialog_negative_button, null)
                        .setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (after[0] == null) {
                                    after[0] = new GregorianCalendar();
                                }
                                after[0].set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
                                tv.setText(dateFormat.format(after[0].getTime()));
                            }
                        })
                        .create()
                        .show();
            }
        });

        v.findViewById(R.id.time_before_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimePicker tp = new TimePicker(context);
                //TODO:set hour, minute
                final TextView tv = (TextView) v;
                new AlertDialog.Builder(context, 0)
                        .setTitle(R.string.dialog_pick_time_title)
                        .setView(tp)
                        .setNegativeButton(R.string.dialog_negative_button, null)
                        .setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (before[0] == null) {
                                    before[0] = new GregorianCalendar();
                                }
                                before[0].set(GregorianCalendar.HOUR, tp.getCurrentHour());
                                before[0].set(GregorianCalendar.MINUTE, tp.getCurrentMinute());
                                tv.setText(timeFormat.format(before[0].getTime()));
                            }
                        })
                        .create()
                        .show();
            }
        });
        v.findViewById(R.id.date_before_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePicker dp = new DatePicker(context);
                //TODO: set date
                final TextView tv = (TextView) v;
                new AlertDialog.Builder(context, 0)
                        .setTitle(R.string.dialog_pick_date)
                        .setView(dp)
                        .setNegativeButton(R.string.dialog_negative_button, null)
                        .setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (before[0] == null) {
                                    before[0] = new GregorianCalendar();
                                }
                                before[0].set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
                                tv.setText(dateFormat.format(before[0].getTime()));
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    public void manageFiltersDialog(final String[] entriesNames, final ManageFiltersResultListener listener) {
        final Set<Integer> deletedFilters = new HashSet<>();
        final boolean[] edited = new boolean[1];
        final AlertDialog d = new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_manage_filters_title)
                .setNegativeButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (edited[0] || !deletedFilters.isEmpty()) {
                            int[] deleted = new int[deletedFilters.size()];
                            int i = 0;
                            for (Integer index : deletedFilters) {
                                deleted[i] = index;
                                i++;
                            }
                            listener.onEditFilterEntries(deleted);
                        }
                    }
                })
                .setNeutralButton(R.string.dialog_manage_filters_add, null)
                .setItems(entriesNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onApplyFilterEntry(entriesNames[which]);
                    }
                })
                .create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                d.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new InputDialog(context, entriesNames, new Callback() {
                                    @Override
                                    public void call(Object result) {
                                        d.dismiss();
                                        listener.onAddFilterEntry((String) result);
                                    }
                                }).show();
                            }
                        });

                ListView lv = d.getListView();
                lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, final long id) {
                        PopupMenu menu = new PopupMenu(context, view);
                        if (!deletedFilters.contains(position)) {
                            menu.getMenuInflater().inflate(R.menu.dialog_filter_item_actions_menu, menu.getMenu());
                            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.menu_delete:
                                            System.out.println("delete");
                                            ((TextView) view).setText(
                                                    String.format("%s %s",
                                                            context.getString(R.string.dialog_manage_filter_deleted_prefix),
                                                            ((TextView) view).getText()));
                                            view.setAlpha(view.getAlpha() * 0.5f);
                                            deletedFilters.add(position);
                                            return true;
                                        case R.id.menu_edit:
                                            System.out.println("edit");
                                            String[] forbidden = new String[entriesNames.length - 1];
                                            System.arraycopy(entriesNames, 0, forbidden, 0, position);
                                            System.arraycopy(entriesNames, position + 1, forbidden, position, entriesNames.length - position);
                                            new InputDialog(context, forbidden, new Callback() {
                                                @Override
                                                public void call(Object result) {
                                                    String name = (String) result;
                                                    if (!entriesNames[position].equals(name)) {
                                                        edited[0] = true;
                                                        entriesNames[position] = name;
                                                    }
                                                }
                                            }).show();
                                            return true;
                                    }
                                    System.out.println("not handled");
                                    return false;
                                }
                            });
                        } else {
                            menu.getMenuInflater().inflate(R.menu.dialog_filter_deleted_item_actions, menu.getMenu());
                            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.menu_restore:
                                            System.out.println("restore");
                                            ((TextView) view).setText(entriesNames[position]);
                                            view.setAlpha(view.getAlpha() * 1/0.5f);
                                            deletedFilters.remove(position);
                                            return true;
                                    }
                                    System.out.println("not handled");
                                    return false;
                                }
                            });
                        }
                        menu.show();
                        return true;
                    }
                });
            }
        });
        d.show();
    }

    public interface SortResultListener {
        void onSortDialogResult(SortDialogResult result);

        void onSortCancel();
    }

    public interface FilterResultListener {
        void onFilterDialogResult(FilterDialogResult result);
    }

    public interface SearchResultListener {
        void onSearchDialogResult(SearchDialogResult result);
    }

    public interface ManageFiltersResultListener {
        void onEditFilterEntries(int[] deletedEntriesIndexes);

        void onAddFilterEntry(String newEntryName);

        void onApplyFilterEntry(String entryName);
    }

    public interface ResultListener extends
            SortResultListener,
            FilterResultListener,
            SearchResultListener,
            ManageFiltersResultListener {
    }

    public static class SortDialogResult {
        public MainActivity.NoteSortOrder order;
        public MainActivity.NoteSortField field;
    }

    public static class FilterDialogResult {
        public MainActivity.NoteDateField dateField;
        public GregorianCalendar after;
        public GregorianCalendar before;
    }

    public static class SearchDialogResult {
        public String title;
        public String description;
    }
}
