package com.reposcope.backend.engine;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class IssueDAGEngine {

    public Map<String, Object> buildAndSort(Map<Integer, List<Integer>> adjacencyList,
                                            Map<Integer, String> issueTitles) {
        List<Integer> topologicalOrder = kahnsAlgorithm(adjacencyList);
        List<Integer> cycles = detectCycles(adjacencyList, topologicalOrder);

        Map<String, Object> result = new HashMap<>();
        result.put("nodes", buildNodes(issueTitles));
        result.put("edges", buildEdges(adjacencyList));
        result.put("topological_order", topologicalOrder);
        result.put("cycles", cycles);
        return result;
    }

    private List<Integer> kahnsAlgorithm(Map<Integer, List<Integer>> graph) {
        Map<Integer, Integer> inDegree = new HashMap<>();
        for (int node : graph.keySet()) inDegree.put(node, 0);

        for (List<Integer> neighbors : graph.values()) {
            for (int neighbor : neighbors) {
                inDegree.put(neighbor,
                        inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (Map.Entry<Integer, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) queue.add(entry.getKey());
        }

        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int node = queue.poll();
            result.add(node);
            for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) queue.add(neighbor);
            }
        }
        return result;
    }

    private List<Integer> detectCycles(Map<Integer, List<Integer>> graph,
                                       List<Integer> topologicalOrder) {
        List<Integer> cycles = new ArrayList<>();
        for (int node : graph.keySet()) {
            if (!topologicalOrder.contains(node)) cycles.add(node);
        }
        return cycles;
    }

    private List<Map<String, Object>> buildNodes(Map<Integer, String> titles) {
        List<Map<String, Object>> nodes = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : titles.entrySet()) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", entry.getKey());
            node.put("title", entry.getValue());
            nodes.add(node);
        }
        return nodes;
    }

    private List<Map<String, Object>> buildEdges(Map<Integer, List<Integer>> graph) {
        List<Map<String, Object>> edges = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : graph.entrySet()) {
            for (int to : entry.getValue()) {
                Map<String, Object> edge = new HashMap<>();
                edge.put("from", entry.getKey());
                edge.put("to", to);
                edges.add(edge);
            }
        }
        return edges;
    }
}