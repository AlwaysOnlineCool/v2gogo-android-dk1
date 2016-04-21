/**
 * @{#} StartViewPagerAdActivity.java Create on 2015-12-24 上午11:09:20
 * <p/>
 * Copyright (c) 2013 by BlueSky.
 * @author <a href="1084986314@qq.com">BlueSky</a>
 * @version 1.0
 */
package com.v2gogo.project.activity;

import java.util.ArrayList;

import android.support.v4.view.ViewPager;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.WelcomeItemInfo;

/**
 * 功能：
 *
 * @ahthor：黄荣星
 * @date:2015-12-24
 * @version::V1.0
 */
public class StartViewPagerAdActivity extends BaseActivity {

    private ViewPager mViewPager;
    private ArrayList<WelcomeItemInfo> mWelcomeItemInfos;

    @Override
    public void clearRequestTask() {

    }

    @Override
    public void onInitViews() {
        mViewPager = (ViewPager) findViewById(R.id.ad_conatiner);
    }

    @Override
    public int getCurrentLayoutId() {
        return R.layout.start_view_pager_ad_activity;
    }

}
