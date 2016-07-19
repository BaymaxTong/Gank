package io.gank.gank.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.gank.gank.R;
import io.gank.gank.entity.Results;
import io.gank.gank.ui.GirlActivity;

/**
 * Created by baymax on 2016/7/12.
 */
public class GirlAdapter extends RecyclerView.Adapter<GirlAdapter.GirlViewHolder>{
    private List<Results> mDatas = new ArrayList<>();
    private Context context;

    public GirlAdapter(){
    }

    public GirlAdapter(Context context){
        this.context = context;
    }

    public void addDatas(List<Results> list){
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public GirlViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GirlViewHolder girlHolder = new GirlViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_girl, parent, false));
        return girlHolder;

    }

    @Override
    public void onBindViewHolder(final GirlViewHolder holder, int position) {
        Results results = mDatas.get(position);

        holder.who.setText(results.getDesc());
        Glide.with(context)
                .load(results.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.girlView);
        holder.cardView.setTag(results);
    }
    /**
     * 清除动画效果，不然快速滑动时，会出现item重叠的现象。
     * @param holder
     */
    @Override
    public void onViewDetachedFromWindow(GirlViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onViewRecycled(GirlViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class GirlViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_girl_item_who)
        TextView who;
        @Bind(R.id.iv_girl_item)
        ImageView girlView;
        @Bind(R.id.card_girl_item)
        CardView cardView;

        public GirlViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_girl_item)
        void toGirlActivity(){
            Intent intent = new Intent(context, GirlActivity.class);
            intent.putExtra("desc", ((Results)cardView.getTag()).getDesc());
            intent.putExtra("url", ((Results)cardView.getTag()).getUrl());
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, cardView, "jump_girl").toBundle());
        }
    }

}
