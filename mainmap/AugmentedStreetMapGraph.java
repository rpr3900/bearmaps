package bearmaps.proj2c;

import lab9.MyTrieSet;
import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.WeirdPointSet;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private HashMap<Point, Node> hash;
    private WeirdPointSet map;
    private MyTrieSet alg = new MyTrieSet();
    private TreeMap<String, ArrayList<String>> collection;
    private TreeMap<String, ArrayList<String>> arboles;
    private TreeMap<String, ArrayList<Node>> nombres;


    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        hash = new HashMap<>();
        arboles = new TreeMap<>();
        nombres = new TreeMap<>();
        collection = new TreeMap<>();

        List<Node> nodes = getNodes();
        List<Point> points = new LinkedList<>();
        for (Node n : nodes) {
            algorithmHelper(n);
            long neighborsSize = neighbors(n.id()).size();
            if (neighborsSize > 0) {
                Point p = new Point(n.lon(), n.lat());
                points.add(p);
                hash.put(p, n);
            }
        }
        map = new WeirdPointSet(points);
    }

    /**Helper method to AugmentedStreetMapGraph
     * Autocomplete system where a user types in a partial query string.
     * @param n
     */
    private void algorithmHelper(Node n) {
        ArrayList<String> ll;
        ArrayList<Node> l;
        if (n.name() == null) {
            return;
        } else {
            String toClean = cleanString(n.name());
            alg.add(toClean);
            if (!arboles.containsKey(toClean)) {
                ll = new ArrayList<>();
                ll.add(n.name());
            } else {
                ll = arboles.get(toClean);
                if (!ll.contains(n.name())) {
                    ll.add(n.name());
                }
            }
            arboles.put(toClean, ll);
            if (!nombres.containsKey(toClean)) {
                l = new ArrayList<>();
                l.add(n);
            } else {
                l = nombres.get(toClean);
                if (!l.contains(n)) {
                    l.add(n);
                }
            }
            nombres.put(toClean, l);
        }
    }

    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        return hash.get(map.nearest(lon, lat)).id();
    }

    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        //return new LinkedList<>();
        List<String> prefixS = new ArrayList<>();
        String toClean = cleanString(prefix);
        if (collection.containsKey(prefix)) {
            prefixS = collection.get(prefix);
        } else {
            for (String s : alg.keysWithPrefix(toClean)) {
                prefixS.addAll(arboles.get(s));
            }
        }
        return prefixS;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> locationsMatch = new ArrayList<>();
        String match = cleanString(locationName);
        if (nombres.get(match) == null) {
            return locationsMatch;
        } else {
            for (Node nodeMatches : nombres.get(match)) {
                TreeMap<String, Object> key = new TreeMap<>();
                key.put("name", nodeMatches.name());
                key.put("id", nodeMatches.id());
                key.put("lat", nodeMatches.lat());
                key.put("lon", nodeMatches.lon());
                locationsMatch.add(key);
            }
        }
        return locationsMatch;
    }

    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
