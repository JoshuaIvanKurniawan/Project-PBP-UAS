package com.antoniuswicaksana.project_pbp.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.antoniuswicaksana.project_pbp.API.JadwalAPI;
import com.antoniuswicaksana.project_pbp.Models.Jadwal;
import com.antoniuswicaksana.project_pbp.R;
import com.antoniuswicaksana.project_pbp.Views.TambahEdit;
import com.antoniuswicaksana.project_pbp.Views.ViewsJadwal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.DELETE;

public class AdapterJadwal extends RecyclerView.Adapter<AdapterJadwal.adapterUserViewHolder> {

    private List<Jadwal> JadwalList;
    private List<Jadwal> JadwalListFiltered;
    private Context context;
    private View view;

    public AdapterJadwal(Context context, List<Jadwal> JadwalList) {
        this.context=context;
        this.JadwalList = JadwalList;
        this.JadwalListFiltered = JadwalList;
    }

    @NonNull
    @Override
    public adapterUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_adapter_jadwal, parent, false);
        return new adapterUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterUserViewHolder holder, int position) {
        final Jadwal Jadwal = JadwalListFiltered.get(position);

        holder.txtId.setText(Jadwal.getId());
        holder.txtTanggal.setText(Jadwal.getTanggal());
        holder.txtWaktu.setText(Jadwal.getWaktu());
        holder.txtKeterangan.setText(Jadwal.getKeterangan());
        Glide.with(context)
                .load(Jadwal.getGambar())
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivGambar);

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Bundle data = new Bundle();
                data.putSerializable("Jadwal", Jadwal);
                data.putString("status", "edit");
                TambahEdit tambahEdit = new TambahEdit();
                tambahEdit.setArguments(data);
                loadFragment(tambahEdit);
            }
        });

        holder.ivHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Anda yakin ingin menghapus Jadwal ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteJadwal(Jadwal.getId());

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyDataSetChanged();
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (JadwalListFiltered != null) ? JadwalListFiltered.size() : 0;
    }

    public class adapterUserViewHolder extends RecyclerView.ViewHolder {
        private TextView txtId, txtTanggal, txtWaktu, txtKeterangan, ivEdit, ivHapus;
        private ImageView ivGambar;

        public adapterUserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId         = (TextView) itemView.findViewById(R.id.txtId);
            txtTanggal          = (TextView) itemView.findViewById(R.id.txtTanggal);
            txtWaktu = (TextView) itemView.findViewById(R.id.txtWaktu);
            txtKeterangan        = (TextView) itemView.findViewById(R.id.txtKeterangan);
            ivGambar        = (ImageView) itemView.findViewById(R.id.ivGambar);
            ivEdit          = (TextView) itemView.findViewById(R.id.ivEdit);
            ivHapus         = (TextView) itemView.findViewById(R.id.ivHapus);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString().toLowerCase();
                if (userInput.isEmpty()) {
                    JadwalListFiltered = JadwalList;
                }
                else {
                    List<Jadwal> filteredList = new ArrayList<>();
                    for(Jadwal Jadwal : JadwalList) {
                        if(Jadwal.getWaktu().toLowerCase().contains(userInput) ||
                                Jadwal.getId().toLowerCase().contains(userInput)) {
                            filteredList.add(Jadwal);
                        }
                    }
                    JadwalListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = JadwalListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                JadwalListFiltered = (ArrayList<Jadwal>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.views_jadwal_fragment,fragment)
                .addToBackStack(null)
                .commit();
    }

    //Fungsi menghapus data Jadwal
    public void deleteJadwal(String npm){
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data Jadwal");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(DELETE, JadwalAPI.URL_DELETE + npm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            //mengubah response string menjadi objek
                            JSONObject obj = new JSONObject(response);

                            //obj.geetString("message") digunakan untuk mengambil pesan dari response
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                            loadFragment(new ViewsJadwal());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //proses penambahan request yang sudah kita buat ke request queue
        //yang sudah dideklarasi
        queue.add(stringRequest);
    }
}