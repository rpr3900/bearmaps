package bearmaps.hw4;
import bearmaps.proj2ab.DoubleMapPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private HashMap<Vertex, Vertex> edgeTo;
    private HashMap<Vertex, Double> distTo;
    private DoubleMapPQ<Vertex> fringe;
    private LinkedList<Vertex> solutions;
    private SolverOutcome outcome;
    private double solutionWeight;
    private double time;
    private int nums;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();
        fringe = new DoubleMapPQ<>();
        solutions = new LinkedList<>();
        solutionWeight = 0.0;
        time = 0.0;

        edgeTo.put(start, null);
        fringe.add(start, heuristic(input, start, end));
        distTo.put(start, 0.0);

        //helper method to AStarSolver
        starHelper(input, start, end, timeout);
    }

    private double heuristic(AStarGraph<Vertex> input, Vertex start, Vertex end) {
        return input.estimatedDistanceToGoal(start, end);
    }
    private void starHelper(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        Vertex p;
        while (fringe.size() != 0 && (time = sw.elapsedTime() / 1000) <= timeout) {
            p = fringe.removeSmallest();
            nums++;
            if (p.equals(end)) {
                solutionWeight = distTo.get(end);
                solutions = new LinkedList<>();
                Vertex smallest = end;
                while (smallest != null) {
                    solutions.addFirst(smallest);
                    smallest = edgeTo.get(smallest);
                }
                outcome = SolverOutcome.SOLVED;
                time = sw.elapsedTime() / 1000;
                return;
            }
            List<WeightedEdge<Vertex>> neighborEdges = input.neighbors(p);
            for (WeightedEdge<Vertex> e : neighborEdges) {
                relaxEdges(e, input, end);
            }
        }
        if ((time = sw.elapsedTime() / 1000) >= timeout) {
            outcome = SolverOutcome.TIMEOUT;
        } else {
            outcome = SolverOutcome.UNSOLVABLE;
        }
    }
    private void relaxEdges(WeightedEdge<Vertex> e, AStarGraph<Vertex> input, Vertex end) {
        double w = e.weight();
        Vertex q = e.to();
        Vertex p = e.from();
        double smallestDistance = distTo.get(p) + w;
        if (!distTo.containsKey(q)) {
            distTo.put(q, smallestDistance);
            edgeTo.put(q, p);
            fringe.add(q, distTo.get(q) + heuristic(input, q, end));
        } else {
            if (smallestDistance < distTo.get(q)) {
                distTo.put(q, smallestDistance);
                edgeTo.put(q, p);
                if (fringe.contains(q)) {
                    fringe.changePriority(q, distTo.get(q) + heuristic(input, q, end));
                }
            }
        }

    }

    public SolverOutcome outcome() {
        return outcome;
    }
    public List<Vertex> solution() {
        return solutions;
    }
    public double solutionWeight() {
        return solutionWeight;
    }
    public int numStatesExplored() {
        return nums;
    }
    public double explorationTime() {
        return time;
    }
}
