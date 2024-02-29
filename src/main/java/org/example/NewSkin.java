package org.example;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class NewSkin extends JFrame {

    private String last_temp_raw_file_path, last_temp_view_file;

    public static boolean isInternetAvailable() {
        try {
            InetAddress.getByName("www.google.com").isReachable(1000); // Google pingelése
            return true;
        } catch (Exception e) {
            return false; // Ha kivétel keletkezik, akkor nincs internetkapcsolat
        }
    }

    NewSkin(){
        SwingUtilities.invokeLater(() -> {

            setTitle("New Skin");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel main = new JPanel();
            main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
            main.setBorder(new EmptyBorder(20, 10, 10, 20));

            JLabel title = new JLabel("Új skin importálása");
            title.setBorder(new EmptyBorder(0,10,30,10));
            title.setFont(new Font("Arial",Font.BOLD,16));
            title.setAlignmentX(Component.CENTER_ALIGNMENT); // Cím középre igazítása
            main.add(title); // Cím hozzáadása a fő panelhez

            JPanel filenamePanel = new JPanel();
            filenamePanel.setLayout(new BoxLayout(filenamePanel, BoxLayout.X_AXIS)); // Fájlnév panel elrendezésének beállítása
            filenamePanel.setBorder(new EmptyBorder(0,5,10,5));

            JLabel filenameLabel = new JLabel("");

            JPanel filenamePanel2 = new JPanel();
            JButton button = new JButton("Fájl tallózása");
            button.setPreferredSize(new Dimension(265,20));

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    int returnValue = fileChooser.showOpenDialog(main); // Fájl tallózó megjelenítése
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        //filenameField.setText(fileChooser.getSelectedFile().getName()); // Kiválasztott fájl nevének beállítása

                        boolean success = false;

                        try {
                            // Fájl elérési útja
                            String filePath = "skin_generator.exe";
                            // ProcessBuilder létrehozása
                            ProcessBuilder builder = new ProcessBuilder(filePath, "--input", fileChooser.getSelectedFile().getPath(), "--mode","full", "--output", "temp/" + fileChooser.getSelectedFile().getName());
                            // Standard I/O csatornák öröklése
                            builder.inheritIO();
                            // Folyamat elindítása
                            Process process = builder.start();
                            // Folyamat leállítása
                            int exitCode = process.waitFor();
                            System.out.println("Folyamat leállt kóddal: " + exitCode);
                            success = !success;
                        } catch (IOException | InterruptedException ee) {
                            ee.printStackTrace();
                        }

                        if(success) {

                            last_temp_raw_file_path = fileChooser.getSelectedFile().getPath();
                            last_temp_view_file = fileChooser.getSelectedFile().getName();

                            File images = new File("temp/" + fileChooser.getSelectedFile().getName());
                            BufferedImage image = null;
                            try {
                                image = ImageIO.read(images);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            int width = 85;
                            int height = 130;
                            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

                            filenameLabel.setIcon(new ImageIcon(scaledImage));

                        }
                    }
                }
            });

            JButton button2 = new JButton("Hozzáadás");
            button2.setPreferredSize(new Dimension(265,20));
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    boolean success = false;

                    try {
                        Files.copy(Path.of(last_temp_raw_file_path), Path.of("skins/"+last_temp_view_file));
                        Files.copy(Path.of("temp/"+last_temp_view_file), Path.of("skin_view/"+last_temp_view_file));
                        Files.delete(Path.of("temp/"+last_temp_view_file));
                        success = !success;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    if(success) {

                        try {
                            // Fájl elérési útja
                            String filePath = "skin_generator.exe";
                            // ProcessBuilder létrehozása
                            ProcessBuilder builder = new ProcessBuilder(filePath, "--input", "skins/" + last_temp_view_file, "--mode", "head", "--output", "skin_view/head/" + last_temp_view_file);
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

                }
            });

            filenamePanel.add(filenameLabel);
            filenamePanel.add(Box.createVerticalStrut(10));
            filenamePanel2.add(button);
            filenamePanel2.add(button2);

            filenamePanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Fájlnév panel középre igazítása
            filenamePanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Fájlnév panel középre igazítása
            main.add(filenamePanel); // Fájlnév panel hozzáadása a fő panelhez
            main.add(filenamePanel2); // Fájlnév panel hozzáadása a fő panelhez

            add(main);

            setSize(320, 500);
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

    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel(new FlatCobalt2IJTheme());
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception ex){
            System.out.println(ex);
        }
        new NewSkin();
    }

}
