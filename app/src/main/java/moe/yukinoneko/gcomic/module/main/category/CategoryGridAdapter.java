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

package moe.yukinoneko.gcomic.module.main.category;

import android.content.Context;
import android.content.Intent;
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
import moe.yukinoneko.gcomic.data.CategoryData;
import moe.yukinoneko.gcomic.glide.GlideUrlFactory;
import moe.yukinoneko.gcomic.module.classify.ClassifyActivity;
import moe.yukinoneko.gcomic.utils.Utils;
import moe.yukinoneko.gcomic.widget.RatioImageView;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.ViewHolder> {
    private final String TAG = "CategoryGridAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<CategoryData> mData;

    private ColorGenerator mColorGenerator;

    public CategoryGridAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
        mColorGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_grid_category, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.category = mData.get(position);

        holder.categoryCover.setOriginalSize(222, 222);
        // @formatter:off
        TextDrawable placeholder = TextDrawable.builder().buildRect(Utils.getFirstCharacter(holder.category.title), mColorGenerator.getColor(holder.category.title));
        Glide.with(mContext)
             .load(GlideUrlFactory.newGlideUrlInstance(holder.category.cover))
             .placeholder(placeholder)
             .into(holder.categoryCover);
        holder.categoryTitle.setText(holder.category.title);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void replaceAll(List<CategoryData> elem) {
        mData.clear();
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.category_cover) RatioImageView categoryCover;
        @BindView(R.id.category_title) AppCompatTextView categoryTitle;

        CategoryData category;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ClassifyActivity.class);
            intent.putExtra(ClassifyActivity.CLASSIFY_TAG_ID, category.tagId);
            intent.putExtra(ClassifyActivity.CLASSIFY_TITLE, category.title);
            mContext.startActivity(intent);
        }
    }
}
