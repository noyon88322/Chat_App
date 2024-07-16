package com.example.test;

import static com.example.test.chat_window.receverIMG;
import static com.example.test.chat_window.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messagesAdapter extends RecyclerView.Adapter{

    Context context;

  ArrayList<msgModelClass> messageAdapterArrayList;
    int ITEM_SENDER = 1;
    int ITEM_RECEVER = 2;

    public messagesAdapter(Context context, ArrayList<msgModelClass> messageAdapterArrayList) {
        this.context = context;
        this.messageAdapterArrayList = messageAdapterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_SENDER){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);

            return new senderViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout,parent,false);
            return new reciverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msgModelClass messages = messageAdapterArrayList.get(position);
        if(holder.getClass() == senderViewHolder.class){
            senderViewHolder ViewHolder = (senderViewHolder) holder;
           ViewHolder.sendertextset.setText(messages.getMessage());
            Picasso.get().load(senderImg).into(ViewHolder.circleImg);
        }else {
            reciverViewHolder ViewHolder = (reciverViewHolder) holder;
            ViewHolder.recivertextset.setText(messages.getMessage());
            Picasso.get().load(receverIMG).into(ViewHolder.circleImg);
        }

    }

    @Override
    public int getItemCount() {
        return messageAdapterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        msgModelClass messages = messageAdapterArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
            return ITEM_SENDER;
        }else {
            return ITEM_RECEVER;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImg;
        TextView sendertextset;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);


            sendertextset=itemView.findViewById(R.id.sendertextset);
            circleImg=itemView.findViewById(R.id.circleImg);


        }
    }

    class reciverViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImg;
        TextView recivertextset;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);

            recivertextset=itemView.findViewById(R.id.recivertextset);
            circleImg=itemView.findViewById(R.id.circleImg);
        }
    }
}
