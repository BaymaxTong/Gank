package io.gank.gank.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.gank.gank.R;
import io.gank.gank.entity.Results;
import io.gank.gank.entity.SResults;
import io.gank.gank.ui.GankActivity;
import io.gank.gank.ui.GirlActivity;

/**
 * Created by baymax on 2016/7/18.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{
    private List<SResults> mDatas = new ArrayList<>();
    private Context context;
    private int lastPosition = -1;

    public SearchAdapter(){
    }

    public SearchAdapter(Context context){
        this.context = context;
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                    .anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void addDatas(List<SResults> list){
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder girlHolder = new MyViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_search, parent, false));
        return girlHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SResults results = mDatas.get(position);

        holder.tv_search.setText(results.getDesc());
        holder.who.setText(results.getWho());
        holder.type.setText(results.getType());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(results.getType().equals("福利")){
                    Intent intent = new Intent(context, GirlActivity.class);
                    intent.putExtra("desc", results.getDesc());
                    intent.putExtra("url", results.getUrl());
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, GankActivity.class);
                    intent.putExtra("desc", results.getDesc());
                    intent.putExtra("url", results.getUrl());
                    context.startActivity(intent);
                }
            }
        });
        setAnimation(holder.cardView, position);
    }
    /**
     * 清除动画效果，不然快速滑动时，会出现item重叠的现象。
     * @param holder
     */
    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_search)
        TextView tv_search;
        @Bind(R.id.tv_search_who)
        TextView who;
        @Bind(R.id.tv_search_type)
        TextView type;
        @Bind(R.id.card_search)
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
