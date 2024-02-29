package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Modrinth {

    public Modrinth() {
    }

    public List<Map<String, String>> modSearch(String q) throws IOException {
        String url = "https://modrinth.com/mods?q=" + q;
        Document doc = Jsoup.connect(url).get();

        List<Map<String, String>> result = new ArrayList<>();

        Elements projectCards = doc.select("article.project-card");
        for (Element div : projectCards) {
            String a = div.select("a.icon").first().attr("href");
            String img = div.select("img.avatar").attr("src");
            String title = div.select("h2.name").first().text();
            Map<String, String> map = new HashMap<>();
            map.put("name", title);
            map.put("icon", img);
            map.put("href", a);
            result.add(map);
        }

        return result;
    }

    public List<Map<String, String>> getModFiles(String modUrl) throws IOException {

        List<Map<String, String>> result = new ArrayList<>();

        String url = "https://modrinth.com" + modUrl + "/versions#all-versions";
        Document doc = Jsoup.connect(url).get();

        Elements versionButtons = doc.select("div.version-button");
        for (Element div : versionButtons) {
            String a = div.select("a.download-button").first().attr("href");
            String title = div.select("a.version__title").first().text();
            String version_mode = div.select("div.version__supports > span").first().text();
            String version = div.select("div.version__supports > span + span").first().text();
            System.out.println(version);
            Map<String, String> map = new HashMap<>();
            map.put("title", title);
            map.put("download", a);
            map.put("version_mode", version_mode);
            map.put("version", version);
            result.add(map);
            System.out.println(map);
        }

        try {

            String url2 = "https://modrinth.com" + modUrl + "/versions?p=2";
            Document doc2 = Jsoup.connect(url2).get();

            Elements versionButtons2 = doc2.select("div.version-button");
            for (Element div : versionButtons2) {
                String a = div.select("a.download-button").first().attr("href");
                String title = div.select("a.version__title").first().text();
                String version_mode = div.select("div.version__supports > span").first().text();
                String version = div.select("div.version__supports > span + span").first().text();
                System.out.println(version);
                Map<String, String> map = new HashMap<>();
                map.put("title", title);
                map.put("download", a);
                map.put("version_mode", version_mode);
                map.put("version", version);
                result.add(map);
                System.out.println(map);
            }

        }catch (Exception e){

        }

        return result;
    }


    public static void main(String[] args) {
        Modrinth manager = new Modrinth();
        try {
            List<Map<String, String>> mods = manager.modSearch("worldedit");
            System.out.println("Mods found:");
            for (Map<String, String> mod : mods) {
                //System.out.println(mod.get("name"));
            }

            // Example usage for getting mod files
            if (!mods.isEmpty()) {
                String firstModUrl = mods.get(0).get("href");
                List<Map<String, String>> files = manager.getModFiles(firstModUrl);
                System.out.println("Files for the first mod:");
                for (Map<String, String> file : files) {
                    System.out.println(file.get("title") + ": " + file.get("download"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

