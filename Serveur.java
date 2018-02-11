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
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;
public class Serveur extends JFrame implements ActionListener
{
    private JPanel panel;
    private JButton sendRequest;
    private JLabel label;
    private List<Data> DataToCompare = new ArrayList<Data>();
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
        this.main();
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

    public void main()
    {
        final File dossier = new File("DossierPartage");
        Connection con;
        boolean copieMachine;
        String query;
        Statement st;
        ResultSet rs;
        PreparedStatement pstmt;
        String sqlUpdate = "UPDATE machine "
            + "SET date_machine = ? "
            + ",programme_machine = ? "
            + ", status_system_machine = ? "
            + ",status_process_machine = ? "
            + ",nogo_machine = ? "
            + ",session_machine = ? "
            + "WHERE id_machine = ?";

        try 
        {
            Class.forName("com.mysql.jdbc.Driver");  
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/system?autoReconnect=true&useSSL=false","root","folio109");
            System.out.println("Database connected!");
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
        //while(true)
        {
            for (final File fileEntry : dossier.listFiles()) {
                xmlDecoding xml = new xmlDecoding(fileEntry);                
                DataToCompare.add(xml.getData());
            }
            query = "SELECT * FROM machine";
            try
            {
                for (int k=0;k<DataToCompare.size();k++)
                {
                    copieMachine=false;
                    st = con.createStatement();
                    pstmt = con.prepareStatement(sqlUpdate);
                    rs = st.executeQuery(query);
                    while (rs.next())
                    {
                        if(rs.getString("id_machine").equals(DataToCompare.get(k).getStation()))
                        {
                            System.out.println("machine trouvee");
                            System.out.println(k);
                            if(isMoreRecent(DataToCompare.get(k).getDate(),rs.getString("date_machine"))==true)
                            {
                                pstmt.setString(1, DataToCompare.get(k).getDate());
                                pstmt.setString(2, DataToCompare.get(k).getProgram());
                                pstmt.setString(3, DataToCompare.get(k).getSystemStatus());
                                pstmt.setString(4, DataToCompare.get(k).getProcessStatus());
                                pstmt.setInt(5, Integer.parseInt(DataToCompare.get(k).getNogo()));
                                pstmt.setString(6, DataToCompare.get(k).getSession());
                                pstmt.setString(7, DataToCompare.get(k).getStation());
                                pstmt.executeUpdate(); 
                            }

                            copieMachine=true;
                            break;
                        } 
                    }
                    if (copieMachine==false)
                    {
                        String query2 = "INSERT INTO machine (id_machine,date_machine,programme_machine,status_system_machine,status_process_machine,nogo_machine,session_machine) VALUES('"+DataToCompare.get(k).getStation()+"','"+DataToCompare.get(k).getDate()+"','"+DataToCompare.get(k).getProgram()+"','"+DataToCompare.get(k).getSystemStatus()+"','"+DataToCompare.get(k).getProcessStatus()+"','"+DataToCompare.get(k).getNogo()+"','"+DataToCompare.get(k).getSession()+"')";
                        int rs2 = st.executeUpdate(query2);
                    }
                    st.close();
                    pstmt.close();
                }
            }
            catch (Exception e)
            {
                throw new IllegalStateException("bug comparaison ou bug insertion dans MySQL", e);
            }
        }
    }

    public boolean isMoreRecent(String p1,String p2)
    {
        if(Integer.parseInt(Character.toString(p1.charAt(6))+Character.toString(p1.charAt(7))+Character.toString(p1.charAt(8))+Character.toString(p1.charAt(9)))>Integer.parseInt(Character.toString(p2.charAt(6))+Character.toString(p2.charAt(7))+Character.toString(p2.charAt(8))+Character.toString(p2.charAt(9))))
            return true;
        if(Integer.parseInt(Character.toString(p1.charAt(3))+Character.toString(p1.charAt(4)))>Integer.parseInt(Character.toString(p2.charAt(3))+Character.toString(p2.charAt(4))))
            return true;
        if(Integer.parseInt(Character.toString(p1.charAt(0))+Character.toString(p1.charAt(1)))>Integer.parseInt(Character.toString(p2.charAt(0))+Character.toString(p2.charAt(1))))
            return true;
        if(Integer.parseInt(Character.toString(p1.charAt(11))+Character.toString(p1.charAt(12)))>Integer.parseInt(Character.toString(p2.charAt(11))+Character.toString(p2.charAt(12))))
            return true;
        if(Integer.parseInt(Character.toString(p1.charAt(14))+Character.toString(p1.charAt(15)))>Integer.parseInt(Character.toString(p2.charAt(14))+Character.toString(p2.charAt(15))))
            return true;
        if(Integer.parseInt(Character.toString(p1.charAt(17))+Character.toString(p1.charAt(18)))>Integer.parseInt(Character.toString(p2.charAt(17))+Character.toString(p2.charAt(18))))
            return true;
        return false;
    }
}

