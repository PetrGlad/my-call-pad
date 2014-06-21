package petrglad.callpad;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class CallListAdapter extends BaseAdapter {

    public static final String PREFIX_RU = "+7";
    private final List<CallItem> callList;

    private final Context context;

    public CallListAdapter(Context context, List<CallItem> data) {
        this.context = context;
        this.callList = data;
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
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, null);
            views = new ViewHolder();
            views.name = (TextView) convertView.findViewById(android.R.id.text1);
            views.phone = (TextView) convertView.findViewById(android.R.id.text2);
            convertView.setTag(views);
        } else {
            views = (ViewHolder) convertView.getTag();
        }

        final CallItem callItem = callList.get(position);
        views.name.setText(callItem.name);
        views.phone.setText(formatNumber(callItem));
        return convertView;
    }

    private String formatNumber(CallItem callItem) {
        // Only north american numbers formatting is supported by Android
        if (callItem.phoneNo.startsWith(PREFIX_RU)) {
            // The NANP is also usable for Russia, reuse it
            final SpannableStringBuilder text =
                    new SpannableStringBuilder(callItem.phoneNo.substring(PREFIX_RU.length()));
            PhoneNumberUtils.formatNanpNumber(text);
            return PREFIX_RU + " " + text.toString();
        } else {
            return PhoneNumberUtils.formatNumber(callItem.phoneNo);
        }
    }

    private static class ViewHolder {
        public TextView phone;
        public TextView name;
    }
}
