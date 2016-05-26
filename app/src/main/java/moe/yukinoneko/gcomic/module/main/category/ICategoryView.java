package moe.yukinoneko.gcomic.module.main.category;

import java.util.List;

import moe.yukinoneko.gcomic.data.CategoryData;
import moe.yukinoneko.gcomic.base.IBaseView;

/**
 * Created by SamuelGjk on 2016/4/18.
 */
public interface ICategoryView extends IBaseView {
    void showMessageSnackbar(String message);

    void setRefreshing(boolean refreshing);

    void updateCategoryList(List<CategoryData> categoryDatas);
}
