package yzw.weather;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import yzw.weather.db.MyDatabaseHelper;
import yzw.weather.fragment.SearchFragment;
import yzw.weather.model.City;
import yzw.weather.model.Oneday;

/**
 * Created by yangzhiwei on 2016/8/25.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.BaseViewHolder>  {

    private CollectionAdapterCallback mCallbacks;
    private Context context;
    private CollectionAdaptertosearch sCallbacks;
    //    private Map<Integer, String> cityMap;
//    private List<Oneday> citys;
    private Cursor cursor;
    private Cursor cursor2;


//    private Oneday od;
//    public static int cityid;
//    private View e;
//    private Object citys = new Object();


    //    private static Fragment fragment;
//    public MyAdapter(Fragment fragment){
//        this.fragment = fragment;
//    }
    public CollectionAdapter(CollectionAdapterCallback callback, /*Map<Integer, String> cityMap, */Cursor cursor, Cursor cursor2, Context context) {
        this.mCallbacks = callback;
//        this.cityMap = cityMap;
        this.cursor = cursor;
        this.context = context;
        this.cursor2 = cursor2;
    }

    public void changeData(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public Cursor getData() {
        return this.cursor;
    }



    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolderCollection extends BaseViewHolder {

        public final TextView collection_textView;
        public final TextView textview_main_temp;
        public final TextView textview_weather_description;
        public final TextView textview_main_temp_max;
        public final TextView textview_main_temp_min;
        public final TextView textview_wind_deg;
        public final TextView textview_main_humidity;
        public final TextView textview_wind_speed;
        public final TextView textView_temperture;

        public ViewHolderCollection(View v) {
            super(v);
            collection_textView = (TextView) v.findViewById(R.id.collection_textView);
            textview_main_temp = (TextView) v.findViewById(R.id.collection_textView2);
            textview_weather_description = (TextView) v.findViewById(R.id.collection_textView3);
            textview_main_temp_max = (TextView) v.findViewById(R.id.collection_textView4);
            textview_main_temp_min = (TextView) v.findViewById(R.id.collection_textView5);
            textview_wind_deg = (TextView) v.findViewById(R.id.collection_textView6);
            textview_main_humidity = (TextView) v.findViewById(R.id.collection_textView7);
            textview_wind_speed = (TextView) v.findViewById(R.id.collection_textView8);
            textView_temperture = (TextView) v.findViewById(R.id.collection_temperturemode);

        }
    }

    public class ViewHolderAdd extends BaseViewHolder {

        public ViewHolderAdd(View itemView) {
            super(itemView);

        }
    }

    public class ViewHolderText extends BaseViewHolder {
        public final TextView textview_text;

        public ViewHolderText(View itemView) {
            super(itemView);
            textview_text = (TextView) itemView.findViewById(R.id.collection_text);
        }
    }

    private static final int VIEW_TYPE_COLLECTION = 1;
    private static final int VIEW_TYPE_ADD = 2;
    private static final int VIEW_TYPE_TEXT = 3;

    @Override
    public int getItemViewType(int position) {
//        if(position == cursor.getCount()){
//            return VIEW_TYPE_ADD;
//        }else return VIEW_TYPE_COLLECTION;
        if (position == 0 || position == 2)
            return VIEW_TYPE_TEXT;
        else if (position == 1)
            return VIEW_TYPE_COLLECTION;
        else if (position == cursor.getCount() + 3)
            return VIEW_TYPE_ADD;
        else return VIEW_TYPE_COLLECTION;


//        return super.getItemViewType(position);
    }

    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;

        if (viewType == VIEW_TYPE_COLLECTION) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.fragment_collection_view, viewGroup, false);
