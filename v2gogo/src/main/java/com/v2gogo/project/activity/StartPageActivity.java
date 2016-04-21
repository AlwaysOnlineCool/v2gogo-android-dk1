package com.v2gogo.project.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.igexin.sdk.PushManager;
import com.testin.agent.TestinAgent;
import com.v2gogo.project.R;
import com.v2gogo.project.db.MatserInfo;
import com.v2gogo.project.domain.WelcomeInfo;
import com.v2gogo.project.domain.WelcomeItemInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.main.image.GlideImageLoader;
import com.v2gogo.project.manager.WelcomeManager;
import com.v2gogo.project.manager.WelcomeManager.IOnAppLoadingCallback;
import com.v2gogo.project.manager.account.AccountLoginManager;
import com.v2gogo.project.manager.account.AccountLoginManager.IAccountLoginCallback;
import com.v2gogo.project.manager.profile.ServicePhoneManager;
import com.v2gogo.project.utils.common.LogUtil;
import com.v2gogo.project.utils.common.SPUtil;
import com.v2gogo.project.utils.http.Constants;
import com.v2gogo.project.views.dialog.AppNoticeDialog;
import com.v2gogo.project.views.dialog.AppNoticeDialog.IonClickSureCallback;

/**
 * 应用程序启动页界面
 *
 * @author houjun
 */
public class StartPageActivity extends BaseActivity implements IonClickSureCallback {

    private ArrayList<WelcomeItemInfo> mWelcomeItemInfo;
    private ImageView mStartPageImageView;
    private AppNoticeDialog mAppNoticeDialog;
    private CustomCountTimer mCustomCountTimer;

    @Override
    public void clearRequestTask() {
        if (mCustomCountTimer != null) {
            mCustomCountTimer.cancel();
        }
        WelcomeManager.clearGetAppLoadingImageTask();
    }

    @Override
    public void onInitViews() {
        TestinAgent.init(this.getApplicationContext());
        TestinAgent.setLocalDebug(false);
        mStartPageImageView = (ImageView) findViewById(R.id.start_page_imageview);
    }

    @Override
    public int getCurrentLayoutId() {
        return R.layout.start_page_activity_layout;
    }

    @Override
    protected void onInitLoadDatas() {
        super.onInitLoadDatas();
        getCurrentDeviceId();
        loadAppStatLocalImage();
        getServicePhone();
        boolean isHasAd = false;
        isHasAd = getLocalWelcomeDatas();
        if (isHasAd && mWelcomeItemInfo != null) {
            // 停留2秒，进入广告
            if (null == mCustomCountTimer) {
                mCustomCountTimer = new CustomCountTimer(2 * 1000, 1 * 1000, true);
            }
        } else {
            // 停留5秒，进入主页或引导页
            accountAutoLogin();
            // 停留2秒，进入广告
            if (null == mCustomCountTimer) {
                mCustomCountTimer = new CustomCountTimer(5 * 1000, 1 * 1000, false);
            }
        }
        mCustomCountTimer.start();
        getAppLoadingImage();

        // ======测试分割线=====
        // SubjectManager.getHttpSubjectData(null);
    }

    /**
     * 获取客服电话
     */
    private void getServicePhone() {
        ServicePhoneManager.getServicePhone(this);
    }

    /**
     *
     */
    private void getAppLoadingImage() {
        WelcomeManager.getAppLoadingImage(new IOnAppLoadingCallback() {
            @Override
            public void onAppLoadingSuccesss(WelcomeInfo welcomeInfo) {
                if (welcomeInfo != null && null != welcomeInfo.getmWelcomeItemInfos() && welcomeInfo.getmWelcomeItemInfos().size() > 0) {
                    for (WelcomeItemInfo welcomeItemInfo : welcomeInfo.getmWelcomeItemInfos()) {
                        if (welcomeItemInfo != null) {
                            Glide.with(StartPageActivity.this).load(welcomeItemInfo.getUrl()).diskCacheStrategy(DiskCacheStrategy.SOURCE);
//							ImageLoader.getInstance().loadImage(welcomeItemInfo.getRealImage(),
//									DisplayImageOptionsFactory.getDefaultDisplayImageOptionsWithNoPhoto(true), new SimpleImageLoadingListener());
                        }
                    }
                }
            }

            @Override
            public void onAppLoadingFail() {
            }
        });
    }

