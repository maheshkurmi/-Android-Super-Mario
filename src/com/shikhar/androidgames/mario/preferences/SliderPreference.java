
package com.shikhar.androidgames.mario.preferences;

import com.shikhar.androidgames.mario.core.R;
import com.shikhar.androidgames.mario.core.R.id;
import com.shikhar.androidgames.mario.core.R.layout;
import com.shikhar.androidgames.mario.core.R.styleable;
import com.shikhar.androidgames.mario.util.Utils;

import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;

public class SliderPreference extends Preference implements OnSeekBarChangeListener {
	private final static int MAX_SLIDER_VALUE = 100;
	private final static int INITIAL_VALUE = 50;
	
	private int mValue = INITIAL_VALUE;
	private String mMinText;
	private String mMaxText;
	
	
	public SliderPreference(Context context) {
		super(context);
		
		setWidgetLayoutResource(R.layout.slider_preference);
	}
	
	public SliderPreference(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.preferenceStyle);
		
		setWidgetLayoutResource(R.layout.slider_preference);
	}
	
	public SliderPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SliderPreference, defStyle, 0);
		mMinText = a.getString(R.styleable.SliderPreference_minText);
		mMaxText = a.getString(R.styleable.SliderPreference_maxText);
		
        a.recycle();
        
        setWidgetLayoutResource(R.layout.slider_preference);
	}
	
	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
	
		if (mMinText != null) {
			TextView minText = (TextView)view.findViewById(R.id.min);
			minText.setText(mMinText);
		}
		
		if (mMaxText != null) {
			TextView maxText = (TextView)view.findViewById(R.id.max);
			maxText.setText(mMaxText);
		}
		
		SeekBar bar = (SeekBar)view.findViewById(R.id.slider);
		bar.setMax(MAX_SLIDER_VALUE);
		bar.setProgress(mValue);
		bar.setOnSeekBarChangeListener(this);
	}
	
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser) {
			mValue = progress;
			persistInt(mValue);
		}
	}
	
	public void onStartTrackingTouch(SeekBar seekBar) {
	}
	
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
	
	
	@Override 
	protected Object onGetDefaultValue(TypedArray ta,int index) {
		int dValue = (int)ta.getInt(index, INITIAL_VALUE);
		
		return (int)Utils.clamp(dValue, 0, MAX_SLIDER_VALUE);
	}
	
	
	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		mValue = defaultValue != null ? (Integer)defaultValue : INITIAL_VALUE;
		
		if (!restoreValue) {
			persistInt(mValue);
		} else {
			mValue = getPersistedInt(mValue);
		}
	}
	

}