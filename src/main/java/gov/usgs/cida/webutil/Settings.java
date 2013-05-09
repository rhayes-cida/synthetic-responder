package gov.usgs.cida.webutil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Settings {
	
	private int responseCode;
	
	public int computeResponseCode(HttpServletRequest request) {
		return responseCode;
	}
	
	public int getResponseCode() {
		return responseCode;
	}
	
	public void setResponseCode(int c) {
		responseCode = c;
	}
	
	public enum FailMode {
		SUCCEED,
		HTTP_CODE,
		NO_ANSWER,
		DROP_CONNECTION
	};
	
	public FailMode failMode = FailMode.SUCCEED;

	public FailMode getFailMode() {
		return failMode;
	}

	public FailMode[] getFailModeOptions() {
		return FailMode.values();
	}
	
	public void setFailMode(FailMode failMode) {
		this.failMode = failMode;
		if (failMode == FailMode.SUCCEED) {
			responseCode = HttpServletResponse.SC_OK;
		}
	}
	
}
