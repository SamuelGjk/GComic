/*
 * Copyright (C) 2016  SamuelGjk <samuel.alva@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

    public long favoriteTime;

    public FavoriteModel(int comicId, String comicTitle, String comicAuthors, String comicCover, long favoriteTime) {
        this.comicId = comicId;
        this.comicTitle = comicTitle;
        this.comicAuthors = comicAuthors;
        this.comicCover = comicCover;
        this.favoriteTime = favoriteTime;
    }
}
