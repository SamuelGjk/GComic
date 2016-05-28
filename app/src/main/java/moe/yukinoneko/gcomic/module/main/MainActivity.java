package moe.yukinoneko.gcomic.module.main;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.ActionMenuView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import com.mypopsy.widget.FloatingSearchView;

import butterknife.BindView;
import butterknife.OnClick;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.BaseActivity;
import moe.yukinoneko.gcomic.base.IBaseView;
import moe.yukinoneko.gcomic.download.DownloadTasksManager;
import moe.yukinoneko.gcomic.module.download.DownloadedComicActivity;
import moe.yukinoneko.gcomic.module.favorite.FavoriteActivity;
import moe.yukinoneko.gcomic.module.search.SearchActivity;

public class MainActivity extends BaseActivity<MainPresenter> implements IBaseView, ActionMenuView.OnMenuItemClickListener {

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.main_app_bar_layout) AppBarLayout mAppBarLayout;
    @BindView(R.id.main_fab) FloatingActionButton mFab;
    @BindView(R.id.main_coordinator_layout) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.main_floating_search_view) FloatingSearchView mSearchView;
    @BindView(R.id.main_pager) ViewPager mPager;
    @BindView(R.id.main_tab_layout) TabLayout mTabLayout;
    @BindView(R.id.drawer_navigation) NavigationView mNavigationView;

    private MainPagerAdapter mAdapter;

    public interface ChildRefresher {
        void doChildRefresh();
    }

    @OnClick(R.id.main_fab)
    void onClick(View view) {
        Fragment f = mAdapter.getItemAt(mPager.getCurrentItem());
        if (f != null && f instanceof ChildRefresher) {
            ((ChildRefresher) f).doChildRefresh();
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
        presenter = new MainPresenter(this, this);
        presenter.init();
    }

    @Override
    public void init() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.navigation_favorite:
                        Intent favoriteIntent = new Intent(MainActivity.this, FavoriteActivity.class);
                        startActivity(favoriteIntent);
                        break;
                    case R.id.navigation_download:
                        Intent downloadIntent = new Intent(MainActivity.this, DownloadedComicActivity.class);
                        startActivity(downloadIntent);
                        break;
                }
                return false;
            }
        });

        mSearchView.setIcon(new DrawerArrowDrawable(this));
        mSearchView.setOnIconClickListener(new FloatingSearchView.OnIconClickListener() {
            @Override
            public void onNavigationClick() {
                if (mSearchView.isActivated()) {
                    mSearchView.setActivated(false);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        mSearchView.setOnMenuItemClickListener(this);
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showClearButton(s.length() > 0 && mSearchView.isActivated());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchView.setOnSearchFocusChangedListener(new FloatingSearchView.OnSearchFocusChangedListener() {
            @Override
            public void onFocusChanged(boolean focused) {
                boolean textEmpty = TextUtils.isEmpty(mSearchView.getText());

                showClearButton(focused && !textEmpty);
            }
        });
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSearchAction(CharSequence charSequence) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra(SearchActivity.SEARCH_KEYWORD, charSequence.toString());
                startActivity(intent);
            }
        });

        mAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mSearchView.setTranslationY(verticalOffset);
            }
        });

        DownloadTasksManager.getInstance(this).bindService();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mSearchView.setText(null);
                break;
            case R.id.menu_settings:
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isActivated()) {
            mSearchView.setActivated(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        DownloadTasksManager.getInstance(this).onDestroy();
        super.onDestroy();
    }

    private void showClearButton(boolean show) {
        // @formatter:off
        mSearchView.getMenu().findItem(R.id.menu_clear).setVisible(show);
    }
}
