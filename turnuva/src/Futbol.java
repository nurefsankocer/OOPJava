import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Futbol {
     /*  bu kısımda futbol takımlarının kazandığı puanlar,
         oynadıkları maç sayısı, kazandıkları, berabere kaldıkları
        ve kaybettikleri maç sayılarını saklamak için  Map
       veri yapısını kulandım */
    private Map<String, Integer> goals = new HashMap<>();
    private Map<String, Integer> points = new HashMap<>();
    public Map<String, Integer> matchesPlayed = new HashMap<>();
    public Map<String, Integer> matchesWon = new HashMap<>();
    public Map<String, Integer> matchesTied = new HashMap<>();
    public Map<String, Integer> matchesLost = new HashMap<>();

    // Bir maç sonucunu eklemek için kullanılır. Takım adları ve skorlar parametre olarak verilir.
    public void addMatchResult(String team1, String team2, int score1, int score2) {
        // Takımların gol ve puanlarını güncelle
        updateGoals(team1, score1);
        updateGoals(team2, score2);
        updatePoints(team1, score1, score2);
        updatePoints(team2, score2, score1);
        updateMatchesPlayed(team1);
        updateMatchesPlayed(team2);
        updateMatchesWon(team1, score1, score2);
        updateMatchesWon(team2, score2, score1);
        updateMatchesTied(score1, score2);
        updateMatchesLost(team1, score1, score2);
        updateMatchesLost(team2, score2, score1);
    }

    // Bir takımın attığı gol sayısını günceller.
    private void updateGoals(String team, int goalsScored) {
        goals.put(team, goals.getOrDefault(team, 0) + goalsScored);
    }

    // Bir takımın kazandığı puanı günceller.
    private void updatePoints(String team, int teamScore, int opponentScore) {
        int point = 0;
        if (teamScore > opponentScore) {
            point = 3;
        } else if (teamScore == opponentScore) {
            point = 1;
        }
        //points adında bir veri yapısına takımın adını ve kazandığı veya berabere kaldığı puanı ekler
        //takımın adı bulunmuyorsa, getOrDefault la direkt 0 puan verilir
        points.put(team, points.getOrDefault(team, 0) + point);
    }

    // Bir takımın oynadığı maç sayısını günceller.
    private void updateMatchesPlayed(String team) {
        matchesPlayed.put(team, matchesPlayed.getOrDefault(team, 0) + 1);
    }

    // Bir takımın kazandığı maç sayısını günceller.
    private void updateMatchesWon(String team, int teamScore, int opponentScore) {
        if (teamScore > opponentScore) {
            matchesWon.put(team, matchesWon.getOrDefault(team, 0) + 1);
        }
    }

    // Berabere biten maçların sayısını günceller.
    private void updateMatchesTied(int teamScore, int opponentScore) {
        if (teamScore == opponentScore) {
            for (String team : goals.keySet()) {
                matchesTied.put(team, matchesTied.getOrDefault(team, 0) + 1);
            }
        }
    }

    // Bir takımın kaybettiği maç sayısını günceller.
    private void updateMatchesLost(String team, int teamScore, int opponentScore) {
        if (teamScore < opponentScore) {
            matchesLost.put(team, matchesLost.getOrDefault(team, 0) + 1);
        }
    }

    // Bir takımın attığı gol sayısını alır.
    public int getGoals(String team) {
        return goals.getOrDefault(team, 0);
    }

    // Bir takımın kazandığı puanı alır.
    public int getPoints(String team) {
        return points.getOrDefault(team, 0);
    }

    // Takımları liderlik sırasına göre sıralar ve bir liderlik tablosu döndürür.
    public Map<String, Integer> getLeaderboard() {
        // Liderlik sıralamasını puana göre yap, yüksek puandan düşük puana
        Map<String, Integer> leaderboard = new LinkedHashMap<>();
        List<Map.Entry<String, Integer>> list = new LinkedList<>(points.entrySet());
        list.sort((o1, o2) -> {
            int result = o2.getValue().compareTo(o1.getValue()); // Puan
            if (result == 0) {
                result = getGoalDifference(o2.getKey()) - getGoalDifference(o1.getKey()); // Gol farkı
                if (result == 0) {
                    result = o1.getKey().compareTo(o2.getKey()); // Alfabetik sıralama
                }
            }
            return result;
        });
//list içindeki değerleri leaderboard Mapine aktarıyoruz. sıralama işlemi için
for (Map.Entry<String, Integer> entry : list) {
            leaderboard.put(entry.getKey(), entry.getValue());
        }
        return leaderboard;
    }

    // Bir takımın gol farkını hesaplar.
    public int getGoalDifference(String team) {
        int scored = goals.getOrDefault(team, 0);
        int conceded = goals.values().stream().mapToInt(Integer::intValue).sum() - scored;
        return scored - conceded;
    }
}
