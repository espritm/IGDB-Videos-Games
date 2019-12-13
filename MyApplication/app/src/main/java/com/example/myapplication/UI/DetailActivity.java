package com.example.myapplication.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.Model.JeuVideo;
import com.example.myapplication.R;

import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {

    JeuVideo GameToShow;
    ImageView imgViewMeteo;
    TextView txtViewGameName;
    TextView txtViewGameSummary;
    TextView txtViewURL ;
    ListView listviewPlatforms;
    TextView txtviewAvailabloOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Display back arrow in navigation bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String jsonGameToShow = getIntent().getStringExtra("gameToShow");

        if (jsonGameToShow == null){
            showErrrorMsgAndFinishActivity();
        }

        try {
            GameToShow = new JeuVideo(jsonGameToShow);
        }
        catch (JSONException e){
            showErrrorMsgAndFinishActivity();
        }

        //update UI here//Retrouver les références de nos vues avec findViewByID
        txtViewGameName = findViewById(R.id.txtViewGameName);
        txtViewGameSummary = findViewById(R.id.txtViewGameSummary);
        txtViewURL = findViewById(R.id.txtViewURL);

        //Configurer l'interface avec les info du FcstDay à afficher.
        txtViewGameName.setText(GameToShow.getName());

        if (GameToShow.getSummary() != null)
            txtViewGameSummary.setText(GameToShow.getSummary());

        //Vidéos
        if (GameToShow.getVideoID() == null)
            txtViewURL.setVisibility(View.GONE);
        else
        {
            txtViewURL.setText(Html.fromHtml("<u>Voir la vidéo</u>"));

            txtViewURL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Load Youtube. If not installed, load www.youtube.com
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + GameToShow.getVideoID()));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + GameToShow.getVideoID()));
                    try {
                        startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        startActivity(webIntent);
                    }
                }
            });
        }

        //Platforms
        listviewPlatforms = findViewById(R.id.listviewPlatforms);
        txtviewAvailabloOn = findViewById(R.id.txtviewAvailabloOn);

        if (GameToShow.getPlatforms() == null || GameToShow.getPlatforms().size() == 0) {
            listviewPlatforms.setVisibility(View.GONE);
            txtviewAvailabloOn.setText(R.string.detailsActivity_availableOn_noInfo);
        }
        else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, GameToShow.getPlatforms());

            listviewPlatforms.setAdapter(adapter);
        }
    }

    private void showErrrorMsgAndFinishActivity(){
        new AlertDialog.Builder(this)
                .setTitle("Erreur")
                .setMessage("Données introuvables, merci de réessayer...")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DetailActivity.this.finish();
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_detail_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == R.id.menu_share){
            shareOnSocialNetwork();
        }

        //Si on a cliqué la flèche de retour dans la ToolBar
        if (item.getItemId() == android.R.id.home) {
            //finish();
            onBackPressed();
        }

        return true;
    }

    private void shareOnSocialNetwork() {

        //Paramètrer l'intent
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");

        //Ajouter le texte qu'on souhaite partager
        intent.putExtra(Intent.EXTRA_TEXT,
                "Regarde ce super jeu : " + GameToShow.getName() + " !");

        //Démarre l'Intent et offre la possibilité à l'utilisateur de choisir
        //son appli préférée
        startActivity(Intent.createChooser(intent, "Partager ce jeu"));
    }
}
