import java.util.*;

public class Voleybol {

    /*  bu kısımda voleybol takımlarının kazandığı puanlar,
     oynadıkları maç sayısı, kazandıkları, berabere kaldıkları
     ve kaybettikleri maç sayılarını saklamak için  Map
     veri yapısını kulandım
     */
    private Map<String, Integer> points = new HashMap<>();
    private Map<String, Integer> matchesPlayed = new HashMap<>();
    private Map<String, Integer> matchesWon = new HashMap<>();
    private Map<String, Integer> matchesTied = new HashMap<>();
    private Map<String, Integer> matchesLost = new HashMap<>();
    private Map<String, String> goalDifference = new HashMap<>();

    /* Burda iki voleybol takımı arasındaki bir maç sonucunu işletip,
     takımların puanlarını ve istatistiklerini güncelletirdik
     */
    public void addMatchResult(String team1, String team2, String score) {
        updatePointsAndStats(team1, team2, score);
        updatePointsAndStats(team2, team1, score);
    }

    private void updatePointsAndStats(String team, String opponent, String score) {
        int matchesPlayedValue = matchesPlayed.getOrDefault(team, 0) + 1;
        int matchesWonValue = matchesWon.getOrDefault(team, 0);
        int matchesTiedValue = matchesTied.getOrDefault(team, 0);
        int matchesLostValue = matchesLost.getOrDefault(team, 0);
        String goalDifferenceValue = goalDifference.getOrDefault(team, "0:0");

        String[] scoreParts = score.split(":");
        int goalsScored = Integer.parseInt(scoreParts[0]);
        int goalsConceded = Integer.parseInt(scoreParts[1]);

        if (goalsScored > goalsConceded) {
            matchesWonValue++;
        } else if (goalsScored == goalsConceded) {
            matchesTiedValue++;
        } else {
            matchesLostValue++;
        }

        matchesPlayed.put(team, matchesPlayedValue);
        matchesWon.put(team, matchesWonValue);
        matchesTied.put(team, matchesTiedValue);
        matchesLost.put(team, matchesLostValue);

        int scored = Integer.parseInt(scoreParts[0]) + Integer.parseInt(goalDifferenceValue.split(":")[0]);
        int conceded = Integer.parseInt(scoreParts[1]) + Integer.parseInt(goalDifferenceValue.split(":")[1]);

        goalDifference.put(team, scored + ":" + conceded);

        int pointsValue = (matchesWonValue * 3) + (matchesTiedValue * 1);
        points.put(team, pointsValue);
    }

    public Map<String, Integer> getLeaderboard() {
        Map<String, Integer> leaderboard = new LinkedHashMap<>();
        List<Map.Entry<String, Integer>> list = new LinkedList<>(points.entrySet());

        list.sort((o1, o2) -> {
            int result = o2.getValue().compareTo(o1.getValue());
            if (result == 0) {
                result = compareGoalDifference(o2.getKey(), o1.getKey());
                if (result == 0) {
                    result = o1.getKey().compareTo(o2.getKey());
                }
            }
            return result;
        });

        for (Map.Entry<String, Integer> entry : list) {
            leaderboard.put(entry.getKey(), entry.getValue());
        }
        return leaderboard;
    }

    private int compareGoalDifference(String team1, String team2) {
        String[] goalDifference1 = goalDifference.get(team1).split(":");
        String[] goalDifference2 = goalDifference.get(team2).split(":");

        int scored1 = Integer.parseInt(goalDifference1[0]);
        int conceded1 = Integer.parseInt(goalDifference1[1]);
        int scored2 = Integer.parseInt(goalDifference2[0]);
        int conceded2 = Integer.parseInt(goalDifference2[1]);

        int diff1 = scored1 - conceded1;
        int diff2 = scored2 - conceded2;

        if (diff1 > diff2) {
            return -1;
        } else if (diff1 < diff2) {
            return 1;
        } else {
            if (scored1 > scored2) {
                return -1;
            } else if (scored1 < scored2) {
                return 1;
            } else {
                return team1.compareTo(team2);
            }
        }
    }

    public String getTeamStats(String team) {
        int matchesPlayedValue = matchesPlayed.getOrDefault(team, 0);
        int matchesWonValue = matchesWon.getOrDefault(team, 0);
        int matchesTiedValue = matchesTied.getOrDefault(team, 0);
        int matchesLostValue = matchesLost.getOrDefault(team, 0);
        int pointsValue = points.getOrDefault(team, 0);
        int setsWonValue = Integer.parseInt(goalDifference.getOrDefault(team, "0:0").split(":")[0]);
        int setsLostValue = Integer.parseInt(goalDifference.getOrDefault(team, "0:0").split(":")[1]);

        return matchesPlayedValue + "\t" + matchesWonValue + "\t" + matchesTiedValue + "\t" + matchesLostValue + "\t" + setsWonValue + ":" + setsLostValue + "\t" + pointsValue;
    }
}
