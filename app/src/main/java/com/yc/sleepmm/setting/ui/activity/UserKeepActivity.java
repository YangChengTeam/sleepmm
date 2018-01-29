package com.yc.sleepmm.setting.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.setting.ui.fragment.UserKeepFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by admin on 2018/1/26.
 */

public class UserKeepActivity extends BaseActivity {

    @BindView(R.id.tl_tab)
    TabLayout mTabTl;

    @BindView(R.id.vp_content)
    ViewPager mContentVp;

    @BindView(R.id.iv_back)
    ImageView mBackImage;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_vip_protocol)
    TextView mVipProtocalTextView;

    private List<Fragment> tabFragments;

    private ContentPagerAdapter contentAdapter;

    private List<String> tabIndicators;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_keep;
    }

    @Override
    public void init() {
        initContent();
        initTab();
    }

    private void initContent() {
        mVipProtocalTextView.setVisibility(View.GONE);
        mTitleTextView.setText(getResources().getString(R.string.person_favor));

        tabIndicators = new ArrayList<>();
        tabFragments = new ArrayList<>();

        tabIndicators.add("音乐");
        tabIndicators.add("电台");
        tabFragments.add(UserKeepFragment.newInstance(1));
        tabFragments.add(UserKeepFragment.newInstance(2));

        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        mContentVp.setAdapter(contentAdapter);

        mTabTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        RxView.clicks(mBackImage).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

    }

    private void initTab() {
        mTabTl.setTabMode(TabLayout.GRAVITY_CENTER);
        mTabTl.setSelectedTabIndicatorHeight(0);
        mTabTl.setupWithViewPager(mContentVp);
        for (int i = 0; i < tabIndicators.size(); i++) {
            TabLayout.Tab itemTab = mTabTl.getTabAt(i);
            if (itemTab != null) {
                itemTab.setText(tabIndicators.get(i));
            }
        }
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }
    }
}
