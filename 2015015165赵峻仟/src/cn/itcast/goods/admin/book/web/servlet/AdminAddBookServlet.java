package cn.itcast.goods.admin.book.web.servlet;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.category.service.CategoryService;


public class AdminAddBookServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
			
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setFileSizeMax(80 * 1024);
		
		List<FileItem> fileItemList = null;
		try {
			fileItemList = sfu.parseRequest(request);
		} catch (FileUploadException e) {
	
			error("涓婁紶鐨勬枃浠惰秴鍑轰簡80KB", request, response);
			return;
		}
	
		Map<String,Object> map = new HashMap<String,Object>();
		for(FileItem fileItem : fileItemList) {
			if(fileItem.isFormField()) {//濡傛灉鏄櫘閫氳〃鍗曞瓧娈�
				map.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
			}
		}
		Book book = CommonUtils.toBean(map, Book.class);//鎶奙ap涓ぇ閮ㄥ垎鏁版嵁灏佽鍒癇ook瀵硅薄涓�
		Category category = CommonUtils.toBean(map, Category.class);//鎶奙ap涓璫id灏佽鍒癈ategory涓�
		book.setCategory(category);
				FileItem fileItem = fileItemList.get(1);//鑾峰彇澶у浘
		String filename = fileItem.getName();

		int index = filename.lastIndexOf("\\");
		if(index != -1) {
			filename = filename.substring(index + 1);
		}

		filename = CommonUtils.uuid() + "_" + filename;
		if(!filename.toLowerCase().endsWith(".jpg")) {
			error("涓婁紶鐨勫浘鐗囨墿灞曞悕蹇呴』鏄疛PG", request, response);
			return;
	}
	
		String savepath = this.getServletContext().getRealPath("/book_img");
		File destFile = new File(savepath, filename);
		try {
			fileItem.write(destFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		ImageIcon icon = new ImageIcon(destFile.getAbsolutePath());
		Image image = icon.getImage();
		if(image.getWidth(null) > 350 || image.getHeight(null) > 350) {
			error("鎮ㄤ笂浼犵殑鍥剧墖灏哄瓒呭嚭浜�350*350锛�", request, response);
			destFile.delete();
			return;
		}

		book.setImage_w("book_img/" + filename);
		
		


		fileItem = fileItemList.get(2);
		filename = fileItem.getName();

		index = filename.lastIndexOf("\\");
		if(index != -1) {
			filename = filename.substring(index + 1);
		}

		filename = CommonUtils.uuid() + "_" + filename;

		if(!filename.toLowerCase().endsWith(".jpg")) {
			error("涓婁紶鐨勫浘鐗囨墿灞曞悕蹇呴』鏄疛PG", request, response);
			return;
		}

		savepath = this.getServletContext().getRealPath("/book_img");
	
		destFile = new File(savepath, filename);
	
		try {
			fileItem.write(destFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		icon = new ImageIcon(destFile.getAbsolutePath());
		image = icon.getImage();

		if(image.getWidth(null) > 350 || image.getHeight(null) > 350) {
			error("鎮ㄤ笂浼犵殑鍥剧墖灏哄瓒呭嚭浜�350*350锛�", request, response);
			destFile.delete();
			return;
		}

		book.setImage_b("book_img/" + filename);
		
	
		book.setBid(CommonUtils.uuid());
		BookService bookService = new BookService();
		bookService.add(book);

		request.setAttribute("msg", "娣诲姞鍥句功鎴愬姛锛�");
		request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request, response);
	}

	private void error(String msg, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("msg", msg);
		request.setAttribute("parents", new CategoryService().findParents());//鎵�鏈変竴绾у垎绫�
		request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").
				forward(request, response);
	}
}
