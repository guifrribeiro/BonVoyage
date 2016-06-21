package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Guilherme on 15/06/2016.
 */
@DatabaseTable(tableName = "country") //transformando a classe em uma tabela
public class Country implements Serializable {
    @DatabaseField(columnName = "ID", id = true)
    private Integer id;
    @DatabaseField(columnName = "ISO")
    private String iso;
    @DatabaseField(columnName = "SHORTNAME")
    private String sn;
    @DatabaseField(columnName = "LONGNAME")
    private String ln;
    @DatabaseField(columnName = "CALLINGCODE")
    private String callingCode;
    @DatabaseField(columnName = "STATUS")
    private Integer status; //campo que vai informar se o país já foi visitado
    @DatabaseField(columnName = "CULTURE")
    private String culture;
    @DatabaseField(columnName = "URLFLAG")
    private String urlFlag;
    @DatabaseField(columnName = "DATE")
    private String dateVisited;
    @DatabaseField(columnName = "DETAILS")
    private String detailsTravel;

    public Country(String detailsTravel, Date dateVisited, String urlFlag, String culture, Integer status, String callingCode, String ln, String sn, String iso, Integer id) {
        this.detailsTravel = detailsTravel;
        this.urlFlag = urlFlag;
        this.culture = culture;
        this.status = status;
        this.callingCode = callingCode;
        this.ln = ln;
        this.sn = sn;
        this.iso = iso;
        this.id = id;
    }

    public Country(String sn, String urlFlag) {
        this.sn = sn;
        this.urlFlag = urlFlag;
    }

    public Country(){}

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getCallingCode() {
        return callingCode;
    }

    public void setCallingCode(String callingCode) {
        this.callingCode = callingCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCulture() { return culture; }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public String getUrlFlag() {
        return urlFlag;
    }

    public void setUrlFlag(String urlFlag) {
        this.urlFlag = urlFlag;
    }

    public String getDate() {
        return dateVisited;
    }

    public void setDate(String dateVisited) {
        this.dateVisited = dateVisited;
    }

    public String getDetailsTravel() {
        return detailsTravel;
    }

    public void setDetailsTravel(String detailsTravel) {
        this.detailsTravel = detailsTravel;
    }
}
