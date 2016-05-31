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

package moe.yukinoneko.gcomic.module.classify;

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
import moe.yukinoneko.gcomic.data.ClassifyData;
import moe.yukinoneko.gcomic.glide.GlideUrlFactory;
import moe.yukinoneko.gcomic.module.details.ComicDetailsActivity;
import moe.yukinoneko.gcomic.utils.Utils;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public class ClassifyListAdapter extends RecyclerView.Adapter<ClassifyListAdapter.ViewHolder> {
    private final String TAG = "ClassifyListAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ClassifyData> mData;

    private ColorGenerator mColorGenerator;

    public ClassifyListAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
        mColorGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_list_classify, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.classify = mData.get(position);

        // @formatter:off
        TextDrawable placeholder = TextDrawable.builder().buildRect(Utils.getFirstCharacter(holder.classify.title), mColorGenerator.getColor(holder.classify.title));
        Glide.with(mContext)
             .load(GlideUrlFactory.newGlideUrlInstance(holder.classify.cover))
             .placeholder(placeholder)
             .into(holder.comicCover);
        holder.comicTitle.setText(holder.classify.title);
        holder.comicTitle.setText(holder.classify.title);
        holder.comicAuthors.setText(mContext.getString(R.string.comic_author_placeholder, holder.classify.authors));
        holder.comicTypes.setText(mContext.getString(R.string.comic_type_placeholder, holder.classify.types));
        holder.lastUpdateTime.setText(mContext.getString(R.string.comic_last_update_time_placeholder, DateFormat.format("yyyy-MM-dd", holder.classify.lastUpdatetime * 1000)));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void addAll(List<ClassifyData> elem) {
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    void replaceAll(List<ClassifyData> elem) {
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

        ClassifyData classify;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // @formatter:on
            ComicDetailsActivity.launchActivity(mContext, comicCover, classify.id, classify.title, classify.authors, classify.cover);
//            Intent intent = new Intent(mContext, ComicDetailsActivity.class);
//
//            intent.putExtra(ComicDetailsActivity.COMIC_DETAILS_ID, classify.id);
//            intent.putExtra(ComicDetailsActivity.COMIC_DETAILS_TITLE, classify.title);
//            intent.putExtra(ComicDetailsActivity.COMIC_DETAILS_COVER, classify.cover);
//
//            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, comicCover, ComicDetailsActivity.SHARED_ELEMENT_COVER);
//            ActivityCompat.startActivity((Activity) mContext, intent, optionsCompat.toBundle());
        }
    }
}
