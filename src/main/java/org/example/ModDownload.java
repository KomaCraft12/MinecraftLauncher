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
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


class ModComponent extends JPanel {
    private JLabel label, label1, label2, label3, imageLabel;
    private JPanel panel;

    public ModComponent(String text, String version, String version_mode, String url, Integer id) {
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
            BufferedImage image = ImageIO.read(imageUrl);
            int width = 85;
            int height = 85;
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

public class ModDownload extends JFrame {
    private JPanel modpackPanel;
    private JPanel controlPanel;
    private JScrollPane scrollPane;
    private JProgressBar progressBar;
    private JButton downloadButton;
    private ArrayList<ModComponent> components = new ArrayList<>();
    private Integer last_id = -1;
    private String jar_url = null;
    private String gamedirector = null;
    private String filename = null;
    private JLabel progress_desc;

    private JSONObject data = new JSONObject();

    ModDownload(String href, String icon, String gdir) {
        SwingUtilities.invokeLater(() -> {

            gamedirector = gdir;

            System.out.println(href);

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
                    Thread thread = new Thread(() -> download_jar());
                    thread.start();
                }
            });

            List<Map<String, String>> modfiles;
            Modrinth modrinth = new Modrinth();
            try {
                modfiles = modrinth.getModFiles(href);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < modfiles.size(); i++) {
                Map<String, String> file = modfiles.get(i);
                String title = file.get("title");
                String download = file.get("download");
                String version = file.get("version");
                String version_mode = file.get("version_mode");
                System.out.println("Title: " + title + ", Download: " + download);

                ModComponent modpack = new ModComponent(title, version, version_mode, icon, 0);
                modpack.setPreferredSize(new Dimension(300, 100));
                modpack.setBorder(new LineBorder(new Color(255, 255, 255, 100), 5, true));
                modpack.setMaximumSize(new Dimension(300, 100));

                components.add(modpack);
                modpackPanel.add(modpack);
                modpackPanel.add(Box.createVerticalStrut(10));

                int finalI = i;
                modpack.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int clickedId = finalI; // Az iteráció által aktuálisan beállított ID értéke
                        System.out.println("MyComponent clicked! ID: " + clickedId);
                        if (last_id != clickedId) {
                            jar_url = download;
                            modpack.setActive();
                            if (last_id != -1) {
                                components.get(last_id).removeActive();
                            }
                            last_id = clickedId;
                        }
                    }
                });
            }




                    /*ModpackComponent modpack = new ModpackComponent(name, version,iconPath, id);
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
                            if(last_id != id-1) {

                                data.clear();
                                data.put("name",name);
                                data.put("icon",iconPath);
                                data.put("forge",forge);
                                data.put("version",version);

                                zip_url = url;

                                modpack.setActive();
                                if (last_id != -1) {

                                    components.get(last_id).removeActive();
                                }
                                last_id = id - 1;
                            }
                        }
                    });*/

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

            setTitle("Mod Download");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            add(scrollPane, BorderLayout.CENTER);
            add(controlPanel, BorderLayout.SOUTH);
            setSize(300, 500);
            setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);
        });
    }


    public void download_jar() {
        System.out.println(jar_url);
        progress_desc.setText("Letöltés...");

        String[] dirs = gamedirector.split("\\\\");

        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                URL url = new URL(jar_url);
                String fileName = getFileNameFromURL(jar_url);
                filename = fileName;
                try (InputStream in = new BufferedInputStream(url.openStream());
                     FileOutputStream fos = new FileOutputStream((dirs[1]+"/"+dirs[2]+"/mods/"+fileName))) {

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
                // Itt írd meg, hogy mit szeretnél csinálni a letöltés befejezésekor, például futtasd a JAR fájlt.
            }
        };
        worker.execute();
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
        new ModDownload("/mod/create","https://cdn.modrinth.com/data/LNytGWDc/icon.png","\\modpacks\\createmod");
    }
}
