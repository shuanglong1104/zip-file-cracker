package main;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import org.apache.log4j.Logger;

import config.ApplicationConfig;
import exceptions.ConfigUncorrectException;
import exceptions.ValidatePasswordException;

public class PasswordCracker {

	private static final Logger logger = Logger.getLogger(PasswordCracker.class);
	private ApplicationConfig config;
	/*
	 * 正确密码
	 */
	private String goodPassword = null;
	/*
	 * 缓存
	 */
	private volatile StatusCache cache;

	public ApplicationConfig getConfig() {
		return config;
	}

	public void setConfig(ApplicationConfig config) {
		this.config = config;
	}

	public String getGoodPassword() {
		return goodPassword;
	}

	public void setGoodPassword(String goodPassword) {
		this.goodPassword = goodPassword;
	}

	/*
	 * 检查配置文件
	 */
	private void validateConfig() throws ConfigUncorrectException {
		logger.info("开始验证配置文件…");

		if (new File(config.getFilePath()).exists() == false) {
			ConfigUncorrectException ce = new ConfigUncorrectException("验证配置文件失败：找不到目标文件");
			logger.error(ce.getMessage(), ce);
			throw ce;
		}

		if (!new File(config.getFilePath()).isFile()) {
			ConfigUncorrectException ce = new ConfigUncorrectException("验证配置文件失败：请输入正确文件路径");
			logger.error(ce.getMessage(), ce);
			throw ce;
		}

		if (config.getMinLength() <= 0 || config.getMaxLength() <= 0) {
			ConfigUncorrectException ce = new ConfigUncorrectException("验证配置文件失败：设置密码长度应为正整数");
			logger.error(ce.getMessage(), ce);
			throw ce;
		}

		if (config.getMinLength() >= config.getMaxLength()) {
			ConfigUncorrectException ce = new ConfigUncorrectException("验证配置文件失败：设置密码长度错误");
			logger.error(ce.getMessage(), ce);
			throw ce;
		}

		if (config.getCharset() == null) {
			ConfigUncorrectException ce = new ConfigUncorrectException("验证配置文件失败：请设置密码字符集");
			logger.error(ce.getMessage(), ce);
			throw ce;
		}

		if (config.getThreadNum() <= 0) {
			config.setThreadNum(1);
		}
	}

	/*
	 * 破解密码
	 */
	public void crack() throws ConfigUncorrectException, InterruptedException {
		validateConfig();

		// 设置初始密码
		String passwordStart = "";
		for (int i = 0; i < config.getMinLength(); i++) {
			passwordStart += config.getCharset().charAt(0);
		}
		// 设置缓存
		char firstChar = config.getCharset().charAt(0);
		char lastChar = config.getCharset().charAt(config.getCharset().length() - 1);
		cache = new StatusCache(passwordStart, new BigInteger("0"), false, firstChar, lastChar);
		// 创建线程
		CrackThread[] crackThreads = new CrackThread[config.getThreadNum()];
		StatusThread statusThread = new StatusThread();

		logger.info("开始破解密码…");

		// 启动多个CrackThread和一个StatusThread
		for (int i = 0; i < config.getThreadNum(); i++) {
			crackThreads[i] = new CrackThread(i);
			crackThreads[i].start();
		}
		statusThread.start();

		// 加入线程
		try {
			for (int i = 0; i < config.getThreadNum(); i++) {
				crackThreads[i].join();
			}
			statusThread.join();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
			throw new InterruptedException("线程非法中断！");
		}

		// 检查是否成功破解
		if (goodPassword != null && !goodPassword.equals("")) {
			logger.info("破解密码成功，密码为" + goodPassword);
		} else {
			logger.warn("破解密码失败，请尝试扩展密码字符集或调整密码长度范围");
		}
	}

	/*
	 * crack线程访问互斥资源缓存，获取待验证的密码
	 */
	private String getNewPassword() {
		synchronized (cache.getPwdLock()) {
			String curPassword = cache.getPassword();
			generateNextPassword(curPassword.toCharArray(), curPassword.length() - 1);
			return curPassword;
		}
	}

	/*
	 * 缓存被一个crack线程访问后，更新下一个待验证的密码 由于计算过程是加锁的，优化计算过程提高计算速度相应提高破解程序性能
	 */
	private void generateNextPassword(char[] buf, int index) {
		char firstChar = cache.getCharSetFirstChar();
		char lastChar = cache.getCharSetLastChar();

		if (buf[index] == lastChar) {
			buf[index] = firstChar;
			if (index > 0) {
				generateNextPassword(buf, index - 1);
			} else {
				String tmp = "";
				for (int i = 0; i < buf.length; i++) {
					tmp += buf[i];
				}
				cache.setPassword(firstChar + tmp);
			}
		} else {
			int charIndex = config.getCharset().indexOf(buf[index]);
			buf[index] = config.getCharset().charAt(charIndex + 1);
			String tmp = "";
			for (int i = 0; i < buf.length; i++) {
				tmp += buf[i];
			}
			cache.setPassword(tmp);
		}
	}

	/*
	 * 调用winrar测试密码
	 */
	private boolean validatePassword(String password) throws IOException, InterruptedException {
		String cmd = config.getEnginePath() + " t -inul -ibck -p" + password + " " + config.getFilePath();
		Process pr = Runtime.getRuntime().exec(cmd);
		if (pr.waitFor() == 0) {
			return true;
		}
		return false;
	}

	/*
	 * crack线程
	 */
	private class CrackThread extends Thread {
		private int i;

		public CrackThread(int i) {
			this.i = i;
		}

		@Override
		public void run() {
			while (true) {
				String curPassword = getNewPassword();
				try {
					if (validatePassword(curPassword) == true) {
						goodPassword = curPassword;
						synchronized (cache.getFinishedLock()) {
							cache.setFinished(true);
							logger.info(i + "号CrackThread破解密码成功，密码为" + goodPassword);
							cache.setCounter(cache.getCounter().add(new BigInteger("1")));
						}
						break;
					} else {
						boolean flag = false;
						synchronized (cache.getFinishedLock()) {
							cache.setCounter(cache.getCounter().add(new BigInteger("1")));
							flag = cache.getFinished();
						}
						if (flag == true) {
							break;
						}
					}
					if (config.getShowDetail()) {
						System.out.println(i + " 正在验证 " + curPassword);
					}
				} catch (IOException | InterruptedException e) {
					ValidatePasswordException ve = new ValidatePasswordException("验证密码出现错误：压缩引擎运行失败");
					logger.error(ve.getMessage(), ve);
					throw ve;
				}
			}
		}
	}

	/*
	 * status线程
	 */
	private class StatusThread extends Thread {
		private int sleepTime = 50;
		private int pre_pwds = 0;
		private int cur_pwds = 0;

		@Override
		public void run() {
			while (!cache.getFinished()) {
				synchronized (cache.getFinishedLock()) {
					cur_pwds = cache.getCounter().intValue() / sleepTime;
					cache.setCounter(new BigInteger("0"));
				}
				if (pre_pwds == 0) {
					pre_pwds = cur_pwds;
				} else {
					pre_pwds = (pre_pwds + cur_pwds) / 2;
				}

				try {
					sleep(sleepTime * 1000);
				} catch (InterruptedException e) {
					logger.error("线程非法中断！计算平均速度可能出错", e);
				}
			}
			System.out.println("破解平均速度约为 " + pre_pwds + "/sec");
		}
	}
}
