package io.gank.gank.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.gank.gank.R;
import io.gank.gank.adapter.GankAdapter;
import io.gank.gank.adapter.SearchAdapter;
import io.gank.gank.entity.Results;
import io.gank.gank.entity.SResults;
import io.gank.gank.net.HttpMethods;
import io.gank.gank.subscribers.ProgressSubscriber;
import io.gank.gank.subscribers.SubscriberOnNextListener;
import io.gank.gank.utils.NetWorkUtil;
import io.gank.gank.utils.SnackBarUtil;
import io.gank.gank.view.BatteryView;

public class SearchActivity extends AppCompatActivity {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.edt_key)
    EditText edtKey;
    @Bind(R.id.tv_search_tip)
    TextView tip;
    @Bind(R.id.battery)
    BatteryView battery;

    private LinearLayoutManager layoutManager;
    private SearchAdapter adapter;
    private SubscriberOnNextListener getDataOnNext;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        getDataOnNext = new SubscriberOnNextListener<List<SResults>>() {
            @Override
            public void onNext(List<SResults> results) {
                //更新数据
                adapter.addDatas(results);
            }
        };
        initView();//初始化view
    }

    private void initView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new SearchAdapter(this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = layoutManager.getChildCount();
                int total = layoutManager.getItemCount();
                int past = layoutManager.findFirstCompletelyVisibleItemPosition();
                if ((visible + past) >= total) {
                    //加载更多
                    search(edtKey.getText().toString().trim(), "all", ++page, false);
                }
            }
        });

        edtKey.setEnabled(true);
        edtKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    recyclerView.setVisibility(View.GONE);
                    //rlTip.setVisibility(View.VISIBLE);
                } else {
                    //Log.i("square", "search key: " + s.toString());
                    tip.setVisibility(View.GONE);
                    search(s.toString(), "all", page, true);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    //进行网络请求
    private void search(String content, String type, int page, boolean refresh) {
        if(!NetWorkUtil.isNetWorkConnected(this)){
            SnackBarUtil.ShortSnackBar(recyclerView,"请连接网络... ( ＞ω＜)", SnackBarUtil.COLOR_PINK, SnackBarUtil.COLOR_GREEN).show();
        }else {
            HttpMethods.getInstance().getSearchData(new ProgressSubscriber(getDataOnNext, this, recyclerView, battery, refresh), content, type, page);
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
}
