package org.hjin.upoa.ui.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.hjin.upoa.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class DateTimePickerDialogFragment extends DialogFragment {

	private final String TAG = "DateTimePickerDialogFragment";

	private Calendar mDefaultTime;

	private Calendar mMaxTime;

	private Calendar mMinTime;

	private TextView mTv;

	private String mTitle;

	private Calendar mCalendar = Calendar.getInstance();

	private DatePicker mDatePicker;

	private TimePicker mTimePicker;

	private DateTimePickerDialogFragment mDialog;

	/**
	 * Create a new instance of ValidateDialogFragment, providing
	 * "validateCodePic" as an argument.
	 */
	public static DateTimePickerDialogFragment newInstance(String title,
			Calendar defaultTime, Calendar maxTime, Calendar minTime, TextView v) {
		DateTimePickerDialogFragment f = new DateTimePickerDialogFragment();
		f.setStyle(STYLE_NO_TITLE, 0);
		Bundle args = new Bundle();
		args.putString("title", title);
		if (null == defaultTime) {
			defaultTime = Calendar.getInstance();
		}

		f.setArguments(args);
		f.mDialog = f;
		f.mTv = v;
		f.mDefaultTime = defaultTime;
		f.mMaxTime = maxTime;
		f.mMinTime = minTime;
		f.mCalendar = Calendar.getInstance();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_datetimepicker_dialog,
				container, false);
		mDatePicker = (DatePicker) v.findViewById(R.id.datepicker);
		mTimePicker = (TimePicker) v.findViewById(R.id.timepicker);
		mDatePicker.setMaxDate(mMaxTime.getTimeInMillis());
		mDatePicker.setMinDate(mMinTime.getTimeInMillis());

		mDatePicker.init(mDefaultTime.get(Calendar.YEAR),
				mDefaultTime.get(Calendar.MONTH),
				mDefaultTime.get(Calendar.DATE), new OnDateChangedListener() {
					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						mCalendar.set(year, monthOfYear, dayOfMonth);
					}
				});

		mTimePicker.setIs24HourView(true);
		mTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				mCalendar.set(Calendar.MINUTE, minute);
			}
		});
		mTimePicker.setCurrentHour(mDefaultTime.get(Calendar.HOUR_OF_DAY));
		mTimePicker.setCurrentMinute(mDefaultTime.get(Calendar.MINUTE));
		mCalendar = mDefaultTime;

		TextView mTitleView = (TextView) v.findViewById(R.id.datetimepicker_title);
		mTitle = getArguments().getString("title");
		mTitleView.setText(mTitle);
		Button backBtn = (Button) v.findViewById(R.id.datetimepicker_back);
		Button submitBtn = (Button) v.findViewById(R.id.datetimepicker_submit);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Date d = mCalendar.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.SIMPLIFIED_CHINESE);
				mTv.setText(sdf.format(d));
				mDialog.dismiss();
			}
		});

		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Window window = getDialog().getWindow();
		window.setBackgroundDrawable(null);
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	public void doBack(View v) {

	}

	public void doClear(View v) {

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

}
