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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import io.gank.gank.ui.GankActivity;
import io.gank.gank.ui.GirlActivity;

/**
 * Created by baymax on 2016/7/7.
 */
public class GankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static int TYPE_GIRL = 1;
    public static int TYPE_NORM = 2;
    private List<Results> mDatas = new ArrayList<>();
    private int lastPosition = -1;
    private Context context;

    public GankAdapter(){
    }

    public GankAdapter(Context context){
        this.context = context;
    }

    public void addDatas(List<Results> list){
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                    .anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_GIRL){
            View view = LayoutInflater.from(context).inflate(R.layout.item_girl_layout, parent, false);
            GirlViewHolder girlHolder = new GirlViewHolder(view);
            return girlHolder;
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            NormalViewHolder normal = new NormalViewHolder(view);
            return normal;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Results results = mDatas.get(position);
        if(holder instanceof NormalViewHolder) {
            final NormalViewHolder normal = (NormalViewHolder) holder;
            normal.tv_desc.setText(results.getDesc());
            normal.tv_type.setText(results.getType());
            normal.tv_who.setText(results.getWho());
            normal.card.setTag(results);
            setAnimation(normal.card, position);
        }else if(holder instanceof GirlViewHolder){
            final GirlViewHolder girl = (GirlViewHolder) holder;
            girl.tv_girl_who.setText(results.getDesc());
            Glide
                    .with(context)
                    .load(results.getUrl())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(girl.iv_girl);
            girl.card_girl.setTag(results);
            setAnimation(girl.card_girl, position);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mDatas.get(position).getType().equals("福利")){
            return TYPE_GIRL;
        }else{
            return TYPE_NORM;
        }
    }
    /**
     * 清除动画效果，不然快速滑动时，会出现item重叠的现象。
     * @param holder
     */
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        @Bind(R.id.tv_type)
        TextView tv_type;
        @Bind(R.id.tv_who)
        TextView tv_who;
        @Bind(R.id.iv_like)
        ImageView iv_like;
        @Bind(R.id.card)
        CardView card;

        public NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card)
        void toGankActivity(){
            Intent intent = new Intent(context, GankActivity.class);
            intent.putExtra("desc", ((Results)card.getTag()).getDesc());
            intent.putExtra("url", ((Results)card.getTag()).getUrl());
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, card, "jump_gank").toBundle());
        }
    }

    public class GirlViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_girl_who)
        TextView tv_girl_who;
        @Bind(R.id.iv_girl)
        ImageView iv_girl;
        @Bind(R.id.card_girl)
        CardView card_girl;

        public GirlViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_girl)
        void toGirlActivity(){
            Intent intent = new Intent(context, GirlActivity.class);
            intent.putExtra("desc", ((Results)card_girl.getTag()).getDesc());
            intent.putExtra("url", ((Results)card_girl.getTag()).getUrl());
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, card_girl, "jump_girl").toBundle());
        }
    }
}