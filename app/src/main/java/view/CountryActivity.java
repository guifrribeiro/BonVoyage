package view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.bonvoyage.R;

import java.sql.SQLException;
import java.util.Calendar;
import controller.VolleySingleton;
import dao.CountryDao;
import dao.DatabaseHelper;
import model.Country;

public class CountryActivity extends AppCompatActivity {

    ImageLoader imageLoader;

    CountryDao countryDao;
    DatabaseHelper dbHelper;

    TextView shortname;
    NetworkImageView flagImage;
    TextView callingCode;
    EditText longname;
    EditText editDetails;
    TextView editDate;
    Button btnVisited;
    Button btnDelete;
    EditText editCulture;

    Country country;
    Country countryShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        dbHelper = new DatabaseHelper(this);

        final Intent intent = getIntent();
        country = (Country) intent.getSerializableExtra("country");
        imageLoader = VolleySingleton.getInstance(this.getApplicationContext()).getImageLoader();

        shortname = (TextView) findViewById(R.id.textSn);
        callingCode = (TextView) findViewById(R.id.textCC);
        longname = (EditText) findViewById(R.id.editLn);
        flagImage = (NetworkImageView) findViewById(R.id.imageFlagCountry);
        editDetails = (EditText) findViewById(R.id.editDetails);
        editDate = (TextView) findViewById(R.id.editDate);
        btnVisited = (Button) findViewById(R.id.buttonVisited);
        btnDelete = (Button) findViewById(R.id.buttonDelVisit);
        editCulture = (EditText) findViewById(R.id.editCulture);

        showInfos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.country, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            updateCountry();
        } else if (id == R.id.action_delete) {
            deleteCountry();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showInfos() {

        try {
            countryDao = new CountryDao(dbHelper.getConnectionSource());

            if(countryDao.queryForId(country.getId()) != null) {

                countryShow = countryDao.queryForId(country.getId());

                shortname.setText(countryShow.getSn());
                callingCode.setText(countryShow.getCallingCode());
                longname.setText(countryShow.getLn());
                editDate.setText(countryShow.getDate());
                editDetails.setText(countryShow.getDetailsTravel());
                editCulture.setText(countryShow.getCulture());
                flagImage.setImageUrl(countryShow.getUrlFlag(), imageLoader);
            } else {
                shortname.setText(country.getSn());
                callingCode.setText(country.getCallingCode());
                longname.setText(country.getLn());
                editCulture.setText(country.getCulture());
                flagImage.setImageUrl(country.getUrlFlag(), imageLoader);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateCountry() {
        try {

            countryDao = new CountryDao(dbHelper.getConnectionSource());

            country.setStatus(0);
            country.setLn(String.valueOf(longname.getText()));
            country.setCulture(String.valueOf(editCulture.getText()));
            country.setDate(String.valueOf(editDate.getText()));
            country.setDetailsTravel(String.valueOf(editDetails.getText()));
            countryDao.deleteById(country.getId());
            countryDao.create(country);

            Toast.makeText(getApplicationContext(), "Viagem salva com sucesso!", Toast.LENGTH_LONG).show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finish();
    }

    public void deleteCountry() {
        try {

            countryDao = new CountryDao(dbHelper.getConnectionSource());
            countryDao.deleteById(country.getId());

            Toast.makeText(getApplicationContext(), "Viagem apagada com sucesso!", Toast.LENGTH_LONG).show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finish();
    }

    public void changeDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(id == 999) {
            return new DatePickerDialog(this, dateListener, year, month, day);
        } else {
            return null;
        }
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            StringBuilder date = new StringBuilder().append(dayOfMonth).append("/").append(monthOfYear+1).append("/").append(year);
            editDate.setText(date);
        }
    };
}

