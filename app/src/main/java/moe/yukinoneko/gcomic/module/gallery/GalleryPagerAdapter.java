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
