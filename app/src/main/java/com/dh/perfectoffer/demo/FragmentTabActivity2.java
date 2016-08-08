package com.dh.perfectoffer.demo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.dh.perfectoffer.R;
import com.dh.perfectoffer.VehicleApp;
import com.dh.perfectoffer.constant.Constant;
import com.dh.perfectoffer.dhutils.CommUtil;
import com.dh.perfectoffer.dhutils.UpgradeAppBL;
import com.dh.perfectoffer.dhutils.swipbackhelper.SwipeBackHelper;
import com.dh.perfectoffer.entity.CheckVersionBean;
import com.dh.perfectoffer.event.framework.alibaba.fastjson.JSON;
import com.dh.perfectoffer.event.framework.net.exception.DataException;
import com.dh.perfectoffer.event.framework.net.fgview.Action;
import com.dh.perfectoffer.event.framework.net.fgview.BaseParser;
import com.dh.perfectoffer.event.framework.net.fgview.OnResponseListener;
import com.dh.perfectoffer.event.framework.net.fgview.Request;
import com.dh.perfectoffer.event.framework.net.fgview.Response;
import com.dh.perfectoffer.event.framework.net.fgview.Response.ErrorMsg;
import com.dh.perfectoffer.event.framework.util.Density;
import com.dh.perfectoffer.fragment.CompanyFragment;
import com.dh.perfectoffer.fragment.FragmentTabHost;
import com.dh.perfectoffer.fragment.HomeFragment;
import com.dh.perfectoffer.fragment.MoreFragment;
import com.dh.perfectoffer.fragment.MyFragment;
import com.dh.perfectoffer.view.UpdateTipDialog;
import com.dh.perfectoffer.xutils.sample.utils.PubUtils;
import com.dh.perfectoffer.xutils.sample.utils.ToastUtil;

import java.util.HashMap;


/**
 * @author 超级小志
 */
public class FragmentTabActivity2 extends VehicleActivity {
    public static final String HOME_TAB_TYPE = "tab_type";
    public static final String TAB_INDEX = "悬赏";
    public static final String TAB_LEIGOU = "公司";

    public static final String TAB_MY = "我";
    public static final String TAB_SETTING = "更多";

    private FragmentTabHost mTabHost;
    private View v_indicator;

    private int preTabPosition;
    private int currentTabPosition;
    private long exitTime = 0;
    private static FragmentTabActivity2 instance;

    public static FragmentTabActivity2 getInstance() {
        if (null == instance) {
            instance = new FragmentTabActivity2();
        }
        return instance;
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.tab_layout2);
        instance = this;
        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        addTab(TAB_INDEX, R.drawable.icon_fragment_rework_d,
                R.drawable.icon_fragment_rework_n, HomeFragment.class);
        addTab(TAB_LEIGOU, R.drawable.icon_fragment_company_d,
                // R.drawable.home_tab_leigou, LejiaquanFragment.class);
                R.drawable.icon_fragment_company_n, CompanyFragment.class);
        addTab(TAB_MY, R.drawable.icon_fragment_my_d,
                R.drawable.icon_fragment_my_n, MyFragment.class);
        addTab(TAB_SETTING, R.drawable.icon_fragment_more_d,
                R.drawable.icon_fragment_more_n, MoreFragment.class);

        v_indicator = findViewById(R.id.v_indicator);

