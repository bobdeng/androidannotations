#Use androidannotations to MVP
##Declare a Interface of View
```
public interface MyView{
  public void showName(String name);
}
```
##Declare bean interface
```
public interface IMyBean(){
  public void getUserName();
}
```
##Implement bean
```
@EBean
public MyBean implements IMyBean(){
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
##In Activity,Fragment,Service or only a common Class
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
