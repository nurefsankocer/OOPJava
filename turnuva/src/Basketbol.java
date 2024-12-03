import java.util.*;

public class Basketbol {
    /*  bu kısımda basketbol takımlarının kazandığı puanlar,
     oynadıkları maç sayısı, kazandıkları, berabere kaldıkları
     ve kaybettikleri maç sayılarını saklamak için  Map
     veri yapısını kulandım(anahtar,değer)
     */
    private Map<String, Integer> points = new HashMap<>();
    private Map<String, Integer> goalsScored = new HashMap<>();
    private Map<String, Integer> goalsConceded = new HashMap<>();
    private Map<String, Integer> matchesPlayed = new HashMap<>();
    private Map<String, Integer> matchesWon = new HashMap<>();
    private Map<String, Integer> matchesTied = new HashMap<>();
    private Map<String, Integer> matchesLost = new HashMap<>();

    /* burda iki basketbol takımı arasındaki bir maç sonucunu işletip
     takımların puanlarını ve istatistiklerini güncellrttirdik
     */
    public void addMatchResult(String team1, String team2, int score1, int score2) {
        updatePointsAndStats(team1, score1, score2);
        updatePointsAndStats(team2, score2, score1);
    }

    private void updatePointsAndStats(String team, int goalsScored, int goalsConceded) {
        int matchesPlayedValue = matchesPlayed.getOrDefault(team, 0) + 1;
        // oynanan macı bir arttırdık
        int matchesWonValue = matchesWon.getOrDefault(team, 0);
        int matchesTiedValue = matchesTied.getOrDefault(team, 0);
        int matchesLostValue = matchesLost.getOrDefault(team, 0);
        int goalsScoredValue = this.goalsScored.getOrDefault(team, 0) + goalsScored;
        //  takımın toplam attığı gol sayısını günceledi
        int goalsConcededValue = this.goalsConceded.getOrDefault(team, 0) + goalsConceded;
        // toplam yenilen golü güncelledi
        int pointsValue = points.getOrDefault(team, 0);

        if (goalsScored > goalsConceded) {
            pointsValue += 2;
            matchesWonValue++;
        } else if (goalsScored == goalsConceded) {
            pointsValue += 1;
            matchesTiedValue++;
        } else {
            matchesLostValue++;
        }
/* eger atılan gol sayısı, yenen gol sayısından fazlaysa, takımın puanı 2 artırılır
 ve kazandığı maç sayısı bir artırılır.
goalsScored ve goalsConceded eşitse, takımın puanı 1 artırılır
 ve berabere kaldığı maç sayısı bir artırılır.
Eğer goalsScored, goalsConceded den azsa,
 takımın kaybettiği maç sayısı bir artırılır. */

        matchesPlayed.put(team, matchesPlayedValue);
        matchesWon.put(team, matchesWonValue);
        matchesTied.put(team, matchesTiedValue);
        matchesLost.put(team, matchesLostValue);
        this.goalsScored.put(team, goalsScoredValue);
        this.goalsConceded.put(team, goalsConcededValue);
        points.put(team, pointsValue);
    }
// güncellenen değerleri tek tek Map 'e geri yazdık
    public Map<String, Integer> getLeaderboard() {
        //  liderlik tablosu oluşturduk.bu tablo, takım adı ve puanları içeren bir LinkedHashMap nesnesi olur
        Map<String, Integer> leaderboard = new LinkedHashMap<>();
        List<Map.Entry<String, Integer>> list = new LinkedList<>(points.entrySet());

        list.sort((o1, o2) -> {
            int result = o2.getValue().compareTo(o1.getValue());
            if (result == 0) {
                result = getGoalDifference(o2.getKey()) - getGoalDifference(o1.getKey());
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

    public String getTeamStats(String team) {
        int matchesPlayedValue = matchesPlayed.getOrDefault(team, 0);
        int matchesWonValue = matchesWon.getOrDefault(team, 0);
        int matchesTiedValue = matchesTied.getOrDefault(team, 0);
        int matchesLostValue = matchesLost.getOrDefault(team, 0);
        int goalsScoredValue = goalsScored.getOrDefault(team, 0);
        int goalsConcededValue = goalsConceded.getOrDefault(team, 0);
        int pointsValue = points.getOrDefault(team, 0);

        return matchesPlayedValue + "\t" + matchesWonValue + "\t" + matchesTiedValue + "\t" + matchesLostValue + "\t" + goalsScoredValue + ":" + goalsConcededValue + "\t" + pointsValue;
    }

 // belirli bir takımın gol farkını hesaplamak için
     public int getGoalDifference(String team) {
        int scored = goalsScored.getOrDefault(team, 0);
        // goalsScored adlı Mapten, verilen takımın attığı gol sayısını alır.
         // Eğer verilen takım goalsScored mapinde bulunmuyorsa 0 olarak kabul edilir
         // bu da takımın attığı gol sayısı demek
        int conceded = goalsConceded.getOrDefault(team, 0);
        //  burda da goalsConceded adındaki Map ten, verilen takımın yediği gol sayısını aldık.
         //  eğer verilen takım goalsConceded veri yapısında bulunmuyorsa, 0 kabul ederiz.
         //  bu da takımın yediği gol sayısı demek
        return scored - conceded;
        // son olarak bir takımın attığı ve yediği gol sayıları arasındaki farkı hesaplattık
    }
}
