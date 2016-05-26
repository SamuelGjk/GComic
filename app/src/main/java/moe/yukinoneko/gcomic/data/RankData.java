package moe.yukinoneko.gcomic.data;

import com.squareup.moshi.Json;

/**
 * Created by SamuelGjk on 2016/4/6.
 */
public class RankData {
    @Json(name = "comic_id") public int comicId;
    public String title;
    public String authors;
    public String status;
    public String cover;
    public String types;
    @Json(name = "last_updatetime") public Long lastUpdatetime;
}
