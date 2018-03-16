package al.artofsoul.batbatgame.handlers;

import org.apache.log4j.Logger;

public class MyLogger {
    private final static Logger logger = Logger.getLogger(Logger.class.getName());

    public void log(String str){
        logger.info("INFO : " + str);
    }
}
