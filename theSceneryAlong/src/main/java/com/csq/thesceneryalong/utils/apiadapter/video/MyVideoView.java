/**
 * @description: 
 * @author chenshiqiang E-mail:csqwyyx@163.com
 * @date 2014年5月21日 下午10:52:50   
 * @version 1.0   
 */
package com.csq.thesceneryalong.utils.apiadapter.video;

import android.content.Context;
import android.util.AttributeSet;

import com.csq.thesceneryalong.ui.views.video.VideoView8;

public class MyVideoView extends VideoView8 implements IVideoView {

	public MyVideoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}
	
	public MyVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}
	
	private void initView(){
	}

	@Override
	public void _setVideoPath(String path) {

		setVideoPath(path);
	}

	@Override
	public void _start() {

		start();
	}

	@Override
	public void _pause() {

		pause();
	}

	@Override
	public void _stop() {

		stopPlayback();
	}

	@Override
	public boolean _isPlaying() {

		return isPlaying();
	}

	@Override
	public void _setVideoListener(VideoPlayerListener listener) {

		setVideoListener(listener);
	}

	@Override
	public void _release() {

		release();
	}

}
