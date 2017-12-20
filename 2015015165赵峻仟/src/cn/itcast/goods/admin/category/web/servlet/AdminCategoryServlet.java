package cn.itcast.goods.admin.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;

public class AdminCategoryServlet extends BaseServlet {
	private CategoryService categoryService = new CategoryService();
	private BookService bookService = new BookService(); 
	

	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("parents", categoryService.findAll());
		return "f:/adminjsps/admin/category/list.jsp";
	}
	

	public String addParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Category parent = CommonUtils.toBean(req.getParameterMap(), Category.class);
		parent.setCid(CommonUtils.uuid());//璁剧疆cid
		categoryService.add(parent);
		return findAll(req, resp);
	}
	
	public String addChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		Category child = CommonUtils.toBean(req.getParameterMap(), Category.class);
		child.setCid(CommonUtils.uuid());//璁剧疆cid
		
		
		String pid = req.getParameter("pid");
		Category parent = new Category();
		parent.setCid(pid);
		child.setParent(parent);
		
		categoryService.add(child);
		return findAll(req, resp);
	}
	

	public String addChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pid = req.getParameter("pid");//褰撳墠鐐瑰嚮鐨勭埗鍒嗙被id
		List<Category> parents = categoryService.findParents();
		req.setAttribute("pid", pid);
		req.setAttribute("parents", parents);
		
		return "f:/adminjsps/admin/category/add2.jsp";
	}
	

	public String editParentPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String cid = req.getParameter("cid");
		Category parent = categoryService.load(cid);
		req.setAttribute("parent", parent);
		return "f:/adminjsps/admin/category/edit.jsp";
	}
	

	public String editParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Category parent = CommonUtils.toBean(req.getParameterMap(), Category.class);
		categoryService.edit(parent);
		return findAll(req, resp);
	}

	public String editChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String cid = req.getParameter("cid");
		Category child = categoryService.load(cid);
		req.setAttribute("child", child);
		req.setAttribute("parents", categoryService.findParents());
		
		return "f:/adminjsps/admin/category/edit2.jsp";
	}
	

	public String editChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Category child = CommonUtils.toBean(req.getParameterMap(), Category.class);
		String pid = req.getParameter("pid");
		Category parent = new Category();
		parent.setCid(pid);
		child.setParent(parent);
		
		categoryService.edit(child);
		return findAll(req, resp);
	}

	public String deleteParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String cid = req.getParameter("cid");
		int cnt = categoryService.findChildrenCountByParent(cid);
		if(cnt > 0) {
			req.setAttribute("msg", "璇ュ垎绫讳笅杩樻湁瀛愬垎绫伙紝涓嶈兘鍒犻櫎锛�");
			return "f:/adminjsps/msg.jsp";
		} else {
			categoryService.delete(cid);
			return findAll(req, resp);
		}
	}
	

	public String deleteChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String cid = req.getParameter("cid");
		int cnt = bookService.findBookCountByCategory(cid);
		if(cnt > 0) {
			req.setAttribute("msg", "璇ュ垎绫讳笅杩樺瓨鍦ㄥ浘涔︼紝涓嶈兘鍒犻櫎锛�");
			return "f:/adminjsps/msg.jsp";
		} else {
			categoryService.delete(cid);
			return findAll(req, resp);
		}
	}
}
