package Main;

import java.awt.*;
import javax.swing.*;

public class Crew extends MainTap {

   private JPanel contentPane;

    public static void main(String[] args) {
        // Create a new instance of CustomFrame
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                   Course frame = new Course();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Crew() {
        // �θ� Ŭ������ ������ ȣ��
        super();


    }

}