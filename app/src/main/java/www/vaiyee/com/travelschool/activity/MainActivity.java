package www.vaiyee.com.travelschool.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import adapter.school_list_adapter;
import bean.School;
import bean.School_List;
import blurUtil.ScreenUtils;
import blurUtil.UtilBitmap;
import blurUtil.UtilBlurBitmap;
import blurUtil.UtilScreenCapture;
import http.HttpCallBack;
import http.HttpClient;
import http.imgCallBckListener;
import http.jianjieCallbackListener;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import www.vaiyee.com.travelschool.R;

public class MainActivity extends AppCompatActivity{
   private RecyclerView school_list;
   private LinearLayout linearLayout;
   private Gson gson;
   private static ImageView  orientation;
   private static TextView area;
   //https://gkcx.eol.cn   后面加上图片url
   private static List<School_List.SchoolBean> schoolList = new ArrayList<>();
   private  static school_list_adapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        school_list = findViewById(R.id.school_list);// recyclerview
        gson = new Gson();
        schoolList.addAll(gson.fromJson(getJson("bj.json"),School_List.class).getSchool());
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        school_list.setLayoutManager(manager);
        adapter = new school_list_adapter(MainActivity.this,schoolList);
        school_list.setAdapter(adapter);
        linearLayout = findViewById(R.id.area_title);
        orientation = findViewById(R.id.orientation);
        area = findViewById(R.id.area);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orientation.setImageResource(R.drawable.up);
                showAreaMenu(linearLayout);
            }
        });
        /*
        HttpClient.getSchoolByID("20", new HttpCallBack<School>() {
            @Override
            public void onSuccess(School school) {
                schoolList = school.schoolList;
                LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                school_list.setLayoutManager(manager);
                school_list.setAdapter(new school_list_adapter(MainActivity.this,schoolList));
            }

            @Override
            public void onFail(Exception e) {
             e.printStackTrace();
            }
        });
        linearLayout = findViewById(R.id.area_title);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"点击了",Toast.LENGTH_LONG).show();
            }
        });
        */

    }


    //获取存储在assets文件夹的省份Json数据
    private String getJson(String fileName)
    {
        String result ="";
        AssetManager assetManager = MainActivity.this.getAssets();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line ="";
            while ((line=bufferedReader.readLine())!=null)
            {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private  static PopupWindow popupWindow;
    private  void  showAreaMenu(View view)
    {
        View contentview = LayoutInflater.from(this).inflate(R.layout.select_area, (ViewGroup) getWindow().getDecorView(), false);
        if (popupWindow==null) {
            popupWindow = new PopupWindow(contentview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        Bitmap bitmap = ScreenUtils.snapShotWithoutStatusBar(this); //获取当前屏幕截图
        ImageView bg = contentview.findViewById(R.id.blur_bg);
        bg.setImageBitmap(bitmap);
        UtilBitmap.blurImageView(this,bg,15);   // 开始模糊背景
        popupWindow.setOutsideTouchable(false);   //设置外面不可点击
        popupWindow.setFocusable(true);  //设置焦点为true，设置了这两个之后点击弹出的外面就会隐藏
        popupWindow.setAnimationStyle(R.style.popuAnim);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                orientation.setImageResource(R.drawable.down);
            }
        });
        popupWindow.showAsDropDown(view);
    }
   public void selectArea(View view)
   {
        switch (view.getId())
        {
            case R.id.dq1:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("hebei.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("河北");
                popupWindow.dismiss();
                break;
            case R.id.dq2:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("sx.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("山西");
                popupWindow.dismiss();
                break;
            case R.id.dq3:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("ln.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("辽宁");
                popupWindow.dismiss();
                break;
            case R.id.dq4:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("jl.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("吉林");
                popupWindow.dismiss();
                break;
            case R.id.dq5:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("hlj.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("黑龙江");
                popupWindow.dismiss();
                break;
            case R.id.dq6:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("js.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("江苏");
                popupWindow.dismiss();
                break;
            case R.id.dq7:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("zj.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("浙江");
                popupWindow.dismiss();
                break;
            case R.id.dq8:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("ah.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("安徽");
                popupWindow.dismiss();
                break;
            case R.id.dq9:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("fj.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("福建");
                popupWindow.dismiss();
                break;
            case R.id.dq10:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("jx.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("江西");
                popupWindow.dismiss();
                break;
            case R.id.dq11:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("sd.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("山东");
                popupWindow.dismiss();
                break;
            case R.id.dq12:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("henan.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("河南");
                popupWindow.dismiss();
                break;
            case R.id.dq13:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("hubei.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("湖北");
                popupWindow.dismiss();
                break;
            case R.id.dq14:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("hunan.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("湖南");
                popupWindow.dismiss();
                break;
            case R.id.dq15:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("gd.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("广东");
                popupWindow.dismiss();
                break;
            case R.id.dq16:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("gx.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("广西");
                popupWindow.dismiss();
                break;
            case R.id.dq17:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("hainan.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("海南");
                popupWindow.dismiss();
                break;
            case R.id.dq18:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("sc.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("四川");
                popupWindow.dismiss();
                break;
            case R.id.dq19:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("gz.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("贵州");
                popupWindow.dismiss();
                break;
            case R.id.dq20:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("yn.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("云南");
                popupWindow.dismiss();
                break;
            case R.id.dq21:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("ssx.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("陕西");
                popupWindow.dismiss();
                break;
            case R.id.dq22:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("gs.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("甘肃");
                popupWindow.dismiss();
                break;
            case R.id.dq23:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("qh.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("青海");
                popupWindow.dismiss();
                break;
            case R.id.dq24:
                Toast.makeText(this,"抱歉。。。暂时还没有收录台湾高校信息",Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
                break;
            case R.id.dq25:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("xj.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("新疆");
                popupWindow.dismiss();
                break;
            case R.id.dq26:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("nx.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("宁夏");
                popupWindow.dismiss();
                break;
            case R.id.dq27:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("xz.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("西藏");
                popupWindow.dismiss();
                break;
            case R.id.dq28:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("nmg.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("内蒙古");
                popupWindow.dismiss();
                break;
            case R.id.dq29:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("bj.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("北京");
                popupWindow.dismiss();
                break;
            case R.id.dq30:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("tj.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("天津");
                popupWindow.dismiss();
                break;
            case R.id.dq31:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("sh.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("上海");
                popupWindow.dismiss();
               break;
            case R.id.dq32:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("cq.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("重庆");
                popupWindow.dismiss();
                break;
            case R.id.dq33:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("xg.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("香港");
                popupWindow.dismiss();
                break;
            case R.id.dq34:
                schoolList.clear();
                schoolList.addAll(gson.fromJson(getJson("am.json"),School_List.class).getSchool());    //这里一定要用addAll()方法，不然改变数据是无效的
                adapter.notifyDataSetChanged();
                area.setText("澳门");
                popupWindow.dismiss();
                break;
        }
   }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
