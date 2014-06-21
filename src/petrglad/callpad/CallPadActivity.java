package petrglad.callpad;

import android.R.drawable;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CallPadActivity extends ListActivity {

    public static final String TAG = CallPadActivity.class.getName();
    public static final long CALL_LOG_WINDOW_MILLIS = TimeUnit.DAYS.toMillis(120);
    private ListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final String[] projection = new String[]{
                CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME, CallLog.Calls.DATE};
        final Cursor cursor = this.managedQuery(
                CallLog.Calls.CONTENT_URI, projection,
                "date > ?",
                new String[]{String.valueOf(System.currentTimeMillis() - CALL_LOG_WINDOW_MILLIS)},
                CallLog.Calls.DATE + " desc");
        listAdapter = new CallListAdapter(this, filterCalls(cursor));
        setListAdapter(listAdapter);
        registerForContextMenu(getListView());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, final long id) {
        Log.v(CallPadActivity.class.getName(), "pos=" + position + ", od=" + id
                + ", data=" + getListView().getItemAtPosition(position));
        final CallItem callItem = (CallItem) l.getAdapter().getItem(position);
        final String phoneNo = callItem.phoneNo;
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_call)
                .setIcon(drawable.ic_menu_call)
                .setMessage(phoneNo)
                .setPositiveButton("Call", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(TAG, "call clicked " + id + ", phoneNo " + phoneNo);
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNo, "")));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    private List<CallItem> filterCalls(Cursor data) {
        // TODO Rewrite data query as pure SQL select

        final Map<String, Integer> numbers = new HashMap<String, Integer>();
        final List<CallItem> result = new ArrayList<CallItem>(20);
        boolean hasMore = data.moveToFirst();
        while (hasMore) {
            final String number = data.getString(data.getColumnIndex(CallLog.Calls.NUMBER));
            final String name = data.getString(data.getColumnIndex(CallLog.Calls.CACHED_NAME));
            final Integer cnt = numbers.get(number);
            numbers.put(number, cnt == null ? 1 : cnt + 1);
            if (cnt == null)
                result.add(new CallItem(name, number));
            hasMore = data.moveToNext();
        }
        Collections.sort(result, new Comparator<CallItem>() {
            @Override
            public int compare(CallItem a, CallItem b) {
                return numbers.get(b.phoneNo) - numbers.get(a.phoneNo);
            }
        });
        return result;
    }
}
