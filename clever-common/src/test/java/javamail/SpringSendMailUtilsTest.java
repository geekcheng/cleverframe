package javamail;

import org.apache.commons.io.FileUtils;
import org.cleverframe.common.javamail.SpringSendMailUtils;
import org.cleverframe.common.time.DateTimeUtils;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-5-21 23:16 <br/>
 */
public class SpringSendMailUtilsTest {
    private final static Logger logger = LoggerFactory.getLogger(SpringSendMailUtilsTest.class);

    /**
     * 所有测试开始之前运行
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    /**
     * 所有测试结束之后运行
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

    /**
     * 每一个测试方法之前运行
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * 每一个测试方法之后运行
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void testSendSimpleEmail() throws IOException {
        boolean flag = false;

        flag = SpringSendMailUtils.sendSimpleEmail(new String[]{"1183409807@qq.com"}, "测试", "内容1", null, null, null, null);
        logger.info(flag + "");

        flag = SpringSendMailUtils.sendSimpleEmail(new String[]{"1183409807@qq.com"}, "测试", "内容2", new String[]{"lzw1000000@163.com"}, null, null, null);
        logger.info(flag + "");

        flag = SpringSendMailUtils.sendSimpleEmail(new String[]{"1183409807@qq.com"}, "测试", "内容3", null, new String[]{"lzw1000000@163.com"}, null, null);
        logger.info(flag + "");

        flag = SpringSendMailUtils.sendSimpleEmail(new String[]{"1183409807@qq.com", "lzw1000000@163.com"}, "测试", "内容4", null, null, "lzw1000000@163.com", null);
        logger.info(flag + "");

        flag = SpringSendMailUtils.sendSimpleEmail(new String[]{"1183409807@qq.com"}, "测试", "内容5", null, null, null, DateTimeUtils.parseDate("2015-02-02 11:11:12"));
        logger.info(flag + "");

        String html = FileUtils.readFileToString(new File("E:\\Source\\HBuilderProject\\My97DatePicker\\index.html"));
        flag = SpringSendMailUtils.sendSimpleEmail(new String[]{"1183409807@qq.com"}, "测试", html, null, null, null, null);
        logger.info(flag + "");
    }

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void testSendMimeMessage() throws IOException {
        boolean flag = false;
        String html = FileUtils.readFileToString(new File("E:\\Source\\HBuilderProject\\My97DatePicker\\index.html"));

        flag = SpringSendMailUtils.sendMimeMessage("1183409807@qq.com", "HTML邮件", html);
        logger.info(flag + "");


        URLDataSource img = new URLDataSource(new URL("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=145605301,663197096&fm=58"));
        Map<String, DataSource> inlineMap = new HashMap<>();
        inlineMap.put("img", img);
        Map<String, DataSource> attachmentMap = new HashMap<>();
        attachmentMap.put("附件.html", new FileDataSource("E:\\Source\\HBuilderProject\\My97DatePicker\\index.html"));
        flag = SpringSendMailUtils.sendMimeMessage("1183409807@qq.com", "HTML邮件", html, inlineMap, attachmentMap);
        logger.info(flag + "");
    }
}
