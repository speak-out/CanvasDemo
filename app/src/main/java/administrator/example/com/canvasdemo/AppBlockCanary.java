package administrator.example.com.canvasdemo;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;

/**
 * Created by yuer on 2016/6/21.
 */
public class AppBlockCanary  extends BlockCanaryContext{
    @Override
    public int getConfigDuration() {
        return 500;
    }

}
