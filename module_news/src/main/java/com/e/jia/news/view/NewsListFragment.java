package com.e.jia.news.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.e.jia.news.R;
import com.e.jia.news.adapter.NewsListAdapter;
import com.e.jia.news.contract.NewsListContract;
import com.e.jia.news.presenter.NewsListPresenter;
import com.jia.base.BaseFragment;
import com.jia.libnet.bean.news.NewsBean;

/**
 * 列表界面
 * Created by jia on 2018/3/31.
 */

public class NewsListFragment extends BaseFragment<NewsListContract.NewsListView, NewsListPresenter>
        implements NewsListContract.NewsListView {

    private SwipeRefreshLayout refresh_layout;
    private RecyclerView recycler_view;
    private TextView tv_no_data;
    private NewsListAdapter adapter;

    private String tag = "";

    @Override
    protected View initFragmentView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.frag_news_pager, null);
        return view;
    }

    @Override
    protected void initFragmentChildView(View view) {
        refresh_layout = view.findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        recycler_view = view.findViewById(R.id.recycler_view);
        tv_no_data=view.findViewById(R.id.tv_no_data);
        adapter=new NewsListAdapter(getActivity());
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getNewsListByTag(tag);
            }
        });
        refresh_layout.setRefreshing(true);
        mPresenter.getNewsListByTag(tag);
        tv_no_data.setVisibility(View.VISIBLE);
        tv_no_data.setText("加载中...");
    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {
//        refresh_layout.setRefreshing(true);
    }

    @Override
    protected NewsListPresenter createPresenter() {
//        return new NewsListPresenter(this.<Long>bindUntilEvent(FragmentEvent.DESTROY));
        return new NewsListPresenter(null);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void onRefreshSuccess(NewsBean bean) {
        refresh_layout.setRefreshing(false);

        adapter.setData(bean.getData());

        tv_no_data.setVisibility(View.GONE);
    }

    @Override
    public void onRefreshFail(String info) {
        refresh_layout.setRefreshing(false);

        Toast.makeText(getContext(),""+info,Toast.LENGTH_LONG).show();

        tv_no_data.setVisibility(View.VISIBLE);
        tv_no_data.setText("暂无数据");
    }
}