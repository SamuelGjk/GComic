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

package moe.yukinoneko.gcomic.module.gallery.external;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import moe.yukinoneko.gcomic.module.gallery.PictureFragment;

/**
 * Created by SamuelGjk on 2016/7/5.
 */
public class ExternalGalleryPagerAdapter extends FragmentStatePagerAdapter {
    private List<byte[]> mBytes;

    public ExternalGalleryPagerAdapter(FragmentManager fm) {
        super(fm);

        mBytes = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return PictureFragment.newInstance(null, mBytes.get(position), true);
    }

    @Override
    public int getCount() {
        return mBytes.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    void replaceAll(List<byte[]> bytes) {
        mBytes.clear();
        mBytes.addAll(bytes);
        notifyDataSetChanged();
    }
}
