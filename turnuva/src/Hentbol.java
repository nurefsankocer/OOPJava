import java.util.*;

public class Hentbol {
    /*  bu kısımda hentbol takımlarının kazandığı puanlar,
     oynadıkları maç sayısı, kazandıkları, berabere kaldıkları
     ve kaybettikleri maç sayılarını saklamak için  Map
     veri yapısını kulandım
     */
    private Map<String, Integer> goalsScored = new HashMap<>();
    private Map<String, Integer> goalsConceded = new HashMap<>();
    private Map<String, Integer> points = new HashMap<>();
    private Map<String, Integer> matchesPlayed = new HashMap<>();
    private Map<String, Integer> matchesWon = new HashMap<>();
    private Map<String, Integer> matchesTied = new HashMap<>();
    private Map<String, Integer> matchesLost = new HashMap<>();

    public void addMatchResult(String team1, String team2, int scored, int conceded) {
        // Takımların gol, puan ve maç bilgilerini güncelle
        updateGoalsScored(team1, scored);
        updateGoalsConceded(team1, conceded);
        updateGoalsScored(team2, conceded);
        updateGoalsConceded(team2, scored);
        updatePoints(team1, scored, conceded);
        updatePoints(team2, conceded, scored);
        updateMatchesPlayed(team1);
        updateMatchesPlayed(team2);
        updateMatchesWon(team1, scored, conceded);
        updateMatchesWon(team2, conceded, scored);
        updateMatchesTied(scored, conceded);
        updateMatchesLost(team1, scored, conceded);
        updateMatchesLost(team2, conceded, scored);
    }
//bir takımın adını ve attığı gol sayısını alıp ve goalsScored yapısını güncelletirdil
    private void updateGoalsScored(String team, int scored) {
        goalsScored.put(team, goalsScored.getOrDefault(team, 0) + scored);
    }
//bi takımın yedigi gol sayısını güncelledi
    private void updateGoalsConceded(String team, int conceded) {
        goalsConceded.put(team, goalsConceded.getOrDefault(team, 0) + conceded);
    }

   // puanını hesaplattık
    private void updatePoints(String team, int scored, int conceded) {
        int point = 0;
        if (scored > conceded) {
            point = 3;
        } else if (scored == conceded) {
            point = 1;
        }
        points.put(team, points.getOrDefault(team, 0) + point);
    }
 // takımın oydadıgı mac sayısını güncellettik
    private void updateMatchesPlayed(String team) {
        matchesPlayed.put(team, matchesPlayed.getOrDefault(team, 0) + 1);
    }

    //kazanılan mac sayısını güncelletme
    private void updateMatchesWon(String team, int scored, int conceded) {
        if (scored > conceded) {
            matchesWon.put(team, matchesWon.getOrDefault(team, 0) + 1);
        }
    }

    private void updateMatchesTied(int scored, int conceded) {
        if (scored == conceded) {
            for (String team : goalsScored.keySet()) {
                // goalsScored Mapinde takım adlarını dolaşır
                matchesTied.put(team, matchesTied.getOrDefault(team, 0) + 1);
            }
        }
    }
//kaybedilen mac sayısını günceller
    private void updateMatchesLost(String team, int scored, int conceded) {
        if (scored < conceded) {
            matchesLost.put(team, matchesLost.getOrDefault(team, 0) + 1);
        }
    }

    public int getGoalsScored(String team) {
        return goalsScored.getOrDefault(team, 0);
    }

    public int getGoalsConceded(String team) {
        return goalsConceded.getOrDefault(team, 0);
    }

    public int getPoints(String team) {
        return points.getOrDefault(team, 0);
    }

    public Map<String, Integer> getLeaderboard() {
        // Liderlik sıralamasını puana ve averaja (gol farkı) göre yap
        List<Map.Entry<String, Integer>> list = new ArrayList<>(points.entrySet());
//points Mapinin girdileri (Map.Entry) bi liste içine kopyalanır.
// Bu listede takımların adı ve puanları var
        list.sort((o1, o2) -> {
            int result = o2.getValue().compareTo(o1.getValue()); // Puan
            if (result == 0) {
                result = calculateGoalDifference(o2.getKey()) - calculateGoalDifference(o1.getKey()); // Averaj (gol farkı)
                if (result == 0) {
                    result = o1.getKey().compareTo(o2.getKey()); // Alfabetik sıralama
                }
            }
            return result;
        });

        Map<String, Integer> leaderboard = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            leaderboard.put(entry.getKey(), entry.getValue());
        }
        return leaderboard;
    }

    public int calculateGoalDifference(String team) {
        int scored = goalsScored.getOrDefault(team, 0);
        int conceded = goalsConceded.getOrDefault(team, 0);
        return scored - conceded;
    }

    public String getTeamStats(String team) {
        int matchesPlayedValue = matchesPlayed.getOrDefault(team, 0);
        int matchesWonValue = matchesWon.getOrDefault(team, 0);
        int matchesTiedValue = matchesTied.getOrDefault(team, 0);
        int matchesLostValue = matchesLost.getOrDefault(team, 0);
        int goalsScoredValue = goalsScored.getOrDefault(team, 0);
        int goalsConcededValue = goalsConceded.getOrDefault(team, 0);
        int pointsValue = points.getOrDefault(team, 0);
        int goalDifference = goalsScoredValue - goalsConcededValue;

        return String.format("%d\t%s\t%d\t%d\t%d\t%d\t%d:%d\t%d",
                getLeaderboard().size(), team, matchesPlayedValue, matchesWonValue, matchesTiedValue, matchesLostValue, goalsScoredValue, goalsConcededValue, pointsValue);
    }

}
