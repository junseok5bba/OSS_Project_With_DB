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
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.Font;

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

   
   public Main() {
      table = new JTable();
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 789, 747);
      contentPane = 
    		  new JPanel();
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
      new_voca.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent arg0) {
      	}
      });
      file_menu.add(new_voca);
      
      JMenuItem view_myword = new JMenuItem("불러오기");
      view_myword.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent e) {
      	}
      });
      file_menu.add(view_myword);
      
      JMenu odap_menu = new JMenu("오답노트");
      odap_menu.setFont(new Font("맑은 고딕", Font.PLAIN, 21));
      menuBar.add(odap_menu);
      
      JMenuItem new_odap = new JMenuItem("새 오답노트");
      new_odap.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent e) {
      	}
      });
      odap_menu.add(new_odap);
      
      JMenuItem view_odap = new JMenuItem("불러오기");
      view_odap.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent e) {
      	}
      });
      odap_menu.add(view_odap);
      
      JMenuItem suneung = new JMenuItem("수능");
      suneung.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
      menuBar.add(suneung);
      JMenuItem toeic = new JMenuItem("토익");
      toeic.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
      menuBar.add(toeic);
      
      JMenuItem tople = new JMenuItem("토플");
      tople.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
      menuBar.add(tople);
      toeic.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent e) {
      		String sql = "select 영단어, 해석 from toeic";
            try {
               conn = dbconnect.dbconn();
               Statement st = conn.createStatement();
               ResultSet rs = st.executeQuery(sql);
               table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception ex) {
               System.out.println(ex.getMessage());
            }
      	}
      });
      suneung.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent arg0) {
      		String sql = "select 영단어, 해석 from 테스트";
            try {
               conn = dbconnect.dbconn();
               Statement st = conn.createStatement();
               ResultSet rs = st.executeQuery(sql);
               table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
               System.out.println(e.getMessage());
            }
      	}
      });
      tople.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		String sql = "select 영단어, 해석 from word";
              try {
                 conn = dbconnect.dbconn();
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql);
                 table.setModel(DbUtils.resultSetToTableModel(rs));
              } catch (Exception e) {
                 System.out.println(e.getMessage());
              }             
        	}
        });
   }
}
