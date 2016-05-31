package moe.yukinoneko.gcomic.module.details;

import moe.yukinoneko.gcomic.data.ComicData;
import moe.yukinoneko.gcomic.base.IBaseView;
import moe.yukinoneko.gcomic.database.model.ReadHistoryModel;

/**
 * Created by SamuelGjk on 2016/4/19.
 */
public interface IComicDetailsView extends IBaseView {
    void showMessageSnackbar(String message);

    void updateComicDetailsContent(ComicData comicData);

    void updateFavoriteMenu(boolean isFavorite);

    void updateReadHistory(ReadHistoryModel history);
}