    @Override
    public void onClickSure() {
        Intent intent = new Intent(StartPageActivity.this, MainTabActivity.class);
        intent.putExtra("isLogin", true);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 用户自动登录
     */
    private void accountAutoLogin() {
        if (V2GogoApplication.getMasterLoginState()) {
            MatserInfo matserInfo = V2GogoApplication.getCurrentMatser();
            String pwd = (String) SPUtil.get(this, Constants.USER_PASS, "");
            AccountLoginManager.accountLogin(matserInfo.getUsername(), pwd, true, new IAccountLoginCallback() {
                @Override
                public void onAccountLoginSuccess(MatserInfo masterInfo) {
                }

                @Override
                public void onAccountLoginFail(String erroMessage) {
                    if (getResources().getString(R.string.user_pwd_error_tip).equals(erroMessage)) {
                        if (mCustomCountTimer != null) {
                            mCustomCountTimer.cancel();
                        }
                        V2GogoApplication.clearMatserInfo(true);
                        showUserExceptionDialog();
                    }
                }
            });
        }
    }

    /**
     * 加载本地开机图片
     */
    private void loadAppStatLocalImage() {
        GlideImageLoader.loadInternalDrawable(this, R.drawable.loading_image_default, mStartPageImageView);
    }

    /**
     * 进入应用
     */
    private void enterApp() {
        boolean isFirst = (Boolean) SPUtil.get(StartPageActivity.this, Constants.IS_FIRST, true);
        if (isFirst) {
            Intent intent = new Intent(StartPageActivity.this, WizardActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(StartPageActivity.this, MainTabActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 判断本地是否有开机图片信息
     *
     * @return
     */
    private boolean getLocalWelcomeDatas() {
        boolean isHasAd = false;
        WelcomeInfo welcomeInfo = WelcomeManager.getAppLocalLoadingImage(this);
        if (welcomeInfo == null) {
            isHasAd = false;
        } else {
            List<WelcomeItemInfo> welcomeItemInfos = welcomeInfo.getmWelcomeItemInfos();
            if (null == welcomeInfo || welcomeItemInfos.size() == 0) {
                isHasAd = false;
            } else {
                mWelcomeItemInfo = (ArrayList<WelcomeItemInfo>) welcomeItemInfos;
                isHasAd = true;
            }
        }
        return isHasAd;
    }

    /**
     * 获得当前设备id
     */
    private void getCurrentDeviceId() {
        PushManager.getInstance().initialize(this.getApplicationContext());
        String string = PushManager.getInstance().getClientid(this);
        LogUtil.d("当前设备的cid->" + string);
        if (!TextUtils.isEmpty(string)) {
            SPUtil.put(this, Constants.CLIENT_ID, string);
        }
    }

    /**
     * 显示用户异常对话框
     */
    private void showUserExceptionDialog() {
        try {
            if (mAppNoticeDialog == null) {
                mAppNoticeDialog = new AppNoticeDialog(this, R.style.style_action_sheet_dialog);
                mAppNoticeDialog.setCancelable(false);
                mAppNoticeDialog.setOnSureCallback(this);
                mAppNoticeDialog.setCanceledOnTouchOutside(false);
            }
            if (!mAppNoticeDialog.isShowing()) {
                mAppNoticeDialog.show();
                mAppNoticeDialog.setSureTitleAndMessage(R.string.user_account_exception_tip, R.string.app_notice_sure_tip);
            }
        } catch (Exception exception) {
        }
    }

    /**
     * 进度主页的倒计时
     *
     * @author houjun
     */
    private class CustomCountTimer extends CountDownTimer {

        private boolean isHasAd;

        public CustomCountTimer(long millisInFuture, long countDownInterval, boolean hasAd) {
            super(millisInFuture, countDownInterval);
            isHasAd = hasAd;
        }

        @Override
        public void onFinish() {
            if (isHasAd) {
                Intent intent = new Intent(StartPageActivity.this, StartPageAdActivity.class);
                intent.putExtra(StartPageAdActivity.WELCOME_ITEM_INFO, mWelcomeItemInfo);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_activity_enter_anim, R.anim.anim_activity_exit_anim);
            } else {
                enterApp();
            }
        }

        @Override
        public void onTick(long arg0) {
        }
    }
}
