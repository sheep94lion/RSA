import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyRSAGui {
    JFrame f;
    JComboBox cb;
    JLabel l1;
    JButton btGenerateKey;
    public MyRSAGui() {
        f = new JFrame("RSA");
        String encryptLevels[] = {"RSA-32", "RSA-64", "RSA-128", "RSA-256", "RSA-512", "RSA-1024"};
        cb = new JComboBox(encryptLevels);
        cb.setBounds(110, 20, 90, 30);
        f.add(cb);

        l1 = new JLabel("Encryption Level:");
        l1.setBounds(10, 20, 100, 30);
        f.add(l1);

        btGenerateKey = new JButton("Generate Key");
        btGenerateKey.setBounds(210, 20, 120, 30);
        f.add(btGenerateKey);

        f.setLayout(null);
        f.setSize(800, 800);
        f.setVisible(true);
    }

    private class BtGenerateKeyAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
        }
    }

    public static void main(String argvs[]) {
        new MyRSAGui();
    }
}
