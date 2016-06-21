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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.mmin18.widget.FlexLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.ToolBarActivity;
import moe.yukinoneko.gcomic.data.ComicData;
import moe.yukinoneko.gcomic.database.model.ReadHistoryModel;
import moe.yukinoneko.gcomic.glide.GlideUrlFactory;
import moe.yukinoneko.gcomic.module.details.bottomsheet.DownloadBottomSheetDialogView;
import moe.yukinoneko.gcomic.module.gallery.GalleryActivity;
import moe.yukinoneko.gcomic.utils.SnackbarUtils;

/**
 * Created by SamuelGjk on 2016/4/19.
 */
public class ComicDetailsActivity extends ToolBarActivity<ComicDetailsPresenter> implements IComicDetailsView, ChapterGridAdapter.OnChapterClickListener, DownloadBottomSheetDialogView.OnBottomSheetDismissListener {

    private final int REQUEST_CODE_COMIC_DETAILS = 10001;

    public static final String COMIC_DETAILS_ID = "COMIC_DETAILS_ID";
    public static final String COMIC_DETAILS_TITLE = "COMIC_DETAILS_TITLE";
    public static final String COMIC_DETAILS_AURTHORS = "COMIC_DETAILS_AURTHORS";
    public static final String COMIC_DETAILS_COVER = "COMIC_DETAILS_COVER";
    public static final String SHARED_ELEMENT_COVER = "SHARED_ELEMENT_COVER";

    @BindView(R.id.comic_details_description) AppCompatTextView comicDetailsDescription;
    @BindView(R.id.author_tag_group) TagGroup authorTagGroup;
    @BindView(R.id.type_tag_group) TagGroup typeTagGroup;
    @BindView(R.id.comic_details_cover) AppCompatImageView comicDetailsCover;
    @BindView(R.id.comic_details_collapsing_toolbar) CollapsingToolbarLayout comicDetailsCollapsingToolbar;
    @BindView(R.id.chapter_list) RecyclerView chapterList;
    @BindView(R.id.comic_details_coordinatorLayout) CoordinatorLayout comicDetailsCoordinatorLayout;
    @BindView(R.id.comic_details_content) FlexLayout comicDetailsContent;
    @BindView(R.id.loading_progress_bar) MaterialProgressBar loadingProgressBar;
    @BindView(R.id.load_error_layout) LinearLayout loadErrorLayout;

    private Menu menu;

    private boolean isFavorite;

    private AlertDialog mDescriptionDialog;

    private DownloadBottomSheetDialogView mBottomSheetDialogView;

    private ChapterGridAdapter mAdapter;

    private int comicId;
    private String comicTitle;
    private String comicAuthors;
    private String comicCover;
    private String firstLetter;

    @OnClick({ R.id.comic_details_description, R.id.comic_details_fab, R.id.load_error_layout })
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.comic_details_description:
                mDescriptionDialog.show();
                break;
            case R.id.comic_details_fab:
                if (mAdapter.getItemCount() > 0) {
                    toGallery(mAdapter.getHistoryChapterPosition(), mAdapter.getHistoryBrowsePosition());
                }
                break;
            case R.id.load_error_layout:
                loadErrorLayout.setVisibility(View.INVISIBLE);
                loadingProgressBar.setVisibility(View.VISIBLE);
                presenter.fetchComicDetails(comicId);
                break;

