package com.financingplat.web.util;

import com.financingplat.web.config.ConfigurationData;
import com.financingplat.web.entity.Attach;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zjc
 * @Description FtpUtils
 * @Date 13:18 2018/8/22
 **/
public class FtpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FtpUtil.class);
	
	private static FTPClient ftpClient = new FTPClient();
	private static String encoding = System.getProperty("file.encoding");

	private static String url = ConfigurationData.FILE_FTP_SERVER_IP;
	private static int port = ConfigurationData.FILE_FTP_SERVER_PORT;
	private static String username = ConfigurationData.FTP_USERNAME;
	private static String password = ConfigurationData.FTP_PASSWORD;
	private static String basePath = ConfigurationData.FTP_FILE_DEFAULT_DIR;


	/**
	 * @Author zjc
	 * @Description //TODO 
	 * @Date 13:16 2018/8/22
	 * @return org.apache.commons.net.ftp.FTPClient
	 **/
	public static FTPClient connFtp() throws Exception {
		LOGGER.info("entering... FtpUtil-->connFtp ... ");
		LOGGER.info("basePath = {}" , basePath);

		try {
			int reply;
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			// 连接FTP服务器
			ftpClient.connect(url, port);
			// 登录
			ftpClient.login(username, password);
			ftpClient.setControlEncoding(encoding);
			// 检验是否连接成功
			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				LOGGER.info("FTP连接失败！");
				ftpClient.disconnect();
			} else{
				LOGGER.info("FTP连接成功！");
			}
		} catch(Exception e){
			LOGGER.error("链接服务器报错！url={},port={},username={},password={},encoding={}",url,port,username,password,encoding,e);
			e.printStackTrace();
		}
		return ftpClient;
	}

	/**
	 * @Author zjc
	 * @Description uploadFile
	 * @Date 13:21 2018/8/22
	 * @Param [path, filename, bis]
	 * @return boolean
	 **/
	public static boolean uploadFile(String path, String filename,
			BufferedInputStream bis) throws IOException {
		LOGGER.info("entering... FtpUtil-->uploadFile ... 1 2 3");
		LOGGER.info("basePath = " + basePath);

		boolean result = false;
		try {
			int reply;
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			// 连接FTP服务器
			ftpClient.connect(url, port);
			// 登录
			ftpClient.login(username, password);
			ftpClient.setControlEncoding(encoding);
			// 检验是否连接成功
			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				LOGGER.info("连接失败！");
				ftpClient.disconnect();
				return result;
			} else{
				LOGGER.info("1 连接成功！");
			}
			String realPath = basePath + path;
			// 转移工作目录至指定目录下
			boolean change = ftpClient.changeWorkingDirectory(realPath);
			LOGGER.info("2 切换目录是否成功: " + (change? "成功": "失败") + ", 文件名:"+ "/========" +realPath +"========"+ filename);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setControlEncoding("UTF-8");
			try{
				if (change) {
					result = ftpClient.storeFile(filename, bis);
					if (result) {
						LOGGER.info("3 上传成功!");
					} else {
						LOGGER.info("上传失败!");
					}
				}
			} catch(Exception e){
				e.getStackTrace();
			}
			bis.close();
			ftpClient.logout();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		LOGGER.info("leaving... FtpUtil-->uploadFile ...");
		return result;
	}
	/**
	 * @Author zjc
	 * @Description uploadImgs
	 * @Date 13:36 2018/8/22
	 * @Param [path, files]
	 * @return java.lang.String
	 **/
	public static String uploadImgs(String path, List<MultipartFile> files) {
		StringBuffer sb = new StringBuffer();
		if(files.size() > 0) {
			try {
				int reply;
				// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
				ftpClient.connect(url, port);
				ftpClient.login(username, password);
				ftpClient.setControlEncoding(encoding);
				// 检验是否连接成功
				reply = ftpClient.getReplyCode();
				if (!FTPReply.isPositiveCompletion(reply)) {
					LOGGER.info("连接失败！");
					ftpClient.disconnect();
				} else{
					LOGGER.info("1 连接成功！");
					String realPath = basePath + path;
					// 转移工作目录至指定目录下
					@SuppressWarnings("unused")
					boolean change = ftpClient.changeWorkingDirectory(realPath);
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					ftpClient.enterLocalPassiveMode();
					ftpClient.setControlEncoding("UTF-8");
					String fileName = "";
					InputStream iis = null;
					BufferedImage bi = null;
					String imageWidth = "";
					String imageHeight = "";
					for(MultipartFile file : files) {
						if(file !=null && !file.isEmpty()) {
							fileName = file.getOriginalFilename();
							iis = file.getInputStream();
							bi = ImageIO.read(iis);
							imageWidth = String.valueOf(bi.getWidth());
							imageHeight = String.valueOf(bi.getHeight());
							iis.close();
							fileName = StringHelper.generateRandomStr() + "_" + imageWidth
									+ "_" + imageHeight + fileName.substring(fileName.lastIndexOf("."));
							if(ftpClient.storeFile(fileName, file.getInputStream())) {
								sb.append(fileName + ",");
							}
						}
					}
					ftpClient.logout();
					if(sb.length() > 0) {
						//删除最后一个,号
						sb.deleteCharAt(sb.length()-1);
					}
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (ftpClient.isConnected()) {
					try {
						ftpClient.disconnect();
					} catch (IOException ioe) {
					}
				}
			}
			
		}
		return sb.toString();
	}
	/**
	 * @Author zjc
	 * @Description uploadFiles
	 * @Date 13:39 2018/8/22
	 * @Param [path, files]
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 **/
	public static Map<String,Object> uploadFiles(String path, List<MultipartFile> files) {
		StringBuffer fileNames = new StringBuffer();
		StringBuffer realNames = new StringBuffer();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(files.size() > 0) {
			try {
				int reply;
				// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
				ftpClient.connect(url, port);
				ftpClient.login(username, password);
				ftpClient.setControlEncoding(encoding);
				// 检验是否连接成功
				reply = ftpClient.getReplyCode();
				if (!FTPReply.isPositiveCompletion(reply)) {
					LOGGER.info("连接失败！");
					ftpClient.disconnect();
				} else{
					LOGGER.info("1 连接成功！");
					
					String realPath = basePath + path;
					// 转移工作目录至指定目录下
					@SuppressWarnings("unused")
					boolean change = ftpClient.changeWorkingDirectory(realPath);
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					ftpClient.enterLocalPassiveMode();
					ftpClient.setControlEncoding("UTF-8");
					String fileName = "";
					for(MultipartFile file : files) {
						if(file !=null && !file.isEmpty()) {
							fileName = file.getOriginalFilename();
							fileName = StringHelper.generateRandomStr() + fileName.substring(fileName.lastIndexOf("."));
							if(ftpClient.storeFile(fileName, file.getInputStream())) {
								fileNames.append(fileName + ",");
								realNames.append(file.getOriginalFilename() + ",");
							}
						}
					}
					ftpClient.logout();
					if(fileNames.length() > 0) {
						//删除最后一个,号
						fileNames.deleteCharAt(fileNames.length()-1);
						//删除最后一个,号
						realNames.deleteCharAt(realNames.length()-1);
					}
				}
			//处理状态：code:1、成功；2、失败
			resultMap.put("code", 1);
			resultMap.put("fileNames", fileNames.toString());
			resultMap.put("realNames", realNames.toString());
			} catch (SocketException e) {
				e.printStackTrace();
				resultMap.put("code", 2);
			} catch (IOException e) {
				e.printStackTrace();
				resultMap.put("code", 2);
			} finally {
				if (ftpClient.isConnected()) {
					try {
						ftpClient.disconnect();
					} catch (IOException ioe) {
						resultMap.put("code", 2);
					}
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * @Author zjc
	 * @Description 将文件上传正式文件夹
	 * @Date 13:41 2018/8/22
	 * @Param [path, files, attachs, infoId, infoType, customerId, attachType]
	 * @return java.lang.String
	 **/
	public static String uploadFiles(String path, List<MultipartFile> files, List<Attach> attachs,
			Integer infoId, Integer infoType, Integer customerId, Integer attachType) {
		StringBuffer sb = new StringBuffer();
		if(files.size() > 0) {
			try {
				int reply;
				// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
				ftpClient.connect(url, port);
				ftpClient.login(username, password);
				ftpClient.setControlEncoding(encoding);
				// 检验是否连接成功
				reply = ftpClient.getReplyCode();
				if (!FTPReply.isPositiveCompletion(reply)) {
					LOGGER.info("连接失败！");
					ftpClient.disconnect();
				} else{
					LOGGER.info("1 连接成功！");
					String realPath = basePath + path;
					// 转移工作目录至指定目录下
					@SuppressWarnings("unused")
					boolean change = ftpClient.changeWorkingDirectory(realPath);
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					ftpClient.enterLocalPassiveMode();
					ftpClient.setControlEncoding("UTF-8");
					String fileName = "";
					Attach attach = null;
					for(MultipartFile file : files) {
						if(file !=null && !file.isEmpty()) {
							fileName = file.getOriginalFilename();
							fileName = StringHelper.generateRandomStr() + fileName.substring(fileName.lastIndexOf("."));
							if(ftpClient.storeFile(fileName, file.getInputStream())) {
								sb.append(fileName + ",");
							}
							attach = new Attach(path, fileName, file.getOriginalFilename(), infoId, infoType, customerId, attachType);
							attachs.add(attach);
						}
					}
					
					ftpClient.logout();
					if(sb.length() > 0) {
						//删除最后一个,号
						sb.deleteCharAt(sb.length()-1);
					}
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (ftpClient.isConnected()) {
					try {
						ftpClient.disconnect();
					} catch (IOException ioe) {
					}
				}
			}
			
		}
		return sb.toString();
	}
	

	/**
	 * @Author zjc
	 * @Description 工具方法：上传图片，成功后返回图片URL
	 * @Date 13:41 2018/8/22
	 * @Param [imgUrl(新图片), oldImageUrl, filePath]
	 * @return java.lang.String
	 **/
	public static String uploadListImage(MultipartFile imgUrl,
			String oldImageUrl, String filePath) throws Exception {
		LOGGER.info("entering ... CommonUtilController-->uploadListImage");
		String imageUrl = "";
		if (imgUrl == null || imgUrl.isEmpty()) {
			System.out.println("imgUrl为空，文件未上传");
		} else {
			// 获取文件原生态的名称
			String fileName = imgUrl.getOriginalFilename();
			InputStream iis = imgUrl.getInputStream();
			BufferedImage bi = ImageIO.read(iis);
			String imageWidth = String.valueOf(bi.getWidth());
			String imageHeight = String.valueOf(bi.getHeight());
			String fileExtension = fileName.substring(fileName.indexOf('.'),
					fileName.length()).toLowerCase();
			iis.close();
			fileName = StringHelper.generateRandomStr() + "_" + imageWidth
					+ "_" + imageHeight + fileExtension;
			String imgStorePath = filePath + "/" + fileName;

			Boolean isUploadSuccess = false;
			// 开始写图片文件, fileName中包含了后缀
			BufferedInputStream bis = new BufferedInputStream(
					imgUrl.getInputStream());
			Boolean isUpload = false;
			try {
				isUpload = FtpUtil2.uploadFile(filePath, fileName, bis);
			} catch (Exception e) {
				isUpload = false;
				e.printStackTrace();
			}
			// 如果之前有图片，则覆盖
			if (isUpload == true) {
				if (oldImageUrl != null && !"".equals(oldImageUrl)) {
					FtpUtil2.deleteFile(oldImageUrl);
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
		LOGGER.info("leaving ... FtpUtil-->uploadListImage");
		return imageUrl;
	}
	

	/**
	 * @Author zjc
	 * @Description 移动文件
	 * @Date 13:42 2018/8/22
	 * @Param [filePath, fileName, targetPath]
	 * @return java.lang.String
	 **/
	public static String moveFile(String filePath,String fileName, String targetPath){
			try {
				int reply;
				ftpClient.setControlEncoding(encoding);
				/*
				 * 为了上传和下载中文文件，有些地方建议使用以下两句代替 new
				 * String(remotePath.getBytes(encoding),"iso-8859-1")转码。 经过测试，通不过。
				 */
				ftpClient.connect(url, port);
				// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
				// 登录
				ftpClient.login(username, password);
				// 设置文件传输类型为二进制
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				// 获取ftp登录应答代码
				reply = ftpClient.getReplyCode();
				// 验证是否登录成功
				if (!FTPReply.isPositiveCompletion(reply)) {
					ftpClient.disconnect();
					LOGGER.error("FTP server refused connection.");
				}
				// 转移到FTP服务器目录至指定的目录下
				String realPath = basePath + filePath;
				// 转移工作目录至指定目录下
				@SuppressWarnings("unused")
				boolean change = ftpClient.changeWorkingDirectory(realPath);
				// 获取文件列表
				ftpClient.rename(fileName, basePath + targetPath + "/" + fileName);
				ftpClient.logout();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (ftpClient.isConnected()) {
					try {
						ftpClient.disconnect();
					} catch (IOException ioe) {
					}
				}
			}
			return targetPath + "/" + fileName;
	}
	

	/**
	 * @Author zjc
	 * @Description 批量移动文件
	 * @Date 13:44 2018/8/22
	 * @Param [attachs, targetImgPath, targetFilePath]
	 * @return void
	 **/
	public static void moveFileList(List<Attach> attachs, String targetImgPath, String targetFilePath){
		try {
			int reply;
			ftpClient.setControlEncoding(encoding);
			/*
			 * 为了上传和下载中文文件，有些地方建议使用以下两句代替 new
			 * String(remotePath.getBytes(encoding),"iso-8859-1")转码。 经过测试，通不过。
			 */
			ftpClient.connect(url, port);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			// 登录
			ftpClient.login(username, password);
			// 设置文件传输类型为二进制
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 获取ftp登录应答代码
			reply = ftpClient.getReplyCode();
			// 验证是否登录成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				LOGGER.error("FTP server refused connection.");
			}
			//移动文件
			String targetPath = "";
			for(Attach attach : attachs) {
				targetPath = attach.getAttachType() == 1 ? targetImgPath : targetFilePath;
				ftpClient.rename(basePath + attach.getAttachPath() + "/" + attach.getFilename(), basePath + targetPath + "/" + attach.getFilename());
				attach.setAttachPath(targetPath);
			}
			ftpClient.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
	}
	
	/**
	 * @Author zjc
	 * @Description 下載文件到ByteArrayOutputStream
	 * @Date 13:44 2018/8/22
	 * @Param [path, filename, in]
	 * @return boolean
	 **/
	public static boolean downAndDeleteFile(String path, String filename,
			ByteArrayOutputStream in) {
		boolean result = false;
		try {
			int reply;
			ftpClient.setControlEncoding(encoding);
			ftpClient.connect(url, port);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			// 登录
			ftpClient.login(username, password);
			// 设置文件传输类型为二进制
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 获取ftp登录应答代码
			reply = ftpClient.getReplyCode();
			// 验证是否登录成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				System.err.println("FTP server refused connection.");
				return result;
			}
			// 转移到FTP服务器目录至指定的目录下
			String realPath = basePath + path;
			// 转移工作目录至指定目录下
			boolean change = ftpClient.changeWorkingDirectory(realPath);
			if(change) {
				// 获取文件列表
				ftpClient.enterLocalPassiveMode();
				FTPFile[] fs = ftpClient.listFiles();
				for (FTPFile ff : fs) {
					if (ff.getName().equals(filename)) {
						ftpClient.retrieveFile(ff.getName(), in);
						ftpClient.deleteFile(ff.getName());
					}
				}
				result = true;
			}
			ftpClient.logout();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}
	

	/**
	 * @Author zjc
	 * @Description 下載文件到OutputStream
	 * @Date 13:45 2018/8/22
	 * @Param [path, filename, in]
	 * @return boolean
	 **/
	public static boolean downLoadFile(String path, String filename,
			OutputStream in) {
		boolean result = false;
		try {
			int reply;
			ftpClient.setControlEncoding(encoding);
			ftpClient.connect(url, port);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			// 登录
			ftpClient.login(username, password);
			// 设置文件传输类型为二进制
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 获取ftp登录应答代码
			reply = ftpClient.getReplyCode();
			// 验证是否登录成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				System.err.println("FTP server refused connection.");
				return result;
			}
			// 转移到FTP服务器目录至指定的目录下
			String realPath = basePath + path;
			// 转移工作目录至指定目录下
			boolean change = ftpClient.changeWorkingDirectory(realPath);
			if(change) {
				// 获取文件列表
				ftpClient.enterLocalPassiveMode();
				FTPFile[] fs = ftpClient.listFiles();
				for (FTPFile ff : fs) {
					if (ff.getName().equals(filename)) {
						ftpClient.retrieveFile(ff.getName(), in);
					}
				}
				result = true;
			}
			ftpClient.logout();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}
	

	/**
	 * @Author zjc
	 * @Description //TODO 
	 * @Date 13:46 2018/8/22
	 * @Param [picAddr图片存放路径, fileName上传后文件名, bis]
	 * @return boolean
	 **/
	public static boolean uploadFromDisk(String picAddr, String fileName, BufferedInputStream bis) throws Exception {
		boolean flag = false;
		flag = uploadFile(picAddr, fileName, bis);
		return flag;
	}

	/**
	 * @Author zjc
	 * @Description 下載文件到本地
	 * @Date 13:47 2018/8/22
	 * @Param [url, port, username, password, remotePath, fileName, localPath]
	 * @return boolean
	 **/
	public static boolean downFile(String url, int port, String username,
			String password, String remotePath, String fileName,
			String localPath) {
		boolean result = false;
		try {
			int reply;
			ftpClient.setControlEncoding(encoding);
			/*
			 * 为了上传和下载中文文件，有些地方建议使用以下两句代替 new
			 * String(remotePath.getBytes(encoding),"iso-8859-1")转码。 经过测试，通不过。
			 */
			ftpClient.connect(url, port);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			// 登录
			ftpClient.login(username, password);
			// 设置文件传输类型为二进制
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 获取ftp登录应答代码
			reply = ftpClient.getReplyCode();
			// 验证是否登录成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				LOGGER.error("FTP server refused connection.");
				return result;
			}
			// 转移到FTP服务器目录至指定的目录下
			ftpClient.changeWorkingDirectory(new String(remotePath
					.getBytes(encoding), "iso-8859-1"));
			// 获取文件列表
			FTPFile[] fs = ftpClient.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftpClient.retrieveFile(ff.getName(), is);
					is.close();
				}
			}
			ftpClient.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}


	/**
	 * @Author zjc
	 * @Description 刪除文件
	 * @Date 13:49 2018/8/22
	 * @Param [filePath]
	 * @return boolean
	 **/
	public static boolean deleteFile(String filePath){
		boolean flag =  false;
		try {
			ftpClient.connect(url, port);
			//改变工作目录到指定目录
			ftpClient.changeWorkingDirectory(basePath);
			ftpClient.enterLocalPassiveMode();
			// 登录
			ftpClient.login(username, password);
			flag = ftpClient.deleteFile(basePath + filePath);
			LOGGER.info("----删除----：" + basePath+ filePath);
			LOGGER.info(flag+"");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            if (ftpClient != null) {
            	try {
					//断开连接
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				} 
            }
        }
		return flag;
	}


	/**
	 * @Author zjc
	 * @Description 批量删除
	 * @Date 13:50 2018/8/22
	 * @Param [delFiles]
	 * @return boolean
	 **/
    public static boolean delete(String[] delFiles) throws IOException {
    	if(!ftpClient.isConnected()){
    		ftpClient.connect(url, port);
			//改变工作目录到指定目录
    		ftpClient.changeWorkingDirectory(basePath);
    		ftpClient.enterLocalPassiveMode();
    		ftpClient.login(username, password);
    	}
        try {
			for (String s : delFiles) {
				boolean b = ftpClient.deleteFile(basePath + s);
				LOGGER.info(b? "删除成功": "删除失败");
			}
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient != null) {
				//断开连接
            	ftpClient.disconnect();
            }
        }
        return true;
    }
    

    /**
     * @Author zjc
     * @Description 删除无效的文件
     * @Date 13:52 2018/8/22
     * @Param [path, useableFileNames]
     * @return boolean
     **/
	public static boolean deleteUselessFiles(String path, List<String> useableFileNames) {
		boolean result = false;
		if(StringUtils.isNotBlank(path) && useableFileNames != null && useableFileNames.size() > 0){
			try {
				int reply;
				ftpClient.setControlEncoding(encoding);
				ftpClient.connect(url, port);
				// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
				ftpClient.login(username, password);
				// 设置文件传输类型为二进制
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				// 获取ftp登录应答代码
				reply = ftpClient.getReplyCode();
				// 验证是否登录成功
				if (!FTPReply.isPositiveCompletion(reply)) {
					ftpClient.disconnect();
					System.err.println("FTP server refused connection.");
					return result;
				}
				// 转移到FTP服务器目录至指定的目录下
				String realPath = basePath + path;
				// 转移工作目录至指定目录下
				boolean change = ftpClient.changeWorkingDirectory(realPath);
				if(change) {
					// 获取文件列表
					ftpClient.enterLocalPassiveMode();
					FTPFile[] fs = ftpClient.listFiles();
					for (FTPFile ff : fs) {
						if(!useableFileNames.contains(ff.getName())) {
							ftpClient.deleteFile(ff.getName());
						}
					}
					result = true;
				}
				ftpClient.logout();
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (ftpClient.isConnected()) {
					try {
						ftpClient.disconnect();
					} catch (IOException ioe) {
					}
				}
			}
		}
		return result;
	}

}