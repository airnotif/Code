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



public class xmlDecoding
{

	private String dateStr;
	private String stationStr;
	private String programStr;
	private String systemStatusStr;
	private String processStatusStr;
	private String nogoStr;
	private String sessionStr;

	public xmlDecoding(String fileName)
	{

		try
		{
			
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    final DocumentBuilder builder = factory.newDocumentBuilder();		
		    final Document document= builder.parse(new File(fileName));

			final Element racine = document.getDocumentElement();
			System.out.println(racine.getNodeName());
			


			final NodeList ref = racine.getElementsByTagName("ref");
			final int nbNoeuds = ref.getLength();
			System.out.println(nbNoeuds);

			for (int j = 0; j<nbNoeuds; j++)
			{	
				final Element bla = (Element) ref.item(j);
				System.out.println(bla.getNodeName());

				final NodeList date = bla.getElementsByTagName("date");
				final Element hum = (Element) date.item(0);
				System.out.println(hum.getNodeName() + " : " + hum.getTextContent());


				final NodeList station = bla.getElementsByTagName("station");
				final Element hum2 = (Element) station.item(0);
				System.out.println(hum2.getNodeName() + " : " + hum2.getTextContent());

				/*switch(j)
				{
					case 0:
						Element date = (Element)ref.item(j);
						dateStr = date.getTextContent();
						System.out.println(dateStr.length());
						break;
					case 1:
						Element station = (Element)ref.item(j);
						stationStr = station.getTextContent();
						System.out.println(stationStr.length());
						break;
					case 2:
						Element program = (Element)ref.item(j);
						programStr = program.getTextContent();
						System.out.println(programStr.length());
						break;
				}*/
			}
			
			final NodeList system = racine.getElementsByTagName("system");
			final int nbNoeuds2 = system.getLength();

			for (int i = 0; i<nbNoeuds2; i++)
			{	
				/*switch(i)
				{
					case 0:
						Element systemStatus = (Element)system.item(i);
						systemStatusStr = systemStatus.getTextContent();
						break;
				}*/
			}

			final NodeList process = racine.getElementsByTagName("process");	
			final int nbNoeuds3 = process.getLength();

			for (int l = 0; l<nbNoeuds3; l++)
			{	
				/*switch(l)
				{
					case 0:
						Element processStatus = (Element)process.item(l);
						processStatusStr = processStatus.getTextContent();
						break;
					case 1:
						Element nogo = (Element)process.item(l);
						nogoStr = nogo.getTextContent();
						break;
				}*/
			}

			final NodeList function = racine.getElementsByTagName("function");
			final int nbNoeuds4 = function.getLength();

			for (int k = 0; k<nbNoeuds4; k++)
			{	
				/*switch(k)
				{
					case 0:
						Element session = (Element)function.item(k);
						sessionStr = session.getTextContent();
						break;
				}*/
			}

			//Data data = new Data(dateStr,stationStr,programStr,systemStatusStr,processStatusStr,nogoStr,sessionStr);							
			//System.out.println(data);
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

	public static void main(String[] args)
	{

		xmlDecoding xml = new xmlDecoding("data.xml");

	}

}