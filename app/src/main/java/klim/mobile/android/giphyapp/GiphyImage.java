package klim.mobile.android.giphyapp;

import android.graphics.Bitmap;

/**
 * Created by e.klim on 11.08.2016.
 */
public class GiphyImage {

    private String url;
    private String imgID;
    private Bitmap sourse;


    public GiphyImage(String url){
        this.url = url;
    }

    public GiphyImage(String url, String id, Bitmap bitmap){
        this.url = url;
        imgID = id;
        sourse = bitmap;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getSourse() {
        return sourse;
    }

    public void setSourse(Bitmap sourse) {
        this.sourse = sourse;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }
}
