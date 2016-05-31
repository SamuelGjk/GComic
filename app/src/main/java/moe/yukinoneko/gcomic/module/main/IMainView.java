package moe.yukinoneko.gcomic.module.main;

import java.util.List;

import moe.yukinoneko.gcomic.base.IBaseView;
import moe.yukinoneko.gcomic.database.model.SearchHistoryModel;

/**
 * Created by SamuelGjk on 2016/5/30.
 */
public interface IMainView extends IBaseView {
    String getSearchKeyword();

    void updateSearchSuggestions(List<SearchHistoryModel> suggestions);
}
