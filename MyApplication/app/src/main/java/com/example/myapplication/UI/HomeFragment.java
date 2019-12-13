package com.example.myapplication.UI;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.JeuxVideosAdapter;
import com.example.myapplication.Model.DonneesJeuxVideos;
import com.example.myapplication.R;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static final int GEOLOCATION_PERMISSION_REQUEST_CODE = 42;

    JeuxVideosAdapter Adapter;
    ListView ListViewDays;
    SwipeRefreshLayout Refresher;
    TextView WelcomeTextView;
    DonneesJeuxVideos JeuxVideos;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        WelcomeTextView = v.findViewById(R.id.textviewWelcome);
        Refresher = v.findViewById(R.id.swiperefreshlayout);
        ListViewDays = v.findViewById(R.id.listview);

        String sUserLogin = getActivity().getPreferences(Context.MODE_PRIVATE)
                .getString("connectedUserLogin", "");

        WelcomeTextView.setText("Salut " + sUserLogin + " !! Voici les jeux vidéos :");

        //Instancie l'adapter et l'associe a la listview
        Adapter = new JeuxVideosAdapter(getActivity(), 0);
        ListViewDays.setAdapter(Adapter);

        //Lorsque Rafraichi manuellement la page
        Refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });

        Refresher.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateData();
            }
        }, 0);

        ListViewDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("gameToShow", Adapter.getItem(position).getJson());
                getActivity().startActivity(intent);
            }
        });

        return v;
    }

    private void updateData(){
        Refresher.setRefreshing(true);

        String sURL = "https://api-v3.igdb.com/games?fields=name,summary,platforms,platforms.name,videos,videos.name,videos.video_id&limit=10";

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.GET, sURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jSon = response;

                        try {
                            JeuxVideos = new DonneesJeuxVideos(jSon);
                        }
                        catch (JSONException e){
                            String sErrMsg = "Erreur durant la desserialisation du Json";

                            if (jSon.contains("City or coordinate not found"))
                                sErrMsg = "Ville inconnue. Merci d'en saisir une autre !";

                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Erreur")
                                    .setMessage(sErrMsg)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //nothing to do
                                        }
                                    })
                            .show();
                        }

                        //Peupler la Listview
                        if (JeuxVideos != null)
                            Adapter.addAll(JeuxVideos.getGames());

                        //Cacher le refresher
                        Refresher.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ;
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user-key", "8eb6b1f7036c72b50dcdec0fbece7410");

                return params;
            }
        }
        ;

        queue.add(request);
    }
}
