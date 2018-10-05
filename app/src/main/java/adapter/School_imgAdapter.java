package adapter;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import www.vaiyee.com.travelschool.R;
import www.vaiyee.com.travelschool.activity.ScaleAnimation;

/**
 * Created by Administrator on 2018/10/5.
 */

public class School_imgAdapter extends RecyclerView.Adapter<School_imgAdapter.ViewHolder> {

    private List<String> imglist;
    private Context context;
    public School_imgAdapter(Context context,List<String>imglist)
    {
        this.context = context;
        this.imglist =imglist;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.school_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String url = "https://gkcx.eol.cn"+imglist.get(position);
        //Log.d("图片地址",url);
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.wu)
                .error(R.drawable.wu)
                .centerCrop()
                .into(holder.img);
        ScaleAnimation scaleAnimation = new ScaleAnimation();
        for (Animator animator : scaleAnimation.getAnimators(holder.itemView))
        {
            animator.setDuration(500).start();
        }
    }

    @Override
    public int getItemCount() {
        return imglist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.school_img);
        }
    }
}
