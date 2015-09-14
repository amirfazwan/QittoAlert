package model;

/**
 * Created by amirfazwan on 10/9/15.
 */
public class Data {

    private String id;
    private String tahun;
    private String minggu;
    private String negeri;
    private String daerah;
    private String lokasi;
    private String kes_terkumpul;
    private String tempoh_wabak;
    private String latitude;
    private String longitude;

    public Data(String id, String tahun, String minggu, String negeri, String daerah, String lokasi, String kes_terkumpul, String tempoh_wabak, String latitude, String longitude) {
        this.id = id;
        this.tahun = tahun;
        this.minggu = minggu;
        this.negeri = negeri;
        this.daerah = daerah;
        this.lokasi = lokasi;
        this.kes_terkumpul = kes_terkumpul;
        this.tempoh_wabak = tempoh_wabak;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getMinggu() {
        return minggu;
    }

    public void setMinggu(String minggu) {
        this.minggu = minggu;
    }

    public String getNegeri() {
        return negeri;
    }

    public void setNegeri(String negeri) {
        this.negeri = negeri;
    }

    public String getDaerah() {
        return daerah;
    }

    public void setDaerah(String daerah) {
        this.daerah = daerah;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getKes_terkumpul() {
        return kes_terkumpul;
    }

    public void setKes_terkumpul(String kes_terkumpul) {
        this.kes_terkumpul = kes_terkumpul;
    }

    public String getTempoh_wabak() {
        return tempoh_wabak;
    }

    public void setTempoh_wabak(String tempoh_wabak) {
        this.tempoh_wabak = tempoh_wabak;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
