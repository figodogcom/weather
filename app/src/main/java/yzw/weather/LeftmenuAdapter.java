package yzw.weather;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by yangzhiwei on 2016/10/21.
 */
public class LeftmenuAdapter extends RecyclerView.Adapter<LeftmenuAdapter.ViewHolder> {
    private leftmenucallback mleftmenucallback;
    int recyclermode;
    private Activity mActivity;
    int colorposition = -1;
    static boolean upstatus = true;

    public LeftmenuAdapter(int recyclermode, Activity mActivity) {
        this.recyclermode = recyclermode;
        this.mActivity = mActivity;
//        this.colorposition = colorposition;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextView;
        public final ImageView mImageView;
        public final ImageView mImageViewcolor;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.leftmenu_textview);
            mImageView = (ImageView) itemView.findViewById(R.id.leftmenu_imageview);
            mImageViewcolor = (ImageView) itemView.findViewById(R.id.leftmenu_color);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_leftmenu_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView mTextView = holder.mTextView;
        ImageView imageView = holder.mImageView;
        final ImageView mImageViewcolor = holder.mImageViewcolor;
        if (recyclermode == 1) {
            switch (position) {
                case 0:
                    mTextView.setText("预报");
                    imageView.setImageResource(R.drawable.ic_home_white_24dp);
                    break;
                case 1:
                    mTextView.setText("收藏夹");
                    imageView.setImageResource(R.drawable.ic_folder_special_white_24dp);
                    break;
                case 2:
                    mTextView.setText("VIP充值");
                    imageView.setImageResource(R.drawable.ic_attach_money_white_24dp);
                    break;
                case 3:
                    mTextView.setText("发送反馈");
                    imageView.setImageResource(R.drawable.ic_email_white_24dp);
                    break;
                default:
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    mTextView.setText("登陆");
                    imageView.setImageResource(R.drawable.ic_account_circle_white_24dp);
                    break;
                case 1:
                    mTextView.setText("设置");
                    imageView.setImageResource(R.drawable.ic_settings_white_24dp);
                    break;
                default:
                    break;
            }
        }
        Log.i("FFF", "colorposition" + String.valueOf(colorposition));
        Log.i("FFF", "position" + String.valueOf(position));
        mImageViewcolor.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        if (colorposition == position)
            mImageViewcolor.setBackgroundColor(Color.parseColor("#6d9eeb"));
        else mImageViewcolor.setBackgroundColor(Color.parseColor("#00FFFFFF"));


        if (recyclermode == 1)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    colorposition = position;
                    upstatus = true;

//                    else mImageViewcolor.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    mleftmenucallback = (leftmenucallback) mActivity;
                    mleftmenucallback.mleftmenucallback(position);
                    mleftmenucallback.mleftmenucolor(recyclermode);

//                    mleftmenucallback.mleftmenucallback(position);

                }
            });
        else holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorposition = position;

                mleftmenucallback = (leftmenucallback) mActivity;
                mleftmenucallback.mleftmenucolor(recyclermode);
                if (position == 1)
                    mleftmenucallback.mleftmenucallback(5);
            }
        });

    }


    @Override
    public int getItemCount() {
        int sum;
        return sum = recyclermode == 1 ? 4 : 2;
    }


    public interface leftmenucallback {
        void mleftmenucallback(int position);

        void mleftmenucolor(int recyclermode);


    }

    void changedate() {
        colorposition = -1;
        notifyDataSetChanged();
    }


}
