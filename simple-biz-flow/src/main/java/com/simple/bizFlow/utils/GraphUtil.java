package com.simple.bizFlow.utils;

import com.simple.bizFlow.core.node.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author joooohnli  2020-04-28 8:17 PM
 */
public class GraphUtil {
    /**
     * graphviz生成svg
     *
     * @param flow
     * @param imagePath svg图片全路径
     */
    public static void drawGraph(Flow flow, String imagePath) {
        Graph g = getFlowGraph(flow);
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {");
        sb.append(g.toDiagram());
        sb.append("}");

        try {
            Graphviz.fromString(sb.toString()).render(Format.SVG).toFile(new File(imagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Graph getFlowGraph(Flow flow) {
        Graph graph = new Graph();
        graph.getNodes().add(new N(flow).setnType(NType.START));
        List<N> ns = flow.getNodes().stream().map(GraphUtil::getNodeGraph).collect(Collectors.toList());
        graph.getNodes().addAll(ns);
        graph.getNodes().add(new N(flow.getId() + "_end", flow.getId() + "_end").setnType(NType.END));

        List<N> link = new ArrayList<>();
        for (N n : graph.getNodes()) {
            if (n instanceof Graph) {
                Graph g = (Graph) n;
                link.add(g.getStart());
                // copy
                graph.getLinks().add(new ArrayList<>(link));

                link.clear();
                link.add(g.getEnd());
            } else {
                link.add(n);
            }
        }
        if (link.size() > 1) {
            graph.getLinks().add(new ArrayList<>(link));
        }

        return graph;
    }

    private static N getNodeGraph(Node node) {
        if (node instanceof Flow) {
            Flow flow = (Flow) node;

            Graph graph = new Graph();
            graph.setnType(NType.FLOW);
            graph.setCluster(flow.getId());
            List<N> ns = flow.getNodes().stream().map(GraphUtil::getNodeGraph).collect(Collectors.toList());
            graph.getNodes().addAll(ns);

            List<N> link = new ArrayList<>();
            for (N n : graph.getNodes()) {
                if (n instanceof Graph) {
                    Graph g = (Graph) n;
                    link.add(g.getStart());
                    // copy
                    graph.getLinks().add(new ArrayList<>(link));

                    link.clear();
                    link.add(g.getEnd());
                } else {
                    link.add(n);
                }
            }
            if (link.size() > 1) {
                graph.getLinks().add(new ArrayList<>(link));
            }

            return graph;
        } else if (node instanceof ParallelNode) {
            ParallelNode parallelNode = (ParallelNode) node;

            Graph graph = new Graph();
            N start = new N(parallelNode).setnType(NType.PARALLEL);
            graph.getNodes().add(start);
            graph.setnType(NType.PARALLEL);
            graph.setCluster(parallelNode.getId());
            List<N> ns = parallelNode.getFlows().stream().map(GraphUtil::getNodeGraph).collect(Collectors.toList());
            graph.getNodes().addAll(ns);
            N end = new N(parallelNode.getMerge());
            graph.getNodes().add(end);

            // first and end
            ns.forEach(n -> {
                if (n instanceof Graph) {
                    Graph g = (Graph) n;
                    graph.getLinks().add(Arrays.asList(start, g.getStart()));
                    graph.getLinks().add(Arrays.asList(g.getEnd(), end));
                } else {
                    graph.getLinks().add(Arrays.asList(start, n));
                    graph.getLinks().add(Arrays.asList(n, end));
                }
            });

            return graph;
        } else if (node instanceof SelectNode) {
            SelectNode selectNode = (SelectNode) node;
            List<Flow> flows = selectNode.getFlows();
            Graph graph = new Graph();
            graph.setnType(NType.SELECT);
            N start = new N(selectNode).setnType(NType.SELECT);
            graph.getNodes().add(start);
            List<N> ns = flows.stream().map(GraphUtil::getNodeGraph).collect(Collectors.toList());
            graph.getNodes().addAll(ns);
            N end = new N(selectNode.getId() + "_end", selectNode.getId() + "_end");
            graph.getNodes().add(end);

            // first and end
            ns.forEach(n -> {
                if (n instanceof Graph) {
                    Graph g = (Graph) n;
                    graph.getLinks().add(Arrays.asList(start, g.getStart()));
                    graph.getLinks().add(Arrays.asList(g.getEnd(), end));
                } else {
                    graph.getLinks().add(Arrays.asList(start, n));
                    graph.getLinks().add(Arrays.asList(n, end));
                }
            });

            return graph;
        } else if (node instanceof WorkNode) {
            return new N(node);
        }
        throw new RuntimeException("unknown node type:" + node);
    }


    static class Graph extends N {
        // define nodes
        private List<N> nodes = new ArrayList<>();
        // links
        private List<List<N>> links = new ArrayList<>();

        private String cluster;

        public String toDiagram() {
            StringBuilder sb = new StringBuilder();
            String nodesDefine = nodes.stream().filter(n -> !(n instanceof Graph)).map(n -> buildNodeDefine(n)).collect(Collectors.joining("\n"));
            String subGrah = nodes.stream().filter(n -> n instanceof Graph).map(g -> buildSubGraph((Graph) g)).collect(Collectors.joining(""));
            if (nodesDefine != null) {
                sb.append(nodesDefine);
            }
            if (subGrah != null) {
                sb.append(subGrah);
            }
            links.forEach(link -> {
                String linkStr = link.stream().map(n -> String.format("\"%s\"", n.id)).collect(Collectors.joining("->"));
                if (linkStr != null) {
                    sb.append(linkStr).append(";");
                }
            });
            return sb.toString();
        }

        @Override
        public String getId() {
            String id = super.getId();
            if (id != null) {
                return id;
            }
            return nodes.get(0).id;
        }


        public List<N> getNodes() {
            return nodes;
        }

        public Graph setNodes(List<N> nodes) {
            this.nodes = nodes;
            return this;
        }

        public List<List<N>> getLinks() {
            return links;
        }

        public Graph setLinks(List<List<N>> links) {
            this.links = links;
            return this;
        }

        public String getCluster() {
            return cluster;
        }

        public Graph setCluster(String cluster) {
            this.cluster = cluster;
            return this;
        }

        public N getStart() {
            N n = nodes.get(0);
            if (n instanceof Graph) {
                return ((Graph) n).getStart();
            }
            return n;
        }

        public N getEnd() {
            N n = nodes.get(nodes.size() - 1);
            if (n instanceof Graph) {
                return ((Graph) n).getEnd();
            }
            return n;
        }
    }

    enum NType {
        START, END, PARALLEL, SELECT, FLOW
    }

    static class N {
        private String id;
        private String label;

        private NType nType;

        public N() {
        }

        public N(Node node) {
            this.id = node.getId();
            List<String> args = Arrays.asList(node.getId(), node.getBean(), node.getDescription());
            this.label = args.stream().filter(Objects::nonNull).collect(Collectors.joining("\n"));
        }

        public N(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public NType getnType() {
            return nType;
        }

        public N setnType(NType nType) {
            this.nType = nType;
            return this;
        }

        public String getId() {
            return id;
        }

        public N setId(String id) {
            this.id = id;
            return this;
        }

        public String getLabel() {
            return label;
        }

        public N setLabel(String label) {
            this.label = label;
            return this;
        }
    }

    static class SubGraph {
        String cluster;
        String color;
        String label;

        public SubGraph(Graph graph) {
            this.cluster = graph.cluster;
            this.color = color;
            this.label = label;
        }
    }

    private static String buildNodeDefine(N n) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\"", n.getId()));

        List<String> args = new ArrayList<>();
        args.add(String.format("label=\"%s\"", n.getLabel()));
        args.add("style=filled");
        if (n.getnType() == NType.START) {
            args.add("shape=circle");
        } else if (n.getnType() == NType.END) {
            args.add("shape=doublecircle");
        } else if (n.getnType() == NType.SELECT) {
            args.add("shape=diamond");
            args.add("color=darkseagreen");
        } else if (n.getnType() == NType.PARALLEL) {
            args.add("shape=parallelogram");
            args.add("color=darkorange1");
        } else {
//            args.add("color=azure");
        }
        sb.append("[");
        sb.append(String.join(",", args));
        sb.append("]");
        sb.append(";");
        return sb.toString();
    }

    private static String buildSubGraph(Graph graph) {
        String cluster = graph.getCluster() != null ? graph.getCluster() : graph.getId();

        StringBuilder sb = new StringBuilder();
        sb.append("subgraph ");
        if (graph.getnType() != null && graph.getnType() == NType.FLOW) {
            sb.append(String.format("\"cluster_%s\"", cluster));
        } else {
            // wil not display
            sb.append("x");
        }
        sb.append("{");
        sb.append(String.format("label=\"%s\";", cluster));
        sb.append("type=dotted;");
        if (graph.getnType() != null && graph.getnType() == NType.FLOW) {
            sb.append("color=lightskyblue1;");
        }
        sb.append(graph.toDiagram());
        sb.append("}");
        return sb.toString();
    }
}
