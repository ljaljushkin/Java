package client;

import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ConnectionState {
	
	public JFrame m_frame;
	
	public JTextField m_textField;
	public JLabel m_label;
	
	public JButton m_button;
	
	public JTextArea m_clientsTextArea;
	public JTextArea m_gamesTextArea;
	
	
	public ConnectionThread m_connectionThread;
	public Socket m_clientSocket;
	public final static int s_port = 4378;
}
