package com.v2gogo.project.activity;

import java.io.File;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.v2gogo.project.InternalLinksTool;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.account.AccountLoginActivity;
import com.v2gogo.project.activity.exchange.ExchangeActivity;
import com.v2gogo.project.activity.home.BaseTabHostActivity;
import com.v2gogo.project.activity.home.HomeActivity2;
import com.v2gogo.project.activity.home.HomeShakeActivity;
import com.v2gogo.project.activity.profile.ProfileActivity;
import com.v2gogo.project.activity.shop.GroupWebViewActivity;
import com.v2gogo.project.activity.shop.ShopActivity;
import com.v2gogo.project.adapter.HomeBottomToolAdapter;
import com.v2gogo.project.domain.ToolInfo;
import com.v2gogo.project.domain.VersionInfo;
import com.v2gogo.project.domain.home.HomeInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HomeManager;
import com.v2gogo.project.manager.VersionManager;
import com.v2gogo.project.manager.VersionManager.IonServerVersionInfosCallback;
import com.v2gogo.project.utils.common.AppUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.common.apk.ApkUtil;
import com.v2gogo.project.utils.common.apk.DownloadApkService;
import com.v2gogo.project.utils.common.apk.StorageUtils;
import com.v2gogo.project.views.dialog.ApkDownloadProgressDialog;
import com.v2gogo.project.views.dialog.AppNoticeDialog;
import com.v2gogo.project.views.dialog.AppNoticeDialog.IonClickSureCallback;
import com.v2gogo.project.views.dialog.AppVersionUpdateDialog;
import com.v2gogo.project.views.dialog.AppVersionUpdateDialog.IonStartDownloadCallback;
import com.ypy.eventbus.EventBus;

/**
 * 主程序界面
 *
 * @author houjun
 */
@SuppressWarnings("deprecation")
public class MainTabActivity extends BaseTabHostActivity implements OnItemClickListener, IonStartDownloadCallback, IonClickSureCallback {

    public static final String BACK = "back";
    public static final String BACK_HOME = "back_home";
    public static final String BACK_SHOP = "back_shop";
    public static final String KICK_OFF = "kick_off";
    public static final String MODIFY_PWD = "modify_pwd";

    private TabHost mTabHost;
    private GridView myGridView;
    private AppNoticeDialog mAppNoticeDialog;
    private AppVersionUpdateDialog mVersionUpdateDialog;
    private ApkDownloadProgressDialog mDownloadProgressDialog;
    private DownloadApkBroadcastReceiver mDownloadApkBroadcastReceiver;
    private List<ToolInfo> mToolInfos;
    private HomeBottomToolAdapter adapter;

    private Intent intent5;// 电商

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        startLoginActivity(intent);
        setContentView(R.layout.activity_main_tab);
        HomeInfo homeInfo = HomeManager.getAppLocalHomeData(this);
        initTabHost();
        setStatusBarbg();
        // setActionBarHeight();
        myGridView = (GridView) findViewById(R.id.home_bottom_tool_gridView);
        adapter = new HomeBottomToolAdapter(mToolInfos, this);
        myGridView.setAdapter(adapter);
        myGridView.setOnItemClickListener(this);
        if (homeInfo != null && homeInfo.getmToolInfos() != null) {
            mToolInfos = homeInfo.getmToolInfos();
            builderRadioBotton(mToolInfos);
        }
        chackAppVersion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(List<ToolInfo> toolInfo) {
        if (toolInfo != null && toolInfo.size() > 0) {
            mToolInfos = toolInfo;
            builderRadioBotton(mToolInfos);
        }
    }

    private void builderRadioBotton(List<ToolInfo> toolInfos) {
        myGridView.setNumColumns(toolInfos.size());
        adapter.resetDataChanged(toolInfos);
    }

