package action;

import java.io.PrintWriter;

import javax.servlet.http.*;

import svc.RestaurantDeleteService;
import vo.*;

public class RestaurantDeleteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		System.out.println("RestaurantDeleteAction");
		RestaurantDeleteService service = new RestaurantDeleteService();
		String filePath =  request.getServletContext().getRealPath("/upload");
		String resName = request.getParameter("resName");
		String photo = service.bringPhoto(resName);
		boolean isDeleteSuccess = service.deleteRestaurantInfo(resName,filePath, photo);
		if(isDeleteSuccess) {
			forward = new ActionForward();
			forward.setPath("restaurantList.re");
			forward.setRedirect(true);
		}else {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html; charset=Utf-8");
			out.print("<script>alert('식당 삭제 실패');history.back();</script>");
		}
		return forward;
	}

}
