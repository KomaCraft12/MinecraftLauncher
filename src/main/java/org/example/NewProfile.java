package org.example;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

class SkinComponent extends JPanel {
    private JLabel label, label1, label2, label3, imageLabel;
    private JPanel panel;

    public SkinComponent(String text, String picture_path, Integer id) {
        setLayout(new BorderLayout()); // BorderLayout beállítása a fő panelhez
        setBackground(Color.GRAY);
        setBounds(20, 20, 260, 100);

        JPanel imagePanel = new JPanel(new BorderLayout());

        // Kép letöltése és beállítása
        try {
            BufferedImage image = ImageIO.read(new File(picture_path));
            int width = 80;
            int height = 140;
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

        label1.setAlignmentX(Component.CENTER_ALIGNMENT); // Vízszintes középre igazítás
        label1.setAlignmentY(Component.CENTER_ALIGNMENT); // Függőleges középre igazítás

        panel.add(Box.createVerticalGlue()); // Függőleges kitöltés hozzáadása
        panel.add(label1);
        panel.add(Box.createVerticalGlue()); // Függőleges kitöltés hozzáadása

        add(panel, BorderLayout.CENTER); // A panelt a fő panel középső részére helyezi
    }



    @Override
    public Dimension getPreferredSize() {
        return new Dimension(260, 140); // Fix méret beállítása
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

public class NewProfile  extends JFrame {

    private ArrayList<SkinComponent> components = new ArrayList<>();
    private Integer last_id = -1;
    private JSONObject data = new JSONObject();
    private String skin_name = "default.png";

    public JSONObject getProfiles(){
        String fileName = "profiles.json"; // JSON fájl elérési útvonala és neve
        JSONObject jsonObject = null;
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(fileName)) {
            jsonObject = (JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            System.err.println("Hiba történt a fájl olvasása során: " + e.getMessage());
        }
        return jsonObject;
    }
    NewProfile(){
        SwingUtilities.invokeLater(() -> {

            JPanel mainPanel = new JPanel();
            JPanel skin_list = new JPanel();
            skin_list.setBounds(0,0,270,1000);
            skin_list.setLayout(new BoxLayout(skin_list,BoxLayout.Y_AXIS));
            skin_list.setBorder(new EmptyBorder(5,5,5,5));

            JLabel titleLabel = new JLabel("Új profil létrehozása");
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            mainPanel.add(titleLabel);

            JLabel nameLabel = new JLabel("Név");
            nameLabel.setPreferredSize(new Dimension(295,30));
            nameLabel.setHorizontalAlignment(SwingConstants.LEADING);

            JTextField nameField = new JTextField();
            nameField.setPreferredSize(new Dimension(295,30));

            JLabel skinLabel = new JLabel("Skinek: ");
            skinLabel.setPreferredSize(new Dimension(295,30));
            skinLabel.setHorizontalAlignment(SwingConstants.LEADING);

            JScrollPane scrollPane = new JScrollPane(skin_list);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setPreferredSize(new Dimension(295,400));

            //skin_list.add(new SkinComponent("Default","skin_view/default.png",0));

            List<String> files_list = getFiles();

            for (int i = 0; i < files_list.size(); i++) {

                String file = files_list.get(i);

                SkinComponent skincomponent = new SkinComponent(file,"skin_view/"+file,i);

                int id = i;

                skin_list.add(skincomponent);
                components.add(skincomponent);
                skin_list.add(Box.createVerticalStrut(10));
                skincomponent.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("MyComponent clicked! ID: " + id);
                        if(last_id != id) {

                            skin_name = file;

                            skincomponent.setActive();
                            if (last_id != -1) {
                                components.get(last_id).removeActive();
                            }
                            last_id = id;
                        }
                    }
                });

            }

            JButton createButton = new JButton("Profil létrehozása");
            createButton.setPreferredSize(new Dimension(295,30));
            createButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    if(!nameField.getText().isEmpty()) {

                        System.out.println("Létrehozás: " + skin_name);
                        JSONObject profiles = getProfiles();
                        Iterator<String> keys = profiles.keySet().iterator();
                        Boolean found = false;
                        while (keys.hasNext()) {
                            String key = keys.next();
                            if (Objects.equals(key, nameField.getText())) {
                                found = true;
                            }
                        }
                        System.out.println(found);

                        if (!found) {

                            UUID uuid = UUID.randomUUID();

                            // Az új modpack hozzáadása
                            JSONObject profile = new JSONObject();
                            profile.put("name", nameField.getText());
                            profile.put("uuid", uuid);
                            profile.put("icon", "skin_view/head/" + skin_name);
                            profile.put("skin", skin_name);
                            profile.put("selected", false);

                            profiles.put(nameField.getText(), profile); // Új modpack hozzáadása a jsonObject-hez

                            // Fájl tartalmának frissítése az új JSON objektummal
                            try (FileWriter file = new FileWriter("profiles.json")) {
                                // Gson inicializálása a szép formázás céljából
                                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                String prettyJsonString = gson.toJson(profiles); // Szépen formázott JSON sztring generálása
                                file.write(prettyJsonString); // Szépen formázott JSON sztring kiírása a fájlba

                                dispose();

                            } catch (IOException ee) {
                                System.err.println("Hiba történt a fájl írása során: " + ee.getMessage());
                                JOptionPane.showMessageDialog(mainPanel, "Hiba történt a kiválasztott elem feldolgozása során!",
                                        "Hiba", JOptionPane.ERROR_MESSAGE);
                            }

                        }

                    } else {

                        JOptionPane.showMessageDialog(mainPanel, "A név mező nem lehet üres!",
                                "Hiba", JOptionPane.ERROR_MESSAGE);

                    }

                }
            });

            mainPanel.add(nameLabel);
            mainPanel.add(nameField);
            mainPanel.add(skinLabel);
            mainPanel.add(scrollPane);
            mainPanel.add(createButton);

            setTitle("New Profile");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            add(mainPanel);
            setSize(320, 620);
            setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);

        });
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
        new NewProfile();
    }

}