        // 获取屏幕宽度的1/4
        final int indicator_width = Density.getSceenWidth(this) / 4;

        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {// 设置v_indicator
                if (tabId.equals(TAB_INDEX)) {
                    currentTabPosition = 0;
                } else if (tabId.equals(TAB_LEIGOU)) {
                    currentTabPosition = 1;
                } else if (tabId.equals(TAB_MY)) {
                    currentTabPosition = 2;
                } else if (tabId.equals(TAB_SETTING)) {
                    currentTabPosition = 3;
                }
                // tab菜单处理
                if (currentTabPosition != preTabPosition) {
                    int from_x, to_x;
                    if (currentTabPosition > preTabPosition) {
                        from_x = currentTabPosition * indicator_width
                                - indicator_width;
                        to_x = from_x + indicator_width;
                    } else {
                        from_x = currentTabPosition * indicator_width
                                + indicator_width;
                        to_x = from_x - indicator_width;
                    }
                    Animation animation = new TranslateAnimation(from_x, to_x,
                            0, 0);
                    animation.setDuration(300);
                    animation.setFillAfter(true);
                    v_indicator.startAnimation(animation);
                    preTabPosition = currentTabPosition;
                }
            }
        });
        // 显示第一个tab
        mTabHost.setCurrentTab(0);
        checkVersion(CommUtil.getVersionCode(this) + "", false);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false);
    }

    public void toNextTab(int position) {
        mTabHost.setCurrentTab(position);
    }

    // 增加TAB,

    /**
     * @param paramString        标示
     * @param selectedDrawableId 选中时图片ID
     * @param normalDrawableId   正常时图片ID
     * @param paramClass         对应的Class
     */
    public void addTab(String paramString, int selectedDrawableId,
                       int normalDrawableId, Class<? extends Fragment> paramClass) {
        LinearLayout indicator = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.tab_indicator_holo2, null);
        TextView tv_text = (TextView) indicator.findViewById(R.id.tv_text);
        ImageView iv_icon = (ImageView) indicator.findViewById(R.id.iv_icon);
        tv_text.setText(paramString);
        iv_icon.setImageDrawable(getCheckedSelector(this, selectedDrawableId,
                normalDrawableId));
        mTabHost.addTab(mTabHost.newTabSpec(paramString)
                .setIndicator(indicator), paramClass, null);
    }

    /*
     * 保证第一次启动页面后，可以调用执行fragment中的刷新相应方法
     */
    @Override
    protected void onStart() {
        super.onStart();
        refreshTabFragment();
    }

    /**
     * 判断当前tab，执行刷新操作
     */
    public void refreshTabFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment f = supportFragmentManager.findFragmentByTag(TAB_LEIGOU);
        if (null != f && f instanceof CompanyFragment) {
            ((CompanyFragment) f).refreshData(false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent) {
            String tabType = intent.getStringExtra(HOME_TAB_TYPE);
            if (!TextUtils.isEmpty(tabType)) {
                if (tabType.equals(TAB_INDEX)) {
                    currentTabPosition = 0;
                } else if (tabType.equals(TAB_LEIGOU)) {
                    currentTabPosition = 1;
                } else if (tabType.equals(TAB_MY)) {
                    currentTabPosition = 2;
                } else if (tabType.equals(TAB_SETTING)) {
                    currentTabPosition = 3;
                }
                mTabHost.setCurrentTab(currentTabPosition);
            }

        }
    }

    /**
     * tab选中与否的样式显示
     *
     * @param context
     * @param idSelectedId
     * @param idNormalId
     * @return
     */
    private static StateListDrawable getCheckedSelector(Context context,
                                                        int idSelectedId, int idNormalId) {

        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormalId == -1 ? null : context.getResources()
                .getDrawable(idNormalId);
        Drawable selected = idSelectedId == -1 ? null : context.getResources()
                .getDrawable(idSelectedId);
        bg.addState(new int[]{android.R.attr.state_selected}, selected);
        bg.addState(new int[]{android.R.attr.state_pressed}, selected);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        super.onActivityResult(requestCode, resultCode, arg2);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                PubUtils.popTipOrWarn(this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                VehicleApp.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示需要升级的对话框
     *
     * @param context
     */
    // public void showNeedUpgradeDialog(Context context, String url) {
    // AlertDialog.Builder adBuilder = new AlertDialog.Builder(context);
    // adBuilder.setTitle(getResources().getString(
    // R.string.update_dialog_title));
    // adBuilder.setMessage(getResources().getString(
    // R.string.update_dialog_iscanupdate));
    // adBuilder.setPositiveButton(getResources().getString(R.string.btn_ok),
    // new UpgradeBtnClick(context, url));
    // adBuilder.setNegativeButton(getResources().getString(R.string.cancel),
    // null);
    // adBuilder.show();
    // }
    public void showNeedUpgradeDialog(final Context context, final String url) {
        final UpdateTipDialog dialog = new com.dh.perfectoffer.view.UpdateTipDialog(context);
        dialog.showUpdateTip(
                getResources().getString(R.string.update_dialog_title),
                getResources().getString(R.string.update_dialog_iscanupdate),
                getResources().getString(R.string.cancel), getResources()
                        .getString(R.string.btn_ok),
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        new UpgradeBtnClick(context, url);
                        dialog.dismiss();
                    }
                });
    }

    // 升级确认按钮点击事件
    public static class UpgradeBtnClick implements OnClickListener {

        private Context context = null;

        private String downloadUrl = null;

        public UpgradeBtnClick(Context context, String downloadUrl) {
            this.context = context;
            this.downloadUrl = downloadUrl;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (this.context == null) {
                Log.w("UpgradeBtnClick.onClick", "Context is null");
                return;
            }
            if (downloadUrl != null && downloadUrl.length() > 1) {
                // Intent in = new Intent(Intent.ACTION_VIEW,
                // Uri.parse(downloadUrl));
                // this.context.startActivity(in);
                UpgradeAppBL upManage = new UpgradeAppBL(this.context);
                upManage.showDownloadDialog(downloadUrl);
            } else {
                Log.w("UpgradeBtnClick.onClick", "downloadUrl is null");
            }
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }
    }

    ;

    public void checkVersion(String versionCode, final boolean isShowToast) {

        HashMap<String, String> pram = new HashMap<String, String>();
        pram.put("version_no", versionCode);

        // 请求parser
        Request<CheckVersionBean> req = new Request<CheckVersionBean>();
        req.setRequestMethod(Request.M_POST);
        req.setBaseParser(new BaseParser<CheckVersionBean>() {
            @Override
            public CheckVersionBean parseResDate(String resBody)
                    throws DataException {
                if (resBody != null && !resBody.equals("")) {
                    // Preference.putString(Constant.PRE_USER_NAME, resBody);
                    return JSON.parseObject(resBody, CheckVersionBean.class);

                }
                return null;
            }
        });
        // req.setBodyRequestParam(bodyRequestParam);
        req.setRequestParams(pram);
        req.setUrl(Constant.URLIP + "webhr/UpdateServlet");

        Action action = new Action(FragmentTabActivity2.this);
        if (isShowToast) {
            action.setDefaultLoadingTipOpen(true);
            action.setShowErrorDialog(true);
        } else {
            action.setDefaultLoadingTipOpen(false);
            action.setShowErrorDialog(false);
        }
        action.execute(req, new OnResponseListener<CheckVersionBean>() {
            @Override
            public void onResponseFinished(Request<CheckVersionBean> request,
                                           Response<CheckVersionBean> response) {
                CheckVersionBean bean = new CheckVersionBean();
                bean = response.getT();
                if (null != bean) {
                    if (bean.getSuccess().equals("1")) {
                        if (bean.getVersion_lable().equals("1"))
                            showNeedUpgradeDialog(FragmentTabActivity2.this,
                                    Constant.URLIPPIC + bean.getVersion_url());
                        else if (isShowToast) {
                            ToastUtil.showShort(FragmentTabActivity2.this,
                                    "已经最新版本，不需要更新！");
                        }
                    }
                }
            }

            @Override
            public void onResponseDataError(Request<CheckVersionBean> equest) {
            }

            @Override
            public void onResponseConnectionError(
                    Request<CheckVersionBean> request, int statusCode) {
            }

            @Override
            public void onResponseFzzError(Request<CheckVersionBean> request,
                                           ErrorMsg errorMsg) {
            }

        });

    }
}
