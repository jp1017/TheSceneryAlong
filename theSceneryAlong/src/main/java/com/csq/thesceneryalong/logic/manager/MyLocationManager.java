/**
 * @description: 
 * @author chenshiqiang E-mail:csqwyyx@163.com
 * @date 2014年4月27日 下午5:26:53   
 * @version 1.0   
 */
package com.csq.thesceneryalong.logic.manager;

import android.location.Location;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.csq.thesceneryalong.app.App;
import com.csq.thesceneryalong.utils.location.LastLocationValidate;
import com.csq.thesceneryalong.utils.location.LocationUtil;

import java.util.ArrayList;
import java.util.List;


public class MyLocationManager implements LocationSource, AMapLocationListener{

	// ------------------------ Constants ------------------------
	
	private volatile static MyLocationManager instance;

	private LocationSource.OnLocationChangedListener mListener;        //高德定位监听器
	private AMapLocationClient mLocationClient;         //高德定位
	private AMapLocationClientOption mLocationOption;   //高德定位参数设置

	private double mLat;
	private double mLng;
	private float mSpeed;
	private float mDirection;


	public static MyLocationManager getInstance() {
		synchronized (MyLocationManager.class) {
			if (instance == null) {
				instance = new MyLocationManager();
			}
		}
		return instance;
	}
	
	private MyLocationManager() {
		mLocationClient = new AMapLocationClient(App.app);

		mLocationValider = new LastLocationValidate();
	}

	// ------------------------- Fields --------------------------
	

	/** 当前位置  */
    private Location mCurrentLocation;
    
    private LastLocationValidate mLocationValider = null;
	
	private AMapLocationListener locationListener = new AMapLocationListener() {


		@Override
		public void onLocationChanged(final AMapLocation loc) {
			if(loc == null){
        		return;
        	}
        	
        	if(loc.getTime() == 0){
        		//网络定位，时间可能为0
        		loc.setTime(System.currentTimeMillis());
        	}
        	
        	//纠偏,通知保存的都是gcj的经纬度
        	LocationUtil.wgsToGcj(loc, false);
        	
        	if(mCurrentLocation == null || mLocationValider.isBetterLocation(loc, mCurrentLocation)){
        		//位置有效
        		mCurrentLocation = loc;
        		
        		handler.removeMessages(WHAT_LOCATION_CHANGED);
        		Message msg = handler.obtainMessage();
            	msg.what = WHAT_LOCATION_CHANGED;
            	msg.obj = mCurrentLocation;
            	handler.sendMessage(msg);
        	}
		}

	};
	
