package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;

import org.example.ModpackDownload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static org.example.NewSkin.isInternetAvailable;

class Placeholder extends JPanel {
    private JLabel label, label1, label2, label3, imageLabel;
    private JPanel panel;

    public Placeholder() {
        setLayout(new BorderLayout()); // BorderLayout beállítása a fő panelhez
        setBackground(Color.GRAY);
        setBounds(20, 20, 260, 120);

        JPanel imagePanel = new JPanel(new BorderLayout());

        String url = "http://mcapi.komaweb.eu/createmod/icon/java2.png";

        imageLabel = new JLabel();
        imageLabel.setBorder(new LineBorder(Color.GRAY, 5, false));
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        add(imagePanel, BorderLayout.WEST);

        // Szöveg beállítása
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // BoxLayout használata függőleges igazításhoz
        panel.setBackground(Color.GRAY);
        JLabel title = new JLabel("",JLabel.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,14));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(5,5,5,5));

        panel.add(Box.createVerticalGlue()); // Függőleges kitöltés hozzáadása
        panel.add(title);
        panel.add(Box.createVerticalGlue());


        add(panel, BorderLayout.CENTER); // A panelt a fő panel középső részére helyezi
    }



    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, getHeight()); // Fix méret beállítása
    }
}

class NewsComponent extends JPanel {

    private JLabel label, label1, label2, label3, imageLabel;
    private JPanel panel;
    public NewsComponent(String title, String text, String url, String author) {
        setLayout(new BorderLayout()); // BorderLayout beállítása a fő panelhez
        setBackground(Color.GRAY);
        setBounds(20, 20, 260, 120);

        JPanel imagePanel = new JPanel(new BorderLayout());

        if(url == "") {
            url = "http://mcapi.komaweb.eu/createmod/icon/java2.png";
        }

        // Kép letöltése és beállítása
        try {
            URL imageUrl = new URL(url);
            BufferedImage image = ImageIO.read(imageUrl);
            int width = 120;
            int height = 120;
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setBorder(new LineBorder(Color.GRAY, 5, false));
            imagePanel.add(imageLabel, BorderLayout.CENTER);
            add(imagePanel, BorderLayout.WEST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Szöveg beállítása
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // BoxLayout használata függőleges igazításhoz
        panel.setBackground(Color.GRAY);
        JLabel title_label = new JLabel(title,JLabel.CENTER);
        title_label.setFont(new Font("Arial",Font.BOLD,14));
        title_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        title_label.setBorder(new EmptyBorder(5,5,5,5));
        JTextArea textArea = new JTextArea(text, 5, 20); // Sorok és oszlopok száma
        textArea.setLineWrap(true); // Automatikus sortörés beállítása
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(new EmptyBorder(5,0,5,5));
        scrollPane.setPreferredSize(new Dimension(450, getHeight())); // Választható: a JScrollPane mérete

        panel.add(Box.createVerticalGlue()); // Függőleges kitöltés hozzáadása
        panel.add(title_label);
        panel.add(scrollPane);
        panel.add(Box.createVerticalGlue());


        add(panel, BorderLayout.CENTER); // A panelt a fő panel középső részére helyezi
    }



    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, getHeight()); // Fix méret beállítása
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

public class Main {

    JFrame frame;
    JButton weboldal,modok,start_btn, btn1,btn2,btn3,btn4,btn5;
    JPanel panel1, panel2, panel3, hasab1, hasab2, box1, box2, news_panel;
    JTextField text2;
    JLabel label, modpack_title, imageLabel, text1;

    Integer id = -1;

    String selected_version, gamedirector = "", username;
    String url = "", title = "Tölts le egy modpacket!", skinfile;
    Boolean isown = false;

    public JSONObject getVersions(){

        String fileName = "versions.json"; // JSON fájl elérési útvonala és neve
        JSONObject jsonObject = null;

        // JSON parser inicializálása
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            // JSON fájl beolvasása és parse-olása
            jsonObject = (JSONObject) parser.parse(reader);

            if(!jsonObject.isEmpty()) {

                // Kulcsok bejárása és az első kulcs kiválasztása
                Iterator<String> keys = jsonObject.keySet().iterator();
                String firstKey = keys.next();
                // Az első kulcs alapján az első elem lekérése
                JSONObject firstElement = (JSONObject) jsonObject.get(firstKey);
                title = firstKey;
                System.out.println(title);
                isown = (Boolean) firstElement.get("isown");
                gamedirector = firstElement.get("gameDirector").toString();
                selected_version = firstElement.get("version").toString();
                url = firstElement.get("icon").toString();

            }

        } catch (IOException | ParseException e) {
            System.err.println("Hiba történt a fájl olvasása során: " + e.getMessage());
        }

        return jsonObject;

    }

    public void loadProfile(){

        String fileName = "profiles.json"; // JSON fájl elérési útvonala és neve
        JSONObject jsonObject = null;

        // JSON parser inicializálása
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            // JSON fájl beolvasása és parse-olása
            jsonObject = (JSONObject) parser.parse(reader);

            jsonObject.forEach((profileName, profileData) -> {

                JSONObject profile = (JSONObject) profileData;

                if((Boolean) profile.get("selected")){

                    skinfile = (String) profile.get("skin");
                    text1.setText("<html>&nbsp;" + (String) profile.get("name") + "</html>");
                    username = (String) profile.get("name");
                    //text1.setEnabled(false);

                }

            });


        } catch (IOException | ParseException e) {
            System.err.println("Hiba történt a fájl olvasása során: " + e.getMessage());
        }

    }

