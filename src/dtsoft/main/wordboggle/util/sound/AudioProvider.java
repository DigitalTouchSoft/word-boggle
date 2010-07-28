package dtsoft.main.wordboggle.util.sound;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import dtsoft.main.wordboggle.R;

public class AudioProvider {
	public static int BAD_WORD_SUBMITED = 0;
	public static int LETTER_CLICKED = 1;
	public static int WORD_CANCELED = 2;
	public static int GOOD_WORD_SUBMITED = 3;
	
	private Context mContext;
	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundPoolMap;
	
	public AudioProvider(Context context) {
		mContext = context;
		init();
	}
	
	private void init() {
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mSoundPoolMap.put(BAD_WORD_SUBMITED, mSoundPool.load(mContext, R.raw.badsubmit, 1));
		mSoundPoolMap.put(LETTER_CLICKED, mSoundPool.load(mContext, R.raw.buttonclick, 1));	
		mSoundPoolMap.put(WORD_CANCELED, mSoundPool.load(mContext, R.raw.buttoncancel, 1));
		mSoundPoolMap.put(GOOD_WORD_SUBMITED, mSoundPool.load(mContext, R.raw.goodsubmit, 1));
	}
	
	private int[] mSoundFiles;
	
	public void playSounds(int[] sounds) {
		mSoundFiles = sounds;
		
		new Thread() {
			@Override
			public void run() {
				playSounds();
			}
		}.start();
		
	}
	
	private void playSounds() {
		AudioManager mgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		float volume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		
		for (int sound : mSoundFiles) {
			mSoundPool.play(mSoundPoolMap.get(sound), volume, volume, 1, 0, 1f);
		}
	}
	
	public void destroy() {
		mSoundPool.release();
		mSoundPool = null;
	}
}