//            final View child = LayoutInflater.from(getContext()).einflate(R.layout.sevenday, scrollContntLayout, false);
//            if(i == 0)
//                child.setBackgroundResource(R.drawable.boder);


            return new ViewHolderCollection(v);
        } else if (viewType == VIEW_TYPE_ADD) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.fragment_collection_addview, viewGroup, false);
            return new ViewHolderAdd(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.fragment_collection_text, viewGroup, false);
            return new ViewHolderText(v);
        }


    }


    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, final int position) {
        SharedPreferences preferences = context.getSharedPreferences("weather", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


//        final Oneday od = citys.get(position);

//        viewHolder.collection_textView.setText(String.valueOf(cityMap.get(od.id)));

        Log.i("position=", String.valueOf(position));

//        int section = getSection(position);
//
//        if (section == 0) {
//            int subPosition = getSubPosition(position);
//
//            if (subPosition == 0) {
//
//            } else if (subPosition == 1) {
//
//            }
//        } else if (section == 1) {
//            int subPosition = getSubPosition(position);
//
//            if (subPosition == 0) {
//
//            } else if (subPosition > 0 && cursor) {
//
//            } else {
//
//            }
//        }


        if (position == 0) {
            ViewHolderText viewHolderText = (ViewHolderText) viewHolder;
            viewHolderText.textview_text.setText("启动位置");

        } else if (position == 1) {
            cursor2.moveToFirst();
            final long _id = cursor2.getLong(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_ID);
            final String name = cursor2.getString(City.COLUMN_INDEX_NAME);
            String weather_description = cursor2.getString(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_WEATHER_DESCRIPTION);
            float main_temp = cursor2.getInt(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_MAIN_TEMP);
            float main_temp_min = cursor2.getFloat(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_MAIN_TEMP_MIN);
            float main_temp_max = cursor2.getFloat(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_MAIN_TEMP_MAX);
            float wind_deg = cursor2.getFloat(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_WIND_DEG);
            float wind_speed = cursor2.getFloat(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_WIND_SPEED);
            int main_humidity = cursor2.getInt(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_MAIN_HUMIDITY);

//                Log.i("SSS",weather_description);


            ViewHolderCollection viewHolder_collection = (ViewHolderCollection) viewHolder;
            viewHolder_collection.collection_textView.setText(name);
            viewHolder_collection.textview_main_temp.setText(String.valueOf((int) main_temp));
            viewHolder_collection.textview_weather_description.setText(weather_description);
            viewHolder_collection.textview_main_temp_max.setText((int) main_temp_max + "°");
            viewHolder_collection.textview_main_temp_min.setText((int) main_temp_min + "°");
            viewHolder_collection.textview_wind_deg.setText(getDegDesc(wind_deg));

            viewHolder_collection.textview_main_humidity.setText(main_humidity + "%");


            if (preferences.getString("tempertruemode", null).equals("sheshi")){
                viewHolder_collection.textView_temperture.setText("°C");
                viewHolder_collection.textview_wind_speed.setText((int) wind_speed + "级");
            }


            else{
                viewHolder_collection.textView_temperture.setText("°F");
                viewHolder_collection.textview_wind_speed.setText((int) wind_speed + "里/时");
            }


            viewHolder_collection.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("SSSS", String.valueOf(_id));
                    mCallbacks.onCollectionAdapterItemSelected(_id, name);
                }
            });


        } else if (position == 2) {
            ViewHolderText viewHolderText = (ViewHolderText) viewHolder;
            viewHolderText.textview_text.setText("最喜欢的地方");
        } else if (position == 3 + cursor.getCount()) {
            ViewHolderAdd viewHolder_add = (ViewHolderAdd) viewHolder;
            viewHolder_add.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SearchActivity.class);
                    context.startActivity(intent);


                }
            });
        } else {
            Log.i("BBC", String.valueOf(position));
            cursor.moveToPosition(position - 3);

            final long _id = cursor.getLong(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_ID);
            final String name = cursor.getString(City.COLUMN_INDEX_NAME);
            String weather_description = cursor.getString(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_WEATHER_DESCRIPTION);
            float main_temp = cursor.getInt(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_MAIN_TEMP);
            float main_temp_min = cursor.getFloat(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_MAIN_TEMP_MIN);
            float main_temp_max = cursor.getFloat(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_MAIN_TEMP_MAX);
            float wind_deg = cursor.getFloat(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_WIND_DEG);
            float wind_speed = cursor.getFloat(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_WIND_SPEED);
            int main_humidity = cursor.getInt(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_MAIN_HUMIDITY);

            ViewHolderCollection viewHolder_collection = (ViewHolderCollection) viewHolder;
            viewHolder_collection.collection_textView.setText(name);
            viewHolder_collection.textview_main_temp.setText(String.valueOf((int) main_temp));
            viewHolder_collection.textview_weather_description.setText(weather_description);
            viewHolder_collection.textview_main_temp_max.setText((int) main_temp_max + "°");
            viewHolder_collection.textview_main_temp_min.setText((int) main_temp_min + "°");
            viewHolder_collection.textview_wind_deg.setText(getDegDesc(wind_deg));
            viewHolder_collection.textview_wind_speed.setText((int) wind_speed + "级");
            viewHolder_collection.textview_main_humidity.setText(main_humidity + "%");

            if (preferences.getString("tempertruemode", null).equals("sheshi")){
                viewHolder_collection.textView_temperture.setText("°C");
                viewHolder_collection.textview_wind_speed.setText((int) wind_speed + "级");
            }


            else{
                viewHolder_collection.textView_temperture.setText("°F");
                viewHolder_collection.textview_wind_speed.setText((int) wind_speed + "里/时");
            }

            viewHolder_collection.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("SSSS", String.valueOf(_id));
                    mCallbacks.onCollectionAdapterItemSelected(_id, name);

                }
            });

            viewHolder_collection.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i("ZZZ", "ZZZ");
                    showPopupWindow(v, position, _id);

                    return true;
                }
            });

        }

