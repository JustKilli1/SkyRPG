package net.marscraft.skyrpg.shared.logmanager;


import net.marscraft.skyrpg.base.Main;

import java.util.logging.Logger;

public class LogManager implements ILogManager{
    private Main _plugin;
    private String _modulName;
    private Logger _logger;

    public LogManager(Main plugin, String modulName){
        _plugin = plugin;
        _modulName = modulName;
        _logger = _plugin.getLogger();
    }

    @Override
    public void debug(String msg) {
        _logger.finest(formatMessage("DEBUG", msg));
    }

    @Override
    public void info(String msg) {
        _logger.info(formatMessage("INFO", msg));
    }

    @Override
    public void warn(String msg) {
        _logger.warning(formatMessage("WARNING", msg));
    }

    @Override
    public void error(String msg) {
        _logger.warning(formatMessage("ERROR", msg));
    }

    @Override
    public void error(String msg, Exception e) {
        _logger.warning(formatMessage("ERROR", msg));
        e.printStackTrace();
    }

    @Override
    public void error(Exception e) {
        _logger.warning(formatMessage("ERROR"));
        e.printStackTrace();
    }

    private String formatMessage(String level){
        return formatMessage(level, "No Content");
    }

    private String formatMessage(String level, String msg){
        String message = "Level: " + level;
        message += " <|> Module: " + _modulName;
        message += " <|> Message: " + msg;
        return message;
    }
}
