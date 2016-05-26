package moe.yukinoneko.gcomic.data;

import com.squareup.moshi.Json;

/**
 * Created by SamuelGjk on 2016/4/18.
 */
public class CategoryData {
    @Json(name = "tag_id") public int tagId;
    public String title;
    public String cover;
}
