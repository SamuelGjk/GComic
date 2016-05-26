package moe.yukinoneko.gcomic.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment {

    protected T presenter;

    protected Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(provideViewLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initPresenter();
        return view;
    }

    protected abstract int provideViewLayoutId();

    protected abstract void initPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.release();
        unbinder.unbind();
    }
}
