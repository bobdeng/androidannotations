package org.androidannotations.annotations.fragment;

/**
 * Created by zhiguodeng on 14-10-31.
 * 用于处理在Activity中侦听Fragment的事件
 */
public interface FragmentEventListener {
    public void onFragmentEvent(int event, Object... args);
}