    /**
     * 点击摇摇乐
     */
    private void clickShake() {
        if (V2GogoApplication.getMasterLoginState()) {
            Intent intent = new Intent(this, HomeShakeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            AccountLoginActivity.forwardAccountLogin(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent) {
            String result = intent.getStringExtra(BACK);
            if (BACK_HOME.equals(result)) {
                mTabHost.setCurrentTab(0);
                setSelectColor(myGridView, 0);
            } else if (BACK_SHOP.equals(result)) {
                mTabHost.setCurrentTab(4);
                setSelectColor(myGridView, getShopIndex());
            } else if (KICK_OFF.equals(result) || MODIFY_PWD.equals(result)) {
                mTabHost.setCurrentTab(0);
                setSelectColor(myGridView, 0);
                startLoginActivity(intent);
            }
        }
    }

    /**
     * 获取团购的索引
     * method desc：
     *
     * @return
     */
    private int getShopIndex() {
        if (mToolInfos != null && mToolInfos.size() > 0) {
            for (int i = 0, size = mToolInfos.size(); i < size; i++) {
                ToolInfo toolInfo = mToolInfos.get(i);
                if (toolInfo.getContentType() == 5) {
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    public void finish() {
        ToastUtil.cancelAllToast();
        if (null != mDownloadApkBroadcastReceiver) {
            unregisterReceiver(mDownloadApkBroadcastReceiver);
        }
        super.finish();
    }

    @Override
    public void onStartDownload(VersionInfo versionInfo) {
        if (versionInfo != null) {
            if (null == mDownloadProgressDialog) {
                mDownloadProgressDialog = new ApkDownloadProgressDialog(this, R.style.style_action_sheet_dialog);
            }
            if (!mDownloadProgressDialog.isShowing()) {
                mDownloadProgressDialog.show();
            }
            if (mDownloadApkBroadcastReceiver == null) {
                mDownloadApkBroadcastReceiver = new DownloadApkBroadcastReceiver();
            }
            IntentFilter intentFilter = new IntentFilter(DownloadApkService.PROGRESS_INTENT);
            registerReceiver(mDownloadApkBroadcastReceiver, intentFilter);
            ApkUtil.startDownloadApk(this, versionInfo.getDownloadUrl());
        }
    }

    @Override
    public void onClickSure() {
        ImageLoader.getInstance().clearMemoryCache();
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (null == mAppNoticeDialog) {
                mAppNoticeDialog = new AppNoticeDialog(this, R.style.style_action_sheet_dialog);
                mAppNoticeDialog.setOnSureCallback(this);
            }
            if (!mAppNoticeDialog.isShowing()) {
                mAppNoticeDialog.show();
                mAppNoticeDialog.setSureTitleAndMessage(R.string.sure_exit_app_tip, R.string.app_notice_sure_tip);
            }
            return false;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    /**
     * 检测app版本
     */
    private void chackAppVersion() {
        VersionManager.getServerVersionInfos(new IonServerVersionInfosCallback() {
            @Override
            public void onServerVersionInfosSuccess(VersionInfo versionInfo) {
                checkAppVersion(versionInfo);
            }

            @Override
            public void onServerVersionInfosFail(String errorMessage) {

            }
        });
    }

    /**
     * 跳转到登录
     *
     * @param intent
     */
    private void startLoginActivity(Intent intent) {
        boolean isLogin = intent.getBooleanExtra("isLogin", false);
        if (isLogin) {
            Intent intent2 = new Intent(this, AccountLoginActivity.class);
            this.startActivity(intent2);
        }
    }

    /**
     * 检测程序的版本
     */
    private void checkAppVersion(VersionInfo versionInfo) {
        if (versionInfo != null) {
            if (!TextUtils.isEmpty(versionInfo.getVername()) && versionInfo.getType() == 0) {
                boolean isNew = isNewVersion(versionInfo.getVername());
                if (isNew) {
                    V2GogoApplication v2GogoApplication = V2GogoApplication.getsIntance();
                    if (v2GogoApplication != null) {
                        v2GogoApplication.mNewApkLoadUrl = versionInfo.getDownloadUrl();
                    }
                    if (versionInfo.getUpdate() == VersionInfo.FOUSE_UPDATE_VERSION) {
                        showAppUpdateDialog(versionInfo, true);
                    } else if (versionInfo.getUpdate() == VersionInfo.HAVE_UPDATE_VERSION) {
                        showAppUpdateDialog(versionInfo, false);
                    }
                }
            }
        }
    }

    protected boolean isNewVersion(String versionName) {
        float newestVersion = Float.valueOf(versionName);
        int localVersion = AppUtil.getVersionCode(this);
        boolean isNew = false;
        if (newestVersion > localVersion) {
            isNew = true;
        }
        return isNew;
    }

    /**
     * 显示app升级对话框
     *
     * @param versionInfo
     */
    private void showAppUpdateDialog(VersionInfo versionInfo, boolean isForceUpdate) {
        if (mVersionUpdateDialog == null) {
            mVersionUpdateDialog = new AppVersionUpdateDialog(this, R.style.style_action_sheet_dialog);
            mVersionUpdateDialog.setOnCallback(this);
        }
        if (!mVersionUpdateDialog.isShowing()) {
            mVersionUpdateDialog.show();
            mVersionUpdateDialog.setVersionInfos(versionInfo, isForceUpdate);
        }
    }

    /**
     * 初始化tabhost
     */
    private void initTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setFocusable(true);
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("1");
        Intent intent = new Intent(this, HomeActivity2.class);
        tabSpec.setIndicator("one").setContent(intent);
        mTabHost.setup(this.getLocalActivityManager());
        mTabHost.addTab(tabSpec);

        TabHost.TabSpec tabSpec2 = mTabHost.newTabSpec("2");
        Intent intent2 = new Intent(this, ExchangeActivity.class);
        tabSpec2.setIndicator("two").setContent(intent2);
        mTabHost.addTab(tabSpec2);

        TabHost.TabSpec tabSpec3 = mTabHost.newTabSpec("3");
        Intent intent3 = new Intent(this, ShopActivity.class);
        tabSpec3.setIndicator("three").setContent(intent3);
        mTabHost.addTab(tabSpec3);

        TabHost.TabSpec tabSpec4 = mTabHost.newTabSpec("4");
        Intent intent4 = new Intent(this, ProfileActivity.class);
        tabSpec4.setIndicator("four").setContent(intent4);
        mTabHost.addTab(tabSpec4);

        TabHost.TabSpec tabSpec5 = mTabHost.newTabSpec("5");
        intent5 = intent = new Intent(this, GroupWebViewActivity.class);
        tabSpec5.setIndicator("five").setContent(intent5);
        mTabHost.addTab(tabSpec5);

        mTabHost.setCurrentTab(0);
    }

    /**
     * 下载进度广播接收器
     *
     * @author houjun
     */
    private class DownloadApkBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent data) {
            if (data != null) {
                String action = data.getAction();
                if (DownloadApkService.PROGRESS_INTENT.equals(action)) {
                    int progress = data.getIntExtra(DownloadApkService.PROGRESS, 0);
                    if (progress != 100) {
                        if (mDownloadProgressDialog != null) {
                            mDownloadProgressDialog.setProgress(progress);
                        }
                    } else {
                        if (mDownloadProgressDialog != null) {
                            mDownloadProgressDialog.dismiss();
                        }
                        mDownloadApkBroadcastReceiver = null;
                        MainTabActivity.this.unregisterReceiver(this);
                        File path = StorageUtils.getCacheDirectory(context);
                        File apkFile = new File(path, "v2gogo.apk");
                        String apkPath = apkFile.getAbsolutePath();
                        ApkUtil.installApk(context, apkPath);
                    }
                }
            }
        }
    }

    private void setSelectColor(AdapterView<?> gridView, int position) {
        try {
            for (int i = 0, len = gridView.getChildCount(); i < len; i++) {
                TextView titile = (TextView) gridView.getChildAt(i).findViewWithTag(i);
                LinearLayout layout = (LinearLayout) gridView.getChildAt(i);
                ImageView imageView = (ImageView) layout.getChildAt(0);
                if (position == i) {
                    if (titile != null && imageView != null) {
                        titile.setTextColor(0xFFff5a00);
                        // imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
                        imageView.setColorFilter(0xFFff5a00);
                    }
                } else {
                    if (titile != null && imageView != null) {
                        titile.setTextColor(0xFF696969);
                        // imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
                        imageView.setColorFilter(0xFF696969);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> gridView, View view, int position, long arg3) {
        if (mToolInfos != null && mToolInfos.size() > 0) {
            setSelectColor(gridView, position);

            ToolInfo toolInfo = mToolInfos.get(position);
            int contentType = toolInfo.getContentType();
            switch (contentType) {
                case 1:// 首页
                    mTabHost.setCurrentTab(0);
                    break;
                case 4:// 内部链接
                    try {
                        String type = toolInfo.getToolUrl();
                        Uri uri = Uri.parse(type);
                        String finaltype = uri.getQueryParameter("type");
                        String url = uri.getQueryParameter("url");
                        String id = uri.getQueryParameter("srcId");
                        if ("3".equals(finaltype)) {
                            mTabHost.setCurrentTab(2);// 内部链接团购列表
                        } else if ("5".equals(finaltype)) {
                            mTabHost.setCurrentTab(1);// 内部链接兑换列表
                        } else {
                            InternalLinksTool.jump2Activity(MainTabActivity.this, Integer.parseInt(finaltype), id, url, null);
                        }
                    } catch (Exception e) {
                    }
                    break;
                case 6:// 团购
                    mTabHost.setCurrentTab(2);
                    break;
                case 2:// 我的
                    mTabHost.setCurrentTab(3);
                    break;
                case 3:// 摇摇乐
                    clickShake();
                    break;
                case 5:// 外部链接
                    if (intent5 != null) {
                        intent5.putExtra(GroupWebViewActivity.URL, toolInfo.getToolUrl());
                        mTabHost.setCurrentTab(4);// 电商
                    }
                    break;
            }
        }
    }
}
