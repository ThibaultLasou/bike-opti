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

    private javax.swing.JFileChooser chooser;
    private String choosertitle;

    public MyJFileChooser() {
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
    }

    public String getCheminChoisi() {
        return cheminChoisi;
    }
}
