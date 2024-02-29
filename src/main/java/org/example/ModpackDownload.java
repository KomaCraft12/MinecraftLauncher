package org.example;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static org.example.NewSkin.isInternetAvailable;


class ModpackComponent extends JPanel {
    private JLabel label, label1, label2, imageLabel;
    private JPanel panel;

    public ModpackComponent(String text, String version, String url, Integer id) {
        setLayout(new BorderLayout()); // BorderLayout beállítása a fő panelhez
        setBackground(Color.GRAY);
        setBounds(20, 20, 260, 100);

        // Kép letöltése és beállítása

            try {
                URL imageUrl;
                BufferedImage image;
                if(isInternetAvailable()) {
                    imageUrl = new URL(url);
                    image = ImageIO.read(imageUrl);
                } else {
                    image = ImageIO.read(new File("skin_view/head/default.png"));
                }
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
        label2 = new JLabel("Version: "+version, JLabel.CENTER);

        label1.setAlignmentX(Component.CENTER_ALIGNMENT); // Vízszintes középre igazítás
        label1.setAlignmentY(Component.CENTER_ALIGNMENT); // Függőleges középre igazítás
        label2.setAlignmentX(Component.CENTER_ALIGNMENT); // Vízszintes középre igazítás
        label2.setAlignmentY(Component.CENTER_ALIGNMENT); // Függőleges középre igazítás

        panel.add(Box.createVerticalGlue()); // Függőleges kitöltés hozzáadása
        panel.add(label1);
        panel.add(label2);
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

public class ModpackDownload extends JFrame {
    private JPanel modpackPanel;
    private JPanel controlPanel;
    private JScrollPane scrollPane;
    private JProgressBar progressBar;
    private JButton downloadButton;
    private ArrayList<ModpackComponent> components = new ArrayList<>();
    private Integer last_id = -1;
    private String zip_url = null;
    private String filename = null;
    private JLabel progress_desc;

    private JSONObject data = new JSONObject();

    ModpackDownload() {
        SwingUtilities.invokeLater(() -> {
            modpackPanel = new JPanel();
            modpackPanel.setLayout(new BoxLayout(modpackPanel,BoxLayout.Y_AXIS));
            modpackPanel.setBorder(new EmptyBorder(5,5,5,5));
            controlPanel = new JPanel();
            progressBar = new JProgressBar(0,100);
            progressBar.setBounds(40,40,160,30);
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            downloadButton = new JButton("Letöltés");
            downloadButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Thread thread = new Thread(() -> download_zip());
                    thread.start();
                }
            });

            if(isInternetAvailable()){


                Database database = new Database();
                ResultSet result = database.getTable("SELECT * FROM modpacks");
                try {
                    while (result.next()) {
                        String name = result.getString("name");
                        String iconPath = result.getString("icon");
                        String forge = result.getString("forge");
                        String version = result.getString("version");
                        String url = result.getString("url");
                        Integer id = result.getInt("id");

                        System.out.println(id);

                        ModpackComponent modpack = new ModpackComponent(name, version, iconPath, id);
                        modpack.setPreferredSize(new Dimension(300, 100)); // Állítsd be a preferált méretet (100x100)
                        modpack.setBorder(new LineBorder(new Color(255, 255, 255, 100), 5, true));
                        modpack.setMaximumSize(new Dimension(300, 100)); // Állítsd be a maximális méretet (100x100)

                        components.add(modpack);
                        modpackPanel.add(modpack);
                        modpackPanel.add(Box.createVerticalStrut(10)); // 10 pixel magas üres tér
                        modpack.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                System.out.println("MyComponent clicked! ID: " + id);
                                if (last_id != id - 1) {

                                    data.clear();
                                    data.put("name", name);
                                    data.put("icon", iconPath);
                                    data.put("forge", forge);
                                    data.put("version", version);

                                    zip_url = url;

                                    modpack.setActive();
                                    if (last_id != -1) {

                                        components.get(last_id).removeActive();
                                    }
                                    last_id = id - 1;
                                }
                            }
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            scrollPane = new JScrollPane(modpackPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            modpackPanel.setBounds(0,0,300,400);

            progressBar.setBorder(new EmptyBorder(10,0,10,0));
            controlPanel.setLayout(new BorderLayout());
            controlPanel.setBorder(new EmptyBorder(10,10,10,10));
            progress_desc = new JLabel("");
            progress_desc.setBorder(new EmptyBorder(5,5,5,5));
            controlPanel.add(progress_desc, BorderLayout.NORTH);
            controlPanel.add(progressBar, BorderLayout.CENTER);
            controlPanel.add(downloadButton, BorderLayout.SOUTH);

            setTitle("Modpack Download");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            add(scrollPane, BorderLayout.CENTER);
            add(controlPanel, BorderLayout.SOUTH);
            setSize(300, 500);
            setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);

            if(!isInternetAvailable()){
                JOptionPane.showMessageDialog(rootPane, "Internet szükséges ehhez a részhez!",
                        "Hiba", JOptionPane.ERROR_MESSAGE);
                dispose();
            }

        });
    }

    public void download_zip(){

        System.out.println(zip_url);
        progress_desc.setText("Letöltés...");

        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                URL url = new URL(zip_url);
                String fileName = getFileNameFromURL(zip_url);
                filename = fileName;
                try (InputStream in = new BufferedInputStream(url.openStream());
                     FileOutputStream fos = new FileOutputStream("temp/"+fileName)) {

                    byte[] buffer = new byte[1024];
                    long fileSize = url.openConnection().getContentLengthLong();
                    long downloadedSize = 0;
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                        downloadedSize += bytesRead;
                        int progress = (int) ((downloadedSize * 100) / fileSize);
                        publish(progress);
                    }
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                for (int progress : chunks) {
                    progressBar.setValue(progress);
                }
            }

            @Override
            protected void done() {
                progressBar.setValue(100);
                //JOptionPane.showMessageDialog(FileDownload.this, "Download complete!");
                try {
                    unzip("temp/"+filename,"modpacks/");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        worker.execute();

    }

    public void unzip(String zipFilePath, String destDirectory) throws IOException {
        progressBar.setValue(0);
        progress_desc.setText("Telepítés...");
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                File destDir = new File(destDirectory);
                if (!destDir.exists()) {
                    destDir.mkdir();
                }
                ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
                ZipEntry entry = zipIn.getNextEntry();
                long totalSize = 0;
                while (entry != null) {
                    totalSize += entry.getSize();
                    entry = zipIn.getNextEntry();
                }
                zipIn.close();

                zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
                entry = zipIn.getNextEntry();
                long totalBytesRead = 0;
                byte[] buffer = new byte[4096];
                while (entry != null) {
                    String filePath = destDirectory + File.separator + entry.getName();
                    if (!entry.isDirectory()) {
                        extractFile(zipIn, filePath, buffer);
                        totalBytesRead += entry.getSize();
                        int progress = (int) ((totalBytesRead * 100) / totalSize);
                        publish(progress);
                    } else {
                        File dir = new File(filePath);
                        dir.mkdir();
                    }
                    zipIn.closeEntry();
                    entry = zipIn.getNextEntry();
                }
                zipIn.close();
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                for (int progress : chunks) {
                    progressBar.setValue(progress);
                    if(progress == progressBar.getMaximum()){
                        Done();
                    }
                }
            }

            private void extractFile(ZipInputStream zipIn, String filePath, byte[] buffer) throws IOException {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                int read;
                while ((read = zipIn.read(buffer)) != -1) {
                    bos.write(buffer, 0, read);
                }
                bos.close();
            }
            private void Done(){
                progressBar.setValue(0);
                register();
            }
        };
        worker.execute();
    }

    public void register(){

        String fileName = "versions.json"; // JSON fájl elérési útvonala és neve
        JSONObject jsonObject = new JSONObject(); // JSON objektum inicializálása

        // JSON parser inicializálása
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            // JSON fájl beolvasása és parse-olása
            jsonObject = (JSONObject) parser.parse(reader);

            String version = data.get("version").toString();
            if(!data.get("forge").toString().isEmpty()){
                version = data.get("version").toString() + "-forge-" + data.get("forge").toString();
            }

            // Az új modpack hozzáadása
            JSONObject modpack = new JSONObject();
            modpack.put("isown", false);
            modpack.put("version", version);
            modpack.put("gameDirector", "\\modpacks\\"+data.get("name").toString().toLowerCase());
            modpack.put("icon", data.get("icon"));

            jsonObject.put(data.get("name").toString(), modpack); // Új modpack hozzáadása a jsonObject-hez

            // Fájl tartalmának frissítése az új JSON objektummal
            try (FileWriter file = new FileWriter(fileName)) {
                // Gson inicializálása a szép formázás céljából
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String prettyJsonString = gson.toJson(jsonObject); // Szépen formázott JSON sztring generálása
                file.write(prettyJsonString); // Szépen formázott JSON sztring kiírása a fájlba
            } catch (IOException e) {
                System.err.println("Hiba történt a fájl írása során: " + e.getMessage());
            }

        } catch (IOException | ParseException e) {
            System.err.println("Hiba történt a fájl olvasása során: " + e.getMessage());
        }


    }

    public static String getFileNameFromURL(String urlString) {
        String fileName = "";
        try {
            URL url = new URL(urlString);
            String path = url.getPath();
            fileName = path.substring(path.lastIndexOf('/') + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception ex) {
            System.out.println(ex);
        }
        new ModpackDownload();
    }
}
