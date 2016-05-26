package moe.yukinoneko.gcomic.database.model;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by SamuelGjk on 2016/5/12.
 */
@Table("gcomic_download")
public class DownloadTaskModel {

    @PrimaryKey(AssignType.BY_MYSELF)
    public int id;

    public int comicId;
    public String comicTitle;
    public String comicCover;

    public int chapterId;
    public String chapterTitle;

    public String firstLetter;

    public int position;
    public int chaptersSize;

    public String url;
    public String path;

    public DownloadTaskModel(int id, int comicId, String comicTitle, String comicCover, int chapterId, String chapterTitle, String firstLetter, int position, int chaptersSize, String url, String path) {
        this.id = id;
        this.comicId = comicId;
        this.comicTitle = comicTitle;
        this.comicCover = comicCover;
        this.chapterId = chapterId;
        this.chapterTitle = chapterTitle;
        this.firstLetter = firstLetter;
        this.position = position;
        this.chaptersSize = chaptersSize;
        this.url = url;
        this.path = path;
    }
}
