package javaproject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.SyndFeedOutput;

public class MainPanel extends JPanel implements ActionListener, MouseListener
{
	int hour_salary_index=0;
	int day_salary_index=0;
	int week_salary_index=0;
	int month_salary_index=0;
	int year_salary_index=0;
	int none_salary_index=0;
	// 직종 종류 가지는 배열
	JobKindInfo[] ageList = new JobKindInfo[4];
	// HashMap<String,String> kind_userId=new HashMap<String,String>();
	// HashMap<String,Integer> userId_age=new HashMap<String,Integer>();
	ArrayList<String> kindList = new ArrayList<String>(); // 직종 리스트
	ArrayList<String> userIdList = new ArrayList<String>(); // 사용자ID 리스트
	ArrayList<Integer> userAgeList = new ArrayList<Integer>(); // 사용자 나이 리스트
	ArrayList<Integer> userSexList = new ArrayList<Integer>(); // 사용자 나이 리스트

	JScrollPane jscrollPane;
	JScrollPane jscrollPane2;
	JTable table = new JTable();
	JTable table2 = new JTable();
	String[] columnNames =
	{ "근무기업명", "모집내용", "학력", "연령,성별", "급여", "마감일" };
	String[] columnNames2 =
		{ "근무기업명","평점", "작성자","작성일시","내용"};
	DefaultTableModel model;
	DefaultTableModel model2;
	final GeocoderExample mapView = new GeocoderExample();

	Connection con = null;
	ArrayList<Job> list = new ArrayList<Job>();
	ArrayList<Comment> clist = new ArrayList<Comment>();
	HashMap<String, String> locationHM = new HashMap<String, String>();
	HashMap<String, HashMap<String, String>> specificLocationHM = new HashMap<String, HashMap<String, String>>();
	HashMap<String, String> kindHM = new HashMap<String, String>();
	HashMap<String, HashMap<String, String>> specificKindHM = new HashMap<String, HashMap<String, String>>();

	JComboBox<String> location_combo = new JComboBox<>();
	JComboBox<String> location_specific_combo = new JComboBox<>();
	JComboBox<String> kind_combo = new JComboBox<>();
	JComboBox<String> kind_specific_combo = new JComboBox<>();
	JButton searchBtn = new JButton("검색");
	JButton favorBtn = new JButton("관심 목록");

	JButton ageBtn = new JButton("나이별");
	JButton sexBtn = new JButton("성별별");

	String url = "http://www.albamon.com/rss/rss_list.asp?scd=";
	boolean check = false;

	Panel panel = new Panel();
	Panel selectPanel = new Panel();

	Panel panel1 = new Panel();
	Panel userPanel = new Panel();

	private JButton lgotBtn = new JButton("로그아웃");
	private mainFrame win;

	JLabel nameLabel = new JLabel();
	JLabel ageLabel = new JLabel();
	JLabel sexLabel = new JLabel();
	JLabel eduLabel = new JLabel();

	public MainPanel(mainFrame win)
	{


		addCommentList();
		this.win = win;
		SetuLabel();
		setLayout(new GridLayout(4, 1, 20, 20));
		initilize(); // combo에 값들 채워넣기
		setAgeList(); // 나이별 직종 리스트 집어넣기

		BufferedImage myPicture;
		try
		{
			myPicture = ImageIO.read(new File("./img/123.png"));
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		location_combo.addActionListener(this);
		kind_combo.addActionListener(this);
		searchBtn.addActionListener(this);
		favorBtn.addActionListener(this);
		ageBtn.addActionListener(this);
		sexBtn.addActionListener(this);
		lgotBtn.addActionListener(this);

		add(panel);

		setPanel1();
		add(panel1);

		// model=new DefaultTableModel();
		model = new NonEditableTableModel();
		model2 = new NonEditableTableModel();
		model.setColumnIdentifiers(columnNames);
		model2.setColumnIdentifiers(columnNames2);
		table.setModel(model);
		table2.setModel(model2);
		table.addMouseListener(this);
		table2.addMouseListener(this);
		jscrollPane = new JScrollPane(table);
		jscrollPane2 = new JScrollPane(table2);

		makeTable2(clist);
		add(jscrollPane);
		add(mapView);
		setPanel();

	}

	public void SetuLabel()
	{
		// String a = win.getuSex();
		nameLabel.setText("이름 : " + win.getuName());
		ageLabel.setText("나이 : " + win.getuAge());
		sexLabel.setText("성별 : " + win.getuSex());
		eduLabel.setText("학력 : " + win.getuEdu());
	}

	private void setuserPanel()
	{
		// TODO Auto-generated method stub
		userPanel.setLayout(new GridLayout(4, 1, 20, 20));
		userPanel.add(nameLabel);
		userPanel.add(ageLabel);
		userPanel.add(sexLabel);
		userPanel.add(eduLabel);
	}

	public void initilize()
	{

		addLocation();
		addSpecificLocation();
		// 지역 이름들 넣기
		Set keys = locationHM.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext())
		{
			location_combo.addItem((String) it.next());
		}

		addKind();
		addSpecificKind();
		// 상세 지역 이름들 넣기
		Set keys3 = kindHM.keySet();
		Iterator it3 = keys3.iterator();
		while (it3.hasNext())
		{
			kind_combo.addItem((String) it3.next());
		}
	}

	public void setPanel()
	{
		panel.setLayout(new GridLayout(1, 3, 20, 20));

		setuserPanel();
		panel.add(userPanel);

		model2.setColumnIdentifiers(columnNames2);
		table2.setModel(model2);
		table2.addMouseListener(this);
		jscrollPane2 = new JScrollPane(table2);
		panel.add(jscrollPane2);
		setselectPanel();
		panel.add(selectPanel);
		
	}

	public void setselectPanel()
	{
		selectPanel.setLayout(new GridLayout(3, 1, 15, 15));
		selectPanel.add(ageBtn);
		selectPanel.add(sexBtn);
		selectPanel.add(lgotBtn);
	}

	public void setPanel1()
	{
		panel1.setLayout(new GridLayout(1, 6, 20, 20));
		panel1.add(location_combo);
		panel1.add(location_specific_combo);
		panel1.add(kind_combo);
		panel1.add(kind_specific_combo);
		panel1.add(searchBtn);
		panel1.add(favorBtn);
	}

	public void addLocation()
	{
		locationHM.put("서울특별시", "I000");
		locationHM.put("광주광역시", "E000");
		locationHM.put("대구광역시", "F000");
		locationHM.put("대전광역시", "G000");
		locationHM.put("부산광역시", "H000");
		locationHM.put("울산광역시", "J000");
		locationHM.put("인천광역시", "K000");
		locationHM.put("세종특별자치시", "1000");
		locationHM.put("경기도", "B000");
		locationHM.put("강원도", "A000");
		locationHM.put("경상남도", "C000");
		locationHM.put("경상북도", "D000");
		locationHM.put("전라남도", "L000");
		locationHM.put("전라북도", "M000");
		locationHM.put("충청남도", "O000");
		locationHM.put("충청북도", "P000");
		locationHM.put("제주특별자치도", "N000");
	}

