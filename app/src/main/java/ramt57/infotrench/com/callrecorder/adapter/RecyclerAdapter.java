package ramt57.infotrench.com.callrecorder.adapter;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;

/**
 * Created by sandhya on 22-Aug-17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private  ArrayList<Contacts> contacts=new ArrayList<>();
    private final int VIEW1 = 0, VIEW2 = 1;
    public RecyclerAdapter(){

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder;
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW1:
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.people_contact,parent,false);
                viewHolder = new MyViewHolder(view);
                break;
            case VIEW2:
                View v2 = inflater.inflate(R.layout.no_contact_list,parent, false);
                viewHolder = new MyViewHolder(v2);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
        }
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        switch (holder.getItemViewType()){
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
        return contacts.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileimage;
        TextView name;
        TextView number;
        ImageView fav;
        TextView time;
        ImageView play,delete,favorite;
        public MyViewHolder(View itemView) {
            super(itemView);
            profileimage=(CircleImageView)itemView.findViewById(R.id.profile_image);
            name=(TextView)itemView.findViewById(R.id.textView2);
            number=(TextView)itemView.findViewById(R.id.textView3);
            fav=(ImageView)itemView.findViewById(R.id.imageView);
            time=(TextView)itemView.findViewById(R.id.textView4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog openDialog = new Dialog(view.getContext());
                    openDialog.setContentView(R.layout.dialog_lyout);
                    openDialog.setTitle("Select Option");
                    openDialog.setCanceledOnTouchOutside(true);
                    play=(ImageView)openDialog.findViewById(R.id.imageView2);
                    favorite=(ImageView)openDialog.findViewById(R.id.imageView3);
                    delete=(ImageView)openDialog.findViewById(R.id.imageView4);
                    play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            Toast.makeText(view.getContext(),contacts.get(getAdapterPosition()).getNumber(),Toast.LENGTH_SHORT).show();
                            openDialog.dismiss();
                        }
                    });
                    openDialog.show();
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
            this.contacts=contacts;
    }
}
