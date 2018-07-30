package net.dgg.cloud.controller;

import net.dgg.cloud.utils.ByteUtil;
import net.dgg.cloud.utils.Office2PDF;
import net.dgg.framework.utils.JsonUtils;
import net.dgg.framework.utils.ResourceUtils;
import net.dgg.framework.utils.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.NFastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gene on 2017/9/26. desc:文件controller
 */
@Controller
public class FileController {
	@Autowired
	private NFastDFSUtil fastDFS;

	/**
	 * ****************************************************************
	 * <p>
	 * 
	 * @Description 文件上传
	 *              </p>
	 *              <p>
	 * @Version V1.0.0
	 *          </p>
	 *          <p>
	 *          author rebin
	 *          </p>
	 *          <p>
	 * @Time
	 *       </p>
	 *       <p>
	 *       xiefangjian@163.com
	 *       </p>
	 * 
	 * @param file
	 * @param request
	 * @param model
	 * @return ***************************************************************
	 */
	@ResponseBody
	@RequestMapping(value = "/file/upload", method = RequestMethod.POST)
	public Map<String, Object> upload(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request, ModelMap model, @RequestHeader HttpHeaders headers,String uploadType) {
		if (null == file) {
			return JsonUtils.getResponseForMap(-2, "非法上传", null);
		}
		String fileName = file.getOriginalFilename();// �ļ����

		String sname = null ;
		if(StringUtils.isNullOrEmpty(fileName.trim())){
			return JsonUtils.getResponseForMap(-2,"文件名称不能为空！",null);
		}
		if (!fileName.contains(".")) {
			sname = fileName;
		}else {
			sname = fileName.substring(fileName.lastIndexOf("."));// 文件后缀“.txt”
		}

		if (!sname.equalsIgnoreCase(".mp4")){
			if (file.getSize()>10485760){
				return JsonUtils.getResponseForMap(-2,"上传失败，附件大小不能超过10M",null);
			}
		}
		// 附件文件类型
		String fileTypes = ".jpg-.jpeg-.gif-.png-.bmp-.doc-.docx-.xls-.xlsx-.vsd-.wps-.pdf-.txt-.xml-.json.ppt";//取消一般的附件上传
		if (!StringUtils.isNullOrEmpty(uploadType)){
			fileTypes=".jpg-.jpeg-.png";
		}
/*		if (!fileName.contains(".")) {
			return JsonUtils.getResponseForMap(-2, "附件 " + fileName + " 不是文件类型", null);
		}*/
		//if (!StringUtils.isNullOrEmpty(uploadType)) {//uploadType没有传过来为null,文件上传类型后台判断
		/*	if (!fileTypes.contains(sname.toLowerCase())) {
				return JsonUtils.getResponseForMap(-2, "文件类型 " + sname + " 不支持", null);
			}*/
		//}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", 0);
		map.put("msg", "请求成功");
		if (!file.isEmpty()) {
			try {
				headers.add("Access-Control-Allow-Origin", "*");
				String file_ext_name = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
				String st = fastDFS.upload(file.getBytes(), fileName, file_ext_name);
//				String st = "12323/3123.txt";
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String fastdfsHost = ResourceUtils.getResource("constants").getMap().get("fasdfs.host");
				String port= ResourceUtils.getResource("constants").getMap().get("fastdfs.tracker_http_port");
				String host=fastdfsHost+":"+port+"/";
				map.put("data", st);
				map.put("fileName", fileName);
				map.put("host",host);
				return map;
			} catch (Exception e) {
				e.printStackTrace();
				map.put("code", -2);
				map.put("msg", "请求失败");
				map.put("data", "");
			}
		}else {
			map.put("code",-2);
			map.put("msg","上传失败，附件  "+fileName+" 内容为空");
		}
		return map;
	}

