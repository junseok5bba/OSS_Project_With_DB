package kr.ac.sunmoon;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class login extends JFrame {

   private JPanel contentPane;
   private JTextField idt;
   private JTextField namet;

   /**
    * Launch the application.
    */
   
   static login frame = new login();
   
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
   
   Connection conn = null;
   Statement st = null;
   ResultSet rs = null;
   String sql = null;
   
   public login() {
      setResizable(false);
      setTitle("Vocabulary - \uB85C\uADF8\uC778");
      
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 265, 135);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);
      
      idt = new JTextField();
      idt.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent e) {
            if(idt.getText().equals("ID"))
               idt.setText("");
         }
         @Override
         public void focusLost(FocusEvent e) {
            if(idt.getText().equals(""))
               idt.setText("ID");
         }
      });
      idt.setText("ID");
      idt.setBounds(12, 10, 116, 21);
      contentPane.add(idt);
      idt.setColumns(10);
      
      namet = new JTextField();
      namet.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent e) {
            if(namet.getText().equals("이름"))
               namet.setText("");
         }
         @Override
         public void focusLost(FocusEvent e) {
            if(namet.getText().equals(""))
               namet.setText("이름");
         }
      });
      namet.setText("\uC774\uB984");
      namet.setBounds(12, 35, 116, 21);
      contentPane.add(namet);
      namet.setColumns(10);
      

      JButton regisb = new JButton("회원가입");
      regisb.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            register regist = new register();
            
            regist.setVisible(true);
         }
      });
      regisb.setBackground(SystemColor.control);
      regisb.setBounds(140, 62, 97, 23);
      contentPane.add(regisb);
      
      JButton loginb = new JButton("로그인");
      loginb.setBackground(SystemColor.control);
      loginb.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               if (idt.getText().equals("") || namet.getText().equals("") || idt.getText().equals("ID") || namet.getText().equals("이름") ) {
                  JOptionPane.showMessageDialog(null, "ID와 이름을 입력해주세요.", "ID와 이름을 입력", JOptionPane.ERROR_MESSAGE);
               } else {
                  conn = dbconnect.dbconn();
                  sql = "select * from member where id = '" + idt.getText() + "'";
                  st = conn.createStatement();
                  rs = st.executeQuery(sql);

                  rs.next();
                  String name = rs.getString(2);
                  
                  String id = idt.getText();

                  if (name.equals(namet.getText())) {
                     Main main = new Main(id);
                     main.setVisible(true);
                     frame.setVisible(false);
                  } else {
                     JOptionPane.showMessageDialog(null, "ID와 이름을 입력해주세요.", "ID와 이름을 입력", JOptionPane.ERROR_MESSAGE);
                  }
               }

            } catch (Exception ex) {
               System.out.println(ex.getMessage());
            }
         }
      });
      loginb.setBounds(140, 10, 97, 46);
      contentPane.add(loginb);
   }
}