	public void addSpecificLocation()
	{
		// 서울 상세지역 정보 넣기
		String[] temp =
		{ "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구",
				"성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구", "전체" };
		String[] value =
		{ "I010", "I020", "I030", "I040", "I050", "I060", "I070", "I080", "I090", "I100", "I110", "I120", "I130",
				"I140", "I150", "I160", "I170", "I180", "I190", "I200", "I210", "I220", "I230", "I240", "I250",
				"I000" };
		HashMap<String, String> hM = new HashMap<String, String>();
		for (int i = 0; i < temp.length; i++)
		{
			hM.put(temp[i], value[i]);
		}
		specificLocationHM.put("서울특별시", hM);
		// 광주광역시
		String[] temp2 =
		{ "광산구", "남구", "동구", "북구", "서구", "전체" };
		String[] value2 =
		{ "E010", "E020", "E030", "E040", "E050", "E000" };
		HashMap<String, String> hM2 = new HashMap<String, String>();
		for (int i = 0; i < temp2.length; i++)
		{
			hM2.put(temp2[i], value2[i]);
		}
		specificLocationHM.put("광주광역시", hM2);
		// 대구광역시
		String[] temp3 =
		{ "남구", "달서구", "달성군", "동구", "북구", "서구", "수성구", "중구", "전체" };
		String[] value3 =
		{ "F010", "F020", "F030", "F040", "F050", "F060", "F070", "F080", "F000" };
		HashMap<String, String> hM3 = new HashMap<String, String>();
		for (int i = 0; i < temp3.length; i++)
		{
			hM3.put(temp3[i], value3[i]);
		}
		specificLocationHM.put("대구광역시", hM3);
		// 대전광역시
		String[] temp4 =
		{ "대덕구", "동구", "서구", "유성구", "중구", "전체" };
		String[] value4 =
		{ "G010", "G020", "G030", "G040", "G050", "G000" };
		HashMap<String, String> hM4 = new HashMap<String, String>();
		for (int i = 0; i < temp4.length; i++)
		{
			hM4.put(temp4[i], value4[i]);
		}
		specificLocationHM.put("대전광역시", hM4);
		// 부산광역시
		String[] temp5 =
		{ "강서구", "금정구", "기장군", "남구", "동구", "동래구", "부산진구", "북구", "사상구", "사하구", "서구", "수영구", "연제구", "영도구", "중구", "해운대구",
				"전체" };
		String[] value5 =
		{ "H010", "H020", "H030", "H040", "H050", "H060", "H070", "H080", "H090", "H100", "H110", "H120", "H130",
				"H140", "H150", "H160", "H000" };
		HashMap<String, String> hM5 = new HashMap<String, String>();
		for (int i = 0; i < temp5.length; i++)
		{
			hM5.put(temp5[i], value5[i]);
		}
		specificLocationHM.put("부산광역시", hM5);
		// 울산광역시
		String[] temp6 =
		{ "남구", "동구", "북구", "울주군", "중구", "전체" };
		String[] value6 =
		{ "J010", "J020", "J030", "J040", "J050", "J000" };
		HashMap<String, String> hM6 = new HashMap<String, String>();
		for (int i = 0; i < temp6.length; i++)
		{
			hM6.put(temp6[i], value6[i]);
		}
		specificLocationHM.put("울산광역시", hM6);
		// 인천광역시
		String[] temp7 =
		{ "강화군", "계양구", "남구", "남동구", "동구", "부평구", "서구", "연수구", "옹진군", "중구", "전체" };
		String[] value7 =
		{ "K010", "K020", "K030", "K040", "K050", "K060", "K070", "K080", "K090", "K100", "K000" };
		HashMap<String, String> hM7 = new HashMap<String, String>();
		for (int i = 0; i < temp7.length; i++)
		{
			hM7.put(temp7[i], value7[i]);
		}
		specificLocationHM.put("인천광역시", hM7);
		// 세종특별자치시
		String[] temp8 =
		{ "세종시" };
		String[] value8 =
		{ "1010", "1000" };
		HashMap<String, String> hM8 = new HashMap<String, String>();
		for (int i = 0; i < temp8.length; i++)
		{
			hM8.put(temp8[i], value8[i]);
		}
		specificLocationHM.put("세종특별자치시", hM8);
		// 경기도
		String[] temp9 =
		{ "가평군", "고양시 덕양구", "고양시 일산동구", "고양시 일산서구", "과천시", "광명시", "광주시", "구리시", "군포시", "김포시", "남양주시", "동두천시", "부천시",
				"성남시 분당구", "성남시 수정구", "성남시 중원구", "수원시 권선구", "수원시 영통구", "수원시 장안구", "수원시 팔달구", "시흥시", "안산시 단원구",
				"안산시 상록구", "안성시", "안양시 동안구", "안양시 만안구", "양주시", "양평군", "여주시", "연천군", "오산시", "용인시 기흥구", "용인시 수지구",
				"용인시 처인구", "의왕시", "의정부시", "이천시", "파주시", "평택시", "포천시", "하남시", "화성시", "전체" };
		String[] value9 =
		{ "B010", "B020", "B030", "B031", "B040", "B050", "B060", "B070", "B080", "B090", "B100", "B110", "B125",
				"B150", "B160", "B170", "B180", "B201", "B190", "B200", "B210", "B220", "B221", "B230", "B240", "B250",
				"B260", "B270", "B280", "B290", "B300", "B310", "B311", "B312", "B320", "B330", "B340", "B350", "B360",
				"B370", "B380", "B390", "B000" };
		HashMap<String, String> hM9 = new HashMap<String, String>();
		for (int i = 0; i < temp9.length; i++)
		{
			hM9.put(temp9[i], value9[i]);
		}
		specificLocationHM.put("경기도", hM9);
		// 강원도
		String[] temp10 =
		{ "강릉시", "고성군", "동해시", "삼척시", "속초시", "양구군", "양양군", "영월군", "원주시", "인제군", "정선군", "철원군", "춘천시", "태백시", "평창군",
				"홍천군", "화천군", "횡성군", "전체" };
		String[] value10 =
		{ "A010", "A020", "A030", "A040", "A050", "A060", "A070", "A080", "A090", "A100", "A110", "A120", "A130",
				"A140", "A150", "A160", "A170", "A180", "A000" };
		HashMap<String, String> hM10 = new HashMap<String, String>();
		for (int i = 0; i < temp10.length; i++)
		{
			hM10.put(temp10[i], value10[i]);
		}
		specificLocationHM.put("강원도", hM10);
		// 경상남도
		String[] temp11 =
		{ "거제시", "거창군", "고성군", "김해시", "남해군", "밀양시", "사천시", "산청군", "양산시", "의령군", "진주시", "창녕군", "창원시 마산합포구", "창원시 마산회원구",
				"창원시 성산구", "창원시 의창구", "창원시 진해구", "통영시", "하동군", "함안군", "함양군", "합천군", "전체" };
		String[] value11 =
		{ "C010", "C020", "C030", "C040", "C050", "C080", "C090", "C100", "C110", "C120", "C130", "C150", "C160",
				"C162", "C164", "C166", "C168", "C170", "C180", "C190", "C200", "C210", "C000" };
		HashMap<String, String> hM11 = new HashMap<String, String>();
		for (int i = 0; i < temp11.length; i++)
		{
			hM11.put(temp11[i], value11[i]);
		}
		specificLocationHM.put("경상남도", hM11);
		// 경상북도
		String[] temp12 =
		{ "경산시", "경주시", "고령군", "구미시", "군위군", "김천시", "문경시", "봉화군", "상주시", "성주군", "안동시", "영덕군", "영양군", "영주시", "영천시",
				"예천군", "울릉군", "울진군", "의성군", "청도군", "청송군", "칠곡군", "포항시 남구", "포항시 북구", "전체" };
		String[] value12 =
		{ "D010", "D020", "D030", "D040", "D050", "D060", "D070", "D080", "D090", "D100", "D110", "D120", "D130",
				"D140", "D150", "D160", "D170", "D180", "D190", "D200", "D210", "D220", "D230", "D240", "D000" };
		HashMap<String, String> hM12 = new HashMap<String, String>();
		for (int i = 0; i < temp12.length; i++)
		{
			hM12.put(temp12[i], value12[i]);
		}
		specificLocationHM.put("경상북도", hM12);
		// 전라남도
		String[] temp13 =
		{ "강진군", "고흥군", "곡성군", "광양시", "구례군", "나주시", "담양군", "목포시", "무안군", "보성군", "순천시", "신안군", "여수시", "영광군", "영암군",
				"완도군", "장성군", "장흥군", "진도군", "함평군", "해남군", "화순군", "전체" };
		String[] value13 =
		{ "L010", "L020", "L030", "L040", "L050", "L060", "L070", "L080", "L090", "L100", "L110", "L120", "L130",
				"L140", "L150", "L160", "L170", "L180", "L190", "L200", "L210", "L220", "L000" };
		HashMap<String, String> hM13 = new HashMap<String, String>();
		for (int i = 0; i < temp13.length; i++)
		{
			hM13.put(temp13[i], value13[i]);
		}
		specificLocationHM.put("전라남도", hM13);
		// 전라북도
		String[] temp14 =
		{ "고창군", "군산시", "김제시", "남원시", "무주군", "부안군", "순창군", "완주군", "익산시", "임실군", "장수군", "전주시 덕진구", "전주시 완산구", "정읍시",
				"진안군", "전체" };
		String[] value14 =
		{ "M010", "M020", "M030", "M040", "M050", "M060", "M070", "M080", "M090", "M100", "M110", "M120", "M130",
				"M140", "M150", "M000" };
		HashMap<String, String> hM14 = new HashMap<String, String>();
		for (int i = 0; i < temp14.length; i++)
		{
			hM14.put(temp14[i], value14[i]);
		}
		specificLocationHM.put("전라북도", hM14);
		// 충청남도
		String[] temp15 =
		{ "공주시", "금산군", "논산시", "당진시", "보령시", "부여군", "서산시", "서천군", "아산시", "예산군", "천안시 동남구", "천안시 서북구", "청양군", "태안군",
				"홍성군", "계룡시", "전체" };
		String[] value15 =
		{ "M010", "M020", "M030", "M040", "M050", "M060", "M070", "M080", "M090", "M100", "M110", "M120", "M130",
				"M140", "M150", "M160", "M000" };
		HashMap<String, String> hM15 = new HashMap<String, String>();
		for (int i = 0; i < temp15.length; i++)
		{
			hM15.put(temp15[i], value15[i]);
		}
		specificLocationHM.put("충청남도", hM15);
		// 충청북도
		String[] temp16 =
		{ "괴산군", "단양군", "보은군", "영동군", "옥천군", "음성군", "제천시", "진천군", "청주시 청원구", "청주시 상당구", "청주시 흥덕구", "충주시", "증평군",
				"청주시 서원구", "전체" };
		String[] value16 =
		{ "M010", "M020", "M030", "M040", "M050", "M060", "M070", "M080", "M090", "M100", "M110", "M120", "M130",
				"M140", "M000" };
		HashMap<String, String> hM16 = new HashMap<String, String>();
		for (int i = 0; i < temp16.length; i++)
		{
			hM16.put(temp16[i], value16[i]);
		}
		specificLocationHM.put("충청북도", hM16);
		// 제주특별자치도
		String[] temp17 =
		{ "서귀포시", "제주시", "전체" };
		String[] value17 =
		{ "N030", "N040", "N000" };
		HashMap<String, String> hM17 = new HashMap<String, String>();
		for (int i = 0; i < temp17.length; i++)
		{
			hM17.put(temp17[i], value17[i]);
		}
		specificLocationHM.put("제주특별자치도", hM17);
	}

