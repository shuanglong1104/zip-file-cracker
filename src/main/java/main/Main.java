package main;

import org.dom4j.DocumentException;

import config.ApplicationConfig;
import config.ConfigLoader;
import exceptions.ConfigUncorrectException;
import exceptions.ValidatePasswordException;

public class Main {

	public static void main(String[] args) {	
		ApplicationConfig config = ApplicationConfig.getInstance();
		ConfigLoader loader = new ConfigLoader();
		PasswordCracker cracker = new PasswordCracker();
		
		try {
			loader.load(config);
			cracker.setConfig(config);
			cracker.crack();
		} catch (DocumentException de) {
			System.out.println(de.getMessage());
			return;
		} catch (ConfigUncorrectException | ValidatePasswordException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		if (cracker.getHasFound()) {
			System.out.println("password is " + cracker.getPassword());
		} else {
			System.out.println("failed");
		}
	}

}
