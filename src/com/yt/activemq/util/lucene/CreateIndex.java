package com.yt.activemq.util.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

/**
 * Created by Administrator on 2016/1/22 0022.
 */
public class CreateIndex {
    private static final String driverClassName = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf-8";
    private static final String username = "root";
    private static final String password = "root";
    private Connection conn;
    //索引生成目录
    private static final String paths = "D:/testluncene";

    public Connection getConnection() {
        if (this.conn == null) {
            try {
                Class.forName(driverClassName);
                conn = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }


    public static Analyzer getAnalyzer() {
        Analyzer analyzer = new StandardAnalyzer();
        analyzer.setVersion(Version.LUCENE_5_4_0);
        return analyzer;
    }

    /**
     * @return
     */
    public static IndexWriter createIndexWriter() {
        IndexWriter indexWriter = null;
        try {
            Directory directory = FSDirectory.open(Paths.get(paths));
            IndexWriterConfig iwc = new IndexWriterConfig(getAnalyzer());
            indexWriter = new IndexWriter(directory, iwc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexWriter;
    }

    public static void closeIndexWriter(IndexWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (CorruptIndexException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public IndexReader getIndexReader(boolean enableNRTReader) {
        Directory dir = null;
        IndexReader reader = null;
        try {
            dir = FSDirectory.open(Paths.get(paths));
            if (null == reader) {
                reader = DirectoryReader.open(dir);
            } else {
                if (enableNRTReader && reader instanceof DirectoryReader) {
                    //开启近实时Reader,能立即看到动态添加/删除的索引变化
                    reader = DirectoryReader.openIfChanged((DirectoryReader) reader);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }


    /**
     * 获取IndexSearcher对象
     *
     * @param reader   IndexReader对象实例
     * @return
     */
    public IndexSearcher getIndexSearcher(IndexReader reader) {
        IndexSearcher searcher = null;
        if (null == reader) {
            throw new IllegalArgumentException("The indexReader can not be null.");
        }
        if (null == searcher) {
            searcher = new IndexSearcher(reader);
        }
        return searcher;
    }

    /**
     * 存储方式分为3种：1、完全存储（Field.Store.YES）；2、不存储（Field.Store.NO）；3、压缩存储（Field.Store.COMPRESS）。
     * 索引方式分为4种：1、不索引（Field.Index.NO）；2、 Field.Index.ANALYZED ；3、 Field.Index.NOT_ANALYZED；4、Field.Index.NOT_ANALYZED_NO_NORMS
     * 创建索引
     */
    public static void createIndex() {
        String[] ids = {"1", "2", "3", "4"};
        String[] names = {"aa", "bb", "cc", "dd"};
        String[] contents = {
                "Using AbstractJExcelView to export data to Excel file via JExcelAPI library",
                "Using AbstractPdfView to export data to Pdf file via Bruno Lowagie’s iText library. ",
                "Example to integrate Log4j into the Spring MVC application. ",
                "Using Hibernate validator (JSR303 implementation) to validate bean in Spring MVC. "};
        IndexWriter writer = null;
        try {
            //创建索引写入者
            writer = createIndexWriter();
            Document doc = null;
            for (int i = 0; i < ids.length; i++) {
                doc = new Document();
                doc.add(new Field("id", ids[i], Field.Store.YES,
                        Field.Index.NOT_ANALYZED_NO_NORMS));
                doc.add(new Field("name", names[i], Field.Store.YES,
                        Field.Index.NOT_ANALYZED_NO_NORMS));
                doc.add(new Field("contents", contents[i], Field.Store.YES,
                        Field.Index.ANALYZED));
                writer.addDocument(doc);
                writer.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIndexWriter(writer);
        }
    }


    /**
     * 搜索
     * @param query
     * @throws Exception
     */
    public static void search(Query query) throws Exception {
        Directory dire=FSDirectory.open(Paths.get(paths));
        IndexReader ir=DirectoryReader.open(dire);
        IndexSearcher is=new IndexSearcher(ir);
        TopDocs td=is.search(query, 1000);
        System.out.println("共为您查找到"+td.totalHits+"条结果");
        ScoreDoc[] sds =td.scoreDocs;
        for (ScoreDoc sd : sds) {
            Document d = is.doc(sd.doc);
            System.out.println(d.getFields().get(0));
            System.out.println(d.get("path") + ":["+d.get("path")+"]");
        }
    }

}
