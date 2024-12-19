package com.multi.contactsapp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

@Component
public class JsonpFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String callback = null;
    String method = null;

    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      callback = httpServletRequest.getParameter("callback");
      method = httpServletRequest.getMethod().toLowerCase();
    }

    if (method.equals("get") && callback != null) {
        OutputStream out = response.getOutputStream();
        out.write(String.format("/**/ %s(", callback).getBytes());
        chain.doFilter(request, response);
        out.write(new JsonpResponseWrapper((HttpServletResponse) response).getData());
        out.write(")".getBytes());
        out.close();
      } else {
        chain.doFilter(request, response);
      }
    }

    private static class JsonpResponseWrapper extends HttpServletResponseWrapper {
      private JsonpResponseWrapper(HttpServletResponse response) {
        super(response);
      }

      private byte[] getData() {
        return new ByteArrayOutputStream().toByteArray();
      }
    }
  }

