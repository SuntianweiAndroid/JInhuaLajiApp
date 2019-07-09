package com.speedata.jinhualajidemo.printerdemo.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * aapt-generated IDs have the high byte nonzero; 
 * clamp to the range under that.  
 * Created by xzc-pc on 2017-07-28.
 */
    public class  IdUtils {

    /** 
     * id 
     * 兼容sdk17(4.2.2)以下 
     * Created by zst on 2016/12/5. 
     */
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.  
            if (sNextGeneratedId.compareAndSet(result, newValue))
            {
                return result;
            }
        }
    }
}
