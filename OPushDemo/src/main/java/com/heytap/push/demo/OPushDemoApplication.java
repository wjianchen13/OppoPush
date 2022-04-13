package com.heytap.push.demo;

import android.app.Application;

import com.heytap.push.demo.util.LogUtil;

/**
 * <p>Title:${Title} </p>
 * <p>Description: OPushDemoApplication</p>
 * <p>Copyright (c) 2016 www.oppo.com Inc. All rights reserved.</p>
 * <p>Company: OPPO</p>
 *
 * @author QuWanxin
 * @version 1.0
 * @date 2017/7/27
 */

public class OPushDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.setDebugs(true);
    }
}
