package adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import www.vaiyee.com.travelschool.R;

/**
 * Created by Administrator on 2018/10/5.
 */

public class MyPagerAdapter extends PagerAdapter {
    private List<View> mViewList;
    private List<String> mTitleList;
    private Context context;
    public MyPagerAdapter(List<View> mViewList,List<String> mTitleList,Context context) {
        this.mViewList = mViewList;
        this.mTitleList = mTitleList;
         this.context = context;
    }

    @Override
    public int getCount() {
        return mViewList.size();//页卡数
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;//官方推荐写法
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));//删除页卡
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);//页卡标题
    }
}
