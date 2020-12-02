package com.antoniuswicaksana.project_pbp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.antoniuswicaksana.project_pbp.databinding.AdapterRecyclerViewBinding;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Event> result = new ArrayList<>();

    public RecyclerViewAdapter(Context context, List<Event> result){
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterRecyclerViewBinding adapterRecyclerViewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.adapter_recycler_view, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(adapterRecyclerViewBinding);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Event evt = result.get(position);
        holder.adapterRecyclerViewBinding.setEvt(evt);
//        Glide.with(context).load(mhs.imgURL).into(holder.foto_profil);
//        if (!mhs.getImgURL().equals("")){
//            Glide.with(context)
//                    .load(mhs.getImgURL())
//                    .into(holder.foto_profil);
//        }else{
//            holder.foto_profil.setImageResource(R.drawable.ic_broken_image);
//        }
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterRecyclerViewBinding adapterRecyclerViewBinding;

        public MyViewHolder(@NonNull AdapterRecyclerViewBinding adapterRecyclerViewBinding){
            super(adapterRecyclerViewBinding.getRoot());
            this.adapterRecyclerViewBinding = adapterRecyclerViewBinding;
        }
        public void onClick(View view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }

}
