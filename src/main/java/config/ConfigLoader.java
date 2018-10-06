package config;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import exceptions.ConfigUncorrectException;

public class ConfigLoader {
	
	private static final Logger logger = Logger.getLogger(ConfigLoader.class);
	
	@SuppressWarnings("rawtypes")
	public void load(ApplicationConfig config) throws DocumentException, ConfigUncorrectException {
		SAXReader reader = new SAXReader();
		logger.info("读取配置文件configuration.xml…");
		Document doc = reader.read(ConfigLoader.class.getClassLoader().getResourceAsStream("configuration.xml"));
		Element root = doc.getRootElement();
		
		for (Iterator it = root.elementIterator(); it.hasNext(); ) {
			Element element = (Element)it.next();
			String elementName = element.getName();
			String elementText = element.getTextTrim();
			
			if (elementText.equals("")) {
				ConfigUncorrectException ce = new ConfigUncorrectException("验证配置文件失败：配置文件中设置不能为空");
				logger.error(ce.getMessage(), ce);
				throw ce;
			}
			
			switch (elementName) {
				case "engine-path":
					config.setEnginePath(elementText);
					logger.info("配置文件内容-压缩引擎路径 engine-path:  " + elementText);
					break;
				case "file-path":
					config.setFilePath(elementText);
					logger.info("配置文件内容-目标文件路径 file-path:  " + elementText);
					break;
				case "min-length":
					config.setMinLength(Integer.parseInt(elementText));
					logger.info("配置文件内容-设置密码最小长度 min-length:  " + elementText);
					break;
				case "max-length":
					config.setMaxLength(Integer.parseInt(elementText));
					logger.info("配置文件内容-设置密码最大长度 max-length:  " + elementText);
					break;
				case "thread-num":
					config.setThreadNum(Integer.parseInt(elementText));
					logger.info("配置文件内容-设置破解线程数 thread-num:  " + elementText);
					break;
				case "show-detail":
					config.setShowDetail(elementText.equals("Yes") ? true : false);
					logger.info("配置文件内容-设置显示测试密码 show-detail:  " + elementText);
					break;
				default:
					config.setCharset(elementText);
					logger.info("配置文件内容-设置字符集 password-charset:	  " + elementText);
					break;
			}			
		}
	}
	
}
