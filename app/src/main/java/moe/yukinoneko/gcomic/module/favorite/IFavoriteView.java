package moe.yukinoneko.gcomic.module.favorite;

import java.util.List;

import moe.yukinoneko.gcomic.base.IBaseView;
import moe.yukinoneko.gcomic.database.model.FavoriteModel;

/**
 * Created by SamuelGjk on 2016/5/9.
 */
public interface IFavoriteView extends IBaseView {
    void setRefreshing(boolean refreshing);

    void updateFavoriteList(List<FavoriteModel> favorites);
}
