package klim.mobile.android.giphyapp;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by e.klim on 11.08.2016.
 */
public class ImageStack implements Serializable {

    private final String NEW_RANDOM_IMAGE = "http://api.giphy.com/v1/gifs/random?api_key=";

    private Context context;
    private String key;
    private GiphyImage LeftImage;
    private GiphyImage RightImage;
    private GiphyImage HoldImage;

    public ImageStack(Context c, ArrayList<GiphyImage> arrayList){
        context = c;
        key = context.getResources().getString(R.string.key_API);
        LeftImage = arrayList.get(0);
        RightImage = arrayList.get(1);
        HoldImage = arrayList.get(2);
    }

    public GiphyImage getLeftImage(){
        return LeftImage;
    }

    public GiphyImage getRightImage(){
        return RightImage;
    }

    public GiphyImage changeImage(Object o) {
        if (HoldImage != null) {
            if (o.equals(LeftImage)) {
                RightImage = HoldImage;
                HoldImage = null;
                LoadNewImage();
                return RightImage;
            } else {
                LeftImage = HoldImage;
                HoldImage = null;
                LoadNewImage();
                return LeftImage;
            }
        }
        return null;
    }

    public void LoadNewImage(){
        String url = NEW_RANDOM_IMAGE.concat(key);
        getImageAsync getImage = new getImageAsync();
        getImage.execute(url);
    }

    private class getImageAsync extends AsyncTask<String,Void,GiphyImage>{

        @Override
        protected GiphyImage doInBackground(String... params) {
            try {
                JSONObject object = NetUtil.getJSON(params[0]);
                return NetUtil.getGiphy(object);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(GiphyImage image) {
            super.onPostExecute(image);
            if(image != null){
                HoldImage = image;
            }
        }
    }
}
