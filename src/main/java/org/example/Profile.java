package org.example;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

class ProfileComponent extends JPanel {
    private JLabel label, label1, label2, imageLabel;
    private JPanel panel;

    public ProfileComponent(String text, String icon, Integer id) {
        setLayout(new BorderLayout()); // BorderLayout beállítása a fő panelhez
        setBackground(Color.GRAY);
        setBounds(20, 20, 260, 100);

        System.out.println(icon);

        // Kép letöltése és beállítása
        try {
            //URL imageUrl = new URL(url);
            File images = new File(icon);
            BufferedImage image = ImageIO.read(images);
            int width = 85;
            int height = 85;
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setBorder(new LineBorder(Color.GRAY, 5, true));
            add(imageLabel, BorderLayout.WEST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Szöveg beállítása
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // BoxLayout használata függőleges igazításhoz
        panel.setBackground(Color.GRAY);
        label1 = new JLabel(text, JLabel.CENTER);
        label1.setFont(new Font("Arial",Font.BOLD,14));

        label1.setAlignmentX(Component.CENTER_ALIGNMENT); // Vízszintes középre igazítás
        label1.setAlignmentY(Component.CENTER_ALIGNMENT); // Függőleges középre igazítás

        panel.add(Box.createVerticalGlue()); // Függőleges kitöltés hozzáadása
        panel.add(label1);
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
        imageLabel.setBorder(new LineBorder(Color.DARK_GRAY,5,true));
    }
    public void removeActive(){
        setBackground(Color.GRAY);
        panel.setBackground(Color.GRAY);
        imageLabel.setBorder(new LineBorder(Color.GRAY,5,true));
    }
}

public class Profile extends JFrame {

    private JPanel panel1, panel2, panel3, hasab1, playerInfoPanel;
    private JLabel player_title, image_label, uuid;
    private static JButton btn1, selectbtn;
    String title = "Hozz létre egy profil-t!", skin = "default.png", useruid;
    Boolean selected;

    public JSONObject getProfiles(){

        String fileName = "profiles.json"; // JSON fájl elérési útvonala és neve
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
                skin = (String) firstElement.get("skin");
                useruid = firstElement.get("uuid").toString();
                selected = (Boolean) firstElement.get("selected");

            }

        } catch (IOException | ParseException e) {
            System.err.println("Hiba történt a fájl olvasása során: " + e.getMessage());
        }

        return jsonObject;

    }

