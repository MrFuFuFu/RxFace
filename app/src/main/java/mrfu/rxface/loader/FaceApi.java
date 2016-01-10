package mrfu.rxface.loader;

import com.squareup.okhttp.OkHttpClient;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import mrfu.rxface.BuildConfig;
import mrfu.rxface.business.Constants;
import mrfu.rxface.loader.custom.CustomMultipartTypedOutput;
import mrfu.rxface.models.FaceResponse;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.QueryMap;
import rx.Observable;
import rx.functions.Func1;

/**
 * face api
 * Created by MrFu on 15/12/15.
 */
public class FaceApi {
    private static String mBoundry;
    private final static int boundaryLength = 32;
    private final static String boundaryAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";

    public static FaceApi instance;

    public static FaceApi getIns() {
        if (null == instance) {
            synchronized (FaceApi.class) {
                if (null == instance) {
                    instance = new FaceApi();
                }
            }
        }
        return instance;
    }

    private final MrFuService mWebService;

    public FaceApi() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.FACE_SERVER_IP)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setRequestInterceptor(mRequestInterceptor)
                .build();
        mWebService = restAdapter.create(MrFuService.class);
        mBoundry = setBoundary();
    }

    private RequestInterceptor mRequestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("connection", "keep-alive");
            request.addHeader("Content-Type", "multipart/form-data; boundary="+ getBoundary() + "; charset=UTF-8");
        }
    };

    public String getBoundary(){
        return mBoundry;
    }

    /**
     * 设置 Content-Type 的 boundary,这里有个强坑:
     * header 的 boundary 和 CustomMultipartTypedOutput 的 boundary 必须相同!!
     * @return mBoundry
     */
    private static String setBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < boundaryLength; ++i)
            sb.append(boundaryAlphabet.charAt(random.nextInt(boundaryAlphabet.length())));
        return sb.toString();
    }

    public interface MrFuService {
        @POST("/detection/detect")
        Observable<FaceResponse> uploadImagePost(
                @Body CustomMultipartTypedOutput listMultipartOutput
        );

        //http://apicn.faceplusplus.com/v2/detection/detect?api_key=7cd1e10dc037bbe9e6db2813d6127475&api_secret=gruCjvStG159LCJutENBt6yzeLK_5ggX&url=http://imglife.gmw.cn/attachement/jpg/site2/20111014/002564a5d7d21002188831.jpg
        @GET("/detection/detect")
        Observable<FaceResponse> uploadImageUrlGet(
                @QueryMap Map<String, String> options
        );
    }

    public Observable<FaceResponse> getDataPost(CustomMultipartTypedOutput listMultipartOutput) {
        return mWebService.uploadImagePost(listMultipartOutput)
                .timeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS)
                .concatMap(new Func1<FaceResponse, Observable<FaceResponse>>() {
                    @Override
                    public Observable<FaceResponse> call(FaceResponse faceResponse) {
                        return faceResponse.filterWebServiceErrors();
                    }
                }).compose(SchedulersCompat.<FaceResponse>applyExecutorSchedulers());
    }

    public Observable<FaceResponse> getDataUrlGet(Map<String, String> options) {
        return mWebService.uploadImageUrlGet(options)
                .timeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS)
                .concatMap(new Func1<FaceResponse, Observable<FaceResponse>>() {
                    @Override
                    public Observable<FaceResponse> call(FaceResponse faceResponse) {
                        return faceResponse.filterWebServiceErrors();
                    }
                }).compose(SchedulersCompat.<FaceResponse>applyExecutorSchedulers());//http://www.jianshu.com/p/e9e03194199e
    }
}
