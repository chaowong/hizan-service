package com.deer.core.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import com.deer.core.utils.trace.Trace;
/**
 * 编码过滤器
 * @author WangChengBin
 *
 */
public class EncodeFilter implements Filter{
	
	private FilterConfig filterConfig;
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
			HttpServletRequest request =(HttpServletRequest) req; //获取带有HTTP协议的request
			HttpServletResponse response = (HttpServletResponse) resp;//获取带有HTTP协议的response
			String coding = "UTF-8";//默认编码utf-8
			String encoding = filterConfig.getInitParameter("encoding");
			if (encoding!=null) {
				coding = encoding;
			}
			
			response.setContentType("text/html;charset="+coding);
			
			//Myrequest myrequest = new Myrequest(request,coding);
			
			chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	/**
	 * 对request的getParameter方法进行了包装
	 * @author Administrator
	 *
	 */
	
	class Myrequest extends HttpServletRequestWrapper{

		private HttpServletRequest request;
		private String encoding;
		
		public Myrequest(HttpServletRequest request,String encoding) {
			super(request);
			
			this.request = request;
			this.encoding = encoding;
		}

		public String getParameter(String name) {
			String value=null;
			String method = request.getMethod();
			if (method.equalsIgnoreCase("post")) {
				try {
					request.setCharacterEncoding(encoding);
					value = request.getParameter(name);
				} catch (UnsupportedEncodingException e) {
					Trace.printStackTrace(e);
				}
			}else {
				System.out.println(request.getRequestURL());
				System.out.println(request.getRequestURI());
				String parameter = request.getParameter(name);
				try {
					value =new String(parameter.getBytes("ISO-8859-1"), encoding) ;
				} catch (UnsupportedEncodingException e) {
					Trace.printStackTrace(e);
				}catch (NullPointerException e) {
					System.out.println("编码过滤器数据为空");
				}
			}
			
			return value;
		}
		
	}

}
