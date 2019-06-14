package kr.ac.sunmoon;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class register extends JFrame {

   private JPanel contentPane2;
   private JTextField ridt;
   private JTextField rnamet;

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               register frame = new register();
               frame.setVisible(true);
               frame.addWindowListener(new WindowAdapter() // 새 창 x 누를시 꺼지게해주기위하여
						{
					public void windowClosing(WindowEvent e)
					{
						frame.setVisible(false);
						frame.dispose();
					}
						});
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
   Statement st = null;
   ResultSet rs = null;
   String sql = null;
   PreparedStatement pstmt = null;
   
   public register() {
     setResizable(false);
      setBounds(100, 100, 265, 135);
      contentPane2 = new JPanel();
      contentPane2.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane2);
      contentPane2.setLayout(null);
      
      ridt = new JTextField();
      ridt.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent e) {
            if(ridt.getText().equals("ID"))
               ridt.setText("");
         }
         @Override
         public void focusLost(FocusEvent e) {
            if(ridt.getText().equals(""))
               ridt.setText("ID");
         }
      });
      ridt.setText("ID");
      ridt.setBounds(12, 10, 116, 21);
      contentPane2.add(ridt);
      ridt.setColumns(10);
      
      rnamet = new JTextField();
      rnamet.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent e) {
            if(rnamet.getText().equals("이름"))
               rnamet.setText("");
         }
         @Override
         public void focusLost(FocusEvent e) {
            if(rnamet.getText().equals(""))
               rnamet.setText("이름");
         }
      });
      rnamet.setText("이름");
      rnamet.setBounds(12, 35, 116, 21);
      contentPane2.add(rnamet);
      rnamet.setColumns(10);
      
      JButton rcheck = new JButton("중복");
      rcheck.setBackground(SystemColor.control);
      rcheck.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
            conn = dbconnect.dbconn();
            sql = "select COUNT(*) from member where id = '" + ridt.getText() + "'";  
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            
            rs.next();
            String iid = rs.getString(1);
            
            if(ridt.getText().equals("") || rnamet.getText().equals("") || ridt.getText().equals("ID") || rnamet.getText().equals("이름")) {
               JOptionPane.showMessageDialog(null, "ID 혹은 이름을 다시 입력하세요.\n *(ID = ID)불가\n*(이름 = 이름)불가", "ID 혹은 이름 다시 입력", JOptionPane.ERROR_MESSAGE);
            }
            else if(iid.equals("1")) {
               JOptionPane.showMessageDialog(null, "이미 존재하는 ID입니다 !\n다시 입력해주세요", "이미 존재하는 ID", JOptionPane.ERROR_MESSAGE);
            }
            else {
               JOptionPane.showMessageDialog(null, "사용할 수 있는 ID 입니다.", "사용할 수 있는 ID", JOptionPane.INFORMATION_MESSAGE);
            }
            }catch (Exception ex1) {
               System.out.println(ex1.getMessage());
            }
         }
      });
      rcheck.setBounds(140, 10, 97, 21);
      contentPane2.add(rcheck);
      
      JButton rregisb = new JButton("회원가입");
      rregisb.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               conn = dbconnect.dbconn();
               sql = "select COUNT(*) from member where id = '" + ridt.getText() + "'";  
               st = conn.createStatement();
               rs = st.executeQuery(sql);
               
               rs.next();
               String iid = rs.getString(1);
               
               if(iid.equals("1")) {
                  JOptionPane.showMessageDialog(null, "이미 존재하는 ID입니다 !\nID를 다시 입력해주세요", "이미 존재하는 ID", JOptionPane.ERROR_MESSAGE);
               }
               if(ridt.getText().equals("") || rnamet.getText().equals("") || ridt.getText().equals("ID") || rnamet.getText().equals("이름")) {
                  JOptionPane.showMessageDialog(null, "ID와 이름 모두 써주세요.", "ID 혹은 이름에 공백 입력", JOptionPane.ERROR_MESSAGE);
               }
               else {
               sql = "insert into member values (?, ?)";
               pstmt = conn.prepareStatement(sql);
                  pstmt.setString(1, ridt.getText());
                  pstmt.setString(2, rnamet.getText());
                  int r = pstmt.executeUpdate();
                  JOptionPane.showMessageDialog(null, "회원가입이 완료 되셨습니다 !", "회원가입 완료", JOptionPane.INFORMATION_MESSAGE);
                  Thread.sleep(750);
                  dispose();
               }
            }catch(Exception ex2) {
               System.out.println(ex2.getMessage());
            } 
         }
      });
      rregisb.setBackground(SystemColor.control);
      rregisb.setBounds(77, 64, 100, 25);
      contentPane2.add(rregisb);
   }
}
