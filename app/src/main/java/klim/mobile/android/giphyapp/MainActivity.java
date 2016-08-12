package klim.mobile.android.giphyapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String NEW_RANDOM_IMAGE = "http://api.giphy.com/v1/gifs/random?api_key=";
    private ImageStack stack;
    private ImageView leftImg;
    private ImageView rightImg;
    private startVote startVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            stack = (ImageStack) savedInstanceState.getSerializable("stack");
        } catch (Exception e){
            stack = null;
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        leftImg = (ImageView) findViewById(R.id.left);
        rightImg = (ImageView) findViewById(R.id.right);
        leftImg.setOnClickListener(this);
        rightImg.setOnClickListener(this);

        if(stack == null) {
            startVote = (startVote) getLastCustomNonConfigurationInstance();
            if(startVote == null) {
                startVote = new startVote();
                startVote.recognition(this);
                startVote.execute(NEW_RANDOM_IMAGE.concat(getString(R.string.key_API)));
            } else {
                startVote.recognition(this);
            }

        } else {
            letsPlay();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_exit:
                finish();
                break;
            default:
                Toast.makeText(this,"dfsfsf",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left:
                try {
                    GiphyImage gi = stack.changeImage(v.getTag());
                    rightImg.setImageBitmap(gi.getSourse());
                    rightImg.setTag(gi);
                } catch (Exception e){
                    Toast.makeText(this,"Пожалуйста, выбирайте медленней",Toast.LENGTH_SHORT).show();
                    stack.LoadNewImage();
                }

                break;
            case R.id.right:
                try {
                    GiphyImage gi = stack.changeImage(v.getTag());
                    leftImg.setImageBitmap(gi.getSourse());
                    leftImg.setTag(gi);
                } catch (Exception e){
                    Toast.makeText(this,"Пожалуйста, выбирайте медленней",Toast.LENGTH_SHORT).show();
                    stack.LoadNewImage();
                }
                break;
        }
    }

    private static class startVote extends AsyncTask<String,Void,ArrayList<GiphyImage>>{

        MainActivity context;
        ProgressDialog pd;

        void recognition(MainActivity activity){
            context = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("wait...");
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setMax(3);
            pd.show();
        }

        @Override
        protected ArrayList<GiphyImage> doInBackground(String... params) {
            ArrayList<GiphyImage> giphyImages = new ArrayList<>();
            for (int i=0; i< 3 ; i++){
                try {
                    JSONObject object = NetUtil.getJSON(params[0]);
                    giphyImages.add(NetUtil.getGiphy(object));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                pd.setProgress(i);
            }
            return giphyImages;
        }

        @Override
        protected void onPostExecute(ArrayList<GiphyImage> images) {
            super.onPostExecute(images);
            pd.dismiss();
            if(images != null )
                if (images.size() == 3) {
                    context.stack = new ImageStack(context, images);
                    context.letsPlay();
                }
            else{
                Toast.makeText(context,"что-то пошло не так",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void letsPlay(){
        leftImg.setImageBitmap(stack.getLeftImage().getSourse());
        leftImg.setTag(stack.getLeftImage());
        rightImg.setImageBitmap(stack.getRightImage().getSourse());
        rightImg.setTag(stack.getRightImage());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("stack",stack);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return startVote;
    }
}
