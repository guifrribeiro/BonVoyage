package dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import model.Country;

/**
 * Created by Guilherme on 19/06/2016.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "countries.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Country, Integer> countryDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource, Country.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onCreate(db, connectionSource);
    }

    public Dao<Country, Integer> getCountryDao() throws SQLException {
        if (countryDao == null) {
            try {
                countryDao = getDao(Country.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return countryDao;
    }

    @Override
    public void close() {
        super.close();
        countryDao = null;
    }

    public Dao<Country, Integer> getDao() throws java.sql.SQLException {
        if(countryDao == null) {
            countryDao = getDao(Country.class);
        }
        return countryDao;
    }
}
