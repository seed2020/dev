package com.innobiz.orange.web.mail.jpa;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class TimezoneUtil {
	
	private static Map<String, TimeZone> timezoneMap = null;
	
	public static TimeZone getTimezone(String timeGap){
		if(timezoneMap==null) setTimezoneMap();
		return timezoneMap.get(timeGap);
	}
	
	private static void setTimezoneMap(){
		String[][] arr = {
				{"-1200","Etc/GMT+12"},
				{"-1100","Pacific/Midway"},
				{"-1000","America/Adak"},
				{"-0930","Pacific/Marquesas"},
				{"-0900","America/Anchorage"},
				{"-0800","America/Dawson"},
				{"-0700","America/Boise"},
				{"-0600","America/Belize"},
				{"-0500","America/Atikokan"},
				{"-0430","America/Caracas"},
				{"-0400","America/Anguilla"},
				{"-0330","Canada/Newfoundland"},
				{"-0300","America/Araguaina"},
				{"-0200","America/Noronha"},
				{"-0100","America/Scoresbysund"},
				{"+0000","Etc/GMT0"},
				{"+0100","Africa/Algiers"},
				{"+0200","Africa/Blantyre"},
				{"+0300","Africa/Asmara"},
				{"+0307","Asia/Riyadh87"},
				{"+0330","Iran"},
				{"+0400","Asia/Baku"},
				{"+0430","Asia/Kabul"},
				{"+0500","Antarctica/Mawson"},
				{"+0530","Asia/Calcutta"},
				{"+0545","Asia/Kathmandu"},
				{"+0600","Asia/Almaty"},
				{"+0630","Asia/Rangoon"},
				{"+0700","Asia/Bangkok"},
				{"+0800","Asia/Brunei"},
				{"+0845","Australia/Eucla"},
				{"+0900","Asia/Seoul"},
				{"+0930","Australia/South"},
				{"+1000","Asia/Khandyga"},
				{"+1030","Australia/LHI"},
				{"+1100","Asia/Sakhalin"},
				{"+1130","Pacific/Norfolk"},
				{"+1200","Asia/Anadyr"},
				{"+1245","Pacific/Chatham"},
				{"+1300","MIT"},
				{"+1400","Pacific/Kiritimati"},	
		};
		if(timezoneMap==null) timezoneMap = new HashMap<String, TimeZone>();
		for(String[] zarr : arr){
			timezoneMap.put(zarr[0], TimeZone.getTimeZone(zarr[1]));
		}
	}
}
