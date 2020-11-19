package com.htstar.ovms.common.core.jackson;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/8/20
 * Company: 航通星空
 * Modified By:
 */
@Component
public class OvmsStringHttpMessageConverter extends StringHttpMessageConverter {
    public OvmsStringHttpMessageConverter(){
        super(Charset.forName("UTF-8"));
    }
}
