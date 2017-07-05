package com.mlesikov;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author Miroslav Genov (miroslav.genov@clouway.com)
 */
public final class EncodingFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    response.setCharacterEncoding("UTF-8");
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {

  }
}
