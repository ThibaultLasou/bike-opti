package graphic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Patrice Camousseigt
 */
public class Window extends JFrame {

    private JPanel container = new JPanel();

    private JComboBox<String> choiceAlgo;

    private JTextField textField1;

    public Window(){
        this.setTitle("Vélib");
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        container.setLayout(new GridBagLayout());
        GridBagConstraints gbc1 = getGbc(0, 0, GridBagConstraints.BOTH);
        GridBagConstraints gbc2 = getGbc(0, 1, GridBagConstraints.BOTH);
        GridBagConstraints gbc3 = getGbc(0, 2, GridBagConstraints.BOTH);

        //Ici, nous changeons juste la façon d'initialiser la JComboBox
        String[] algoString = {"Réduit", "AAC"};
        choiceAlgo = makeJComboBox(algoString);
        JPanel jPanel1 = new JPanel();
        JLabel label1 = new JLabel("Algorithme");
        jPanel1.add(label1);
        jPanel1.add(choiceAlgo);
        container.add(jPanel1, gbc1);

        textField1 = makeJTextField("textField1");
        JPanel jPanel2 = new JPanel();
        JLabel label2 = new JLabel("Entrées");
        jPanel2.add(label2);
        jPanel2.add(textField1);
        container.add(jPanel2, gbc2);

        JButton b = new JButton ("OK");
        b.addActionListener(new BoutonListener());
        JPanel jPanel3 = new JPanel();
        jPanel3.add(b);
        container.add(jPanel3, gbc3);

        this.setContentPane(container);
        this.setVisible(true);
    }

    private JComboBox<String> makeJComboBox(String[] choices) {
        JComboBox<String> combo = new JComboBox<>(choices);

        //Ajout du listener
        combo.setPreferredSize(new Dimension(100, 20));
        combo.setForeground(Color.blue);

        return combo;
    }

    private JTextField makeJTextField(String field) {
        JTextField jtf = new JTextField(field);

        Font police = new Font("Arial", Font.ITALIC, 14);
        jtf.setFont(police);
        jtf.setPreferredSize(new Dimension(150, 30));
        jtf.setForeground(Color.BLUE);

        return jtf;
    }

    private static GridBagConstraints getGbc(int x, int y, int fill) {
        final int I_GAP = 3;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 2.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(I_GAP, I_GAP, I_GAP, I_GAP);
        gbc.fill = fill;

        return gbc;
    }

    private boolean checkUserInput() {
        return true;
    }

    class BoutonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("TEXT : " + choiceAlgo.getSelectedItem().toString());
            System.out.println("TEXT : " + textField1.getText());
        }
    }

}
