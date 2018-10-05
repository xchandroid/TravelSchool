package www.vaiyee.com.travelschool.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import adapter.MyPagerAdapter;
import adapter.School_imgAdapter;
import http.HttpClient;
import http.imgCallBckListener;
import http.jianjieCallbackListener;
import www.vaiyee.com.travelschool.R;

public class DetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView img;
    private RecyclerView recyclerView,ablumList,mvlist;
    private LayoutInflater inflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    private List<String> imgList = new ArrayList<>();
    private View view1,view2,view3;
    private SwipeRefreshLayout refreshLayout;
    private TextView content;
    private School_imgAdapter adapter;
    private boolean isJianjie = false,isImg = false,isZhoubian = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21)
        {
            View decorview = getWindow().getDecorView();
            decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        viewPager = (ViewPager) findViewById(R.id.vp_view);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        img = (ImageView) findViewById(R.id.photo);
        Glide.with(this).load("https://gkcx.eol.cn/upload/schoollogo/"+id+".jpg").into(img);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refreshLayout.setEnabled(false);       //禁止SwipeRefreshLayout下拉
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout collapsingToolbarLayout = (net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout) findViewById(R.id.collasping_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true); //显示返回按钮
            actionBar.setHomeButtonEnabled(true);  //设置点击返回
        }
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00F5FF"));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbarLayout.setTitle(getIntent().getStringExtra("name"));   //设置标题学校名字
        view1 = LayoutInflater.from(this).inflate(R.layout.jianjie,viewPager,false);
        content = view1.findViewById(R.id.content);
        view2 = LayoutInflater.from(this).inflate(R.layout.xuanyuanfengguang,viewPager,false);
        recyclerView = view2.findViewById(R.id.img_recyclerview);
        view3 = LayoutInflater.from(this).inflate(R.layout.zhoubian,viewPager,false);
        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);

        mTitleList.add("简介");
        mTitleList.add("校园风光");
        mTitleList.add("周边");

        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));

        viewPager.setAdapter(new MyPagerAdapter(mViewList,mTitleList));
        tabLayout.setupWithViewPager(viewPager); //将TabLayout和ViewPager关联起来。
        //tabLayout.setTabsFromPagerAdapter(new MyPagerAdapter(mViewList)); //给Tabs设置适配器
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        HttpClient.getjianjie(id, this, new jianjieCallbackListener() {
            @Override
            public void onSuccess(String jianjie) {
                content.setText(jianjie);
                isJianjie = true;
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0:

                        break;
                    case 1:
                        if (isImg)
                        {
                            return;
                        }
                        HttpClient.getSchoolImg(id, DetailsActivity.this, new imgCallBckListener() {
                            @Override
                            public void onSuccess(List list) {
                                imgList.addAll(list);
                                adapter = new School_imgAdapter(DetailsActivity.this,imgList);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailsActivity.this);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(adapter);
                                isImg = true;
                            }
                        });
                        break;
                    case 2:

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
            return super.onOptionsItemSelected(item);
    }
}
