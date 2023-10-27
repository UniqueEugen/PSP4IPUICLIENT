package client;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class GUIClient extends Frame implements ActionListener, WindowListener {
    private TextField connectIP, connectPort, tf2, tf3;
    private TextArea ta;
    private Label la, la1, la2, la3, la4, la5, la6;
    private Socket clientSocket = null;
    private ObjectOutputStream coos;
    private ObjectInputStream cois;
    private String clientMessage;
    private String serverMessage;
    private final String END_CMD = "DSCNNCTD";
    private final String CLOSE_CMD = "CLOSED_SERVER";

    public void GUI() {
        setTitle("КЛИЕНТ");
        connectIP = new TextField("127.0.0.1");
        connectPort = new TextField("2525");
        tf2 = new TextField();
        tf3 = new TextField();
        ta = new TextArea("Hello");
        la = new Label("IP ADRESS");
        la1 = new Label("port");
        la2 = new Label("sending date");
        la3 = new Label("result ");
        la4 = new Label(" ");
        la5 = new Label("X");
        la6 = new Label("Y");
        Button connectBtn = new Button("connect");
        Button sendBtn = new Button("send");
        Button disconnectBtn = new Button("disconnect");
        connectIP.setBounds(200, 50, 70, 25);
        connectPort.setBounds(330, 50, 70, 25);
        la5.setBounds(140, 200, 10, 25);
        tf2.setBounds(155, 200, 50, 25);
        la6.setBounds(215, 200, 10, 25);
        tf3.setBounds(230, 200, 50, 25);
        ta.setBounds(150, 300, 0, 40);
        connectBtn.setBounds(50, 50, 70, 25);
        sendBtn.setBounds(50, 200, 70, 25);
        disconnectBtn.setBounds(50, 80, 70, 25);
        la.setBounds(130, 50, 150, 25);
        la1.setBounds(280, 50, 150, 25);
        la2.setBounds(150, 150, 150, 25);
        la3.setBounds(160, 250, 150, 25);
        la4.setBounds(600, 10, 150, 25);
        add(connectIP);
        add(connectPort);
        add(tf2);
        add(tf3);
        add(connectBtn);
        add(sendBtn);
        add(disconnectBtn);
        add(ta);
        add(la);
        add(la1);
        add(la2);
        add(la3);
        add(la4);
        add(la5);
        add(la6);
        setSize(600, 600);
        setVisible(true);
        addWindowListener(this);
        connectBtn.addActionListener(new ButtonConnect());
        sendBtn.addActionListener(this);
        disconnectBtn.addActionListener(new ButtonDisconnect());
        tf2.getText();
        tf3.getText();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (clientSocket != null && !clientSocket.isClosed()) { // если сокет не пустой и сокет открыт
            try {
                cois.close();
                coos.close();
                clientSocket.close(); // сокет  закрывается
            } catch (IOException ex) {
            }
        }
        this.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }


    public void actionPerformed(ActionEvent e) {
        if (clientSocket == null) {
            return;
        }
        try {
            clientMessage = "DEKART,";
            clientMessage += tf2.getText() + " ";
            clientMessage += tf3.getText() + " ";
            System.out.println(clientMessage);
            coos.writeObject(clientMessage);
            String str = (String) cois.readObject();
            System.out.println(str);
            ta.setText(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class ButtonConnect implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                clientSocket = new Socket(InetAddress.getByName(connectIP.getText()), Integer.parseInt(connectPort.getText()));
                coos = new ObjectOutputStream(clientSocket.getOutputStream());
                cois = new ObjectInputStream(clientSocket.getInputStream());
                ta.setText("Connecting successful!");
            } catch (NumberFormatException ex) {
            } catch (UnknownHostException ex) {
            } catch (IOException ex) {
            }
        }
    }
    public class ButtonDisconnect implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                clientMessage = END_CMD+",";
                System.out.println(clientMessage);
                coos.writeObject(clientMessage);
                ta.setText((String) cois.readObject());
                cois.close();
                coos.close();
                clientSocket.close();
            } catch (NumberFormatException ex) {
            } catch (UnknownHostException ex) {
            } catch (IOException | ClassNotFoundException ex) {
            }
        }
    }
}

