package graphic;


import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Interface extends JFrame {
    private JPanel jpanel_lancement;

    private JButton button_validate;
    private JPanel jpanel_input_user;
    private JPanel jpanel_result_map;
    private JTabbedPane tabbedPane1;
    private JPanel jpanel_parametrage;
    private JPanel jpanel_root;
    private JRadioButton SAARadioButton;
    private JFormattedTextField formattedTextField3;
    private JComboBox comboBox1;
    private JList list1;
    private JList list2;
    private JList list3;
    private JList list4;
    private JPanel jpanel_result_txt;
    private JPanel jpanel_results;
    private JFormattedTextField formattedTextField6;
    private JButton appliquerButton;
    private JFormattedTextField formattedTextField7;
    private JButton appliquerButton1;
    private JButton appliquerButton2;
    private JFormattedTextField formattedTextField8;
    private JButton appliquerButton3;
    private JFormattedTextField formattedTextField9;
    private JRadioButton recuitDeterministeRadioButton;
    private JSlider slider1;
    private JRadioButton radioStochastiqueRadioButton;
    private JTextArea textAreaResultat;

    private final JFXPanel jfxPanel = new JFXPanel();
    private WebEngine engine;

    public Interface() {

        this.setTitle("Vélib");
        //this.setContentPane(jpanel_root);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // =================== ajout du menu ===================

        JMenuBar bar = new JMenuBar();

        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem menuItemNouveau = new JMenuItem("Nouvelle simulation");
        JMenuItem menuItemSauvegarder = new JMenuItem(new AbstractAction("My Menu Item") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jFileChooser = new JFileChooser();
                String path = jFileChooser.getCheminChoisi() + "/" + jFileChooser.NOM_FICHIER_DEFAUT;

                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(path, "UTF-8");
                    writer.println(textAreaResultat.getText());
                    writer.close();
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        menuItemSauvegarder.setText("Sauvegarder résultats");

        JMenuItem menuItemQuitter = new JMenuItem("Quitter");
        menuFichier.add(menuItemNouveau);
        menuFichier.add(menuItemSauvegarder);
        menuFichier.add(menuItemQuitter);

        JMenu menuAide = new JMenu("Aide");

        bar.add(menuFichier);
        bar.add(menuAide);

        this.add(bar, BorderLayout.NORTH);
        this.add(jpanel_root, BorderLayout.CENTER);

        // =================== gestion boutons granularite ===================

        comboBox1.addItem("semaines");
        comboBox1.addItem("mois");

        // =================== gestion boutons choix algo ===================

        recuitDeterministeRadioButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                radioStochastiqueRadioButton.setSelected(false);
                SAARadioButton.setSelected(false);
            }
        });

        radioStochastiqueRadioButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                recuitDeterministeRadioButton.setSelected(false);
                SAARadioButton.setSelected(false);
            }
        });

        SAARadioButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                recuitDeterministeRadioButton.setSelected(false);
                radioStochastiqueRadioButton.setSelected(false);
            }
        });

        // =================== gestion precision ===================

        //slider1.setMinorTickSpacing(1);

        // =================== bouton valider ===================

        button_validate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.print("CLICKE OK");
                //JOptionPane.showConfirmDialog(null, "CLICKE OK");
            }
        });

        // =================== resultat texte ===================


        createScene();
        jpanel_result_map.add(jfxPanel);


        this.pack();
        this.setVisible(true);
    }

    void ecrireResultat(String texte) {
        textAreaResultat.append(texte + '\n');
    }

    

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        jpanel_root = new JPanel();
        jpanel_root.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        jpanel_root.setPreferredSize(new Dimension(1200, 600));
        tabbedPane1 = new JTabbedPane();
        tabbedPane1.setToolTipText("tooltiptext");
        jpanel_root.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        jpanel_lancement = new JPanel();
        jpanel_lancement.setLayout(new BorderLayout(0, 0));
        jpanel_lancement.setPreferredSize(new Dimension(800, 600));
        jpanel_lancement.setToolTipText("");
        tabbedPane1.addTab("Lancement", jpanel_lancement);
        jpanel_input_user = new JPanel();
        jpanel_input_user.setLayout(new BorderLayout(0, 0));
        jpanel_input_user.setMinimumSize(new Dimension(300, 106));
        jpanel_input_user.setPreferredSize(new Dimension(270, 50));
        jpanel_lancement.add(jpanel_input_user, BorderLayout.SOUTH);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setPreferredSize(new Dimension(300, 50));
        jpanel_input_user.add(panel1, BorderLayout.WEST);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Granularité"));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 20, 0, 20);
        panel1.add(panel2, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Les dernières ");
        panel2.add(label1, BorderLayout.WEST);
        formattedTextField3 = new JFormattedTextField();
        formattedTextField3.setMaximumSize(new Dimension(10, 2147483647));
        formattedTextField3.setMinimumSize(new Dimension(15, 26));
        formattedTextField3.setPreferredSize(new Dimension(25, 26));
        panel2.add(formattedTextField3, BorderLayout.CENTER);
        comboBox1 = new JComboBox();
        comboBox1.setPreferredSize(new Dimension(120, 26));
        panel2.add(comboBox1, BorderLayout.EAST);
        button_validate = new JButton();
        button_validate.setHorizontalAlignment(0);
        button_validate.setHorizontalTextPosition(11);
        button_validate.setPreferredSize(new Dimension(60, 27));
        button_validate.setRolloverEnabled(false);
        button_validate.setSelected(false);
        button_validate.setText("Ok");
        button_validate.setVerticalAlignment(0);
        jpanel_input_user.add(button_validate, BorderLayout.EAST);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        jpanel_input_user.add(panel3, BorderLayout.CENTER);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel4.setMinimumSize(new Dimension(10, 50));
        panel4.setPreferredSize(new Dimension(110, 50));
        panel3.add(panel4, BorderLayout.CENTER);
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Algorithme", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, -1, -1, panel4.getFont())));
        recuitDeterministeRadioButton = new JRadioButton();
        recuitDeterministeRadioButton.setAlignmentY(0.0f);
        recuitDeterministeRadioButton.setMargin(new Insets(0, 1, 0, 1));
        recuitDeterministeRadioButton.setText("Recuit déterministe");
        recuitDeterministeRadioButton.setVerticalAlignment(1);
        panel4.add(recuitDeterministeRadioButton);
        radioStochastiqueRadioButton = new JRadioButton();
        radioStochastiqueRadioButton.setActionCommand("Radio Stoch");
        radioStochastiqueRadioButton.setAlignmentY(0.0f);
        radioStochastiqueRadioButton.setLabel("Recuit stochastique");
        radioStochastiqueRadioButton.setMargin(new Insets(0, 1, 0, 1));
        radioStochastiqueRadioButton.setText("Recuit stochastique");
        radioStochastiqueRadioButton.setVerticalAlignment(1);
        panel4.add(radioStochastiqueRadioButton);
        SAARadioButton = new JRadioButton();
        SAARadioButton.setAlignmentY(0.0f);
        SAARadioButton.setAutoscrolls(false);
        SAARadioButton.setMargin(new Insets(0, 1, 0, 1));
        SAARadioButton.setText("SAA");
        SAARadioButton.setVerticalAlignment(1);
        panel4.add(SAARadioButton);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel5, BorderLayout.EAST);
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Précision"));
        final JLabel label2 = new JLabel();
        label2.setText("-");
        label2.setVerticalAlignment(1);
        label2.setVerticalTextPosition(1);
        panel5.add(label2);
        slider1 = new JSlider();
        slider1.setAlignmentX(0.0f);
        slider1.setAlignmentY(0.0f);
        slider1.setDoubleBuffered(false);
        slider1.setMaximum(2);
        slider1.setMinorTickSpacing(1);
        slider1.setPaintLabels(false);
        slider1.setPaintTicks(true);
        panel5.add(slider1);
        final JLabel label3 = new JLabel();
        label3.setText("+");
        label3.setVerticalAlignment(1);
        label3.setVerticalTextPosition(1);
        panel5.add(label3);
        jpanel_results = new JPanel();
        jpanel_results.setLayout(new GridBagLayout());
        jpanel_lancement.add(jpanel_results, BorderLayout.CENTER);
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(400);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanel_results.add(splitPane1, gbc);
        jpanel_result_map = new JPanel();
        jpanel_result_map.setLayout(new BorderLayout(0, 0));
        splitPane1.setRightComponent(jpanel_result_map);
        jpanel_result_txt = new JPanel();
        jpanel_result_txt.setLayout(new BorderLayout(0, 0));
        jpanel_result_txt.setBackground(new Color(-1));
        jpanel_result_txt.setMinimumSize(new Dimension(0, 0));
        jpanel_result_txt.setPreferredSize(new Dimension(0, 0));
        splitPane1.setLeftComponent(jpanel_result_txt);
        final JLabel label4 = new JLabel();
        label4.setAlignmentX(0.0f);
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setHorizontalAlignment(0);
        label4.setText("Résultats");
        jpanel_result_txt.add(label4, BorderLayout.NORTH);
        textAreaResultat = new JTextArea();
        jpanel_result_txt.add(textAreaResultat, BorderLayout.CENTER);
        jpanel_parametrage = new JPanel();
        jpanel_parametrage.setLayout(new GridBagLayout());
        jpanel_parametrage.setPreferredSize(new Dimension(800, 600));
        tabbedPane1.addTab("Paramètres avancés", jpanel_parametrage);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        panel6.setPreferredSize(new Dimension(600, 200));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanel_parametrage.add(panel6, gbc);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel6.add(panel7, gbc);
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "<html>Coût c<sub>i</sub></html>"));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel7.add(panel8, gbc);
        formattedTextField6 = new JFormattedTextField();
        formattedTextField6.setPreferredSize(new Dimension(40, 26));
        panel8.add(formattedTextField6);
        appliquerButton = new JButton();
        appliquerButton.setText("Appliquer");
        panel8.add(appliquerButton);
        list1 = new JList();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel7.add(list1, gbc);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel6.add(panel9, gbc);
        panel9.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "<html>Coût v<sub>i</sub></html>"));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel9.add(panel10, gbc);
        formattedTextField7 = new JFormattedTextField();
        formattedTextField7.setMinimumSize(new Dimension(40, 26));
        formattedTextField7.setPreferredSize(new Dimension(40, 26));
        panel10.add(formattedTextField7);
        appliquerButton1 = new JButton();
        appliquerButton1.setText("Appliquer");
        panel10.add(appliquerButton1);
        list2 = new JList();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel9.add(list2, gbc);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel6.add(panel11, gbc);
        panel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "<html>Coût W<sub>i</sub></html>"));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel11.add(panel12, gbc);
        formattedTextField9 = new JFormattedTextField();
        formattedTextField9.setPreferredSize(new Dimension(40, 26));
        panel12.add(formattedTextField9);
        appliquerButton2 = new JButton();
        appliquerButton2.setText("Appliquer");
        panel12.add(appliquerButton2);
        list3 = new JList();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel11.add(list3, gbc);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel6.add(panel13, gbc);
        panel13.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "<html>Capacité k<sub>i</sub></html>"));
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel13.add(panel14, gbc);
        formattedTextField8 = new JFormattedTextField();
        formattedTextField8.setPreferredSize(new Dimension(40, 26));
        panel14.add(formattedTextField8);
        appliquerButton3 = new JButton();
        appliquerButton3.setText("Appliquer");
        panel14.add(appliquerButton3);
        list4 = new JList();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel13.add(list4, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanel_root;
    }

    class JFileChooser extends JPanel {

        public final String NOM_FICHIER_DEFAUT = "resultats_velib.txt";
        private final String CHEMIN_DEFAUT = ".";

        private String cheminChoisi = CHEMIN_DEFAUT;

        private javax.swing.JFileChooser chooser;
        private String choosertitle;

        public JFileChooser() {
            int result;

            chooser = new javax.swing.JFileChooser();
            chooser.setCurrentDirectory(new File(CHEMIN_DEFAUT));
            chooser.setDialogTitle(choosertitle);
            chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
            //
            // disable the "All files" option.
            //
            chooser.setAcceptAllFileFilterUsed(false);
            //
            if (chooser.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
                System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
                System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
                cheminChoisi = chooser.getSelectedFile().toString();
                System.out.println(cheminChoisi);
            } else {
                System.out.println("No Selection ");
            }

        }

        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        void ouvrirExplorateurFichier() {
            JFrame frame = new JFrame("");
            JFileChooser panel = new JFileChooser();
            frame.addWindowListener(
                    new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                            System.exit(0);
                        }
                    }
            );
            frame.getContentPane().add(panel, "Center");
            frame.setSize(panel.getPreferredSize());
            frame.setVisible(true);
        }

        public String getCheminChoisi() {
            return cheminChoisi;
        }
    }


    private void createScene() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                WebView view = new WebView();
                engine = view.getEngine();
                engine.load(getClass().getResource("Connector.html").toExternalForm());

                jfxPanel.setScene(new Scene(view));
            }
        });
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

}
