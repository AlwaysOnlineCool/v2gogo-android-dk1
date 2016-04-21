package com.v2gogo.project.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.sharesdk.framework.ShareSDK;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.v2gogo.project.R;
import com.v2gogo.project.activity.home.theme.UploadPictureActivity;
import com.v2gogo.project.domain.ShareInfo;
import com.v2gogo.project.domain.home.theme.ThemePhotoUploadResultInfo;
import com.v2gogo.project.domain.home.theme.UploadErrorInfo;
import com.v2gogo.project.main.V2GogoApplication;
import com.v2gogo.project.manager.HttpJsonObjectRequest.IOnDataReceiveMessageCallback;
import com.v2gogo.project.manager.UserInfoManager;
import com.v2gogo.project.manager.config.PhotoServerUrlConfig;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.manager.union.UnionPayManager;
import com.v2gogo.project.manager.upload.CommentUploadMultimediaManager;
import com.v2gogo.project.manager.upload.CommentUploadMultimediaManager.IonFactMultimediaUploadCallback;
import com.v2gogo.project.manager.upload.ThemePhotoUploadManager;
import com.v2gogo.project.utils.StatusCode;
import com.v2gogo.project.utils.common.AppUtil;
import com.v2gogo.project.utils.common.BitmapCompressUtil;
import com.v2gogo.project.utils.common.MD5Util;
import com.v2gogo.project.utils.common.PhotoUtil;
import com.v2gogo.project.utils.common.ToastUtil;
import com.v2gogo.project.utils.http.IntentExtraConstants;
import com.v2gogo.project.utils.share.CustomPlatformActionListener;
import com.v2gogo.project.utils.share.ShareUtils;
import com.v2gogo.project.views.dialog.V2gogoShareDialog;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.IonItemClickCallback;
import com.v2gogo.project.views.dialog.V2gogoShareDialog.SHARE_TYPE;
import com.v2gogo.project.views.webview.NoFadeColorWebView.IonPayCallback;
import com.v2gogo.project.views.webview.ProgressWebView;
import com.v2gogo.project.views.webview.ProgressWebView.IonReceiveTitleCallback;
import com.ypy.eventbus.EventBus;

/**
 * 功能：
 *
 * @ahthor：黄荣星
 * @date:2015-12-15
 * @version::V1.0
 */
@SuppressWarnings("deprecation")
public class WebViewActivity extends BaseActivity implements IonReceiveTitleCallback, IonPayCallback, IonItemClickCallback {
    public static final String URL = "url";
    public static final String IS_BACK_HOME = "is_back_home";

    private String url;
    private boolean isBackHome;

    private AbsoluteLayout mAbsoluteLayout;
    private ProgressWebView mWebView;
    private ImageButton mBack;
    private TextView mTitle;

    private ImageButton mShareImgBtn;
    private TextView mCloseTv;
    private V2gogoShareDialog mShareDialog;
    private ShareInfo mShareInfo;

    private static WebViewActivity instance;

    private IWXAPI mApi;// 微信支付API

    @Override
    public void onInitViews() {
        mApi = WXAPIFactory.createWXAPI(this, null);// 微信api对象
        EventBus.getDefault().register(this);

        mTitle = (TextView) findViewById(R.id.webview_title);
        mBack = (ImageButton) findViewById(R.id.common_app_action_bar_back);
        mWebView = (ProgressWebView) findViewById(R.id.webview_activity_webview);
        mAbsoluteLayout = (AbsoluteLayout) findViewById(R.id.webview_activity_webview_container);
        mShareImgBtn = (ImageButton) findViewById(R.id.commen_webview_share);
        mCloseTv = (TextView) findViewById(R.id.commen_webview_close);
    }

    @Override
    public int getCurrentLayoutId() {
        return R.layout.webview_layout;
    }

    @Override
    protected void onInitIntentData(Intent intent) {
        instance = this;
        super.onInitIntentData(intent);
        ShareSDK.initSDK(this);
        if (null != intent) {
            url = intent.getStringExtra(URL);
            isBackHome = intent.getBooleanExtra(IS_BACK_HOME, false);
        }
        mShareInfo = new ShareInfo();
        mShareInfo.setHref(url);
    }

