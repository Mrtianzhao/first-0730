package net.dgg.cloud.utils;

import java.io.*;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

/**
 * 
 * @author hwl_sz
 * 
 * @desc 需要OpenOffice第三插件的支持 ,支持window\linux\mac等系统
 */
public class Office2PDF {
	public static final String[] OFFICE_POSTFIXS = { "doc", "docx", "xls", "xlsx", "ppt", "pptx","png","txt","vsd","vsdx","pdf"};

	/**
	 * 根据操作系统的名称，获取OpenOffice的安装目录 如我的安装目录：C:/Program Files/OpenOffice 4
	 */
	public static  String getOfficeHome() {
		String osName = System.getProperty("os.name");
		if (Pattern.matches("Linux.*", osName)) {
//			return "/opt/openoffice.org3";
			return "/opt/openoffice4";
		} else if (Pattern.matches("Windows.*", osName)) {
			// return "C:/Program Files/OpenOffice 4";
			return "C:/Program Files (x86)/OpenOffice 4";
		} else if (Pattern.matches("Mac.*", osName)) {
			return "/Application/OpenOffice.org.app/Contents";
		}
		return null;
	}

	/**
	 * 转换文件
	 * 
	 * @param inputFilePath
	 *            转换的office源文件路径
	 * @param outputFilePath
	 *            输出目标文件路径
	 */
	private static void converterFile(String inputFilePath, String outputFilePath) {
		File inputFile = new File(inputFilePath);
		File outputFile = new File(outputFilePath);
		// 假如目标路径不存在,则新建该路径
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}

		DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
		// 获取OpenOffice 的安装目录
		String officeHome = getOfficeHome();
		config.setOfficeHome(officeHome);
		// 启动OpenOffice的服务
		OfficeManager officeManager = config.buildOfficeManager();
		officeManager.start();

		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);

		converter.convert(inputFile, outputFile);
		System.out.println("文件：" + inputFilePath + "\n转换为\n目标文件：" + outputFile + "\n成功！");

		officeManager.stop();
	}

	/**
	 * 转换文件
	 * 
	 * @param inputFile
	 *            转换的office源文件路径
	 *            输出目标文件路径
	 */
	public static byte[] converterFileNew(File inputFile) {
		File htmlFile = null;
		byte[] b = null;
		try {
			htmlFile = File.createTempFile("temp", ".pdf");// 创建临时文件

			DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
			// 获取OpenOffice 的安装目录
			String officeHome = getOfficeHome();
			config.setOfficeHome(officeHome);
			//获取空闲端口
			for(int i=30000;i<65000;i++){
			   int portInt = (int)((Math.random()*9+1)*1000);
				try {
					DatagramSocket ds=new DatagramSocket(portInt);
					config.setPortNumbers(portInt);
					System.out.println("enabled port:"+portInt);
					break;
				} catch (SocketException e) {
					//e.printStackTrace();
					System.out.println("busy port:"+portInt);
				}
			}
			// 设置转换端口，默认为8100
			//config.setPortNumbers(port);
			// 设置任务执行超时为5分钟
			config.setTaskExecutionTimeout(1000 * 60 * 5L);
			// 设置任务队列超时为24小时
			config.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
			// 启动OpenOffice的服务
			OfficeManager officeManager = config.buildOfficeManager();
			officeManager.start();

			OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);

			converter.convert(inputFile, htmlFile);

			b = ByteUtil.File2byte(htmlFile.getPath());

			//删除临时文件

			// System.out.println("文件：" + inputFile.getName() + "\n转换为\n目标文件：" +
			// htmlFile.getCanonicalPath()
			// + "\n成功！");

			officeManager.stop();
			
			inputFile.delete();
			
			htmlFile.delete();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭临时文件
			htmlFile.deleteOnExit();// 程序退出时删除临时文件
		}
		return b;

	}
	/**
	 * excel转html
	 *
	 * @param inputFile
	 *            转换的office源文件路径
	 *            输出目标文件路径
	 */
	public static byte[] converterFileNew2(File inputFile,String fileType) {
		File htmlFile = null;
		byte[] b = null;
		try {

			htmlFile = File.createTempFile("temp", ".html");// 创建临时文件

			DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
			// 获取OpenOffice 的安装目录
			String officeHome = getOfficeHome();
			config.setOfficeHome(officeHome);
			//获取空闲端口
			for(int i=30000;i<65000;i++){
				int portInt = (int)((Math.random()*9+1)*1000);
				try {
					DatagramSocket ds=new DatagramSocket(portInt);
					config.setPortNumbers(portInt);
					System.out.println("enabled port:"+portInt);
					break;
				} catch (SocketException e) {
					//e.printStackTrace();
					System.out.println("busy port:"+portInt);
				}
			}
			// 设置转换端口，默认为8100
			//config.setPortNumbers(port);
			// 设置任务执行超时为5分钟
			config.setTaskExecutionTimeout(1000 * 60 * 5L);
			// 设置任务队列超时为24小时
			config.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
			// 启动OpenOffice的服务
			OfficeManager officeManager = config.buildOfficeManager();
			officeManager.start();

			OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);

			converter.convert(inputFile, htmlFile);
            //设置格式 重新写入
			StringBuffer htmlSb = new StringBuffer();
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(htmlFile), Charset.forName("utf-8")));
				while (br.ready()) {
					htmlSb.append(br.readLine());
				}
				br.close();
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// HTML文件字符串  设置HTML格式
			String htmlStr = htmlSb.toString();
			//System.out.println("htmlStr=" + htmlStr);
			htmlStr = htmlStr.replaceAll("gb2312","utf-8");
			//System.out.println("htmlStr2=" + htmlStr);

			byte[] buff=new byte[]{};
			buff=htmlStr.getBytes();
			FileOutputStream out=new FileOutputStream(htmlFile);
			out.write(buff,0,buff.length);
			//----------
			b = ByteUtil.File2byte(htmlFile.getPath());
			officeManager.stop();

			inputFile.delete();

			htmlFile.delete();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭临时文件
			htmlFile.deleteOnExit();// 程序退出时删除临时文件
		}
		return b;

	}
	/**
	 * excel转html
	 *
	 * @param inputFile
	 *            转换的office源文件路径
	 *            输出目标文件路径
	 */
	public static String converterFileNew3(File inputFile,String fileType) {
		File htmlFile = null;
		String htmlStr = "";
		byte[] b = null;
		try {

			htmlFile = File.createTempFile("temp", ".html");// 创建临时文件

			DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
			// 获取OpenOffice 的安装目录
			String officeHome = getOfficeHome();
			config.setOfficeHome(officeHome);
			//获取空闲端口
			for(int i=30000;i<65000;i++){
				int portInt = (int)((Math.random()*9+1)*1000);
				try {
					DatagramSocket ds=new DatagramSocket(portInt);
					config.setPortNumbers(portInt);
					System.out.println("enabled port:"+portInt);
					break;
				} catch (SocketException e) {
					//e.printStackTrace();
					System.out.println("busy port:"+portInt);
				}
			}
			// 设置转换端口，默认为8100
			//config.setPortNumbers(port);
			// 设置任务执行超时为5分钟
			config.setTaskExecutionTimeout(1000 * 60 * 5L);
			// 设置任务队列超时为24小时
			config.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
			// 启动OpenOffice的服务
			OfficeManager officeManager = config.buildOfficeManager();
			officeManager.start();

			OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);

			converter.convert(inputFile, htmlFile);
			//设置格式 重新写入
			StringBuffer htmlSb = new StringBuffer();
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(htmlFile), Charset.forName("utf-8")));
				while (br.ready()) {
					htmlSb.append(br.readLine());
				}
				br.close();
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// HTML文件字符串  设置HTML格式
			htmlStr = htmlSb.toString();
			//System.out.println("htmlStr=" + htmlStr);
			htmlStr = htmlStr.replaceAll("gb2312","utf-8");
			String tempPath =System.getProperty("java.io.tmpdir")+File.separator;
			System.out.println("------------------"+tempPath);
			//System.out.println("htmlStr2=" + htmlStr);

			byte[] buff=new byte[]{};
			buff=htmlStr.getBytes();
			FileOutputStream out=new FileOutputStream(htmlFile);
			out.write(buff,0,buff.length);
			//----------
			b = ByteUtil.File2byte(htmlFile.getPath());
			officeManager.stop();

			inputFile.delete();

			htmlFile.delete();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭临时文件
			htmlFile.deleteOnExit();// 程序退出时删除临时文件
		}
		return htmlStr;
	}
	/**
	 * 将(.doc|.docx|.xls|.xlsx|.ppt|.pptx)等office文件 转化为pdf文件
	 * 
	 * @param inputFilePath
	 *            待转换的源文件路径
	 * @param outputFilePath
	 *            输出的目录文件路径，如果未指定(null)，则按在源文件当前目录生成同名的pdf文件
	 * @return 处理结果
	 */
	public static boolean openOffice2Pdf(String inputFilePath, String outputFilePath) {
		boolean flag = false;
		File inputFile = new File(inputFilePath);
		ArrayList<String> office_Formats = new ArrayList<String>();
		Collections.addAll(office_Formats, OFFICE_POSTFIXS);
		if ((null != inputFilePath) && (inputFile.exists())) {
			// 判断目标文件路径是否为空
			if (office_Formats.contains(getPostfix(inputFilePath))) {
				if (null == outputFilePath) {
					// 转换后的文件路径
					String outputFilePath_new = inputFilePath.toLowerCase().replaceAll("." + getPostfix(inputFilePath),
							".pdf");
					converterFile(inputFilePath, outputFilePath_new);
					flag = true;
				} else {
					converterFile(inputFilePath, outputFilePath);
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * 获取文件的后缀名
	 */
	private static String getPostfix(String inputFilePath) {
		String[] p = inputFilePath.split("\\.");
		if (p.length > 0) {
			return p[p.length - 1];
		} else {
			return null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("//");
		Office2PDF.openOffice2Pdf("E:/1.png", null);
		Office2PDF.openOffice2Pdf("E:/2.png", null);
	}
}
