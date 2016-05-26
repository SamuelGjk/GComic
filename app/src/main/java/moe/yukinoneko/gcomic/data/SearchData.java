package moe.yukinoneko.gcomic.data;

import com.squareup.moshi.Json;

/**
 * Created by SamuelGjk on 2016/4/6.
 */
public class SearchData {
    public int id;
    public String status;
    public String title;
    @Json(name = "last_name") public String lastName;
    public String cover;
    public String authors;
    public String types;
    @Json(name = "hot_hits") public int hotHits;
}
