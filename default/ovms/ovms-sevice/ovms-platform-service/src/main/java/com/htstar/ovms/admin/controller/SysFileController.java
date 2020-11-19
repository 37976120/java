package com.htstar.ovms.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.entity.SysFile;
import com.htstar.ovms.admin.service.SysFileService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * 文件管理
 *
 * @author Luckly
 * @date 2019-06-18 17:18:42
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys-file")
@Api(value = "sys-file", tags = "文件管理")
public class SysFileController {
	private final SysFileService sysFileService;

	/**
	 * 分页查询
	 *
	 * @param page    分页对象
	 * @param sysFile 文件管理
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "分页查询")
	@GetMapping("/page")
	public R getSysFilePage(Page page, SysFile sysFile) {
		return R.ok(sysFileService.page(page, Wrappers.query(sysFile)));
	}


	/**
	 * 通过id删除文件管理
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除文件管理", notes = "通过id删除文件管理")
	@SysLog("删除文件管理")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_file_del')")
	public R removeById(@PathVariable Long id) {
		return R.ok(sysFileService.deleteFile(id));
	}

	/**
	 * 上传文件
	 * 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异常
	 *
	 * @param file 资源
	 * @return R(/ admin / bucketName / filename)
	 */
	@ApiOperation(value = "单文件上传", notes = "单文件上传")
	@PostMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file) {
		return sysFileService.uploadFile(file);
	}

	/**
	 * Description: 多文件上传
	 * Author: flr
	 * Date: 2020/7/16 12:01
	 * Company: 航通星空
	 * Modified By:
	 */
	@ApiOperation(value = "多文件上传", notes = "多文件上传")
	@PostMapping("/uploads")
	public R uploads(@RequestParam("files")  MultipartFile[] files) {
		return sysFileService.uploadFiles(files);
	}



	/**
	 * 获取文件
	 *
	 * @param bucket   桶名称
	 * @param fileName 文件空间/名称
	 * @param response
	 * @return
	 */
	@Inner(false)
	@GetMapping("/{bucket}/{fileName}")
	public void file(@PathVariable String bucket, @PathVariable String fileName, HttpServletResponse response) {
		sysFileService.getFile(bucket, fileName, response);
	}

}
