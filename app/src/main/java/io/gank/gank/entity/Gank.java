package io.gank.gank.entity;

/**
 * 通用的干货数据
 * Created by baymax on 2016/7/6.
 */
public class Gank<T> {
    private String error;

    private T results;

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
        results = results;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("error = " + error);
        if(results != null){
            sb.append(results.toString());
        }
        return sb.toString();
    }
}
