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
