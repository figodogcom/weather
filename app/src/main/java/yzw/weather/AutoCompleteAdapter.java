package yzw.weather;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by yangzhiwei on 2016/11/8.
 */
public class AutoCompleteAdapter extends BaseAdapter implements Filterable{

    public Context context;

    public AutoCompleteAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 11;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(context).inflate(R.layout.bang,viewGroup); /////?????null or viewgroup
        TextView mTextView = (TextView) v.findViewById(R.id.fragment_setting_common_searchresult);
        Log.i("test","test");
        mTextView.setText("11111");
        return v;


    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
