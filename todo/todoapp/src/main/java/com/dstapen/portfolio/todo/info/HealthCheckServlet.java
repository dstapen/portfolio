// Copyright (c) 2014 by Daria Stepanova.
package com.dstapen.portfolio.todo.info;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Daria Stepanova
 */
public class HealthCheckServlet extends HttpServlet {

    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println("<html><body>it works!</body></html>");
    }
}