	public void addKind()
	{
		String[] temp =
		{ "외식,음료", "유통,판매", "문화,여가,생활", "서비스", "사무직", "고객상담,리서치,영업", "생산,건설,운송", "IT,컴퓨터", "교육,강사", "디자인", "미디어" };
		String[] value =
		{ "1000", "2000", "3000", "4000", "6000", "7000", "8000", "9000", "A000", "B000", "C000" };
		for (int i = 0; i < temp.length; i++)
		{
			kindHM.put(temp[i], value[i]);
		}
	}

	public void addSpecificKind()
	{
		// 외식,음료
		String[] temp =
		{ "일반음식점", "레스토랑", "패밀리레스토랑", "패스트푸드점", "치킨전문점", "커피전문점", "아이스크림,디저트", "베이커리,도넛", "호프,일반주점", "바(bar)",
				"급식,푸드시스템", "외식,음료 기타" };
		String[] value =
		{ "1010", "1020", "1030", "1040", "1050", "1060", "1070", "1080", "1090", "1100", "1110", "1990" };
		HashMap<String, String> hM = new HashMap<String, String>();
		for (int i = 0; i < temp.length; i++)
		{
			hM.put(temp[i], value[i]);
		}
		specificKindHM.put("외식,음료", hM);
		// 유통,판매
		String[] temp2 =
		{ "백화점,면세점", "유통점,마트", "편의점", "의류,잡화매장", "뷰티,헬스스토어", "휴대폰,전자기기매장", "가구,침구,생활소품", "서점,문구,팬시", "약국", "농수산,청과,축산",
				"유통,판매 기타" };
		String[] value2 =
		{ "2010", "2020", "2030", "2050", "2060", "2070", "2080", "2090", "2100", "2110", "2990" };
		HashMap<String, String> hM2 = new HashMap<String, String>();
		for (int i = 0; i < temp2.length; i++)
		{
			hM2.put(temp2[i], value2[i]);
		}
		specificKindHM.put("유통,판매", hM2);
		// 문화,여가,생활
		String[] temp3 =
		{ "놀이공원,테마파크", "호텔,리조트,숙박", "여행,캠프,레포츠", "영화,공연", "전시,컨벤션,세미나", "스터디룸,독서실,고시원", "PC방", "노래방", "볼링,당구장", "스크린골프",
				"도서,DVD대여점", "DVD방,멀티방,만화카페", "오락실,게임장", "찜질방,사우나,스파", "휘트니스,스포츠", "공인중개", "골프캐디", "고속도로휴게소",
				"문화,여가,생활,기타" };
		String[] value3 =
		{ "3010", "3030", "3040", "3043", "3046", "3050", "3060", "3070", "3080", "3090", "3100", "3110", "3120",
				"3130", "3140", "3160", "3170", "3180", "3990" };
		HashMap<String, String> hM3 = new HashMap<String, String>();
		for (int i = 0; i < temp3.length; i++)
		{
			hM3.put(temp3[i], value3[i]);
		}
		specificKindHM.put("문화,여가,생활", hM3);
		// 서비스
		String[] temp4 =
		{ "매장관리,판매", "캐셔,카운터", "서빙", "주방,조리", "바리스타", "안내데스크", "주차관리,주차도우미", "보안,경비,경호", "주유,세차", "배달", "전단지배포",
				"청소,미화", "헤어,피부,미용", "병원,간호,의료", "애완,동물병원", "베이비시터,가사도우미", "결혼,연회,장례도우미", "판촉도우미", "이벤트,행사스텝", "나레이터모델",
				"피팅모델", "사진촬영,편집", "서비스 기타" };
		String[] value4 =
		{ "4010", "4020", "4030", "4040", "4060", "4070", "4090", "4095", "4100", "4110", "4120", "4130", "4140",
				"4150", "4160", "4170", "4180", "4190", "4200", "4210", "4220", "4230", "4990" };
		HashMap<String, String> hM4 = new HashMap<String, String>();
		for (int i = 0; i < temp4.length; i++)
		{
			hM4.put(temp4[i], value4[i]);
		}
		specificKindHM.put("서비스", hM4);
		// 사무직
		String[] temp5 =
		{ "사무보조,비서", "경리,재무,회계", "인사,총무", "마케팅,광고,홍보", "실험,연구보조", "번역,통역", "편집,교정,교열", "공공기관,공기업,협회", "학교,도서관,교육기관",
				"사무직 기타" };
		String[] value5 =
		{ "6010", "6020", "6030", "6033", "6036", "6040", "6050", "6060", "6070", "6080", "6990" };
		HashMap<String, String> hM5 = new HashMap<String, String>();
		for (int i = 0; i < temp5.length; i++)
		{
			hM5.put(temp5[i], value5[i]);
		}
		specificKindHM.put("사무직", hM5);
		// 고객상담,리서치,영업
		String[] temp6 =
		{ "고객상담,인바운드", "텔레마케팅,아웃바운드", "영업,세일즈", "설문조사,리서치", "영업관리,지워", "고객상담,리서치,영업 기타" };
		String[] value6 =
		{ "7010", "7020", "7030", "7040", "7050", "7990" };
		HashMap<String, String> hM6 = new HashMap<String, String>();
		for (int i = 0; i < temp6.length; i++)
		{
			hM6.put(temp6[i], value6[i]);
		}
		specificKindHM.put("고객상담,리서치,영업", hM6);
		// 생산,건설,운송
		String[] temp7 =
		{ "공사,건설,시공", "생산,제조,품질검사", "정비,수리,설치,A/S", "포장,선별,분류", "입출고,물류,창고관리", "PVC", "운송,이사", "운전,대리운전", "택배,퀵서비스",
				"화물,중장비,특수차", "조선소", "생산,건설,운송 기타" };
		String[] value7 =
		{ "8010", "8020", "8030", "8040", "8050", "8055", "8060", "8070", "8080", "8090", "8100", "8990" };
		HashMap<String, String> hM7 = new HashMap<String, String>();
		for (int i = 0; i < temp7.length; i++)
		{
			hM7.put(temp7[i], value7[i]);
		}
		specificKindHM.put("생산,건설,운송", hM7);
		// IT,컴퓨터
		String[] temp8 =
		{ "웹,모바일기획", "사이트,컨텐츠운영", "쇼핑몰,소셜커머스운영", "바이럴,SNS마케팅", "게임운영(GM)", "프로그래머", "QA,테스터,검증", "시스템,네트워크,보안",
				"컴퓨터A/S", "IT,컴퓨터 기타" };
		String[] value8 =
		{ "9005", "9010", "9013", "9016", "9020", "9060", "9063", "9066", "9070", "9990" };
		HashMap<String, String> hM8 = new HashMap<String, String>();
		for (int i = 0; i < temp8.length; i++)
		{
			hM8.put(temp8[i], value8[i]);
		}
		specificKindHM.put("IT,컴퓨터", hM8);
		// 교육,강사
		String[] temp9 =
		{ "입시,보습학원", "외국어,어학원", "컴퓨터,정보통신", "예체능,레포츠", "유아,유치원", "방문,학습지", "보조교사", "자격증,기술학원", "교육,강사 기타" };
		String[] value9 =
		{ "A010", "A020", "A030", "A040", "A050", "A060", "A070", "A080", "A990" };
		HashMap<String, String> hM9 = new HashMap<String, String>();
		for (int i = 0; i < temp9.length; i++)
		{
			hM9.put(temp9[i], value9[i]);
		}
		specificKindHM.put("교육,강사", hM9);
		// 디자인
		String[] temp10 =
		{ "웹,모바일디자인", "그래픽,편집디자인", "제품,산업디자인", "CAD,CAM,인테리어디자인", "캐릭터,애니메이션디자인", "패션,잡화디자인", "디자인 기타" };
		String[] value10 =
		{ "B010", "B020", "B030", "B040", "B050", "B060", "B990" };
		HashMap<String, String> hM10 = new HashMap<String, String>();
		for (int i = 0; i < temp10.length; i++)
		{
			hM10.put(temp10[i], value10[i]);
		}
		specificKindHM.put("디자인", hM10);
		// 미디어
		String[] temp11 =
		{ "보조출연,방청", "방송스텝,촬영보조", "동영상촬영,편집", "조명,음향", "방송사,프로덕션", "신문,잡지,출판", "미디어 기타" };
		String[] value11 =
		{ "C010", "C020", "C030", "C040", "C050", "C060", "C990" };
		HashMap<String, String> hM11 = new HashMap<String, String>();
		for (int i = 0; i < temp11.length; i++)
		{
			hM11.put(temp11[i], value11[i]);
		}
		specificKindHM.put("미디어", hM11);
	}

