import com.yanhua.entity.Video;
import com.yanhua.qcloud.util.PropUtils;
import com.yanhua.service.IVideoService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author zhiwei.yan
 * @Description: TODO
 * @date:2017-08-19 15:45.
 */
//让测试运行与spring测试环境
@RunWith(SpringJUnit4ClassRunner.class)
//spring整合JUnit4测试，使用此注解加载配置文件
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class VideoServiceImplTest {

	private static Logger logger = Logger.getLogger(VideoServiceImplTest.class);

//	private ApplicationContext ac = null;

	@Autowired
	private IVideoService videoService;


	@Test
	public void testGetVideoById() {
		Video video = videoService.getVideoById(31);
		logger.info(video.toString());

	}


	@Test
	public void test1() {
		System.out.print(PropUtils.getConfig("SecretId"));
	}

}
