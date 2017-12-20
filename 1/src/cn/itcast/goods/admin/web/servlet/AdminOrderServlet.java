package cn.itcast.goods.admin.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.service.OrderService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	

	private int getPc(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pc");
		if(param != null && !param.trim().isEmpty()) {
			try {
				pc = Integer.parseInt(param);
			} catch(RuntimeException e) {}
		}
		return pc;
	}
	

	private String getUrl(HttpServletRequest req) {
		String url = req.getRequestURI() + "?" + req.getQueryString();
		/*
		 * 濡傛灉url涓瓨鍦╬c鍙傛暟锛屾埅鍙栨帀锛屽鏋滀笉瀛樺湪閭ｅ氨涓嶇敤鎴彇銆�
		 */
		int index = url.lastIndexOf("&pc=");
		if(index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}

	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int pc = getPc(req);
		
		String url = getUrl(req);
		
		
		PageBean<Order> pb = orderService.findAll(pc);
		
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	

	public String findByStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int pc = getPc(req);
		/*
		 * 2. 寰楀埌url锛�...
		 */
		String url = getUrl(req);
		/*
		 * 3. 鑾峰彇閾炬帴鍙傛暟锛歴tatus
		 */
		int status = Integer.parseInt(req.getParameter("status"));
		/*
		 * 4. 浣跨敤pc鍜宑id璋冪敤service#findByCategory寰楀埌PageBean
		 */
		PageBean<Order> pb = orderService.findByStatus(status, pc);
		/*
		 * 5. 缁橮ageBean璁剧疆url锛屼繚瀛楶ageBean锛岃浆鍙戝埌/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * 鏌ョ湅璁㈠崟璇︾粏淇℃伅
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		Order order = orderService.load(oid);
		req.setAttribute("order", order);
		String btn = req.getParameter("btn");//btn璇存槑浜嗙敤鎴风偣鍑诲摢涓秴閾炬帴鏉ヨ闂湰鏂规硶鐨�
		req.setAttribute("btn", btn);
		return "/adminjsps/admin/order/desc.jsp";
	}
	
	/**
	 * 鍙栨秷璁㈠崟
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String cancel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		/*
		 * 鏍￠獙璁㈠崟鐘舵��
		 */
		int status = orderService.findStatus(oid);
		if(status != 1) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "鐘舵�佷笉瀵癸紝涓嶈兘鍙栨秷锛�");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);//璁剧疆鐘舵�佷负鍙栨秷锛�
		req.setAttribute("code", "success");
		req.setAttribute("msg", "鎮ㄧ殑璁㈠崟宸插彇娑堬紝鎮ㄤ笉鍚庢倲鍚楋紒");
		return "f:/adminjsps/msg.jsp";		
	}
	
	/**
	 * 鍙戣揣鍔熻兘
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deliver(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		/*
		 * 鏍￠獙璁㈠崟鐘舵��
		 */
		int status = orderService.findStatus(oid);
		if(status != 2) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "鐘舵�佷笉瀵癸紝涓嶈兘鍙戣揣锛�");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 3);//璁剧疆鐘舵�佷负鍙栨秷锛�
		req.setAttribute("code", "success");
		req.setAttribute("msg", "鎮ㄧ殑璁㈠崟宸插彂璐э紝璇锋煡鐪嬬墿娴侊紝椹笂纭鍚э紒");
		return "f:/adminjsps/msg.jsp";		
	}
}
