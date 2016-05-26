package moe.yukinoneko.gcomic.database.model;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by SamuelGjk on 2016/5/9.
 */
@Table("gcomic_favorite")
public class FavoriteModel {

    @PrimaryKey(AssignType.BY_MYSELF)
    public int comicId;

    public String comicTitle;

    public String comicAuthors;

    public String comicCover;

    public FavoriteModel(int comicId, String comicTitle, String comicAuthors, String comicCover) {
        this.comicId = comicId;
        this.comicTitle = comicTitle;
        this.comicAuthors = comicAuthors;
        this.comicCover = comicCover;
    }
}
