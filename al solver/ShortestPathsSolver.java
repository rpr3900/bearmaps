package bearmaps.hw4;

import java.util.List;

/**
 * Interface for shortest path solvers.
 */
public interface ShortestPathsSolver<Vertex> {
    SolverOutcome outcome();
    List<Vertex> solution();
    double solutionWeight();
    int numStatesExplored();
    double explorationTime();
}
