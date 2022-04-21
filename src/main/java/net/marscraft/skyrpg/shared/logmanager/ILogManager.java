package net.marscraft.skyrpg.shared.logmanager;

public interface ILogManager {

    /**
     * The Log will be displayed with the Module name and the Message you added
     * @param msg The Message to define the Log Message
     */
    public void debug(String msg);
    /**
     * The Log will be displayed with the Module name and the Message you added
     * @param msg The Message to define the Log Message
     */
    public void info(String msg);
    /**
     * The Log will be displayed with the Module name and the Message you added
     * @param msg The Message to define the Log Message
     */
    public void warn(String msg);
    /**
     * The Log will be displayed with the Module name and the Message you added
     * @param msg The Message to define the Log Message
     */
    public void error(String msg);
    /**
     * The Log will be displayed with the Module name and the Message you added
     * @param msg The Message to define the Log Message
     * @param e The Exception witch is thrown
     */
    public void error(String msg, Exception e);
    /**
     * The Log will be displayed with the Module name
     * @param e The Exception witch is thrown
     */
    public void error(Exception e);

}
