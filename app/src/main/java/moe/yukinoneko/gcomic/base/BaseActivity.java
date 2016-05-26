package moe.yukinoneko.gcomic.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    protected T presenter;

    protected Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentViewId());
        unbinder = ButterKnife.bind(this);
        initPresenter();
    }

    protected abstract int provideContentViewId();

    protected abstract void initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.release();
        unbinder.unbind();
    }
}
