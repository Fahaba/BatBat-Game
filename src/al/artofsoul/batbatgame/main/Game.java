package al.artofsoul.batbatgame.main;

import al.artofsoul.batbatgame.handlers.MyLogger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;

public class Game {

    public static MyLogger logger = new MyLogger();

	public static void main(String[] args) {

        String log4jConfPath = "log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);

        JFrame window = new JFrame("batbat Game");
		window.add(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}