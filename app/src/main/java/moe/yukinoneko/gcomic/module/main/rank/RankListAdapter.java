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

package moe.yukinoneko.gcomic.module.main.rank;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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
import moe.yukinoneko.gcomic.data.RankData;
import moe.yukinoneko.gcomic.glide.GlideUrlFactory;
import moe.yukinoneko.gcomic.module.details.ComicDetailsActivity;
import moe.yukinoneko.gcomic.utils.Utils;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public class RankListAdapter extends RecyclerView.Adapter<RankListAdapter.ViewHolder> {
    private final String TAG = "RankListAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<RankData> mData;

    private ColorGenerator mColorGenerator;

    public RankListAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
        mColorGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_list_rank, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.rank = mData.get(position);

        // @formatter:off
        TextDrawable placeholder = TextDrawable.builder().buildRect(Utils.getFirstCharacter(holder.rank.title), mColorGenerator.getColor(holder.rank.title));
        Glide.with(mContext)
             .load(GlideUrlFactory.newGlideUrlInstance(holder.rank.cover))
             .placeholder(placeholder)
             .into(holder.comicCover);
        holder.comicTitle.setText(holder.rank.title);
        holder.comicAuthors.setText(mContext.getString(R.string.comic_author_placeholder, holder.rank.authors));
        holder.comicTypes.setText(mContext.getString(R.string.comic_type_placeholder, holder.rank.types));
        holder.lastUpdateTime.setText(mContext.getString(R.string.comic_last_update_time_placeholder, DateFormat.format("yyyy-MM-dd", holder.rank.lastUpdatetime * 1000)));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void addAll(List<RankData> elem) {
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    void replaceAll(List<RankData> elem) {
        mData.clear();
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.comic_cover) AppCompatImageView comicCover;
        @BindView(R.id.comic_title) AppCompatTextView comicTitle;
        @BindView(R.id.comic_authors) AppCompatTextView comicAuthors;
        @BindView(R.id.comic_types) AppCompatTextView comicTypes;
        @BindView(R.id.last_update_time) AppCompatTextView lastUpdateTime;

        RankData rank;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ComicDetailsActivity.launchActivity(mContext, comicCover, rank.comicId, rank.title, rank.authors, rank.cover);
//            Intent intent = new Intent(mContext, ComicDetailsActivity.class);
//
//            intent.putExtra(ComicDetailsActivity.COMIC_DETAILS_ID, rank.comicId);
//            intent.putExtra(ComicDetailsActivity.COMIC_DETAILS_TITLE, rank.title);
//            intent.putExtra(ComicDetailsActivity.COMIC_DETAILS_COVER, rank.cover);
//
//            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, comicCover, ComicDetailsActivity.SHARED_ELEMENT_COVER);
//            ActivityCompat.startActivity((Activity) mContext, intent, optionsCompat.toBundle());
        }
    }
}
