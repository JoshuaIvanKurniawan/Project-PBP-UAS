package com.antoniuswicaksana.project_pbp.Views;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;


public class TambahEdit extends Fragment {
    private TextInputEditText txtTanggal, txtWaktu, txtKeterangan;
    private Button btnSimpan, btnBatal;
    private String status, Keterangan;
    private Jadwal Jadwal;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tambah_edit, container, false);
        setAtribut(view);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtWaktu.getText().length()<1 )
                {
                    if(txtWaktu.getText().length()<1)
                        txtWaktu.setError("Data Tidak Boleh Kosong");
                }
                else
                {
                    String Tanggal      = txtTanggal.getText().toString();
                    String Waktu     = txtWaktu.getText().toString();

                    if(status.equals("tambah"))
                        tambahJadwal(Tanggal, Waktu, Keterangan);
                    else
                        editJadwal(Tanggal, Waktu, Keterangan);
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ViewsJadwal());
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.btnSearch).setVisible(false);
        menu.findItem(R.id.btnAdd).setVisible(false);
    }

    public void setAtribut(View view){
        Jadwal   = (Jadwal) getArguments().getSerializable("Jadwal");
        txtTanggal      = view.findViewById(R.id.txtTanggal);
        txtWaktu     = view.findViewById(R.id.txtWaktu);
        txtKeterangan     = view.findViewById(R.id.txtKeterangan);
        btnSimpan   = view.findViewById(R.id.btnSimpan);
        btnBatal    = view.findViewById(R.id.btnBatal);

        status = getArguments().getString("status");


        if(status.equals("tambah"))
        {
            Glide.with(getContext())
                    .load("https://1080motion.com/wp-content/uploads/2018/06/NoImageFound.jpg.png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .circleCrop()
                    .skipMemoryCache(true);
        }
        else
        {
            txtTanggal.setEnabled(false);
            txtWaktu.setText(Jadwal.getWaktu());
            txtTanggal.setText(Jadwal.getTanggal());
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            fragmentTransaction.setReorderingAllowed(false);
        }
        fragmentTransaction.replace(R.id.tambah_edit_fragment, fragment)
                .detach(this)
                .attach(this)
                .commit();
    }

    //fungsi ini digunakan untuk menambahkan data Jadwal dengan butuh 4 parameter key
    //yang diperlukan (Tanggal, Waktu, jenis_kelamin, dan prodi) untuk parameter ini harus sama
    //Waktunya dan hal ini dapat dilihat pada fungsi getParams
    public void tambahJadwal(final String Tanggal, final String Waktu, final String Keterangan){
        //deklarasi queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data Jadwal");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, JadwalAPI.URL_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //bagian jika response berhasil
                        progressDialog.dismiss();
                        try {
                            //mengubah response string menjadi objek
                            JSONObject obj = new JSONObject(response);
                            //obj.getString("status") digunakan untuk mengambil pesan status dari response
                            if (obj.getString("status").equals("Success")) {
                                loadFragment(new ViewsJadwal());
                            }

                            //obj.getString("message") digunakan untuk mengambil pesan message dari response
                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //bagian jika response jaringan terdapat gangguan/error
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        //proses memasukkan / mengirimkan parameter key dengan data
                        //value dan Waktu key nya harus sesuai dengan parameter key ang diminta
                        //oleh jaringan API
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Tanggal", Tanggal);
                        params.put("Waktu", Waktu);
                        params.put("Keterangan", Keterangan);


                        return params;
                    }
                };

        //proses penambahan request yang sudah kita buat ke request queue
        //yang sudah dideklarasi
        queue.add(stringRequest);
    }

    //fungsi ini digunakan untuk mengubah data Jadwal dengan butuh 3 parameter key yang
    //diperlukan (Waktu, jenis_kelamin dan prodi) untuk parameter in iharus sama Waktunua
    //dan hal ini dapat dilihat pada fungsi getParams
    public void editJadwal(final String Tanggal, final String Waktu, final String Keterangan){
        //deklarasi queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengubah data Jadwal");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(PUT, JadwalAPI.URL_UPDATE + Tanggal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            //mengubah response string menjadi objek
                            JSONObject obj = new JSONObject(response);

                            //obj.geetString("message") digunakan untuk mengambil pesan dari response
                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                            loadFragment(new ViewsJadwal());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        //proses memasukkan / mengirimkan parameter key dengan data
                        //value dan Waktu key nya harus sesuai dengan parameter key ang diminta
                        //oleh jaringan API
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Waktu", Waktu);
                        params.put("Keterangan", Keterangan);

                        return params;
                    }
                };

        //proses penambahan request yang sudah kita buat ke request queue
        //yang sudah dideklarasi
        queue.add(stringRequest);
    }
}
