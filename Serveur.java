import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;

public class Serveur extends JFrame implements ActionListener
{
	private JPanel panel;
	private JButton sendRequest;
	private JLabel label;

	public Serveur()
	{
		super("Serveur");		
    	setSize(400, 200);
		this.setLayout(new FlowLayout());
		label = new JLabel("");
		add(label);
		sendRequest = new JButton("Send");
		sendRequest.addActionListener(this);
		add(sendRequest);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		
			if(e.getSource() == sendRequest)
			{
				
				try
				{
					Socket s = new Socket("10.117.93.212",7801);
					PrintWriter pw = new PrintWriter(s.getOutputStream());
					pw.write("Coucou");
					pw.flush();
					pw.close();
					s.close();
				}
				catch (UnknownHostException ea) {
         			ea.printStackTrace();
      			}
				catch(IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
			
	}

	public static void main(String[] args)
	{
		Serveur s = new Serveur();
	}


}