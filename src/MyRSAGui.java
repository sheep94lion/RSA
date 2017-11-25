import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyRSAGui {
    JFrame f;
    JComboBox cb;
    JLabel l1;
    JButton btGenerateKey, btEncrypt, btDecrypt;
    JTextArea areaE, areaN, areaD, areaP, areaQ, areaMsg, areaEMsg, areaDMsg;
    MyRSA myRSA;
    String encryptLevels[] = {"RSA-1024", "RSA-512", "RSA-256", "RSA-128", "RSA-64", "RSA-2048"};
    int encryptLevelsI[] = {512, 256, 128, 64, 32, 1024};
    public MyRSAGui() {
        f = new JFrame("RSA");
        cb = new JComboBox(encryptLevels);
        cb.setBounds(110, 20, 90, 30);
        f.add(cb);

        l1 = new JLabel("Encryption Level:");
        l1.setBounds(10, 20, 100, 30);
        f.add(l1);

        btGenerateKey = new JButton("Generate Key");
        btGenerateKey.setBounds(210, 20, 120, 30);
        btGenerateKey.addActionListener(new BtGenerateKeyAction());
        f.add(btGenerateKey);

        btEncrypt = new JButton("Encrypt Message");
        btEncrypt.setBounds(410, 20, 150, 30);
        btEncrypt.addActionListener(new BtEncryptAction());
        f.add(btEncrypt);

        btDecrypt = new JButton("Decrypt Message");
        btDecrypt.setBounds(600, 20, 150, 30);
        btDecrypt.addActionListener(new BtDecryptAction());
        f.add(btDecrypt);

        JLabel lN = new JLabel("Public: N");
        lN.setBounds(10, 70, 80, 20);
        f.add(lN);
        areaN = new JTextArea();
        areaN.setEditable (true); // set textArea non-editable
        areaN.setLineWrap(true);
        JScrollPane scrollN = new JScrollPane ( areaN );
        scrollN.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        scrollN.setBounds(10, 100, 320, 80);
        f.add(scrollN);
        JLabel lE = new JLabel("Public: E");
        lE.setBounds(10, 190, 80, 20);
        f.add(lE);
        areaE = new JTextArea();
        areaE.setEditable (true); // set textArea non-editable
        areaE.setLineWrap(true);
        JScrollPane scrollE = new JScrollPane ( areaE );
        scrollE.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        scrollE.setBounds(10, 220, 320, 80);
        f.add(scrollE);
        JLabel lD = new JLabel("Private: D");
        lD.setBounds(10, 310, 80, 20);
        f.add(lD);
        areaD = new JTextArea();
        areaD.setEditable (true); // set textArea non-editable
        areaD.setLineWrap(true);
        JScrollPane scrollD = new JScrollPane ( areaD );
        scrollD.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        scrollD.setBounds(10, 340, 320, 80);
        f.add(scrollD);
        JLabel lP = new JLabel("Private: P");
        lP.setBounds(10, 430, 80, 20);
        f.add(lP);
        areaP = new JTextArea();
        areaP.setEditable (true); // set textArea non-editable
        areaP.setLineWrap(true);
        JScrollPane scrollP = new JScrollPane ( areaP );
        scrollP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        scrollP.setBounds(10, 460, 320, 80);
        f.add(scrollP);
        JLabel lQ = new JLabel("Private: Q");
        lQ.setBounds(10, 550, 80, 20);
        f.add(lQ);
        areaQ = new JTextArea();
        areaQ.setEditable (true); // set textArea non-editable
        areaQ.setLineWrap(true);
        JScrollPane scrollQ = new JScrollPane ( areaQ );
        scrollQ.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        scrollQ.setBounds(10, 580, 320, 80);
        f.add(scrollQ);

        JLabel lMsg = new JLabel("Message:");
        lMsg.setBounds(410, 70, 80, 20);
        f.add(lMsg);
        areaMsg = new JTextArea();
        areaMsg.setEditable (true); // set textArea non-editable
        areaMsg.setLineWrap(true);
        JScrollPane scrollMsg = new JScrollPane ( areaMsg );
        scrollMsg.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        scrollMsg.setBounds(410, 100, 320, 80);
        f.add(scrollMsg);

        JLabel lEMsg = new JLabel("Encypted Message:");
        lEMsg.setBounds(410, 190, 130, 20);
        f.add(lEMsg);
        areaEMsg = new JTextArea();
        areaEMsg.setEditable (true); // set textArea non-editable
        areaEMsg.setLineWrap(true);
        JScrollPane scrollEMsg = new JScrollPane ( areaEMsg );
        scrollEMsg.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        scrollEMsg.setBounds(410, 220, 320, 80);
        f.add(scrollEMsg);

        JLabel lDMsg = new JLabel("Decrypted Message:");
        lDMsg.setBounds(410, 310, 130, 20);
        f.add(lDMsg);
        areaDMsg = new JTextArea();
        areaDMsg.setEditable (true); // set textArea non-editable
        areaDMsg.setLineWrap(true);
        JScrollPane scrollDMsg = new JScrollPane ( areaDMsg );
        scrollDMsg.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        scrollDMsg.setBounds(410, 340, 320, 80);
        f.add(scrollDMsg);

        f.setLayout(null);
        f.setSize(800, 800);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private class BtGenerateKeyAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            myRSA = new MyRSA(encryptLevelsI[cb.getSelectedIndex()]);
            //System.out.println(encryptLevelsI[cb.getSelectedIndex()]);
            areaD.setText(myRSA.getDString());
            areaE.setText(myRSA.getEString());
            areaN.setText(myRSA.getNString());
            areaP.setText(myRSA.getPString());
            areaQ.setText(myRSA.getQString());
        }
    }

    private class BtEncryptAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            byte[] d = MyRSA.hexStringToByteArray(areaD.getText());
            byte[] e = MyRSA.hexStringToByteArray(areaE.getText());
            byte[] n = MyRSA.hexStringToByteArray(areaN.getText());
            byte[] p = MyRSA.hexStringToByteArray(areaP.getText());
            byte[] q = MyRSA.hexStringToByteArray(areaQ.getText());
            myRSA = new MyRSA(new MyBigInteger(e), new MyBigInteger(d), new MyBigInteger(n), new MyBigInteger(p), new MyBigInteger(q));
            String message = areaMsg.getText();
            byte[] eMsg = myRSA.encrypt(message.getBytes());
            areaEMsg.setText(MyRSA.bytesToHex(eMsg));
        }
    }

    private class BtDecryptAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            byte[] d = MyRSA.hexStringToByteArray(areaD.getText());
            byte[] e = MyRSA.hexStringToByteArray(areaE.getText());
            byte[] n = MyRSA.hexStringToByteArray(areaN.getText());
            byte[] p = MyRSA.hexStringToByteArray(areaP.getText());
            byte[] q = MyRSA.hexStringToByteArray(areaQ.getText());
            myRSA = new MyRSA(new MyBigInteger(e), new MyBigInteger(d), new MyBigInteger(n), new MyBigInteger(p), new MyBigInteger(q));
            byte[] eMsgBytes = MyRSA.hexStringToByteArray(areaEMsg.getText());
            areaDMsg.setText(new String(myRSA.decrypt(eMsgBytes)));
        }
    }

    public static void main(String argvs[]) {
        new MyRSAGui();
    }
}
