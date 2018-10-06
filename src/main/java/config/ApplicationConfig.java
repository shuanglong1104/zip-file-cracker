package config;

public class ApplicationConfig {
	
	/*
	 * 	解压引擎路径
	 */
	private String enginePath;
	/*
	 * 	目标文件路径
	 */
	private String filePath;
	/*
	 * 	猜测密码最小和最大长度
	 */
	private int minLength;
	private int maxLength;
	/*
	 * 	密码字符集
	 */
	private String charset;
	/*
	 * 	破解线程数
	 */
	private int threadNum;
	/*
	 * 	显示测试密码信息
	 */
	private boolean showDetail;
	
	/*
	 * 	singleton
	 */
	private static ApplicationConfig config;
	
	private ApplicationConfig() {
		
	}
	
	public static ApplicationConfig getInstance() {
		if (config == null) {
			config = new ApplicationConfig();
		}
		return config;
	}
	
	public String getEnginePath() {
		return enginePath;
	}
	public void setEnginePath(String enginePath) {
		this.enginePath = enginePath;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getMinLength() {
		return minLength;
	}
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public int getThreadNum() {
		return threadNum;
	}
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public boolean getShowDetail() {
		return showDetail;
	}

	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}	
}
