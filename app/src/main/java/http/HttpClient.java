package http;

import android.app.Activity;
import android.icu.text.LocaleDisplayNames;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import bean.School;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/9/27.
 */

public class HttpClient {
    private  static  final String SCHOOLPATH = "http://119.29.166.254:9090/api/university/getUniversityByProvinceId?id=20";
    private static  final   String SchoolImg = "https://gkcx.eol.cn/schoolhtm/1034/schoolImageList/list_1.htm";
    private static ExecutorService executorService;
    static {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())   //添加拦截器伪造头为电脑网页访问
                .build();
        OkHttpUtils.initClient(okHttpClient);    //初始化OkHttpClinet
    }
     static {
        executorService = Executors.newCachedThreadPool();//创建一个可以根据实际情况调整线程池中的线程数量的线程池
     }

       public static void getSchoolByID(String id, final HttpCallBack<School> callBack)
       {
             OkHttpUtils.get().url(SCHOOLPATH)
                     .build()
                     .execute(new JsonCallBack<School>(School.class) {
                 @Override
                 public void onError(Call call, Exception e, int id) {
                     callBack.onFail(e);
                 }

                 @Override
                 public void onResponse(School response, int id) {
                     callBack.onSuccess(response);
                 }
             });
       }


       //获取学校图片
       public static void getSchoolImg(final String ID, final Activity activity, final imgCallBckListener listener)
       {
           final List<String> imgList = new ArrayList<>();
                       executorService.execute(new Runnable() {
                           @Override
                           public void run() {
                               Document document = null;
                               try {
                                   document = Jsoup.connect("https://gkcx.eol.cn/schoolhtm/"+ID+"/schoolImageList/list_1.htm").get();
                                   Elements element = document.select("div.images"); //获取包含大图的div
                                   int size = element.select("img").size(); //获取img标签的数量
                                   for (int i=0;i<size;i++)  //for循环将图片地址添加到List集合
                                   {
                                       imgList.add(element.select("img").get(i).attr("src"));
                                   }
                                   //切换到主线程显示图片
                                   activity.runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           listener.onSuccess(imgList);
                                       }
                                   });
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }

                       });
       }

       public static void getjianjie(final String id, final Activity activity, final jianjieCallbackListener listener)
       {
           final StringBuilder stringBuilder = new StringBuilder();
           executorService.execute(new Runnable() {
               @Override
               public void run() {
                   try {

                       Document document = Jsoup.connect("https://gkcx.eol.cn/schoolhtm/schoolInfo/"+id+"/10056/detail.htm").get();
                       Elements elements = document.select("div.content.news").select("p");  //获取所有<p>标签，简介都在标签里面
                       for (int i=0;i<elements.size();i++)
                       {
                           stringBuilder.append(elements.get(i).text()+"\n");
                       }
                       activity.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               listener.onSuccess(stringBuilder.toString());  //将获取到的简介内容返回
                           }
                       });
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           });
       }
}
