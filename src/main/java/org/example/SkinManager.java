package org.example;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
//import java.util.Base64;

import org.apache.commons.codec.binary.Base64;

class SkinComponent2 extends JPanel {
    private JLabel label, label1, label2, imageLabel;
    private JPanel panel;
    private JButton btn;

    public SkinComponent2(String text, String icon, Integer id) {
        setLayout(new BorderLayout()); // BorderLayout beállítása a fő panelhez
        setBackground(Color.GRAY);
        setBounds(20, 20, 260, 100);

        System.out.println(icon);

        JLabel title = new JLabel(text);
        title.setHorizontalAlignment(SwingConstants.CENTER); // A cím vízszintesen középre igazítása
        title.setFont(new Font("Arial",Font.BOLD,14));
        title.setBorder(new EmptyBorder(10,5,5,5));
        add(title,BorderLayout.NORTH);

        // Kép letöltése és beállítása
        try {
            //URL imageUrl = new URL(url);
            File images = new File(icon);
            BufferedImage image = ImageIO.read(images);
            int width = 200;
            int height = 300;
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setBorder(new LineBorder(Color.GRAY, 5, true));
            add(imageLabel, BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }

        btn = new JButton("Törlés");
        btn.setFont(new Font("Arial",Font.BOLD,14));

        add(btn, BorderLayout.SOUTH); // A panelt a fő panel középső részére helyezi
        if(icon.contains("default")){
            btn.setEnabled(false);
        }
        setBorder(new EmptyBorder(10,10,10,10));
    }



    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 375); // Fix méret beállítása
    }

    public void setActive(){
        setBackground(Color.DARK_GRAY);
        panel.setBackground(Color.DARK_GRAY);
        imageLabel.setBorder(new LineBorder(Color.DARK_GRAY,5,true));
    }
    public void removeActive(){
        setBackground(Color.GRAY);
        panel.setBackground(Color.GRAY);
        imageLabel.setBorder(new LineBorder(Color.GRAY,5,true));
    }
}

public class SkinManager extends JFrame {

    private JPanel main;

    public static String imageToBase64(String imagePath) throws FileNotFoundException {

        File f = new File(imagePath);
        FileInputStream fin = new FileInputStream(f);
        byte imagebytearray[] = new byte[(int)f.length()];
        try {
            fin.read(imagebytearray);
            String imagetobase64 = Base64.encodeBase64String(imagebytearray);
            fin.close();
            return imagetobase64;
        } catch (IOException e){
            return null;
        }

        /*String base64Image = "";
        File file = new File(imagePath);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a Image file from file system
            byte[] imageData = new byte[(int) file.length()];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return base64Image;*/
    }

    SkinManager() {

        SwingUtilities.invokeLater(() -> {

            setTitle("SkinManager");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            JLabel title = new JLabel("Letöltött skinek");
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setFont(new Font("Arial",Font.BOLD,28));
            title.setBorder(new EmptyBorder(10,5,20,5));

            main = new JPanel(new GridLayout(0, 4, 10, 10)); // Maximum 4 elem egy sorban, több sor engedélyezett
            main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Térköz a panel körül

            // Dimension(200, 375);

            printSkins();

            JScrollPane scrollPane = new JScrollPane(main); // A main panel beágyazása a JScrollPane-be
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Függőleges görgetősáv mindig látható

            JLabel bottom = new JLabel("");
            bottom.setBorder(new EmptyBorder(3,3,3,3));

            add(title, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);
            add(bottom, BorderLayout.SOUTH);
            setSize(1300, 775);
            setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);

        });
    }

    public void printSkins(){

        main.removeAll();

        JPanel uj_skin = new JPanel(new GridBagLayout());
        /*uj_skin.setPreferredSize(new Dimension(200, 375));
        uj_skin.setMaximumSize(new Dimension(200,375));*/
        uj_skin.setBackground(Color.GRAY);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH; // A komponensek kitöltik az rendelkezésre álló teret
        gbc.weighty = 0.85; // Az első komponens magasságának beállítása
        gbc.gridy = 0; // Az első sorba helyezés
        JLabel plusz = new JLabel("+");
        plusz.setFont(new Font("Arial", Font.BOLD, 144));
        plusz.setHorizontalAlignment(SwingConstants.CENTER); // A JLabel középre igazítása vízszintesen
        uj_skin.add(plusz, gbc); // Az első komponens hozzáadása a panelhez
        gbc.weighty = 0.15; // A második komponens magasságának beállítása
        gbc.gridy = 1; // A második sorba helyezés
        JLabel szoveg = new JLabel("Új skin importálása");
        szoveg.setFont(new Font("Arial", Font.BOLD, 16));
        szoveg.setHorizontalAlignment(SwingConstants.CENTER); // A JLabel középre igazítása vízszintesen
        uj_skin.add(szoveg, gbc); // A második komponens hozzáadása a panelhez
        main.add(uj_skin);
        uj_skin.addMouseListener(new MouseAdapter() { // Eseménykezelő hozzáadása
            @Override
            public void mouseClicked(MouseEvent e) {

                System.out.println("Új skin létrehozása");

                NewSkin anotherWindow = new NewSkin();
                anotherWindow.setVisible(true);
                anotherWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        // Ide kerülhet az a kód, amit futtatni szeretnél, amikor a dialógusablak bezáródik
                        System.out.println("A dialógusablak bezárva.");
                        printSkins();
                    }
                });

            }
        });

        List<String> files_list = getFiles();

        for (int i = 0; i < files_list.size(); i++) {

            String file = files_list.get(i);
            SkinComponent2 component2 = new SkinComponent2(file, "skin_view/"+file, i);
            /*component2.setPreferredSize(new Dimension(200, 375));
            component2.setMaximumSize(new Dimension(200,375));*/
            main.add(component2);

        }

        if(files_list.size() < 4){
            for(int i = files_list.size()-1; i<6; i++){

                JPanel uj_skin2 = new JPanel(new GridBagLayout());
                uj_skin2.setBackground(Color.GRAY);
                main.add(uj_skin2);

            }
        }

        main.revalidate();
        main.repaint();

    }

    public static java.util.List<String> getFiles() {

        String path = "skin_view/";
        System.out.println(path);
        List<String> filesList = new ArrayList<>();

        File directory = new File(path);

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null && files.length > 0) {
                for (File file : files) {
                    // Hozzáadja a fájlok és könyvtárak nevét a listához
                    if(file.isFile()) {
                        filesList.add(file.getName());
                    }
                }
            } else {
                System.out.println("A mappa üres.");
            }
        } else {
            System.out.println("A megadott útvonal nem egy mappa. (mods)");
        }
        return filesList;
    }

    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel(new FlatCobalt2IJTheme());
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception ex){
            System.out.println(ex);
        }
        new SkinManager();
    }

}
