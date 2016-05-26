package moe.yukinoneko.gcomic;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;

/**
 * Created by SamuelGjk on 2016/5/12.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FileDownloadLog.NEED_LOG = true;

        FileDownloader.init(getApplicationContext());
    }
}
