package gestionnaireFichier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Permet d'ouvrir un explorateur de fichiers
 */
public class MyJFileChooser extends JPanel {

    public final String NOM_FICHIER_DEFAUT = "resultats_velib.txt";
    private final String CHEMIN_DEFAUT = ".";

    private String cheminChoisi = CHEMIN_DEFAUT;

    public MyJFileChooser() {

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(CHEMIN_DEFAUT));
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        if (chooser.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            cheminChoisi = chooser.getSelectedFile().toString();
        } else {
            cheminChoisi = null;
            System.out.println("No Selection ");
        }

    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }


    public String getCheminChoisi() {
        return cheminChoisi;
    }
}
