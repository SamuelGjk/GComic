package moe.yukinoneko.gcomic.module.gallery;

import java.util.List;

import moe.yukinoneko.gcomic.base.IBaseView;

/**
 * Created by SamuelGjk on 2016/4/20.
 */
public interface IGalleryView extends IBaseView {
    void showMessageSnackbar(String message);

    void updatePagerContent(List<String> urls, List<byte[]> bytes, boolean isFromDisk);

    void fetchFailure();
}
