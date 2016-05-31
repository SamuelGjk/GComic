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
