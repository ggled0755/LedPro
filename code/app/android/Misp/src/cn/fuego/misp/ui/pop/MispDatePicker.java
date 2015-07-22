package cn.fuego.misp.ui.pop;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class MispDatePicker extends DialogFragment implements
		DatePickerDialog.OnDateSetListener, OnTimeSetListener
{
	private MispPopWindowListener listener;
	private Date date;
	private int dtType;
	//用来解决重复执行ontimeset bug 4.2/4.1
	private boolean flag= true;
	public MispDatePicker(MispPopWindowListener listener,Date date,int type)
	{
		this.listener = listener;
		this.date = date;
		this.dtType=type;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		final Calendar c = Calendar.getInstance();
		if(dtType==0)
		{
			if(null != date)
			{
				c.setTime(date);
			}
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog datPicker = new DatePickerDialog(getActivity(), this, year, month, day);

			return datPicker;
		}
		else
		{
			
			if(null!=date)
			{
				c.setTime(date);
			}
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			
			TimePickerDialog timePicker = new TimePickerDialog(getActivity(), this, hour, minute, true);

			return timePicker;
		}
		
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day)
	{
		if(flag)
		{
			Log.d("OnDateSet", "select year:" + year + ";month:" + month + ";day:"
					+ day);
			int temp = month+1;
			String dataString;
			if(temp<10)
			{
				dataString = year+"-0"+temp + "-" + day;
			}
			else
			{
				dataString =  year+"-"+temp + "-" + day;
			}
			listener.onConfirmClick(dataString);
			flag=false;
		}
		{
			flag=true;
		}

	}
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute)
	{
		if(flag)
		{
			flag=false;
			Log.d("onTimeSet", "select hourOfDay:" + hourOfDay + ";minute:" + minute);
			String timeString =	null;
			if(minute<10)
			{
				timeString = hourOfDay+":0"+minute;
			}
			else
			{
				timeString= hourOfDay+":"+minute;
			}
			
			listener.onConfirmClick(timeString);
		}
		else
		{
			flag=true;
		}

		
	}
	
	
}
