package moe.yukinoneko.gcomic.database.model;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by SamuelGjk on 2016/5/31.
 */
@Table("gcomic_read_history")
public class ReadHistoryModel {

    @PrimaryKey(AssignType.BY_MYSELF)
    public int comicId;

    public int chapterId;

    public ReadHistoryModel(int comicId, int chapterId) {
        this.comicId = comicId;
        this.chapterId = chapterId;
    }
}
