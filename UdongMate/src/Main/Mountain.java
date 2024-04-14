package Main;
import java.awt.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import JDBC.*;

public class Mountain extends MainTap {
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                   Mountain frame = new Mountain();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Mountain() {
        super();
        setLocationRelativeTo(null);
         contentPane = new JPanel();
         contentPane.setBorder(new EmptyBorder(100, 100, 100, 100)); // Add padding
         contentPane.setLayout(new BorderLayout(0, 10)); // Set vertical gap to 10


         // Define column names
         String[] columnNames = {"��", "�ڽ� ����", "����", "���̵�"};

         // Create table model
         DefaultTableModel model = new DefaultTableModel(columnNames, 0);

         try {
             Connection con = Jdbc.get();

             // SQL ����
             String sql = "SELECT mountain.mt_name, mountain.mt_dist, address.addr_name, mountain.lev_no FROM mountain JOIN address ON mountain.addr_no = address.addr_no";
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();

             // ����� ���̺� �𵨿� �߰�
             while (rs.next()) {
                 String mtName = rs.getString("mt_name");
                 String addrName = rs.getString("addr_name");
                 int levNo = rs.getInt("lev_no");
                 int mtDist = rs.getInt("mt_dist");
                 double Number = Math.round(mtDist * 10) / 10.0;
                 // ���� ��ȣ�� ���� ���ڿ��� ����
                 String level;
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

                 // ���� �߰��Ͽ� �����ͺ��̽����� ������ ���� ���̺� �𵨿� �߰�
                 model.addRow(new Object[]{mtName, Number + "km", addrName, level});
             }

             rs.close();
             pstmt.close();
             con.close();
         } catch (Exception e) {
             e.printStackTrace();
         }


        // ���̺� ����
        JTable table = new JTable(model);

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
                    String mountainName = (String) table.getValueAt(row, 0);
                    displayGroundDetails(mountainName);
                }
            }
        });
    }


    private void displayGroundDetails(String mountainName) {
        JFrame frame = new JFrame("Details - " + mountainName);
        frame.setSize(800, 450);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.WHITE); // ���� ������� ����

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE); // ���� ������� ����

        try {
            Connection con = Jdbc.get();

            String sql = "SELECT * FROM mountain JOIN address ON mountain.addr_no = address.addr_no WHERE mountain.mt_name = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, mountainName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String mtName = rs.getString("mt_name");
                String addrName = rs.getString("addr_name");
                int levNo = rs.getInt("lev_no");
                int mtDist = rs.getInt("mt_dist");
                String mtImg = rs.getString("mt_img");
                String mtInfo = rs.getString("mt_info");
                double distance = Math.round(mtDist * 10) / 10.0;

                JPanel infoPanel = new JPanel(new BorderLayout());
                infoPanel.setBackground(Color.WHITE); // ���� ������� ����

                DefaultTableModel detailModel = new DefaultTableModel();
                detailModel.addColumn(mtName);
                
                detailModel.addRow(new Object[]{"�ּ� : " + addrName});
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

                JTextArea infoTextArea = new JTextArea(mtInfo);
                infoTextArea.setOpaque(false); // ���� ���� ����
                infoTextArea.setLineWrap(true);
                infoTextArea.setWrapStyleWord(true);
                infoTextArea.setEditable(false);
                infoTextArea.setFont(new Font("Gong Gothic Light", Font.PLAIN, 18)); // ��Ʈ ����

                // infoTextArea�� detailTable ���� ���� ����
                detailScrollPane.setPreferredSize(new Dimension(detailScrollPane.getWidth(), 130));
                infoPanel.add(infoTextArea, BorderLayout.CENTER);

                detailsPanel.add(infoPanel, BorderLayout.CENTER);

                JLabel imgLabel = new JLabel();
                if (mtImg != null && !mtImg.isEmpty()) {
                    ImageIcon icon = new ImageIcon(mtImg);
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
}