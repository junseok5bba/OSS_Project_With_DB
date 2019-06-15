package kr.ac.sunmoon;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import net.proteanit.sql.*;

import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

import javax.swing.*;
import java.awt.Font;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame {

	private JPanel contentPane; // 단어징 기본 프로그램의 패널
	private JPanel testpane; // 테스트 화면으 패널
	private JTable table; // 단어장의 영단어, 해석을 저장하기 위한 테이블
	private JTextField wordeng; // 단어추가를 할때 영단어
	private JTextField wordkor; // 단어추가를 할때 해석
	private JTextField testResultText; // 테스트 결과
	private JTextField testFailCountText; // 최근 테스트 결과의 틀린 개수를 보여주기 위함
	private JTextField TotalCount; // 단어의 총 개수

	static String id = null; // 로그인한 ID
	static Main frame = new Main(id);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
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

	// DB연결에 필요한 변수
	Connection conn = null;
	PreparedStatement pstmt = null;

	// DB의 값을 추가, 삭제 등을 하기 위한 변수들
	String tablename = ""; // 각 카테고리를 눌렀을 때 그 테이블 이름
	String kor; // 추가할 해석
	String eng; // 추가할 영어
	String voca_id; // 선택한 단어장의 voca_id(단어장번호)를 알기 위한 변수
	String sql = null; // DB에 적용하기 위한 SQL문
	int point = 0; // 테스트의 각 라디오 버튼을 눌렀을 때 어떤 것을 눌렀는지를 알기 위한 변수
	int size = 0; // 테스트를 볼떄 테스트를 볼 단어의 개수
	int selectindex = 0; // 테스트에서 랜덤한 값을 라디오버튼에 넣었을때 영단어와 매칭되지않는 해석이 없을 때 랜덤하게 넣기 위한 변수
	int[] rad; // 각 라디오 버튼에 넣을 해석의 인덱스를 저장하기 위한 변수

	public Main(String id) {
		// 테스트화면에 출력되는 내용
		testpane = new JPanel();
		testpane.setBorder(new EmptyBorder(5, 5, 5, 5));

		// 버튼그룹
		ButtonGroup btng = new ButtonGroup();

		// 테스트 영단어
		JLabel wordl = new JLabel("영단어");
		wordl.setFont(new Font("굴림", Font.BOLD, 25));
		wordl.setHorizontalAlignment(SwingConstants.CENTER);
		wordl.setBounds(35, 30, 360, 53);
		testpane.add(wordl);

		// 테스트를 볼때 다음으로 넘어 갔을 때 라디오 버튼이 선택되어 있지 않도록하기위한 버튼그룹에 추가되지않은 라디오버튼(NULL라디오버튼)
		JRadioButton nullrad = new JRadioButton("New radio button");
		btng.add(nullrad);

		// 테스트의 1번 라디오버튼
		JRadioButton rad1 = new JRadioButton("New radio button");
		rad1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				point = 0;
				System.out.println(point);
			}
		});
		rad1.setHorizontalAlignment(SwingConstants.CENTER);
		rad1.setFont(new Font("굴림", Font.PLAIN, 16));
		rad1.setBounds(35, 95, 360, 23);
		btng.add(rad1);

		// 테스트의 2번 라디오버튼
		JRadioButton rad2 = new JRadioButton("New radio button");
		rad2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				point = 1;
				System.out.println(point);
			}
		});
		rad2.setHorizontalAlignment(SwingConstants.CENTER);
		rad2.setFont(new Font("굴림", Font.PLAIN, 16));
		rad2.setBounds(35, 125, 360, 23);
		btng.add(rad2);

		// 테스트의 3번 라디오버튼
		JRadioButton rad3 = new JRadioButton("New radio button");
		rad3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				point = 2;
				System.out.println(point);
			}
		});
		rad3.setHorizontalAlignment(SwingConstants.CENTER);
		rad3.setFont(new Font("굴림", Font.PLAIN, 16));
		rad3.setBounds(35, 155, 360, 23);
		btng.add(rad3);

		// 테스트의 4번 라디오버튼
		JRadioButton rad4 = new JRadioButton("New radio button");
		rad4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				point = 3;
				System.out.println(point);
			}
		});
		rad4.setHorizontalAlignment(SwingConstants.CENTER);
		rad4.setFont(new Font("굴림", Font.PLAIN, 16));
		rad4.setBounds(35, 185, 360, 23);
		btng.add(rad4);

		// 테스트 패널에 각 라디오 버튼들을 추가
		testpane.add(rad1);
		testpane.add(rad2);
		testpane.add(rad3);
		testpane.add(rad4);

		setResizable(false); // 메인 화면의 사이즈를 조정하지 못하도록 설정
		table = new JTable();
		table.setFillsViewportHeight(true);
		setBounds(100, 100, 780, 747);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 252, 253));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 46, 355, 604);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(table);

		// 상단의 메뉴바를 생성
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 765, 35);
		contentPane.add(menuBar);

		// 파일이라는 메뉴 생성
		JMenu file_menu = new JMenu("파일");
		file_menu.setBackground(new Color(255, 255, 255));
		file_menu.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		menuBar.add(file_menu);

		// 파일 메뉴의 새_딴어장 기능 추가
		JMenuItem new_voca = new JMenuItem("새 단어장");
		new_voca.setBackground(SystemColor.text);
		new_voca.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		new_voca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					conn = dbconnect.dbconn();
					Statement st = conn.createStatement();
					tablename = id + "새_단어장";
					sql = "insert into voca (vocaname, vocaowner) values (?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, tablename);
					pstmt.setString(2, id);
					int r = pstmt.executeUpdate();

					StringBuilder sb = new StringBuilder();
					String sql = sb.append("create table " + tablename + "(").append("wordid int(10) auto_increment, ")
							.append("영단어 VARCHAR(50), ").append("해석 VARCHAR(50), ").append("vocaid int(10), ")
							.append("primary key(wordid), ").append("constraint " + tablename
									+ "_fk_vocaid foreign key(vocaid) references voca(vocaid)")
							.append(");").toString();
					st.execute(sql);
					sql = "select 영단어, 해석 from " + tablename;
					ResultSet rs = st.executeQuery(sql);
					while (rs.next())
						voca_id = rs.getString(1);
					table.setModel(DbUtils.resultSetToTableModel(rs));
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		file_menu.add(new_voca);

		// 파일 메뉴의 불러오기 기능
		JMenuItem view_myword = new JMenuItem("불러오기");
		view_myword.setBackground(SystemColor.text);
		view_myword.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		view_myword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tablename = id + "새_단어장";
				String sql = "select 영단어, 해석 from " + tablename;
				try {
					conn = dbconnect.dbconn();
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);
					table.setModel(DbUtils.resultSetToTableModel(rs));

					sql = "select * from voca where vocaname = '" + tablename + "'";
					st = conn.createStatement();
					rs = st.executeQuery(sql);
					while (rs.next())
						voca_id = rs.getString(1);

					TotalCount.setText(table.getRowCount() + "개");
					testResultText.setText("");
					testFailCountText.setText("");
				} catch (Exception f) {
					System.out.println(f.getMessage());
				}
			}
		});
		file_menu.add(view_myword);

		// 오답노트라는 메뉴를 생성
		JMenu odap_menu = new JMenu("오답노트");
		odap_menu.setBackground(new Color(255, 255, 255));
		odap_menu.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		menuBar.add(odap_menu);

		// 오답노트 메뉴의 새 오답노트기능
		JMenuItem new_odap = new JMenuItem("새 오답노트");
		new_odap.setBackground(SystemColor.text);
		new_odap.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		new_odap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					conn = dbconnect.dbconn();
					Statement st = conn.createStatement();
					tablename = id + "오답노트";
					sql = "insert into voca (vocaname, vocaowner) values (?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, tablename);
					pstmt.setString(2, id);
					int r = pstmt.executeUpdate();

					StringBuilder sb = new StringBuilder();
					String sql = sb.append("create table " + tablename + "(").append("wordid int(10) auto_increment, ")
							.append("영단어 VARCHAR(50), ").append("해석 VARCHAR(50), ").append("vocaid int(10), ")
							.append("primary key(wordid), ").append("constraint " + tablename
									+ "_fk_vocaid foreign key(vocaid) references voca(vocaid)")
							.append(");").toString();
					st.execute(sql);
					sql = "select 영단어, 해석 from " + tablename;
					ResultSet rs = st.executeQuery(sql);
					while (rs.next())
						voca_id = rs.getString(1);
					table.setModel(DbUtils.resultSetToTableModel(rs));

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});
		odap_menu.add(new_odap);

		// 오답노트 안의 불러오기 기능
		JMenuItem view_odap = new JMenuItem("불러오기");
		view_odap.setBackground(SystemColor.text);
		view_odap.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		view_odap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tablename = id + "오답노트";
				String sql = "select 영단어, 해석 from " + tablename;
				try {
					conn = dbconnect.dbconn();
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);
					table.setModel(DbUtils.resultSetToTableModel(rs));

					sql = "select * from voca where vocaname = '" + tablename + "'";
					st = conn.createStatement();
					rs = st.executeQuery(sql);
					while (rs.next())
						voca_id = rs.getString(1);

					TotalCount.setText(table.getRowCount() + "개");
					testResultText.setText("");
					testFailCountText.setText("");
				} catch (Exception d) {
					System.out.println(d.getMessage());
				}
			}
		});
		odap_menu.add(view_odap);

		// 상단의 수능 탭
		JMenuItem suneung = new JMenuItem("수능");
		suneung.setHorizontalAlignment(SwingConstants.CENTER);
		suneung.setBackground(SystemColor.text);
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
					TotalCount.setText(table.getRowCount() + "개");
					testResultText.setText("");
					testFailCountText.setText("");
				} catch (Exception a) {
					System.out.println(a.getMessage());
				}
			}
		});
		menuBar.add(suneung);

		// 상단의 토익 탭
		JMenuItem toeic = new JMenuItem("토익");
		toeic.setHorizontalAlignment(SwingConstants.CENTER);
		toeic.setBackground(SystemColor.text);
		toeic.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		toeic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select 영단어, 해석 from 토익";
				try {
					conn = dbconnect.dbconn();
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);
					table.setModel(DbUtils.resultSetToTableModel(rs));
					tablename = "토익";
					voca_id = "2";
					TotalCount.setText(table.getRowCount() + "개");
					testResultText.setText("");
					testFailCountText.setText("");
				} catch (Exception c) {
					System.out.println(c.getMessage());
				}
			}
		});
		menuBar.add(toeic);

		// 상단의 토플 탭
		JMenuItem tople = new JMenuItem("토플");
		tople.setHorizontalAlignment(SwingConstants.CENTER);
		tople.setBackground(SystemColor.text);
		tople.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		tople.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "select 영단어, 해석 from 토플";
				try {
					conn = dbconnect.dbconn();
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);
					table.setModel(DbUtils.resultSetToTableModel(rs));
					tablename = "토플";
					voca_id = "3";
					TotalCount.setText(table.getRowCount() + "개");
					testResultText.setText("");
					testFailCountText.setText("");
				} catch (Exception b) {
					System.out.println(b.getMessage());
				}
			}
		});
		menuBar.add(tople);

		// 상단의 종료 탭
		JMenuItem menu_quit = new JMenuItem("종료");
		menu_quit.setHorizontalAlignment(SwingConstants.CENTER);
		menu_quit.setBackground(SystemColor.text);
		menu_quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu_quit.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		menuBar.add(menu_quit);

		// 추가 및 삭제 기능들의 패널
		JPanel wordpanel = new JPanel();
		wordpanel.setBorder(new LineBorder(new Color(192, 192, 192), 2, true));
		wordpanel.setBackground(new Color(255, 252, 253));
		wordpanel.setBounds(399, 46, 353, 149);
		contentPane.add(wordpanel);
		wordpanel.setLayout(null);

		// 추가 및 삭제 기능의 영단어 입력 텍스트
		wordeng = new JTextField();
		wordeng.setBounds(88, 26, 138, 31);
		wordpanel.add(wordeng);
		wordeng.setColumns(10);

		// 추가 및 삭제 기능의 해석 입력 텍스트
		wordkor = new JTextField();
		wordkor.setBounds(88, 75, 138, 31);
		wordpanel.add(wordkor);
		wordkor.setColumns(10);

		// 추가 및 삭제 기능의 추가 버튼
		JButton addword = new JButton("추가");
		addword.setBackground(new Color(255, 255, 255));
		addword.setFont(new Font("맑은 고딕", Font.PLAIN, 21));
		addword.setBounds(250, 19, 91, 37);
		addword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eng = wordeng.getText();
				kor = wordkor.getText();
				try {
					int cnt = 0;
					conn = dbconnect.dbconn();
					String sql = "select * from " + tablename + " where 영단어 = '" + eng + "'"; // 중복을 검사하기위함
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);
					while (rs.next()) {
						cnt++; // 입력한 영단어 값인 데이터베이스 값을 가져와서 다음행으로 갈때마다 cnt증가
					}

					if (cnt == 0) // 위에서 cnt가 증가하지 않았다면 ( 중복된 값이 없다면 )
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

						TotalCount.setText(table.getRowCount() + "개");
					} else // 중복된다면
					{
						JOptionPane.showMessageDialog(null, "중복된 단어입니다 !", "중복된 단어", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex1) {
					System.out.println(ex1.getMessage());
				}
			}
		});
		wordpanel.add(addword);

		// 추가 및 삭제 기능의 삭제 버튼
		JButton delword = new JButton("삭제");
		delword.setBackground(new Color(255, 255, 255));
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
					TotalCount.setText(table.getRowCount() + "개");
				} catch (SQLException del) {
					// TODO Auto-generated catch block
					del.printStackTrace();
				}
			}
		});
		delword.setFont(new Font("맑은 고딕", Font.PLAIN, 21));
		delword.setBounds(250, 75, 91, 37);
		wordpanel.add(delword);

		// 추가 및 삭제 텍스트에서 영단어를 입력하도록 알려주는 라벨
		JLabel wordeng_label = new JLabel("영단어");
		wordeng_label.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		wordeng_label.setBounds(19, 26, 61, 25);
		wordpanel.add(wordeng_label);

		// 추가 및 삭제 텍스트에서 해석 입력하도록 알려주는 라벨
		JLabel wordkorlabel = new JLabel("해석");
		wordkorlabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		wordkorlabel.setBounds(18, 81, 43, 25);
		wordpanel.add(wordkorlabel);

		// 팝업 전환 및 테스트 기능들의 패널
		JPanel optionpanel = new JPanel();
		optionpanel.setBorder(new LineBorder(new Color(192, 192, 192), 2, true));
		optionpanel.setBackground(new Color(255, 252, 253));
		optionpanel.setBounds(398, 528, 354, 122);
		contentPane.add(optionpanel);
		optionpanel.setLayout(null);

		// 팝업 전환이라는 버튼을 생성
		JButton popupbutton = new JButton("팝업 전환");
		popupbutton.setBackground(new Color(255, 255, 255));
		popupbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getActionCommand().equals("팝업 전환")) {
					minsize minsz = new minsize(tablename);
					minsz.setVisible(true);
				}
			}
		});
		popupbutton.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		popupbutton.setBounds(190, 18, 152, 86);
		optionpanel.add(popupbutton);

		// 테스트 라는 버튼을 생성
		JButton btnNewButton = new JButton("테스트");
		btnNewButton.setBounds(12, 18, 161, 86);
		optionpanel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				model.setNumRows(0);


				ArrayList<String> testWords = new ArrayList<String>();
				int failCount = 0; // 틀린개수
				int n = 0; // 라디오 버튼 별로 값을 넣기 위한 변수
				int testword[]; // 시험볼 단어를 랜덤하게 선정하기 위한 변수
				Random rand = new Random(); // 랜덤하게 하도록 랜덤함수

				conn = dbconnect.dbconn(); // DB연결
				try {
					ArrayList<String> englist = new ArrayList<>(); //DB의 영단어 리스트
					ArrayList<String> korlist = new ArrayList<>(); //DB의 해석 리스트

					
					sql = "select 영단어, 해석 from " + tablename;
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);

					while (rs.next()) { //sql문의 결과에서 다음행이 있을 때마다
						englist.add(rs.getString(1)); //영단어를 영단어리스트에 저장 
						korlist.add(rs.getString(2)); //해석을 해석리스트에 저장
					}

					if (englist.size() >= 20) // 테스트 버튼을 눌렀을 때 선택된 테이블의 단어의 개수가 20개 이상일떄
						size = 20;
					else // 20개 미만일 떄
						size = englist.size();

					ArrayList<String> pick = new ArrayList<>(); //선택한 답을 저장하는 리스트
					testword = new int[size]; // 

					//시험볼 단어의 영단어를 랜덤하게 저장
					for (int x = 0; x < size; x++) { 
						testword[x] = rand.nextInt(englist.size());
						for (int y = 0; y < x; y++) {
							if (testword[y] == testword[x]) {
								x--;
							}
						}
					}

					//시험볼 단어의 개수 만큼 반복
					for (int i = 0; i < size; i++) {
						int cnt = 0; //
						point = 5; //선택한 값 초기화 ( 5일 시 선택하지않은 것으로 간주 )
					
						selectindex = rand.nextInt(4); //0~3까지 랜덤하게 갑 지정 (몇번 라디오 버튼인지 )
						rad = new int[4];

						//rad배열에 해석의 인덱스값을 랜덤하게 저장
						for (int x = 0; x < 4; x++) {
							rad[x] = rand.nextInt(englist.size());
							for (int y = 0; y < x; y++) {
								if (rad[y] == rad[x]) {
									x--;
								}
							}
						}
						
						//라디오버튼1~4에 해당 영단어와 매칭되는 단어가 있는지 없는지 판별하기 위함
						for (n = 0; n < 4; n++)
							if (rad[n] == testword[i])
								cnt++;

						//매칭되는 단어가 없으면 랜덤한 라디오버튼에 매칭되는 해석을 저장
						if (cnt == 0)
							rad[selectindex] = testword[i];

						n = 0;
						wordl.setText(englist.get(testword[i]));
						testWords.add(englist.get(testword[i]));
						rad1.setText(korlist.get(rad[n++]));
						rad2.setText(korlist.get(rad[n++]));
						rad3.setText(korlist.get(rad[n++]));
						rad4.setText(korlist.get(rad[n++]));

						JOptionPane.showOptionDialog(null, testpane, "테스트", JOptionPane.DEFAULT_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, null, null);
						if (point < 4) {
							if (testword[i] == rad[point])
								pick.add("정답인   " + korlist.get(rad[point]));
							else
								pick.add("오답인   " + korlist.get(rad[point]));
							if (testword[i] != rad[point])
								failCount++;
						} else {
							pick.add("선택하지 않으셨습니다");
							failCount++;
						}
						nullrad.setSelected(true); //이전에 선태한 라디오버튼이 선택되지않도록 하기 위함

					}
					DecimalFormat form = new DecimalFormat("#.#");
					if (englist.size() != 0) {
						testResultText.setText(form.format(100 * ((size - (double) failCount) / (double) size)) + " 점");
						testFailCountText.setText(failCount + " 개");
					}
					// 결과 메시지를 준비합니다.

					String resultMessage = "";
					// 모든 결과 내용을 결과 메시지에 담습니다.
					for (int i = 0; i < pick.size(); i++) {
						resultMessage += (i + 1) + "번째 시도 : 단어 '" + testWords.get(i) + "'에 대하여 '" + pick.get(i)
								+ "' 라고 답했습니다.\n";
					}
					resultMessage += "테스트 점수 : " + form.format(100 * ((size - (double) failCount) / (double) size))
							+ " 점";
					resultMessage += "\n틀린 개수 : " + failCount + " 개";
					// 결과 메시지를 스크롤 팬에 담겨진 JOptionPane의 형태로 사용자에게 보여줍니다.
					JTextArea textArea = new JTextArea(resultMessage);
					JScrollPane scrollPane = new JScrollPane(textArea);
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);
					scrollPane.setPreferredSize(new Dimension(500, 500));
					
					String sql = "select 영단어, 해석 from " + tablename;
					st = conn.createStatement();
					rs = st.executeQuery(sql);
					table.setModel(DbUtils.resultSetToTableModel(rs));
					
					JOptionPane.showMessageDialog(null, scrollPane, "테스트 결과", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception test) {
					test.getMessage();
				}
			}
		});
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setFont(new Font("맑은 고딕", Font.PLAIN, 18));

		// 총단어수와 최근 테스트의 점수와 틀린개수들이 있는 패널
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(192, 192, 192), 2, true));
		panel.setBackground(new Color(255, 252, 253));
		panel.setBounds(399, 213, 353, 277);
		contentPane.add(panel);
		panel.setLayout(null);

		//총단어수라는 라벨을 생성
		JLabel TotalCountLabel = new JLabel("총 단어수");
		TotalCountLabel.setBounds(19, 44, 102, 25);
		TotalCountLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		panel.add(TotalCountLabel);

		//총 단어수를 출력하는 TextField생성
		TotalCount = new JTextField();
		TotalCount.setBounds(143, 40, 191, 36);
		panel.add(TotalCount);
		TotalCount.setColumns(10);
		TotalCount.setBackground(Color.WHITE);
		TotalCount.setHorizontalAlignment(SwingConstants.RIGHT);
		TotalCount.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		TotalCount.setEditable(false);

		//최근점수라는 라벨을 생성
		JLabel testResultLabel = new JLabel("최근 점수");
		testResultLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		testResultLabel.setBounds(19, 118, 119, 33);
		panel.add(testResultLabel);

		//최근점수를 출력하는 TextField생성
		testResultText = new JTextField();
		testResultText.setBackground(Color.WHITE);
		testResultText.setHorizontalAlignment(SwingConstants.RIGHT);
		testResultText.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		testResultText.setEditable(false);
		testResultText.setColumns(10);
		testResultText.setBounds(143, 118, 191, 36);
		panel.add(testResultText);

		//최근 틀린 개수라는 라벨 생성
		JLabel testFailCountLabel = new JLabel("최근 틀린 개수");
		testFailCountLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		testFailCountLabel.setBounds(19, 204, 129, 33);
		panel.add(testFailCountLabel);

		//최근 틀린개수를 보여주는 TextField생성
		testFailCountText = new JTextField();
		testFailCountText.setForeground(Color.BLACK);
		testFailCountText.setBackground(Color.WHITE);
		testFailCountText.setHorizontalAlignment(SwingConstants.RIGHT);
		testFailCountText.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		testFailCountText.setEditable(false);
		testFailCountText.setColumns(10);
		testFailCountText.setBounds(143, 204, 191, 36);
		panel.add(testFailCountText);
	}
}
