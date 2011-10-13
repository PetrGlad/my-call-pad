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

public class CallPadActivity extends ListActivity {

    private ListAdapter listAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String[] projection = new String[] { CallLog.Calls._ID, CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME };
        Cursor query = this.managedQuery(CallLog.Calls.CONTENT_URI, projection, null,
                null, null);

        listAdapter = new CallListAdapter(this, query);
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
                .setTitle("Confirm call")
                .setIcon(drawable.ic_menu_call)
                .setMessage(phoneNo)
                .setPositiveButton("Call", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(getClass().getName(), "call clicked " + id + ", phoneNo " + phoneNo);
                        startActivity(new Intent(
                                Intent.ACTION_CALL,
                                Uri.fromParts("tel", phoneNo, "")));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }
}
