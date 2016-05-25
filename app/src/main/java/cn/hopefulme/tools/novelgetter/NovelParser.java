package cn.hopefulme.tools.novelgetter;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hopef on 2016/5/25.
 * 小说解析器
 * 解析小说源，取得目录结构
 */
public class NovelParser {

    private Parser parser;

    private List<Link> list;

    public String[] getListLinkTexts() {
        String[] linkTexts = new String[list.size()];
        for(int i = 0; i < list.size(); i ++)
            linkTexts[i] = list.get(i).linkText;
        return linkTexts;
    }

    public String getListLink(int which) {
        return list.get(which).link;
    }

    public NovelParser(final String url) {
        this(url,null);
    }

    public NovelParser(final String url,final Runnable callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list = new ArrayList<>(2);
                    parser = new Parser((new URL(url)).openConnection());
                    parser.setEncoding("gbk");
                    parserList();
                    if(callback != null) callback.run();
                } catch (MalformedURLException e){
                } catch (ParserException e) {
                } catch (IOException e) {
                }
            }
        }).start();
    }


    private void parserList() {
        if(null == parser) return;
        parser.reset();
        try {
            NodeFilter filter = new HasAttributeFilter("id","list");
            NodeList nodes = parser.extractAllNodesThatMatch(filter);
            NodeVisitor visitor = new NodeVisitor() {
                @Override
                public void visitTag(Tag tag) {
                    if(tag instanceof LinkTag) {
                        LinkTag linkTag = (LinkTag) tag;
                        Link link = new Link(linkTag.getLink(),linkTag.getLinkText());
                        list.add(link);
                    }
                }
            };
            nodes.elementAt(0).accept(visitor);
        } catch (ParserException e) {
        }
    }

    private class Link {
        private String link;
        private String linkText;

        private Link(String link,String linkText) {
            this.link = link;
            this.linkText = linkText;
        }
    }
}
