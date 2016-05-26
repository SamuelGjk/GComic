package moe.yukinoneko.gcomic.module.download;

import java.util.List;

import moe.yukinoneko.gcomic.base.IBaseView;
import moe.yukinoneko.gcomic.database.model.DownloadTaskModel;

/**
 * Created by SamuelGjk on 2016/5/13.
 */
public interface IDownloadedComicView extends IBaseView {
    void updateDownloadedComicList(List<DownloadTaskModel> comics);
}
