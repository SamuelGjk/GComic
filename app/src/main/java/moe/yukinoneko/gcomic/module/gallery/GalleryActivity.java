package moe.yukinoneko.gcomic.module.gallery;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.util.List;

import butterknife.BindView;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.ToolBarActivity;
import moe.yukinoneko.gcomic.data.ComicData;
import moe.yukinoneko.gcomic.download.DownloadTasksManager;
import moe.yukinoneko.gcomic.network.GComicApi;
import moe.yukinoneko.gcomic.utils.SnackbarUtils;
import moe.yukinoneko.gcomic.utils.Utils;

/**
 * Created by SamuelGjk on 2016/4/20.
 */
public class GalleryActivity extends ToolBarActivity<GalleryPresenter> implements IGalleryView {
    private final String TAG = "GalleryActivity";

    public static final String GALLERY_CMOIC_ID = "GALLERY_CMOIC_ID";
    public static final String GALLERY_CMOIC_FIRST_LETTER = "GALLERY_CMOIC_FIRST_LETTER";
    public static final String GALLERY_CHAPTER_LIST = "GALLERY_CHAPTER_LIST";
    public static final String GALLERY_CHAPTER_POSITION = "GALLERY_CHAPTER_POSITION";

    @BindView(R.id.gallery_pager) ViewPager galleryPager;
    @BindView(R.id.bottom_bar) LinearLayout bottomBar;
    @BindView(R.id.seek_bar) AppCompatSeekBar seekBar;
    @BindView(R.id.cur_page) AppCompatTextView curPage;
    @BindView(R.id.total_pages) AppCompatTextView totalPages;

    private View mDecorView;

    private GalleryPagerAdapter mPagerAdapter;

    private int mComicId;
    private String firstLetter;
    private List<ComicData.ChaptersBean.ChapterBean> mChapters;
    private int mChapterPosition;

    private int intCurPage;

    private Handler mHandler;
    private Runnable mPreviousRunnable, mNextRunnable;

    private final int NOT_FULL_SCREEN_FLAG = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
    private final int FULL_SCREEN_FLAG = NOT_FULL_SCREEN_FLAG | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void initPresenter() {
        presenter = new GalleryPresenter(this, this);
        presenter.init();
    }

    @Override
    public void init() {
        mDecorView = getWindow().getDecorView();

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) bottomBar.getLayoutParams();
        lp.setMargins(0, 0, 0, Utils.getNavigationBarHeight(this));

        mHandler = new Handler();
        mPreviousRunnable = new Runnable() {
            @Override
            public void run() {
                if (mChapterPosition < mChapters.size() - 1) {
                    mChapterPosition += 1;
                    mToolbar.setTitle(mChapters.get(mChapterPosition).chapterTitle);
                    fetchChapterContent(mChapters.get(mChapterPosition).chapterId);
                } else {
                    galleryPager.setCurrentItem(1);
                    showMessageSnackbar(getString(R.string.already_first));
                }
            }
        };
        mNextRunnable = new Runnable() {
            @Override
            public void run() {
                if (mChapterPosition > 0) {
                    mChapterPosition -= 1;
                    mToolbar.setTitle(mChapters.get(mChapterPosition).chapterTitle);
                    fetchChapterContent(mChapters.get(mChapterPosition).chapterId);
                } else {
                    galleryPager.setCurrentItem(mPagerAdapter.getCount() - 2);
                    showMessageSnackbar(getString(R.string.already_last));
                }
            }
        };

        galleryPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                seekBar.setProgress(position - 1);

                intCurPage = position;
                if (intCurPage > mPagerAdapter.getCount() - 2) {
                    intCurPage = mPagerAdapter.getCount() - 2;
                } else if (intCurPage < 1) {
                    intCurPage = 1;
                }
                curPage.setText(String.valueOf(intCurPage));

                if (position == 0) {
                    mHandler.postDelayed(mPreviousRunnable, 1000);
                } else if (position == mPagerAdapter.getCount() - 1) {
                    mHandler.postDelayed(mNextRunnable, 1000);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerAdapter = new GalleryPagerAdapter(getSupportFragmentManager());
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
                galleryPager.setCurrentItem(progress + 1, false);
            }
        });

        Intent intent = getIntent();

        mComicId = intent.getIntExtra(GALLERY_CMOIC_ID, -1);
        firstLetter = intent.getStringExtra(GALLERY_CMOIC_FIRST_LETTER);
        mChapters = intent.getParcelableArrayListExtra(GALLERY_CHAPTER_LIST);
        mChapterPosition = intent.getIntExtra(GALLERY_CHAPTER_POSITION, -1);

        mToolbar.setTitle(mChapters.get(mChapterPosition).chapterTitle);

        galleryPager.setCurrentItem(1, false);

        fetchChapterContent(mChapters.get(mChapterPosition).chapterId);
    }

    @Override
    public void showMessageSnackbar(String message) {
        SnackbarUtils.showShort(galleryPager, message);
    }

    @Override
    public void updatePagerContent(List<String> urls, List<byte[]> bytes, boolean isFromDisk) {
        int count = isFromDisk ? bytes.size() : urls.size();
        totalPages.setText(String.valueOf(count));
        seekBar.setMax(count - 1);
        galleryPager.setCurrentItem(1, false);
        mPagerAdapter.replaceAll(urls, bytes, isFromDisk);
//        galleryPager.setCurrentItem(1, false);
    }

    @Override
    public void fetchFailure() {
        galleryPager.setCurrentItem(intCurPage <= 1 ? 1 : intCurPage);
    }

    private void fetchChapterContent(int chapterId) {
        // @formatter:off
        String url = GComicApi.getInstance().generateDownloadUrl(firstLetter, mComicId, chapterId);
        String path = DownloadTasksManager.getInstance(this).generatePath(url);
        int id = FileDownloadUtils.generateId(url, path);

        if (DownloadTasksManager.getInstance(this).isExist(path) && DownloadTasksManager.getInstance(this).isDownloadedById(id)) {
            presenter.fetchPicturesFromDisk(path);
        } else {
            presenter.fetchChapterDetails(mComicId, chapterId);
        }
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

                          view.setVisibility(View.GONE);
                      }
                  })
                  .start();
        mDecorView.setSystemUiVisibility(isShown ? FULL_SCREEN_FLAG : NOT_FULL_SCREEN_FLAG);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mPreviousRunnable);
        mHandler.removeCallbacks(mNextRunnable);
        super.onDestroy();
    }
}
