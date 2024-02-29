package org.example;

import java.io.*;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.jar.*;

public class JarFileModifier {

    private static String version;

    /*JarFileModifier(String ver){
        version = ver;
    }*/

    public static void Modifier(String modpack, String version, String skinfile ,Boolean isdefault) {

        System.out.println(skinfile);

        // FÁJLOK RENDEZÉSE A SKIN ÁTIRÁSÁHOZ

        String jarPath = "modpacks/"+modpack+"/versions/"+version+"/"+version+".jar";
        String jarPath2 = "modpacks/"+modpack+"/versions/"+version+"/"+version+"_new.jar";

        String old_file_path = "modpacks/"+modpack+"/versions/"+version+"/"+version+".jar.old";

        System.out.println(jarPath);
        System.out.println(jarPath2);
        System.out.println(old_file_path);

        File old_file = new File(old_file_path);
        if (!old_file.exists()) {
            try {
                Files.copy(Paths.get(jarPath), Paths.get(old_file_path));
                System.out.println("A fájl sikeresen másolva lett.");
            } catch (IOException e){
                System.err.println("Hiba történt a fájl másolása közben: " + e.getMessage());
            }
        } else {
            System.out.println("A fájl már létezik.");
        }

        if(isdefault){

            File jarfile = new File(jarPath);
            // Ellenőrizzük, hogy a fájl létezik-e
            if (jarfile.exists()) {
                // Fájl törlése
                if (jarfile.delete()) {
                    System.out.println("A fájl sikeresen törölve lett.");
                } else {
                    System.out.println("Nem sikerült törölni a fájlt.");
                }
            } else {
                System.out.println("A fájl nem létezik.");
            }

            try {
                Files.copy(Paths.get(old_file_path), Paths.get(jarPath));
            } catch (IOException e) {

            }

        } else {

            try {
                Files.copy(Paths.get(old_file_path), Paths.get(jarPath2));
            } catch (IOException e) {

            }


            String[] excludedFiles = {
                    "assets/minecraft/textures/entity/player/slim/alex.png",
                    "assets/minecraft/textures/entity/player/slim/ari.png",
                    "assets/minecraft/textures/entity/player/slim/efe.png",
                    "assets/minecraft/textures/entity/player/slim/kai.png",
                    "assets/minecraft/textures/entity/player/slim/makena.png",
                    "assets/minecraft/textures/entity/player/slim/noor.png",
                    "assets/minecraft/textures/entity/player/slim/steve.png",
                    "assets/minecraft/textures/entity/player/slim/sunny.png",
                    "assets/minecraft/textures/entity/player/slim/zuri.png",

                    "assets/minecraft/textures/entity/player/wide/alex.png",
                    "assets/minecraft/textures/entity/player/wide/ari.png",
                    "assets/minecraft/textures/entity/player/wide/efe.png",
                    "assets/minecraft/textures/entity/player/wide/kai.png",
                    "assets/minecraft/textures/entity/player/wide/makena.png",
                    "assets/minecraft/textures/entity/player/wide/noor.png",
                    "assets/minecraft/textures/entity/player/wide/steve.png",
                    "assets/minecraft/textures/entity/player/wide/sunny.png",
                    "assets/minecraft/textures/entity/player/wide/zuri.png",
            };

            try (JarFile jarFile = new JarFile(jarPath2);
                 JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(jarPath))) {

                // Másoljuk át a JAR fájl tartalmát az új JAR fájlba, kihagyva a megadott fájlokat
                jarFile.stream().forEach(jarEntry -> {
                    try {
                        if (!Arrays.asList(excludedFiles).contains(jarEntry.getName())) {
                            JarEntry entry = new JarEntry(jarEntry.getName());
                            outputStream.putNextEntry(entry);
                            try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }
                            }
                            outputStream.closeEntry();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            /*File jarfile = new File(jarPath);
            // Ellenőrizzük, hogy a fájl létezik-e
            if (jarfile.exists()) {
                // Fájl törlése
                if (jarfile.delete()) {
                    System.out.println("A fájl sikeresen törölve lett.");
                } else {
                    System.out.println("Nem sikerült törölni a fájlt.");
                }
            } else {
                System.out.println("A fájl nem létezik.");
            }*/

                // Hozzáadjuk a módosított fájlt az új JAR fájlhoz
                File newFile = new File("skins/"+skinfile);
                for (String file : excludedFiles) {
                    JarEntry entry = new JarEntry(file);
                    outputStream.putNextEntry(entry);
                    try (InputStream inputStream = new FileInputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                    outputStream.closeEntry();
                }

                // További módosítások...
            } catch (IOException e) {
                e.printStackTrace();
            }

            File jarfile = new File(jarPath2);
            // Ellenőrizzük, hogy a fájl létezik-e
            if (jarfile.exists()) {
                // Fájl törlése
                if (jarfile.delete()) {
                    System.out.println("A fájl sikeresen törölve lett.");
                } else {
                    System.out.println("Nem sikerült törölni a fájlt.");
                }
            } else {
                System.out.println("A fájl nem létezik.");
            }

        }

    }



    public static void main(String[] args) {

        //Modifier("","");

    }
}