    Main(){

        SwingUtilities.invokeLater(() -> {

            Database database = new Database();

            frame = new JFrame("Minecraft Launcher");

            // felső menű
            panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel1.setBounds(0,0,1300,50);
            panel1.setBackground(Color.decode("#8B4513"));

            btn1 = new JButton("Beállitások");
            btn2 = new JButton("Modpack letöltése");
            btn3 = new JButton("Modpack létrehozása");
            btn4 = new JButton("Profilok");
            btn5 = new JButton("Skinek");
            btn1.setBackground(Color.decode("#CD853F"));
            btn2.setBackground(Color.decode("#CD853F"));
            btn3.setBackground(Color.decode("#CD853F"));
            btn4.setBackground(Color.decode("#CD853F"));
            btn5.setBackground(Color.decode("#CD853F"));
            btn1.setForeground(Color.BLACK);
            btn2.setForeground(Color.BLACK);
            btn3.setForeground(Color.BLACK);
            btn4.setForeground(Color.BLACK);
            btn5.setForeground(Color.BLACK);
            btn1.setBorder(BorderFactory.createLineBorder(Color.decode("#CD853F"),5,true));
            //btn1.setBorder(new LineBorder(Color.WHITE,5,true));
            btn2.setBorder(BorderFactory.createLineBorder(Color.decode("#CD853F"),5,true));
            btn3.setBorder(BorderFactory.createLineBorder(Color.decode("#CD853F"),5,true));
            btn4.setBorder(BorderFactory.createLineBorder(Color.decode("#CD853F"),5,true));
            btn5.setBorder(BorderFactory.createLineBorder(Color.decode("#CD853F"),5,true));
            btn1.setPreferredSize(new Dimension(160,35));
            btn2.setPreferredSize(new Dimension(160,35));
            btn3.setPreferredSize(new Dimension(160,35));
            btn4.setPreferredSize(new Dimension(160,35));
            btn5.setPreferredSize(new Dimension(160,35));
            btn1.setPressedIcon(null);
            btn2.setPressedIcon(null);
            btn3.setPressedIcon(null);
            btn4.setPressedIcon(null);
            btn5.setPressedIcon(null);

            btn2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Ebben a blokkban hajthatja végre a gomb lenyomására reagáló műveleteket

                    ModpackDownload anotherWindow = new ModpackDownload();
                    anotherWindow.setVisible(true);
                    anotherWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            // Ide kerülhet az a kód, amit futtatni szeretnél, amikor a dialógusablak bezáródik
                            System.out.println("A dialógusablak bezárva.");
                            printModpacks();
                        }
                    });

                }
            });
            btn4.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Ebben a blokkban hajthatja végre a gomb lenyomására reagáló műveleteket

                    Profile anotherWindow = new Profile();
                    anotherWindow.setVisible(true);
                    anotherWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            // Ide kerülhet az a kód, amit futtatni szeretnél, amikor a dialógusablak bezáródik
                            System.out.println("A dialógusablak bezárva.");
                            //printModpacks();
                            loadProfile();
                        }
                    });

                }
            });
            btn5.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Ebben a blokkban hajthatja végre a gomb lenyomására reagáló műveleteket

                    SkinManager anotherWindow = new SkinManager();
                    anotherWindow.setVisible(true);
                    anotherWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            // Ide kerülhet az a kód, amit futtatni szeretnél, amikor a dialógusablak bezáródik
                            System.out.println("A dialógusablak bezárva.");
                            //printModpacks();
                            //loadProfile();
                        }
                    });

                }
            });

            panel1.add(btn1);
            panel1.add(btn2);
            panel1.add(btn3);
            panel1.add(btn4);
            panel1.add(btn5);


            // ---------------------------------------------------------------------------------------
            // középső rész
            panel2 = new JPanel();
            panel2.setLayout(new GridBagLayout());
            panel2.setBounds(0,50,1300,650);
            panel2.setBackground(Color.decode("#CD853F"));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1; // fix szélesség
            gbc.weighty = 1;
            gbc.fill = GridBagConstraints.BOTH;

            // Első panel konfigurálása
            hasab1 = new JPanel();
            hasab1.setLayout(new BoxLayout(hasab1, BoxLayout.Y_AXIS));
            hasab1.setBackground(Color.DARK_GRAY);
            hasab1.setBorder(new EmptyBorder(5, 5, 5, 5));

            // JScrollPane hozzáadása a tartalompanelhez
            JScrollPane scrollPane = new JScrollPane(hasab1);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setPreferredSize(new Dimension(300, 650)); // 200 széles
            panel2.add(scrollPane, gbc);

            printModpacks();

            JPanel hasab2 = new JPanel();
            hasab2.setLayout(new BoxLayout(hasab2, BoxLayout.Y_AXIS)); // BoxLayout használata függőleges igazításhoz
            hasab2.setBackground(Color.decode("#CD853F"));
            hasab2.setPreferredSize(new Dimension(1000, 600)); // Méret beállítása
            hasab2.setBorder(new EmptyBorder(5, 5, 5, 30));

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1; // maradék hely elfoglalása
            gbc.fill = GridBagConstraints.BOTH;
            panel2.add(hasab2, gbc);

            // Gombok panel hozzáadása
            JPanel gombok = new JPanel(new FlowLayout(FlowLayout.LEFT));
            gombok.setPreferredSize(new Dimension(1000, 20));
            gombok.setBorder(new EmptyBorder(0,5,5,5));
            gombok.setBackground(new Color(255, 255, 255, 100)); // Átlátszó háttér beállítása
            gombok.setOpaque(false);
            weboldal = new JButton("Weboldal");

            if(isown){
                weboldal.setEnabled(false);
            } else {
                weboldal.setEnabled(true);
            }

            weboldal.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    System.out.println(selected_version);
                    System.out.println(url);
                    Integer id = null;

                    String version = selected_version.split("-")[0];

                    System.out.println(version);

                    ResultSet result = database.getTable("SELECT * FROM modpacks WHERE version = '" + version + "' AND icon = '" + url + "' AND name = '"+modpack_title.getText()+"'");

                    try {
                        while (result.next()) {
                            id = result.getInt("id");
                            System.out.println(id);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Ebben a blokkban hajthatja végre a gomb lenyomására reagáló műveleteket
                    String modpack_url = "https://minecraft.komaweb.eu/modpack.php?id=" + id;

                    try {
                        Desktop.getDesktop().browse(new URI(modpack_url));
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            });
            gombok.add(weboldal); // Gombok panel tartalma
            modok = new JButton("Modok");
            gombok.add(modok); // Gombok panel tartalma

            modok.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Ebben a blokkban hajthatja végre a gomb lenyomására reagáló műveleteket

                    ModManager anotherWindow = new ModManager(modpack_title.getText());
                    anotherWindow.setVisible(true);
                    anotherWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            // Ide kerülhet az a kód, amit futtatni szeretnél, amikor a dialógusablak bezáródik
                            System.out.println("A dialógusablak bezárva.");
                            printModpacks();
                        }
                    });

                }
            });

            modok.setEnabled(false);
            weboldal.setEnabled(false);

            JPanel szoveg = new JPanel();
            szoveg.setPreferredSize(new Dimension(szoveg.getWidth(), 600));
            szoveg.setBackground(new Color(255, 255, 255, 100)); // Átlátszó háttér beállítása
            szoveg.setOpaque(false);
            //szoveg.setLayout(new BoxLayout(szoveg, BoxLayout.Y_AXIS)); // BoxLayout használata függőleges igazításhoz
            szoveg.add(Box.createVerticalGlue());

            modpack_title = new JLabel(title);
            modpack_title.setFont(new Font("Arial", Font.PLAIN, 24));
            szoveg.add(modpack_title); // A JLabel középre igazítása
            szoveg.add(Box.createVerticalGlue()); // Függőleges kitöltés hozzáadása

            JPanel seged = new JPanel(new FlowLayout(FlowLayout.LEADING));
            seged.setPreferredSize(new Dimension(950,600));
            seged.setBackground(Color.decode("#CD853F"));

            JPanel picture_panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
            picture_panel.setBackground(Color.decode("#CD853F"));
            picture_panel.setPreferredSize(new Dimension(300,610));
            picture_panel.setBorder(new EmptyBorder(18,10,10,10));

            // Kép letöltése és beállítása

            imageLabel = new JLabel();

            if(!modpack_title.getText().isEmpty()) {
                if(selected_version != null){
                    if(selected_version.contains("forge")) {
                        modok.setEnabled(true);
                    }
                }
                if(isown){
                    weboldal.setEnabled(true);
                }
                try {

                    BufferedImage image;
                    URL imageUrl = null;
                    if(isInternetAvailable()) {
                        if (!url.isEmpty()) {
                            imageUrl = new URL(url);
                        } else {
                            imageUrl = new URL("https://minecraft.komaweb.eu/modpacks/vanilla/icon/vanilla.png");
                        }
                        image = ImageIO.read(imageUrl);
                        imageLabel.setBorder(new LineBorder(Color.GRAY, 5, true));
                    } else {
                        image = ImageIO.read(new File("skin_view/head/default.png"));
                        imageLabel.setBorder(new EmptyBorder(2,2,2,2));
                    }

                    int width = 260;
                    int height = 260;
                    Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                weboldal.setEnabled(false);
                modok.setEnabled(false);
            }
            picture_panel.add(imageLabel, BorderLayout.WEST);


            news_panel = new JPanel();
            news_panel.setLayout(new BoxLayout(news_panel, BoxLayout.Y_AXIS));
            news_panel.setBorder(new EmptyBorder(0,10,10,10));
            news_panel.setBackground(Color.decode("#CD853F"));

            JScrollPane newsScrollPane = new JScrollPane(news_panel);
            newsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            newsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            newsScrollPane.setBorder(new EmptyBorder(8,0,0,0));
            newsScrollPane.setPreferredSize(new Dimension(640, 600)); // 200 széles
            //newsScrollPane.setBounds(0,0,640,600);

            /*JLabel teszt = new JLabel("EZ egy teszt a hirrekre");
            news_panel.add(teszt);*/

            printNews();

            seged.add(picture_panel);

            seged.add(newsScrollPane);

            szoveg.add(seged);

            hasab2.add(gombok); // Gombok panel hozzáadása
            hasab2.add(szoveg); // Szöveg panel hozzáadása


            // ---------------------------------------------------------------------------------------
            // alsó sáv
            panel3 = new JPanel();
            panel3.setLayout(new GridBagLayout());
            panel3.setBounds(0,700,1300,40);
            panel3.setBackground(Color.decode("#8B4513"));

            GridBagConstraints gbc2 = new GridBagConstraints();

            box1 = new JPanel();
            box1.setPreferredSize(new Dimension(300, 40)); // 200 széles
            box1.setBackground(Color.decode("#8B4513"));

            box1.setAlignmentY(Component.CENTER_ALIGNMENT);

            gbc2.gridx = 0;
            gbc2.gridy = 0;
            gbc2.weightx = 0; // fix szélesség
            gbc2.fill = GridBagConstraints.BOTH;
            panel3.add(box1, gbc2);

            label = new JLabel("Név: ");
            label.setFont(new Font("Arial",Font.PLAIN,16));


            box1.setOpaque(false);

            text1 = new JLabel("Nincs kiválasztva");
            text1.setPreferredSize(new Dimension(250,29));
            text1.setHorizontalAlignment(JTextField.LEFT);
            text1.setBackground(Color.decode("#CD853F"));
            text1.setForeground(Color.WHITE);
            text1.setBorder(BorderFactory.createLineBorder(Color.decode("#CD853F"),2,true));
            text1.setFont(new Font("Arial",Font.PLAIN,16));
            loadProfile();

            box1.add(label);
            box1.add(text1);

            box2 = new JPanel();
            box2.setPreferredSize(new Dimension(1000, 40)); // 200 széles
            box2.setBackground(Color.decode("#8B4513"));
            gbc2.gridx = 1;
            gbc2.gridy = 0;
            gbc2.weightx = 1; // maradék hely elfoglalása
            gbc2.fill = GridBagConstraints.BOTH;
            panel3.add(box2, gbc2);

            start_btn = new JButton("Inditás");
            start_btn.setBackground(Color.decode("#228B22"));
            start_btn.setForeground(Color.BLACK);
            start_btn.setBorder(BorderFactory.createLineBorder(Color.decode("#228B22"),5,true));
            start_btn.setPreferredSize(new Dimension(160,29));
            start_btn.setPressedIcon(null);
            start_btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    if(selected_version != null && gamedirector != null) {

                        String player = "";
                        if (username.isEmpty()) {
                            Random rand = new Random();
                            int number = rand.nextInt(1000, 9999);
                            player = "Player" + number;
                            username = player;
                            text1.setText("<html>&nbsp;"+player+"</html>");
                        } else {
                            player = username;
                        }

                        String currentDirectory = System.getProperty("user.dir");
                        System.out.println("Current directory: " + currentDirectory);

                        String director = (currentDirectory + "" + gamedirector).replace("\\", "\\\\");

                        String command = "mc_run.exe --gamedirector \"" + director + "\" --version \"" + selected_version + "\" --username \"" + player + "\" --javadir \"C:\\Program Files\\Java\\jdk-17.0.8\\bin\\javaw.exe\"";

                        System.out.println(command);

                        JarFileModifier jarfilemodifier = new JarFileModifier();
                        jarfilemodifier.Modifier(modpack_title.getText().toLowerCase(),selected_version,skinfile,false);

                        // Az elindított folyamat várása egy másik szálon
                        Runtime runtime = Runtime.getRuntime();
                        Thread waitForThread = new Thread(() -> {
                            try {
                                Process process = runtime.exec(command);
                                process.waitFor();
                            } catch (InterruptedException | IOException ee) {
                                ee.printStackTrace();
                            }
                        });
                        waitForThread.start();

                    } else {

                        JOptionPane.showMessageDialog(frame, "Nincs telepítve egy játékverzió sem!");

                    }

                }
            });
            box2.add(start_btn);

            // ---------------------------------------------------------------------------------------

            frame.add(panel1);
            frame.add(panel2);
            frame.add(panel3);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 775);
            frame.setLocationRelativeTo(null);
            frame.setLayout(null);
            frame.setResizable(false);
            frame.setVisible(true);


        });

    }

    public void printNews(){

        news_panel.removeAll();
        news_panel.add(Box.createVerticalStrut(10));

        String version = selected_version.split("-")[0];
        Integer id = null;
        Integer szamlalo = 0;

        if(isInternetAvailable()) {

            Database database = new Database();
            ResultSet result = database.getTable("SELECT * FROM modpacks WHERE version = '" + version + "' AND icon = '" + url + "' AND name = '" + modpack_title.getText() + "'");
            try {
                while (result.next()) {
                    id = result.getInt("id");
                    System.out.println(id);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            Integer news_id = null;
            String author, title, description = null;
            ResultSet news_result = database.getTable("SELECT * FROM news WHERE modpack_id = " + id + " ORDER BY date DESC LIMIT 10");
            if (news_result != null) {
                try {
                    while (news_result.next()) {
                        szamlalo += 1;
                        news_id = news_result.getInt("id");
                        title = news_result.getString("title");
                        description = news_result.getString("description");


                        NewsComponent component = new NewsComponent(title, description,url, "");
                        news_panel.add(component);
                        news_panel.add(Box.createVerticalStrut(10));

                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }


        }

        if(szamlalo < 4){
            for(int i = szamlalo; i<4; i++){
                Placeholder component = new Placeholder();
                news_panel.add(component);
                news_panel.add(Box.createVerticalStrut(10));
            }
        }

        news_panel.add(Box.createVerticalStrut(20));
        news_panel.revalidate();
        news_panel.repaint();
    }

    public void printModpacks(){

        System.out.println("etsfdddsadew");

        JSONObject versions =  getVersions();
        hasab1.removeAll();
        if(versions != null) {
            versions.forEach((modpackName, modpackData) -> {

                String name = String.valueOf(modpackName);

                JSONObject modpack = (JSONObject) modpackData;
                String version = (String) modpack.get("version");
                String gameDirector = (String) modpack.get("gameDirector");
                String icon = (String) modpack.get("icon");
                Boolean isOwn = (Boolean) modpack.get("isown");

                ModpackComponent component = new ModpackComponent(name, version,icon,0);
                component.setAlignmentX(Component.LEFT_ALIGNMENT); // Beállítjuk a balra igazítást
                component.setPreferredSize(new Dimension(300, 100)); // Állítsd be a preferált méretet (100x100)
                component.setBorder(new LineBorder(new Color(255, 255, 255, 100), 5, true));

                component.setMaximumSize(new Dimension(300, 100)); // Állítsd be a maximális méretet (100x100)

                component.addMouseListener(new MouseAdapter() { // Eseménykezelő hozzáadása
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        isown = isOwn;
                        url = icon;
                        selected_version = version;
                        gamedirector = gameDirector;

                        // Kép letöltése és beállítása
                            try {
                                BufferedImage image;
                                if(isInternetAvailable()) {
                                    URL imageUrl = new URL(icon);
                                    image = ImageIO.read(imageUrl);
                                } else {
                                    image = ImageIO.read(new File("skin_view/head/default.png"));
                                    imageLabel.setBorder(new EmptyBorder(2,2,2,2));
                                }

                                int width = 260;
                                int height = 260;
                                Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                                imageLabel.setIcon(new ImageIcon(scaledImage));
                            } catch (IOException ee) {
                                ee.printStackTrace();
                            }

                        modpack_title.setText(name);
                        if(isown){
                            weboldal.setEnabled(false);
                        } else {
                            weboldal.setEnabled(true);
                        }
                        if(selected_version != null){
                            if(selected_version.contains("forge")) {
                                modok.setEnabled(true);
                            } else {
                                modok.setEnabled(false);
                            }
                        } else {
                            modok.setEnabled(false);
                        }

                        printNews();

                    }
                });
                hasab1.add(component);
                hasab1.add(Box.createVerticalStrut(10)); // 10 pixel magas üres tér
            });
            hasab1.revalidate();
            hasab1.repaint();
        }

    }

    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel(new FlatCobalt2IJTheme());
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception ex){
            System.out.println(ex);
        }
        new Main();
    }
}