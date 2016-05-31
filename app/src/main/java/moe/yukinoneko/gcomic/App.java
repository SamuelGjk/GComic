/*
 * Copyright (C) 2016  SamuelGjk <samuel.alva@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
