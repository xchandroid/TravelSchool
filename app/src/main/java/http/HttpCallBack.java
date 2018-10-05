package http;

/**
 * Created by Administrator on 2018/9/27.
 */

public abstract class HttpCallBack<T> {
   public abstract void onSuccess(T t);
   public  abstract void onFail(Exception e);
}
