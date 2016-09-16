/**
 * @description: 
 * @author chenshiqiang E-mail:csqwyyx@163.com
 * @date 2014年5月11日 下午4:55:14   
 * @version 1.0   
 */
package com.csq.thesceneryalong.ui.views.scenery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.csq.thesceneryalong.R;
import com.csq.thesceneryalong.db.Scenery;
import com.csq.thesceneryalong.utils.apiadapter.ApiUtil;
import com.csq.thesceneryalong.utils.apiadapter.video.IVideoView;
import com.csq.thesceneryalong.utils.apiadapter.video.VideoPlayerListener;
import com.csq.thesceneryalong.utils.dbmodel.SceneryUtil;

public class ScenenryVideoItemView extends RelativeLayout implements ScenenryItemView{

	// ------------------------ Constants ------------------------

	// ------------------------- Fields --------------------------
	protected IVideoView vvMedia;
	protected ImageView ivVideoPlay;
	protected ProgressBar pbProgress;
	
	//private Scenery scenery;
	
	private VideoPlayerListener listener = new VideoPlayerListener() {
		
		@Override
		public void onVideoStoped() {

			ivVideoPlay.post(new Runnable() {
				@Override
				public void run() {

					ivVideoPlay.setVisibility(View.VISIBLE);
				}
			});
		}
		
		@Override
		public void onVideoPrepared() {

			
		}
		
		@Override
		public void onVideoPlayed() {

			ivVideoPlay.post(new Runnable() {
				@Override
				public void run() {

					ivVideoPlay.setVisibility(View.INVISIBLE);
					pbProgress.setVisibility(View.VISIBLE);
				}
			});
		}
		
		@Override
		public void onVideoPaused() {

			ivVideoPlay.post(new Runnable() {
				@Override
				public void run() {

					ivVideoPlay.setVisibility(View.VISIBLE);
				}
			});
		}
		
		@Override
		public void onVideoEnd() {

			ivVideoPlay.post(new Runnable() {
				@Override
				public void run() {

					ivVideoPlay.setVisibility(View.VISIBLE);
					pbProgress.setProgress(100);
				}
			});
		}
		
		@Override
		public void onProgressChanged(int progress) {

			pbProgress.setProgress(progress);
		}

		@Override
		public void onSizeChanged(final int w, final int h) {

			/*pbProgress.post(new Runnable() {
				@Override
				public void run() {

					if(w > 0 && h > 0){
						pbProgress.setVisibility(View.VISIBLE);
					}else{
						pbProgress.setVisibility(View.INVISIBLE);
					}
				}
			});*/
		}
	};

	// ----------------------- Constructors ----------------------
	
	public ScenenryVideoItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	private void initView(Context context){
		View v = LayoutInflater.from(context).inflate(R.layout.itemview_scenery_video, 
				this, 
				true);
		
		
		vvMedia = ApiUtil.getVideoView(this, R.id.vvMedia);
		ivVideoPlay = (ImageView) v.findViewById(R.id.ivPlay);
		pbProgress = (ProgressBar) v.findViewById(R.id.pbProgress);
		
		vvMedia._setVideoListener(listener);
	}

	// -------- Methods for/from SuperClass/Interfaces -----------
	
	@Override
	protected void onDetachedFromWindow() {

		super.onDetachedFromWindow();
		onHide();
	}
	
	@Override
	public void setData(Scenery scenery) {

		//this.scenery = scenery;
		
		String path = SceneryUtil.getSceneryFilePath(scenery);
		vvMedia._setVideoPath(path);
	}

	@Override
	public void releaseResources() {

		vvMedia._release();
	}
	
	@Override
	public void onShow() {

		vvMedia._start();
		ivVideoPlay.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onHide() {

		if(vvMedia._isPlaying()){
			vvMedia._stop();
		}
		ivVideoPlay.setVisibility(View.VISIBLE);
		pbProgress.setVisibility(View.INVISIBLE);
	}

	// --------------------- Methods public ----------------------

	// --------------------- Methods private ---------------------

	// --------------------- Getter & Setter ---------------------

	// --------------- Inner and Anonymous Classes ---------------
}
