import com.financingplat.web.util.FtpUtil;
import com.financingplat.web.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 上传工具类
 **/
public class UploadHelper {

	private static final Logger logger = LoggerFactory.getLogger(UploadHelper.class);

	/**
	 * 工具方法：上传图片，成功后返回图片URL
	 * @param imgFile 新图片文件
	 * @param oldImageUrl 原有图片路径
	 * @param filePath 图片保存路径
	 * @return 返回图片保存的位置
	 * @throws IOException
	 */
	public static String uploadImage(MultipartFile imgFile, String oldImageUrl, String filePath) throws IOException {
		logger.info("entering ... UploadHelper --> uploadImage method");
		String imageUrl = oldImageUrl;
		if (imgFile == null || imgFile.isEmpty()) {
			logger.info("imgFile 新图片文件为空不上传");
		} else {
			// 获取文件原生态的名称
			String fileName = imgFile.getOriginalFilename();
			InputStream iis = imgFile.getInputStream();
			BufferedImage bi = ImageIO.read(iis);
			String imageWidth = String.valueOf(bi.getWidth());
			String imageHeight = String.valueOf(bi.getHeight());
			String fileExtension = fileName.substring(fileName.indexOf('.'),
					fileName.length()).toLowerCase();
			iis.close();
			fileName = StringHelper.generateRandomStr() + "_" + imageWidth
					+ "*" + imageHeight + fileExtension;
			String imgStorePath = filePath + "/" + fileName;

			Boolean isUploadSuccess = false;
			// 开始写图片文件, fileName中包含了后缀
			BufferedInputStream bis = new BufferedInputStream(imgFile.getInputStream());
			Boolean isUpload = false;
			try {
				isUpload = FtpUtil.uploadFile(filePath, fileName, bis);
			} catch (Exception e) {
				isUpload = false;
				e.printStackTrace();
			}
			// 如果之前有图片，则覆盖
			if (isUpload == true) {
				if (oldImageUrl != null && !"".equals(oldImageUrl)) {
					FtpUtil.deleteFile(oldImageUrl);
				}
				isUploadSuccess = true;
			} else {
				isUploadSuccess = false;
			}
			bis.close();
			if (isUploadSuccess) {
				imageUrl = imgStorePath;
			}
		}
		logger.info("leaving ... UploadHelper --> uploadImage method");
		return imageUrl;
	}
	
	/**
	 * 工具方法：上传图片，成功后返回图片URL
	 * @param imgFile		图片文件
	 * @param oldImageUrl	原文件地址
	 * @param filePath		文件存放位置
	 * @param CimageWidth	规定的图片宽度
	 * @param CimageHeight 规定的图片高度
	 * @param CimgSize		图片大小（单位M）
	 * @return	map			图片存储成功返回图片地址imageUrl；图片不合规返回 code /code=2 图片尺寸不合格 code=3 图片太大 code=4 图片为空
	 * @throws IOException
	 */
	public static Map<String, Object> uploadImageCheckSizeAndBig(MultipartFile imgFile, String oldImageUrl,
                                                                 String filePath, Integer CimageWidth, Integer CimageHeight, Integer CimgSize
													) throws IOException {
		logger.info("entering ... UploadHelper --> uploadImage method");
		Map<String, Object> resultMap = new HashMap<>();
		String imageUrl = oldImageUrl;
		if (imgFile == null || imgFile.isEmpty()) {
			logger.info("imgFile 新图片文件为空不上传");
			resultMap.put("code", 4);
			return resultMap;
		} else {
			// 获取文件原生态的名称
			String fileName = imgFile.getOriginalFilename();
			InputStream iis = imgFile.getInputStream();
			BufferedImage bi = ImageIO.read(iis);
			long size = imgFile.getSize();
			int width = bi.getWidth();
			int height = bi.getHeight();
			int oldSize = (int) (size / 1024);

			if(!(CimageWidth == bi.getWidth()) || !(CimageHeight == bi.getHeight())) {
				logger.debug("上传图片尺寸超限，上传图片尺寸为：" + width + "*" + height);
				resultMap.put("code", 2);
				return resultMap;
			}
			if (!(CimgSize > oldSize)) {
				logger.debug("上传图超限，上传图片为：" + oldSize + "k" );
				resultMap.put("code", 3);
				return resultMap;
			}

			resultMap.put("code", 1);
			String imageWidth = String.valueOf(width);
			String imageHeight = String.valueOf(height);
			String fileExtension = fileName.substring(fileName.indexOf('.'),
					fileName.length()).toLowerCase();
			iis.close();
			fileName = StringHelper.generateRandomStr() + "_" + imageWidth
					+ "*" + imageHeight + fileExtension;
			String imgStorePath = filePath + "/" + fileName;

			Boolean isUploadSuccess = false;
			// 开始写图片文件, fileName中包含了后缀
			BufferedInputStream bis = new BufferedInputStream(imgFile.getInputStream());
			Boolean isUpload = false;
			try {
				isUpload = FtpUtil.uploadFile(filePath, fileName, bis);
			} catch (Exception e) {
				isUpload = false;
				e.printStackTrace();
			}
			// 如果之前有图片，则覆盖
			if (isUpload != true) {
				isUploadSuccess = false;
			} else {
				if (oldImageUrl != null && !"".equals(oldImageUrl)) {
					FtpUtil.deleteFile(oldImageUrl);
				}
				isUploadSuccess = true;
			}
			bis.close();
			if (isUploadSuccess) {
				resultMap.put("imageUrl", imgStorePath);
			}
		}
		logger.info("leaving ... UploadHelper --> uploadImage method");
		return resultMap;
	}
}
