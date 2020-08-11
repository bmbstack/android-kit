package com.bmbstack.kit.app;

import com.bmbstack.kit.umeng.UmengAgent;
import com.bmbstack.kit.util.XUtils;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

/**
 * Created by wangming on 4/18/17.
 */

public class App extends BaseApplication {
    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        AppHook.checkUpgrade();


        if (!AndPermission.hasPermissions(this, Permission.WRITE_EXTERNAL_STORAGE)) {
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.WRITE_EXTERNAL_STORAGE)
                    .onGranted(permissions -> {

                    })
                    .onDenied(permissions -> {

                    })
                    .start();
        }

        // Umeng设置
        String channelId = XUtils.getUmengChannel(this, "bmbstack");
        UmengAgent.setKeySecretWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        UmengAgent.init(getApplicationContext(), "58f3107365b6d6275d000454", channelId, null, true,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);

        initSmartRefresh();
    }

    @Override
    protected boolean appBuildConfigDebug() {
        return BuildConfig.DEBUG;
    }

    private void initSmartRefresh() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, refreshLayout) -> {
            refreshLayout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
            return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, refreshLayout) -> new ClassicsFooter(context).setDrawableSize(20));
    }
}