    @Override
    protected void onInitLoadDatas() {
        super.onInitLoadDatas();
        mWebView.loadUrl(url);
    }

    @Override
    protected void registerListener() {
        mWebView.setOnReceiveTitleCallback(this);
        mWebView.setOnPayCallback(this);
        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                clickBack();
            }
        });
        mShareImgBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        mCloseTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static WebViewActivity getInstance() {
        if (instance == null) {
            instance = new WebViewActivity();
        }
        return instance;
    }

    public V2gogoShareDialog getShareDialog() {
        return mShareDialog;
    }

    /**
     * method desc：弹出分享对话框
     */
    public void share() {
        if (null == mShareDialog) {
            mShareDialog = new V2gogoShareDialog(this, R.style.style_action_sheet_dialog);
            mShareDialog.setItemClickCallback(this);
        }
        if (!mShareDialog.isShowing()) {
            mShareDialog.show();
        }
    }

    @Override
    public void finish() {
        mAbsoluteLayout.removeView(mWebView);
        mWebView.destoryWebview();
        if (!AppUtil.isMainIntentExist(this)) {
            Intent intent = new Intent(this, MainTabActivity.class);
            startActivity(intent);
        }
        super.finish();
    }

    @Override
    public void onReceiveTitle(String title) {
        mTitle.setText(title);
        mShareInfo.setTitle(title);
        mShareInfo.setDescription(title);
    }

    @Override
    public void clearRequestTask() {
    }

    @Override
    protected void onPause() {
        if (!TextUtils.isEmpty(url) && url.contains("refreshcoin")) {
            UserInfoManager.updateUserInfos();
        }
        mWebView.pauseWebview();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.loadUrl(url);
        mWebView.resumeWebview();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProgressWebView.FILECHOOSER_RESULTCODE) {
            if (null == mWebView.mUploadMessage)
                return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            mWebView.mUploadMessage.onReceiveValue(result);
            mWebView.mUploadMessage = null;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clickBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void clickBack() {
        if (isBackHome) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                Intent intent = new Intent(WebViewActivity.this, MainTabActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void getAlbumPath(String albumPath) {
        super.getAlbumPath(albumPath);
        if (null != albumPath) {
            PhotoUtil.forward2Crop(this, Uri.fromFile(new File(albumPath)));
        }
    }

    @Override
    protected void getCameraPath(String cameraPath) {
        super.getCameraPath(cameraPath);
        if (cameraPath != null) {
            PhotoUtil.cameraCropImageUri(this, Uri.fromFile(new File(cameraPath)));
        }
    }

    @Override
    protected void getCompressPath(Bitmap bitmap) {
        super.getCompressPath(bitmap);
        if (null != bitmap) {
            try {
                boolean result = bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(ThemePhotoUploadManager.FILE_PATH));
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                if (result) {
                    forward2Upload(ThemePhotoUploadManager.FILE_PATH);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 爆料文件上传成功回调H5
     *
     * @param url  文件路径
     * @param type 0 ： 图片 1：音频 2：视频
     */
    private void onMultiMedia(String url, int type) {
        if (mWebView != null) {
            mWebView.loadUrl("javascript:onMultiMedia('" + url + "','" + type + "');");
        }
    }

    /**
     * 获得图片文件地址
     */
    @Override
    protected void getFactAlbumPath(String albumPath) {
        super.getFactAlbumPath(albumPath);
        // 进行文件压缩
        showLoadingDialog(R.string.fact_upload_ing);
        Bitmap compressBitmap = BitmapCompressUtil.getimage(albumPath);
        // 上传文件
        uploadFactBitmap(compressBitmap);

    }

    private void uploadFactBitmap(Bitmap compressBitmap) {
        if (compressBitmap != null) {
            String path = PhotoUtil.getAvatarPath();
            File mFile = new File(path);
            if (!mFile.exists()) {
                mFile.exists();
            }
            try {
                boolean result = compressBitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(mFile));
                if (!compressBitmap.isRecycled()) {
                    compressBitmap.recycle();
                }
                if (!compressBitmap.isRecycled()) {
                    compressBitmap.recycle();
                }
                if (result) {
                    // LogUtil.e("hrx","图片大小："+mFile.getTotalSpace());
                    // showLoadingDialog(R.string.profile_modify_avatar_uploading);
                    // AccountAvatarUploadManager.modifyAccountAvatar(mFile,
                    // V2GogoApplication.getCurrentMatser().getUsername());
                    CommentUploadMultimediaManager.uploadFactMultimediaImg(mFile, new IonFactMultimediaUploadCallback() {
                        @Override
                        public void onUploadProgress(double progress) {

                        }

                        @Override
                        public void onUploadFail(int code, String errorMessage) {
                            UploadErrorInfo uploadErrorInfo = new UploadErrorInfo();
                            uploadErrorInfo.setCode(code);
                            uploadErrorInfo.setMessage(errorMessage);
                            displayUploadErrorTip(uploadErrorInfo);
                        }

                        @Override
                        public void onUploadCallback(String key) {
                            // 上传成功
                            dismissLoadingDialog();
                            ToastUtil.showInfoToast(WebViewActivity.this, R.string.fact_upload_success);
                            String imgPathStr = PhotoServerUrlConfig.PHOTO_SERVER_URL + key;
                            // H5回调
                            onMultiMedia(imgPathStr, 0);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void getFactVideoAlbumPath(String albumPath) {
        super.getFactVideoAlbumPath(albumPath);
        showLoadingDialog(R.string.fact_upload_ing);
        File mFile = new File(albumPath);
        try {
            FileInputStream fileInputStream = new FileInputStream(mFile);
            int size = fileInputStream.available();
            if (size > 500 * 1024 * 1024)// 大于500M
            {
                ToastUtil.showInfoToast(WebViewActivity.this, R.string.fact_upload_video_max_size);
                return;
            }
            CommentUploadMultimediaManager.uploadFactMultimediaVideo(mFile, new IonFactMultimediaUploadCallback() {
                @Override
                public void onUploadProgress(double progress) {

                }

                @Override
                public void onUploadFail(int code, String errorMessage) {
                    dismissLoadingDialog();
                    UploadErrorInfo uploadErrorInfo = new UploadErrorInfo();
                    uploadErrorInfo.setCode(code);
                    uploadErrorInfo.setMessage(errorMessage);
                    displayUploadErrorTip(uploadErrorInfo);
                }

                @Override
                public void onUploadCallback(String key) {
                    // 上传成功
                    dismissLoadingDialog();
                    ToastUtil.showInfoToast(WebViewActivity.this, R.string.fact_upload_success);
                    String url = PhotoServerUrlConfig.PHOTO_SERVER_URL + key;
                    onMultiMedia(url, 2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void getFactVoicePath(String voicePath) {
        super.getFactVoicePath(voicePath);
        showLoadingDialog(R.string.fact_upload_ing);
        File mFile = new File(voicePath);
        try {
            CommentUploadMultimediaManager.uploadFactMultimediaVoice(mFile, new IonFactMultimediaUploadCallback() {
                @Override
                public void onUploadProgress(double progress) {

                }

                @Override
                public void onUploadFail(int code, String errorMessage) {
                    dismissLoadingDialog();
                    UploadErrorInfo uploadErrorInfo = new UploadErrorInfo();
                    uploadErrorInfo.setCode(code);
                    uploadErrorInfo.setMessage(errorMessage);
                    displayUploadErrorTip(uploadErrorInfo);
                }

                @Override
                public void onUploadCallback(String key) {
                    // 上传成功
                    dismissLoadingDialog();
                    ToastUtil.showInfoToast(WebViewActivity.this, R.string.fact_upload_success);
                    String url = PhotoServerUrlConfig.PHOTO_SERVER_URL + key;
                    onMultiMedia(url, 1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void getFactCameraPath(Bitmap bitmap) {
        super.getFactCameraPath(bitmap);
        // 进行文件压缩
        showLoadingDialog(R.string.fact_upload_ing);
        Bitmap compressBitmap = BitmapCompressUtil.comp(bitmap);
        // 上传文件
        uploadFactBitmap(compressBitmap);
    }

    /**
     * 跳转到上传
     *
     * @param resultPhotoPath
     */
    private void forward2Upload(String resultPhotoPath) {
        Intent intent = new Intent(this, UploadPictureActivity.class);
        intent.putExtra(IntentExtraConstants.PATH, resultPhotoPath);
        intent.putExtra(IntentExtraConstants.TID, mWebView.getmTid());
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // 点击分享渠道
    @Override
    public void onShareClick(SHARE_TYPE type) {

        String tip = getResources().getString(R.string.share_success_tip);
        com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN;
        if (type == SHARE_TYPE.SHARE_WENXI_COLLECTIONS) {
            shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_WEIXIN_FRIENDS;
        } else if (type == SHARE_TYPE.SHARE_QQ) {
            shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_QQ;
        } else if (type == SHARE_TYPE.SHARE_QZONE) {
            shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_QZONE;
        } else if (type == SHARE_TYPE.SHARE_MESSAGE) {
            shareType = com.v2gogo.project.utils.share.ShareUtils.SHARE_TYPE.SHARE_MESSAGE;
        }
        if (mWebView != null && mWebView.getShareInfo() != null) {
            mShareInfo = mWebView.getShareInfo();
        }
        ShareUtils.share(this, mShareInfo.getTitle(), mShareInfo.getDescription(), mShareInfo.getHref(), mShareInfo.getImageUrl(), shareType,
                new CustomPlatformActionListener(this, tip));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 接收上传图片成功的消息
     *
     * @param progress
     */
    public void onEventMainThread(ThemePhotoUploadResultInfo photoUploadResultInfo) {
        if (null != photoUploadResultInfo && photoUploadResultInfo.getmThemePhotoInfo() != null) {
            String imgPathStr = photoUploadResultInfo.getmThemePhotoInfo().getPhotoImg();
            String userName = V2GogoApplication.getCurrentMatser().getUsername();
            mWebView.loadUrl("javascript:replaceImgSrc('" + imgPathStr + "','" + userName + "');");
        }
    }

    // ==============实现接口方法===================
    @Override
    public void onPay(String ProductId, String buyNum, String subType) {
        pay(ProductId, buyNum, subType);
    }

    /**
     * 支付
     *
     * @param payType
     */
    private void pay(String projectId, String amount, final String payType) {
        UnionPayManager.getLovePayTransactionNo(projectId, "2", amount, new IOnDataReceiveMessageCallback() {

            @Override
            public void onSuccess(int code, String message, JSONObject response) {
                if (StatusCode.SUCCESS == code)// 成功
                {
                    if (payType != null && payType.equals("121"))// 微信支付
                    {
                        // 流水交易号
                        JSONObject jsonObject = response.optJSONObject("result");
                        String prepayId = jsonObject.optString("prepayid", "");
                        startWeixinPay(prepayId);
                    } else {

                    }
                } else {// 失败
                    ToastUtil.showAlertToast(WebViewActivity.this, message);
                }
            }

            @Override
            public void onError(String errorMessage) {
                ToastUtil.showAlertToast(WebViewActivity.this, errorMessage);
            }
        });
    }

    /**
     * method desc：开始银联支付
     */
    private void startWeixinPay(String prepayId) {
        mApi.registerApp(ServerUrlConfig.PAY_WEIXIN_APPID);

        String nonceStr = getRandomString(32);
        String timeStamp = (int) (System.currentTimeMillis() / 1000) + "";

        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", ServerUrlConfig.PAY_WEIXIN_APPID);
        parameters.put("partnerid", ServerUrlConfig.PAY_WEIXIN_PARTERID);
        parameters.put("prepayid", prepayId);
        parameters.put("package", "Sign=WXPay");
        parameters.put("noncestr", nonceStr);
        parameters.put("timestamp", timeStamp);

        String sign = createSign(parameters);

        PayReq request = new PayReq();
        request.appId = ServerUrlConfig.PAY_WEIXIN_APPID;
        request.partnerId = ServerUrlConfig.PAY_WEIXIN_PARTERID;
        request.prepayId = prepayId;
        request.packageValue = "Sign=WXPay";
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;

        mApi.sendReq(request);
    }

    /**
     * method desc：随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 微信支付签名算法sign
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    public String createSign(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + ServerUrlConfig.PAY_WEIXIN_MIYAO);
        // sb.append("key=wjzpgz2015D52DFdsf256546DSF23564");
        String sign = MD5Util.getMD5String(sb.toString()).toUpperCase();
        return sign;
    }

}
