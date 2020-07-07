package com.pxyz.officeeditapi.configuration;

import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author Ijiran
 * @Package com.pxyz.officeeditapi.configuration
 * @date 2020-07-04 19:06
 */
@Component
@ApplicationPath("/wopi/files/")
public class JaxrsApplication extends Application {



}
