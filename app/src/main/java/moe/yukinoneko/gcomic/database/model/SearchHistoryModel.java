package moe.yukinoneko.gcomic.database.model;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by SamuelGjk on 2016/5/30.
 */
@Table("gcomic_search_history")
public class SearchHistoryModel {
    @PrimaryKey(AssignType.BY_MYSELF)
    public int _id;

    public String keyword;

    public SearchHistoryModel(int _id, String keyword) {
        this._id = _id;
        this.keyword = keyword;
    }
}
