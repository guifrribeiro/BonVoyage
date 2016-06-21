package dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import model.Country;

/**
 * Created by Guilherme on 19/06/2016.
 */
public class CountryDao extends BaseDaoImpl<Country, Integer> {

    public CountryDao(ConnectionSource connectionSource) throws SQLException {
        super(Country.class);
        setConnectionSource(connectionSource);
        initialize();
    }
}
