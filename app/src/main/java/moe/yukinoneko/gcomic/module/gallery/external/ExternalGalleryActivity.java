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

import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.List;

import butterknife.BindView;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.ToolBarActivity;
import moe.yukinoneko.gcomic.utils.FileUtlis;
import moe.yukinoneko.gcomic.utils.Utils;
import moe.yukinoneko.gcomic.widget.HackyViewPager;

/**
 * Created by SamuelGjk on 2016/7/5.
 */
public class ExternalGalleryActivity extends ToolBarActivity<ExternalGalleryPresenter> implements IExternalGalleryView {

    @BindView(R.id.gallery_pager) HackyViewPager galleryPager;
    @BindView(R.id.bottom_bar) LinearLayout bottomBar;
    @BindView(R.id.seek_bar) AppCompatSeekBar seekBar;
    @BindView(R.id.cur_page) AppCompatTextView curPage;
    @BindView(R.id.total_pages) AppCompatTextView totalPages;

    private View mDecorView;

    private ExternalGalleryPagerAdapter mPagerAdapter;

    private final int NOT_FULL_SCREEN_FLAG = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
    private final int FULL_SCREEN_FLAG = NOT_FULL_SCREEN_FLAG | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void initPresenter() {
        presenter = new ExternalGalleryPresenter(this, this);
        presenter.init();
    }

    @Override
    public void init() {
        mDecorView = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) bottomBar.getLayoutParams();
            lp.setMargins(0, 0, 0, Utils.getNavigationBarHeight(this));
        }

        galleryPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                seekBar.setProgress(position);
                curPage.setText(String.valueOf(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerAdapter = new ExternalGalleryPagerAdapter(getSupportFragmentManager());
        galleryPager.setAdapter(mPagerAdapter);

        seekBar.setKeyProgressIncrement(1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                galleryPager.setCurrentItem(progress, false);
            }
        });

        curPage.setText("1");

        String path = FileUtlis.getPath(this, getIntent().getData());
        mToolbar.setTitle(path != null ? path.substring(path.lastIndexOf("/") + 1) : "No Title");
        presenter.fetchPicturesFromDisk(path);
    }

    @Override
    public void updatePagerContent(List<byte[]> bytes) {
        totalPages.setText(String.valueOf(bytes.size()));
        seekBar.setMax(bytes.size());
        mPagerAdapter.replaceAll(bytes);
    }

    public void toggleSystemUi() {
        boolean isShown = mToolbar.getAlpha() == 1.0f;
        ViewCompat.animate(mToolbar)
                  .alpha(isShown ? 0.0f : 1.0f)
                  .start();
        ViewCompat.animate(bottomBar)
                  .alpha(isShown ? 0.0f : 1.0f)
                  .setListener(new ViewPropertyAnimatorListenerAdapter() {
                      @Override
                      public void onAnimationStart(View view) {
                          super.onAnimationStart(view);

                          if (view.isShown()) {
                              return;
                          }

                          view.setVisibility(View.VISIBLE);
                      }

                      @Override
                      public void onAnimationEnd(View view) {
                          super.onAnimationEnd(view);

                          if (view.getAlpha() == 1.0f) {
                              return;
                          }

                          view.setVisibility(View.INVISIBLE);
                      }
                  })
                  .start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDecorView.setSystemUiVisibility(isShown ? FULL_SCREEN_FLAG : NOT_FULL_SCREEN_FLAG);
        }
    }
}
