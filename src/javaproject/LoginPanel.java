package javaproject;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel implements ActionListener
{
	private JTextField idTxt = new JTextField();
	private JPasswordField pwTxt = new JPasswordField();
	private JButton loginBtn = new JButton("로그인");
	private JButton regBtn = new JButton("회원가입");

	private JLabel idLabel = new JLabel("ID : ");
	private JLabel pwLabel = new JLabel("PW : ");
	private JPanel textPanel = new JPanel();
	private mainFrame win;

	public LoginPanel(mainFrame win)
	{
		this.win = win;
		this.setLayout(new BorderLayout());
		loginBtn.addActionListener(this);
		regBtn.addActionListener(this);
		pwTxt.addActionListener(this);
		textPanel.setLayout(new GridLayout(3,2,30 ,30));
		
		idLabel.setHorizontalAlignment(JLabel.CENTER);
		pwLabel.setHorizontalAlignment(JLabel.CENTER);
		
		textPanel.add(idLabel);
		textPanel.add(idTxt);
		textPanel.add(pwLabel);
		textPanel.add(pwTxt);
		textPanel.add(loginBtn);
		textPanel.add(regBtn);

		Component com1 = Box.createRigidArea(new Dimension(300,300));
		Component com2 = Box.createRigidArea(new Dimension(300,300));
		Component com3 = Box.createRigidArea(new Dimension(300,300));
		Component com4 = Box.createRigidArea(new Dimension(500,500));
		this.add(com1,BorderLayout.EAST);
		this.add(com2,BorderLayout.WEST);
		this.add(com3,BorderLayout.NORTH);
		this.add(com4,BorderLayout.SOUTH);
		this.add(textPanel,BorderLayout.CENTER);

	}

	public void initCompo()
	{
		// TODO Auto-generated method stub
		idTxt.setText("");
		pwTxt.setText("");
		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object obj = e.getSource();
		String tmp = new String(pwTxt.getPassword());
		if(obj ==loginBtn || obj== pwTxt)
		{

			try
			{
				Connection con = mainFrame.getConnection();
				Statement myStmt = con.createStatement();
				ResultSet myRs = myStmt.executeQuery("select * from user");
				while(myRs.next())
				{
					if(idTxt.getText().equals(myRs.getString("id"))&&tmp.equals(myRs.getString("pw")))
					{
						win.setuID(myRs.getString("id"));
						win.setuName(myRs.getString("name"));
						win.setuAge(myRs.getString("age"));
						win.setuSex(myRs.getString("sex"));
						win.setuEdu(myRs.getString("edu"));
						
						System.out.println(myRs.getString("edu"));
						System.out.println(win.getuEdu());

						win.mainpanel.SetuLabel();
						win.changePanel("mainpanel");
						return;
					}
				}

				JOptionPane.showMessageDialog(win, "로그인에 실패하였습니다.");
			} catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
		{
			win.changePanel("registerpanel");
		}
		initCompo();
	}

}
