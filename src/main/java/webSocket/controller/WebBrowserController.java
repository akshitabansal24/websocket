package webSocket.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
public class WebBrowserController {

    @GetMapping("/start")
    public void startBrowser(@RequestParam("browser") String browser,
                             @RequestParam("url") String url) throws IOException {
    	String app = "";
    	switch(browser) {
	    	case "chrome":
	    		app = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
	    		break;
	    	case "notepad":
	    		app = "C:\\Program Files (x86)\\Notepad++\\notepad++.exe";
	    		break;
	    	default:
	    		app="";
	    		break;
    	}
    			
    			
        ProcessBuilder processBuilder = new ProcessBuilder(app,url);

        System.out.println("Opening browser " + browser + " with " + url);

        processBuilder.start();
    }

    @GetMapping("/stop")
    public void stopBrowser(@RequestParam("browser") String browser) throws IOException {
        // Command to find the process ID of the browser on Windows
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "tasklist | findstr /i " + browser);

        Process process = processBuilder.start();

        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        
        String line;
        String processId = null;
        
        while ((line = bufferedReader.readLine()) != null) {
            // Parse the output to extract the process ID
            String[] parts = line.trim().split("\\s+");
            if (parts.length > 1) {
                processId = parts[1]; // The second column typically contains the PID
                break;
            }
        }

        bufferedReader.close();

        if (processId != null) {
            System.out.println("Stopping browser " + browser + " with PID " + processId);
            
            // Command to kill the process
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "taskkill /PID " + processId + " /F");
            pb.start();
        } else {
            System.out.println("No process found for browser: " + browser);
        }
    }


    @GetMapping("/getUrl")
    public String getActiveTabUrl(@RequestParam("browser") String browser) throws IOException {
        ProcessBuilder processBuilder =
                new ProcessBuilder("osascript", "-e", "tell application \"" + browser + "\" to get url of active tab of the front window");

        Process process = processBuilder.start();

        InputStream inputStream = process.getInputStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String activeTabUrl = bufferedReader.readLine();

        bufferedReader.close();

        System.out.println("Active browser " + activeTabUrl);

        return activeTabUrl;
    }
}
/*
* Commands for quick testing
* */

// http://localhost:8080/start?browser=Firefox&url=https://www.facebook.com/
// http://localhost:8080/start?browser=Safari&url=https://www.facebook.com/
// http://localhost:8080/start?browser=Google%20Chrome&url=https://www.facebook.com/

//http://localhost:8080/stop?browser=Firefox
//http://localhost:8080/stop?browser=Google%20Chrome
//http://localhost:8080/getUrl?browser=Google%20Chrome
//  osascript -e "tell application \"Google Chrome\" to get url of active tab of the front window"
//  osascript -e "tell application \"Mozilla Firefox\" to get url of active tab of the front window"
