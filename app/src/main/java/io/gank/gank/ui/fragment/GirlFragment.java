package io.gank.gank.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.gank.gank.R;
import io.gank.gank.adapter.GirlAdapter;
import io.gank.gank.entity.Results;
import io.gank.gank.net.HttpMethods;
import io.gank.gank.subscribers.ProgressSubscriber;
import io.gank.gank.subscribers.SubscriberOnNextListener;
import io.gank.gank.utils.NetWorkUtil;
import io.gank.gank.utils.SnackBarUtil;
import io.gank.gank.view.BatteryView;
import io.gank.gank.view.HouseLoadingView;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class GirlFragment extends Fragment {
    @Bind(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.battery)
    BatteryView battery;

    private StaggeredGridLayoutManager layoutManager;
    private GirlAdapter adapter;
    private SubscriberOnNextListener getDataOnNext;
    private int page = 1;
    private String title;
    private int lastVisibleItem ;
    private Realm realm;

    public GirlFragment() {
        // Required empty public constructor
    }

    public static GirlFragment getInstance(String title){
        GirlFragment girlFragment = new GirlFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        girlFragment.setArguments(bundle);
        return girlFragment;
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gank, container, false);
        ButterKnife.bind(this, rootView);

        getDataOnNext = new SubscriberOnNextListener<List<Results>>() {
            @Override
            public void onNext(List<Results> results) {
                //更新数据
                adapter.addDatas(results);
                //缓存数据
                realm = Realm.getDefaultInstance();
                for(int i = 0;i < results.size();i++){
                    RealmResults<Results> res = realm.where(Results.class).equalTo("desc",results.get(i).getDesc())
                            .findAll();
                    if (res == null||res.size() == 0) {
                        realm.beginTransaction();
                        Results realmRes = realm.copyToRealm(results.get(i));
                        realm.commitTransaction();
                    }
                }
                realm.close();
            }
        };

        init();

        return rootView;
    }

    private void init() {
        initView();
        initNet();
    }

    private void initView() {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new GirlAdapter(getContext());
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
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem +2>=layoutManager.getItemCount()) {
                    loadData(title, ++page, false);//加载更多
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] positions= layoutManager.findLastVisibleItemPositions(null);
                lastVisibleItem = Math.max(positions[0],positions[1]);
            }
        });
    }

    private void initNet() {
        //首次请求数据
        loadData(title, page, false);
    }

    //进行网络请求
    private void loadData(String type, int page, boolean refresh){
        if(!NetWorkUtil.isNetWorkConnected(getContext())){
            getTagResult("福利");
            SnackBarUtil.ShortSnackBar(recyclerView,"网络飞到外太空啦... ( ＞ω＜)", SnackBarUtil.COLOR_PINK, SnackBarUtil.COLOR_GREEN).show();
        }else {
            HttpMethods.getInstance().getGankData(new ProgressSubscriber(getDataOnNext, getActivity(), recyclerView, battery, refresh), type, page);
        }
    }

    public void getTagResult(String tag){
        realm = Realm.getDefaultInstance();
        List<Results> data = new ArrayList<>();
        RealmResults<Results> result2 = realm.where(Results.class).equalTo("type",tag)
                .findAll();
        Log.d("debug",result2.toString());
        for(int i = 0;i<result2.size();i++){
            Results item = result2.get(i);
            Results r = new Results();              //必须把查询到的数据转换一下不然报错。
            r.set_id(item.get_id());
            r.setCreatedAt(item.getCreatedAt());
            r.setDesc(item.getDesc());
            r.setPublishedAt(item.getPublishedAt());
            r.setSource(item.getSource());
            r.setType(item.getType());
            r.setUrl(item.getUrl());
            r.setWho(item.getWho());
            r.setUsed(item.getUsed());
            data.add(r);
        }
        adapter.addDatas(data);
        realm.close();
    }

}
