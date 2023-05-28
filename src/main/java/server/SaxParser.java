package server;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

public class SaxParser extends DefaultHandler{

    private String thisElement = "";
    User user= new User();
   private String transferType;


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        thisElement=qName;
    }
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (thisElement.equalsIgnoreCase("name")){
            user.setName(new String(ch, start, length));
        }
       else if(thisElement.equalsIgnoreCase("password")){
           user.setPassword(new String(ch, start, length));
        }
        else if(thisElement.equalsIgnoreCase("type")){
            transferType=new String(ch, start, length);
        }
    }
    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        thisElement="";
    }

    public User getUser(){
        return user;
    }

    public String getType(){
        return transferType;
    }


}
