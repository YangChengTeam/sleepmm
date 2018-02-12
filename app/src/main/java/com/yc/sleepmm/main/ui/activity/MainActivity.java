package com.yc.sleepmm.main.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hwangjr.rxbus.RxBus;
import com.music.player.lib.manager.MusicPlayerManager;
import com.vondear.rxtools.RxPermissionsTool;
import com.vondear.rxtools.RxPhotoTool;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yc.sleepmm.R;
import com.yc.sleepmm.base.view.BaseActivity;
import com.yc.sleepmm.base.view.ExitDialog;
import com.yc.sleepmm.index.ui.fragment.IndexFragment;
import com.yc.sleepmm.main.ui.adapter.MainAdapter;
import com.yc.sleepmm.setting.constants.BusAction;
import com.yc.sleepmm.setting.ui.fragment.SettingFragment;
import com.yc.sleepmm.sleep.ui.fragment.SleepFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        //初始化MusicService
        MusicPlayerManager.getInstance().bindService(this);
        mList = new ArrayList<>();
        mList.add(new IndexFragment());
        mList.add(new SleepFragment());
        mList.add(new SettingFragment());
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), mList);
        mMainViewPager.setAdapter(mainAdapter); //视图加载适配器
        mMainViewPager.addOnPageChangeListener(this);
        mMainViewPager.setOffscreenPageLimit(2);
        mMainViewPager.setCurrentItem(0);


        mainBottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT);
        mainBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mainBottomNavigationBar.setBarBackgroundColor(android.R.color.transparent);

        mainBottomNavigationBar.addItem((new BottomNavigationItem(R.mipmap.main_index_select, getString(R.string.main_index)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.main_index_normal))).setInActiveColorResource(R.color.gray_aaaaaa).setActiveColorResource(R.color.blue_08a8ea))
                .addItem((new BottomNavigationItem(R.mipmap.main_index_sleep_select, getString(R.string.main_sleep)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.main_index_sleep_normal))).setInActiveColorResource(R.color.gray_aaaaaa).setActiveColorResource(R.color.blue_08a8ea))
                .addItem((new BottomNavigationItem(R.mipmap.main_index_miemie_select, getString(R.string.main_miemie)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.main_index_miemie_normal))).setInActiveColorResource(R.color.gray_aaaaaa).setActiveColorResource(R.color.blue_08a8ea))
                .setFirstSelectedPosition(0)
                .initialise();

        mainBottomNavigationBar.setTabSelectedListener(this);

        applyPermission();
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

    @Override
    public void onBackPressed() {

        final ExitDialog exitDialog = new ExitDialog(this);
        exitDialog.setTvTintContent("你确定要退出么?");
        exitDialog.setOnConfirmListener(new ExitDialog.onConfirmListener() {
            @Override
            public void onConfirm() {
                exitDialog.dismiss();
                finish();
                System.exit(0);
            }
        });
        exitDialog.show();
    }

    private void goItem(int position) {
        mainBottomNavigationBar.selectTab(position, true);
//        mMainViewPager.setCurrentItem(position, true);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int position = intent.getIntExtra("position", -1);
        if (position >= 0)
            goItem(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayerManager.getInstance().clearNotifycation();
        MusicPlayerManager.getInstance().unBindService(this);
        MusicPlayerManager.getInstance().deleteObservers();
        MusicPlayerManager.getInstance().deteleAllPlayerStateListener();
    }

    private void applyPermission() {
        RxPermissionsTool.
                with(this).
                addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.CAMERA).
                initPermission();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == RESULT_OK) {
//                    RxPhotoTool.cropImage(MainActivity.this,data.getData() );// 裁剪图片
                    initUCrop(data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                   /* data.getExtras().get("data");*/
//                    RxPhotoTool.cropImage(ActivityUser.this, RxPhotoTool.imageUriFromCamera);// 裁剪图片
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                }

                break;
            case RxPhotoTool.CROP_IMAGE://普通裁剪后的处理
//                    roadImageView(RxPhotoTool.cropImageUri, ivAvatar);
                RxBus.get().post(BusAction.GET_PICTURE, RxPhotoTool.cropImageUri);
                break;

            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
//                       roadImageView(resultUri, ivAvatar);
                    RxBus.get().post(BusAction.GET_PICTURE, resultUri);
                }
                break;
        }
    }


    public void initUCrop(Uri uri) {
        //Uri destinationUri = RxPhotoTool.createImagePathUri(this);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), imageName + ".png"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark));

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
        //options.setOvalDimmedLayer(true);
        //设置是否展示矩形裁剪框
        // options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        //options.setCropGridColumnCount(2);
        //设置横线的数量
        //options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }
}
