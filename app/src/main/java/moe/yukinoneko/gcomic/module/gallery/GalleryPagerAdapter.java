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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SamuelGjk on 2016/4/20.
 */
public class GalleryPagerAdapter extends FragmentStatePagerAdapter {
    private List<String> mUrls;
    private List<byte[]> mBytes;

    private int count = 1;
    private boolean isFromDisk;

    public GalleryPagerAdapter(FragmentManager fm) {
        super(fm);

        mUrls = new ArrayList<>();
        mBytes = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0 || position == count + 1) {
            return new LoadingFragment();
        }
        if (mUrls.isEmpty() && mBytes.isEmpty()) {
            return PictureFragment.newInstance("nodata", null, isFromDisk);
        }
        return PictureFragment.newInstance(isFromDisk ? null : mUrls.get(position - 1), isFromDisk ? mBytes.get(position - 1) : null, isFromDisk);
    }

    @Override
    public int getCount() {
        return count + 2;
    }

    @Override
    public float getPageWidth(int position) {
        if (position == 0 || position == count + 1) {
            return 0.2f;
        }
        return super.getPageWidth(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    void replaceAll(List<String> urls, List<byte[]> bytes, boolean isFromDisk) {
        this.isFromDisk = isFromDisk;

        if (isFromDisk) {
            mBytes.clear();
            mBytes.addAll(bytes);
            count = bytes.size();
        } else {
            mUrls.clear();
            mUrls.addAll(urls);
            count = urls.size();
        }

        notifyDataSetChanged();
    }
}
