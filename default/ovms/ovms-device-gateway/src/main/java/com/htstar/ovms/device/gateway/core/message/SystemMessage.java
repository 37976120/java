package com.htstar.ovms.device.gateway.core.message;
import lombok.Data;

import java.io.Serializable;

/**
 * Author: flr
 * Company: 航通星空
 * Modified By:
 */
@Data
public class SystemMessage implements Serializable {
    private String remoteAddress;
    private String localAddress;
}
