package com.deer.core.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页实体
 * 
 * @author WangChengBin 2014-12-30
 */
@SuppressWarnings("all")
public class Pager implements Serializable {

	private int startIdx; // 起始索引
	private int limit; // 每页显示数
	private int total; // 总记录数
	private int pageIdx;//当前页码
	private int pages;//总页数
	private List rows = new ArrayList(); // 记录的集合

	/**初始化分页方式1
	 * @param pageIdx
	 *            当前页
	 * @param total
	 *            总记录数
	 * @param limit
	 *            每页显示数
	 */
	public Pager(Integer pageIdx, int total, int limit) {
		
		this.total = total;
		this.pageIdx = (pageIdx!=null&&!pageIdx.equals("")&&!pageIdx.equals("0"))?pageIdx:1;
		this.limit = limit;
		
		startIdx = (pageIdx-1)*this.limit;
		pages = total%this.limit==0?total/this.limit:total/this.limit+1;
	}

	
	public int getStartIdx() {
		return startIdx;
	}

	public void setStartIdx(int startIdx) {
		this.startIdx = startIdx;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageIdx() {
		return pageIdx;
	}

	public void setPageIdx(int pageIdx) {
		this.pageIdx = pageIdx;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

 
}
