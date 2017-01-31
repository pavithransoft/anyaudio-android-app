package any.audio.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import any.audio.Config.Constants;
import any.audio.Managers.FontManager;
import any.audio.Models.PlaylistItem;
import any.audio.Network.ConnectivityUtils;
import any.audio.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ankit on 1/31/2017.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistItemHolder> {

    private static Context context;
    private static PlaylistAdapter mInstance;
    public ArrayList<PlaylistItem> playlistItems;
    private Typeface typeface;
    private PlaylistItemListener playlistItemListener;

    public PlaylistAdapter(Context context) {
        this.context = context;
        playlistItems = new ArrayList<>();
        typeface = FontManager.getInstance(context).getTypeFace(FontManager.FONT_MATERIAL);
    }

    public void setPlaylistItem(ArrayList<PlaylistItem> items){
        this.playlistItems = items;
         notifyDataSetChanged();
    }

    public static PlaylistAdapter getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PlaylistAdapter(context);
        }
        return mInstance;
    }

    @Override
    public PlaylistItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View playlistView = LayoutInflater.from(context).inflate(R.layout.playlist_item,null,false);
        return new PlaylistItemHolder(playlistView);

    }

    @Override
    public void onBindViewHolder(PlaylistItemHolder holder, int position) {

        PlaylistItem currentItem = playlistItems.get(position);

        holder.title.setText(currentItem.getTitle());
        holder.artist.setText(currentItem.getUploader());
        holder.removeBtn.setTypeface(typeface);

        if(ConnectivityUtils.getInstance(context).isConnectedToNet()){
            Picasso.with(context).load(getImageUrl(currentItem.getYoutubeId())).into(holder.thumbnail);
        }

    }

    @Override
    public int getItemCount() {
        return playlistItems.size();
    }

    private String getImageUrl(String vid) {
        //return "https://i.ytimg.com/vi/kVgKfScL5yk/hqdefault.jpg";
        return "https://i.ytimg.com/vi/"+vid+"/hqdefault.jpg";  // additional query params => ?custom=true&w=240&h=256
    }

    public void popItem() {
        playlistItems.remove(0);
        notifyItemRemoved(0);
    }

    public void appendItem(PlaylistItem item) {
        playlistItems.add(item);
        notifyItemInserted(playlistItems.size()-1);
    }

    public static class PlaylistItemHolder extends RecyclerView.ViewHolder{

        CircleImageView thumbnail;
        TextView title;
        TextView artist;
        TextView removeBtn;

        public PlaylistItemHolder(View itemView) {
            super(itemView);

            thumbnail = (CircleImageView) itemView.findViewById(R.id.playlist_item_thumbnail);
            title = (TextView) itemView.findViewById(R.id.playlist_item_title);
            artist = (TextView) itemView.findViewById(R.id.playlist_item_artist);
            removeBtn = (TextView) itemView.findViewById(R.id.playlist_item_cancel_btn_text);

            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PlaylistAdapter adapter = PlaylistAdapter.getInstance(context);
                    adapter.streamItem(getAdapterPosition());

                }
            });

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PlaylistAdapter adapter = PlaylistAdapter.getInstance(context);
                    adapter.streamItem(getAdapterPosition());

                }
            });

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    }

    private void streamItem(int adapterPosition) {

        if(playlistItemListener!=null){
            playlistItemListener.onPlaylistItemTapped(playlistItems.get(adapterPosition));
        }

    }

    public void setPlaylistItemListener(PlaylistItemListener listener){
        this.playlistItemListener = listener;
    }

    public interface PlaylistItemListener{

        void onPlaylistItemTapped(PlaylistItem item);

    }

}
