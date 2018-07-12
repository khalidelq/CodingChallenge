package khalidelq.app.codingchallenge.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.GraphRequest;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import khalidelq.app.codingchallenge.R;
import khalidelq.app.codingchallenge.models.Album;

public class Grid_Adapter extends BaseAdapter {

    Context c ;
    ArrayList<Album> albums ;

    public Grid_Adapter(Context c, ArrayList<Album> albums) {
        this.albums = albums ;
        this.c = c;
    }

    @Override
    public int getCount() {
        return (albums == null) ? 0 : albums.size();
    }

    @Override
    public Object getItem(int position) {
        return albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(albums.get(position).getId());
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        // Recycle the the old Views
        ViewHolder holder;
        if(convertview == null) {
            convertview = LayoutInflater.from(c).inflate(R.layout.list_item_layout,viewGroup,false);
            holder = new ViewHolder();
            holder._grid_item_image = (ImageView) convertview.findViewById(R.id.grid_item_image);
            holder._grid_item_title = (TextView) convertview.findViewById(R.id.grid_item_title);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }

        // Set the Album Title
        holder._grid_item_title.setText(albums.get(position).getName());
        // Set the Album Image
        Picasso.with(c)
                .load(albums.get(position).getUrl())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder._grid_item_image);


        return convertview;
    }

}
