package moe.yukinoneko.gcomic.module.gallery;

import android.content.Context;
import android.util.Log;

import java.util.List;

import moe.yukinoneko.gcomic.base.BasePresenter;
import moe.yukinoneko.gcomic.data.ChapterData;
import moe.yukinoneko.gcomic.network.GComicApi;
import moe.yukinoneko.gcomic.utils.FileUtlis;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by SamuelGjk on 2016/4/20.
 */
public class GalleryPresenter extends BasePresenter<IGalleryView> {
    public static final String TAG = "GalleryPresenter";

    public GalleryPresenter(Context context, IGalleryView iView) {
        super(context, iView);
    }

    void fetchChapterDetails(int comicId, int chapterId) {
        Log.d(TAG, "NETWORK");
        Subscription subscription = GComicApi.getInstance()
                                             .fetchChapterDetails(comicId, chapterId)
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .subscribe(new Action1<ChapterData>() {
                                                 @Override
                                                 public void call(ChapterData chapterData) {
                                                     iView.updatePagerContent(chapterData.pageUrl, null, false);
                                                 }
                                             }, new Action1<Throwable>() {
                                                 @Override
                                                 public void call(Throwable throwable) {
                                                     iView.showMessageSnackbar(throwable.getMessage());
                                                     iView.fetchFailure();
                                                 }
                                             });
        addSubscription(subscription);
    }

    void fetchPicturesFromDisk(String path) {
        Log.d(TAG, "DISK");
        Subscription subscription = FileUtlis.readFileContent(path)
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .subscribe(new Action1<List<byte[]>>() {
                                                 @Override
                                                 public void call(List<byte[]> bytes) {
                                                     iView.updatePagerContent(null, bytes, true);
                                                 }
                                             });
        addSubscription(subscription);
    }
}
