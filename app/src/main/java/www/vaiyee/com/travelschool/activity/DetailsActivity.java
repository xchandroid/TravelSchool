package www.vaiyee.com.travelschool.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import OverLayUtil.DrivingRouteOverlay;
import OverLayUtil.PoiOverlay;
import adapter.MyPagerAdapter;
import adapter.School_imgAdapter;
import http.AddressToLatitudeLongitude;
import http.HttpClient;
import http.imgCallBckListener;
import http.jianjieCallbackListener;
import www.vaiyee.com.travelschool.R;

public class DetailsActivity extends AppCompatActivity implements OnGetPoiSearchResultListener,View.OnClickListener,BaiduMap.OnMarkerClickListener,OnGetRoutePlanResultListener{

    private TabLayout tabLayout;
    private NoScrollViewPager viewPager;
    private ImageView img;
    private RecyclerView recyclerView,ablumList,mvlist;
    private LayoutInflater inflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    private List<String> imgList = new ArrayList<>();
    private static View view1,view2,view3;
    private SwipeRefreshLayout refreshLayout;
    private TextView content;
    private School_imgAdapter adapter;
    private boolean isJianjie = false,isImg = false,isZhoubian = false;
    private static TextureMapView mapView; //百度地图视图
    private static BaiduMap baiduMap;
    private AppBarLayout appBarLayout;
    private String name; //学校名字
    private EditText input;
    private Button search;
    private static double longitude=0, latitude=0;  //学校的经纬度
    private static List<PoiInfo> poiAddrInfos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21)
        {
            View decorview = getWindow().getDecorView();
            decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        SDKInitializer.initialize(getApplicationContext()); //初始化百度地图
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        final String id = intent.getStringExtra("id");
        viewPager = (NoScrollViewPager) findViewById(R.id.vp_view);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        img = (ImageView) findViewById(R.id.photo);
        Glide.with(this).load("https://gkcx.eol.cn/upload/schoollogo/"+id+".jpg").into(img);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refreshLayout.setEnabled(false);       //禁止SwipeRefreshLayout下拉
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collasping_toolbar);
        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setBackgroundResource(R.drawable.main_bg);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
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
        input = view3.findViewById(R.id.input);
        search = view3.findViewById(R.id.search_near);
        search.setOnClickListener(this);
        mapView = view3.findViewById(R.id.baidumap);
        mapView.removeViewAt(1); //删除百度地图logo
        baiduMap = mapView.getMap(); //获取地图视图的控制器
        baiduMap.setOnMarkerClickListener(this); //给地图设置点击标点时的监听
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
        viewPager.setAdapter(new MyPagerAdapter(mViewList,mTitleList,this));
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
                        if (isZhoubian)
                        {
                            return;
                        }
                        requestPermission();
                        appBarLayout.setExpanded(false);
                        if (name.equals("钦州学院"))
                        {
                            name = "北部湾大学";
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final AddressToLatitudeLongitude addressToLatitudeLongitude = new AddressToLatitudeLongitude(name);
                                addressToLatitudeLongitude.getLatAndLngByAddress();//访问网络获取经纬度
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run() {
                                        getLocationByLL(addressToLatitudeLongitude.getLatitude(),addressToLatitudeLongitude.getLongitude());//获取到经纬度后要切到主线程显示地图，不然会卡死
                                    }
                                });
                            }
                        }).start();
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


    /*
     *根据经纬度前往地点
     */
    public void getLocationByLL(double la, double lg)
    {
        //地理坐标的数据结构
        LatLng latLng = new LatLng(la, lg);
        //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.setMapStatus(msu); //移动到学校位置
        baiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(17)); //放大地图
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.xuexiaopoin); //加载坐标图片
        OverlayOptions overlayOptions = new MarkerOptions().position(latLng).icon(descriptor); //构建学校坐标标点
        baiduMap.addOverlay(overlayOptions);  //将学校坐标标记到地图上
        latitude = la;
        longitude = lg;//保存返回的经纬度为全局变量，在后面的周边搜索需要用到这个经纬度
    }



    private void requestPermission() {
        List<String>permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add( Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty())
        {
            String []pers = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this,pers,1);
        }

    }
    /**
     * 搜索周边地理位置
     */
    private void searchNeayBy(double mCurrentLantitude,double mCurrentLongitude,String key) {
        PoiNearbySearchOption option = new PoiNearbySearchOption();  //附近搜索参数类
        option.keyword(key);
        option.sortType(PoiSortType.distance_from_near_to_far);
        option.location(new LatLng(mCurrentLantitude, mCurrentLongitude));
        option.radius(5000);
        option.pageCapacity(40);
        PoiSearch poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);  //设置搜索结果回调监听为本Activity
        poiSearch.searchNearby(option);  //执行搜索操作

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case  1:
                if (grantResults.length>0) {
                    for (int r : grantResults) {
                        if (r != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "您必须同意所有权限才能正常使用本程序", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    Toast.makeText(this,"授权通过",Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {

        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
            Toast.makeText(DetailsActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }

        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
            poiAddrInfos.clear();
            poiAddrInfos.addAll(poiResult.getAllPoi());//将搜索结果点坐标信息加入集合
            baiduMap.clear(); //清除当前地图上的标点(百度底层已经写好了)
            PoiOverlay poiOverlay = new PoiOverlay(baiduMap); //地图上标点工具类
            poiOverlay.setData(poiResult);// 设置返回的POI数据
            //baiduMap.setOnMarkerClickListener(poiOverlay);
            poiOverlay.addToMap();// 将所有的overlay添加到地图上
            poiOverlay.zoomToSpan();
            int totalPage = poiResult.getTotalPageNum();// 获取总分页数
            Toast.makeText(
                    DetailsActivity.this,
                    "总共查到" + poiResult.getTotalPoiNum() + "个兴趣点, 分为"
                           + totalPage + "页", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            //周边搜索按钮
            case R.id.search_near:
                String info = input.getText().toString();
                if (!TextUtils.isEmpty(info))
                {
                    searchNeayBy(latitude,longitude,info);
                }
                 else
                {
                    Toast.makeText(this,"请输入搜索内容",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng latLng = marker.getPosition();
        for (int i=0;i<poiAddrInfos.size();i++)
        {
            if (latLng.equals(poiAddrInfos.get(i).location))  //判断点击的哪一个标点坐标
            {
                Toast.makeText(this,poiAddrInfos.get(i).name,Toast.LENGTH_LONG).show();
                DrivingRoutePlanOption option = new DrivingRoutePlanOption();
                PlanNode start = PlanNode.withLocation(new LatLng(latitude,longitude)); //学校的坐标为起点
                PlanNode end = PlanNode.withLocation(latLng); // 点击地图上的点为终点坐标
                option.from(start).to(end);
                RoutePlanSearch routePlanSearch = RoutePlanSearch.newInstance();
                routePlanSearch.setOnGetRoutePlanResultListener(this);   //注册路线搜索结果回调监听
                routePlanSearch.drivingSearch(option); //  开始搜索驾车路线
            }
        }
        return false;
    }





    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
           //步行路线
    }


    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
      //火车路线
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
             //自驾路线
        DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
        if (drivingRouteResult.getRouteLines().size()>0)
        {
            overlay.setData(drivingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            Toast.makeText(this,"距离:"+drivingRouteResult.getRouteLines().get(0).getDistance()/1000+"千米",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
           //自行车
    }
}
