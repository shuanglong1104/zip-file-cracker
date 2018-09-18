package config;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sun.istack.internal.logging.Logger;

public class ConfigLoader {
	
	private static final Logger logger = Logger.getLogger(ConfigLoader.class);
	
	@SuppressWarnings("rawtypes")
	public void load(ApplicationConfig config) throws DocumentException {
		SAXReader reader = new SAXReader();
		logger.info("读取配置文件configuration.xml…");
		Document doc = reader.read(ConfigLoader.class.getClassLoader().getResourceAsStream("configuration.xml"));
		Element root = doc.getRootElement();
		
		for (Iterator it = root.elementIterator(); it.hasNext(); ) {
			Element element = (Element)it.next();
			String elementName = element.getName();
			String elementText = element.getTextTrim();
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
				default:
					config.setCharset(elementText);
					logger.info("配置文件内容-设置字符集 password-charset:	  " + elementText);
					break;
			}			
		}
	}
	
}
