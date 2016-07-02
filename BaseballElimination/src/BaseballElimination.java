/**
 * Created by Zhao on 2015-11-21.
 */

import edu.princeton.cs.algs4.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class BaseballElimination {
    private int teamNum;
    private String[] teamNames;
    private List teamList;
    private int[] wins;
    private int[] lost;
    private int[] remain;
    private int[][] games;
    private int vertexNum;
    private Bag<String> betterTeams;
    private int cache;


    // convert team ids to the currespond vertex id in flownetwork
    private int idToCb(int id1, int id2) {
        int first = Math.min(id1, id2);
        int second = Math.max(id1, id2);

        int j = 0;
        for (int i = 1; i <= first; i++) {
            j += i;
        }

        int vid = first * teamNum - j + second - first;

        return vid;
    }

    private int teamVertex(int id) {
        // team vertex order is reversed just for the ease of computing.
        return vertexNum - id - 2;
    }

    private boolean testElimiate(String team) {
        int id = teamList.indexOf(team);
        betterTeams = new Bag<String>();

        for (int rivalId = 0; rivalId < teamNum; rivalId++) {
            if (id != rivalId) {
                if (wins[id] + remain[id] < wins[rivalId]) {
                    betterTeams.add(teamNames[rivalId]);
                }

            }
        }
        cache = id;
        if (betterTeams.size() > 0) {
            return true;
        } else {

            return complexElim(id);
        }
    }

    private boolean complexElim(int teamId) {

        //StdOut.println("Enter complex");

        FlowNetwork flow = buildFlow(teamId);
        FordFulkerson ff = new FordFulkerson(flow, 0, vertexNum - 1);
        //StdOut.println(ff.value());
        //StdOut.println(flow.toString());


        for (int i = 0; i < teamNum; i++) {
            if (ff.inCut(teamVertex(i))) {
                betterTeams.add(teamNames[i]);
            }
        }
        return (betterTeams.size() > 0);

    }

    private int capCal(int teamId, int rivalId) {
        int cap = wins[teamId] + remain[teamId] - wins[rivalId];
        if (cap < 0) {
            return 0;
        } else return cap;
    }


    private FlowNetwork buildFlow(int teamId) {
        vertexNum = teamNum * (teamNum - 1) / 2 + teamNum + 2;
        FlowNetwork flow = new FlowNetwork(vertexNum);

        // Add edges
        for (int team1 = 0; team1 < teamNum; team1++) {

            for (int team2 = team1 + 1; team2 < teamNum; team2++) {
                if (teamId != team1 && teamId != team2) {
                    flow.addEdge(new FlowEdge(0, idToCb(team1, team2), games[team1][team2]));
                    //StdOut.println(games[team1][team2]);
                    flow.addEdge(new FlowEdge(idToCb(team1, team2), teamVertex(team1), Double.POSITIVE_INFINITY));
                    flow.addEdge(new FlowEdge(idToCb(team1, team2), teamVertex(team2), Double.POSITIVE_INFINITY));


                }

            }

        }
        for (int i = 0; i < teamNum; i++) {
            if (i != teamId) {
                flow.addEdge(new FlowEdge(teamVertex(i), vertexNum - 1, capCal(teamId, i)));
            }
        }
        return flow;

    }

    private void testFlow(int teamid) {
        buildFlow(teamid);
        //StdOut.println(flow.toString());
    }


    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below
        In in = new In(filename);
        //get team number
        String line = in.readLine();
        teamNum = Integer.parseInt(line);
        teamNames = new String[teamNum];
        wins = new int[teamNum];
        lost = new int[teamNum];
        remain = new int[teamNum];
        games = new int[teamNum][teamNum];
        cache = -1;

        for (int c = 0; c < teamNum; c++) {
            line = in.readLine().trim();
            String[] tokens = line.split(" +");
            teamNames[c] = tokens[0];
            wins[c] = Integer.parseInt(tokens[1]);
            lost[c] = Integer.parseInt(tokens[2]);
            remain[c] = Integer.parseInt(tokens[3]);

            for (int i = 4; i < tokens.length; i++) {
                games[c][i - 4] = Integer.parseInt(tokens[i]);
            }
        }

        teamList = Arrays.asList(teamNames);


    }

    public int numberOfTeams() {
        return teamNum;
    }                       // number of teams

    public Iterable<String> teams()                                // all teams
    {
        return teamList;
    }

    public int wins(String team)                      // number of wins for given team
    {
        int i = teamList.indexOf(team);
        if (i == -1) {
            throw new IllegalArgumentException(team + "does not exist");
        }
        return wins[i];
    }

    public int losses(String team)                    // number of losses for given team
    {
        int i = teamList.indexOf(team);
        if (i == -1) {
            throw new IllegalArgumentException(team + "does not exist");
        }
        return lost[i];
    }

    public int remaining(String team)                 // number of remaining games for given team
    {
        int i = teamList.indexOf(team);
        if (i == -1) {
            throw new IllegalArgumentException(team + "does not exist");
        }
        return remain[i];
    }

    public int against(String team1, String team2)    // number of remaining games between team1 and team2
    {
        int i = teamList.indexOf(team1);
        int j = teamList.indexOf(team2);

        if (i == -1) {
            throw new IllegalArgumentException(team1 + "does not exist");
        }
        if (j == -1) {
            throw new IllegalArgumentException(team2 + "does not exist");
        }

        return games[i][j];
    }

    public boolean isEliminated(String team)              // is given team eliminated?
    {
        int i = teamList.indexOf(team);
        if (i == -1) {
            throw new IllegalArgumentException(team + "does not exist");
        }
        return testElimiate(team);
    }

    public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
    {
        int id = teamList.indexOf(team);
        if (cache == -1 || id != cache) {
            isEliminated(team);
        }
        //StdOut.println(betterTeams.size());
        if (!betterTeams.isEmpty()) {
            return betterTeams;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        // write your code here
        BaseballElimination test = new BaseballElimination("E:\\baseball\\teams4.txt");
        StdOut.println(test.teamNum);
        StdOut.println(test.teams());

        if (test.certificateOfElimination("Atlanta")!=null) {
            for (String team : test.certificateOfElimination("Atlanta")) {
                StdOut.println(team);
            }

            test.isEliminated("New_York");

            for (String team : test.certificateOfElimination("Atlanta")) {
                StdOut.println(team);
            }
        }


        test.testFlow(1);
    }
}
