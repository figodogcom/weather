package yzw.weather.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.List;

import yzw.weather.CollectionAdapter;
import yzw.weather.R;
import yzw.weather.SearchAdapter;
import yzw.weather.db.MyDatabaseHelper;
import yzw.weather.model.City;
import yzw.weather.util.CityListUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements CollectionAdapter.CollectionAdaptertosearch {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private SearchAdapter searchAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mlayoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param param1 Parameter 1.
     * //     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment newInstance() {
        Fragment fragment = new SearchFragment();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_collection_search, container, false);
        final SearchView sv = (SearchView) rootView.findViewById(R.id.searchview_text);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.searchview_list);
        mlayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mlayoutManager);

        searchAdapter = new SearchAdapter();
        mRecyclerView.setAdapter(searchAdapter);

//        sv.setOnQueryTextListener();
        sv.setIconifiedByDefault(false);
        // 设置该SearchView显示搜索按钮
        sv.setSubmitButtonEnabled(true);
        // 设置该SearchView内默认显示的提示文本
        sv.setQueryHint("查找");

        // 为该SearchView组件设置事件监听器
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 用户输入字符时激发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("KKK", "KKK");
                // 如果newText不是长度为0的字符串
                if (TextUtils.isEmpty(newText)) {
                    // 清除ListView的过滤
                    searchAdapter.changeData(null,getContext());

                } else {
                    List<City> cityList = CityListUtil.searchCity(getContext(), newText);
                    searchAdapter.changeData(cityList,getContext());
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // 实际应用中应该在该方法内执行实际查询
                // 此处仅使用Toast显示用户输入的查询内容

                Log.i("KKK", query);
                List<City> cityList = CityListUtil.searchCity(getContext(), query);


//                searchAdapter = new SearchAdapter(cityList);
//                mRecyclerView.setAdapter(searchAdapter);

                searchAdapter.changeData(cityList,getContext());

                Toast.makeText(getContext(), "您的选择是:" + query
                        , Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        return rootView;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void CollectionAdaptertosearchclick() {
        Fragment fragment = newInstance();
        ;
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
