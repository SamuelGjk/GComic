package moe.yukinoneko.gcomic.utils;

import java.io.ByteArrayOutputStream;
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
//    public static Observable<List<byte[]>> readFileContent(final String path) {
//        return Observable.defer(new Func0<Observable<List<byte[]>>>() {
//            @Override
//            public Observable<List<byte[]>> call() {
//                List<byte[]> result = new ArrayList<>();
//                try {
//                    ZipFile zf = new ZipFile(path);
//                    InputStream is = new BufferedInputStream(new FileInputStream(path));
//                    ZipInputStream zis = new ZipInputStream(is);
//                    ZipEntry ze;
//                    while ((ze = zis.getNextEntry()) != null) {
//                        long size = ze.getSize();
//                        if (size > 0) {
//                            InputStream zeis = zf.getInputStream(ze);
//                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                            byte[] buffer = new byte[1024];
//                            int len;
//                            while ((len = zeis.read(buffer, 0, buffer.length)) != -1) {
//                                bos.write(buffer, 0, len);
//                            }
//                            result.add(bos.toByteArray());
//                            bos.close();
//                            zeis.close();
//                        }
//                    }
//                    zis.closeEntry();
//                    zis.close();
//                    is.close();
//                    zf.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return Observable.just(result);
//            }
//        }).subscribeOn(Schedulers.io());
//    }

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
}
