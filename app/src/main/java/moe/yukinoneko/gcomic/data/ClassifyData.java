package moe.yukinoneko.gcomic.data;

import com.squareup.moshi.Json;

/**
 * Created by SamuelGjk on 2016/4/18.
 */
public class ClassifyData {
    public int id;
    public String title;
    public String authors;
    public String status;
    public String cover;
    public String types;
    @Json(name = "last_updatetime") public Long lastUpdatetime;
    public int num;
}
