package javaproject;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterPanel extends JPanel implements ActionListener
{
	private JTextField idTxt = new JTextField();
	private JComboBox<Integer> ageBox = new JComboBox<Integer>();
	private JComboBox<String> sexBox = new JComboBox<String>();
	private JComboBox<String> eduBox = new JComboBox<String>();
	private JTextField nameTxt = new JTextField();
	private JPasswordField pwTxt = new JPasswordField();
	private JButton regBtn = new JButton("회원가입");
	private JButton cancelBtn = new JButton("취소");

	private JLabel idLabel = new JLabel("ID : ");
	private JLabel ageLabel = new JLabel("Age : ");
	private JLabel sexLabel = new JLabel("성별 : ");
	private JLabel pwLabel = new JLabel("PW : ");
	private JLabel nameLabel = new JLabel("이름 : ");
	private JLabel eduLabel = new JLabel("학력 : ");


	private int age;
	private int sex;
	private int edu;
	private mainFrame win;

	public RegisterPanel(mainFrame win)
	{

		this.setLayout(new BorderLayout());
		idLabel.setHorizontalAlignment(JLabel.CENTER);
		ageLabel.setHorizontalAlignment(JLabel.CENTER);
		sexLabel.setHorizontalAlignment(JLabel.CENTER);
		pwLabel.setHorizontalAlignment(JLabel.CENTER);
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		eduLabel.setHorizontalAlignment(JLabel.CENTER);
		this.win = win;
		JPanel pan = new JPanel(new GridLayout(7, 2, 30, 70));
		setLayout(new BorderLayout());
		regBtn.setSize(70, 20);
		regBtn.setLocation(10, 10);
		initval();
		initComboBox();
		pan.add(idLabel);
		pan.add(idTxt);
		pan.add(pwLabel);
		pan.add(pwTxt);
		pan.add(nameLabel);
		pan.add(nameTxt);
		pan.add(ageLabel);
		pan.add(ageBox);
		pan.add(sexLabel);
		pan.add(sexBox);
		pan.add(eduLabel);
		pan.add(eduBox);
		pan.add(regBtn);
		pan.add(cancelBtn);
		regBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
		

		Component com1 = Box.createRigidArea(new Dimension(70,70));
		Component com2 = Box.createRigidArea(new Dimension(70,70));
		Component com3 = Box.createRigidArea(new Dimension(150,150));
		Component com4 = Box.createRigidArea(new Dimension(150,150));
		this.add(com3,BorderLayout.EAST);
		this.add(com4,BorderLayout.WEST);
		this.add(com2,BorderLayout.NORTH);
		this.add(com1,BorderLayout.SOUTH);
		this.add(pan,BorderLayout.CENTER);
	}

	private void initval()
	{
		// TODO Auto-generated method stub
		age = 13;
		edu = 1;
		sex = 1;
	}

	private void initComboBox()
	{
		for (int i = 13; i < 70; i++)
		{
			ageBox.addItem((Integer) i);
		}
		sexBox.addItem((String) "남자");
		sexBox.addItem((String) "여자");

		eduBox.addItem((String) "중졸");
		eduBox.addItem((String) "고졸");
		eduBox.addItem((String) "대졸");
		ageBox.addActionListener(this);
		sexBox.addActionListener(this);
		eduBox.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object obj = e.getSource();
		if (obj == regBtn)
		{
			String tmp = new String(pwTxt.getPassword());
			Connection con;
			try
			{
				con = mainFrame.getConnection();

				Statement myStmt = con.createStatement();
				String sql = "insert into user " + " (id, pw, name, age, sex, edu)" + " values ('" + idTxt.getText()
						+ "', '" + tmp + "', '" + nameTxt.getText() + "', '" + age + "', '" + sex + "', '" + edu+ "')";
				myStmt.executeUpdate(sql);

			} catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(win, "회원가입이 완료되었습니다.");

			win.changePanel("loginpanel");
			initCompo();
		} else if (obj ==cancelBtn)
		{
			win.changePanel("loginpanel");
			initCompo();
		
		}
		else if (obj == eduBox)
		{
			String tmp;
			tmp = (String) eduBox.getSelectedItem();
			if (tmp.equals("중졸"))
			{
				edu = 1;
			} else if (tmp.equals("고졸"))
			{
				edu = 2;
			} else if (tmp.equals("대졸"))
			{
				edu = 3;
			}
		} else if (obj == sexBox)
		{
			String tmp;
			tmp = (String) sexBox.getSelectedItem();
			if (tmp.equals("남자"))
			{
				sex = 1;
			} else if (tmp.equals("여자"))
			{
				sex = 2;
			}
		} else if (obj == ageBox)
		{
			age = (int) ageBox.getSelectedItem();
		}
	}

	public void initCompo()
	{
		idTxt.setText("");
		ageBox.setSelectedIndex(0);
		sexBox.setSelectedIndex(0);
		eduBox.setSelectedIndex(0);
		nameTxt.setText("");
		pwTxt.setText("");
	}
}
