package BellmanFordAlgo;

import Model.CurrencyData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VertexCreator {

    private CurrencyData currencyData;
    private List<Vertex> vertexList;
    private List<Edge> edgeList;
    private HashMap<String, Vertex> seenVertex;


    public VertexCreator(CurrencyData currencyData) {
        this.currencyData = currencyData;
        vertexList = new ArrayList<>();
        edgeList = new ArrayList<>();
        seenVertex = new HashMap<>();
    }

    public void addEdge(String s, String c, Double d){
        Vertex a = new Vertex(s);
        Vertex b = new Vertex(c);
        vertexList.add(a);
        vertexList.add(b);
        edgeList.add(initializeEdge(a,b,d));
    }

    private Map<String, ArrayList<Double>> createHashMapCopy(){
        Map<String, ArrayList<Double>> innerData = new HashMap<>();
        for(Object s: currencyData.getMap().keySet()){
            innerData.put((String) s, (ArrayList) currencyData.getMap().get(s));
        }
        return innerData;
    }

    public void initializeEdgeList() {
        // Can createa  set of things I have seen so far initalized as an empty set
        Map innerData = createHashMapCopy();


        List<String> keys = new ArrayList<>();
        for (Object o : innerData.keySet()) {
            String key = String.valueOf(o);
            keys.add(key);
        }


        List<String> iteratedKey = new ArrayList<>();
        List<String> tickers = currencyData.getTickers();
        for (String t : tickers) {
            for (String key : keys) {
                if(iteratedKey.contains(key)){
                    continue;
                }
                String exch1 = key.substring(0, 3);
                String exch2 = key.substring(4, 7);
                ArrayList<Double> d = (ArrayList<Double>) innerData.get(key);
                if (exch1.contains(t)) { // && check if t is in the set
                    edgeList.add(initializeEdge(findVertexById(exch1), findVertexById(exch2), (-1 * Math.log(d.get(0)))));
                    edgeList.add(initializeEdge(findVertexById(exch2), findVertexById(exch1), (1 / (-1 * Math.log(d.get(1))))));
                    innerData.remove(key);
                    iteratedKey.add(key);
                }
                if (exch2.contains(t)) {
                    edgeList.add(initializeEdge(findVertexById(exch2), findVertexById(exch1), (-1 * Math.log(d.get(1)))));
                    edgeList.add(initializeEdge(findVertexById(exch1), findVertexById(exch2), (1 / (-1 * Math.log(d.get(0))))));
                    innerData.remove(key);
                    iteratedKey.add(key);
                }
            }
        }
    }

    private Edge initializeEdge(Vertex startVertex, Vertex targetVertex, Double weight){
        Edge edge = new Edge(weight, seenVertex);
        edge.createTargetVertex(targetVertex);
        edge.createStartVertex(startVertex);

        return edge;
    }


    private Vertex findVertexById(String t) {
        for (Vertex v : vertexList) {
            if (v.getId().equals(t)) {
                return v;
            }
        }
        return null;
    }

    public void initializeVertexList() {
        List<String> tickers = currencyData.getTickers();
        for (String t : tickers) {
            vertexList.add(new Vertex(t));
        }
    }

    public CurrencyData getCurrencyData() {
        return currencyData;
    }

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public Map getData() {
        return this.currencyData.getMap();
    }

    public List<String> getTickers() {
        return this.currencyData.getTickers();
    }
}
