package petrglad.callpad;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

        this.setListAdapter(listAdapter);

        // SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Collection<String> listItems = new ArrayList<String>();
        // for (Sensor s : sm.getSensorList(Sensor.TYPE_ALL))
        // listItems.add(s.getName() + " (" + s.getType() + ")");
        // setListAdapter(new ArrayAdapter<String>(this,
        // android.R.layout.simple_list_item_1,
        // listItems.toArray(new String[listItems.size()])));

        registerForContextMenu(getListView());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.v(CallPadActivity.class.getName(), "pos=" + position + ", od=" + id
                + ", data=" + getListView().getItemAtPosition(position));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (v.getId() == android.R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            int menuPos = 0;
            CallItem callItem = (CallItem) listAdapter.getItem(info.position);
            final String phoneNo = callItem.phoneNo;
            menu.setHeaderTitle(phoneNo);
            menu.add(Menu.NONE, menuPos, menuPos, "Call").setOnMenuItemClickListener(
                    new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Log.v(getClass().getName(), "call clicked " + item.getItemId());
                            startActivity(new Intent(
                                    "android.intent.action.DIAL",
                                    Uri.fromParts("tel", phoneNo, "")));
                            return false;
                        }
                    });
            menu.add(Menu.NONE, menuPos, menuPos, "Dismiss");
        }
    }
}
