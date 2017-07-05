package com.mlesikov.security

import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * @author Stanislava Kaukova (stanislava.kaukova@clouway.com).
 */
class SecurityFilter : Filter {

  @Throws(ServletException::class)
  override fun init(filterConfig: FilterConfig) {

  }

  @Throws(IOException::class, ServletException::class)
  override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
    val req = request as HttpServletRequest
    val resp = response as HttpServletResponse

    /*
     * When an instance responds to the /_ah/start request with an HTTP status code of 200–299 or 404, it is considered
     * to have successfully started and can handle additional requests. Otherwise, App Engine terminates the instance.
     * Manual scaling instances are restarted immediately, while basic scaling instances are restarted only when needed
     * for serving traffic.
     */
    if (req.requestURI.startsWith("/_ah/")) {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND)
      return
    }

    // Security checks are discarded when request comes from api-gateway. The complete functionality
    // is going to be removed after api-gateway is working as expected.

    filterChain.doFilter(request, response)
  }

  override fun destroy() {}
}
