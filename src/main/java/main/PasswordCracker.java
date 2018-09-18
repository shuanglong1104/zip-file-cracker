package main;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import config.ApplicationConfig;
import exceptions.ConfigUncorrectException;
import exceptions.ValidatePasswordException;

public class PasswordCracker {
	
	private static final Logger logger = Logger.getLogger(PasswordCracker.class);
	private ApplicationConfig config;
	private String password = null;
	private boolean hasFound = false;

	public ApplicationConfig getConfig() {
		return config;
	}

	public void setConfig(ApplicationConfig config) {
		this.config = config;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getHasFound() {
		return hasFound;
	}

	public void setHasFound(boolean hasFound) {
		this.hasFound = hasFound;
	}

	private void validateConfig() throws ConfigUncorrectException {
		logger.info("开始验证配置文件…");
		
		if (new File(config.getFilePath()).exists() == false) {
			ConfigUncorrectException ce = new ConfigUncorrectException("验证配置文件失败：目标文件路径不存在");
			logger.error(ce.getMessage(), ce);
			throw ce;
		}

		if (config.getMinLength() >= config.getMaxLength()) {
			ConfigUncorrectException ce = new ConfigUncorrectException("验证配置文件失败：设置密码长度范围错误");
			logger.error(ce.getMessage(), ce);
			throw ce;
		}
	}

	private boolean validatePassword(String password) throws IOException, InterruptedException {
		String cmd = config.getEnginePath() + " t -inul -ibck -p" + password + " " + config.getFilePath();
		Process pr = Runtime.getRuntime().exec(cmd);
		if (pr.waitFor() == 0) {
			return true;
		}
		return false;
	}

	public void crack() throws ConfigUncorrectException, ValidatePasswordException {
		validateConfig();
		
		logger.info("开始破解密码…");
		
		for (int length = config.getMinLength(); length <= config.getMaxLength(); length++) {
			try {
				generatePassword("", length);
			} catch (IOException | InterruptedException e) {
				ValidatePasswordException ve = new ValidatePasswordException("验证密码出现错误：压缩引擎运行失败");
				logger.error(ve.getMessage(), ve);
				throw ve;
			}
			
			if (this.hasFound) {
				return;
			}
		}
		
		if(this.hasFound == false && this.password == null) {
			logger.warn("破解密码失败，请尝试扩展密码字符集或调整密码长度范围");
		}
	}

	private void generatePassword(String str, int length) throws IOException, InterruptedException {
		if (str.length() == length) {
			if (validatePassword(str)) {
				this.hasFound = true;
				this.password = str;
				logger.info("破解密码成功，密码为" + str);
			}
			return;
		}
		
		for (int i = 0; i < config.getCharset().length(); i++) {
			generatePassword(str + config.getCharset().charAt(i), length);
			if (this.hasFound) {
				return;
			}
		}
	}

}
