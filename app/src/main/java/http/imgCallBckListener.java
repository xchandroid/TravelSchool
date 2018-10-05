package http;

import java.util.List;

/**
 * Created by Administrator on 2018/10/3.
 */

public interface imgCallBckListener<T> {
     void onSuccess(List<String> imgList);
}
