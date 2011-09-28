package petrglad.callpad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CallListAdapter extends BaseAdapter {

    private final List<CallItem> callList;

    private final Context context;

    public CallListAdapter(Context context, Cursor data) {
        this.context = context;
        this.callList = filterCalls(data);
    }

    private List<CallItem> filterCalls(Cursor data) {
        // Oh, Java, you're so painfully verbose.

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

    @Override
    public int getCount() {
        return callList.size();
    }

    @Override
    public Object getItem(int location) {
        return callList.get(location);
    }

    @Override
    public long getItemId(int itemId) {
        return itemId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder views;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2,
                    null);
            views = new ViewHolder();
            views.name = (TextView) convertView.findViewById(android.R.id.text1);
            views.phone = (TextView) convertView.findViewById(android.R.id.text2);
            convertView.setTag(views);
        } else
            views = (ViewHolder) convertView.getTag();

        final CallItem callItem = callList.get(position);
        views.name.setText(callItem.name);
        views.phone.setText(callItem.phoneNo);

        return convertView;
    }

    private static class ViewHolder {
        public TextView phone;
        public TextView name;
    }
}