	public void setUrl()
	{
		url = "http://www.albamon.com/rss/rss_list.asp?scd=";
		String location = (String) location_combo.getSelectedItem();
		url += locationHM.get(location); // 선택한 지역의 값
		url += "&gcd=";

		String specificLocation = (String) location_specific_combo.getSelectedItem();
		url += specificLocationHM.get(location).get(specificLocation);
		url += "&rbcd=";

		String kind = (String) kind_combo.getSelectedItem();
		url += kindHM.get(kind);
		url += "&rpcd=";

		String specificKind = (String) kind_specific_combo.getSelectedItem();
		url += specificKindHM.get(kind).get(specificKind);
		url += "&gubun=1";

		url += ".rss";
	}

	public void parseXML()
	{
		try
		{
			String feedType = "rss_2.0";
			String fileName = "feed.xml";
			SyndFeed feed2 = new SyndFeedImpl();
			feed2.setFeedType(feedType);

			FeedFetcher fetcher = new HttpURLFeedFetcher();
			SyndFeed feed = fetcher.retrieveFeed(new URL(url));

			for (Object o : feed.getEntries())
			{
				SyndEntry entry = (SyndEntry) o;
				// Description---"+entry.getDescription().getValue());;
				feed2.setTitle(entry.getTitle());
				feed2.setLink(entry.getLink());
				feed2.setDescription(entry.getDescription().getValue());

				/*
				 * SyndEntry entry2; SyndContent description;
				 * 
				 * entry2=new SyndEntryImpl(); entry2.setTitle(); entry2.setLink("exam");
				 * description=new SyndContentImpl(); description.setValue("Cool");
				 * entry.setDescription(description); entries.add(entry);
				 * feed.setEntries(entries);
				 */

			}

			Writer writer = new FileWriter(fileName);
			SyndFeedOutput output = new SyndFeedOutput();
			output.output(feed, writer);
			writer.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				addMarker(list);
			}
		});
		// TODO Auto-generated method stub
		Object o = e.getSource();
		if (o == favorBtn)
		{
			try
			{
				con = mainFrame.getConnection();
				Statement myStmt = con.createStatement();
				ResultSet myRs = myStmt.executeQuery("select * from favorjob");
				list.clear();
				deleteTable();
				while (myRs.next())
				{
					if (win.getuID().equals(myRs.getString("userid")))
					{

						Job job = new Job(myRs.getString("title"), myRs.getString("link"), myRs.getString("name"),
								myRs.getString("content"), myRs.getString("kind"), myRs.getString("location"),
								myRs.getString("subway"), myRs.getString("education"), myRs.getString("age_sex"),
								myRs.getString("salary"), myRs.getString("end"));
						list.add(job);
					}
				}
				
			
	            saveSalaryKind();
				makeTable(list);
				t.start();
				// addMarker(list);
			} catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (o == location_combo)
		{
			String item = (String) location_combo.getSelectedItem();

			int size = specificLocationHM.get(item).size();
			String[] temp = new String[size];
			Set keys = specificLocationHM.get(item).keySet();
			Iterator it = keys.iterator();
			int i = 0;
			while (it.hasNext())
			{
				temp[i] = (String) it.next();
				i++;
			}
			location_specific_combo.setModel(new DefaultComboBoxModel<>(temp));
		} else if (o == kind_combo)
		{
			String item = (String) kind_combo.getSelectedItem();

			int size = specificKindHM.get(item).size();
			String[] temp = new String[size];
			Set keys = specificKindHM.get(item).keySet();
			Iterator it = keys.iterator();
			int i = 0;
			while (it.hasNext())
			{
				temp[i] = (String) it.next();
				i++;
			}
			kind_specific_combo.setModel(new DefaultComboBoxModel<>(temp));
		} else if (o == searchBtn)
		{
			setUrl(); // 선택된사항들로 url 만들기
			parseXML();
			try
			{
				parseXmlFile();
			} catch (ParserConfigurationException | SAXException | IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// addJobInfo();
			
            saveSalaryKind();
			makeTable(list);
			t.start();
		}
		if (o == ageBtn)
		{
			System.out.println("bb");
			kindList.clear();
			userIdList.clear();
			userAgeList.clear();
			try
			{
				System.out.println("cc");
				con = mainFrame.getConnection();
				Statement myStmt = con.createStatement();
				ResultSet myRs = myStmt.executeQuery("select * from favorjob");
				while (myRs.next())
				{

					System.out.println("dd");
					String[] j = myRs.getString("kind").split(",");
					for (int i = 0; i < j.length; i++)
					{
						kindList.add(j[i]);
						userIdList.add(myRs.getString("userid"));
					}
				}
				myRs.close();

				con.close();
			} catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try
			{
				System.out.println("ee");
				System.out.println(userIdList.size());
				con = mainFrame.getConnection();
				for (int i = 0; i < userIdList.size(); i++)
				{
					Statement myStmt = con.createStatement();
					ResultSet myRs = myStmt.executeQuery(
							"select age from user where id = " + "'" + userIdList.get(i).toString() + "'");
					while (myRs.next())
					{
						userAgeList.add(myRs.getInt("age"));
						System.out.println(userAgeList.get(0).toString());
						System.out.println("aa");
					}
					myRs.close();
				}

				con.close();
			} catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			createAgeDialog(); // 다이얼로그 생성 나이별 선호 직종 확인 가능
		}
		if (o == sexBtn)
		{
			kindList.clear();
			userIdList.clear();
			userSexList.clear();
			try
			{
				con = mainFrame.getConnection();
				Statement myStmt = con.createStatement();
				ResultSet myRs = myStmt.executeQuery("select kind, userid from favorjob");
				while (myRs.next())
				{
					String[] j = myRs.getString("kind").split(",");
					for (int i = 0; i < j.length; i++)
					{
						kindList.add(j[i]);
						userIdList.add(myRs.getString("userid"));
					}

				}
				myRs.close();

				for (int i = 0; i < userIdList.size(); i++)
				{
					myRs = myStmt.executeQuery(
							"select sex from user where id = " + "'" + userIdList.get(i).toString() + "'");
					while (myRs.next())
					{
						userSexList.add(myRs.getInt("sex"));
					}
					myRs.close();
				}

				myRs.close();

				con.close();
			} catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			createSexDialog();
		}
		if (o == lgotBtn)
		{
			win.setuID("");
			win.setuName("");
			win.setuAge("");
			win.setuSex("");
			win.setuEdu("");
			win.changePanel("loginpanel");
		}
	}

	private void createSexDialog()
	{
		HashMap<String, Integer> mRank = new HashMap<String, Integer>();
		HashMap<String, Integer> wRank = new HashMap<String, Integer>();
		mRank.clear();
		wRank.clear();

		String mTxt = "";
		String wTxt = "";
		final JDialog dlg = new JDialog();
		dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		System.out.println("포문 전");
		for (int i = 0; i < userSexList.size(); i++)
		{
			if (userSexList.get(i) == 1)
			{//
				// For man
				if (mRank.get(kindList.get(i)) == null)
				{
					mRank.put(kindList.get(i), 1);
				} else
				{
					mRank.put(kindList.get(i), mRank.get(kindList.get(i)) + 1);
				}
			} else
			{
				// For woman
				if (wRank.get(kindList.get(i)) == null)
				{
					wRank.put(kindList.get(i), 1);
				} else
				{
					wRank.put(kindList.get(i), wRank.get(kindList.get(i)) + 1);
				}
			}
		}
		System.out.println("포문 후");
		int mMax = 0;
		int wMax = 0;
		
		System.out.println("포ㅇㅇ");
		Set entrySet = mRank.entrySet();
		Set entrySet2 = wRank.entrySet();
		Iterator it = entrySet.iterator();
		Iterator it2 = entrySet2.iterator();

		System.out.println("남자");
		while (it.hasNext())
		{
			Map.Entry me = (Map.Entry) it.next();
			if ((Integer)me.getValue()>mMax)
			{
				mTxt = (String) me.getKey();
				mMax = (Integer) me.getValue();
			}
			System.out.println("Key is: " + me.getKey() + " & " + " value is: " + me.getValue());
		}
		System.out.println("여자");
		while (it2.hasNext())
		{
			Map.Entry me2 = (Map.Entry) it2.next();
			if ((Integer)me2.getValue()>wMax)
			{
				wTxt = (String) me2.getKey();
				wMax = (Integer) me2.getValue();
			}
			System.out.println("Key is: " + me2.getKey() + " & " + " value is: " + me2.getValue());
		}

		// Obtaining an iterator for the entry set

		JLabel mLabel = new JLabel("남자 : ");
		JLabel wLabel = new JLabel("여자 : ");

		dlg.setTitle("성별 선호 직종");
		dlg.setLayout(new GridLayout(2, 2, 30, 30));
		
		mLabel.setText(mLabel.getText() + mTxt);
		wLabel.setText(wLabel.getText() + wTxt);
		
		dlg.add(mLabel);
		dlg.add(wLabel);

		dlg.setSize(300, 300);
		dlg.setVisible(true);
	}

	public void createAgeDialog()
	{

		ArrayList<HashMap<String, Integer>> mRank = new ArrayList<HashMap<String, Integer>>();
		mRank.add(new HashMap<String, Integer>());
		mRank.add(new HashMap<String, Integer>());
		mRank.add(new HashMap<String, Integer>());
		mRank.add(new HashMap<String, Integer>());

		for (int i = 0; i < userAgeList.size(); i++)
		{
			System.out.println("AgeFor");
			if (userAgeList.get(i) > 9 && userAgeList.get(i) < 20)
			{
				System.out.println("if1");
				if (mRank.get(0).get(kindList.get(i)) == null)
				{
					System.out.println("if2");
					mRank.get(0).put(kindList.get(i), 1);
					System.out.println(mRank.get(0).get(kindList.get(i)));
				} else
				{
					mRank.get(0).put(kindList.get(i), mRank.get(0).get(kindList.get(i)) + 1);
				}
			} else if (userAgeList.get(i) > 19 && userAgeList.get(i) < 30)
			{
				if (mRank.get(1).get(kindList.get(i)) == null)
				{
					mRank.get(1).put(kindList.get(i), 1);
				} else
				{
					mRank.get(1).put(kindList.get(i), mRank.get(1).get(kindList.get(i)) + 1);
				}
			} else if (userAgeList.get(i) > 29 && userAgeList.get(i) < 40)
			{
				if (mRank.get(2).get(kindList.get(i)) == null)
				{
					mRank.get(2).put(kindList.get(i), 1);
				} else
				{
					mRank.get(2).put(kindList.get(i), mRank.get(2).get(kindList.get(i)) + 1);
				}
			} else
			{
				if (mRank.get(3).get(kindList.get(i)) == null)
				{
					mRank.get(3).put(kindList.get(i), 1);
				} else
				{
					mRank.get(3).put(kindList.get(i), mRank.get(3).get(kindList.get(i)) + 1);
				}
			}
		}

		String m1 = "";
		String m2 = "";
		String m3 = "";
		String m4 = "";

		int ma1 = 0;
		int ma2 = 0;
		int ma3 = 0;
		int ma4 = 0;

		String maidx1 = "";
		String maidx2 = "";
		String maidx3 = "";
		String maidx4 = "";

		Set entrySet = mRank.get(0).entrySet();
		Set entrySet2 = mRank.get(1).entrySet();
		Set entrySet3 = mRank.get(2).entrySet();
		Set entrySet4 = mRank.get(3).entrySet();
		Iterator it = entrySet.iterator();
		Iterator it2 = entrySet2.iterator();
		Iterator it3 = entrySet3.iterator();
		Iterator it4 = entrySet4.iterator();

		System.out.println("10대");
		while (it.hasNext())
		{
			Map.Entry me = (Map.Entry) it.next();
			System.out.println("Key is: " + me.getKey() + " & " + " value is: " + me.getValue());
			if (ma1 < (Integer) me.getValue())
			{
				ma1 = (Integer) me.getValue();
				maidx1 = (String) me.getKey();
			}

		}

		System.out.println("20대");
		while (it2.hasNext())
		{
			Map.Entry me2 = (Map.Entry) it2.next();
			System.out.println("Key is: " + me2.getKey() + " & " + " value is: " + me2.getValue());
			if (ma2 < (Integer) me2.getValue())
			{
				ma2 = (Integer) me2.getValue();
				maidx2 = (String) me2.getKey();
			}
		}

		System.out.println("30대");
		while (it3.hasNext())
		{
			Map.Entry me3 = (Map.Entry) it3.next();
			System.out.println("Key is: " + me3.getKey() + " & " + " value is: " + me3.getValue());
			if (ma3 < (Integer) me3.getValue())
			{
				ma3 = (Integer) me3.getValue();
				maidx3 = (String) me3.getKey();
			}
		}

		System.out.println("40대");
		while (it4.hasNext())
		{
			Map.Entry me4 = (Map.Entry) it4.next();
			System.out.println("Key is: " + me4.getKey() + " & " + " value is: " + me4.getValue());
			if (ma4 < (Integer) me4.getValue())
			{
				ma4 = (Integer) me4.getValue();
				maidx4 = (String) me4.getKey();
			}
		}

		final JDialog dlg = new JDialog();

		dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		JLabel label10 = new JLabel("10대");

		JLabel txt10 = new JLabel(maidx1);

		JLabel label20 = new JLabel("20대");

		JLabel txt20 = new JLabel(maidx2);

		JLabel label30 = new JLabel("30대");

		JLabel txt30 = new JLabel(maidx3);

		JLabel label40 = new JLabel("40대");

		JLabel txt40 = new JLabel(maidx4);

		System.out.println("나이별 선호 직종");
		dlg.setTitle("나이별 선호 직종");
		dlg.setLayout(new GridLayout(4, 2, 20, 20));
		dlg.add(label10);
		dlg.add(txt10);

		dlg.add(label20);
		dlg.add(txt20);

		dlg.add(label30);
		dlg.add(txt30);
		dlg.add(label40);
		dlg.add(txt40);

		dlg.setSize(500, 500);
		dlg.setVisible(true);
	}

	public void setAgeList()
	{
		for (int i = 0; i < 4; i++)
		{
			ageList[i] = new JobKindInfo();
			Set key = specificKindHM.keySet(); // 업직종 키값들
			Iterator it = key.iterator();
			while (it.hasNext())
			{
				Set key2 = specificKindHM.get(it.next()).keySet(); // 상세 직종 키값들
				Iterator it2 = key2.iterator();

				while (it2.hasNext())
				{
					String temp = it2.next().toString();
					ageList[i].getList().add(temp); // 상세 직종 키값들 ArrayList<String>에 저장
				}
			}
		}
	}

	private void addMarker(ArrayList<Job> list2)
	{
		Random random = new Random();
		String Geocoded = "";
		for (int i = 0; i < list2.size(); i++)
		{
			try
			{
				Thread.sleep(1000 - random.nextInt(50));
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Geocoded = ParseURL.getData(list2.get(i).getLink());
			mapView.performGeocode(Geocoded, list2.get(i));
		}
	}

	public void parseXmlFile() throws ParserConfigurationException, SAXException, IOException
	{
		list.clear();// list 초기화해주기
		String fileUrl = "./feed.xml";
		// DOM Document 객체 생성하기 위한 메소드
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

		// DOM 파서로부터 입력받은 파일을 파싱하도록 요청
		DocumentBuilder parser = f.newDocumentBuilder();
		Document xmlDoc = null;
		xmlDoc = parser.parse(fileUrl);

		// 루트 엘리먼트 접근
		Element root = xmlDoc.getDocumentElement();
		NodeList n1 = root.getElementsByTagName("item");

		// 하위 엘리먼트 접근
		for (int i = 0; i < n1.getLength(); i++)
		{
			Node itemNode = n1.item(i);
			Element itemElement = (Element) itemNode;
			String title = itemElement.getElementsByTagName("title").item(0).getTextContent();
			String link = itemElement.getElementsByTagName("link").item(0).getTextContent();
			String description = itemElement.getElementsByTagName("description").item(0).getTextContent();

			int index1 = description.indexOf("모집내용");
			String name = description.substring(10, index1 - 10); // 근무기업명

			int index2 = description.indexOf("직종");
			String content = description.substring(index1 + 6, index2 - 10); // 모집내용

			int index3 = description.indexOf("지역");
			String kind = description.substring(index2 + 4, index3 - 10);

			int index4 = description.indexOf("지하철역");
			String location = description.substring(index3 + 4, index4 - 10);

			int index5 = description.indexOf("학력");
			String subway = description.substring(index4 + 6, index5 - 10);

			int index6 = description.indexOf("연령·성별");
			String education = description.substring(index5 + 4, index6 - 10);

			int index7 = description.indexOf("급여");
			String age_sex = description.substring(index6 + 7, index7 - 10);

			int index8 = description.indexOf("마감일");
			String salary = description.substring(index7 + 4, index8 - 10);

			int index9 = description.indexOf("<a");
			String end = description.substring(index8 + 5, index9 - 10);

			Job job = new Job(title, link, name, content, kind, location, subway, education, age_sex, salary, end);
			job.printinfo();
			list.add(job);
		}
	}

	public void makeTable(ArrayList<Job> list2)
	{
		// String[] columnNames={"근무기업명","모집내용","학력","연령,성별","급여","마감일"};
		Object[][] data = new Object[list2.size()][6];

		deleteTable();
		for (int i = 0; i < list2.size(); i++)
		{
			data[i][0] = list2.get(i).getName(); // Job 근무기업명
			data[i][1] = list2.get(i).getContent(); // Job 모집내용
			data[i][2] = list2.get(i).getEducation(); // 학력
			data[i][3] = list2.get(i).getAge_sex(); // 연령,성별
			data[i][4] = list2.get(i).getSalary(); // 급여
			data[i][5] = list2.get(i).getEnd(); // 마감일
			model.addRow(data[i]);
		}
		System.out.println(table.getValueAt(1, 1));
		MyTableCellRenderer renderer = new MyTableCellRenderer();
		
	    table.setDefaultRenderer(Object.class, renderer);
	}
	public class MyTableCellRenderer extends DefaultTableCellRenderer
	{
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

              Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, 0, 1);
        
              if (!isSelected) 
              {
            	  if(row==0)
            	  {
            		  cell.setBackground(Color.RED);
            	  }
            	  else if(row==hour_salary_index)
            	  {
            		  cell.setBackground(Color.RED);
            	  }
            	  else if(row==(hour_salary_index+day_salary_index))
            	  {
            		  cell.setBackground(Color.RED);
            	  }
            	  else if(row==(hour_salary_index+day_salary_index+week_salary_index))
            	  {
            		  cell.setBackground(Color.RED);
            	  }
            	  else if(row==(hour_salary_index+day_salary_index+week_salary_index+month_salary_index))
            	  {
            		  cell.setBackground(Color.RED);
            	  }
            	  else if(row==(hour_salary_index+day_salary_index+week_salary_index+month_salary_index+year_salary_index))
            	  {
            		  cell.setBackground(Color.RED);
            	  }
            	 
            	  else
            	  {
            		  cell.setBackground(Color.WHITE);
            	  }
              }
			
              return this;
        }
	}
	public void addCommentList()
	{
		try
		{
			Connection con = mainFrame.getConnection();
			Statement myStmt = con.createStatement();
			ResultSet myRs = myStmt.executeQuery("select * from comment");
			while(myRs.next())
			{
				clist.add(new Comment(myRs.getString("company"), myRs.getString("userid"), myRs.getString("time"), myRs.getString("comment"), myRs.getInt("rating")));
			}
		} catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	public void makeTable2(ArrayList<Comment> clist2)
	{

		
		Object[][] data = new Object[clist2.size()][5];
		deleteTable2();
		for (int i = 0; i < clist2.size(); i++)
		{
			data[i][0] = clist2.get(i).getCompany(); // Job 근무기업명
			data[i][1] = clist2.get(i).getRating(); // Job 근무기업명
			data[i][2] = clist2.get(i).getUserid(); // Job 모집내용
			data[i][3] = clist2.get(i).getTime(); // 학력
			data[i][4] = clist2.get(i).getComment(); // 연령,성별
			model2.addRow(data[i]);
		}
		
	
	}

	private void deleteTable()
	{
		if (model.getRowCount() > 0)
		{
			for (int i = model.getRowCount() - 1; i > -1; i--)
			{
				model.removeRow(i);
			}
		}
	}	
	private void deleteTable2()
	{
		if(model2==null)
		{
			return;
		}
		if (model2.getRowCount() > 0)
		{
			for (int i = model2.getRowCount() - 1; i > -1; i--)
			{
				model2.removeRow(i);
			}
		}
	}

	public void createDialog(int index) // 선택했을때 다이얼로그 창 띄어주기
	{
		final JDialog dlg = new JDialog();
		try
		{
			con = mainFrame.getConnection();
		} catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JLabel nameLabel = new JLabel("근무기업명");
		JLabel nameText = new JLabel(list.get(index).getName());

		JLabel contentLabel = new JLabel("모집내용");
		JLabel contentText = new JLabel(list.get(index).getContent());

		JLabel educationLabel = new JLabel("학력");
		JLabel educationText = new JLabel(list.get(index).getEducation());

		JLabel age_sexLabel = new JLabel("연령,성별");
		JLabel age_sexText = new JLabel(list.get(index).getAge_sex());

		JLabel salaryLabel = new JLabel("급여");
		JLabel salaryText = new JLabel(list.get(index).getSalary());

		JLabel endLabel = new JLabel("마감일");
		JLabel endText = new JLabel(list.get(index).getEnd());

		JButton cancelBtn = new JButton("취소");
		JButton selectBtn = new JButton("찜하기");
		JButton commentBtn = new JButton("코멘트작성");

		dlg.setTitle("Job 정보");
		dlg.setLayout(new GridLayout(8, 2, 20, 20));
		dlg.add(nameLabel);
		dlg.add(nameText);

		dlg.add(contentLabel);
		dlg.add(contentText);

		dlg.add(educationLabel);
		dlg.add(educationText);

		dlg.add(age_sexLabel);
		dlg.add(age_sexText);

		dlg.add(salaryLabel);
		dlg.add(salaryText);

		dlg.add(endLabel);
		dlg.add(endText);

		dlg.add(cancelBtn);
		dlg.add(selectBtn);
		dlg.add(commentBtn);

		dlg.setSize(500, 500);
		dlg.setVisible(true);
		cancelBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO Auto-generated method stub
				dlg.dispose();
			}

		});
		selectBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				list.get(index).addDB(win.getuID(), con);
				JOptionPane.showMessageDialog(win, "관심목록에 추가되었습니다.");
				dlg.dispose();
			}
		});

		commentBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CommentDialog a = new CommentDialog(win.getuName(), nameText.getText());
				dlg.dispose();

				clist.clear();
				addCommentList();
				deleteTable2();
				makeTable2(clist);
			}
		});
	}

	public void TableChange()
	{
		clist.clear();
		addCommentList();
		deleteTable2();
		makeTable2(clist);
	}


	public void createDialog2(int index) // 선택했을때 다이얼로그 창 띄어주기
	{
		final JDialog dlg = new JDialog();
		
		JLabel nameLabel = new JLabel("근무기업명");
		JLabel nameText = new JLabel(clist.get(index).getCompany());

		JLabel contentLabel = new JLabel("작성자");
		JLabel contentText = new JLabel(clist.get(index).getUserid());

		JLabel educationLabel = new JLabel("작성시간");
		JLabel educationText = new JLabel(clist.get(index).getTime());

		JLabel age_sexLabel = new JLabel("내용");
		JLabel age_sexText = new JLabel(clist.get(index).getComment());

		JButton cancelBtn = new JButton("취소");

		dlg.setTitle("Job 정보");
		dlg.setLayout(new GridLayout(8, 2, 20, 20));
		dlg.add(nameLabel);
		dlg.add(nameText);

		dlg.add(contentLabel);
		dlg.add(contentText);

		dlg.add(educationLabel);
		dlg.add(educationText);

		dlg.add(age_sexLabel);
		dlg.add(age_sexText);

		dlg.add(cancelBtn);

		dlg.setSize(500, 500);
		dlg.setVisible(true);
		cancelBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO Auto-generated method stub
				dlg.dispose();
			}

		});
	}
	
	
	
	
	
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub
		// 더블클릭했을때
		Object obj = e.getSource();
		if (obj == table)
		{
			if (e.getClickCount() == 2)
			{
				int row = table.getSelectedRow();
				createDialog(row);
			}
		} 
		else if (obj == table2)
		{

			if (e.getClickCount() == 1)
			{
				TableChange();
			}
			if (e.getClickCount() == 2)
			{
				int row = table2.getSelectedRow();
				createDialog2(row);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		Object obj = e.getSource();
		if (obj == table2)
		{
				TableChange();
		}
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public class NonEditableTableModel extends DefaultTableModel
	{
		@Override
		public boolean isCellEditable(int row, int column)
		{
			return false;
		}
	}
	
	public void sortJobList(ArrayList<Job> li)
	   {
	      comparator comp=new comparator();
	      Collections.sort(li,comp);
	   }
	public class comparator implements java.util.Comparator<Job>
	   {
	      @Override
	      public int compare(Job o1, Job o2) {
	         // TODO Auto-generated method stub
	         if(o1.getSalary().compareTo(o2.getSalary())<0)   //o1<o2
	         {
	            return 1;
	         }
	         else if(o1.getSalary().compareTo(o2.getSalary())>0)
	         {
	            return -1;
	         }
	         else
	         {
	            return 0;
	         }
	      }
	   }
	public void saveSalaryKind()      //여기서 순서대로 list에 넣어준다.
	   {
	      ArrayList<Job> hourSalary=new ArrayList<Job>();   //시급
	      ArrayList<Job> daySalary=new ArrayList<Job>();   //일급
	      ArrayList<Job> weekSalary=new ArrayList<Job>();	//주급
	      ArrayList<Job> monthSalary=new ArrayList<Job>();//월급
	      ArrayList<Job> yearSalary=new ArrayList<Job>();   //연봉
	      ArrayList<Job> noneSalary=new ArrayList<Job>();   //추후협의
	     
	      for(int i=0; i<list.size(); i++)
	      {
	    	  System.out.println(list.get(i).getSalary().substring(1,3));
	         //시급일경우
	         if(list.get(i).getSalary().substring(1,3).equals("시급"))
	         {
	            hourSalary.add(list.get(i));
	         }
	         else if(list.get(i).getSalary().substring(1,3).equals("일급"))
	         {
	            daySalary.add(list.get(i));
	         }
	         else if(list.get(i).getSalary().substring(1,3).equals("추후"))
	         {
	            noneSalary.add(list.get(i));
	         }
	         else if(list.get(i).getSalary().substring(1,3).equals("월급"))
	         {
	            monthSalary.add(list.get(i));
	         }
	         else if(list.get(i).getSalary().substring(1,3).equals("연봉"))
	         {
	            yearSalary.add(list.get(i));
	         }
	         else if(list.get(i).getSalary().substring(1,3).equals("주급"))
	         {
	        	 weekSalary.add(list.get(i));
	         }
	      }
	      
	      sortJobList(hourSalary);
	      sortJobList(daySalary);
	      sortJobList(weekSalary);
	      sortJobList(monthSalary);
	      sortJobList(yearSalary);
	      sortJobList(noneSalary);
	      
	      //kindSalary에 나눠서 저장하고 다시 list에 넣어주자
	      list.clear();
	      for(int i=0; i<hourSalary.size(); i++)
	      {
	         list.add(hourSalary.get(i));
	      }
	      for(int i=0; i<daySalary.size(); i++)
	      {
	         list.add(daySalary.get(i));
	      }
	      for(int i=0; i<weekSalary.size(); i++)
	      {
	         list.add(weekSalary.get(i));
	      }
	      for(int i=0; i<monthSalary.size(); i++)
	      {
	         list.add(monthSalary.get(i));
	      }
	      for(int i=0; i<yearSalary.size(); i++)
	      {
	         list.add(yearSalary.get(i));
	      }
	      for(int i=0; i<noneSalary.size(); i++)
	      {
	         list.add(noneSalary.get(i));
	      }
	      hour_salary_index=hourSalary.size();
	      day_salary_index=daySalary.size();
	      week_salary_index=weekSalary.size();
	      month_salary_index=monthSalary.size();
	      year_salary_index=yearSalary.size();
	      none_salary_index=noneSalary.size();
	      
	      
	   }

}