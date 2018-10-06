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
		} catch (ConfigUncorrectException | ValidatePasswordException | InterruptedException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		String password = cracker.getGoodPassword();
		if (password != null && !password.equals("")) {
			System.out.println("Password is " + password);
		} else {
			System.out.println("Failed");
		}
	}

}
