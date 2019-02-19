package com.pgt.bikelock.utils;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class Log4jUtil {

    private static String LineSeparator = System.getProperty("line.separator");
    private static String LogSeparator = LineSeparator + "***********************************************";

    private Log4jUtil() {
    }

    private static String extractRequest(HttpServletRequest request) {
        StringBuilder result = new StringBuilder(LineSeparator);

        //extract URL
        result.append("METHOD: " + request.getMethod() + " " + request.getRequestURL().toString() + LineSeparator);

        //extract headers
        result.append("HEADER:" + LineSeparator + "{");
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            result.append("{" + headerName + " : " + request.getHeader(headerName) + "}");
            if (headerNames.hasMoreElements())
                result.append(",");
        }
        result.append("}" + LineSeparator + "QUERY PARAMS:" + LineSeparator + "{");


        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            result.append("{" + paramName + " : " +request.getParameter(paramName) + "}");
            if (params.hasMoreElements())
                result.append(",");
        }
        result.append("}" + LineSeparator);

        return result.toString();
    }

    public static String format(HttpServletRequest request, String message) {
        if (request != null && message != null) {
            return extractRequest(request) + "MESSAGE: " + message + LogSeparator;
        }
        return null;
    }

    public static String format(HttpServletRequest request) {
        if (request != null) {
            return extractRequest(request) + LogSeparator;
        }
        return null;
    }

}
