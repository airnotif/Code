import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import org.w3c.dom.Document;
import java.io.IOException;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.util.*;

public class xmlDecoding
{

    private String dateStr;
    private String stationStr;
    private String programStr;
    private String systemStatusStr;
    private String processStatusStr;
    private String nogoStr;
    private String sessionStr;
    Data data; 
    public xmlDecoding(File fichier)
    {

        try
        {

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();       
            final Document document= builder.parse(fichier);

            final Element racine = document.getDocumentElement();
            //System.out.println(racine.getNodeName());


            final NodeList ref = racine.getElementsByTagName("ref");            
            final Element n1 = (Element) ref.item(0);
            //System.out.println(bla.getNodeName());

            final NodeList date = n1.getElementsByTagName("date");
            final Element dateElem = (Element) date.item(0);
            //System.out.println(dateElem.getNodeName() + " : " + dateElem.getTextContent());
            dateStr=dateElem.getTextContent();
            
            final NodeList station = n1.getElementsByTagName("station");
            final Element stationElem = (Element) station.item(0);
            //System.out.println(stationElem.getNodeName() + " : " + stationElem.getTextContent());
            stationStr=stationElem.getTextContent();
            
            final NodeList program = n1.getElementsByTagName("program");
            final Element programElem = (Element) program.item(0);
            //System.out.println(programElem.getNodeName() + " : " + programElem.getTextContent());
            programStr=programElem.getTextContent();
            
            final NodeList system = racine.getElementsByTagName("system");          
            final Element n2 = (Element) system.item(0);

            final NodeList systemStatus = n2.getElementsByTagName("status");
            final Element systemStatusElem = (Element) systemStatus.item(0);
            //System.out.println(systemStatusElem.getNodeName() + " : " + systemStatusElem.getTextContent());
            systemStatusStr=systemStatusElem.getTextContent();

            final NodeList process = racine.getElementsByTagName("process");
            final Element n3 = (Element) process.item(0);

            final NodeList processStatus = n3.getElementsByTagName("status");
            final Element processStatusElem = (Element) processStatus.item(0);
            //System.out.println(processStatusElem.getNodeName() + " : " + processStatusElem.getTextContent());
            processStatusStr=processStatusElem.getTextContent();
            
            final NodeList nogo = n3.getElementsByTagName("nogo");
            final Element nogoElem = (Element) nogo.item(0);
            //System.out.println(nogoElem.getNodeName() + " : " + nogoElem.getTextContent());
            nogoStr=nogoElem.getTextContent();
            
            final NodeList function = racine.getElementsByTagName("function");
            final Element n4 = (Element) function.item(0);

            final NodeList session = n4.getElementsByTagName("session");
            final Element sessionElem = (Element) session.item(0);
            //System.out.println(sessionElem.getNodeName() + " : " + sessionElem.getTextContent());   
            sessionStr=sessionElem.getTextContent();
            
            data = new Data(dateStr,stationStr,programStr,systemStatusStr,processStatusStr,nogoStr,sessionStr);                          
        }
        catch (final ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (final SAXException e)
        {
            e.printStackTrace();
        }
        catch (final IOException e) 
        {
            e.printStackTrace();
        }   
    }
    public Data getData()
    {
        return this.data;
    }
    public static void main(String[] args)
    {

    }

}