            default:
                break;
        }
    }

    public static void launchActivity(Context context, View comicCoverView, int comicId, String comicTitle, String comicAuthors, String comicCover) {
        launchActivityForResult(context, comicCoverView, comicId, comicTitle, comicAuthors, comicCover, -1);
    }

    public static void launchActivityForResult(Context context, View comicCoverView, int comicId, String comicTitle, String comicAuthors, String comicCover, int requestCode) {
        Intent intent = new Intent(context, ComicDetailsActivity.class);

        intent.putExtra(ComicDetailsActivity.COMIC_DETAILS_ID, comicId);
        intent.putExtra(ComicDetailsActivity.COMIC_DETAILS_TITLE, comicTitle);
        intent.putExtra(ComicDetailsActivity.COMIC_DETAILS_AURTHORS, comicAuthors);
        intent.putExtra(ComicDetailsActivity.COMIC_DETAILS_COVER, comicCover);

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, comicCoverView, ComicDetailsActivity.SHARED_ELEMENT_COVER);
        ActivityCompat.startActivityForResult((Activity) context, intent, requestCode, optionsCompat.toBundle());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_comic_details;
    }

    @Override
    protected void initPresenter() {
        presenter = new ComicDetailsPresenter(this, this);
        presenter.init();
    }

    @Override
    public void init() {
        Intent intent = getIntent();

        comicDetailsCollapsingToolbar.setTitle(intent.getStringExtra(COMIC_DETAILS_TITLE));

        comicId = intent.getIntExtra(COMIC_DETAILS_ID, -1);
        comicTitle = intent.getStringExtra(COMIC_DETAILS_TITLE);
        comicAuthors = intent.getStringExtra(COMIC_DETAILS_AURTHORS);
        comicCover = intent.getStringExtra(COMIC_DETAILS_COVER);

        ViewCompat.setTransitionName(comicDetailsCover, SHARED_ELEMENT_COVER);
        Glide.with(this)
             .load(GlideUrlFactory.newGlideUrlInstance(comicCover))
             .dontAnimate()
             .into(comicDetailsCover);

        chapterList.setFocusable(false);
        chapterList.setNestedScrollingEnabled(false);
        chapterList.setHasFixedSize(true);
        mAdapter = new ChapterGridAdapter(this);
        mAdapter.setOnChapterClickListener(this);
        chapterList.setAdapter(mAdapter);

        mDescriptionDialog = new AlertDialog.Builder(this).create();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (presenter != null) {
                    presenter.fetchComicDetails(comicId);
                }
            }
        }, 358);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_COMIC_DETAILS) {
            presenter.fetchReadHistory(comicId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.details, menu);
        presenter.isFavorite(comicId);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorite:
                if (isFavorite) {
                    presenter.unFavoriteComic(comicId);
                } else {
                    presenter.favoriteComic(comicId, comicTitle, comicAuthors, comicCover);
                }
                break;
            case R.id.menu_download:
                if (mBottomSheetDialogView != null) {
                    mBottomSheetDialogView.show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMessageSnackbar(String message) {
        SnackbarUtils.showShort(comicDetailsCoordinatorLayout, message);
    }

    @Override
    public void updateComicDetailsContent(ComicData comicData) {
        firstLetter = comicData.firstLetter;

        mBottomSheetDialogView = new DownloadBottomSheetDialogView(this, comicId, comicTitle, comicCover, firstLetter, mAdapter.getData());
        mBottomSheetDialogView.setOnBottomSheetDismissListener(this);

        comicDetailsDescription.setText(comicData.description);
        mDescriptionDialog.setMessage(comicData.description);

        List<String> authors = new ArrayList<>();
        for (ComicData.AuthorsBean author : comicData.authors) {
            authors.add(author.tagName);
        }
        authorTagGroup.setTags(authors);

        List<String> types = new ArrayList<>();
        for (ComicData.TypesBean type : comicData.types) {
            types.add(type.tagName);
        }
        typeTagGroup.setTags(types);

        mAdapter.setDownloadedChapters(comicData.downloadedChapters);
        mAdapter.setReadHistory(comicData.readHistory);

        final List<ComicData.ChaptersBean.ChapterBean> chapters = comicData.chapters.get(0).data;

        ViewCompat.animate(loadingProgressBar)
                  .alpha(0.0f)
                  .setListener(new ViewPropertyAnimatorListenerAdapter() {
                      @Override
                      public void onAnimationEnd(View view) {
                          super.onAnimationEnd(view);

                          loadingProgressBar.setVisibility(View.INVISIBLE);

                          comicDetailsContent.setVisibility(View.VISIBLE);
                          mAdapter.replaceAll(chapters);
                      }
                  })
                  .start();
    }

    @Override
    public void updateFavoriteMenu(boolean isFavorite) {
        // @formatter:off
        this.isFavorite = isFavorite;
        menu.findItem(R.id.menu_favorite).setIcon(isFavorite ? R.mipmap.ic_favorite_white : R.mipmap.ic_favorite_border_white);
    }

    // @formatter:on
    @Override
    public void updateReadHistory(ReadHistoryModel history) {
        mAdapter.setReadHistory(history);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadError() {
        loadingProgressBar.setVisibility(View.INVISIBLE);
        loadErrorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChapterClick(int position) {
        toGallery(position, position == mAdapter.getHistoryChapterPosition() ? mAdapter.getHistoryBrowsePosition() : 1);
    }

    @Override
    protected void onDestroy() {
        if (mBottomSheetDialogView != null) {
            mBottomSheetDialogView.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onBottomSheetDismiss() {
        mAdapter.notifyDataSetChanged();
    }

    private void toGallery(int chapterPosition, int browsePosition) {
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra(GalleryActivity.GALLERY_CMOIC_ID, comicId);
        intent.putExtra(GalleryActivity.GALLERY_CMOIC_FIRST_LETTER, firstLetter);
        intent.putParcelableArrayListExtra(GalleryActivity.GALLERY_CHAPTER_LIST, mAdapter.getAllChapters());
        intent.putExtra(GalleryActivity.GALLERY_CHAPTER_POSITION, chapterPosition);
        intent.putExtra(GalleryActivity.GALLERY_BROWSE_POSITION, browsePosition);
        startActivityForResult(intent, REQUEST_CODE_COMIC_DETAILS);
    }
}
