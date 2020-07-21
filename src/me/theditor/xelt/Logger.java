package me.theditor.xelt;

public class Logger {
	
	public Logger() {
		this.info("Logger Started");
	}
	
	public void info(String log) {
		this.log("INFO", log);
	}
	
	public void warn(String log) {
		this.log("WARN", log);
	}
	
	public void fatal(String log) {
		this.log("FATAL", log);
	}
	
	private void log(String type, String log) {
		System.out.println("[" + type + "]" + "[" + System.currentTimeMillis() + "] " + log);
	}

}
