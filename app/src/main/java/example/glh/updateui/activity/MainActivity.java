package example.glh.updateui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import example.glh.updateui.R;
import example.glh.updateui.bean.VersionBean;
import example.glh.updateui.downloadapk.DownloadApk;
import example.glh.updateui.utils.GetSign;
import example.glh.updateui.utils.StringUtils;
import example.glh.updateui.utils.VersionUtil;
import example.glh.updateui.widget.CommonDialog;
import example.glh.updateui.widget.OnMyNegativeListener;
import example.glh.updateui.widget.OnMyPositiveListener;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_APP_INSTALL = 2;
    private CommonDialog updateDialog;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //去请求版本号
        requestVersion();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO(Context context) {
        if (context == null) {
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }

    /**
     * 开启设置安装未知来源应用权限界面
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        startActivityForResult(intent, REQUEST_CODE_APP_INSTALL);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_APP_INSTALL:
                    if (!StringUtils.isEmpty(url)) {
                        //下载apk
                        DownloadApk.getInstance().downLoadApk(MainActivity.this, url);
                    }
                    break;
            }
        }
    }

    private void requestVersion() {
        String str = GetSign.getSign();
        String[] strs = str.split(",");
        OkHttpUtils.post()
                .url("https://glh.zlz99.com/" + "/Api/Versions/check_versions")
                .addParams("t", strs[0])
                .addParams("r", strs[1])
                .addParams("e", strs[2])
                .addParams("type", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        VersionBean bean = JSON.parseObject(response, VersionBean.class);
                        int versionCode = VersionUtil.getVersionCode(MainActivity.this);
                        if (bean.getData() != null && bean.getData().getData() != null) {
                            VersionBean.DataBeanX.DataBean data = bean.getData().getData();
                            String number = data.getNumber();
                            url = data.getUrl();
                            double version = Double.parseDouble(number);
                            //如果服务端的版本号大于本地的版本号
                            if (version > versionCode) {
                                showUpdateDialog(MainActivity.this, data);
                            }
                        }
                    }
                });
    }

    public void showUpdateDialog(final Context mContext, VersionBean.DataBeanX.DataBean bean) {
        String updateContent = bean.getContent();
        final String url = bean.getUrl();
        updateDialog = new CommonDialog(mContext, "发现新版本啦", updateContent, "更新", "暂不更新",
                new OnMyPositiveListener() {
                    @Override
                    public void onClick() {
                        //适配Android8.0以上
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            boolean hasInstallPermission = isHasInstallPermissionWithO(MainActivity.this);
                            if (!hasInstallPermission) {
                                showRequestPermissionDialog();
                            } else {
                                //下载apk
                                DownloadApk.getInstance().downLoadApk(MainActivity.this, url);
                            }
                        } else {
                            //Android8.0以下
                            //下载apk
                            DownloadApk.getInstance().downLoadApk(MainActivity.this, url);
                        }
                    }
                }, new OnMyNegativeListener() {
            @Override
            public void onClick() {
                super.onClick();
            }
        });
        updateDialog.show();
    }

    private void showRequestPermissionDialog() {
        CommonDialog requestPermission = new CommonDialog(this, "您还没有允许应用安装未知应用权限", "是否去允许", "是", "否",
                new OnMyPositiveListener() {
                    @Override
                    public void onClick() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            //跳转页面判断允许安卓位置应用权限
                            startInstallPermissionSettingActivity(MainActivity.this);
                        }
                    }
                }, new OnMyNegativeListener() {
            @Override
            public void onClick() {
                super.onClick();
            }
        });
        requestPermission.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        initPermission();
    }

    private void initPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE
                )

                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {

                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        /**
                         * 当用户没有允许该权限时，回调该方法
                         */
                        Toast.makeText(MainActivity.this, "没有获取相关权限，可能导致相关功能无法使用", Toast.LENGTH_SHORT).show();
                        /**
                         * 判断用户是否点击了禁止后不再询问，AndPermission.hasAlwaysDeniedPermission(MainActivity.this, data)
                         */
                        if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, data)) {
                            //true，弹窗再次向用户索取权限
                        }
                    }
                }).start();
    }
}