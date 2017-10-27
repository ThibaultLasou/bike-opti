package gestionnaireFichier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class MyJFileChooser extends JPanel {

    public final String NOM_FICHIER_DEFAUT = "resultats_velib.txt";
    private final String CHEMIN_DEFAUT = ".";

    private String cheminChoisi = CHEMIN_DEFAUT;

    public MyJFileChooser() {

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(CHEMIN_DEFAUT));
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        if (chooser.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
            cheminChoisi = chooser.getSelectedFile().toString();
            System.out.println(cheminChoisi);
        } else {
            cheminChoisi = null;
            System.out.println("No Selection ");
        }

    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    /*void ouvrirExplorateurFichier() {
        JFrame frame = new JFrame("");
        MyJFileChooser panel = new MyJFileChooser();
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
    }*/

    public String getCheminChoisi() {
        return cheminChoisi;
    }
}