    Profile(){
        SwingUtilities.invokeLater(() -> {

            panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel1.setBounds(0,0,1300,50);
            panel1.setBackground(Color.decode("#8B4513"));

            btn1 = new JButton("Új profil");
            btn1.setBackground(Color.decode("#CD853F"));
            btn1.setForeground(Color.BLACK);
            btn1.setBorder(BorderFactory.createLineBorder(Color.decode("#CD853F"),5,false));
            btn1.setPreferredSize(new Dimension(160,35));
            btn1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    NewProfile anotherWindow = new NewProfile();
                    anotherWindow.setVisible(true);
                    anotherWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            // Ide kerülhet az a kód, amit futtatni szeretnél, amikor a dialógusablak bezáródik
                            System.out.println("A dialógusablak bezárva.");
                            printProfiles();
                        }
                    });
                }
            });

            panel1.add(btn1);

    // KÖZÉPSŐŐ

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
            hasab1.setBorder(new EmptyBorder(7, 7, 7, 5));

            // JScrollPane hozzáadása a tartalompanelhez
            JScrollPane scrollPane = new JScrollPane(hasab1);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setPreferredSize(new Dimension(300, 650)); // 200 széles
            panel2.add(scrollPane, gbc);

            JPanel hasab2 = new JPanel();
            hasab2.setLayout(new BoxLayout(hasab2, BoxLayout.Y_AXIS)); // BoxLayout használata függőleges igazításhoz
            hasab2.setBackground(Color.decode("#CD853F"));
            hasab2.setPreferredSize(new Dimension(1000, 600)); // Méret beállítása
            hasab2.setBorder(new EmptyBorder(3, 1, 3, 18));

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1; // maradék hely elfoglalása
            gbc.fill = GridBagConstraints.BOTH;
            panel2.add(hasab2, gbc);



            // PLAYER INFOOO

            // Fő tartálypanel létrehozása
            JPanel containerPanel = new JPanel();
            containerPanel.setLayout(new BorderLayout());

            getProfiles();

            // Cím label létrehozása
            player_title = new JLabel(title, SwingConstants.CENTER);
            player_title.setFont(new Font("Arial", Font.PLAIN, 24));
            player_title.setHorizontalAlignment(SwingConstants.CENTER);
            player_title.setBorder(new EmptyBorder(30,5,5,5));
            containerPanel.add(player_title, BorderLayout.NORTH);

            // Hasábok panel létrehozása
            JPanel columnsPanel = new JPanel();
            columnsPanel.setLayout(new GridLayout(1, 2)); // Két hasáb egyforma szélességgel
            containerPanel.add(columnsPanel, BorderLayout.CENTER);

            // Bal oldali hasáb (skin) létrehozása
            JPanel skinPanel = new JPanel();
            skinPanel.setLayout(new BorderLayout());

            // IMAGE
            // Egyik hasáb, amely a képet tartalmazza
            JPanel imagePanel = new JPanel();
            imagePanel.setLayout(new BorderLayout());
            imagePanel.setPreferredSize(new Dimension(500, 600)); // A méret módosítása
            imagePanel.add(Box.createVerticalStrut(10), BorderLayout.NORTH); // Üres kitöltés hozzáadása a hasáb tetejére
            image_label = new JLabel();
            image_label.setBorder(new EmptyBorder(3,20,3,3));
            if(skin != null) {
                BufferedImage image = null;
                try {
                    image = ImageIO.read(new File("skin_view/" + skin));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                int width = 260;
                int height = 420;
                Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                image_label.setIcon(new ImageIcon(scaledImage));
            }
            image_label.setBackground(Color.WHITE);
            imagePanel.add(image_label);
            skinPanel.add(imagePanel);
            // Ide jön a skin
            columnsPanel.add(skinPanel);

            // Jobb oldali hasáb (játékos információk és gombok) létrehozása
            playerInfoPanel = new JPanel();
            playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));

            uuid = new JLabel(useruid);
            playerInfoPanel.add(Box.createVerticalStrut(90));
            playerInfoPanel.add(uuid);
            selectbtn = new JButton("Kiválasztás");
            selectbtn.setBackground(Color.decode("#228B22"));
            selectbtn.setForeground(Color.BLACK);
            selectbtn.setBorder(new EmptyBorder(7,7,7,7));
            selectbtn.setFont(new Font("Arial",Font.PLAIN,14));
            selectbtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    selectbtn.setEnabled(false);

                    String key = player_title.getText();

                    Gson gson = new Gson();

                    try (FileReader reader = new FileReader("profiles.json")) {
                        // JSON fájl beolvasása JsonObject-ba
                        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

                        // A többi elem selected értékének módosítása false-ra
                        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                            if (!entry.getKey().equals(key)) {
                                JsonObject playerObject = (JsonObject) entry.getValue();
                                playerObject.addProperty("selected", false);
                            } else {
                                JsonObject playerObject = (JsonObject) entry.getValue();
                                playerObject.addProperty("selected", true);
                            }
                        }

                        // Módosított JSON mentése a fájlba
                        /*try (FileWriter writer = new FileWriter("profiles.json")) {
                            gson.toJson(jsonObject, writer);
                        }*/

                        // Fájl tartalmának frissítése az új JSON objektummal
                        try (FileWriter file = new FileWriter("profiles.json")) {
                            // Gson inicializálása a szép formázás céljából
                            Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
                            String prettyJsonString = gson2.toJson(jsonObject); // Szépen formázott JSON sztring generálása
                            file.write(prettyJsonString); // Szépen formázott JSON sztring kiírása a fájlba
                        } catch (IOException ee) {
                            System.err.println("Hiba történt a fájl írása során: " + ee.getMessage());
                        }

                        System.out.println("A Kende aljson-ban a selected érték sikeresen módosítva lett.");
                    } catch (IOException ee) {
                        ee.printStackTrace();
                    }
                    printProfiles();

                }
            });
            playerInfoPanel.add(Box.createVerticalStrut(10));
            if(selected == null){
                selectbtn.setEnabled(false);
            } else {
                if (selected) {
                    selectbtn.setEnabled(false);
                } else {
                    selectbtn.setEnabled(true);
                }
            }
            playerInfoPanel.add(selectbtn);

            columnsPanel.add(playerInfoPanel);

            // A tartálypanel hozzáadása a fő panelhez
            hasab2.add(containerPanel);


            // ALSÓÓÓ

            panel3 = new JPanel();
            panel3.setLayout(new GridBagLayout());
            panel3.setBounds(0,700,1300,40);
            panel3.setBackground(Color.decode("#8B4513"));


            setTitle("Profile");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            add(panel1);
            add(panel2);
            add(panel3);
            setSize(1300, 775);
            setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);

            printProfiles();

        });
    }

    public void printProfiles(){

        //System.out.println("etsfdddsadew");

        JSONObject versions =  getProfiles();
        hasab1.removeAll();
        if(versions != null) {
            versions.forEach((ProfileName, profileData) -> {

                String name = String.valueOf(ProfileName);

                JSONObject profile = (JSONObject) profileData;
                String username = (String) profile.get("name");
                String uuids = (String) profile.get("uuid");
                String icon = (String) profile.get("icon");
                String skin = (String) profile.get("skin");
                Boolean selected = (Boolean) profile.get("selected");

                System.out.println(name);

                ProfileComponent component = new ProfileComponent(name, icon,0);
                component.setAlignmentX(Component.LEFT_ALIGNMENT); // Beállítjuk a balra igazítást
                component.setPreferredSize(new Dimension(300, 100)); // Állítsd be a preferált méretet (100x100)
                component.setBorder(new LineBorder(new Color(255, 255, 255, 100), 5, true));

                component.setMaximumSize(new Dimension(300, 100)); // Állítsd be a maximális méretet (100x100)

                component.addMouseListener(new MouseAdapter() { // Eseménykezelő hozzáadása
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if(selected){
                            selectbtn.setEnabled(false);
                        } else {
                            selectbtn.setEnabled(true);
                        }

                        player_title.setText(username);
                        uuid.setText(uuids);

                        File exist_test = new File("skin_view/"+skin);
                        if(!exist_test.exists()) {

                            try {
                                // Fájl elérési útja
                                String filePath = "skin_generator.exe";
                                // ProcessBuilder létrehozása
                                ProcessBuilder builder = new ProcessBuilder(filePath, "--input", "skins/" + skin,"--mode","full", "--output", "skin_view/" + skin);
                                // Standard I/O csatornák öröklése
                                builder.inheritIO();
                                // Folyamat elindítása
                                Process process = builder.start();
                                // Folyamat leállítása
                                int exitCode = process.waitFor();
                                System.out.println("Folyamat leállt kóddal: " + exitCode);
                            } catch (IOException | InterruptedException ee) {
                                ee.printStackTrace();
                            }

                        }

                        BufferedImage image = null;
                        try {
                            image = ImageIO.read(new File("skin_view/"+skin));
                        } catch (IOException ee) {
                            throw new RuntimeException(ee);
                        }
                        int width = 260;
                        int height = 420;
                        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        image_label.setIcon(new ImageIcon(scaledImage));


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
        new Profile();
    }

}
