package io.github.halilsenaydin.shared.utils;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.*;

public class CoveragePrinter {
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    // ANSI
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";

    // Column widths
    private static final int FILE_W = 45;
    private static final int COL_W = 9;

    public static void main(String[] args) throws Exception {
        String path = (args != null && args.length > 0)
                ? args[0]
                : "target/site/jacoco/jacoco.xml";

        File xml = new File(path);

        if (!xml.exists()) {
            System.out.println("Coverage XML not found: " + xml.getPath());
        
            return;
        }

        var dbf = DocumentBuilderFactory.newInstance();

        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        Document doc;

        try (var is = new FileInputStream(xml)) {
            doc = dbf.newDocumentBuilder().parse(is);
        }

        NodeList packages = doc.getElementsByTagName("package");

        printHeader();

        Totals total = new Totals();

        for (int i = 0; i < packages.getLength(); i++) {
            Element pkg = (Element) packages.item(i);
            NodeList files = pkg.getElementsByTagName("sourcefile");

            for (int j = 0; j < files.getLength(); j++) {
                Element file = (Element) files.item(j);
                String name = file.getAttribute("name");
                Counters c = readCounters(file);
                List<Integer> uncovered = readUncoveredLines(file);

                total.add(c);

                printRow(
                        name,
                        pct(c.instr),
                        pct(c.branch),
                        pct(c.method),
                        pct(c.line),
                        uncoveredLinesStr(uncovered));
            }
        }

        line();
        printTotal(total);
        line();
    }

    // ================= PRINT =================
    private static void printHeader() {
        line();
        System.out.println(
                padRight("File", FILE_W) +
                        padLeft("Stmts", COL_W) +
                        padLeft("Branch", COL_W) +
                        padLeft("Funcs", COL_W) +
                        padLeft("Lines", COL_W) +
                        "   Uncovered Line(s)");
        line();
    }

    private static void printRow(String file,
            double stmts,
            double branch,
            double funcs,
            double lines,
            String uncovered) {

        String row = padRight(file, FILE_W) +
                color(stmts) +
                color(branch) +
                color(funcs) +
                color(lines) +
                "   " + uncovered;

        System.out.println(row);
    }

    private static void printTotal(Totals t) {
        System.out.println(
                padRight("TOTAL", FILE_W) +
                        color(pct(t.instr)) +
                        color(pct(t.branch)) +
                        color(pct(t.method)) +
                        color(pct(t.line)));
    }

    private static void line() {
        System.out.println(CYAN +
                "-".repeat(110) +
                RESET);
    }

    // ================= FORMAT =================
    private static String color(double pct) {
        String raw = DF.format(pct) + "%";
        String padded = padLeft(raw, COL_W);

        if (pct >= 90)
            return GREEN + padded + RESET;

        if (pct >= 70)
            return YELLOW + padded + RESET;

        return RED + padded + RESET;
    }

    private static String padRight(String s, int w) {
        return s.length() >= w ? s.substring(0, w)
                : s + " ".repeat(w - s.length());
    }

    private static String padLeft(String s, int w) {
        return s.length() >= w ? s
                : " ".repeat(w - s.length()) + s;
    }

    // ================= DATA =================
    private static Counters readCounters(Element file) {
        NodeList counters = file.getElementsByTagName("counter");
        Counters c = new Counters();

        for (int i = 0; i < counters.getLength(); i++) {
            Element el = (Element) counters.item(i);
            String type = el.getAttribute("type");
            int covered = Integer.parseInt(el.getAttribute("covered"));
            int missed = Integer.parseInt(el.getAttribute("missed"));

            switch (type) {
                case "INSTRUCTION" -> c.instr.add(covered, missed);
                case "BRANCH" -> c.branch.add(covered, missed);
                case "METHOD" -> c.method.add(covered, missed);
                case "LINE" -> c.line.add(covered, missed);
            }
        }
        return c;
    }

    private static List<Integer> readUncoveredLines(Element file) {
        NodeList lines = file.getElementsByTagName("line");
        List<Integer> uncovered = new ArrayList<>();

        for (int i = 0; i < lines.getLength(); i++) {
            Element l = (Element) lines.item(i);
            int ci = Integer.parseInt(l.getAttribute("ci"));

            if (ci == 0) {
                uncovered.add(Integer.parseInt(l.getAttribute("nr")));
            }
        }
        return uncovered;
    }

    private static String uncoveredLinesStr(List<Integer> list) {
        if (list.isEmpty())
            return "";

        list.sort(Integer::compareTo);

        StringBuilder sb = new StringBuilder();
        int start = list.get(0);
        int prev = start;

        for (int i = 1; i < list.size(); i++) {
            int cur = list.get(i);

            if (cur == prev + 1) {
                prev = cur;
            } else {
                appendRange(sb, start, prev);

                sb.append(", ");

                start = prev = cur;
            }
        }
        appendRange(sb, start, prev);

        return sb.toString();
    }

    private static void appendRange(StringBuilder sb, int s, int e) {
        if (s == e)
            sb.append(s);

        else
            sb.append(s).append("-").append(e);
    }

    private static double pct(Counter c) {
        int total = c.covered + c.missed;

        return total == 0 ? 100 : (c.covered * 100.0 / total);
    }

    private static class Counter {
        int covered;
        int missed;

        void add(int c, int m) {
            covered += c;
            missed += m;
        }
    }

    private static class Counters {
        Counter instr = new Counter();
        Counter branch = new Counter();
        Counter method = new Counter();
        Counter line = new Counter();
    }

    private static class Totals extends Counters {
        void add(Counters c) {
            instr.add(c.instr.covered, c.instr.missed);
            branch.add(c.branch.covered, c.branch.missed);
            method.add(c.method.covered, c.method.missed);
            line.add(c.line.covered, c.line.missed);
        }
    }
}