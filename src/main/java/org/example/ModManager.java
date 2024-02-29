package org.example;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class FileComponent extends JPanel {
    private JLabel label, label1, label2, imageLabel;
    private JPanel panel;
    private Integer active;

    public FileComponent(String text, String url, Integer id, Integer act) {
        active = act;
        setLayout(new BorderLayout()); // BorderLayout beállítása a fő panelhez
        if(active == 1){
            setBackground(Color.GRAY);
        } else {
            if(active == 2){
                setForeground(Color.GRAY);
            } else {
                setBackground(Color.GRAY);
            }
        }
        setBounds(20, 20, 260, 100);

        if(url == ""){
            url = "https://minecraft.komaweb.eu/modpacks/createmod/icon/createmod.png";
        }

        System.out.println(url);

        // Kép letöltése és beállítása
        BufferedImage image;
        try {
            // Kép panel létrehozása
            JPanel imagePanel = new JPanel(new BorderLayout());
            imagePanel.setBackground(Color.WHITE);

            // Kép betöltése és beállítása
            URL imageUrl = new URL(url);
            image = ImageIO.read(imageUrl);
            System.out.println(image);
            int width = 80;
            int height = 80;
            if (image != null) {
                Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setBackground(Color.WHITE);
                imageLabel.setForeground(Color.WHITE);
                if (active == 1) {
                    //panel.setBackground(Color.decode("#3f8f29"));
                    imageLabel.setBorder(new LineBorder(Color.decode("#556B2F"), 5, false));
                } else {
                    if (active == 2) {
                        imageLabel.setBorder(new LineBorder(Color.GRAY, 5, false));
                    } else {
                        imageLabel.setBorder(new LineBorder(Color.decode("#6B2F30"), 5, false));
                    }
                }
                imagePanel.add(imageLabel, BorderLayout.CENTER);
                add(imagePanel, BorderLayout.WEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Szöveg beállítása
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // BoxLayout használata függőleges igazításhoz
        if(active == 1){
            panel.setBackground(Color.decode("#556B2F"));
        } else {
            if(active == 2){
                panel.setBackground(Color.GRAY);
            } else {
                panel.setBackground(Color.decode("#6B2F30"));
            }
        }

        label1 = new JLabel(text, JLabel.CENTER);
        label1.setForeground(Color.WHITE);
        label1.setAlignmentX(Component.CENTER_ALIGNMENT); // Vízszintes középre igazítás
        label1.setAlignmentY(Component.CENTER_ALIGNMENT); // Függőleges középre igazítás
        label1.setFont(new Font("Arial",Font.BOLD,14));

        panel.add(Box.createVerticalGlue()); // Függőleges kitöltés hozzáadása
        panel.add(label1);
        panel.add(Box.createVerticalGlue()); // Függőleges kitöltés hozzáadása

        add(panel, BorderLayout.CENTER); // A panelt a fő panel középső részére helyezi
    }



    @Override
    public Dimension getPreferredSize() {
        return new Dimension(610, 100); // Fix méret beállítása
    }

    public void setActive(){
        //setBackground(Color.DARK_GRAY);
        panel.setBackground(Color.DARK_GRAY);
        imageLabel.setBorder(new LineBorder(Color.DARK_GRAY,5,false));
        label1.setForeground(Color.WHITE);
        setBackground(Color.GRAY);
    }
    public void removeActive(){
        if(active == 1){
            setBackground(Color.decode("#556B2F"));
            panel.setBackground(Color.decode("#556B2F"));
            imageLabel.setBorder(new LineBorder(Color.decode("#556B2F"),5,false));
        } else {
            setBackground(Color.decode("#6B2F30"));
            panel.setBackground(Color.decode("#6B2F30"));
            imageLabel.setBorder(new LineBorder(Color.decode("#6B2F30"),5,false));
        }
        //label1.setForeground(Color.BLACK);
        setBackground(Color.GRAY);
    }
}

class ModComponent2 extends JPanel {
    private JLabel label, label1, label2, label3, imageLabel;
    private JPanel panel;

    public ModComponent2(String text, String version, String version_mode, String url, Integer id) {
        setLayout(new BorderLayout()); // BorderLayout beállítása a fő panelhez
        setBackground(Color.GRAY);
        setBounds(20, 20, 260, 100);

        JPanel imagePanel = new JPanel(new BorderLayout());

        if(url == ""){
            url = "http://mcapi.komaweb.eu/createmod/icon/java2.png";
            imagePanel.setBackground(Color.WHITE);
        } else {
            imagePanel.setBackground(Color.GRAY);
        }

        // Kép letöltése és beállítása
        try {
            URL imageUrl = new URL(url);
            System.out.println(imageUrl);
            BufferedImage image = ImageIO.read(imageUrl);
            int width = 80;
            int height = 80;
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setBorder(new LineBorder(Color.GRAY, 5, false));
            //add(imageLabel, BorderLayout.WEST);

            imagePanel.add(imageLabel, BorderLayout.CENTER);
            add(imagePanel, BorderLayout.WEST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Szöveg beállítása
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // BoxLayout használata függőleges igazításhoz
        panel.setBackground(Color.GRAY);
        label1 = new JLabel(text, JLabel.CENTER);
        label1.setFont(new Font("Arial",Font.PLAIN,14));
        label2 = new JLabel(version_mode, JLabel.CENTER);
        label3 = new JLabel(version, JLabel.CENTER);

        label1.setAlignmentX(Component.CENTER_ALIGNMENT); // Vízszintes középre igazítás
        label1.setAlignmentY(Component.CENTER_ALIGNMENT); // Függőleges középre igazítás
        label2.setAlignmentX(Component.CENTER_ALIGNMENT); // Vízszintes középre igazítás
        label2.setAlignmentY(Component.CENTER_ALIGNMENT); // Függőleges középre igazítás
        label3.setAlignmentX(Component.CENTER_ALIGNMENT); // Vízszintes középre igazítás
        label3.setAlignmentY(Component.CENTER_ALIGNMENT); // Függőleges középre igazítás

        panel.add(Box.createVerticalGlue()); // Függőleges kitöltés hozzáadása
        panel.add(label1);
        panel.add(label2);
        panel.add(label3);
        panel.add(Box.createVerticalGlue()); // Függőleges kitöltés hozzáadása

        add(panel, BorderLayout.CENTER); // A panelt a fő panel középső részére helyezi
    }



    @Override
    public Dimension getPreferredSize() {
        return new Dimension(260, getHeight()); // Fix méret beállítása
    }

    public void setActive(){
        setBackground(Color.DARK_GRAY);
        panel.setBackground(Color.DARK_GRAY);
        imageLabel.setBorder(new LineBorder(Color.DARK_GRAY,5,false));
    }
    public void removeActive(){
        setBackground(Color.GRAY);
        panel.setBackground(Color.GRAY);
        imageLabel.setBorder(new LineBorder(Color.GRAY,5,false));
    }
}

public class ModManager extends JFrame {

    private JPanel bal, jobb, bal_nav, jobb_nav, bal_data, jobb_data;
    private JScrollPane jobb_scrollPane, bal_scrollPane;
    private JLabel bal_title, jobb_title;

    private JTextField search_text;
    private JButton search_btn, enable_btn, disable_btn, delete_btn;

    private ArrayList<FileComponent> components = new ArrayList<>();
    private Integer last_id = -1;

    private static Integer id = 0;
    private static String file_name = "";

    private static String modpack, gamedirector;

    ModManager(String title) {
        SwingUtilities.invokeLater(() -> {
            modpack = title;
            // Elhelyezési kényszer létrehozása
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;

            getversion();

// Bal oldali panel létrehozása és beállítása
            bal = new JPanel();
            bal.setLayout(new BoxLayout(bal, BoxLayout.Y_AXIS));
            bal.setBackground(Color.decode("#8B4513"));
            bal.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // FlowLayout használata, középre igazítás

            // Üres JLabel hozzáadása a bal oldali panelhez a bal_nav panel előtt
            JLabel emptyLabel = new JLabel();
            emptyLabel.setPreferredSize(new Dimension(650, 10)); // Állítsd be az üres JLabel méretét igény szerint
            bal.add(emptyLabel);

            bal_title = new JLabel("Telepített módok"); // Cím beállítása
            bal_title.setFont(new Font("Arial", Font.PLAIN, 24));
            bal.add(bal_title); // Cím hozzáadása a bal oldali panelhez
            // Üres JLabel hozzáadása a bal oldali panelhez a bal_nav panel előtt
            emptyLabel = new JLabel();
            emptyLabel.setPreferredSize(new Dimension(650, 10)); // Állítsd be az üres JLabel méretét igény szerint
            bal.add(emptyLabel);

            // Bal oldali navigációs panel létrehozása és beállítása
            bal_nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
            bal_nav.setBackground(Color.decode("#CD853F"));
            bal_nav.setPreferredSize(new Dimension(630, 50)); // Állítsd be a bal_nav panel méretét igény szerint
            bal_nav.setBorder(new EmptyBorder(5,5,10,10));
            bal.add(bal_nav); // Bal oldali navigációs panel hozzáadása a bal oldali panelhez


            delete_btn = new JButton("Törlés");
            delete_btn.setForeground(Color.BLACK);
            delete_btn.setBackground(Color.decode("#FF0000"));
            delete_btn.setPreferredSize(new Dimension(100,30));
            delete_btn.setBorder(new EmptyBorder(5,5,5,5));
            bal_nav.add(delete_btn); // JButton hozzáadása a panelhez
            delete_btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    removefile(file_name);
                }
            });

            disable_btn = new JButton("Letíltás");
            disable_btn.setForeground(Color.BLACK);
            disable_btn.setBackground(Color.decode("#FFA500"));
            disable_btn.setPreferredSize(new Dimension(100,30));
            disable_btn.setBorder(new EmptyBorder(5,5,5,5));
            bal_nav.add(disable_btn); // JButton hozzáadása a panelhez
            disable_btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    disableFile(file_name);
                }
            });

            enable_btn = new JButton("Engedélyezés");
            enable_btn.setForeground(Color.BLACK);
            enable_btn.setBackground(Color.decode("#228B22"));
            enable_btn.setPreferredSize(new Dimension(100,30));
            enable_btn.setBorder(new EmptyBorder(5,5,5,5));
            bal_nav.add(enable_btn); // JButton hozzáadása a panelhez
            enable_btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    enableFile(file_name);
                }
            });

            emptyLabel = new JLabel();
            emptyLabel.setPreferredSize(new Dimension(650, 10)); // Állítsd be az üres JLabel méretét igény szerint
            bal.add(emptyLabel);

            // Bal oldali navigációs panel létrehozása és beállítása
            //bal_data = new JPanel(new FlowLayout(FlowLayout.LEFT));
            bal_data = new JPanel();
            bal_data.setLayout(new BoxLayout(bal_data, BoxLayout.Y_AXIS));
            bal_data.setBorder(new LineBorder(Color.decode("#CD853F"), 5, true));
            bal_data.setBackground(Color.decode("#CD853F"));

            //bal_data.setPreferredSize(new Dimension(630, 620)); // Állítsd be a bal_nav panel méretét igény szerint
            //bal_data.setBorder(new EmptyBorder(5,5,10,10));
            //bal.add(bal_data); // Bal oldali navigációs panel hozzáadása a bal oldali panelhez

            bal_scrollPane = new JScrollPane(bal_data);
            bal_scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            bal_scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            //bal_scrollPane.setBorder(new EmptyBorder(5,5,10,10));
            bal_scrollPane.setPreferredSize(new Dimension(635, 620));
            //bal_scrollPane.setBounds(0,0,630,620);
            bal.add(bal_scrollPane);

            printFiles();

