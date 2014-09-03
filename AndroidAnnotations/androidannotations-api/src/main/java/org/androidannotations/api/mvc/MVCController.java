package org.androidannotations.api.mvc;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class MVCController {

    Context context;
    private Executor notImmediatelyExecutor = Executors
            .newScheduledThreadPool(1);
    private Executor executor;
    BroadcastReceiver receiver;
    private static  MVCController instance=null;
    public static void Bind(Context context) {
        if(instance==null)
        {
            instance=new MVCController(context);
        }
    }

    public void destroy() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    private MVCController(Context context) {
        this.context = context;
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                doAction(context, intent);
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver,
                new IntentFilter(CyclopsConstants.MVC_ADAPTER_MAIN_ACTION));
        int threadCount = Runtime.getRuntime().availableProcessors() * 2;
        if (threadCount < 4) {
            threadCount = 4;
        } else {
            if (threadCount > 8)
                threadCount = 8;
        }
        this.executor = new ThreadPoolExecutor(threadCount, threadCount, 1l,
                TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
    }


    public void doAction(final Context context, final Intent intent) {
        @SuppressWarnings("unchecked")
        final Class actionClass = (Class) intent
                .getSerializableExtra(CyclopsConstants.EXTRA_ACTION);
        if (actionClass != null) {
            Executor currentExecutor = intent.getIntExtra(
                    CyclopsConstants.EXTRA_PRIORITY, -1) >= 0 ? this.executor
                    : this.notImmediatelyExecutor;
            //System.out.println("Invoke:"+System.currentTimeMillis());
            currentExecutor.execute(new PriorityTask(intent.getIntExtra(
                    CyclopsConstants.EXTRA_PRIORITY, 0)) {
                @Override
                public void run() {
                    runMethod(actionClass, intent);
                }
            });
        }
    }

    private void runMethod(final Class actionClass,
            final Intent intent) {
        try {
            Object action = this.getAction(actionClass, intent);
            final String methodName = intent
                    .getStringExtra(CyclopsConstants.EXTRA_METHOD);
            Method method = actionClass.getMethod(methodName, Context.class,
                    Intent.class);
            method.setAccessible(true);
            method.invoke(action, context, intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getAction(final Class actionClass,
            final Intent intent) throws Exception {
        Method newInstance = null;
        // For android annotation
        try {
            newInstance = actionClass.getMethod("getInstance_", Context.class);
        } catch (Exception e) {
        }
        Object action = null;
        if (newInstance != null) {
            action = newInstance.invoke(null, context);
        } else {
            action = actionClass.newInstance();
        }
        return action;

    }

}
