package moe.yukinoneko.gcomic.module.classify;

import java.util.List;

import moe.yukinoneko.gcomic.data.ClassifyData;
import moe.yukinoneko.gcomic.base.IBaseView;

/**
 * Created by SamuelGjk on 2016/4/18.
 */
public interface IClassifyView extends IBaseView {
    void showMessageSnackbar(String message);

    void setRefreshing(boolean refreshing);

    void updateClassifyList(List<ClassifyData> classifyDatas);
}
