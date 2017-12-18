package javaproject;

import java.awt.BorderLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class CommentDialog extends JDialog implements ActionListener {
   ButtonGroup group = new ButtonGroup();
   ArrayList<JRadioButton> b = new ArrayList<JRadioButton>();

   JButton accept = new JButton("Accept");
   JButton cancel = new JButton("Cancel");
   
   JPanel firstlow = new JPanel();
   JPanel secondlow = new JPanel();
   JTextArea dec = new JTextArea();
   
   String comment;
   int rating;
   String getTime;
   String UserID;
   String company;

   public CommentDialog()
   {
   
   }
   public CommentDialog(String id, String company)
   {
      this.UserID = id;
      this.company = company;
      dec.setText(comment);
      for(int i = 0 ; i<5 ;i++)
      {
         b.add(new JRadioButton(Integer.toString(i+1)));
         group.add(b.get(i));
         firstlow.add(b.get(i));
      }
      initCompo();
      rating = 0;
      accept.addActionListener(this);
      cancel.addActionListener(this);
      
      secondlow.add(accept);
      secondlow.add(cancel);

      this.setLayout(new BorderLayout(15, 15));
      this.add(firstlow, BorderLayout.NORTH);
      this.add(dec, BorderLayout.CENTER);
      this.add(secondlow, BorderLayout.SOUTH);

      this.setVisible(true);
      this.setSize(300, 300);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      Object obj = e.getSource();
      
      if(obj == accept)
      {
         for(int i = 0 ; i<5 ; i++)
         {
            if(b.get(i).isSelected())
            {
               rating = Integer.valueOf(b.get(i).getText());
            }
         }
         comment = dec.getText();
         getTime = CurrentTime.getCurrentTime();

         Connection con;
         try
         {
            con = mainFrame.getConnection();
            Statement myStmt = con.createStatement();
            String sql = "insert into comment " + " (company, userid, time, comment, rating)" + " values ('" + company
                  + "', '" + UserID + "', '" + getTime + "', '" + comment+"', '" + rating+ "')";
            myStmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "완료!");

         } catch (Exception e1)
         {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this, "실패!");
         }
         initCompo();
         this.dispose();
      }
      else
      {
         
      }

   }
   private void initCompo() {

      comment = "";
      b.get(0).setSelected(true);
      
   }

}