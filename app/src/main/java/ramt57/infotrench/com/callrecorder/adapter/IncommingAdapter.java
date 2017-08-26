package ramt57.infotrench.com.callrecorder.adapter;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.fragments.Incomming;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;

/**
 * Created by sandhya on 26-Aug-17.
 */

public class IncommingAdapter extends RecyclerView.Adapter<IncommingAdapter.MyViewHolder> {
    private static ArrayList<Contacts> contacts=new ArrayList<>();
    private final int VIEW1 = 0, VIEW2 = 1;
    static IncommingAdapter.itemClickListener listener;
    public IncommingAdapter(){

    }

    public void setListener(itemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public IncommingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IncommingAdapter.MyViewHolder viewHolder;
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW1:
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.people_contact,parent,false);
                viewHolder = new IncommingAdapter.MyViewHolder(view);
                break;
            case VIEW2:
                View v2 = inflater.inflate(R.layout.no_contact_list,parent, false);
                viewHolder = new IncommingAdapter.MyViewHolder(v2);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new IncommingAdapter.MyViewHolder(v);
                break;
        }
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(IncommingAdapter.MyViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW1:
                holder.name.setText(contacts.get(position).getName());
                holder.number.setText(contacts.get(position).getNumber());
                holder.profileimage.setImageBitmap(contacts.get(position).getPhoto());
                holder.time.setText(contacts.get(position).getTime());
                break;
            case VIEW2:
                holder.name.setText(contacts.get(position).getNumber());
                holder.time.setText(contacts.get(position).getTime());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return IncommingAdapter.contacts.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileimage;
        TextView name;
        TextView number;
        ImageView fav;
        TextView time;
        ImageView play,delete,favorite;
        public MyViewHolder(final View itemView) {
            super(itemView);
            profileimage=(CircleImageView)itemView.findViewById(R.id.profile_image);
            name=(TextView)itemView.findViewById(R.id.textView2);
            number=(TextView)itemView.findViewById(R.id.textView3);
            fav=(ImageView)itemView.findViewById(R.id.imageView);
            time=(TextView)itemView.findViewById(R.id.textView4);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view,getAdapterPosition());
                }
            });
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
        IncommingAdapter.contacts=contacts;
    }

    public interface itemClickListener{
        public void onClick(View v,int position);
    }
}