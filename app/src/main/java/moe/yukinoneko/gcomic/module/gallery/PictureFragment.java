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

package moe.yukinoneko.gcomic.module.gallery;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.base.BaseFragment;
import moe.yukinoneko.gcomic.base.BasePresenter;
import moe.yukinoneko.gcomic.base.IBaseView;
import moe.yukinoneko.gcomic.glide.GlideUrlFactory;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by SamuelGjk on 2016/4/20.
 */
public class PictureFragment extends BaseFragment<PictureFragment.PicturePresenter> implements IBaseView {
    private static final String PIC_URL = "PIC_URL";
    private static final String PIC_BYTE = "PIC_BYTE";
    private static final String IS_FROM_DISK = "IS_FROM_DISK";

    @BindView(R.id.pv_picture) PhotoView pvPicture;
    @BindView(R.id.loading_progress_bar) ProgressBar loadingProgressBar;

    private String mPicUrl;
    private byte[] mPicByte;
    private boolean isFromDisk;

    public static PictureFragment newInstance(String picUrl, byte[] picByte, boolean isFromDisk) {
        PictureFragment fragment = new PictureFragment();
        Bundle args = new Bundle();
        args.putString(PIC_URL, picUrl);
        args.putByteArray(PIC_BYTE, picByte);
        args.putBoolean(IS_FROM_DISK, isFromDisk);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int provideViewLayoutId() {
        return R.layout.fragment_picture;
    }

    @Override
    protected void initPresenter() {
        presenter = new PicturePresenter(getContext(), this);
        presenter.init();
    }

    @Override
    public void init() {
        if (getArguments() != null) {
            mPicUrl = getArguments().getString(PIC_URL);
            mPicByte = getArguments().getByteArray(PIC_BYTE);
            isFromDisk = getArguments().getBoolean(IS_FROM_DISK);
        }
        pvPicture.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                ((GalleryActivity) getActivity()).toggleSystemUi();
            }
        });

        // 别问我为什么这样写= =
        if (isFromDisk) {
            Glide.with(this)
                 .load(mPicByte)
                 .listener(new RequestListener<byte[], GlideDrawable>() {
                     @Override
                     public boolean onException(Exception e, byte[] model, Target<GlideDrawable> target, boolean isFirstResource) {
                         return false;
                     }

                     @Override
                     public boolean onResourceReady(GlideDrawable resource, byte[] model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                         loadingProgressBar.setVisibility(View.INVISIBLE);
                         return false;
                     }
                 })
                 .into(pvPicture);
        } else {
            Glide.with(this)
                 .load(GlideUrlFactory.newGlideUrlInstance(mPicUrl))
                 .listener(new RequestListener<GlideUrl, GlideDrawable>() {
                     @Override
                     public boolean onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {
                         return false;
                     }

                     @Override
                     public boolean onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                         loadingProgressBar.setVisibility(View.INVISIBLE);
                         return false;
                     }
                 })
                 .into(pvPicture);
        }
    }

    class PicturePresenter extends BasePresenter<IBaseView> {
        public PicturePresenter(Context context, IBaseView iView) {
            super(context, iView);
        }
    }
}
