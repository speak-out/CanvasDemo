package administrator.example.com.canvasdemo;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.jakewharton.rxbinding.view.RxView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_get;
    private Button bt_post;
    private ImageView imageView;


     OkHttpClient client= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        bt_get = (Button) findViewById(R.id.bt_get);
//        RxView.clicks(bt_get).debounce(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                SystemClock.sleep(2000);
//                Toast.makeText(MainActivity.this,"点击我吧",Toast.LENGTH_LONG).show();
//            }
//        });



//        Observable.just(1,2,3,4)
//                .subscribeOn(Schedulers.io())             //观察者在哪里监听
//                .observeOn(Schedulers.computation())  //指定回调在哪一个线程
//                .subscribe(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer integer) {
//                        Log.e("打印字母",integer+"");
//                    }
//                });
//          Observable.just("longajin").map(new Func1<String, Bitmap>() {    //可以改变返回值
//              @Override
//              public Bitmap call(String s) {
//                  return null;
//              }
//          });
 //        WeakReference   弱应用
//        ImageLoad();
//        String[] names = new String[]{"李铁柱","赵二丫"};
//        Observable.from(names).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                    Log.e("name",s);
//            }
//        });

//        GetAndPostRequest();


    }

    private void ImageLoad() {
        final ImageView imageView = (ImageView) findViewById(R.id.imgageview);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawble = getTheme().getDrawable(R.drawable.linxinru);
                subscriber.onNext(drawble);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Drawable>() {
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this,"加载完成",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this,"加载失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNext(Drawable drawable) {
              imageView.setImageDrawable(drawable);
            }
        });
    }

    private void GetAndPostRequest() {
        String cachedirectory = Environment.getExternalStorageDirectory() + "/longjian/caches/";
        File httpCacheDirectory = new File(cachedirectory);
        // 暂时设置缓存 10M
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        //        OkHttpClient mOkHttpClient = OkHttpClient.Builder./*addInterceptor(interceptor).cache(cache) .build();*/
        OkHttpClient.Builder builder  =new  OkHttpClient.Builder();
        client =  builder.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR).cache(cache).build();

        bt_get=(Button)findViewById(R.id.bt_get);
        bt_post=(Button)findViewById(R.id.bt_post);

        bt_get.setOnClickListener(this);
        bt_post.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_get:
                getRequest();
                break;

            case R.id.bt_post:
                postRequest();
                break;

        }
    }

    private void getRequest() {

        final Request request=new Request.Builder()
                .get()
                .tag(this)
                .url("http://www.wooyun.org")
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.i("WY","打印GET响应的数据：" + response.body().string());
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void postRequest() {

//        RequestBody formBody = new FormEncodingBuilder()
//                .add("","")
//                .build();

        final Request request = new Request.Builder()
                .url("http://www.wooyun.org")
//                .post(formBody)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.i("WY","打印POST响应的数据：" + response.body().string());
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
//            Response originalResponse = chain.proceed(chain.request());
//            return originalResponse.newBuilder().removeHeader("Pragma")
//                    .header("Cache-Control", String.format("max-age=60")).build();//设置了缓存时间为60秒

            Request request = chain.request();
            Log.e("龙健","request="+request);
            Response response = chain.proceed(request);
            Log.e("龙健","response="+response);

            String cacheControl = request.cacheControl().toString();
            if (TextUtils.isEmpty(cacheControl)) {
                cacheControl = "public, max-age=60";
            }
            return response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }
    };

}
// public class RxBus {
//
//    private final Subject<Object, Object> _bus;
//
//    private static class RxBusHolder {
//        private static final RxBus instance = new RxBus();
//    }
//
//    private RxBus() {
//        _bus = new SerializedSubject<>(PublishSubject.create());
//    }
//
//    public static synchronized RxBus getInstance() {
//        return RxBusHolder.instance;
//    }
//
//    public void post(Object o) {
//        _bus.onNext(o);
//    }
//
//    public <T> Observable<T> toObserverable(Class<T> eventType) {
//        return _bus.ofType(eventType);
//    }
