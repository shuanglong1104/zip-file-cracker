package test;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Test;

public class Dom4jTest {

	@SuppressWarnings("rawtypes")
	public void readElements(Element root) {
		System.out.print(root.getName().trim() + ":" + root.getText().trim());

		for (Iterator it = root.attributeIterator(); it.hasNext(); ) {
			Attribute attribute = (Attribute)it.next();
			System.out.print(" " + attribute.getName().trim() + "=" + attribute.getValue().trim());
		}
		
		System.out.print("\n");

		for (Iterator it = root.elementIterator(); it.hasNext(); ) {
			Element element = (Element)it.next();
			readElements(element);
		}
	}

	@Test
	public void test() throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(Test.class.getClassLoader().getResourceAsStream("sample.xml"));
		Element root = doc.getRootElement();
		readElements(root);
		
		// xpath
		@SuppressWarnings("unchecked")
		List<Node> list = doc.selectNodes("//users/user");
		assertEquals(2, list.size());
	}

}
