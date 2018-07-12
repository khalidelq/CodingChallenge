package khalidelq.app.codingchallenge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import khalidelq.app.codingchallenge.R;
import khalidelq.app.codingchallenge.models.Photo;


public class SimpleGrid_Adapter extends BaseAdapter {

    Context c ;
    ArrayList<Photo> photos ;

    public SimpleGrid_Adapter(Context c, ArrayList<Photo> albums) {
        this.photos = albums ;
        this.c = c;
    }

    @Override
    public int getCount() {
        return (photos == null) ? 0 : photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(photos.get(position).getId());
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        // Recycle the the old Views
        ViewHolder holder;
        if(convertview == null) {
            convertview = LayoutInflater.from(c).inflate(R.layout.simple_grid_item_layout,viewGroup,false);
            holder = new ViewHolder();
            holder._grid_item_image = (ImageView) convertview.findViewById(R.id.simple_grid_item_image);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }
        // Set the Album Image
        Picasso.with(c)
                .load(photos.get(position).getLowResUrl())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder._grid_item_image);


        return convertview;
    }

}
