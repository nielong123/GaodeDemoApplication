package gaode.trajectory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import bean.Bean.ReturnValueBean.DataListBean;

/**
 * Created by nl on 2016/11/22.
 */

public final class TrajectRunnable implements Runnable {

    private static final int MOVEMARKER = 0;           //移动点
    private static final int MOVEFINISH = 1;           //移动完成
    private static final int MOVESTOP = 2;             //停止移动
    private static final int MOVESTART = 3;            //开始移动
    //    private static final int UPDATAMOVEINDEX = 4;          //更新运动状态
    private static final int MOVERESTART = 5;          //重启移动
    private static final int FORCERESTART = 6;            //强制更改到初始状态

    private int playIndex, maxIndex;
    private long DELYED = 100l;
    private boolean isStop;
    private TrajectListener listener;
    private TrajectHandler handler;

    public TrajectRunnable(int maxIndex, TrajectListener trajectListener) {
        this.maxIndex = maxIndex;
        this.listener = trajectListener;
        this.handler = new TrajectHandler(this);
    }

    /****
     * 停止播放
     */
    public void stopPlay() {
        isStop = true;
    }

    /***
     * 重启播放
     */
    public void reStartPlay() {
        isStop = false;
    }

    /****
     * 设置播放进度
     *
     * @param index
     */
    public void setPlayIndex(int index) {
        this.playIndex = index;
    }

    @Override
    public void run() {
        if (maxIndex != 0) {
            if (playIndex == 0) {
                Message msg = new Message();
                msg.what = MOVESTART;
                handler.sendMessage(msg);
            }
            if (playIndex == maxIndex) {
                Message msg = new Message();
                msg.what = MOVEFINISH;
                handler.sendMessage(msg);
            }
            Message msg = new Message();
            msg.what = MOVEMARKER;
            Bundle bundle = new Bundle();
            bundle.putInt(MOVEMARKER + "", playIndex++);
            msg.setData(bundle);
            handler.sendMessage(msg);
            if (!isStop) {
                handler.postDelayed(this, DELYED);
            }
        }
    }

    class TrajectHandler extends Handler {
        WeakReference<TrajectRunnable> trajectRunnable;

        public TrajectHandler(TrajectRunnable trajectRunnable) {
            super(Looper.getMainLooper());
            this.trajectRunnable = new WeakReference<TrajectRunnable>(trajectRunnable);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TrajectRunnable runnable = trajectRunnable.get();
            switch (msg.what) {
                case MOVEMARKER:
                    Bundle bundle = msg.getData();
                    if (runnable.listener != null) {
                        runnable.listener.onUpdataPlaying(bundle.getInt(MOVEMARKER + ""));
                    }
                    break;
                case MOVEFINISH:
                    playIndex = 0;
                    if (runnable.listener != null) {
                        runnable.listener.onFinishPlaying();
                    }
                    break;
                case MOVESTOP:
                    if (runnable.listener != null) {
                        runnable.listener.onStopPlaying();
                    }
                    break;
                case MOVESTART:
                    if (runnable.listener != null) {
                        runnable.listener.onStartPlaying();
                    }
                    break;
//                case UPDATAMOVEINDEX:
//                    break;
                case MOVERESTART:
                    break;
                case FORCERESTART:
                    break;
            }
        }
    }

    public interface TrajectListener {

        public void onUpdataPlaying(int index);

        public void onStartPlaying();

        public void onStopPlaying();

        public void onFinishPlaying();
    }
}
