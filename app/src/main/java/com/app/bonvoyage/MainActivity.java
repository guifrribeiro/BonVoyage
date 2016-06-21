package com.app.bonvoyage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.util.LruCache;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.CountryDao;
import dao.DatabaseHelper;
import model.Country;
import view.CountryActivity;
import view.GridViewAdapter;
import view.LoginFbActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int menu;

    private String urlBase = "http://mypushapi-dev.us-east-1.elasticbeanstalk.com/world/countries/active";
    private String urlFlagBase = "http://mypushapi-dev.us-east-1.elasticbeanstalk.com/world/countries/";
    private static String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ImageLoader imageLoader;

    private GridView gridView;

    private DatabaseHelper dbHelper;
    private CountryDao countryDao;
    private ArrayList<Country> countriesFinal;
    private GridViewAdapter gridViewAdapter;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        gridView = (GridView) findViewById(R.id.gridView);

        setSupportActionBar(toolbar);

        countriesFinal = new ArrayList<Country>();
        //setando o Helper
        dbHelper = new DatabaseHelper(this);
        gridViewAdapter = new GridViewAdapter(this, R.layout.grid_adapter, countriesFinal, imageLoader);

        AdapterView.OnItemClickListener gridClick = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(MainActivity.this, CountryActivity.class);
                intent.putExtra("country", countriesFinal.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };

        gridView.setOnItemClickListener(gridClick);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        pDialog = new ProgressDialog(this);

        //makeJsonArrayRequest();
        //makeJsonObject();
        makeJsonArray();

    }

    private ArrayList<Country> makeJsonArray() {

        showpDialog("Viajando o mundo..");
        JsonArrayRequest request = new JsonArrayRequest(urlBase, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        countriesFinal.clear();

                                try {

                                    for(int i = 0; i < response.length(); i++) {
                                        JSONObject country = (JSONObject) response.get(i);
                                        //organizar saporra
                                        Country c = new Country();
                                        c.setId((Integer) country.get("id"));
                                        c.setIso((String) country.get("iso"));
                                        c.setSn((String) country.get("shortname"));
                                        c.setLn((String) country.get("longname"));
                                        c.setCallingCode((String) country.get("callingCode"));
                                        c.setStatus((Integer) country.get("status"));
                                        c.setCulture((String) country.get("culture"));
                                        c.setUrlFlag(urlFlagBase + country.get("id").toString() + "/flag");

                                        countriesFinal.add(c);

                                    }

                                    showGrid(countriesFinal);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Ocorreu um erro com o avião, tente viajar novamente mais tarde.", Toast.LENGTH_LONG).show();
                                }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Verifique sua conexão com a internet para continuar a viagem.");
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(request);

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });

        gridView.setAdapter(new GridViewAdapter(this, R.layout.grid_adapter, countriesFinal, imageLoader));
        //VolleyApplication.getInstance().getRequestQueue().add(request);
        menu = 0;
        return countriesFinal;
    }

    private ArrayList<Country> makeJsonArrayVisited() {

        showpDialog("Lembrando viagens..");
        JsonArrayRequest request = new JsonArrayRequest(urlBase, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    countryDao = new CountryDao(dbHelper.getConnectionSource());
                    countriesFinal.clear();

                    if((countryDao.countOf()) > 0) {

                        countriesFinal = (ArrayList<Country>) countryDao.queryForEq("STATUS", 0);

                        showGrid(countriesFinal);

                    } else {
                        Toast.makeText(getApplicationContext(), "Não temos lembranças, por favor no ajude a ter lembranças fascinantes.", Toast.LENGTH_LONG).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Verifique sua conexão com a internet para continuar a viagem.");
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(request);

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });

        //gridView.setAdapter(new GridViewAdapter(this, R.layout.grid_adapter, countriesFinal, imageLoader));
        //VolleyApplication.getInstance().getRequestQueue().add(request);
        menu = 1;
        return countriesFinal;
    }

    public void showGrid(ArrayList<Country> countries){
        //Looping through all the elements of json array
        gridView.setAdapter(null);
        //Creating GridViewAdapter Object
        GridViewAdapter gridViewAdapter = new GridViewAdapter(this, R.layout.grid_adapter, countries, imageLoader);
        //Adding adapter to gridview
        gridView.setAdapter(gridViewAdapter);

    }

    private void showpDialog(String text) {
        pDialog.setMessage(text);
        pDialog.setCancelable(false);
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (!pDialog.isShowing()) ;
        pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_countries) {
            makeJsonArray();
            // Handle the camera action
        } else if (id == R.id.nav_visited) {
            makeJsonArrayVisited();
        /*} else if (id == R.id.nav_slideshow) {*/

        } else if (id == R.id.nav_manage) {
            Intent loginFbActivity = new Intent(this, LoginFbActivity.class);
            startActivity(loginFbActivity);
        } /*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(menu == 0) {
            makeJsonArray();
        } else if (menu == 1) {
            makeJsonArrayVisited();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.app.bonvoyage/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.app.bonvoyage/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public boolean reloadActivity(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_countries) {
            makeJsonArray();
            // Handle the camera action
        } else if (id == R.id.nav_visited) {
            makeJsonArrayVisited();
        /*} else if (id == R.id.nav_slideshow) {*/

        } else if (id == R.id.nav_manage) {
            Intent loginFbActivity = new Intent(this, LoginFbActivity.class);
            startActivity(loginFbActivity);
        } /*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
