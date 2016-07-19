package io.gank.gank.net;

import java.util.List;

import io.gank.gank.entity.Gank;
import io.gank.gank.entity.Results;
import io.gank.gank.entity.SResults;
import io.gank.gank.entity.Search;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 请求的接口服务
 * Created baymax on 16/7/5.
 */
public interface GankService {

    //请求不同类型干货（通用）      API：http://gank.io/api/data/all/20/2
    @GET("data/{type}/"+20+"/{page}")
    Observable<Gank<List<Results>>> getGankData(@Path("type") String type, @Path("page") int page);

    //请求搜索                     API:http://gank.io/api/search/query/listview/category/Android/count/10/page/1
    @GET("search/query/{content}/category/{type}/count/"+20+"/page/{page}")
    Observable<Search<List<SResults>>> getSearchData(@Path("content") String content, @Path("type") String type, @Path("page") int page);
}
