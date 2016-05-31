package moe.yukinoneko.gcomic.module.about;

import android.support.v7.widget.AppCompatTextView;

import butterknife.BindView;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.IBaseView;
import moe.yukinoneko.gcomic.base.ToolBarActivity;
import moe.yukinoneko.gcomic.utils.Utils;

/**
 * Created by SamuelGjk on 2016/5/30.
 */
public class AboutActivity extends ToolBarActivity<AboutPresenter> implements IBaseView {
    @BindView(R.id.text_app_version) AppCompatTextView textAppVersion;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initPresenter() {
        presenter = new AboutPresenter(this, this);
        presenter.init();
    }

    @Override
    public void init() {
        textAppVersion.setText(getString(R.string.app_version, Utils.getVersionName(this)));
    }
}
