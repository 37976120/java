package com.htstar.ovms.act.listener;

import cn.hutool.core.collection.CollUtil;
import com.htstar.ovms.admin.api.entity.SysUser;
import com.htstar.ovms.admin.api.feign.RemoteUserService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.core.util.SpringContextHolder;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ovms
 * @date 2018/9/27
 * 请假流程监听器
 */
@Slf4j
public class LeaveProcessTaskListener implements TaskListener {

	/**
	 * 查询提交人的上级
	 *
	 * @param delegateTask
	 */
	@Override
	public void notify(DelegateTask delegateTask) {
		SimpMessagingTemplate simpMessagingTemplate = SpringContextHolder.getBean(SimpMessagingTemplate.class);
		RemoteUserService userService = SpringContextHolder.getBean(RemoteUserService.class);

		R<List<SysUser>> result = userService.ancestorUsers(SecurityUtils.getUser().getUsername());
		List<String> remindUserList = new ArrayList<>();

		if (CollUtil.isEmpty(result.getData())) {
			log.info("用户 {} 不存在上级,任务单由当前用户审批", SecurityUtils.getUser().getUsername());
			delegateTask.addCandidateUser(SecurityUtils.getUser().getUsername());
			remindUserList.add(SecurityUtils.getUser().getUsername());
		} else {
			List<String> userList = result.getData().stream().map(SysUser::getUsername).collect(Collectors.toList());
			log.info("当前任务 {}，由 {}处理", delegateTask.getId(), userList);
			delegateTask.addCandidateUsers(userList);
			remindUserList.addAll(userList);
		}

		remindUserList.forEach(user -> {
			String target = String.format("%s-%s", SecurityUtils.getUser().getUsername(), TenantContextHolder.getEtpId());
			simpMessagingTemplate.convertAndSendToUser(target, "/remind", delegateTask.getName());
		});
	}
}
