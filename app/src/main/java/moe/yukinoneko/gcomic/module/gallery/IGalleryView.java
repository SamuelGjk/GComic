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

package moe.yukinoneko.gcomic.module.gallery;

import java.util.List;

import moe.yukinoneko.gcomic.base.IBaseView;

/**
 * Created by SamuelGjk on 2016/4/20.
 */
public interface IGalleryView extends IBaseView {
    void showMessageSnackbar(int resId);

    void updatePagerContent(List<String> urls, List<byte[]> bytes, boolean isFromDisk);

    void loadError();
}
