package io.gank.gank.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.gank.gank.R;
import io.gank.gank.adapter.GankAdapter;
import io.gank.gank.entity.Results;
import io.gank.gank.net.HttpMethods;
import io.gank.gank.subscribers.ProgressSubscriber;
import io.gank.gank.subscribers.SubscriberOnNextListener;
import io.gank.gank.utils.NetWorkUtil;
import io.gank.gank.utils.SnackBarUtil;
import io.gank.gank.view.BatteryView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GankFragment extends Fragment {

    @Bind(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.battery)
    BatteryView battery;

    private LinearLayoutManager layoutManager;
    private GankAdapter adapter;
    private SubscriberOnNextListener getDataOnNext;
    private int page = 1;
    private String title;

    public GankFragment() {
    }

    public static GankFragment getInstance(String title){
        GankFragment gankFragment = new GankFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        gankFragment.setArguments(bundle);
        return gankFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        title = bundle.getString("title");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_gank, container, false);
        ButterKnife.bind(this, rootView);

        getDataOnNext = new SubscriberOnNextListener<List<Results>>() {
            @Override
            public void onNext(List<Results> results) {
                //更新数据
                adapter.addDatas(results);
            }
        };
        init();

        return rootView;
    }

    private void init() {
        //首次请求数据
        loadData(title, page, true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new GankAdapter(getContext());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                page = 1;
                loadData(title, page, false);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = layoutManager.getChildCount();
                int total = layoutManager.getItemCount();
                int past = layoutManager.findFirstCompletelyVisibleItemPosition();
                if ((visible + past) >= total) {
                    //加载更多
                    loadData(title, ++page, false);
                }
            }
        });
    }

    //进行网络请求
    private void loadData(String type, int page, boolean refresh){
        if(!NetWorkUtil.isNetWorkConnected(getContext())){
            SnackBarUtil.ShortSnackBar(recyclerView,"请连接网络... ( ＞ω＜)", SnackBarUtil.COLOR_PINK, SnackBarUtil.COLOR_GREEN).show();
        }else{
            HttpMethods.getInstance().getGankData(new ProgressSubscriber(getDataOnNext, getActivity(), recyclerView, battery, refresh), type, page);
        }
    }

}
