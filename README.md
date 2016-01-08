<<<<<<< HEAD
<<<<<<< HEAD
#Use androidannotations to MVP
###Declare a Interface of View
```
public interface MyView{
  public void showName(String name);
}
```
###Declare bean interface
```
public interface IMyBean{
  public void getUserName();
}
```
###Implement bean
```
@EBean
public MyBean implements IMyBean{
  @ViewInterface //New Annotation
  MyView myView;
  @Override
  @Background //Use AA to run background
  public void getUserName(){
     //read user from db or Http server or other
     myView.showName("hello");
  }
}
```
###In Activity,Fragment,Service or only a common Class
```
@EActivity(R.layout.layout_main)
public MyActivity{
  @Bean(MyBean.class)
  IMyBean myBean;
  
  @Click(R.id.btn)
  public void click(){
    //Call myBean, excuted in background
    myBean.getUserName();
  }
  //MyBean will invoke this in method getUserName, so need to be UIThread safe
  @UIThread
  public void showName(String name){
    showToast(name);
  }
}
```
#EIntent,IntentExtra
```
@EIntent("BroadcastAction-XX")
public class MyIntent{
  @IntentExtra
  public String name;
  @IntentExtra(IntentObjectType.PARCELABLE)
  public UserInfo userInfo;
  @IntentExtra
  public SerializableObject obj;
}
```
###sendBroadCast
```
sendBroadCast(new MyIntent_().setName("name").setUserInfo(userInfo).getIntent());
```
###receive broadcast
```
MyIntent_ myIntent=MyIntent_.build(intent);
myIntent.getName()
```
#ContextEvent
###Write a event interface
```
public interface IMyEvent{
	public void onEvent1(String action);
}
```
###In Fragment,Adapter,View..
```
@ContextEvent
IMyEvent myEvent;
.............
myEvent.onEvent1("action");

```
###In activity
```
//Not necessary,but recommend implements IMyEvent
implements IMyEvent
public void onEvent1(String action){
	//do something
}
```

[AndroidAnnotations](http://androidannotations.org/)
=======
# Fast Android Development. Easy maintenance.

=======
# Fast Android Development. Easy maintenance.

### Note: we presented AndroidAnnotations at [Devoxx 2012](http://devoxx.com/display/DV12/Android+DDD+%28Diet+Driven+Development%29%21), video available on [Parleys.com](http://www.parleys.com/#st=5&id=3550)!

>>>>>>> 96090578b57011fab18dcfe714da38bca2899feb
[![Android Annotations Logo](https://github.com/excilys/androidannotations/wiki/img/aa-logo.png)](https://github.com/excilys/androidannotations/wiki/Home) 

**AndroidAnnotations** is an Open Source framework that **speeds up** Android development.
It takes care of the **plumbing**, and lets you concentrate on what's really important. By **simplifying** your code, it facilitates its **maintenance**.

### [**Documentation**](https://github.com/excilys/androidannotations/wiki/Home)

<<<<<<< HEAD
[![Build Status](https://travis-ci.org/excilys/androidannotations.svg?branch=develop)](https://travis-ci.org/excilys/androidannotations/builds) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AndroidAnnotations-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/128)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/excilys/androidannotations?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
>>>>>>> dd05c0576d7b8da39d46c5589ee672f2d2c6c556
=======
[![githalytics.com alpha](https://cruel-carlota.pagodabox.com/a2b0e064a3f1d4fb3d5406cc596797f2 "githalytics.com")](http://githalytics.com/excilys/androidannotations)
>>>>>>> 96090578b57011fab18dcfe714da38bca2899feb
