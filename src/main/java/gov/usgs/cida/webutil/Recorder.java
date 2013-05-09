package gov.usgs.cida.webutil;

import gov.usgs.cida.webutil.Settings.FailMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class Recorder {
	
	public static class Hit {

		private String remoteAddr;
		private FailMode mode;
		private int responseCode;
		private Date when;
		
		public Hit(String remoteAddr, FailMode mode, int code) {
			this.when = new Date();
			this.mode = mode;
			this.remoteAddr = remoteAddr;
			this.responseCode = code;
		}

		public String getRemoteAddr() {
			return remoteAddr;
		}

		public FailMode getMode() {
			return mode;
		}

		public int getResponseCode() {
			return responseCode;
		}

		public Date getWhen() {
			return when;
		}
		
		
	}
	
	private static int capacity = 100;
	
	private Deque<Hit> history = new LinkedList<Hit>();
	
	public synchronized void record(HttpServletRequest request, Settings.FailMode mode, int code) {
		Hit hit = new Hit(request.getRemoteAddr(), mode, code);
		
		history.addLast(hit);
		if (history.size() > capacity) {
			history.removeFirst();
		}
	}
	
	public List<Hit> getHistory() {
		return new ArrayList<Hit>(history);
	}
}