//        if(position != cursor.getCount()){
//            cursor.moveToPosition(position);
//
//            final long _id = cursor.getLong(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_ID);
//            final String name = cursor.getString(City.COLUMN_INDEX_NAME);
//            String weather_description = cursor.getString(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_WEATHER_DESCRIPTION);
//            float main_temp = cursor.getInt(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_MAIN_TEMP);
//            float main_temp_min = cursor.getFloat(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_MAIN_TEMP_MIN);
//            float main_temp_max = cursor.getFloat(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_MAIN_TEMP_MAX);
//            float wind_deg = cursor.getFloat(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_WIND_DEG);
//            float wind_speed = cursor.getFloat(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_WIND_SPEED);
//            int main_humidity = cursor.getInt(City.COLUMN_COUNT + Oneday.COLUMN_INDEX_MAIN_HUMIDITY);
//
//            ViewHolderCollection viewHolder_collection = (ViewHolderCollection) viewHolder;
//            viewHolder_collection.collection_textView.setText(name);
//            viewHolder_collection.textview_main_temp.setText(String.valueOf((int) main_temp));
//            viewHolder_collection.textview_weather_description.setText(weather_description);
//            viewHolder_collection.textview_main_temp_max.setText((int) main_temp_max + "°");
//            viewHolder_collection.textview_main_temp_min.setText((int) main_temp_min + "°");
//            viewHolder_collection.textview_wind_deg.setText(getDegDesc(wind_deg));
//            viewHolder_collection.textview_wind_speed.setText((int) wind_speed + "级");
//            viewHolder_collection.textview_main_humidity.setText(main_humidity + "%");
//
//            viewHolder_collection.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.i("SSSS",String.valueOf(_id));
//                    mCallbacks.onCollectionAdapterItemSelected(_id, name);
//                }
//            });
//
//            viewHolder_collection.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    Log.i("ZZZ","ZZZ");
//                    showPopupWindow(v,position);
//
//                    return true;
//                }
//            });
//
//        }else {
//            ViewHolderAdd viewHolder_add = (ViewHolderAdd) viewHolder;
//            viewHolder_add.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, SearchActivity.class);
//                    context.startActivity(intent);
//
//
//                }
//            });
////
////
////            viewHolder_add.itemView.getContext()
//
//        }


    }

//    private int getsection(int position){
//        int section;
//        if(position <= 1)
//            section = 1;
//        else section = 2;
//
//
//    }


    @Override
    public int getItemCount() {
        if (cursor != null) {
            Log.i("ZZZ", String.valueOf(cursor.getCount()));
            return (cursor.getCount() + 4);
        }
        return 3;
    }

    public interface CollectionAdapterCallback {
        void onCollectionAdapterItemSelected(long cityid, String name);

    }

    public interface CollectionAdaptertosearch {
        void CollectionAdaptertosearchclick();
    }

    public String getDegDesc(float deg) {
        if (deg <= 22.5 || deg >= 337.5) {
            return "北风";
        } else if (deg <= 67.5) return "东北风";
        else if (deg <= 112.5) return "东风";
        else if (deg <= 157.5) return "东南风";
        else if (deg <= 202.5) return "南风";
        else if (deg <= 247.5) return "西南风";
        else if (deg <= 292.5) return "西风";
        else if (deg <= 337.5) return "西北风";

        return "未知";
    }

    private void showPopupWindow(View view, final int position, final long _id) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.pop_window, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 设置按钮的点击事件
        TextView textView_pop = (TextView) contentView.findViewById(R.id.textview_popwindow);
        textView_pop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);           /////??????????????
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Log.i("qqqq", "delete from favourite where _id = " + cursor.getLong(City.COLUMN_INDEX_ID));
//                db.execSQL("delete from favourite where _id = " + cursor.getLong(City.COLUMN_INDEX_ID)); ///导致删除收藏不正确，原因待查
//                db.execSQL("delete from weather where _id = " + cursor.getLong(City.COLUMN_INDEX_ID));
                db.execSQL("delete from favourite where _id = " + _id);
                db.execSQL("delete from weather where _id = " + _id);
                Cursor cursor2 = db.rawQuery("select * from favourite left join weather on favourite._id = weather._id", null);
                changeData(cursor2);

                popupWindow.dismiss();


            }
        });


        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.write));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);


    }





//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public final TextView collection_textView;
//        public final TextView collection_textView2;
//
//        public ViewHolder(View v) {
//            super(v);
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            collection_textView = (TextView) v.findViewById(R.id.collection_textView);
//            collection_textView2 = (TextView) v.findViewById(R.id.collection_textView2);
//        }
//
//    }
//
//
//
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        // Create a new view.
//        View v = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.fragment_collection_view, viewGroup, false);
//
//        return new ViewHolder(v);
//    }
//
//    public void onBindViewHolder()

}
