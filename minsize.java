import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.*;

import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLayeredPane;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JTextPane;
import javax.swing.JInternalFrame;
import java.awt.Color;

public class minsize extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String tablename = null;
					minsize frame = new minsize(tablename);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	int i = 0;

	public minsize(String tablename) {

		setAlwaysOnTop(true);
		setResizable(false);
		setBounds(100, 100, 300, 160);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 2, 0, 0));

		JTextPane engl = new JTextPane();
		contentPane.add(engl);

		JTextPane korl = new JTextPane();
		contentPane.add(korl);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				try {
					Thread myThread = new Thread();
					conn = dbconnect.dbconn();
					stmt = conn.createStatement();
					String usevocabulary = "use vocabulary";
					stmt.executeUpdate(usevocabulary);

					String showword = "select * from " + tablename;
					rs = stmt.executeQuery(showword);

					ArrayList<String> eng = new ArrayList<>();
					ArrayList<String> kor = new ArrayList<>();

					while (rs.next()) {
						eng.add(rs.getString(2));
						kor.add(rs.getString(3));
					}

					for (int x = 0; x < eng.size(); x++) {
						System.out.println(eng.get(x));
						System.out.println(kor.get(x));

						engl.setText(eng.get(x));
						korl.setText(kor.get(x));
						myThread.sleep(3000);
					}
				} catch (Exception d) {
					System.out.println(d.getMessage());
				}
			}
		});
	}
}
