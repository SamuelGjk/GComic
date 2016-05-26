package moe.yukinoneko.gcomic.module.download.tasks;

import java.util.List;

import moe.yukinoneko.gcomic.base.IBaseView;
import moe.yukinoneko.gcomic.data.ComicData;
import moe.yukinoneko.gcomic.database.model.DownloadTaskModel;

/**
 * Created by SamuelGjk on 2016/5/13.
 */
public interface IDownloadTasksView extends IBaseView {
    void showMessageSnackbar(String message);

    void updateDownloadTasksList(List<DownloadTaskModel> tasks);

    void setComicFullChapters(List<ComicData.ChaptersBean.ChapterBean> chapters);
}
