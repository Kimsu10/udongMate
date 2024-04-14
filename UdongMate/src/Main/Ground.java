package Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.*;

import JDBC.*;

public class Ground extends MainTap {
    private JPanel contentPane;
    private JComboBox<String> addrDropdown;
    private JComboBox<String> levDropdown;
    private JComboBox<String> cycleDropdown;
    private JComboBox<String> dogDropdown;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Ground frame = new Ground();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Ground() {
        super();
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(100, 100, 100, 100)); // ���� �߰�
        contentPane.setLayout(new BorderLayout(0, 10)); // ���� ���� 10���� ����

        // Create JPanel for checkboxes
        JPanel dropPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 60, 10)); // FlowLayout�� ���� ����� LEADING���� ����

        JPanel addrPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // FlowLayout�� ���� ����� LEADING���� ����
        JLabel addrLabel = new JLabel("����");
        addrPanel.add(addrLabel);
        String addr[] = {"���þ���", "���α�", "�߱�", "��걸", "������", "������", "���빮��", "�߶���", "���ϱ�",
                "���ϱ�", "������", "�����", "����", "���빮��", "������", "��õ��", "������",
                "���α�", "��õ��", "��������", "���۱�", "���Ǳ�", "���ʱ�", "������", "���ı�", "������"};
        addrDropdown = new JComboBox<>(addr);
        addrDropdown.setFont(new Font("Gong Gothic Light", Font.PLAIN, 15));
        addrPanel.add(addrDropdown);

        JPanel levPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // FlowLayout�� ���� ����� LEADING���� ����
        JLabel levLabel = new JLabel("���̵�");
        levPanel.add(levLabel);
        String lev[] = {"���þ���", "��", "��", "��"};
        levDropdown = new JComboBox<>(lev);
        levDropdown.setFont(new Font("Gong Gothic Light", Font.PLAIN, 16));
        levPanel.add(levDropdown);

        JPanel cyclePanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // FlowLayout�� ���� ����� LEADING���� ����
        JLabel cycleLabel = new JLabel("������ �̿�");
        cyclePanel.add(cycleLabel);
        String cycle[] = {"���þ���", "����", "�Ϻκ� ����", "�Ұ���"};
        cycleDropdown = new JComboBox<>(cycle);
        cycleDropdown.setFont(new Font("Gong Gothic Light", Font.PLAIN, 16));
        cyclePanel.add(cycleDropdown);

        JPanel dogPanel = new JPanel(new FlowLayout(FlowLayout.LEADING)); // FlowLayout�� ���� ����� LEADING���� ����
        JLabel dogLabel = new JLabel("�ݷ����� ����");
        dogPanel.add(dogLabel);
        String dog[] = {"���þ���", "����", "�Ұ���"};
        dogDropdown = new JComboBox<>(dog);
        dogDropdown.setFont(new Font("Gong Gothic Light", Font.PLAIN, 16));
        dogPanel.add(dogDropdown);

        dropPanel.add(addrPanel);
        dropPanel.add(levPanel);
        dropPanel.add(cyclePanel);
        dropPanel.add(dogPanel);

        JButton searchButton = new JButton("�˻�");
        searchButton.setPreferredSize(new Dimension(70, 30)); // ��ư ũ�� ����
        dropPanel.add(searchButton); // ��ư �߰�
        searchButton.setFont(new Font("Gong Gothic Light", Font.PLAIN, 19));
        searchButton.setBackground(Color.white);

        JScrollPane checkBoxScrollPane = new JScrollPane(dropPanel);
        contentPane.add(checkBoxScrollPane, BorderLayout.NORTH);

        Font cellFont2 = new Font("Gong Gothic Light", Font.PLAIN, 19);


        for (Component comp : addrPanel.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setFont(cellFont2);
            }
        }


        for (Component comp : levPanel.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setFont(cellFont2);
            }
        }

        for (Component comp : cyclePanel.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setFont(cellFont2);
            }
        }

        for (Component comp : dogPanel.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setFont(cellFont2);
            }
        }
        

        // �� �̸� ����
        String[] columnNames = {"��å/����", "�ڽ� ����", "����", "���̵�", "������", "�ݷ��� ����"};

        // ���̺� �� ����
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                // ���õ� ���ǵ��� �����ɴϴ�.
                String selectedAddr = (String) addrDropdown.getSelectedItem();
                String selectedLev = (String) levDropdown.getSelectedItem(); // ���õ� ���̵� ��������
                String selectedCycle = (String) cycleDropdown.getSelectedItem();
                String selectedDog = (String) dogDropdown.getSelectedItem();

                try {
                    Connection con = Jdbc.get();

                    // SQL ����
                    String sql = "SELECT * FROM ground JOIN address ON ground.addr_no = address.addr_no WHERE ";
                    String s[][] = {
                          {"addr_name = ? "}, {"lev_no = ? "}, {"gr_cycleok = ? "}, {"gr_dogok = ? "}
                    };
                    int c[]=  {0, 0, 0, 0};
                    
                    if(selectedAddr != "���þ���") c[0]++;
                    if(selectedLev != "���þ���") c[1]++;
                    if(selectedCycle != "���þ���") c[2]++;
                    if(selectedDog != "���þ���") c[3]++;

                    int cnt = 0;
                   
                    for(int i = 0;i < 4;i++) {
                       if(c[i] == 1) cnt++;
                    }
                    
                    for(int i = 0;i < 4;i++) {
                       for(int j = 0;j < s[i].length;j++) {
                          if(c[i] == 1) {
                             sql += s[i][j];
                             
                             if(cnt > 1) {
                                  sql += "AND ";
                                  cnt--;
                               }
                             
                          }else break;
                       }
       
                    }
                    
                    
                    sql += "ORDER BY gr_dist ASC";

                    System.out.println(sql);
                    
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    
                    for(int i = 1;i <= 4;i++) {
                       if(c[0] == 1) {
                          pstmt.setString(i, selectedAddr);
                          c[0] = 0;
                          System.out.println("1");
                          continue;
                       }
                       if(c[1] == 1) {
                          pstmt.setInt(i, getLevNo(selectedLev));
                          c[1] = 0;
                          System.out.println("2");
                          continue;
                       }
                       if(c[2] == 1) {
                          pstmt.setInt(i, getCycleOkNum(selectedCycle));
                          c[2] = 0;
                          System.out.println("3");
                          continue;
                       }
                       if(c[3] == 1) {
                          pstmt.setInt(i, getDogOkNum(selectedDog));
                          c[3] = 0;
                          System.out.println("4");
                          continue;
                       }
                    }

                    ResultSet rs = pstmt.executeQuery();

                    // ���� ���̺� �� �ʱ�ȭ
                    model.setRowCount(0);

                    // ����� ���̺� �𵨿� �߰�
                    while (rs.next()) {
                        // ������ �ڵ�� �����մϴ�.
                        String grName = rs.getString("gr_name");
                        String addrName = rs.getString("addr_name");
                        int levNo = rs.getInt("lev_no");
                        double grDist = rs.getDouble("gr_dist");
                        int cycleOK_num = rs.getInt("gr_cycleok");
                        int dogOK_num = rs.getInt("gr_dogok");
                        double Number = Math.round(grDist * 10) / 10.0;
                        // ���� ��ȣ�� ���� ���ڿ� ����
                        String level;
                        String cycleOk;
                        String dogOk;
                        switch (cycleOK_num) {
                            case 0:
                                cycleOk = "�Ұ���";
                                break;
                            case 1:
                                cycleOk = "����";
                                break;
                            case 2:
                                cycleOk = "�Ϻκ� ����";
                                break;
                            default:
                                cycleOk = "�� �� ����";
                        }

                        switch (dogOK_num) {
                            case 0:
                                dogOk = "�Ұ���";
                                break;
                            case 1:
                                dogOk = "����";
                                break;
                            default:
                                dogOk = "�� �� ����";
                        }

                        switch (levNo) {
                            case 1:
                                level = "��";
                                break;
                            case 2:
                                level = "��";
                                break;
                            case 3:
                                level = "��";
                                break;
                            default:
                                level = "�� �� ����";
                        }

                        // �����ͺ��̽����� ������ ���� ���̺� �𵨿� �߰�
                        model.addRow(new Object[]{grName, Number + "km", addrName, level, cycleOk, dogOk});
                    }

                    rs.close();
                    pstmt.close();
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            Connection con = Jdbc.get();

            // SQL ����
            String sql = "SELECT * FROM "
                    + "ground JOIN address ON ground.addr_no = address.addr_no "
                    + "ORDER BY gr_dist ASC";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // ����� ���̺� �𵨿� �߰�
            while (rs.next()) {
                // ������ �ڵ�� �����մϴ�.
                String grName = rs.getString("gr_name");
                String addrName = rs.getString("addr_name");
                int levNo = rs.getInt("lev_no");
                double grDist = rs.getDouble("gr_dist");
                int cycleOK_num = rs.getInt("gr_cycleok");
                int dogOK_num = rs.getInt("gr_dogok");
                double Number = Math.round(grDist * 10) / 10.0;
                // ���� ��ȣ�� ���� ���ڿ� ����
                String level;
                String cycleOk;
                String dogOk;
                switch (cycleOK_num) {
                    case 0:
                        cycleOk = "�Ұ���";
                        break;
                    case 1:
                        cycleOk = "����";
                        break;
                    case 2:
                        cycleOk = "�Ϻκ� ����";
                        break;
                    default:
                        cycleOk = "�� �� ����";
                }

                switch (dogOK_num) {
                    case 0:
                        dogOk = "�Ұ���";
                        break;
                    case 1:
                        dogOk = "����";
                        break;
                    default:
                        dogOk = "�� �� ����";
                }

                switch (levNo) {
                    case 1:
                        level = "��";
                        break;
                    case 2:
                        level = "��";
                        break;
                    case 3:
                        level = "��";
                        break;
                    default:
                        level = "�� �� ����";
                }

                // �����ͺ��̽����� ������ ���� ���̺� �𵨿� �߰�
                model.addRow(new Object[]{grName, Number + "km", addrName, level, cycleOk, dogOk});
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ���̺� ����
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table.getColumnModel().getColumn(0).setPreferredWidth(200); 
        table.getColumnModel().getColumn(1).setPreferredWidth(90); 
        table.getColumnModel().getColumn(2).setPreferredWidth(90); 
        table.getColumnModel().getColumn(3).setPreferredWidth(70); 
        table.getColumnModel().getColumn(4).setPreferredWidth(70); 
        table.getColumnModel().getColumn(5).setPreferredWidth(70);

        Font cellFont = new Font("Gong Gothic Light", Font.PLAIN, 20);
        table.setFont(cellFont);
        table.setRowHeight(30);

        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        table.getTableHeader().setFont(new Font("Gong Gothic Light", Font.PLAIN, 30));
        table.getTableHeader().setBackground(Color.gray);

        // ���̺��� ��輱�� ���� ��ũ�� �гο� �߰�
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // ��ũ�� �г��� ��輱 ����
        contentPane.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ���̺��� ���콺 ������ �߰�
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    // Ŭ�� �̺�Ʈ ó��
                    String groundName = (String) table.getValueAt(row, 0);
                    displayGroundDetails(groundName);
                }
            }
        });
    }

    private void displayGroundDetails(String groundName) {
        JFrame frame = new JFrame("Details - " + groundName);
        frame.setSize(800, 450);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.WHITE); // ���� ������� ����

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE); // ���� ������� ����

        try {
            Connection con = Jdbc.get();

            String sql = "SELECT * FROM ground JOIN address ON ground.addr_no = address.addr_no WHERE ground.gr_name = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, groundName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String grName = rs.getString("gr_name");
                String addrName = rs.getString("addr_name");
                int levNo = rs.getInt("lev_no");
                int grDist = rs.getInt("gr_dist");
                int cycleOK_num = rs.getInt("gr_cycleok");
                int dogOK_num = rs.getInt("gr_dogok");
                String grImg = rs.getString("gr_img");
                String grInfo = rs.getString("gr_info");
                double distance = Math.round(grDist * 10) / 10.0;

                JPanel infoPanel = new JPanel(new BorderLayout());
                infoPanel.setBackground(Color.WHITE); // ���� ������� ����

                DefaultTableModel detailModel = new DefaultTableModel();
                detailModel.addColumn(grName);
                
                detailModel.addRow(new Object[]{"�ּ� : " + addrName});
                detailModel.addRow(new Object[]{"������ ���� ���� : " + (cycleOK_num == 1 ? "����" : "�Ұ���")});
                detailModel.addRow(new Object[]{"�ݷ����� ���� ���� ���� : " + (dogOK_num == 1 ? "����" : "�Ұ���")});
                detailModel.addRow(new Object[]{"�Ÿ� : " + (distance + " km")});
                detailModel.addRow(new Object[]{"�ڽ� �Ұ�"});

                JTable detailTable = new JTable(detailModel);
                detailTable.setFont(new Font("Gong Gothic Light", Font.PLAIN, 20));
                detailTable.setRowHeight(30);
                detailTable.setShowGrid(false); // �׵θ� ���ֱ�
                
                TableCellRenderer headerRenderer = detailTable.getTableHeader().getDefaultRenderer();
                detailTable.getTableHeader().setFont(new Font("Gong Gothic Light", Font.PLAIN, 30));
                detailTable.getTableHeader().setBackground(Color.white);
                JScrollPane detailScrollPane = new JScrollPane(detailTable);
                detailScrollPane.setBorder(BorderFactory.createEmptyBorder());
                detailScrollPane.getViewport().setBackground(new Color(0, 0, 0, 0)); // ���� ���� ����
                infoPanel.add(detailScrollPane, BorderLayout.NORTH);

                JTextArea infoTextArea = new JTextArea(grInfo);
                infoTextArea.setOpaque(false); // ���� ���� ����
                infoTextArea.setLineWrap(true);
                infoTextArea.setWrapStyleWord(true);
                infoTextArea.setEditable(false);
                infoTextArea.setFont(new Font("Gong Gothic Light", Font.PLAIN, 18)); // ��Ʈ ����

                // infoTextArea�� detailTable ���� ���� ����
                detailScrollPane.setPreferredSize(new Dimension(detailScrollPane.getWidth(), 190));
                infoPanel.add(infoTextArea, BorderLayout.CENTER);

                detailsPanel.add(infoPanel, BorderLayout.CENTER);

                JLabel imgLabel = new JLabel();
                if (grImg != null && !grImg.isEmpty()) {
                    ImageIcon icon = new ImageIcon(grImg);
                    Image image = icon.getImage();
                    Image scaledImage = image.getScaledInstance(300, 400, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    imgLabel.setIcon(scaledIcon);
                } else {
                    imgLabel.setText("�̹����� �������� �ʽ��ϴ�.");
                }
                imgLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                detailsPanel.add(imgLabel, BorderLayout.WEST); // �̹����� ���� ��ܿ� �߰�
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame.add(detailsPanel);
        frame.setVisible(true);
    }


    private int getLevNo(String lev) {
        switch (lev) {
            case "��":
                return 1;
            case "��":
                return 2;
            case "��":
                return 3;
            default:
                return 0;
        }
    }

    private int getCycleOkNum(String cycle) {
        switch (cycle) {
            case "�Ұ���":
                return 0;
            case "����":
                return 1;
            case "�Ϻκ� ����":
                return 2;
            default:
                return 0;
        }
    }

    private int getDogOkNum(String dog) {
        switch (dog) {
            case "�Ұ���":
                return 0;
            case "����":
                return 1;
            default:
                return 0;
        }
    }
}