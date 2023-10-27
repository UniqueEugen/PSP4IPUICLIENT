package client;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
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

public class GUIJframeClient extends JFrame implements ActionListener, WindowListener {
    private Socket clientSocket = null;
    private ObjectOutputStream coos;
    private ObjectInputStream cois;
    private JTextField connectIP;
    private JTextField Xfield;
    private JTextField Yfield;
    private JTextField connectPort;
    private JTextArea answer;
    private String clientMessage;
    private String serverMessage;
    private final String END_CMD = "DSCNNCTD";
    private final String CLOSE_CMD = "CLOSED_SERVER";
    private final String MATRIX_CMD = "MATRIX";
    public GUIJframeClient() {
        super("TCP/IP клиент");
        super.setBounds(200, 100, 600, 450);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagConstraints gbc = new GridBagConstraints();
        Container container = super.getContentPane();
        container.setLayout(new GridBagLayout());
        JLabel connectLbIP = new JLabel("IP-адрес: ");
        connectIP = new JTextField("127.0.0.1");
        JLabel connectLBPort = new JLabel("Порт: ");
        connectPort = new Num("2525");

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0,0,0,0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        container.add(connectLbIP, gbc);
        gbc.insets = new Insets(0,0,0,0);
        gbc.ipadx = 20;
        gbc.gridx = 1;
        gbc.gridy = 0;
        container.add(connectIP, gbc);
        gbc.insets = new Insets(0,0,0,0);
        gbc.ipady = 10;
        gbc.gridx = 2;
        gbc.gridy = 0;
        container.add(connectLBPort, gbc);
        gbc.insets = new Insets(0,-50,0,0);
        gbc.ipadx = 10;
        gbc.gridx = 3;
        gbc.gridy = 0;
        container.add(connectPort, gbc);

        JButton connectBtn = new JButton("Соединить");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,-20,0,20);
        gbc.ipady = 20;
        gbc.gridx = 1;
        gbc.gridy = 1;
        container.add(connectBtn, gbc);
        connectBtn.addActionListener(new ButtonConnect());

        JButton disconnectBtn = new JButton("Отсоединиться");
        gbc.insets = new Insets(10,-20,0,20);
        gbc.ipady = 20;
        gbc.gridx = 2;
        gbc.gridy = 1;
        container.add(disconnectBtn, gbc);
        disconnectBtn.addActionListener(new ButtonDisconnect());

        JLabel data = new JLabel("Данные: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 20;
        gbc.gridx = 1;
        gbc.gridy = 2;
        container.add(data,gbc);
        JLabel x = new JLabel("X ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 20;
        gbc.gridx = 1;
        gbc.gridy = 3;
        container.add(x,gbc);
        Xfield = new JTextField("0");
        gbc.ipady = 20;
        gbc.gridx = 2;
        gbc.gridy = 3;
        container.add(Xfield,gbc);
        JLabel y = new JLabel("Y ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 4;
        container.add(y,gbc);
        Yfield = new JTextField("0");
        gbc.gridx = 2;
        gbc.gridy = 4;
        container.add(Yfield,gbc);

        JButton sendBtn = new JButton("Отослать");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 5;
        container.add(sendBtn,gbc);
        sendBtn.addActionListener(this);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 20;
        gbc.gridx = 1;
        gbc.gridy = 6;
        JLabel serverResponse = new JLabel("Ответ сервера: ");
        container.add(serverResponse, gbc);
        answer = new JTextArea(1, 2);
        gbc.gridx = 2;
        gbc.gridy = 6;
        container.add(answer, gbc);


        PlainDocument docX = (PlainDocument) Xfield.getDocument();
        docX.setDocumentFilter(new DigitFilter());
        PlainDocument docY = (PlainDocument) Yfield.getDocument();
        docY.setDocumentFilter(new DigitFilter());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (clientSocket == null) {
            answer.setText("Связь с сервером не установлена!");
            return;
        }
        try {
            String clientMessage = "DEKART,";
            clientMessage += Xfield.getText() + " ";
            clientMessage += Yfield.getText();
            System.out.println(clientMessage);
            coos.writeObject(clientMessage);
            String str = (String) cois.readObject();
            System.out.println(str);
            answer.setText(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
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

    public class ButtonConnect implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                clientSocket = new Socket(InetAddress.getByName(connectIP.getText()), Integer.parseInt(connectPort.getText()));
                coos = new ObjectOutputStream(clientSocket.getOutputStream());
                cois = new ObjectInputStream(clientSocket.getInputStream());
                answer.setText("Connecting successful!");
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
                answer.setText((String) cois.readObject());
                cois.close();
                coos.close();
                clientSocket.close();
                clientSocket=null;
            } catch (NumberFormatException ex) {
            } catch (UnknownHostException ex) {
            } catch (IOException | ClassNotFoundException ex) {
            }
        }
    }
}

