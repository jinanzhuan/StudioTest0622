package com.example.l12_fragment3;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by shuwei on 2016/6/18.
 */
public class TitleFragment extends ListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //设置适配器
        setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_list_title, DataUtils.TITLES));
        //设置listview为单选模式(同时item布局中添加 android:background="?android:attr/activatedBackgroundIndicator")
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //设置默认选中第一项
        getListView().setItemChecked(0,true);
        ShowDetails(0);




    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //加载动态的Fragment
        ShowDetails(position);



    }

    private void ShowDetails(int position) {
        DetailsFragment detailsFragment=new DetailsFragment();

        //传递选中的item的下标
        Bundle bundle=new Bundle();
        bundle.putInt("position",position);
        detailsFragment.setArguments(bundle);

        //获得transaction并进行替换操作，最后提交事务
        this.getFragmentManager().beginTransaction()
                .replace(R.id.ll_main_details, detailsFragment).commit();
    }
}
