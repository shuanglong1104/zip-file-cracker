package main;

import java.math.BigInteger;

public class StatusCache {
	/*
	 * 	存放下一个待验证的password
	 * 	由pwdLock锁住
	 */
	private String password;
	/*
	 * 	存放用于计算破解平均速度的计数器
	 * 	由finishedLock锁住
	 */
	private BigInteger counter;
	/*
	 * 	存放结束标识，表示是否找到正确密码
	 *      由finishedLock锁住
	 */
	private boolean finished;
	/*
	 * 	该类的两个私有锁
	 */
	private final Object finishedLock = new Object();
	private final Object pwdLock = new Object();
	/*
	 * 	存放设定字符集的首尾字符，用于更新密码
	 */
	private final char charSetFirstChar;
	private final char charSetLastChar;
	
	public StatusCache(String password, BigInteger counter, boolean finished,
			char charSetFirstChar, char charSetLastChar) {
		this.password = password;
		this.counter = counter;
		this.finished = finished;
		this.charSetFirstChar = charSetFirstChar;
		this.charSetLastChar = charSetLastChar;
	}
		
	public String getPassword() {
		return new String(password);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public BigInteger getCounter() {
		return new BigInteger(counter.toByteArray());
	}

	public void setCounter(BigInteger counter) {
		this.counter = counter;
	}

	public boolean getFinished() {
		return new Boolean(finished);
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Object getFinishedLock() {
		return finishedLock;
	}

	public Object getPwdLock() {
		return pwdLock;
	}
	
	public char getCharSetFirstChar() {
		return charSetFirstChar;
	}

	public char getCharSetLastChar() {
		return charSetLastChar;
	}
}
