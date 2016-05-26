package moe.yukinoneko.gcomic.module.search;

import java.util.List;

import moe.yukinoneko.gcomic.base.IBaseView;
import moe.yukinoneko.gcomic.data.SearchData;

/**
 * Created by SamuelGjk on 2016/4/18.
 */
public interface ISearchView extends IBaseView {
    void showMessageSnackbar(String message);

    void setRefreshing(boolean refreshing);

    void updateResultList(List<SearchData> searchDatas);
}
