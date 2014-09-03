package org.androidannotations.api.mvc;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

public class MVCAdapter
{
	
	private Map<String, Method> actionMethod = new HashMap<String, Method>();
	private BroadcastReceiver receiver;
	private Object owner;
	
	private Context context;
	
	public MVCAdapter(Context context)
	{
		this.context = context;
	}
	
	public void register(final Object obj)
	{
		owner = obj;
		receiver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				doResponse(context,intent);
			}
		};
		
		Method [] methods = obj.getClass().getMethods();
		
		for(Method m : methods)
		{
		    if(m.getName().startsWith(CyclopsConstants.PREFIX_RESPONSE)){
				actionMethod.put(m.getName(), m);
				LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter(m.getName()));
			}
		}
	}
	public void invokeAction(Class action,String method, Intent intent,boolean broadcast,int priority)
	{
	    if(intent==null) intent=new Intent();
		intent.putExtra(CyclopsConstants.EXTRA_PRIORITY, priority);
		this.invokeAction(action, method, intent,broadcast);
	}
	/**
	 * 
	 * @param action
	 * @param method
	 * @param intent
	 * @param broadcast if set false,only the owner can receive the result.
	 */
	public void invokeAction(Class action,String method, Intent intent,boolean broadcast)
	{
		if(intent == null)
		{
			intent = new Intent();
		}
		intent.setAction(CyclopsConstants.MVC_ADAPTER_MAIN_ACTION);
		intent.putExtra(CyclopsConstants.EXTRA_ACTION, action);
		if(TextUtils.isEmpty(method)){
			method=CyclopsConstants.DEFAULT_METHOD_NAME;
		}
		intent.putExtra(CyclopsConstants.EXTRA_METHOD, method);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
	private void doResponse(Context context, Intent intent)
	{
		Method m = actionMethod.get(intent.getAction());
		if(m != null)
		{
			try
			{
			   
				m.setAccessible(true);
				
				m.invoke(owner, context, intent);
 			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}

	public void unregister()
	{
		LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
		actionMethod.clear();
		this.owner=null;
		receiver=null;
		this.context=null;
	}
}
