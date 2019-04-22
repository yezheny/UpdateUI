package example.glh.updateui.downloadapk;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import example.glh.updateui.R;

/**
 * Created by ${YGP} on 2017/7/18.
 */

public class DownloadApk {

    private volatile static DownloadApk mInstance = null;

    private DownloadApk() {

    }

    public static DownloadApk getInstance() {
        if (mInstance == null) {
            synchronized (DownloadApk.class) {
                if (mInstance == null) {
                    mInstance = new DownloadApk();
                }
            }
        }
        return mInstance;
    }

    public void downLoadApk(final Context mContext, final String url) {
        //进度条
        mProgressDialog = new Dialog(mContext, R.style.FullHeightDialog);
        mProgressDialog.setContentView(R.layout.loading_dialog);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgress = mProgressDialog.findViewById(R.id.progressBar);
        mTvProgressCount = mProgressDialog.findViewById(R.id.tv_progress_count);
        ImageView ivDismiss = mProgressDialog.findViewById(R.id.iv_dismiss);
        ivDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "后台下载中...", Toast.LENGTH_SHORT);
                progressDismiss();
            }
        });
        mProgress.setMax(100);
        mProgressDialog.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(url);
                    //安装APK
                    installApk(file, mContext);
                    progressDismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void progressDismiss() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private Dialog mProgressDialog;
    private ProgressBar mProgress;
    private TextView mTvProgressCount;
    private long curTime = 0;

    public File getFileFromServer(String path) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            long contentLength = conn.getContentLength();
            InputStream is = conn.getInputStream();
            // /storage/emulated/0
            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                if (System.currentTimeMillis() - curTime > 1000 || total == contentLength) {
                    //获取当前下载量
                    long progress = total * 100L / contentLength;
                    //设置UI在主线程中，直接设置会报异常
                    mProgress.setProgress((int) progress);
                    Message message = new Message();
                    message.what = 0;
                    message.arg1 = (int) progress;
                    handler.sendMessage(message);
                    curTime = System.currentTimeMillis();
                }
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    public void installApk(File file, Context mContext) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // Android7.0及以上版本
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                //Uri contentUri = FileProvider.getUriForFile(mContext, "应用包名" + ".fileProvider", file);//参数二:应用包名+".fileProvider"(和步骤一中的Manifest文件中的provider节点下的authorities对应) 
                Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".fileprovider", file);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");

            } else {
                // Android7.0以下版本
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            //progressDismiss();
        }
        /*Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        mContext.startActivity(intent);*/
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    int progress = msg.arg1;
                    mTvProgressCount.setText(progress + "%");
                    break;
                default:
                    break;
            }
        }
    };
}
