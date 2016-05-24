package cn.hopefulme.tools.novelgetter;

import org.htmlparser.*;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by hopef on 2016/5/24.
 * 页面解析器
 * 解析指定url页面
 */
public class PageParser {

    private Parser parser;

    private String content;

    private String bookname;

    private String last;

    private String next;

    public String getContent() {
        return content == null ? "" : content;
    }

    public String getBookName() {
        return bookname == null ? "" : bookname;
    }

    public PageParser(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    parser = new Parser((new URL(url)).openConnection());
                    content = parserContent();
                    parser.reset();
                    bookname = parserBookName();
                }catch (MalformedURLException e) {
                }catch (IOException e) {
                }catch (ParserException e) {
                }
            }
        }).start();
    }

    private String parserContent() {
        if(parser == null) return null;
        NodeFilter filter = new HasAttributeFilter("id","content");
        try {
            NodeList nodes = parser.extractAllNodesThatMatch(filter);
            return nodes.size() > 0 ? nodesToText(nodes.elementAt(0).getChildren()) : null;

        } catch (ParserException e) {

        }
        return null;
    }

    private String nodesToText(NodeList nodes) {
        String text = "";
        for (Node node : nodes.toNodeArray()) {
            if(node instanceof TextNode) text += node.toPlainTextString();
            else if(node instanceof TagNode && ((TagNode)node).getTagName().equals("BR")) text += "\n";
        }
        return text;
    }

    private String parserBookName() {
        if(parser == null) return null;
        NodeFilter filter = new HasAttributeFilter("class","bookname");
        try {
            NodeList nodes = parser.extractAllNodesThatMatch(filter);
            return nodes.size() > 0 ? nodes.elementAt(0).getChildren().elementAt(1).getChildren().asString() : null;
        } catch (ParserException e) {

        }
        return null;
    }
}
