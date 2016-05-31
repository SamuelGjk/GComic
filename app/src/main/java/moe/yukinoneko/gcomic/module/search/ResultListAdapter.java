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

package moe.yukinoneko.gcomic.module.search;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
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
import moe.yukinoneko.gcomic.data.SearchData;
import moe.yukinoneko.gcomic.glide.GlideUrlFactory;
import moe.yukinoneko.gcomic.module.details.ComicDetailsActivity;
import moe.yukinoneko.gcomic.utils.Utils;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ViewHolder> {
    private final String TAG = "ResultListAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<SearchData> mData;

    private ColorGenerator mColorGenerator;

    public ResultListAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
        mColorGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_list_search_result, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.result = mData.get(position);

        // @formatter:off
        TextDrawable placeholder = TextDrawable.builder().buildRect(Utils.getFirstCharacter(holder.result.title), mColorGenerator.getColor(holder.result.title));
        Glide.with(mContext)
             .load(GlideUrlFactory.newGlideUrlInstance(holder.result.cover))
             .placeholder(placeholder)
             .into(holder.comicCover);
        holder.comicTitle.setText(holder.result.title);
        holder.comicTitle.setText(holder.result.title);
        holder.comicAuthors.setText(mContext.getString(R.string.comic_author_placeholder, holder.result.authors));
        holder.comicTypes.setText(mContext.getString(R.string.comic_type_placeholder, holder.result.types));
        holder.lastUpdateTime.setText(mContext.getString(R.string.comic_last_name_placeholder, holder.result.lastName));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void addAll(List<SearchData> elem) {
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    void replaceAll(List<SearchData> elem) {
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

        SearchData result;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ComicDetailsActivity.launchActivity(mContext, comicCover, result.id, result.title, result.authors, result.cover);
//            Intent intent = new Intent(mContext, ComicDetailsActivity.class);
//
//            intent.putExtra(ComicDetailsActivity.COMIC_DETAILS_ID, result.id);
//            intent.putExtra(ComicDetailsActivity.COMIC_DETAILS_TITLE, result.title);
//            intent.putExtra(ComicDetailsActivity.COMIC_DETAILS_COVER, result.cover);
//
//            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, comicCover, ComicDetailsActivity.SHARED_ELEMENT_COVER);
//            ActivityCompat.startActivity((Activity) mContext, intent, optionsCompat.toBundle());
        }
    }
}
