package com.java.wangyihan.data.model;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RssHandler extends DefaultHandler {

    RssFeed rssFeed;
    RssItem rssItem;

    String lastElementName = "";// 标记变量，用于标记在解析过程中我们关心的几个标签，若不是我们关心的标签记做0

    final int RSS_TITLE = 1;// 若是 title 标签，记做1，注意有两个title，但我们都保存在item的成员变量中
    final int RSS_LINK = 2;// 若是 link 标签，记做2
    final int RSS_DESCRIPTION = 3;// 若是 description 标签，记做3
    final int RSS_CATEGORY = 4;// 若是category标签,记做 4
    final int RSS_PUBDATE = 5; // 若是pubdate标签,记做5,注意有两个pubdate,但我们都保存在item的pubdate成员变量中

    int currentFlag = 0;

    public RssHandler() {

    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        rssFeed = new RssFeed();
        rssItem = new RssItem();

    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        // 获取字符串
        String text = new String(ch, start, length);
        //Log.e("要获取的内容：" , text);

        switch (currentFlag) {
            case RSS_TITLE:
                rssItem.setTitle(rssItem.getTitle() + text);
                //currentFlag = 0;// 设置完后，重置为开始状态
                break;
            case RSS_PUBDATE:
                rssItem.setPubdate(rssItem.getPubdate() + text);
                //currentFlag = 0;// 设置完后，重置为开始状态
                break;
            case RSS_CATEGORY:
                rssItem.setCategory(rssItem.getCategory() + text);
                //currentFlag = 0;// 设置完后，重置为开始状态
                break;
            case RSS_LINK:
                rssItem.setLink(rssItem.getLink() + text);
                //currentFlag = 0;// 设置完后，重置为开始状态
                break;
            case RSS_DESCRIPTION:
                rssItem.setDescription(rssItem.getDescription() + text);
                //currentFlag = 0;// 设置完后，重置为开始状态
                break;
            default:
                break;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        //System.out.println(localName + " " + qName);
        //Log.e(localName, qName);
        if ("chanel".equals(qName)) {
            // 这个标签内没有我们关心的内容，所以不作处理，currentFlag=0
            currentFlag = 0;
            return;
        }
        if ("item".equals(qName)) {
            rssItem = new RssItem();
            //Log.e(localName, qName);
            //System.out.println("get item");
            currentFlag = 0;
            return;
        }
        if ("title".equals(qName)) {
            currentFlag = RSS_TITLE;
            return;
        }
        if ("description".equals(qName)) {
            currentFlag = RSS_DESCRIPTION;
            return;
        }
        if ("link".equals(qName)) {
            currentFlag = RSS_LINK;
            return;
        }
        if ("pubDate".equals(qName)) {
            currentFlag = RSS_PUBDATE;
            return;
        }
        if ("category".equals(qName)) {
            currentFlag = RSS_CATEGORY;
            return;
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
        // 如果解析一个item节点结束，就将rssItem添加到rssFeed中。
        if ("item".equals(qName)) {

            if (rssFeed == null)
                rssFeed = new RssFeed();
            rssFeed.addItem(rssItem);
            return;
        }
        if ("title".equals(qName)) {
            currentFlag = 0;
            return;
        }
        if ("description".equals(qName)) {
            currentFlag = 0;
            return;
        }
        if ("link".equals(qName)) {
            currentFlag = 0;
            return;
        }
        if ("pubDate".equals(qName)) {
            currentFlag = 0;
            return;
        }
        if ("category".equals(qName)) {
            currentFlag = 0;
            return;
        }

    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    public RssFeed getRssFeed() {
        return rssFeed;
    }

}


