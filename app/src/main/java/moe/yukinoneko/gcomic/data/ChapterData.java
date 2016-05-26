package moe.yukinoneko.gcomic.data;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by SamuelGjk on 2016/4/6.
 */
public class ChapterData {
    @Json(name = "chapter_id") public int chapterId;
    @Json(name = "comic_id") public int comicId;
    public String title;
    @Json(name = "chapter_order") public int chapterOrder;
    public int direction;
    public int picnum;
    @Json(name = "comment_count") public int commentCount;
    @Json(name = "page_url") public List<String> pageUrl;
}
