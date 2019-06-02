package kr.ac.sunmoon;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.*;
import net.proteanit.sql.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JTextField;
import javax.swing.*;
import java.awt.Font;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.Color;
import java.awt.Dimension;

public class Main extends JFrame {

	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	Connection conn = null;
	PreparedStatement pstmt = null;
	String tablename;
	String word_id;
	String kor;
	String eng;
	String voca_id;

	private JTextField wordeng;
	private JTextField wordkor;

	public Main() {
		table = new JTable();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 780, 747);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 46, 355, 604);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(table);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 765, 35);
		contentPane.add(menuBar);

		JMenu file_menu = new JMenu("파일");
		file_menu.setFont(new Font("맑은 고딕", Font.PLAIN, 21));
		menuBar.add(file_menu);

		JMenuItem new_voca = new JMenuItem("새 단어장");
		new_voca.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		new_voca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					conn = dbconnect.dbconn();
					Statement st = conn.createStatement();
					StringBuilder sb = new StringBuilder();
					String sql = sb.append("create table 새_단어장(").append("wordid int(10) auto_increment, ")
							.append("영단어 VARCHAR(50), ").append("해석 VARCHAR(50), ").append("vocaid int(10), ")
							.append("primary key(wordid), ")
							.append("constraint 새_단어장_fk_vocaid foreign key(vocaid) references voca(vocaid)")
							.append(");").toString();
					st.execute(sql);
					tablename = "새_단어장";
					sql = "select 영단어, 해석 from " + tablename;
					ResultSet rs = st.executeQuery(sql);
					table.setModel(DbUtils.resultSetToTableModel(rs));
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		file_menu.add(new_voca);

		JMenuItem view_myword = new JMenuItem("불러오기");
		view_myword.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		view_myword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select 영단어, 해석 from 새_단어장";
				try {
					conn = dbconnect.dbconn();
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);
					table.setModel(DbUtils.resultSetToTableModel(rs));
					tablename = "새_단어장";
					voca_id = "4";
				} catch (Exception f) {
					System.out.println(f.getMessage());
				}
			}
		});
		file_menu.add(view_myword);

		JMenu odap_menu = new JMenu("오답노트");
		odap_menu.setFont(new Font("맑은 고딕", Font.PLAIN, 21));
		menuBar.add(odap_menu);

		JMenuItem new_odap = new JMenuItem("새 오답노트");
		new_odap.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		new_odap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					conn = dbconnect.dbconn();
					Statement st = conn.createStatement();
					StringBuilder sb = new StringBuilder();
					String sql = sb.append("create table 오답노트(").append("wordid int(10) auto_increment, ")
							.append("영단어 VARCHAR(50), ").append("해석 VARCHAR(50), ").append("vocaid int(10), ")
							.append("primary key(wordid), ")
							.append("constraint 오답노트_fk_vocaid foreign key(vocaid) references voca(vocaid)")
							.append(");").toString();
					st.execute(sql);
					tablename = "오답노트";
					sql = "select 영단어, 해석 from " + tablename;
					ResultSet rs = st.executeQuery(sql);
					table.setModel(DbUtils.resultSetToTableModel(rs));
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});
		odap_menu.add(new_odap);

		JMenuItem view_odap = new JMenuItem("불러오기");
		view_odap.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		view_odap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select 영단어, 해석 from 오답노트";
				try {
					conn = dbconnect.dbconn();
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);
					table.setModel(DbUtils.resultSetToTableModel(rs));
					tablename = "오답노트";
					voca_id = "5";
				} catch (Exception d) {
					System.out.println(d.getMessage());
				}
			}
		});
		odap_menu.add(view_odap);

		JMenuItem suneung = new JMenuItem("수능");
		suneung.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		suneung.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "select 영단어, 해석 from 수능";
				try {
					conn = dbconnect.dbconn();
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);
					table.setModel(DbUtils.resultSetToTableModel(rs));
					tablename = "수능";
					voca_id = "1";
				} catch (Exception a) {
					System.out.println(a.getMessage());
				}
			}
		});
		menuBar.add(suneung);

		JMenuItem toeic = new JMenuItem("토익");
		toeic.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		toeic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select 영단어, 해석 from toeic";
				try {
					conn = dbconnect.dbconn();
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);
					table.setModel(DbUtils.resultSetToTableModel(rs));
					tablename = "toeic";
					voca_id = "2";
				} catch (Exception c) {
					System.out.println(c.getMessage());
				}
			}
		});
		menuBar.add(toeic);

		JMenuItem tople = new JMenuItem("토플");
		tople.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		tople.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "select 영단어, 해석 from tople";
				try {
					conn = dbconnect.dbconn();
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);
					table.setModel(DbUtils.resultSetToTableModel(rs));
					tablename = "tople";
					voca_id = "3";
				} catch (Exception b) {
					System.out.println(b.getMessage());
				}
			}
		});
		menuBar.add(tople);

		JPanel wordpanel = new JPanel();
		wordpanel.setBackground(Color.WHITE);
		wordpanel.setBounds(399, 46, 353, 149);
		contentPane.add(wordpanel);
		wordpanel.setLayout(null);

		wordeng = new JTextField();
		wordeng.setBounds(88, 26, 138, 31);
		wordpanel.add(wordeng);
		wordeng.setColumns(10);

		wordkor = new JTextField();
		wordkor.setBounds(88, 75, 138, 31);
		wordpanel.add(wordkor);
		wordkor.setColumns(10);

		JButton addword = new JButton("추가");
     		addword.setBackground(SystemColor.control);
      		addword.setFont(new Font("맑은 고딕", Font.PLAIN, 21));
      		addword.setBounds(250, 19, 91, 37);
      		addword.addActionListener(new ActionListener() {
         		public void actionPerformed(ActionEvent e) {
            			eng = wordeng.getText();
            			kor = wordkor.getText();
         
            			try {
            				int cnt = 0;
               				conn = dbconnect.dbconn();
               				String sql = "select * from "+tablename+" where 영단어 = '" + eng + "'"; //중복을 검사하기위함
               				Statement st = conn.createStatement();
               				ResultSet rs = st.executeQuery(sql);
               				while(rs.next()) {
            	   				cnt++; // 입력한 영단어 값인 데이터베이스 값을 가져와서 다음행으로 갈때마다 cnt증가
               				}
               
               				if(cnt == 0) // 위에서 cnt가 증가하지 않았다면 ( 중복된 값이 없다면 )
               				{
               					sql = "insert into " + tablename + "(영단어, 해석, vocaid) values ( ?, ?, ?)";
               					pstmt = conn.prepareStatement(sql);
               					pstmt.setString(1, eng);
               					pstmt.setString(2, kor);
               					pstmt.setString(3, voca_id);
               					int r = pstmt.executeUpdate();

               					sql = "select 영단어, 해석 from " + tablename;
               					st = conn.createStatement();
               					rs = st.executeQuery(sql);
               					table.setModel(DbUtils.resultSetToTableModel(rs));

               					wordeng.setText("");
               					wordkor.setText("");
               				}
		       			else // 중복된다면
               				{
            	   				JOptionPane.showMessageDialog(null, "중복된 단어입니다 !", "중복된 단어", JOptionPane.ERROR_MESSAGE);
               				}
            			} catch (Exception ex1) {
               				System.out.println(ex1.getMessage());
            			}
         		}
      		});
      		wordpanel.add(addword);

		JButton delword = new JButton("삭제");
		delword.setBackground(SystemColor.control);
		delword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				int col = 0;
				Object value = table.getValueAt(row, col);
				try {
					Statement st = conn.createStatement();
					StringBuilder sb = new StringBuilder();
					String sql = sb.append("delete from " + tablename + " where 영단어 = '").append(value + "'")
							.append(";").toString();
					st.executeUpdate(sql);
					sql = "select 영단어, 해석 from " + tablename;
					ResultSet rs = st.executeQuery(sql);
					table.setModel(DbUtils.resultSetToTableModel(rs));
				} catch (SQLException del) {
					// TODO Auto-generated catch block
					del.printStackTrace();
				}
			}
		});
		delword.setFont(new Font("맑은 고딕", Font.PLAIN, 21));
		delword.setBounds(250, 75, 91, 37);
		wordpanel.add(delword);

		JLabel wordeng_label = new JLabel("영단어");
		wordeng_label.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		wordeng_label.setBounds(19, 26, 61, 25);
		wordpanel.add(wordeng_label);

		JLabel wordkorlabel = new JLabel("해석");
		wordkorlabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		wordkorlabel.setBounds(18, 81, 43, 25);
		wordpanel.add(wordkorlabel);

		JPanel optionpanel = new JPanel();
		optionpanel.setBackground(Color.WHITE);
		optionpanel.setBounds(398, 528, 354, 122);
		contentPane.add(optionpanel);
		optionpanel.setLayout(null);

		
		
		JButton popupbutton = new JButton("미니 모드");
		popupbutton.setBackground(SystemColor.control);
		popupbutton.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		popupbutton.setBounds(12, 18, 161, 86);
		optionpanel.add(popupbutton);
		
		JButton quitbutton = new JButton("종료");
		quitbutton.setBackground(SystemColor.control);
		quitbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		quitbutton.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		quitbutton.setBounds(190, 18, 152, 86);
		optionpanel.add(quitbutton);
	}
}
