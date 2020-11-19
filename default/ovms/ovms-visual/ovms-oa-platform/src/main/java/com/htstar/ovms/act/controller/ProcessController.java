package com.htstar.ovms.act.controller;

import cn.hutool.core.io.IoUtil;
import com.htstar.ovms.act.service.ProcessService;
import com.htstar.ovms.common.core.constant.enums.ResourceTypeEnum;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.annotation.Inner;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.Map;

/**
 * @author ovms
 * @date 2018/9/25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/process")
public class ProcessController {
	private final ProcessService processService;

	@GetMapping
	public R list(@RequestParam Map<String, Object> params) {
		return R.ok(processService.getProcessByPage(params));
	}

	@Inner(value = false)
	@GetMapping(value = "/resource/{proInsId}/{procDefId}/{resType}")
	public ResponseEntity resourceRead(@PathVariable String procDefId, @PathVariable String proInsId, @PathVariable String resType) {

		HttpHeaders headers = new HttpHeaders();

		if (ResourceTypeEnum.XML.getType().equals(resType)) {
			headers.setContentType(MediaType.APPLICATION_XML);
		} else {
			headers.setContentType(MediaType.IMAGE_PNG);
		}

		InputStream resourceAsStream = processService.readResource(procDefId, proInsId, resType);
		return new ResponseEntity(IoUtil.readBytes(resourceAsStream), headers, HttpStatus.CREATED);
	}

	@PutMapping("/status/{procDefId}/{status}")
	public R updateState(@PathVariable String procDefId, @PathVariable String status) {
		return R.ok(processService.updateStatus(status, procDefId));
	}

	@DeleteMapping("/{deploymentId}")
	public R deleteProcIns(@PathVariable String deploymentId) {
		return R.ok(processService.removeProcIns(deploymentId));
	}
}
