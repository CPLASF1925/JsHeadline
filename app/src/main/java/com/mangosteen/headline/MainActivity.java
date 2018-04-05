package com.mangosteen.headline;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.e.jia.news.view.NewsFragment;
import com.e.jia.picture.PictureFragment;
import com.e.jia.video.VideoFragment;
import com.google.gson.Gson;
import com.jia.base.BaseActivity;
import com.jia.base.BasePresenter;
import com.jia.base.annotation.BindEventBus;
import com.jia.base.eventbus.Event;
import com.jia.libnet.bean.channel.NewsChannel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@BindEventBus
public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    private static final int TAB_NEWS = 0;
    private static final int TAB_PICTURE = 1;
    private static final int TAB_VIDEO = 2;

    @BindView(R.id.tab_bottom)
    TabLayout mTabLayout;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    private String[] titles = {"新闻", "图片", "视频"};
    private Fragment mNewsFragment;
    private Fragment mPictureFragment;
    private Fragment mVideoFragment;

    @Override
    protected void initActivityView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        NewsChannel channels=new NewsChannel();

        List<NewsChannel.Channel> list=new ArrayList<>();

        list.add(new NewsChannel.Channel("推荐","__all__",true));
        list.add(new NewsChannel.Channel("热点","news_hot",true));
        list.add(new NewsChannel.Channel("社会","news_society",true));
        list.add(new NewsChannel.Channel("娱乐","news_entertainment",true));
        list.add(new NewsChannel.Channel("科技","news_tech",true));
        list.add(new NewsChannel.Channel("军事","news_military",true));
        list.add(new NewsChannel.Channel("体育","news_sports",true));
        list.add(new NewsChannel.Channel("汽车","news_car",true));

        list.add(new NewsChannel.Channel("财经","news_finance",false));
        list.add(new NewsChannel.Channel("国际","news_world",false));
        list.add(new NewsChannel.Channel("时尚","news_fashion",false));
        list.add(new NewsChannel.Channel("旅游","news_travel",false));
        list.add(new NewsChannel.Channel("探索","news_discovery",false));
        list.add(new NewsChannel.Channel("育儿","news_baby",false));
        list.add(new NewsChannel.Channel("养生","news_regimen",false));
        list.add(new NewsChannel.Channel("故事","news_story",false));
        list.add(new NewsChannel.Channel("美文","news_essay",false));
        list.add(new NewsChannel.Channel("游戏","news_game",false));
        list.add(new NewsChannel.Channel("历史","news_history",false));
        list.add(new NewsChannel.Channel("美食","news_food",false));

        channels.setChannels(list);


        Log.e(TAG, "initActivityView: "+new Gson().toJson(channels));
    }

    @Override
    protected void initView() {
        initTabLayout();

        initToolBar();

        //状态变化时删除老的Fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentById(R.id.frag_container);
        if (fragment != null) {
            ft.remove(fragment);
            fm.popBackStack();
            ft.commit();
        }

        select(TAB_NEWS);
    }

    /**
     * 初始化 选项卡
     */
    private void initTabLayout() {
        for (String title : titles) {
            TabLayout.Tab tab = mTabLayout.newTab(); //创建tab
            tab.setText(title); //设置文字
            tab.setIcon(R.mipmap.ic_launcher); //设置图片
            mTabLayout.addTab(tab); //添加到tabLayout中
        }

        mTabLayout.getTabAt(0).setIcon(R.drawable.selector_main_tab_news);
        mTabLayout.getTabAt(1).setIcon(R.drawable.selector_main_tab_img);
        mTabLayout.getTabAt(2).setIcon(R.drawable.selector_main_tab_video);
        mTabLayout.addOnTabSelectedListener(this);
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        setSupportActionBar(toolbar);
        //设置导航的图标
        toolbar.setNavigationIcon(com.e.jia.news.R.drawable.ic_menu);
        // 设置主标题
        toolbar.setTitle("新闻");
        // 左侧图标点击
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }

    @Subscribe
    public void onHandleEvent(Event e) {

    }

    private void select(int positon) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        hideAllFragments(transaction);

        switch (positon) {
            case TAB_NEWS:
                if (mNewsFragment == null) {
                    mNewsFragment = new NewsFragment();
                    transaction.add(R.id.frag_container, mNewsFragment);
                } else {
                    transaction.show(mNewsFragment);
                }
                toolbar.setTitle("新闻");
                break;
            case TAB_PICTURE:
                if (mPictureFragment == null) {
                    mPictureFragment = new PictureFragment();
                    transaction.add(R.id.frag_container, mPictureFragment);
                } else {
                    transaction.show(mPictureFragment);
                }
                toolbar.setTitle("图片");
                break;
            case TAB_VIDEO:
                if (mVideoFragment == null) {
                    mVideoFragment = new VideoFragment();
                    transaction.add(R.id.frag_container, mVideoFragment);
                } else {
                    transaction.show(mVideoFragment);
                }
                toolbar.setTitle("视频");
                break;
        }
        transaction.commit();
    }

    private void hideAllFragments(FragmentTransaction transaction) {
        if (mNewsFragment != null) transaction.hide(mNewsFragment);
        if (mPictureFragment != null) transaction.hide(mPictureFragment);
        if (mVideoFragment != null) transaction.hide(mVideoFragment);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        select(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Snackbar.make(toolbar, "搜索", Snackbar.LENGTH_LONG).show();
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
