package adapter;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import javax.microedition.khronos.opengles.GL;

import bean.School;
import bean.School_List;
import http.HttpClient;
import http.imgCallBckListener;
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;
import www.vaiyee.com.travelschool.R;
import www.vaiyee.com.travelschool.activity.BaseAnimation;
import www.vaiyee.com.travelschool.activity.DetailsActivity;
import www.vaiyee.com.travelschool.activity.ScaleAnimation;


/**
 * Created by Administrator on 2018/9/27.
 */

public class school_list_adapter extends RecyclerView.Adapter<school_list_adapter.ViewHolder> {
    private  Context context;
    private List<School_List.SchoolBean> school_list;
    public school_list_adapter(Context context, List<School_List.SchoolBean>school_list)
    {
        this.context = context;
        this.school_list = school_list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.school_item,parent,false); //这里一定要设置parent,和false,不然自布局不能正常显示
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final School_List.SchoolBean school = school_list.get(position);
        holder.name.setText(school.getSchoolname());
        if (school.getImgurl()==null)// 判断是否已获取到对应学校的图片
        {
            HttpClient.getSchoolImg(school.getSchoolid(), (Activity) context, new imgCallBckListener() {
                @Override
                public void onSuccess(List imgList) {
                    if (imgList.size() <= 0) {
                        holder.img.setImageResource(R.drawable.wu);
                        school.setImgurl("kong");
                    } else {
                        String url = "https://gkcx.eol.cn" + imgList.get(0);
                        //Log.d("图片地址", url);
                        Glide.with(context).load(url).centerCrop().into(holder.img);
                        school.setImgurl(url);  //将获取的学校图片赋值到相应学校对象
                    }
                }
            });
        }
        else   //如果已经获取过学校了就直接加载，不用再去爬网址
        {
            Glide.with(context).load(school.getImgurl())
                    .centerCrop()
                    .placeholder(R.drawable.wu)
                    .error(R.drawable.wu)
                    .into(holder.img);
        }
        ScaleAnimation animation = new ScaleAnimation();
        for (Animator anim : animation.getAnimators(holder.itemView)) {
            //anim.setInterpolator(new OvershootInterpolator());
            anim.setDuration(500).start();
        }
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DetailsActivity.class);
                intent.putExtra("id",school.getSchoolid());
                intent.putExtra("name",school.getSchoolname());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return school_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView name,distance;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.school_img);
            name = itemView.findViewById(R.id.school_name);
            //distance = itemView.findViewById(R.id.distance);
        }
    }
}
