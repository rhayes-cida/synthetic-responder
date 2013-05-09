package gov.usgs.cida.webutil;

import gov.usgs.cida.webutil.Recorder.Hit;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Failer {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(Failer.class);
	
	@Autowired
	private Recorder recorder;
	
	@Autowired
	private Settings settings;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Failer() {
        super();
    }

    private void sendResponse(HttpServletResponse resp, int code, String text) throws IOException {
    	logger.info("Sending response, code {} text {}", code, text);
    	
    	resp.setContentType("text/plain");
    	resp.setStatus(code);  // clears response content buffer
    	Writer w = resp.getWriter();
    	try {
    		w.write(text);
    	} finally {
    		w.close();
    	}
    }
    
    // Every request needs the settings
    @ModelAttribute("settings")
    public Settings getSettings() {
        return settings;
    }
    
    @RequestMapping("/response")
	public void reponse(
			HttpServletRequest request, 
			HttpServletResponse response
	) throws ServletException, IOException {
		int code = settings.computeResponseCode(request);

		logger.info("response for {} with settings {}", request.getRemoteAddr(),settings);
		
		recorder.record(request, settings.getFailMode(), code);

		switch (settings.getFailMode()) {
			case SUCCEED:
				sendResponse(response, HttpServletResponse.SC_OK, "OK");
				break;
				
			case HTTP_CODE:
				sendResponse(response, code, "Fail");
				break;
				
			case NO_ANSWER:
				response.sendRedirect("http://no.such.server:999/index.html");
				break;
				
			case DROP_CONNECTION:

				response.setContentLength(31459);
				OutputStream os = response.getOutputStream();
				os.write("just a roll, just a roll".getBytes());
				// brutal termination before expected end
				os.close();
		}
	}

    @RequestMapping(value="/",method=RequestMethod.GET)
    public String settingsUI(Model model) {
    	List<Hit> history = recorder.getHistory();
		model.addAttribute("history", history);
    	logger.debug("published history length {}", history);
    	
    	return "index";
    }
    
    @RequestMapping(value="/",method=RequestMethod.POST)
    public String set(
    		@ModelAttribute("settings") Settings s,
    		Model model
    		) {
    	settings.setResponseCode(s.getResponseCode());
    	settings.setFailMode(s.getFailMode());  // this must come after response code setting
    	
    	logger.info("Set mode={} code={}", settings.getFailMode(), settings.getResponseCode());
    	
    	model.addAttribute("history", recorder.getHistory());
    	return "index";
    }
    
}
