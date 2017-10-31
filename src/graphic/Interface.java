package graphic;


import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import algorithms.Recuit;
import algorithms.SAA;
import algorithms.Scenario;
import gestionnaireFichier.MyJFileChooser;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import vls.ProblemeVLS;
import vls.ScenarioVLS;
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

/**
 * Permet le lancement du programme avec l'interface graphique
 */
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
    private JPanel jpanel_result_txt;
    private JPanel jpanel_results;
    private JTextArea textArea6;
    private JRadioButton recuitDeterministeRadioButton;
    private JSlider slider1;
    private JRadioButton recuitStochastiqueRadioButton;
    private JTextArea textAreaResultat;
    private JButton chargerFichierConfigurationButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton downloadConfigButton;
    private JLabel labelFichierConfig;

    private final JFXPanel jfxPanel = new JFXPanel();
    private WebEngine engine;

    private ArrayList<StationVelo> stationVelos = parserFichier();
    private ArrayList<ScenarioVLS> scenarii = parserScenars();

    private ProblemeVLS p;
    private Recuit<Integer, ScenarioVLS> recuit;
    private SAA<Integer, ScenarioVLS> saa;
    private NiveauPrecision niveauPrecision = PRECISION_HAUTE;
    private boolean[] parametresFixes = {false, false, false};

    public Interface() {
        p = new ProblemeVLS(stationVelos, scenarii);
        recuit = new Recuit<Integer, ScenarioVLS>(p, niveauPrecision, 0.8);
        saa = new SAA<Integer, ScenarioVLS>(5, recuit);

        this.setTitle("Vélib");
        //this.setContentPane(jpanel_root);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // =====================================
        // =========== Ajout du menu ===========
        // =====================================

        JMenuBar bar = new JMenuBar();

        JMenu menuFichier = new JMenu("Fichier");
        // reinitialise l'interface
        JMenuItem menuItemNouveau = new JMenuItem(new AbstractAction("My Menu Item") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                nouvelleSimulation();
            }
        });
        menuItemNouveau.setText("Nouvelle simulation");
        // sauvegarde les resultats dans un fichier texte
        JMenuItem menuItemSauvegarder = new JMenuItem(new AbstractAction("My Menu Item 2") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MyJFileChooser jFileChooser = new MyJFileChooser();
                if (jFileChooser.getCheminChoisi() != null) {
                    String path = jFileChooser.getCheminChoisi()/* + "/" + jFileChooser.NOM_FICHIER_DEFAUT*/;
                    try {
                        PrintWriter writer = new PrintWriter(path, "UTF-8");
                        writer.println(textAreaResultat.getText());
                        writer.close();
                    } catch (Exception e) {
                        path = jFileChooser.getCheminChoisi() + "/" + jFileChooser.NOM_FICHIER_DEFAUT;
                        try {
                            PrintWriter writer = new PrintWriter(path, "UTF-8");
                            writer.println(textAreaResultat.getText());
                            writer.close();
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        });
        menuItemSauvegarder.setText("Sauvegarder résultats");
        // ferme le programme
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
        // affiche le manuel utilisateur sous format pdf dans une nouvelle fenetre
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

        // permet de telecharger le fichier csv modèle
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

        // permet de charger le fichier csv
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
                            }
                            parametresFixes = new boolean[]{true, true, true};
                            ecrireCoutStation(list1, varC, coutsFichierConfig);
                            ecrireCoutStation(list2, varV, coutsFichierConfig);
                            ecrireCoutStation(list3, varW, coutsFichierConfig);
                            effacerAffichage(textField1);
                            effacerAffichage(textField2);
                            effacerAffichage(textField3);
                            labelFichierConfig.setText("Fichier chargé de " + path);
                            createScene();
                        } catch (Exception e1) {
                        }
                    }
                }
        );

        // applique un listener sur les zones de texte
        appliquerCoutPartoutListener(textField1, list1, varC);
        appliquerCoutPartoutListener(textField2, list2, varV);
        appliquerCoutPartoutListener(textField3, list3, varW);

        // =====================================
        // ============ Lancement ==============
        // =====================================

        // =================== gestion boutons choix algo ===================

        recuitDeterministeRadioButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                recuitStochastiqueRadioButton.setSelected(false);
                SAARadioButton.setSelected(false);
            }
        });

        recuitStochastiqueRadioButton.addMouseListener(new MouseAdapter() {
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
                recuitStochastiqueRadioButton.setSelected(false);
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
                    recuit.setPrecision(niveauPrecision);
                    if (recuitDeterministeRadioButton.isSelected()) {
                        p.deterministe();
                        recuit.solve();
                    } else if (recuitStochastiqueRadioButton.isSelected()) {
                        p.stochastique();
                        recuit.solve();
                    } else if (SAARadioButton.isSelected()) {
                        p.stochastique();
                        saa.solve();
                    }
                    textAreaResultat.setText(p.toStringResults());

                }
            }
        });

        // =================== resultat texte ===================


        createScene();
        jpanel_result_map.add(jfxPanel);

        this.pack();
        this.setVisible(true);
    }

    /**
     * Retourne l'objet courant this
     *
     * @return this
     */
    private Interface This() {
        return this;
    }

    // ============================================
    // ================ écriture ==================
    // ============================================

    /**
     * Reinitialise l'interface graphique
     */
    private void nouvelleSimulation() {
        // parametrages avances
        labelFichierConfig.setText("Aucun fichier sélectionné");
        effacerAffichage(textField1);
        effacerAffichage(textField2);
        effacerAffichage(textField3);
        effacerAffichage(list1);
        effacerAffichage(list2);
        effacerAffichage(list3);
        parametresFixes = new boolean[]{false, false, false};
        // lancement
        effacerAffichage(textAreaResultat);
        recuitDeterministeRadioButton.setSelected(false);
        recuitStochastiqueRadioButton.setSelected(false);
        SAARadioButton.setSelected(false);
        createScene();
    }

    private void effacerAffichage(JLabel labelFichierConfig) {
        labelFichierConfig.setText("");
    }

    /**
     * Reinitialise un JTextComponent
     *
     * @param jTextComponent le JTextComponent à réinitialiser
     */
    private void effacerAffichage(JTextComponent jTextComponent) {
        jTextComponent.setText("");
    }

    /**
     * Reinitialise un JList
     *
     * @param jList le JList à réinitialiser
     */
    private void effacerAffichage(JList jList) {
        jList.setListData(new Vector());
    }

    /**
     * Ecrit un texte et ajoute une nouvelle ligne dans la fenêtre de resultat
     *
     * @param texte le texte à ecrire
     */
    public void ecrireResultat(String texte) {
        textAreaResultat.append(texte + '\n');
    }

    /**
     * Ecrit le cout de la variable de premier niveau associe dans une jlist
     *
     * @param jList              la jlist dans laquelle afficher les stations
     * @param paramPremierNiveau la variable de premier niveau associe
     * @param cout               le cout
     */
    private void ecrireCoutStation(JList jList, ParamPremierNiveau paramPremierNiveau, String cout) {
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < stationVelos.size(); i++) {
            StationVelo stationVelo = stationVelos.get(i);
            listModel.addElement("Station n°" + stationVelo.getNumber() + "  :  " + cout + "€");
            stationVelo.setParamPremierNiveau(paramPremierNiveau, Integer.valueOf(cout));
        }
        jList.setModel(listModel);
    }

    /**
     * Ecrit le cout de la variable de premier niveau associe dans une jlist
     *
     * @param jList              la jlist dans laquelle afficher les stations
     * @param paramPremierNiveau la variable de premier niveau associe
     * @param coutsParStation    les couts pour chaque stations
     */
    private void ecrireCoutStation(JList jList, ParamPremierNiveau paramPremierNiveau, HashMap<Integer, ArrayList<Integer>> coutsParStation) {
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < stationVelos.size(); i++) {
            int numeroStation = stationVelos.get(i).getNumber();
            listModel.addElement("Station n°" + numeroStation + "  :  " + coutsParStation.get(numeroStation).get(paramPremierNiveau.indice) + "€");
        }
        jList.setModel(listModel);
    }

    // ============================================
    // ================ listener ==================
    // ============================================

    /**
     * Applique le listener qui permet d'ajouter la valeur entree à toutes les stations dans la liste
     *
     * @param jText              la valeur entrée
     * @param jList              la liste dans laquelle afficher les stations
     * @param paramPremierNiveau le parametre voulu
     */
    private void appliquerCoutPartoutListener(JTextField jText, JList jList, ParamPremierNiveau paramPremierNiveau) {
        CaretListener update = e -> {
            JTextField text = (JTextField) e.getSource();
            String value = text.getText();
            try {
                if (!value.isEmpty()) {
                    // permet au try catch de verifier s'il s'agit bien d'un integer
                    Integer.valueOf(value);

                    ecrireCoutStation(jList, paramPremierNiveau, value);
                    parametresFixes[paramPremierNiveau.indice] = true;
                    // reinit l'interface graphique
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

    /**
     * Verifier que l'utilisateur a bien choisi un algorithme
     *
     * @return true si au moins un algorithme est sélectionné
     */
    private boolean verifSelectionAlgo() {
        return recuitDeterministeRadioButton.isSelected()
                || recuitStochastiqueRadioButton.isSelected()
                || SAARadioButton.isSelected();
    }

    /**
     * Verification de tous les parametres necessaires au lancement du programme
     *
     * @return true si l'utilisateur a entré toutes les valeurs necessaires
     */
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

    /**
     * Affiche le manuel utilisateur dans une nouvelle fenetre
     */
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
        recuitStochastiqueRadioButton = new JRadioButton();
        recuitStochastiqueRadioButton.setActionCommand("Radio Stoch");
        recuitStochastiqueRadioButton.setAlignmentY(0.0f);
        recuitStochastiqueRadioButton.setLabel("Recuit stochastique");
        recuitStochastiqueRadioButton.setMargin(new Insets(0, 1, 0, 1));
        recuitStochastiqueRadioButton.setText("Recuit stochastique");
        recuitStochastiqueRadioButton.setVerticalAlignment(1);
        panel2.add(recuitStochastiqueRadioButton);
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
        final JScrollPane scrollPane1 = new JScrollPane();
        jpanel_result_txt.add(scrollPane1, BorderLayout.CENTER);
        textAreaResultat = new JTextArea();
        scrollPane1.setViewportView(textAreaResultat);
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
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel5.add(scrollPane2, gbc);
        list1 = new JList();
        scrollPane2.setViewportView(list1);
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
        final JScrollPane scrollPane3 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel7.add(scrollPane3, gbc);
        list2 = new JList();
        scrollPane3.setViewportView(list2);
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
        final JScrollPane scrollPane4 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel9.add(scrollPane4, gbc);
        list3 = new JList();
        scrollPane4.setViewportView(list3);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 0);
        jpanel_parametrage.add(panel11, gbc);
        panel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Fichier configuration"));
        downloadConfigButton = new JButton();
        downloadConfigButton.setHorizontalAlignment(11);
        downloadConfigButton.setHorizontalTextPosition(11);
        downloadConfigButton.setText("Télécharger");
        panel11.add(downloadConfigButton);
        chargerFichierConfigurationButton = new JButton();
        chargerFichierConfigurationButton.setHorizontalAlignment(0);
        chargerFichierConfigurationButton.setText("Charger");
        panel11.add(chargerFichierConfigurationButton);
        labelFichierConfig = new JLabel();
        labelFichierConfig.setBackground(new Color(-1));
        labelFichierConfig.setText("Aucun fichier sélectionné");
        panel11.add(labelFichierConfig);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jpanel_root;
    }

    /**
     * Permet de généraliser le niveau de précision pour le curseur de la précision
     */
    public enum NiveauPrecision {

        PRECISION_BASSE(1, 1),
        PRECISION_MOYENNE(2, 2),
        PRECISION_HAUTE(4, 4);

        public int nombrePaliers;
        public int nombreIterations;

        NiveauPrecision(int nombrePaliers, int nombreIterations) {
            this.nombrePaliers = nombrePaliers;
            this.nombreIterations = nombreIterations;
        }
    }


    // ===================================================
    // ====== interface dynamique - ne pas toucher =======
    // ===================================================

    /**
     * Creer les marqueurs sous forme de chaine de caracteres
     *
     * @return les marqueurs sous format chaine de caracteres executables en javascript
     */
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

    /**
     * Genere un marqueur Maps sous format chaine de caractere à partir d'un objet StationVelo
     *
     * @param stationVelo la station de velo
     * @return le marqueur sous forme de chaine de caracteres
     */
    private String genererMarqueur(StationVelo stationVelo) {
        return "new StationVelo(\"" + stationVelo.getNom()
                + "\", {lat:" + stationVelo.getPosition().getLat() + ", lng:" + stationVelo.getPosition().getLng()
                + "}," + "\"" + genererInfoMarqueur(stationVelo) + "\""
                + ", " + stationVelo.genererPositionsDemandesStochastiques() + ")";
    }

    /**
     * Genere le modele d'affichage pour une ligne dans l'infowindow
     *
     * @param etiquette le nom du parametre
     * @param valeur    la valeur du parametre
     * @return l'affichage sous forme de chaine de caractere en html
     */
    private String genererInfoLigne(String etiquette, int valeur) {
        return "<b>" + etiquette + " : </b>" + valeur + "<br/>";
    }

    /**
     * Genere l'infowindow en fonction des valeurs de la station de velo
     *
     * @param stationVelo la station de velo
     * @return l'infowindow javascript voulu
     */
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
        info += genererInfoLigne("x", stationVelo.getX());
        info += "</div></html>";
        return info;
    }

    /**
     * Lance le browser
     */
    private void createScene() {

        Platform.runLater(() -> {

            try {
                // recupere le fichier modele html dans les ressources
                File file = new File("src/graphic/Connector.html");
                org.jsoup.nodes.Document doc = Jsoup.parse(file, "UTF-8");
                org.jsoup.nodes.Element div = doc.getElementById("stations");
                // injecte les marqueurs
                div.text(creerMarqueurs());
                // System.out.println(creerMarqueurs());

                WebView view = new WebView();
                engine = view.getEngine();
                engine.setJavaScriptEnabled(true);
                // System.out.println(doc.toString().replace("&lt;", "<").replace("&gt;", ">"));
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
