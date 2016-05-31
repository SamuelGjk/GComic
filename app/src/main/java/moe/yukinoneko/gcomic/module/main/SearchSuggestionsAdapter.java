package moe.yukinoneko.gcomic.module.main;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moe.yukinoneko.gcomic.R;
import moe.yukinoneko.gcomic.database.model.SearchHistoryModel;

/**
 * Created by SamuelGjk on 2016/5/30.
 */
public class SearchSuggestionsAdapter extends RecyclerView.Adapter<SearchSuggestionsAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<SearchHistoryModel> mData;

    public SearchSuggestionsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_list_search_suggestion, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.history = mData.get(position);
        holder.textSearchSuggestion.setText(holder.history.keyword);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void replaceAll(List<SearchHistoryModel> elem) {
        mData.clear();
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.text_search_suggestion) AppCompatTextView textSearchSuggestion;
        @BindView(R.id.button_remove_suggestion) AppCompatImageButton buttonRemoveSuggestion;

        SearchHistoryModel history;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            buttonRemoveSuggestion.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_remove_suggestion:
                    mData.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    onSuggestionClickListener.onRemoveClick(history);
                    break;

                default:
                    onSuggestionClickListener.onSuggestionClick(history);
                    break;
            }
        }
    }

    private OnSuggestionClickListener onSuggestionClickListener;

    public void setOnRemoveClickListener(OnSuggestionClickListener onSuggestionClickListener) {
        this.onSuggestionClickListener = onSuggestionClickListener;
    }

    interface OnSuggestionClickListener {
        void onSuggestionClick(SearchHistoryModel history);

        void onRemoveClick(SearchHistoryModel history);
    }
}
