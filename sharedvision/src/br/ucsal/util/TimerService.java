package br.ucsal.util;

import org.apache.log4j.Logger;

public class TimerService {

	public static void sleep(long sleeptime) {
		try {
			Thread.sleep(sleeptime);
		} catch (InterruptedException e) {
			logger.error("Sleep error:"+e.getMessage());
		}
	}

	final static Logger logger = Logger.getLogger(TimerService.class);

}
