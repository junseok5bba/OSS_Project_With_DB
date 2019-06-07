package kr.ac.sunmoon;

import java.awt.BorderLayout;
import java.util.*;
import java.util.Map.Entry;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

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
      setBounds(100, 100, 301, 160);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
                  contentPane.setLayout(null);
            
                  JLabel engl = new JLabel();
                  engl.setHorizontalAlignment(SwingConstants.CENTER);
                  engl.setBounds(0, 18, 289, 38);
                  engl.setFont(new Font("굴림", Font.BOLD, 20));
                  contentPane.add(engl);

                  SimpleAttributeSet ecenter = new SimpleAttributeSet();
                  StyleConstants.setAlignment(ecenter, StyleConstants.ALIGN_CENTER);

                  
                  
            JLabel korl = new JLabel();
            korl.setHorizontalAlignment(SwingConstants.CENTER);
            korl.setBounds(0, 58, 289, 38);
            korl.setFont(new Font("굴림", Font.BOLD, 20));
            contentPane.add(korl);

            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);


      ArrayList<String> eng = new ArrayList<>();
      ArrayList<String> kor = new ArrayList<>();

      addWindowListener(new WindowAdapter() {
         @Override
         public void windowActivated(WindowEvent e) {
            try {
               conn = dbconnect.dbconn();
               stmt = conn.createStatement();
               String usevocabulary = "use vocabulary";
               stmt.executeUpdate(usevocabulary);

               String showword = "select * from " + tablename;
               rs = stmt.executeQuery(showword);

               while (rs.next()) {
                  eng.add(rs.getString(2));
                  kor.add(rs.getString(3));
               }

               Timer timer;
               TimerTask timerTask;

               timer = new Timer();
               timerTask = new TimerTask() {
                  int x = 0;

                  public void run() {
                     engl.setText(eng.get(x));
                     korl.setText(kor.get(x));
                     x++;
                     if (x >= eng.size())
                        x = 0;
                  }

               };
               timer.schedule(timerTask, 500, 3000);
            } catch (Exception d) {
               System.out.println(d.getMessage());
            }
         }
      });
   }

}
