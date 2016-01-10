package mrfu.rxface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by MrFu on 16/1/10.
 */
public class BaseActivity extends AppCompatActivity {
    protected Subscription mSubscription = Subscriptions.empty();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(BaseActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(BaseActivity.this);
        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
    }
}