	/**
	 * 文件下载
	 * 
	 * @param
	 * @param
	 * @return
	 * @throws IOException
	 * @throws MyException
	 */
	@RequestMapping(value = "/file/download")
	public byte[] download(String fileId, String fileName, HttpServletResponse response)
			throws IOException, MyException {
		OutputStream output = null;
		try {
			byte[] dfile = fastDFS.getFile(fileId);
//			response.setContentType("application/x-download");// 设置为下载application/x-download
			response.setContentType("application/multipart/form-data");
			String filenamedisplay = fileName;
			filenamedisplay = URLEncoder.encode(filenamedisplay, "UTF-8");
//			response.addHeader("Content-Disposition", "attachment;filename=" + filenamedisplay); // attachment表示网页会出现保存、打开对话框
			response.addHeader("Content-Disposition",  "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
			output = response.getOutputStream();
			output.write(dfile);
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				output.close();
				output = null;
			}
		}
		return null;
	}
	
	/**
	 * 预览
	 * 
	 * @param
	 * @param
	 * @return
	 * @throws IOException
	 * @throws MyException
	 */
	@RequestMapping(value = "/file/readonline")
	public byte[] readonline(String fileId, String fileName, HttpServletResponse response)
			throws IOException, MyException {
		OutputStream output = null;
		try {
			String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            byte[] dfile = fastDFS.getFile(fileId);
            if(fileType.equals(".pdf")){
				response.setContentType("application/pdf");
				String filenamedisplay = fileName;
				filenamedisplay = URLEncoder.encode(filenamedisplay, "UTF-8");
				response.addHeader("application/pdf", "attachment;filename=" + filenamedisplay); // attachment表示网页会出现保存、打开对话框
				output = response.getOutputStream();
				output.write(dfile);
				output.flush();
			}/*else if(fileType.equals(".xls")||fileType.equals(".xlsx")){  弹窗方式
				File f = null;
				f=File.createTempFile("tmp", ".xlsx");
				ByteUtil.byte2File(dfile, f.getPath());//创建临时文件
				byte[] pdf = Office2PDF.converterFileNew2(f,fileType);
				String filenamedisplay = fileName;
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");
				// 2.设置文件头：最后一个参数是设置下载文件名
				response.setHeader("Content-Disposition", "attachment;fileName=" + filenamedisplay);
				output = response.getOutputStream();
				output.write(pdf);
				output.flush();
			}*/else{
				File f = null;
				f=File.createTempFile("tmp", fileType);
				ByteUtil.byte2File(dfile, f.getPath());//创建临时文件
				byte[] pdf = Office2PDF.converterFileNew(f);
				response.setContentType("application/pdf");
				String filenamedisplay = fileName;
				filenamedisplay = URLEncoder.encode(filenamedisplay, "UTF-8");
				response.addHeader("application/pdf", "attachment;filename=" + filenamedisplay); // attachment表示网页会出现保存、打开对话框
				output = response.getOutputStream();
				output.write(pdf);
				output.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				output.close();
				output = null;
			}
		}
		return null;
	}
	/**
	 * excel --- Html  新窗口
	 *
	 * @param
	 * @param
	 * @return
	 * @throws IOException
	 * @throws MyException
	 */
	@RequestMapping(value = "/file/excelHtml")
	@ResponseBody
	public Object excelHtml(String fileId, String fileName)
			throws IOException, MyException {
		Map<String, Object> map = new HashMap();
		try {
			String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
			byte[] dfile = fastDFS.getFile(fileId);
		 	if(fileType.equals(".xls")||fileType.equals(".xlsx")){
				File f = null;
				f=File.createTempFile("tmp", ".xlsx");
				ByteUtil.byte2File(dfile, f.getPath());//创建临时文件
				String excelHtml = Office2PDF.converterFileNew3(f,fileType);
				map.put("code","0");
				map.put("msg","成功");
				map.put("data",excelHtml);
			}
		} catch (Exception e) {
			map.put("code","-1");
			map.put("msg","系统异常");
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * img64上传
	 * 
	 * @param
	 * @param
	 * @return
	 * @throws IOException
	 * @throws MyException
	 */
	@RequestMapping(value = "/file/imgUpload")
	public byte[] imgUpload(@RequestBody String img64,HttpServletResponse response){
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] buf = decoder.decodeBuffer(img64);
//			File f = null;
//		    f=File.createTempFile("tmp", ".png");
//			ByteUtil.byte2File(img64.getBytes(), f.getPath());//创建临时文件
			String fileId = fastDFS.upload(buf, "headTest", "headTest");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}


    @RequestMapping(value = "/file/down", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getFile(String file_id, String filename,@RequestHeader HttpHeaders headers){
        byte[] content = null;
        try {
            content = fastDFS.getFile(file_id);
            headers.setContentDispositionFormData("attachment",  new String(file_id.getBytes("UTF-8"),"iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(content, headers, HttpStatus.CREATED);
    }

	/**
	 * 白皮书文件上传限制为30M，在原上传方法上修改
	 * @param file
	 * @param request
	 * @param model
	 * @param headers
	 * @param uploadType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/whitepaper/file/upload", method = RequestMethod.POST)
	public Map<String, Object> whitepaperUpload(@RequestParam(value = "file", required = false)
															 MultipartFile file,
									  HttpServletRequest request, ModelMap model, @RequestHeader HttpHeaders headers,String uploadType) {
		if (null == file) {
			return JsonUtils.getResponseForMap(-2, "非法上传", null);
		}
		String fileName = file.getOriginalFilename();// �ļ����

		String sname = null ;
		if(StringUtils.isNullOrEmpty(fileName.trim())){
			return JsonUtils.getResponseForMap(-2,"文件名称不能为空！",null);
		}
		if (!fileName.contains(".")) {
			sname = fileName;
		}else {
			sname = fileName.substring(fileName.lastIndexOf("."));// 文件后缀“.txt”
		}

		if (!sname.equalsIgnoreCase(".mp4")){
			if (file.getSize()>31457280){
				return JsonUtils.getResponseForMap(-2,"上传失败，附件大小不能超过30M",null);
			}
		}
		// 附件文件类型
		String fileTypes = ".jpg-.jpeg-.gif-.png-.bmp-.doc-.docx-.xls-.xlsx-.vsd-.wps-.pdf-.txt-.xml-.json.ppt";//取消一般的附件上传
		if (!StringUtils.isNullOrEmpty(uploadType)){
			fileTypes=".jpg-.jpeg-.png";
		}
/*		if (!fileName.contains(".")) {
			return JsonUtils.getResponseForMap(-2, "附件 " + fileName + " 不是文件类型", null);
		}*/
		//if (!StringUtils.isNullOrEmpty(uploadType)) {//uploadType没有传过来为null,文件上传类型后台判断
		/*	if (!fileTypes.contains(sname.toLowerCase())) {
				return JsonUtils.getResponseForMap(-2, "文件类型 " + sname + " 不支持", null);
			}*/
		//}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", 0);
		map.put("msg", "请求成功");
		if (!file.isEmpty()) {
			try {
				headers.add("Access-Control-Allow-Origin", "*");
				String file_ext_name = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
				String st = fastDFS.upload(file.getBytes(), fileName, file_ext_name);
//				String st = "12323/3123.txt";
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String fastdfsHost = ResourceUtils.getResource("constants").getMap().get("fasdfs.host");
				String port= ResourceUtils.getResource("constants").getMap().get("fastdfs.tracker_http_port");
				String host=fastdfsHost+":"+port+"/";
				map.put("data", st);
				map.put("fileName", fileName);
				map.put("host",host);
				return map;
			} catch (Exception e) {
				e.printStackTrace();
				map.put("code", -2);
				map.put("msg", "请求失败");
				map.put("data", "");
			}
		}else {
			map.put("code",-2);
			map.put("msg","上传失败，附件  "+fileName+" 内容为空");
		}
		return map;
	}
}
