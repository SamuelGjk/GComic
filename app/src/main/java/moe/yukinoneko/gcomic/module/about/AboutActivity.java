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
