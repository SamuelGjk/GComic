package moe.yukinoneko.gcomic.glide;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import moe.yukinoneko.gcomic.network.GComicApi;

/**
 * Created by SamuelGjk on 2016/4/18.
 */
public class GlideUrlFactory {
    public static GlideUrl newGlideUrlInstance(String url) {
        // @formatter:off
        return new GlideUrl(url, new LazyHeaders.Builder().addHeader("Referer", GComicApi.REFERER).build());
    }
}
