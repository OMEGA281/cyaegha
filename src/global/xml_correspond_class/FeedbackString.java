package global.xml_correspond_class;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class FeedbackString extends XMLCorrespondCLass
{
	@XmlRootElement(name="statement")
	class Statement
	{
		int ID;
		String name;
		String value;
		String help;
		@XmlAttribute
		public int getID() {
			return ID;
		}
		public void setID(int iD) {
			ID = iD;
		}
		@XmlElement
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@XmlElement
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		@XmlElement
		public String getHelp() {
			return help;
		}
		public void setHelp(String help) {
			this.help = help;
		}
	}
	@XmlRootElement(name="FeedbackString")
	class feedbackString
	{
		private Set<Statement> set=new HashSet<>();
		@XmlElement
		public void setSet(Set<Statement> set) {
			this.set = set;
		}
		public Set<Statement> getSet() {
			return set;
		}
	}
}
