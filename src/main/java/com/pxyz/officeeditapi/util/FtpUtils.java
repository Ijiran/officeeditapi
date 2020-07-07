package com.pxyz.officeeditapi.util;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Ftp 工具类
 * @author epri-xpjt
 *
 */
public class FtpUtils {

	private final static Log FtpUtilslog=LogFactory.getLog(FtpUtils.class);
	/**
	 * 国际化上传文件Ftp路径
	 */
	public static String InternationUploadPath = "/accessory/internation/";
	/**
	 * 院长信箱传文件Ftp路径
	 */
	public static String DeabmailboxUploadPath = "/accessory/deanmailbox/";
	
	/**
	 * 调查研究项目
	 */
	public static String SurveyUploadPath = "/accessory/survey/";

	/**
	 * 通知公告
	 */
	public static String NoticeUploadPath = "/accessory/notice/";
	
	/**
	 * 评标专家
	 */
	public static String EvaluationUploadPath = "/accessory/evaluation/";
	
	/**
	 * 微信图片上传路径
	 */
	public static String WeChatCoverReverseUploadPath = "/cover/wechat/reverse/";

	/**
	 * 微信图片上传路径
	 */
	public static String WeChatCoverUploadPath = "/cover/wechat/";

	/**
	 * 临时文件上传路径
	 */
	public static String TempUploadPath = "/temp/";
	
	/**
	 * 模板路径
	 */
	public static String templatePath = "/accessory/template/";

	/**
	 * 微信运维反馈路径
	 */
	public static String wechatSupportPath = "/wechatSupport/";

	/**
	 * 技术标准/项目申报流程路径
	 */
	public static String projectApprovalPath = "/accessory/techstandard/projectApproval/";

	/**
	 * 总部验收文档
	 */
	public static String checkUploadPath = "/accessory/internation/prjCheck/";

	public static FtpHelper getFtpHelper() {
		String ftpServer = "10.85.60.103";
		int ftpPort = 21;
		String ftpUsername = "tygl";
		String ftpPassword ="kpAc6Mzk1M";
		FtpHelper ftp = new FtpHelper(ftpServer, ftpPort, ftpUsername,
				ftpPassword);
		return ftp;
	}

	/**
	 * 上传文件到Ftp
	 * 
	 * @param file
	 * @param path
	 */
	public static void uploadFile(File file, String path) {
		if (null == file) {
			FtpUtilslog.error("文件不能为空！");
			return;
		}
		if (Rtext.isEmpty(path)) {
			FtpUtilslog.error("上传路径不能为空！");
			return;
		}
		FtpHelper ftp = getFtpHelper();
		if (!path.contains(".")) {
			path += file.getName();
		}
		try {
			ftp.uplodeFile(file, path);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftp.disconnect();
		}

	}

	/**
	 * 上传文件到Ftp
	 * 
	 * @param in
	 * @param path
	 */
	public static void uploadFile(InputStream in, String path) {
		if (null == in) {
			FtpUtilslog.error("文件不能为空！");
			return;
		}
		if (Rtext.isEmpty(path)) {
			FtpUtilslog.error("上传路径不能为空！");
			return;
		}
		FtpHelper ftp = getFtpHelper();
		try {
			ftp.uplodeFile(in, path);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftp.disconnect();
		}

	}

	/**
	 * 刪除Ftp文件
	 */
	public static void deleteFile(String path) {
		if (Rtext.isEmpty(path)) {
			FtpUtilslog.error("上传路径不能为空！");
			return;
		}

		FtpHelper ftp = getFtpHelper();
		try {
			ftp.deleteFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftp.disconnect();
		}

	}

	/**
	 * 读取文件
	 * 
	 * @param path
	 * @return
	 */
	public static InputStream readFile(String path) {
		if (Rtext.isEmpty(path)) {
			FtpUtilslog.error("上传路径不能为空！");
			return null;
		}

		FtpHelper ftp = getFtpHelper();
		InputStream in = null;
		try {
			in = ftp.downloadFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftp.disconnect();
		}
		return in;
	}

	public static String getFileName(String path) {
		String name = null;
		if (!Rtext.isEmpty(path)) {
			int n = path.lastIndexOf("/");
			if (n > 0) {
				name = path.substring(n + 1);
			}
		}
		return name;
	}

	/**
	 * 得到ftp模块路径
	 * @param module
	 * @return
	 */
	public static String getFtpModulePath(String module) {
		if ("internation".equalsIgnoreCase(module)) {
			return FtpUtils.InternationUploadPath;
		} else if ("deanmailbox".equalsIgnoreCase(module)) {
			return FtpUtils.DeabmailboxUploadPath;
		} else if ("notice".equalsIgnoreCase(module)) {
			return FtpUtils.NoticeUploadPath;
		} else if ("wechat".equalsIgnoreCase(module)) {
			return FtpUtils.WeChatCoverUploadPath;
		} else if ("wechat_reverse".equalsIgnoreCase(module)) {
			return FtpUtils.WeChatCoverReverseUploadPath;
		} else if("evaluation".equalsIgnoreCase(module)){
			return FtpUtils.EvaluationUploadPath;
		} else if("survey".equalsIgnoreCase(module)){
			return FtpUtils.SurveyUploadPath;
		}
		else {
			return FtpUtils.TempUploadPath;
		}

	}

}
