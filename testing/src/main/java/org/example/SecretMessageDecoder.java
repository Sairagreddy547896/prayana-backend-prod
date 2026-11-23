package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecretMessageDecoder {

    static class DataPoint {
        int x, y;
        char ch;

        DataPoint(int x, char ch, int y) {
            this.x = x;
            this.ch = ch;
            this.y = y;
        }
    }

    public static void decodeSecretMessage(String docUrl) {
        try {
            // Connect and parse Google Doc HTML directly
            Document doc = Jsoup.connect(docUrl).get();
            Elements rows = doc.select("tr");

            if (rows.isEmpty()) {
                System.out.println("Could not find table rows in document.");
                return;
            }

            // Map column indices from header row
            Elements headers = rows.get(0).select("td, th");
            int xCol = -1, charCol = -1, yCol = -1;

            for (int i = 0; i < headers.size(); i++) {
                String h = headers.get(i).text().toLowerCase();
                if (h.contains("x")) xCol = i;
                else if (h.contains("char") || h.contains("unicode")) charCol = i;
                else if (h.contains("y")) yCol = i;
            }

            if (xCol == -1) xCol = 0;
            if (charCol == -1) charCol = 1;
            if (yCol == -1) yCol = 2;

            List<DataPoint> data = new ArrayList<>();
            int maxX = 0, maxY = 0;

            // Extract coordinates and characters from data rows
            for (int i = 1; i < rows.size(); i++) {
                Elements cells = rows.get(i).select("td");
                if (cells.size() < 3) continue;

                try {
                    int x = Integer.parseInt(cells.get(xCol).text().trim());
                    String cStr = cells.get(charCol).text().trim();
                    if (cStr.isEmpty()) continue;
                    char ch = cStr.charAt(0);
                    int y = Integer.parseInt(cells.get(yCol).text().trim());

                    data.add(new DataPoint(x, ch, y));
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                } catch (Exception ignored) {}
            }

            // Construct 2D grid filled with spaces
            char[][] grid = new char[maxY + 1][maxX + 1];
            for (char[] row : grid) {
                Arrays.fill(row, ' ');
            }

            for (DataPoint point : data) {
                grid[point.y][point.x] = point.ch;
            }

            // Print output (row maxY down to 0)
            for (int y = maxY; y >= 0; y--) {
                System.out.println(new String(grid[y]));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String docUrl = "https://docs.google.com/document/d/e/2PACX-1vSvM5gDINvt7npYHhp_XfsJvuntUhq184By5xO_pA4b_gCWeXb6dM6ZXwN8rE6S4ghUsCj2VKR21oEP/pub";
        decodeSecretMessage(docUrl);
    }
}