package graphic;


import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import gestionnaireFichier.MyJFileChooser;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import vls.StationVelo;
import vls.StationVelo.ParamPremierNiveau;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTML;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.*;
import java.util.List;
import java.util.Timer;

import static gestionnaireFichier.GestionnaireFichier.*;
import static graphic.Interface.NiveauPrecision.PRECISION_BASSE;
import static graphic.Interface.NiveauPrecision.PRECISION_HAUTE;
import static graphic.Interface.NiveauPrecision.PRECISION_MOYENNE;
import static vls.StationVelo.*;
import static vls.StationVelo.ParamPremierNiveau.*;

public class Interface extends JFrame {
    private JPanel jpanel_lancement;

    private JButton button_validate;
    private JPanel jpanel_input_user;
    private JPanel jpanel_result_map;
    private JTabbedPane tabbedPane1;
    private JPanel jpanel_parametrage;
    private JPanel jpanel_root;
    private JRadioButton SAARadioButton;
    private JList list1;
    private JList list2;
    private JList list3;
    private JList list4;
    private JPanel jpanel_result_txt;
    private JPanel jpanel_results;
    private JTextArea textArea6;
    private JRadioButton recuitDeterministeRadioButton;
    private JSlider slider1;
    private JRadioButton radioStochastiqueRadioButton;
    private JTextArea textAreaResultat;
    private JButton chargerFichierConfigurationButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton downloadConfigButton;
    private JLabel labelFichierConfig;

    private final JFXPanel jfxPanel = new JFXPanel();
    private WebEngine engine;

    private ArrayList<StationVelo> stationVelos = parserFichier();
    private NiveauPrecision niveauPrecision = PRECISION_HAUTE;
    private boolean[] parametresFixes = {false, false, false, false};

