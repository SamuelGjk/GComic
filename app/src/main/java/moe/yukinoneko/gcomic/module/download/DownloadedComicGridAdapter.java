package moe.yukinoneko.gcomic.module.download;

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
import moe.yukinoneko.gcomic.database.model.DownloadTaskModel;
import moe.yukinoneko.gcomic.glide.GlideUrlFactory;
import moe.yukinoneko.gcomic.module.download.tasks.DownloadTasksActivity;
import moe.yukinoneko.gcomic.utils.Utils;
import moe.yukinoneko.gcomic.widget.RatioImageView;

/**
 * Created by SamuelGjk on 2016/5/13.
 */
public class DownloadedComicGridAdapter extends RecyclerView.Adapter<DownloadedComicGridAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<DownloadTaskModel> mData;

    private ColorGenerator mColorGenerator;

    public DownloadedComicGridAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
        mColorGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_grid_downloaded_comic, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.taskModel = mData.get(position);

        holder.comicCover.setOriginalSize(222, 222);
        // @formatter:off
        TextDrawable placeholder = TextDrawable.builder().buildRect(Utils.getFirstCharacter(holder.taskModel.comicTitle), mColorGenerator.getColor(holder.taskModel.comicTitle));
        Glide.with(mContext)
             .load(GlideUrlFactory.newGlideUrlInstance(holder.taskModel.comicCover))
             .placeholder(placeholder)
             .into(holder.comicCover);
        holder.comicTitle.setText(holder.taskModel.comicTitle);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void replaceAll(List<DownloadTaskModel> elem) {
        mData.clear();
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    // @formatter:on
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.downloaded_comic_cover) RatioImageView comicCover;
        @BindView(R.id.downloaded_comic_title) AppCompatTextView comicTitle;

        DownloadTaskModel taskModel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, DownloadTasksActivity.class);
            intent.putExtra(DownloadTasksActivity.DOWMLOADED_COMIC_ID, taskModel.comicId);
            intent.putExtra(DownloadTasksActivity.DOWMLOADED_COMIC_TITLE, taskModel.comicTitle);
            mContext.startActivity(intent);
        }
    }
}
