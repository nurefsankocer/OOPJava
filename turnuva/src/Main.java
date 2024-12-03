import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        futballInfo();
        basketballInfo();
        handballInfo();
        voleybolInfo();

    }

    public static void futballInfo() {
        /* dosyanın adını ve yolunu fileName değişkenine atıyoruz: */
        Futbol futbolStats = new Futbol();
        String fileName = "C:\\Users\\LENOVO\\IdeaProjects\\turnuva\\src\\fixtures.txt";

        System.out.println(" Futbol Fikstürü");
         /* dosyadan veri okuma işlemini try-with-resources (belirli kaynakları otomatik olarak
        açar ve kapatır) kullanarak gerçekleştiriyoruz */
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;

            /* while döngüsü ile dosyayı satır satır okuyoruz */
            while ((line = bufferedReader.readLine()) != null) {
                //Her döngü adımında, bir satır okunur bu satırın null olup olmadığı kontrol
                // edili.null değilse, dosyanın sonuna gelinmemiş demektir ve döngü devam eder.

                String[] parts = line.split("\t");

                if (parts.length >= 4 && parts[0].equals("F")) {
                    String team1 = parts[1];
                    String team2 = parts[2];
                    //maçtaki iki takımın adını alır
                    String score = parts[3];
                    //maç skorunu alır
                    String[] scoreParts = score.split(":");
                    //skoru iki bölüme ayırdım
                    int team1Score = Integer.parseInt(scoreParts[0]);
                    int team2Score = Integer.parseInt(scoreParts[1]);
                    //ve burda da onları tam sayıya dönüştürdük

                    futbolStats.addMatchResult(team1, team2, team1Score, team2Score);
                    //futbolStats nesnesine işledik
                }
            }

            // Liderlik sıralamasını al
            Map<String, Integer> leaderboard = futbolStats.getLeaderboard();

            // Tabloyu oluştur ve sonuçları yazdır

            try {
                FileWriter writer = new FileWriter("football.txt");
                writer.write("Sıra\t\tTakım\tOynanan Maç\tKazanılan Maç\tBerabere\tKaybedilen Maç\tGol Farkı\tToplam Puan\n");

                int rank = 1;
                for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
                    String teamName = entry.getKey();
                    int matchesPlayed = futbolStats.matchesPlayed.getOrDefault(teamName, 0);
                    int matchesWon = futbolStats.matchesWon.getOrDefault(teamName, 0);
                    int matchesTied = futbolStats.matchesTied.getOrDefault(teamName, 0);
                    int matchesLost = futbolStats.matchesLost.getOrDefault(teamName, 0);
                    int goalsScored = futbolStats.getGoals(teamName);
                    int goalsConceded = goalsScored - futbolStats.getGoalDifference(teamName);
                    int totalPoints = entry.getValue();

                    line = rank + "\t" + teamName + "\t" + matchesPlayed + "\t" + matchesWon + "\t" + matchesTied + "\t" + matchesLost + "\t" + goalsScored + ":" + goalsConceded + "\t" + totalPoints + "\n";
                    writer.write(line);
                    rank++;
                }

                writer.close();
                System.out.println("Sonuçlar 'football.txt' dosyasına yazıldı.");
                //istisna durum yakalattık ve konsola yazdırdık
            } catch (IOException e) {
                System.err.println("Dosya yazma hatası: " + e.getMessage());
            }

        } // sorunu konsola acıklar
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void basketballInfo() {

        Basketbol basketball = new Basketbol();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\LENOVO\\IdeaProjects\\turnuva\\src\\fixtures.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("B")) {
                    String[] parts = line.split("\t");
                    if (parts.length != 4) {
                        System.err.println("Geçersiz veri formatı: " + line);
                        continue;
                    }

                    String team1 = parts[1];
                    String team2 = parts[2];
                    String[] scores = parts[3].split(":");
                    int score1 = Integer.parseInt(scores[0]);
                    int score2 = Integer.parseInt(scores[1]);

                    basketball.addMatchResult(team1, team2, score1, score2);
                }
            }

            reader.close();

            System.out.println("\n Basketball Fikstürü:");
            try {
                FileWriter writer = new FileWriter("basketball.txt");
                writer.write("Sıra\tTakım\tOynanan Maç\tKazanılan Maç\tBerabere\tKaybedilen Maç\tGol Atılan:Gol Yenilen\tToplam Puan\n");

                int rank = 1;
                for (Map.Entry<String, Integer> entry : basketball.getLeaderboard().entrySet()) {
                    String teamName = entry.getKey();
                    String stats = basketball.getTeamStats(entry.getKey());
                    line = rank + "\t" + teamName + "\t" + stats + "\n";
                    writer.write(line);
                    rank++;
                }

                writer.close();
                System.out.println("Sonuçlar 'basketball.txt' dosyasına yazıldı.");
            } catch (IOException e) {
                System.err.println("Dosya yazma hatası: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }
    }
    public static void handballInfo() {
        Hentbol hentbol = new Hentbol();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\LENOVO\\IdeaProjects\\turnuva\\src\\fixtures.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("H")) {
                    String[] parts = line.split("\t");
                    if (parts.length != 4) {
                        System.err.println("Geçersiz veri formatı: " + line);
                        continue;
                    }

                    String team1 = parts[1];
                    String team2 = parts[2];
                    String[] scores = parts[3].split(":");
                    int score1 = Integer.parseInt(scores[0]);
                    int score2 = Integer.parseInt(scores[1]);

                    hentbol.addMatchResult(team1, team2, score1, score2);
                }
            }

            reader.close();

            System.out.println("\n Hentbol Fikstürü:");
            try {
                FileWriter writer = new FileWriter("hentbol.txt");
                writer.write("Sıra\tTakım\tOynanan Maç\tKazanılan Maç\tBerabere\tKaybedilen Maç\tGol Atılan:Gol Yenilen\tToplam Puan\n");

                int rank = 1;
                for (Map.Entry<String, Integer> entry : hentbol.getLeaderboard().entrySet()) {
                    String teamName = entry.getKey();
                    String stats = hentbol.getTeamStats(entry.getKey());
                    line = rank + "\t" + teamName + "\t" + stats + "\n";
                    writer.write(line);
                    rank++;
                }

                writer.close();
                System.out.println("Sonuçlar 'hentbol.txt' dosyasına yazıldı.");
            } catch (IOException e) {
                System.err.println("Dosya yazma hatası: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }
    }

    public static void voleybolInfo() {
        Voleybol volleyball = new Voleybol();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\LENOVO\\IdeaProjects\\turnuva\\src\\fixtures.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("V")) {
                    String[] parts = line.split("\t");
                    if (parts.length != 4) {
                        System.err.println("Geçersiz veri formatı: " + line);
                        continue;
                    }

                    String team1 = parts[1];
                    String team2 = parts[2];
                    String score = parts[3];

                    volleyball.addMatchResult(team1, team2, score);
                }
            }

            reader.close();
            System.out.println("\n Voleybol Fikstürü");
            try {
                FileWriter writer = new FileWriter("volleyball.txt");
                writer.write("Sıra\tTakım\tOynanan Maç\tKazanılan Maç\tBerabere\tKaybedilen Maç\tGol Farkı\tToplam Puan\n");

                int rank = 1;
                for (Map.Entry<String, Integer> entry : volleyball.getLeaderboard().entrySet()) {
                    String stats = volleyball.getTeamStats(entry.getKey());
                    writer.write(rank + "\t" + entry.getKey() + "\t" + stats + "\n");
                    rank++;
                }

                writer.close();
                System.out.println("Sonuçlar 'volleyball.txt' dosyasına yazıldı.");

            } catch (IOException e) {
                System.err.println("Dosya okuma veya yazma hatası: " + e.getMessage());
            }


        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

