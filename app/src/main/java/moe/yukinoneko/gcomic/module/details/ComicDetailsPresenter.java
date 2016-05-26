package moe.yukinoneko.gcomic.module.details;

import android.content.Context;

import java.util.ArrayList;

import moe.yukinoneko.gcomic.base.BasePresenter;
import moe.yukinoneko.gcomic.data.ComicData;
import moe.yukinoneko.gcomic.database.GComicDB;
import moe.yukinoneko.gcomic.database.model.DownloadTaskModel;
import moe.yukinoneko.gcomic.database.model.FavoriteModel;
import moe.yukinoneko.gcomic.download.DownloadTasksManager;
import moe.yukinoneko.gcomic.network.GComicApi;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;

/**
 * Created by SamuelGjk on 2016/4/19.
 */
public class ComicDetailsPresenter extends BasePresenter<IComicDetailsView> {

    public ComicDetailsPresenter(Context context, IComicDetailsView iView) {
        super(context, iView);
    }

    void fetchComicDetails(int comicId) {
        Subscription subscription = Observable.zip(GComicApi.getInstance().fetchComicDetails(comicId),
                                                    DownloadTasksManager.getInstance(mContext).getTasksByComicId(new String[]{ String.valueOf(comicId) }),
                                                    new Func2<ComicData, ArrayList<DownloadTaskModel>, ComicData>() {
                                                        @Override
                                                        public ComicData call(ComicData comicData, ArrayList<DownloadTaskModel> downloadTaskModels) {
                                                            for (DownloadTaskModel model : downloadTaskModels) {
                                                                comicData.downloadedChapters.add(model.chapterId);
                                                            }
                                                            return comicData;
                                                        }
                                                    })
                                              .observeOn(AndroidSchedulers.mainThread())
                                              .subscribe(new Action1<ComicData>() {
                                                  @Override
                                                  public void call(ComicData comicData) {
                                                      iView.updateComicDetailsContent(comicData);
                                                  }
                                              }, new Action1<Throwable>() {
                                                  @Override
                                                  public void call(Throwable throwable) {
                                                      iView.showMessageSnackbar(throwable.getMessage());
                                                  }
                                              });
//        Subscription subscription = GComicApi.getInstance()
//                                             .fetchComicDetails(comicId)
//                                             .observeOn(AndroidSchedulers.mainThread())
//                                             .subscribe(new Action1<ComicData>() {
//                                                 @Override
//                                                 public void call(ComicData comicData) {
//                                                     iView.updateComicDetailsContent(comicData);
//                                                 }
//                                             }, new Action1<Throwable>() {
//                                                 @Override
//                                                 public void call(Throwable throwable) {
//                                                     iView.showMessageSnackbar(throwable.getMessage());
//                                                 }
//                                             });
        addSubscription(subscription);
    }

    void favoriteComic(int comicId, String comicTitle, String comicAuthors, String comicCover) {
        Subscription subscription = GComicDB.getInstance(mContext)
                                            .insert(new FavoriteModel(comicId, comicTitle, comicAuthors, comicCover))
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Action1<Long>() {
                                                @Override
                                                public void call(Long aLong) {
                                                    iView.updateFavoriteMenu(aLong != -1);
                                                }
                                            });
        addSubscription(subscription);
    }

    void unFavoriteComic(int comicId) {
        Subscription subscription = GComicDB.getInstance(mContext)
                                            .deleteWhere(FavoriteModel.class, "comicId", new String[]{ String.valueOf(comicId) })
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Action1<Integer>() {
                                                @Override
                                                public void call(Integer integer) {
                                                    iView.updateFavoriteMenu(!(integer > 0));
                                                }
                                            });
        addSubscription(subscription);
    }

    void isFavorite(int comicId) {
        Subscription subscription = GComicDB.getInstance(mContext)
                                            .queryByWhere(FavoriteModel.class, "comicId", new String[]{ String.valueOf(comicId) })
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Action1<ArrayList<FavoriteModel>>() {
                                                @Override
                                                public void call(ArrayList<FavoriteModel> favoriteModels) {
                                                    iView.updateFavoriteMenu(!favoriteModels.isEmpty());
                                                }
                                            });
        addSubscription(subscription);
    }
}
