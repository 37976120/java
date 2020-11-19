package com.htstar.ovms.common.core.util;

import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.i18n.MsgCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author ovms
 */
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "响应信息主体")
public class R<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@ApiModelProperty(value = "返回标记：成功标记=0，失败标记=1")
	private int code;

	@Getter
	@Setter
	@ApiModelProperty(value = "返回信息")
	private String msg;


	@Getter
	@Setter
	@ApiModelProperty(value = "数据")
	private T data;

	public static <T> R<T> ok() {
		return restResult(null, CommonConstants.SUCCESS, "success");
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, CommonConstants.SUCCESS, null);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, CommonConstants.SUCCESS, msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, CommonConstants.FAIL, null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, CommonConstants.FAIL, msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, CommonConstants.FAIL, null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, CommonConstants.FAIL, msg);
	}

	public static <T> R<T> error(int code, T data, String msg) {
		return restResult(data, code, msg);
	}

	/**
	 * 国际化返回码处理
	 * @param msgCode
	 * @param <T>
	 * @return
	 */
	public static <T> R<T> failed(MsgCode msgCode) {
		return restResult(null, CommonConstants.FAIL, msgCode.msg());
	}

	private static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}
    @Override
    public String toString() {
        return "R{" + "code=" + code + ", msg='" + msg + '\''
                + ", data=" + data + '}';
    }

}