	private final int WHAT_LOCATION_CHANGED = 6;
	private Handler handler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {

			if(msg.what == WHAT_LOCATION_CHANGED){
				
				Location loc = (Location) msg.obj;
				
				//位置更新相关操作
				for(LocationCallback lc : locationCallbacks){
					lc.locationChanged(loc);
				}
			}
			return false;
		}
	});
	
	/**
	 * 定位最短时间，毫秒
	 */
	private long locateMinTimeSeconds  = 5000;
	
	/**
	 * 定位最短距离，米
	 */
	private float locateMinDistanceMeters = 8;
	
	/**
	 * 所有应用程序位置监听器
	 */
	private List<LocationCallback> locationCallbacks 
		= new ArrayList<LocationCallback>();
	

	// ----------------------- Constructors ----------------------

	// -------- Methods for/from SuperClass/Interfaces -----------

	// --------------------- Methods public ----------------------
	
	private volatile boolean isListenLocation = false;
	/**
	 * @description: 开始定位
	 * @author: chenshiqiang E-mail:csqwyyx@163.com
	 */
	public synchronized void startListenLocation(){
/*		if(!isListenLocation){
			if(mLocationClient.getProvider(LocationProviderProxy.AMapNetwork) != null){
				// API定位采用GPS定位方式，第一个参数是定位provider，第二个参数时间最短是2000毫秒
				//第三个参数距离间隔单位是米，第四个参数是定位监听者
				mLocationClient.requestLocationUpdates(LocationProviderProxy.AMapNetwork,
						locateMinTimeSeconds, 
						locateMinDistanceMeters, 
						locationListener);
			}
			
			if(mLocationClient.getProvider(LocationManager.GPS_PROVIDER) != null){
				mLocationClient.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						locateMinTimeSeconds, 
						locateMinDistanceMeters, 
						locationListener);
			}
			
			isListenLocation = true;
		}*/
	}
	
	/**
	 * @description: 停止定位
	 * @author: chenshiqiang E-mail:csqwyyx@163.com
	 */
	public synchronized void stopListenLocation(){
		//只要有需要回调位置的地方，不能停止定位
		if(isListenLocation && locationCallbacks.isEmpty()){
//			mLocationClient.removeUpdates(locationListener);
			
			isListenLocation = false;
		}
	}
	
	/**
	 * @description: 注册一个位置回调
	 * @author: chenshiqiang E-mail:csqwyyx@163.com
	 * @param listener
	 */
	public void registLocationCallback(LocationCallback listener){
		locationCallbacks.add(listener);
	}
	/**
	 * @description: 取消注册一个位置回调
	 * @author: chenshiqiang E-mail:csqwyyx@163.com
	 * @param listener
	 */
	public void unRegistLocationCallback(LocationCallback listener){
		locationCallbacks.remove(listener);
	}

	// --------------------- Methods private ---------------------
	
	

	// --------------------- Getter & Setter ---------------------

	/**
	 * @description: 获取当前位置
	 * @author: chenshiqiang E-mail:csqwyyx@163.com
	 */
	public Location getCurrentLocation() {
		return mCurrentLocation;
	}
	
	/**
	 * @description: 获取最近gcj位置，先获取mCurrentLocation，再getLastKnowLocation(先gps，再网络)
	 * @author: chenshiqiang E-mail:csqwyyx@163.com
	 */
	public Location getLatestKnowLocation() {
		Location loc = getCurrentLocation();
		if(loc == null){
			loc = mLocationClient.getLastKnownLocation();
			if(loc == null){
				loc = mLocationClient.getLastKnownLocation();
			}
			//不是通过getLastKnownLocation获得的，都是新的，可以改变原始对象
			if(loc != null){
				LocationUtil.wgsToGcj(loc, false);
			}
		}
		return loc;
	}

	@Override
	public void onLocationChanged(AMapLocation loc) {
		if(loc == null){
			return;
		}

		if(loc.getTime() == 0){
			//网络定位，时间可能为0
			loc.setTime(System.currentTimeMillis());
		}

		//纠偏,通知保存的都是gcj的经纬度
		LocationUtil.wgsToGcj(loc, false);

		if(mCurrentLocation == null || mLocationValider.isBetterLocation(loc, mCurrentLocation)){
			//位置有效
			mCurrentLocation = loc;

			handler.removeMessages(WHAT_LOCATION_CHANGED);
			Message msg = handler.obtainMessage();
			msg.what = WHAT_LOCATION_CHANGED;
			msg.obj = mCurrentLocation;
			handler.sendMessage(msg);
		}
	}

	@Override
	public void activate(OnLocationChangedListener onLocationChangedListener) {
		mListener = onLocationChangedListener;
		if (mLocationClient == null) {
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mLocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
			mLocationOption.setInterval(5000);//设置自动定位时间间隔
			//设置定位参数
			mLocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mLocationClient.startLocation();
		}
	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mLocationClient != null) {
			mLocationClient.stopLocation();
			mLocationClient.onDestroy();
		}
		mLocationClient = null;
	}

	// --------------- Inner and Anonymous Classes ---------------
	
	public interface LocationCallback{
		public void locationChanged(Location newLoc);
	}
}
