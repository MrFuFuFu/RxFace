package mrfu.rxface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mrfu.rxface.business.Constants;
import mrfu.rxface.business.DealData;
import mrfu.rxface.loader.FaceApi;
import mrfu.rxface.models.FacePlusPlusEntity;
import mrfu.rxface.models.NeedDataEntity;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.iv_face_get)
    ImageView iv_face_get;

    @Bind(R.id.iv_face_post)
    ImageView iv_face_post;

    @Bind(R.id.btn_recongize_get)
    Button btn_recongize_get;

    @Bind(R.id.btn_recongize_post)
    Button btn_recongize_post;

    @Bind(R.id.tv_age_get)
    TextView tv_age_get;

    @Bind(R.id.tv_age_post)
    TextView tv_age_post;

    /**
     * 0:GET button
     * 1:POST button
     */
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Glide.with(this).load(Constants.IMAGE_URL).fitCenter().into(iv_face_get);
        iv_face_post.setImageResource(R.drawable.jobs);
    }

    @OnClick(R.id.btn_recongize_get)
    public void btn_recongize_get(View view){
        type = 0;
        Map<String, String> options = new HashMap<>();
        options.put("api_key", Constants.API_KEY);
        options.put("api_secret", Constants.API_SECRET);
        options.put("url", Constants.IMAGE_URL);
        FaceApi.getIns()//api_key={apiKey}&api_secret={apiSecret}&url={imgUrl}
                .getDataUrlGet(options)
                .cache()
                .flatMap(new Func1<FacePlusPlusEntity, Observable<NeedDataEntity>>() {
                    @Override
                    public Observable<NeedDataEntity> call(FacePlusPlusEntity facePlusPlusEntity) {
                        Bitmap bitmap = null;
                        try {
                            bitmap = Glide.with(MainActivity.this).load(Constants.IMAGE_URL).asBitmap().into(-1,-1).get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        NeedDataEntity entity = new NeedDataEntity();
                        entity.bitmap = DealData.drawLineGetBitmap(facePlusPlusEntity, bitmap);
                        entity.displayStr = DealData.getDisplayInfo(facePlusPlusEntity);
                        return Observable.just(entity);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(setBitmapDataObserver);
    }

    @OnClick(R.id.btn_recongize_post)
    public void btn_recongize_post(View view){
        type = 1;
        BitmapDrawable mDrawable =  (BitmapDrawable) iv_face_post.getDrawable();
        final Bitmap bitmap = mDrawable.getBitmap();

        FaceApi.getIns()
                .getDataPost(DealData.mulipartData(bitmap, FaceApi.getIns().getBoundary()))
                .cache()
                .flatMap(new Func1<FacePlusPlusEntity, Observable<NeedDataEntity>>() {
                    @Override
                    public Observable<NeedDataEntity> call(FacePlusPlusEntity facePlusPlusEntity) {
                        NeedDataEntity entity = new NeedDataEntity();
                        entity.bitmap = DealData.drawLineGetBitmap(facePlusPlusEntity, bitmap);
                        entity.displayStr = DealData.getDisplayInfo(facePlusPlusEntity);
                        return Observable.just(entity);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(setBitmapDataObserver);
    }

    private Observer<NeedDataEntity> setBitmapDataObserver = new Observer<NeedDataEntity>() {

        @Override
        public void onNext(final NeedDataEntity needDataEntity) {
            if (needDataEntity == null){
                return;
            }
            switch (type){
                case 0:
                    if (!TextUtils.isEmpty(needDataEntity.displayStr)){
                        tv_age_get.setText(needDataEntity.displayStr);
                    }
                    if (needDataEntity.bitmap != null){
                        iv_face_get.setImageBitmap(needDataEntity.bitmap);
                    }
                    break;
                case 1:
                    if (!TextUtils.isEmpty(needDataEntity.displayStr)){
                        tv_age_post.setText(needDataEntity.displayStr);
                    }
                    if (needDataEntity.bitmap != null){
                        iv_face_post.setImageBitmap(needDataEntity.bitmap);
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onCompleted() {
            Log.i("MrFu", "onCompleted");
        }

        @Override
        public void onError(final Throwable error) {
            error.printStackTrace();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Uri uri = Uri.parse("https://github.com/MrFuFuFu/ImageViewEx");
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
