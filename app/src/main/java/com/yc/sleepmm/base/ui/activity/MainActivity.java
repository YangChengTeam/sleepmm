package com.yc.sleepmm.base.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.ui.adapter.MainAdapter;
import com.yc.sleepmm.base.ui.fragment.IndexFragment;
import com.yc.sleepmm.setting.ui.fragment.SettingFragment;
import com.yc.sleepmm.base.ui.fragment.SleepFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.main_viewPager)
    ViewPager mMainViewPager;
    @BindView(R.id.main_bottom_navigation_bar)
    BottomNavigationBar mainBottomNavigationBar;
    private List<Fragment> mList; //ViewPager的数据源

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        mList = new ArrayList<>();
        mList.add(new IndexFragment());
        mList.add(new SleepFragment());
        mList.add(new SettingFragment());
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), mList);
        mMainViewPager.setAdapter(mainAdapter); //视图加载适配器
        mMainViewPager.addOnPageChangeListener(this);


        mainBottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT);
        mainBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mainBottomNavigationBar.setBarBackgroundColor(android.R.color.transparent);

        mainBottomNavigationBar.addItem((new BottomNavigationItem(R.mipmap.main_index_select, getString(R.string.main_index)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.main_index_normal))).setInActiveColorResource(R.color.gray_aaaaaa).setActiveColorResource(R.color.blue_08a8ea))
                .addItem((new BottomNavigationItem(R.mipmap.main_index_sleep_select, getString(R.string.main_sleep)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.main_index_sleep_normal))).setInActiveColorResource(R.color.gray_aaaaaa).setActiveColorResource(R.color.blue_08a8ea))
                .addItem((new BottomNavigationItem(R.mipmap.main_index_miemie_select, getString(R.string.main_miemie)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.main_index_miemie_normal))).setInActiveColorResource(R.color.gray_aaaaaa).setActiveColorResource(R.color.blue_08a8ea))
                .setFirstSelectedPosition(0)
                .initialise();

        mainBottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {
        mMainViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mainBottomNavigationBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
