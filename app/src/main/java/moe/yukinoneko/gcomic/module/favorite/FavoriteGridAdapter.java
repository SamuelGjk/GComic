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

package moe.yukinoneko.gcomic.module.favorite;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.database.model.FavoriteModel;
import moe.yukinoneko.gcomic.glide.GlideUrlFactory;
import moe.yukinoneko.gcomic.module.details.ComicDetailsActivity;
import moe.yukinoneko.gcomic.utils.Utils;
import moe.yukinoneko.gcomic.widget.RatioImageView;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public class FavoriteGridAdapter extends RecyclerView.Adapter<FavoriteGridAdapter.ViewHolder> {
    private final String TAG = "FavoriteGridAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<FavoriteModel> mData;

    private ColorGenerator mColorGenerator;

    public FavoriteGridAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
        mColorGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_grid_favorite, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.favorite = mData.get(position);

        holder.comicCover.setOriginalSize(222, 222);
        // @formatter:off
        TextDrawable placeholder = TextDrawable.builder().buildRect(Utils.getFirstCharacter(holder.favorite.comicTitle), mColorGenerator.getColor(holder.favorite.comicTitle));
        Glide.with(mContext)
             .load(GlideUrlFactory.newGlideUrlInstance(holder.favorite.comicCover))
             .placeholder(placeholder)
             .into(holder.comicCover);
        holder.comicTitle.setText(holder.favorite.comicTitle);
        holder.comicAuthors.setText(holder.favorite.comicAuthors);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void replaceAll(List<FavoriteModel> elem) {
        mData.clear();
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.favorite_comic_cover) RatioImageView comicCover;
        @BindView(R.id.favorite_comic_title) AppCompatTextView comicTitle;
        @BindView(R.id.favorite_comic_authors) AppCompatTextView comicAuthors;

        FavoriteModel favorite;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ComicDetailsActivity.launchActivity(mContext, comicCover, favorite.comicId, favorite.comicTitle, favorite.comicAuthors, favorite.comicCover);
        }
    }
}
