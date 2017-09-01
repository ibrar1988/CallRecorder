package ramt57.infotrench.com.callrecorder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;

/**
 * Created by sandhya on 01-Sep-17.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder7> {
    private static ArrayList<Contacts> contacts=new ArrayList<>();
    private final int VIEW1 = 0, VIEW2 = 1;
    Context ctx;
    public FavouriteAdapter(){

    }
    @Override
    public  FavouriteAdapter.MyViewHolder7 onCreateViewHolder(ViewGroup parent, int viewType) {
        FavouriteAdapter.MyViewHolder7 viewHolder;
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW1:
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.people_contact,parent,false);
                viewHolder = new  FavouriteAdapter.MyViewHolder7(view);
                ctx=view.getContext();
                break;
            case VIEW2:
                View v2 = inflater.inflate(R.layout.no_contact_list,parent, false);
                viewHolder = new  FavouriteAdapter.MyViewHolder7(v2);
                ctx=v2.getContext();
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new  FavouriteAdapter.MyViewHolder7(v);
                ctx=v.getContext();
                break;
        }
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(FavouriteAdapter.MyViewHolder7 holder, int position) {
        switch (holder.getItemViewType()){
            case VIEW1:
                if(ContactProvider.checkFav(ctx,contacts.get(position).getNumber())){
                    //not favourite
                    holder.favorite.setImageResource(R.drawable.ic_star_border_black_24dp);
                }else{
                    //favourite
                    holder.favorite.setImageResource(R.drawable.ic_star_black_24dp);
                }
                if(ContactProvider.checkContactToRecord(ctx,contacts.get(position).getNumber())){
                    //record
                    holder.state.setImageResource(R.drawable.ic_microphone);
                }else{
                    //dont wanna record
                    holder.state.setImageResource(R.drawable.ic_muted);
                }
                holder.name.setText(contacts.get(position).getName());
                holder.number.setText(contacts.get(position).getNumber());
                if(contacts.get(position).getPhoto()!=null){
                    holder.profileimage.setImageBitmap(contacts.get(position).getPhoto());
                }else {
                    holder.profileimage.setImageResource(R.drawable.profile);
                }
                holder.time.setText(contacts.get(position).getTime());
                break;
            case VIEW2:
                if(ContactProvider.checkFav(ctx,contacts.get(position).getNumber())){
                    //not favourite
                    holder.favorite.setImageResource(R.drawable.ic_star_border_black_24dp);
                }else{
                    //favourite
                    holder.favorite.setImageResource(R.drawable.ic_star_black_24dp);
                }
                if(ContactProvider.checkContactToRecord(ctx,contacts.get(position).getNumber())){
                    //record
                    holder.state.setImageResource(R.drawable.ic_microphone);
                }else{
                    //dont wanna record
                    holder.state.setImageResource(R.drawable.ic_muted);
                }
                holder.name.setText(contacts.get(position).getNumber());
                holder.time.setText(contacts.get(position).getTime());
                break;
        }

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    public static class MyViewHolder7 extends RecyclerView.ViewHolder{
        CircleImageView profileimage;
        TextView name;
        TextView number;
        TextView time;
        ImageView state,favorite;
        public MyViewHolder7(View itemView) {
            super(itemView);
            profileimage=(CircleImageView)itemView.findViewById(R.id.profile_image);
            name=(TextView)itemView.findViewById(R.id.textView2);
            number=(TextView)itemView.findViewById(R.id.textView3);
            favorite=(ImageView)itemView.findViewById(R.id.imageView);
            time=(TextView)itemView.findViewById(R.id.textView4);
            state=(ImageView)itemView.findViewById(R.id.imageView5);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(contacts.get(position).getName()!=null){
            return VIEW1;
        }else{
            return VIEW2;
        }
    }
    public void setContacts(ArrayList<Contacts> contacts){
        FavouriteAdapter.contacts=contacts;

    }
}
