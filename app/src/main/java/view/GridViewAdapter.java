package view;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.bonvoyage.R;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.CountryDao;
import dao.DatabaseHelper;
import model.Country;

/**
 * Created by Guilherme on 16/06/2016.
 */
public class GridViewAdapter extends BaseAdapter {

    //Context
    private Context context;

    private int resource;
    private LayoutInflater inflater;

    private NetworkImageView flagImage;
    private TextView shortname;
    private ImageLoader imageLoader;

    private CountryDao countryDao;
    private DatabaseHelper dbHelper;

    //Array que contém as countries
    private ArrayList<Country> countries;

    private SparseBooleanArray selectedIds;

    public GridViewAdapter (Context context, int resource, ArrayList<Country> countries, ImageLoader imageLoader){
        //Getting all the values
        this.context = context;
        this.countries = countries;
        this.imageLoader = imageLoader;
        inflater = LayoutInflater.from(context);
        dbHelper = new DatabaseHelper(context);
        try {
            countryDao = new CountryDao(dbHelper.getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int position) {
        return countries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.grid_adapter, null);

            // Localizar campos no xml
            holder.flagImage = (NetworkImageView) view.findViewById(R.id.country_flag_image);
            holder.shortName = (TextView) view.findViewById(R.id.textViewGrid);
            holder.imageVisited = (ImageView) view.findViewById(R.id.imageVisited);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        Country country;

        holder.flagImage.setVisibility(View.VISIBLE);
        holder.flagImage.setDefaultImageResId(R.mipmap.ic_launcher_tbv);

        // setando os conteudos
        holder.flagImage.setImageUrl(countries.get(position).getUrlFlag(), imageLoader);
        holder.shortName.setText(countries.get(position).getSn());
        try {
            country = countryDao.queryForId(countries.get(position).getId());
        } catch (SQLException e) {
            country = null;
            e.printStackTrace();
        }
        if(country != null && country.getStatus() == 0) {
            holder.imageVisited.setVisibility(View.VISIBLE);
        } else {
            holder.imageVisited.setVisibility(View.GONE);
        }
        return view;
    }

    public static class Holder {
        NetworkImageView flagImage;
        ImageView imageVisited;
        TextView shortName;
    }
}

