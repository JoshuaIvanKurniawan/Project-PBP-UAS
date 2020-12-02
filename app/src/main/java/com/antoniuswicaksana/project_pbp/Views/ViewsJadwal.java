package com.antoniuswicaksana.project_pbp.Views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antoniuswicaksana.project_pbp.API.JadwalAPI;
import com.antoniuswicaksana.project_pbp.Adapters.AdapterJadwal;
import com.antoniuswicaksana.project_pbp.Models.Jadwal;
import com.antoniuswicaksana.project_pbp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class ViewsJadwal extends Fragment {
    private RecyclerView recyclerView;
    private AdapterJadwal adapter;
    private List<Jadwal> listJadwal;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_views_jadwal, container, false);
        setAdapter();
        getJadwal();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem         = menu.findItem(R.id.btnSearch);
        final MenuItem addItem      = menu.findItem(R.id.btnAdd);

        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                addItem.setVisible(false);
                return false;
            }
        });

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnAdd) {
            Bundle data = new Bundle();
            data.putString("status", "tambah");
            TambahEdit tambahEdit = new TambahEdit();
            tambahEdit.setArguments(data);

            loadFragment(tambahEdit);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setAdapter(){
        getActivity().setTitle("Data Jadwal");
        listJadwal = new ArrayList<Jadwal>();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new AdapterJadwal(view.getContext(), listJadwal);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.views_jadwal_fragment,fragment)
                .commit();
    }

    //Fungsi menampilkan data Jadwal
    public void getJadwal() {
        //deklarasi queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //meminta tanggapan string daari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data Jadwal");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, JadwalAPI.URL_READ,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //bagian jika response berhasil
                progressDialog.dismiss();
                try {
                    //mengambil data response json object yang berupa data Jadwal
                    JSONArray jsonArray = response.getJSONArray("Jadwal");

                    if (!listJadwal.isEmpty())
                        listJadwal.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //mengubah data jsonArray tertentu menjadi object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        String id                   = jsonObject.optString("id");
                        String tanggal              = jsonObject.optString("tanggal");
                        String waktu                = jsonObject.optString("waktu");
                        String keterangan           = jsonObject.optString("keterangan");


                        //membuat objek user
                        Jadwal Jadwal =
                                new Jadwal(id, tanggal, waktu, keterangan);

                        //menambahkan obejk user tadi ke list user
                        listJadwal.add(Jadwal);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), response.optString("messsage"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //bagian jika response jaringan terdapat gangguan/error
                progressDialog.dismiss();
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //proses penambahan request yang sudah kita buat ke request queue
        //yang sudah dideklarasi
        queue.add(stringRequest);
    }
}
