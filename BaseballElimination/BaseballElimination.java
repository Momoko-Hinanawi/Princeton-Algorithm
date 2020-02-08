/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class BaseballElimination
{
    private int teamAmount;
    private String names[];
    private int win[];
    private int loss[];
    private int rest[];
    private int g[][];
    private TreeMap<String, Integer> teamID = new TreeMap<>();

    public BaseballElimination(String filename)                    // create a baseball division From given filename in format specified below
    {
        //Read data from file
        In in = new In(filename);
        teamAmount = in.readInt();
        names = new String[teamAmount];
        win = new int[teamAmount];
        loss = new int[teamAmount];
        rest = new int[teamAmount];
        g = new int[teamAmount][teamAmount];
        for (int i = 0; i != teamAmount; ++i)
        {
            names[i] = in.readString();
            win[i] = in.readInt();
            loss[i] = in.readInt();
            rest[i] = in.readInt();
            teamID.put(names[i], i);
            for (int j = 0; j != teamAmount; ++j)
            {
                g[i][j] = in.readInt();
            }
        }


    }

    public int numberOfTeams()                        // number of teams
    {
        return teamAmount;
    }

    public Iterable<String> teams()                                // all teams
    {
        List<String> list = Arrays.asList(names);
        return list;
    }

    public int wins(String team)                      // number of wins for given team
    {
        if (!teamID.containsKey(team)) throw new IllegalArgumentException();
        return win[teamID.get(team)];
    }

    public int losses(String team)                    // number of losses for given team
    {
        if (!teamID.containsKey(team)) throw new IllegalArgumentException();
        return loss[teamID.get(team)];
    }

    public int remaining(String team)                 // number of remaining games for given team
    {
        if (!teamID.containsKey(team)) throw new IllegalArgumentException();
        return rest[teamID.get(team)];
    }

    public int against(String team1, String team2)    // number of remaining games between team1 and team2
    {
        if (!teamID.containsKey(team1)) throw new IllegalArgumentException();
        if (!teamID.containsKey(team2)) throw new IllegalArgumentException();
        return g[teamID.get(team1)][teamID.get(team2)];
    }

    private int getGameID(int i, int j)
    {
        return (teamAmount - 1) * i + j;
    }

    private int firstTeamID(int gameID)
    {
        return gameID / (teamAmount - 1);
    }

    private int secondTeamID(int gameID)
    {
        return gameID % (teamAmount - 1);
    }

    public boolean isEliminated(String team)              // is given team eliminated?
    {
        if (!teamID.containsKey(team)) throw new IllegalArgumentException();
        // calculate max possible wins
        int friendlyTeamID = teamID.get(team);
        int friendlyMaxScore = win[friendlyTeamID] + rest[friendlyTeamID];

        int[] maxWinsPossible = new int[teamAmount];
        for (int i = 0; i != teamAmount; ++i)
        {
            maxWinsPossible[i] = friendlyMaxScore - win[i];
            if (maxWinsPossible[i] < 0) return true; //trivial elimination
        }
        maxWinsPossible[friendlyTeamID] = 0;
        // build graph
        int sink = teamAmount * teamAmount + teamAmount + 1;
        FlowNetwork flowNetwork = new FlowNetwork(sink + 1);
        int capacity = 0;
        for (int i = 0; i != teamAmount; ++i)
        {
            for (int j = 0; j != teamAmount; ++j)
            {
                if (i >= j || i == friendlyTeamID || j == friendlyTeamID)
                    continue; //ignore repeated games or games with allys
                capacity += g[i][j];
                flowNetwork.addEdge(new FlowEdge(0, getGameID(i, j), g[i][j]));
                flowNetwork.addEdge(new FlowEdge(getGameID(i, j), teamAmount * teamAmount + i, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(getGameID(i, j), teamAmount * teamAmount + j, Double.POSITIVE_INFINITY));
            }
        }
        for (int i = 0; i != teamAmount; ++i)
        {
            flowNetwork.addEdge(new FlowEdge(teamAmount * teamAmount + i, sink, maxWinsPossible[i]));
        }
        // run max flow algorithm
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, sink);
        double maxFlow = fordFulkerson.value();
        // process output data
        return (maxFlow < capacity);
    }

    public Iterable<String> certificateOfElimination(String
                                                             team)  // subset R of teams that eliminates given team; null if not eliminated
    {
        if (!teamID.containsKey(team)) throw new IllegalArgumentException();
        // calculate max possible wins
        int friendlyTeamID = teamID.get(team);
        int friendlyMaxScore = win[friendlyTeamID] + rest[friendlyTeamID];

        int[] maxWinsPossible = new int[teamAmount];
        for (int i = 0; i != teamAmount; ++i)
        {
            maxWinsPossible[i] = friendlyMaxScore - win[i];
            if (maxWinsPossible[i] < 0)
            {
                Stack<String> stack = new Stack<>();
                stack.push(names[i]);
                return stack;
            }
            //trivial elimination

        }
        maxWinsPossible[friendlyTeamID] = 0;
        // build graph
        int sink = teamAmount * teamAmount + teamAmount + 1;
        FlowNetwork flowNetwork = new FlowNetwork(sink + 1);
        for (int i = 0; i != teamAmount; ++i)
        {
            for (int j = 0; j != teamAmount; ++j)
            {
                if (i >= j || i == friendlyTeamID || j == friendlyTeamID)
                    continue; //ignore repeated games or games with allys

                flowNetwork.addEdge(new FlowEdge(0, getGameID(i, j), g[i][j]));
                flowNetwork.addEdge(new FlowEdge(getGameID(i, j), teamAmount * teamAmount + i, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(getGameID(i, j), teamAmount * teamAmount + j, Double.POSITIVE_INFINITY));
            }
        }
        for (int i = 0; i != teamAmount; ++i)
        {
            flowNetwork.addEdge(new FlowEdge(teamAmount * teamAmount + i, sink, maxWinsPossible[i]));
        }
        // run max flow algorithm
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, sink);
        Stack<String> list = new Stack<>();
        for (int i = 0; i != teamAmount; ++i)
        {
            if (fordFulkerson.inCut(teamAmount * teamAmount + i))
                list.push(names[i]);
        }
        if (list.size() == 0) list = null;
        return list;
    }

    public static void main(String[] args)
    {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams())
        {
            if (division.isEliminated(team))
            {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else
            {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
