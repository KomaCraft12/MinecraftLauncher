package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.IOException;

public class Example {
    private String selected_version;
    private String gamedirector;

    public static void main(String[] args) {
        new Example();
    }

    public Example() {
        JFrame frame = new JFrame("Minecraft Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 775);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel1.setPreferredSize(new Dimension(1300, 50));
        panel1.setBackground(Color.decode("#8B4513"));

        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.setBackground(Color.decode("#CD853F"));

        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel3.setPreferredSize(new Dimension(1300, 40));
        panel3.setBackground(Color.decode("#8B4513"));

        JPanel hasab1 = new JPanel(new GridBagLayout());
        hasab1.setBorder(new EmptyBorder(5, 5, 5, 5));
        hasab1.setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 0; // Reset weighty to 0 to prevent the panel from expanding vertically

        JSONObject versions = getVersions();

        versions.forEach((modpackName, modpackData) -> {
            String name = (String) modpackName;
            JSONObject modpack = (JSONObject) modpackData;
            String version = (String) modpack.get("version");
            String gameDirector = (String) modpack.get("gameDirector");
            String icon = (String) modpack.get("icon");

            ModpackComponent component = new ModpackComponent(name, "11",icon, 0);
            component.setAlignmentX(Component.LEFT_ALIGNMENT);
            component.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selected_version = version;
                    gamedirector = gameDirector;
                    // Update modpack_title with the clicked modpack name
                    // modpack_title.setText(name);
                    System.out.println(name);
                }
            });
            gbc.gridy++; // Increase gridy to add the next component below
            hasab1.add(component, gbc);
        });

        JScrollPane scrollPane = new JScrollPane(hasab1);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(300, 650));
        panel2.add(scrollPane, BorderLayout.CENTER);

        frame.add(panel1, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.CENTER);
        frame.add(panel3, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Dummy method, replace with your actual method to get JSON data
    private JSONObject getVersions() {
        // Sample JSON data
        String fileName = "versions.json"; // JSON fájl elérési útvonala és neve
        JSONObject jsonObject = null;

        // JSON parser inicializálása
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            // JSON fájl beolvasása és parse-olása
            jsonObject = (JSONObject) parser.parse(reader);

            // Minden modpack feldolgozása
            /*jsonObject.forEach((modpackName, modpackData) -> {
                JSONObject modpack = (JSONObject) modpackData;
                String version = (String) modpack.get("version");
                String gameDirector = (String) modpack.get("gameDirector");
                String icon = (String) modpack.get("icon");

                // Modpack adatok megjelenítése
                System.out.println("Modpack neve: " + modpackName);
                System.out.println("Verzió: " + version);
                System.out.println("Játékigazgató elérési útja: " + gameDirector);
                System.out.println("Ikon URL-címe: " + icon);
                System.out.println();
            });*/

        } catch (IOException | ParseException e) {
            System.err.println("Hiba történt a fájl olvasása során: " + e.getMessage());
        }

        return jsonObject;
    }
}
