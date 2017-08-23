package ramt57.infotrench.com.callrecorder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;

/**
 * Created by sandhya on 22-Aug-17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    ArrayList<Contacts> contacts=new ArrayList<>();
    public RecyclerAdapter(ArrayList<Contacts> contacts){
        this.contacts=contacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.people_contact,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(contacts.get(position).getName());
        holder.number.setText(contacts.get(position).getNumber());
        holder.profileimage.setImageBitmap(contacts.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileimage;
        TextView name;
        TextView number;
        ImageView fav;
        public MyViewHolder(View itemView) {
            super(itemView);
            profileimage=(CircleImageView)itemView.findViewById(R.id.profile_image);
            name=(TextView)itemView.findViewById(R.id.textView2);
            number=(TextView)itemView.findViewById(R.id.textView3);
            fav=(ImageView)itemView.findViewById(R.id.imageView);
        }
    }

}
