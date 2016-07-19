package io.gank.gank.entity;

/**
 * 搜索类数据
 * Created by baymax on 2016/7/18.
 */
public class Search<T> {
    private  int count;

    private String error;

    private T results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("count = " + count);
        sb.append(", error = " + error);
        if(results != null){
            sb.append(results.toString());
        }
        return sb.toString();
    }
}
