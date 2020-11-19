package com.htstar.ovms.act.controller;

import com.htstar.ovms.act.dto.ModelForm;
import com.htstar.ovms.act.service.ModelService;
import com.htstar.ovms.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author ovms
 * @date 2018/9/25
 */
@RestController
@RequestMapping("/model")
@AllArgsConstructor
public class ModelController {
	private final ModelService modelService;

	@PostMapping(value = "/insert")
	public R<Boolean> insertForm(@RequestBody @Valid ModelForm form) {
		modelService.create(form.getName(), form.getKey(), form.getDesc(), form.getCategory());
		return R.ok(Boolean.TRUE);
	}

	@PostMapping
	public R createModel(@RequestParam String name, @RequestParam String key,
						 @RequestParam String desc, @RequestParam String category) {
		return R.ok(modelService.create(name, key, desc, category));
	}

	@GetMapping
	public R getModelPage(@RequestParam Map<String, Object> params) {
		return R.ok(modelService.getModelPage(params));
	}

	@DeleteMapping("/{id}")
	public R removeModelById(@PathVariable("id") String id) {
		return R.ok(modelService.removeModelById(id));

	}

	@PostMapping("/deploy/{id}")
	public R deploy(@PathVariable("id") String id) {
		return modelService.deploy(id) ? R.ok() : R.failed();
	}
}
