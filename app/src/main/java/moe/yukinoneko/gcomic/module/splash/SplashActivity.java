package moe.yukinoneko.gcomic.module.splash;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.BaseActivity;
import moe.yukinoneko.gcomic.base.IBaseView;
import moe.yukinoneko.gcomic.module.main.MainActivity;
import moe.yukinoneko.gcomic.utils.SnackbarUtils;

/**
 * Created by SamuelGjk on 2016/5/2.
 */
public class SplashActivity extends BaseActivity<SplashPresenter> implements IBaseView {

    private final int REQUEST_CODE_ASK_PERMISSIONS = 10001;

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initPresenter() {
        presenter = new SplashPresenter(this, this);
        presenter.init();
    }

    @Override
    public void init() {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.ask_permission_msg)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            showPermissionDeniedSnackbar();
                        }
                    })
                    .show();
        } else {
            toMainActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults.length > 0) {
                boolean granted = true;
                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        showPermissionDeniedSnackbar();
                        granted = false;
                        break;
                    }
                }

                if (granted) {
                    toMainActivity();
                }
            } else {
                showPermissionDeniedSnackbar();
            }
        }
    }

    private void showPermissionDeniedSnackbar() {
        SnackbarUtils.showShort(findViewById(android.R.id.content), R.string.permission_denied);
    }

    private void toMainActivity() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        mHandler.postDelayed(mRunnable, 1000);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }
}
