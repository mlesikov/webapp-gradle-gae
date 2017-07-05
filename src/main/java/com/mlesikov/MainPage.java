package com.mlesikov;

import com.google.common.io.ByteStreams;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Miroslav Genov (miroslav.genov@clouway.com)
 */
public class MainPage extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String indexPage = getServletContext().getRealPath("/WEB-INF/index.tpl.html");

    InputStream inputStream = new FileInputStream(new File(indexPage));
    OutputStream outputStream = resp.getOutputStream();

    resp.setContentType("text/html");
    ByteStreams.copy(inputStream, outputStream);
  }
}
