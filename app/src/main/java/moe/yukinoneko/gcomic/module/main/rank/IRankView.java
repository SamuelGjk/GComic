package moe.yukinoneko.gcomic.module.main.rank;

import java.util.List;

import moe.yukinoneko.gcomic.data.RankData;
import moe.yukinoneko.gcomic.base.IBaseView;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public interface IRankView extends IBaseView {
    void showMessageSnackbar(String message);

    void setRefreshing(boolean refreshing);

    void updateRankList(List<RankData> rankDatas);
}
