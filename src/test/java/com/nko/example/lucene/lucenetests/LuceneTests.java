package com.nko.example.lucene.lucenetests;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wltea.analyzer.lucene.IKAnalyzer;
import com.nko.example.lucene.model.Student;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


import java.io.IOException;

import java.nio.file.Paths;


/**
 * @ProjectName: lucene
 * @Package: com.nko.example.lucene.lucenetests
 * @ClassName: LuceneTests
 * @Author: sun71
 * @Description:
 * @Date: 2019/8/1 10:33
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LuceneTests {

        private Directory directory;
        private IndexReader indexReader;
        private IndexSearcher indexSearcher;

        /**
         * 创建索引
         * @throws IOException
         */
        @Test
        public void indexWriterTest() throws IOException {
            long start = System.currentTimeMillis();
            //设置索引存放的位置；设置在当前目录中
            Directory directory = FSDirectory.open(Paths.get("luceneindex"));
            //Analyzer analyzer = new StandardAnalyzer(); // 标准分词器，用于英文
            //Analyzer analyzer = new SmartChineseAnalyzer();//中文分词
            //Analyzer analyzer = new ComplexAnalyzer();//中文分词
            //Analyzer analyzer = new IKAnalyzer();//中文分词
            Analyzer analyzer = new IKAnalyzer();//中文分词
            //创建索引写入配置
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            //创建索引写入对象
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
            //创建Document对象，存储索引
            Document doc = new Document();
            Student student = new Student("张三","00001","男");
            //将字段加入到doc中
            doc.add(new StringField("name", student.getName(),Field.Store.YES));
            doc.add(new StringField("studentId", student.getStudentId(), Field.Store.YES));
            doc.add(new StringField("sex", student.getSex(), Field.Store.YES));

            //将doc对象保存到索引库中
            indexWriter.addDocument(doc);
            indexWriter.commit();
            //关闭流
            indexWriter.close();
            long end = System.currentTimeMillis();
            System.out.println("索引花费了" + (end - start) + " 毫秒");
        }

        /**
         * 按词条搜索
         * <p>
         * TermQuery是最简单、也是最常用的Query。TermQuery可以理解成为“词条搜索”，
         * 在搜索引擎中最基本的搜索就是在索引中搜索某一词条，而TermQuery就是用来完成这项工作的。
         * 在Lucene中词条是最基本的搜索单位，从本质上来讲一个词条其实就是一个名/值对。
         * 只不过这个“名”是字段名，而“值”则表示字段中所包含的某个关键字。
         *
         * @throws IOException
         */
        @Test
        public void termQueryTest() throws IOException {
            //索引存放的位置，设置在当前目录中
            directory = FSDirectory.open(Paths.get("luceneindex/"));
            //创建索引的读取器
            indexReader = DirectoryReader.open(directory);
            //创建一个索引的查找器，来检索索引库
            indexSearcher = new IndexSearcher(indexReader);
            String searchField = "name";
            //这是一个条件查询的api，用于添加条件
            TermQuery query = new TermQuery(new Term(searchField, "张三"));
            //执行查询，并打印查询到的记录数
            executeQuery(query);
        }

        /**
         * 执行查询，并打印查询到的记录数
         *
         * @param query
         * @throws IOException
         */
        public void executeQuery(Query query) throws IOException {

            TopDocs topDocs = indexSearcher.search(query, 100);
            //打印查询到的记录数
            System.out.println("总共查询到" + topDocs.totalHits + "个文档");
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {

                //取得对应的文档对象
                Document document = indexSearcher.doc(scoreDoc.doc);
                System.out.println("name：" + document.get("name"));
                System.out.println("studentId：" + document.get("studentId"));
                System.out.println("sex：" + document.get("sex"));
            }
        }
    }
