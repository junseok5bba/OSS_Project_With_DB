import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.*;

import net.proteanit.sql.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;

public class Main extends JFrame {

	private JPanel contentPane;
	private JTable table;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	Connection conn = null;
	
	public Main() {
		table = new JTable();
		String sql = "select * from word";
		try {
			conn = dbconnect.dbconn();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(56, 41, 331, 152);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(table);
	}
}
