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

package moe.yukinoneko.gcomic.module.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import moe.yukinoneko.gcomic.module.main.category.CategoryFragment;
import moe.yukinoneko.gcomic.module.main.rank.RankFragment;

/**
 * Created by SamuelGjk on 2016/4/6.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private final String titles[] = { "今日排行榜", "漫画分类" };

    private SparseArray<Fragment> mPages;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        mPages = new SparseArray<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f;
        switch (position) {
            case 0:
                f = new RankFragment();
                break;
            case 1:
                f = new CategoryFragment();
                break;

            default:
                f = null;
                break;
        }
        mPages.put(position, f);
        return f;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (0 <= mPages.indexOfKey(position)) {
            mPages.remove(position);
        }
        super.destroyItem(container, position, object);
    }

    public Fragment getItemAt(int position) {
        return mPages.get(position);
    }
}
