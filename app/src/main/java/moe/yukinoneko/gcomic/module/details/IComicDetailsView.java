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

package moe.yukinoneko.gcomic.module.details;

import moe.yukinoneko.gcomic.data.ComicData;
import moe.yukinoneko.gcomic.base.IBaseView;
import moe.yukinoneko.gcomic.database.model.ReadHistoryModel;

/**
 * Created by SamuelGjk on 2016/4/19.
 */
public interface IComicDetailsView extends IBaseView {
    void showMessageSnackbar(String message);

    void updateComicDetailsContent(ComicData comicData);

    void updateFavoriteMenu(boolean isFavorite);

    void updateReadHistory(ReadHistoryModel history);

    void loadError();
}