// Jobb oldali panel létrehozása és beállítása
            jobb = new JPanel();
            jobb.setLayout(new BoxLayout(jobb, BoxLayout.Y_AXIS));
            jobb.setBackground(Color.decode("#8B4513"));
            jobb.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // FlowLayout használata, középre igazítás

            // Üres JLabel hozzáadása a jobb oldali panelhez a jobb_title label előtt
            JLabel emptyLabel2 = new JLabel();
            emptyLabel2.setPreferredSize(new Dimension(650, 10)); // Állítsd be az üres JLabel méretét igény szerint
            jobb.add(emptyLabel2);

            jobb_title = new JLabel("Módok telepítése"); // Cím beállítása
            jobb_title.setFont(new Font("Arial", Font.PLAIN, 24));
            jobb.add(jobb_title); // Cím hozzáadása a jobb oldali panelhez

            // Üres JLabel hozzáadása a jobb oldali panelhez a jobb_title label előtt
            emptyLabel2 = new JLabel();
            emptyLabel2.setPreferredSize(new Dimension(650, 10)); // Állítsd be az üres JLabel méretét igény szerint
            jobb.add(emptyLabel2);

            // Jobb oldali navigációs panel létrehozása és beállítása
            jobb_nav = new JPanel(new GridBagLayout()); // GridBagLayout használata
            jobb_nav.setBackground(Color.decode("#CD853F"));
            jobb_nav.setBorder(new EmptyBorder(10, 10, 10, 10));
            jobb_nav.setPreferredSize(new Dimension(630, 50));
            jobb.add(jobb_nav); // Bal oldali navigációs panel hozzáadása a bal oldali panelhez

            gbc.weightx = 3.0; // JTextfield súlya (2/3)
            gbc.gridx = 0;
            gbc.gridy = 0;

            search_text = new JTextField();
            search_text.setPreferredSize(new Dimension(200, 30)); // JTextField mérete
            jobb_nav.add(search_text, gbc); // JTextfield hozzáadása a panelhez

            // Elhelyezési kényszer újrahasznosítása
            gbc.weightx = 1.0; // JButton súlya (1/3)
            gbc.gridx = 1;

            search_btn = new JButton("Keresés");
            search_btn.setForeground(Color.BLACK);
            search_btn.setBackground(Color.decode("#228B22"));
            search_btn.setBorder(new EmptyBorder(5,5,5,5));
            jobb_nav.add(search_btn, gbc); // JButton hozzáadása a panelhez
            search_btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        searchMod(search_text.getText());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            // Üres JLabel hozzáadása a jobb oldali panelhez a jobb_title label előtt
            emptyLabel2 = new JLabel();
            emptyLabel2.setPreferredSize(new Dimension(650, 10)); // Állítsd be az üres JLabel méretét igény szerint
            jobb.add(emptyLabel2);

            // Bal oldali navigációs panel létrehozása és beállítása
            //jobb_data = new JPanel(new FlowLayout(FlowLayout.LEFT));
            jobb_data = new JPanel();
            jobb_data.setLayout(new BoxLayout(jobb_data, BoxLayout.Y_AXIS));
            jobb_data.setBackground(Color.decode("#CD853F"));
            jobb_data.setBorder(new LineBorder(Color.decode("#CD853F"), 5, true));
            //jobb_data.setPreferredSize(new Dimension(630, 620)); // Állítsd be a bal_nav panel méretét igény szerint
            //jobb_data.setBorder(new EmptyBorder(5,5,10,10));
            //jobb.add(jobb_data); // Bal oldali navigációs panel hozzáadása a bal oldali panelhez

            jobb_scrollPane = new JScrollPane(jobb_data);
            jobb_scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jobb_scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            //jobb_scrollPane.setBounds(0,0,630,620);
            jobb_scrollPane.setPreferredSize(new Dimension(635, 620));

            //jobb_scrollPane.setBorder(new EmptyBorder(5,5,10,10));
            jobb.add(jobb_scrollPane);

            // Keret beállítása és panel hozzáadása
            setTitle("ModManager");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(1, 2)); // GridLayout használata egy sorral és két oszloppal
            add(bal); // Bal oldali panel hozzáadása
            add(jobb); // Jobb oldali panel hozzáadása
            setSize(1300, 775);
            setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);

        });
    }

    public void getversion(){

        String fileName = "versions.json"; // JSON fájl elérési útvonala és neve
        JSONObject jsonObject = null;

        // JSON parser inicializálása
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            // JSON fájl beolvasása és parse-olása
            jsonObject = (JSONObject) parser.parse(reader);

            JSONObject element = (JSONObject) jsonObject.get(modpack);

            gamedirector = element.get("gameDirector").toString();


        } catch (IOException | ParseException e) {
            System.err.println("Hiba történt a fájl olvasása során: " + e.getMessage());
        }

    }

    private void searchMod(String query) throws IOException {

        jobb_data.removeAll();

        Modrinth modrinth = new Modrinth();

        List<Map<String, String>> mods = modrinth.modSearch(query);
        System.out.println("Mods found:");
        for (Map<String, String> mod : mods) {
            System.out.println(mod.get("name"));

            //FileComponent modcomponent = new FileComponent(mod.get("name"),mod.get("icon"),0,2);
            ModComponent2 modcomponent = new ModComponent2(mod.get("name"),"","",mod.get("icon"),0);
            modcomponent.setBorder(new LineBorder(new Color(255, 255, 255, 100), 5, true));

            //modcomponent.setPreferredSize(new Dimension(650,60));
            /*modcomponent.setPreferredSize(new Dimension(650,60));
            modcomponent.setMaximumSize(new Dimension(650, 60));*/

            modcomponent.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    System.out.println(mod.get("href"));

                    ModDownload anotherWindow = new ModDownload(mod.get("href"),mod.get("icon"),gamedirector);
                    anotherWindow.setVisible(true);
                    anotherWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            // Ide kerülhet az a kód, amit futtatni szeretnél, amikor a dialógusablak bezáródik
                            System.out.println("A dialógusablak bezárva.");
                        }
                    });

                }
            });

            jobb_data.add(modcomponent);
            jobb_data.add(Box.createVerticalStrut(10)); // 10 pixel magas üres tér

        }

        jobb_data.revalidate();
        jobb_data.repaint();

    }

    public void printFiles(){

        components.clear();
        bal_data.removeAll();

        List<String> files_list = getFiles();

        for (int i = 0; i < files_list.size(); i++) {
            String file = files_list.get(i);

            System.out.println(file.contains("disabled_"));

            FileComponent filecomponent;

            if(file.contains("disabled_")){
                file = file.replace("disabled_","");
                filecomponent = new FileComponent(file, "http://mcapi.komaweb.eu/createmod/icon/java2.png", i, 0);
                System.out.println(file);
            } else {
                //https://minecraft.komaweb.eu/java.png
                filecomponent = new FileComponent(file, "http://mcapi.komaweb.eu/createmod/icon/java2.png", i,1);
                //filecomponent.setBackground(Color.GREEN);
            }
            //filecomponent.setBorder(new LineBorder(new Color(255, 255, 255, 100), 5, true));
            filecomponent.setPreferredSize(new Dimension(650,100));
            filecomponent.setMaximumSize(new Dimension(650, 100)); // Állítsd be a maximális méretet (100x100)
            filecomponent.setBorder(new LineBorder(new Color(255, 255, 255, 100), 5, false));
            bal_data.add(filecomponent);
            bal_data.add(Box.createVerticalStrut(10)); // 10 pixel magas üres tér
            components.add(filecomponent);

            int finalId = i; // Az 'i' értékét le kell fagyasztani az aktuális ciklus lépése során

            String finalFile = file;
            filecomponent.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //System.out.println("MyComponent clicked! ID: " + finalId);
                    if (last_id != finalId) {
                        file_name = finalFile;
                        filecomponent.setActive();
                        if (last_id != -1) {
                            components.get(last_id).removeActive();
                        }
                        last_id = finalId;
                    }
                }
            });
            bal_data.revalidate();
            bal_data.repaint();
        }
    }

    public static List<String> getFiles() {

        String[] dirs = gamedirector.split("\\\\");

        //System.out.println(modenable[2]);

        String modenable_path = (dirs[1]+"/"+dirs[2]+"/mods").replace("\\","/");
        System.out.println(modenable_path);
        String modisable_path = (dirs[1]+"/"+dirs[2]+"/mods_disable").replace("\\","/");
        List<String> filesList = new ArrayList<>();

        File directory = new File(modenable_path);
        File directory2 = new File(modisable_path);

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null && files.length > 0) {
                for (File file : files) {
                    // Hozzáadja a fájlok és könyvtárak nevét a listához
                    filesList.add(file.getName());
                }
            } else {
                System.out.println("A mappa üres.");
            }
        } else {
            System.out.println("A megadott útvonal nem egy mappa. (mods)");
        }
        if (directory2.isDirectory()) {
            File[] files = directory2.listFiles();

            if (files != null && files.length > 0) {
                for (File file : files) {
                    // Hozzáadja a fájlok és könyvtárak nevét a listához
                    filesList.add("disabled_"+file.getName());
                }
            } else {
                System.out.println("A mappa üres.");
            }
        } else {
            System.out.println("A megadott útvonal nem egy mappa. (mods_disable)");
        }
        return filesList;
    }

    public boolean removefile(String filename){

        String[] dirs = gamedirector.split("\\\\");

        System.out.println(filename);

        // Fájl objektum létrehozása
        File file = new File((dirs[1]+"/"+dirs[2]+"/mods/")+filename);
        if(!file.exists()){
            file = new File((dirs[1]+"/"+dirs[2]+"/mods_disable/")+filename);
        }

        // Ellenőrizzük, hogy a fájl létezik-e
        if (file.exists()) {
            // Fájl törlése
            if (file.delete()) {
                System.out.println("A fájl sikeresen törölve lett.");
                last_id = -1;
            } else {
                System.out.println("Nem sikerült törölni a fájlt.");
            }
        } else {
            System.out.println("A fájl nem létezik.");
        }

        printFiles();

        return true;
    }

    public boolean enableFile(String filename){

        String[] dirs = gamedirector.split("\\\\");

        // Forrásfájl és célhely létrehozása
        File sourceFile = new File((dirs[1]+"/"+dirs[2]+"/mods_disable/")+filename);
        File destinationFile = new File((dirs[1]+"/"+dirs[2]+"/mods/")+filename);

        try {
            // Fájl áthelyezése
            if (sourceFile.renameTo(destinationFile)) {
                System.out.println("A fájl sikeresen áthelyezve lett.");
            } else {
                System.out.println("Nem sikerült áthelyezni a fájlt.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        printFiles();

        return true;

    }

    public boolean disableFile(String filename){

        String[] dirs = gamedirector.split("\\\\");

        // Forrásfájl és célhely létrehozása
        File sourceFile = new File((dirs[1]+"/"+dirs[2]+"/mods/")+filename);
        File destinationFile = new File((dirs[1]+"/"+dirs[2]+"/mods_disable/")+filename);

        try {
            // Fájl áthelyezése
            if (sourceFile.renameTo(destinationFile)) {
                System.out.println("A fájl sikeresen áthelyezve lett.");
            } else {
                System.out.println("Nem sikerült áthelyezni a fájlt.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        printFiles();

        return true;

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception ex) {
            System.out.println(ex);
        }
        new ModManager("CreateMod");
    }

}