    public Interface() {

        this.setTitle("Vélib");
        //this.setContentPane(jpanel_root);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // =====================================
        // =========== Ajout du menu ===========
        // =====================================

        JMenuBar bar = new JMenuBar();

        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem menuItemNouveau = new JMenuItem(new AbstractAction("My Menu Item") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                nouvelleSimulation();
            }
        });
        menuItemNouveau.setText("Nouvelle simulation");
        JMenuItem menuItemSauvegarder = new JMenuItem(new AbstractAction("My Menu Item 2") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MyJFileChooser jFileChooser = new MyJFileChooser();
                if (jFileChooser.getCheminChoisi() != null) {
                    String path = jFileChooser.getCheminChoisi() + "/" + jFileChooser.NOM_FICHIER_DEFAUT;
                    try {
                        PrintWriter writer = new PrintWriter(path, "UTF-8");
                        writer.println(textAreaResultat.getText());
                        writer.close();
                    } catch (Exception e) {
                    }
                }
            }
        });
        menuItemSauvegarder.setText("Sauvegarder résultats");

        JMenuItem menuItemQuitter = new JMenuItem(new AbstractAction("My Menu Item 2") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                This().dispose();
                return;
            }
        });
        menuItemQuitter.setText("Quitter");
        menuFichier.add(menuItemNouveau);
        menuFichier.add(menuItemSauvegarder);
        menuFichier.add(menuItemQuitter);

        JMenu menuAide = new JMenu("Aide");
        menuAide.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                afficherManuelUtilisateur();
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        bar.add(menuFichier);
        bar.add(menuAide);

        this.add(bar, BorderLayout.NORTH);
        this.add(jpanel_root, BorderLayout.CENTER);

        // =====================================
        // ======== Paramétrage avancé =========
        // =====================================

        downloadConfigButton.addActionListener(e -> {
                    MyJFileChooser jFileChooser = new MyJFileChooser();
                    if (jFileChooser.getCheminChoisi() != null) {
                        String path = jFileChooser.getCheminChoisi();
                        ArrayList<Integer> numeroStations = new ArrayList<>();
                        for (StationVelo stationVelo : stationVelos) {
                            numeroStations.add(stationVelo.getNumber());
                        }
                        boolean fichierCree = creerFichierConfiguration(numeroStations, path);
                        if (fichierCree) {
                            labelFichierConfig.setText("Fichier téléchargé dans " + path + NOM_DEFAUT_FICHIER_CONFIG);
                        } else {
                            labelFichierConfig.setText("Erreur : veuillez sélectionner un dossier.");
                        }
                    }
                }
        );

        chargerFichierConfigurationButton.addActionListener(e -> {
                    MyJFileChooser jFileChooser = new MyJFileChooser();
                    if (jFileChooser.getCheminChoisi() != null) {
                        String path = jFileChooser.getCheminChoisi();
                        try {
                            HashMap<Integer, ArrayList<Integer>> coutsFichierConfig = parserFichierConfiguration(path);
                            for (StationVelo stationVelo : stationVelos) {
                                ArrayList<Integer> cout = coutsFichierConfig.get(stationVelo.getNumber());
                                stationVelo.setParamPremierNiveau(varC, cout.get(varC.indice));
                                stationVelo.setParamPremierNiveau(varV, cout.get(varV.indice));
                                stationVelo.setParamPremierNiveau(varW, cout.get(varW.indice));
                                stationVelo.setParamPremierNiveau(varK, cout.get(varK.indice));
                            }
                            parametresFixes = new boolean[]{true, true, true, true};
                            ecrireCoutStation(list1, varC, coutsFichierConfig);
                            ecrireCoutStation(list2, varV, coutsFichierConfig);
                            ecrireCoutStation(list3, varW, coutsFichierConfig);
                            ecrireCoutStation(list4, varK, coutsFichierConfig);
                            effacerAffichage(textField1);
                            effacerAffichage(textField2);
                            effacerAffichage(textField3);
                            effacerAffichage(textField4);
                            labelFichierConfig.setText("Fichier chargé de " + path);
                            createScene();
                        } catch (Exception e1) {
                        }
                    }
                }
        );

        appliquerCoutPartoutListener(textField1, list1, varC);
        appliquerCoutPartoutListener(textField2, list2, varV);
        appliquerCoutPartoutListener(textField3, list3, varW);
        appliquerCoutPartoutListener(textField4, list4, varK);

        // =====================================
        // ============ Lancement ==============
        // =====================================

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

        slider1.addChangeListener(event -> {
            int value = slider1.getValue();
            niveauPrecision = NiveauPrecision.values()[value];
        });

        // =================== bouton valider ===================

        button_validate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (verificationInputUser()) {
                    JOptionPane.showMessageDialog(null, "C'est parti !");
                }
            }
        });

        // =================== resultat texte ===================


        createScene();
        jpanel_result_map.add(jfxPanel);

        this.pack();
        this.setVisible(true);
    }

    private Interface This() {
        return this;
    }

    // ============================================
    // ================ écriture ==================
    // ============================================

    private void nouvelleSimulation() {
        // parametrages avances
        labelFichierConfig.setText("Aucun fichier sélectionné");
        effacerAffichage(textField1);
        effacerAffichage(textField2);
        effacerAffichage(textField3);
        effacerAffichage(textField4);
        effacerAffichage(list1);
        effacerAffichage(list2);
        effacerAffichage(list3);
        effacerAffichage(list4);
        parametresFixes = new boolean[]{false, false, false, false};
        // lancement
        effacerAffichage(textAreaResultat);
        recuitDeterministeRadioButton.setSelected(false);
        radioStochastiqueRadioButton.setSelected(false);
        SAARadioButton.setSelected(false);
        createScene();
    }

    private void effacerAffichage(JLabel labelFichierConfig) {
        labelFichierConfig.setText("");
    }

    private void effacerAffichage(JTextComponent jTextComponent) {
        jTextComponent.setText("");
    }

    private void effacerAffichage(JList jList) {
        jList.setListData(new Vector());
    }

    public void ecrireResultat(String texte) {
        textAreaResultat.append(texte + '\n');
    }

    private void ecrireCoutStation(JList jList, ArrayList<String> couts) {
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < couts.size(); i++) {
            listModel.addElement("Station " + (i + 1) + couts.get(i));
        }
        jList.setModel(listModel);
    }

    private void ecrireCoutStation(JList jList, ParamPremierNiveau varPremierNiveau, String cout) {
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < stationVelos.size(); i++) {
            StationVelo stationVelo = stationVelos.get(i);
            listModel.addElement("Station n°" + stationVelo.getNumber() + "  :  " + cout + "€");
            stationVelo.setParamPremierNiveau(varPremierNiveau, Integer.valueOf(cout));
        }
        jList.setModel(listModel);
    }

    private void ecrireCoutStation(JList jList, ParamPremierNiveau var, HashMap<Integer, ArrayList<Integer>> coutsParStation) {
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < stationVelos.size(); i++) {
            int numeroStation = stationVelos.get(i).getNumber();
            listModel.addElement("Station n°" + numeroStation + "  :  " + coutsParStation.get(numeroStation).get(var.indice) + "€");
        }
        jList.setModel(listModel);
    }

    // ============================================
    // ================ listener ==================
    // ============================================

    private void appliquerCoutPartoutListener(JTextField jText, JList jList, ParamPremierNiveau varPremierNiveau) {
        CaretListener update = e -> {
            JTextField text = (JTextField) e.getSource();
            String value = text.getText();
            try {
                if (!value.isEmpty()) {
                    Integer.valueOf(value);
                    System.out.println(value);
                    ecrireCoutStation(jList, varPremierNiveau, value);
                    parametresFixes[varPremierNiveau.indice] = true;
                    createScene();
                }
            } catch (Exception ex) {
            }
        };
        jText.addCaretListener(update);
    }

    // ============================================
    // ========= verification input user ==========
    // ============================================

    private boolean verifSelectionAlgo() {
        return recuitDeterministeRadioButton.isSelected()
                || radioStochastiqueRadioButton.isSelected()
                || SAARadioButton.isSelected();
    }

    private boolean verificationInputUser() {

        if (!verifSelectionAlgo()) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un algorithme");
            return false;
        }

        for (int i = 0; i < parametresFixes.length; i++) {
            boolean parametresStationInit = parametresFixes[i];
            if (!parametresStationInit) {
                JOptionPane.showMessageDialog(null, "Veuillez entrer une valeur pour : " + ParamPremierNiveau.values()[i].nom);
                return false;
            }
        }

        return true;
    }

    private void afficherManuelUtilisateur() {
        JFrame frame = new JFrame();
        frame.setTitle("Aide");
        frame.setPreferredSize(new Dimension(800, 800));
        frame.setLayout(new BorderLayout());

        final JFXPanel jfxPanel = new JFXPanel();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                WebView view = new WebView();
                WebEngine engine = view.getEngine();
                engine.load(getClass().getResource("manuelUtilisateur/Utilisateur.html").toExternalForm());

                jfxPanel.setScene(new Scene(view));
            }
        });

        frame.add(jfxPanel);
        frame.pack();
        frame.setVisible(true);
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
        button_validate = new JButton();
        button_validate.setHorizontalAlignment(0);
        button_validate.setHorizontalTextPosition(11);
        button_validate.setPreferredSize(new Dimension(60, 27));
        button_validate.setRolloverEnabled(false);
        button_validate.setSelected(false);
        button_validate.setText("Ok");
        button_validate.setVerticalAlignment(0);
        jpanel_input_user.add(button_validate, BorderLayout.EAST);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        jpanel_input_user.add(panel1, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel2.setMinimumSize(new Dimension(10, 50));
        panel2.setPreferredSize(new Dimension(110, 50));
        panel1.add(panel2, BorderLayout.CENTER);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Algorithme", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, -1, -1, panel2.getFont())));
        recuitDeterministeRadioButton = new JRadioButton();
        recuitDeterministeRadioButton.setAlignmentY(0.0f);
        recuitDeterministeRadioButton.setMargin(new Insets(0, 1, 0, 1));
        recuitDeterministeRadioButton.setSelected(false);
        recuitDeterministeRadioButton.setText("Recuit déterministe");
        recuitDeterministeRadioButton.setVerticalAlignment(1);
        panel2.add(recuitDeterministeRadioButton);
        radioStochastiqueRadioButton = new JRadioButton();
        radioStochastiqueRadioButton.setActionCommand("Radio Stoch");
        radioStochastiqueRadioButton.setAlignmentY(0.0f);
        radioStochastiqueRadioButton.setLabel("Recuit stochastique");
        radioStochastiqueRadioButton.setMargin(new Insets(0, 1, 0, 1));
        radioStochastiqueRadioButton.setText("Recuit stochastique");
        radioStochastiqueRadioButton.setVerticalAlignment(1);
        panel2.add(radioStochastiqueRadioButton);
        SAARadioButton = new JRadioButton();
        SAARadioButton.setAlignmentY(0.0f);
        SAARadioButton.setAutoscrolls(false);
        SAARadioButton.setMargin(new Insets(0, 1, 0, 1));
        SAARadioButton.setText("SAA");
        SAARadioButton.setVerticalAlignment(1);
        panel2.add(SAARadioButton);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(panel3, BorderLayout.EAST);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Précision"));
        final JLabel label1 = new JLabel();
        label1.setAlignmentY(0.0f);
        label1.setDoubleBuffered(false);
        label1.setText("-");
        label1.setVerticalAlignment(1);
        label1.setVerticalTextPosition(1);
        panel3.add(label1);
        slider1 = new JSlider();
        slider1.setAlignmentX(0.0f);
        slider1.setAlignmentY(0.0f);
        slider1.setDoubleBuffered(false);
        slider1.setMaximum(2);
        slider1.setMinorTickSpacing(1);
        slider1.setPaintLabels(false);
        slider1.setPaintTicks(true);
        slider1.setPreferredSize(new Dimension(200, 20));
        panel3.add(slider1);
        final JLabel label2 = new JLabel();
        label2.setAlignmentY(0.0f);
        label2.setText("+");
        label2.setVerticalAlignment(1);
        label2.setVerticalTextPosition(1);
        panel3.add(label2);
        jpanel_results = new JPanel();
        jpanel_results.setLayout(new GridBagLayout());
        jpanel_lancement.add(jpanel_results, BorderLayout.CENTER);
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(400);
        GridBagConstraints gbc;
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
        final JLabel label3 = new JLabel();
        label3.setAlignmentX(0.0f);
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setHorizontalAlignment(0);
        label3.setText("Résultats");
        jpanel_result_txt.add(label3, BorderLayout.NORTH);
        textAreaResultat = new JTextArea();
        jpanel_result_txt.add(textAreaResultat, BorderLayout.CENTER);
        jpanel_parametrage = new JPanel();
        jpanel_parametrage.setLayout(new GridBagLayout());
        jpanel_parametrage.setPreferredSize(new Dimension(800, 600));
        tabbedPane1.addTab("Paramètres avancés", jpanel_parametrage);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        panel4.setPreferredSize(new Dimension(600, 200));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        jpanel_parametrage.add(panel4, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel4.add(panel5, gbc);
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "<html>Coût c<sub>i</sub></html>"));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel5.add(panel6, gbc);
        textField1 = new JTextField();
        textField1.setName("c");
        textField1.setPreferredSize(new Dimension(40, 26));
        panel6.add(textField1);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel5.add(scrollPane1, gbc);
        list1 = new JList();
        scrollPane1.setViewportView(list1);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel4.add(panel7, gbc);
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "<html>Coût v<sub>i</sub></html>"));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel7.add(panel8, gbc);
        textField2 = new JTextField();
        textField2.setName("v");
        textField2.setPreferredSize(new Dimension(40, 26));
        panel8.add(textField2);
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel7.add(scrollPane2, gbc);
        list2 = new JList();
        scrollPane2.setViewportView(list2);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel4.add(panel9, gbc);
        panel9.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "<html>Coût w<sub>i</sub></html>"));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel9.add(panel10, gbc);
        textField3 = new JTextField();
        textField3.setName("w");
        textField3.setPreferredSize(new Dimension(40, 26));
        panel10.add(textField3);
        final JScrollPane scrollPane3 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel9.add(scrollPane3, gbc);
        list3 = new JList();
        scrollPane3.setViewportView(list3);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel4.add(panel11, gbc);
        panel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "<html>Capacité k<sub>i</sub></html>"));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel11.add(panel12, gbc);
        textField4 = new JTextField();
        textField4.setName("k");
        textField4.setPreferredSize(new Dimension(40, 26));
        panel12.add(textField4);
        final JScrollPane scrollPane4 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel11.add(scrollPane4, gbc);
        list4 = new JList();
        scrollPane4.setViewportView(list4);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 0);
        jpanel_parametrage.add(panel13, gbc);
        panel13.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Fichier configuration"));
        downloadConfigButton = new JButton();
        downloadConfigButton.setHorizontalAlignment(11);
        downloadConfigButton.setHorizontalTextPosition(11);
        downloadConfigButton.setText("Télécharger");
        panel13.add(downloadConfigButton);
        chargerFichierConfigurationButton = new JButton();
        chargerFichierConfigurationButton.setHorizontalAlignment(0);
        chargerFichierConfigurationButton.setText("Charger");
        panel13.add(chargerFichierConfigurationButton);
        labelFichierConfig = new JLabel();
        labelFichierConfig.setBackground(new Color(-1));
        labelFichierConfig.setText("Aucun fichier sélectionné");
        panel13.add(labelFichierConfig);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanel_root;
    }

    enum NiveauPrecision {

        PRECISION_BASSE(1, 1),
        PRECISION_MOYENNE(2, 2),
        PRECISION_HAUTE(4, 4);

        int nombrePaliers;
        int nombreIterations;

        NiveauPrecision(int nombrePaliers, int nombreIterations) {
            this.nombrePaliers = nombrePaliers;
            this.nombreIterations = nombreIterations;
        }
    }


    // ===================================================
    // ====== interface dynamique - ne pas toucher =======
    // ===================================================

    private String creerMarqueurs() {
        String content = "";
        for (StationVelo stationVelo : stationVelos) {
            content += genererMarqueur(stationVelo);
            content += ",\n";
        }
        if (!content.isEmpty()) {
            content = content.substring(0, content.length() - 2);
        }
        content = "const stations = [\n" + content + "];";
        return content;
    }

    private String genererMarqueur(StationVelo stationVelo) {
        return "new StationVelo(\"" + stationVelo.getNom()
                + "\", {lat:" + stationVelo.getPosition().getLat() + ", lng:" + stationVelo.getPosition().getLng()
                + "}," + "\"" + genererInfoMarqueur(stationVelo) + "\"" + ")";
    }

    private String genererInfoLigne(String etiquette, int valeur) {
        return "<b>" + etiquette + " : </b>" + valeur + "<br/>";
    }

    private String genererInfoMarqueur(StationVelo stationVelo) {
        String info = "<html><div>";
        info += "<b>" + stationVelo.getNom() + "</b><br/>";
        info += genererInfoLigne("Capacité", stationVelo.getBikeStands());
        info += genererInfoLigne("Vélos disponibles", stationVelo.getAvailableBikes());
        info += genererInfoLigne("Supports de vélos disponibles", stationVelo.getAvailableBikeStands());
        info += genererInfoLigne("Coût c", stationVelo.getC());
        info += genererInfoLigne("Coût v", stationVelo.getV());
        info += genererInfoLigne("Coût w", stationVelo.getW());
        info += genererInfoLigne("Capacité k", stationVelo.getK());
        info += "</div></html>";
        return info;
    }

    private void createScene() {

        Platform.runLater(() -> {

            try {
                File file = new File("src/graphic/Connector.html");
                org.jsoup.nodes.Document doc = Jsoup.parse(file, "UTF-8");
                org.jsoup.nodes.Element div = doc.getElementById("stations");
                div.text(creerMarqueurs());
                System.out.println(creerMarqueurs());

                WebView view = new WebView();
                engine = view.getEngine();
                engine.setJavaScriptEnabled(true);
                engine.loadContent(doc.toString().replace("&lt;", "<").replace("&gt;", ">"));

                jfxPanel.setScene(new Scene(view));
            } catch (IOException e) {
                e.printStackTrace();
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
