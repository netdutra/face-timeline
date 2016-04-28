package br.com.livetouch.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import br.com.livetouch.bean.PostBean;
import br.com.livetouch.utils.Utils;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostsViewHolder> {

    private final String TAG = "PostAdapter";

    private final List<PostBean> post;

    private final Context context;

    private PostOnClickListener postOnClickListener;

    public PostAdapter(Context context, List<PostBean> post, PostOnClickListener postOnClickListener) {
        this.context = context;
        this.post = post;
        this.postOnClickListener = postOnClickListener;
    }

    @Override
    public int getItemCount() {
        return this.post != null ? this.post.size() : 0;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_post, viewGroup, false);
        PostsViewHolder holder = new PostsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, final int position) {

        final PostBean bean = post.get(position);
        try {
            Date date = bean.createdDate();
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
            String strDate = DateUtils.formatDateTime(context,date.getTime(),DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR);
            holder.txtData.setText(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.txtData.setText("");
        }
        Utils.loadPictureSmall(holder.img);
        holder.txtTitle.setText(bean.getStory());
        holder.txtDescricao.setText(bean.getMessage());

    }

    public interface PostOnClickListener {
        public void onClickPost(View view, int idx);
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {

        public TextView txtData;
        public TextView txtTitle;
        public TextView txtDescricao;

        public ImageView img;

        public PostsViewHolder(final View view) {
            super(view);

            txtData = (TextView) view.findViewById(R.id.data);
            txtTitle = (TextView) view.findViewById(R.id.txtTitulo);
            txtDescricao = (TextView) view.findViewById(R.id.txtDescricao);
            img = (ImageView) view.findViewById(R.id.imgLista);
        }
    }

}
