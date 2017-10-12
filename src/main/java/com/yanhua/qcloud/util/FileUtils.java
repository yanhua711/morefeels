package com.yanhua.qcloud.util;

import com.yanhua.qcloud.module.VideoParam;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class FileUtils {

	private static String fileName = "os.name";

	private static String fileWindows = "WINDOWS";

//	private static final String IMAGEFORMAT = ".jpg"; // 图片后缀

	private static final String FILE = "file";

	private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

	private static final String TYPESTR = "typeStr";

	private static final String SAVEDIRECTORY = "saveDirectory";

	private static final String FILESOURCE = "fileSource";

	private static final String JPG = "jpg";

	private static final int DEFAULT_BUFFER_SIZE = 1024;

	private static final int SUCCESSCODE = 200;

	private static final int EXCEEDSIZE = 501;

	private static final int UNSURPORTEDNAME = 502;

	private static final int PATHERROR = 503;

	/**
	 *
	 * @Description:本地文件上传到文件服务器
	 * @param path
	 *            本地路径
	 * @param relativePath
	 *            服务器上存放的相对路径
	 * @param fileNameUpload
	 *            去掉文件格式的后缀的文件名，比如上传文件11.jpg, 则fileNameUpload=11
	 * @param pictureSize
	 *            图片尺寸(需要压缩时填)
	 * @param fileSource
	 *            公司编码(区分不同公司品牌)
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @author zhiwei.yan
	 * @date 2016年4月16日 下午5:35:32
	 */
	public static String saveFileToServer(String path, String relativePath, String fileNameUpload, String pictureSize,
			String fileSource, String uploadUrl) throws ClientProtocolException, IOException {
		LOG.info("path:" + path + ",relativePath:" + relativePath + ",fileNameUpload:" + fileNameUpload
				+ ",pictureSize:" + pictureSize);
		final File file = new File(path);
		final HttpClient client = HttpClients.createDefault();
		final HttpPost post = new HttpPost(uploadUrl);
		final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		final FileBody fileBody = new FileBody(file);
		builder.addTextBody(SAVEDIRECTORY, relativePath); // 文件保存目录。客户端可以指定文件保存在哪个目录下。如果没有指定，则按照默认规则保存。
		builder.addTextBody(FILESOURCE, fileSource); // 文件来源。该参数传递的是各模块的名称。暂定ISSWX测试
		builder.addPart(FILE, fileBody);
		builder.addTextBody(TYPESTR, pictureSize); // 压缩尺寸，例如:
													// "557*500,720*360,1890*270"。
		//builder.addTextBody("isRename", fileNameUpload);
		// //上传的文件进行重命名参数，若该参数为空，则按照上传的文件名保存文件。若该参数不为空，则按上传的参数名保存文件。需要注意的是，不是上传同名同类型的文件。
		post.setEntity(builder.build());

		final HttpResponse resp = client.execute(post);
		String log = "";
		final int responseCode = resp.getStatusLine().getStatusCode();
		if (responseCode == SUCCESSCODE) {
			log = EntityUtils.toString(resp.getEntity());
			LOG.info(log);
			file.delete();
			final Header[] headers = resp.getAllHeaders();
			String location = null;
			for (Header h : headers) {
				LOG.info(h.getName() + ": " + h.getValue());
				if ("Location".equalsIgnoreCase(h.getName())) {
					location = h.getValue();
					break;
				}
			}
			if (location != null) {
				LOG.info(location);
			}
		} else if (responseCode == EXCEEDSIZE) {
			return "超过上传大小限制";
		} else if (responseCode == UNSURPORTEDNAME) {
			return "不支持的扩展名";
		} else if (responseCode == PATHERROR) {
			return "路径格式错误";
		}
		return log;
	}

	/**
	 *
	 * @Description:获取输入流(微信图片服务器流)
	 * @param url
	 * @return
	 * @author zhiwei.yan
	 * @date 2016年4月16日 下午5:41:14
	 */
	public static InputStream getInputStream(String url) {
		InputStream is = null;
		try {
			final URL urlGet = new URL(url);
			final HttpURLConnection httpUrlConnection = (HttpURLConnection) urlGet.openConnection();
			httpUrlConnection.setRequestMethod("GET");
//			httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在 http正文内，因此需要设为true,
			// 默认情况下是false;
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setDoInput(true);
			// Post 请求不能使用缓存
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.connect();
			// 获取文件转化为byte流
			is = httpUrlConnection.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}

	/**
	 *
	 * @Description:保存到临时路径
	 * @param is
	 * @param fileName
	 * @param fileType
	 *            传入"1"表示需要自动裁剪，否则默认不裁剪
	 * @return
	 * @throws IOException
	 * @author zhiwei.yan
	 * @date 2016年4月16日 下午5:42:28
	 */
	public static String saveTmp(InputStream is, String fileName, String fileType) throws IOException {
		final byte[] data = new byte[DEFAULT_BUFFER_SIZE];
		int len = 0;
		FileOutputStream fileOutputStream = null;
		// 临时存放路径
		final String tmpPath = FileUtils.getBaseFilePath() + fileName + "." + fileType;
		try {
			final File file2 = new File(FileUtils.getBaseFilePath());
			if (!file2.exists()) {
				file2.mkdirs();
			}
			// 定义一个临时存放路径
			fileOutputStream = new FileOutputStream(tmpPath);
			while ((len = is.read(data)) != -1) {
				fileOutputStream.write(data, 0, len);
			}
			// LOG.info("自动居中裁剪后图片：" + newTmpPath);
		} catch (IOException e) {
			LOG.error(e.toString());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return tmpPath;
	}

	/**
	 *
	 * @Description:取得basepath
	 * @return
	 * @author zhiwei.yan
	 * @date 2016年4月16日 下午5:44:44
	 */
	public static String getBaseFilePath() {
		String basePath = null;
		// window，linux下路径不同
		final String os = System.getProperty(fileName).toUpperCase();
		if (os.contains(fileWindows)) {
			basePath = PropUtils.getConfig("WfileBasePath");
		} else {
			basePath = PropUtils.getConfig("LfileBasePath");
		}
		return basePath;
	}

	/**
	 *
	 * @Description:自动居中裁剪
	 * @param localUrl
	 * @return
	 * @author zhiwei.yan
	 * @date 2016年4月30日 下午8:49:53
	 */
	public static void autoCutImage(String localUrl) {
		final File picture = new File(localUrl);
		BufferedImage sourceImg = null;
		FileInputStream is = null;
		ImageInputStream iis = null;
		try {
			sourceImg = ImageIO.read(new FileInputStream(picture));
			// 图片尺寸-宽
			final int width = sourceImg.getWidth();
			// 图片尺寸-高
			final int height = sourceImg.getHeight();
			// 读取图片文件
			is = new FileInputStream(localUrl);
			/**
			 * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader 声称能够解码指定格式。
			 * 参数：formatName - 包含非正式格式名称 . (例如 "jpeg" 或 "tiff")等 。
			 */
			final Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(JPG);
			final ImageReader reader = it.next();
			// 获取图片流
			iis = ImageIO.createImageInputStream(is);
			/**
			 * iis:读取源。true:只向前搜索 将它标记为 ‘只向前搜索’。 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许
			 * reader 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
			 */
			reader.setInput(iis, true);
			/**
			 * 描述如何对流进行解码的类 用于指定如何在输入时从 Java Image I/O
			 * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件 将从其 ImageReader 实现的
			 * getDefaultReadParam 方法中返回 ImageReadParam 的实例。
			 */
			final ImageReadParam param = reader.getDefaultReadParam();
			/**
			 * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
			 * 的左上顶点的坐标(x，y)、宽度和高度可以定义这个区域。
			 */
			Rectangle rect = null;
			final int side = (height - width) / 2; // 顶点坐标，根据宽高长度，分别为x,y坐标
			if (height >= width) { // 高比宽大，以宽为边，side为y轴顶点，否则以高为边
				rect = new Rectangle(0, side, width, width);
			} else {
				rect = new Rectangle(side, height, height, height);
			}
			// 提供一个 BufferedImage，将其用作解码像素数据的目标。
			param.setSourceRegion(rect);
			/**
			 * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将 它作为一个完整的
			 * BufferedImage 返回。
			 */
			final BufferedImage bi = reader.read(0, param);
			// 保存新图片
			ImageIO.write(bi, JPG, new File(localUrl));
			LOG.info("裁剪后的图片：" + localUrl);
		} catch (Exception e) {
			LOG.error(e.toString());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.error(e.toString());
				}
			}
			if (iis != null) {
				try {
					iis.close();
				} catch (IOException e) {
					LOG.error(e.toString());
				}
			}
		}
	}

	/**
	 *
	 * @Description:form表单提交文件保存本地
	 * @param files 多个文件
	 * @param fileParm 文件参数
	 * @return 保存成功图片服务器返回结果
	 * @author zhiwei.yan
	 * @date 2016年10月24日 下午4:35:25
	 */
	public static String saveOfFileupload(MultipartFile[] files, VideoParam fileParm) {
		final String path = getBaseFilePath();
		final StringBuilder sb = new StringBuilder("");
		OutputStream out = null;
		InputStream in = null;
		try {
			//多个文件解析
			int times = 0;
			for (MultipartFile item : files) {
				// 如果获取的 表单信息是普通的 文本 信息
				if (!item.isEmpty()) {
					times++;
					// 获取项目路径名
					final String value = item.getOriginalFilename();
					// 索引到最后一个反斜杠
					final int start = value.lastIndexOf(".");
					// 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
					final String fileSuffix = value.substring(start);
					fileParm.setFileType(fileSuffix.substring(1));
					// 临时文件名
					final String fileName = fileParm.getFileName() + "_" + System.currentTimeMillis() + fileSuffix;
					// 输出流，用于写入本地
					out = new FileOutputStream(new File(path, fileName));
					// 获取form表单的输入流
					in = item.getInputStream();


					int length = 0;
					final byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
					// in.read(buf) 每次读到的数据存放在 buf 数组中
					while ((length = in.read(buf)) != -1) {
						// 在 buf 数组中 取出数据 写到 （输出流）磁盘上
						out.write(buf, 0, length);
					}
					//返回本地路径
					sb.append(path).append(File.separator).append(fileName);
					in.close();
					out.close();
				}
			}
			return sb.toString();
		} catch (Exception e) {
			LOG.error("------form upload file to local error : " + e.toString());
		} finally {
			try {
				if (in != null) {
					in.close();
					try {
						if(out != null) {
							out.close();
						}
					} catch (IOException e) {
						LOG.error("-------" + e.toString());
					}
				}
			} catch (IOException e) {
				LOG.error("-------" + e.toString());
			}

		}
		return "";
	}


	public static void main(String[] args) {
		final String url = "http://1253236957.vod2.myqcloud.com/294b1b8avodgzp1253236957/6a44ff429031868223131705198/f0.mp4";
		try {
			final String localPath = saveTmp(getInputStream(url), "\\testCloud1", "mp4");
			LOG.info("-----localPath:" + localPath);
		} catch (IOException e) {
			LOG.error(e.toString());
		}
	}

	/**
	 * 将文件转成base64 字符串
	 * @param path
	 * @return  *
	 * @throws Exception
	 */
	public static String encodeBase64File(String path) throws Exception {
		File file = new File(path);;
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return new BASE64Encoder().encode(buffer);
	}
}
