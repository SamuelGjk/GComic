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

package moe.yukinoneko.gcomic.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by SamuelGjk on 2016/5/19.
 */
public class FileUtlis {
    public static final String FILE_SUFFIX = ".jpg";

    public static Observable<List<byte[]>> readFileContent(final String path) {
        return Observable.defer(new Func0<Observable<List<byte[]>>>() {
            @Override
            public Observable<List<byte[]>> call() {
                List<byte[]> result = new ArrayList<>();
                try {
                    ZipFile zf = new ZipFile(path);
                    ZipEntry ze;
                    for (int i = 0; i < zf.size(); i++) {
                        ze = zf.getEntry(i + FILE_SUFFIX);
                        long size = ze.getSize();
                        if (size > 0) {
                            InputStream zeis = zf.getInputStream(ze);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = zeis.read(buffer, 0, buffer.length)) != -1) {
                                bos.write(buffer, 0, len);
                            }
                            result.add(bos.toByteArray());
                            bos.close();
                            zeis.close();
                        }
                    }
                    zf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return Observable.just(result);
            }
        }).subscribeOn(Schedulers.io());
    }

    public static void deleteFiles(List<String> paths) {
        for (String path : paths) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
