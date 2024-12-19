package com.multi.oauth10server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.thinker.oauth.generator.TokenGenerator;

import com.multi.oauth10server.model.ConsumerVO;
import com.multi.oauth10server.model.UsersVO;
import com.multi.oauth10server.service.ConsumerService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AppListController {
	
	@Autowired
	private ConsumerService consumerService;
	
	@GetMapping("/viewAppList")
	public ModelAndView viewAppList(HttpSession session, HttpServletResponse response) throws Exception {
		
		UsersVO usersVO = (UsersVO)session.getAttribute("usersVO");
		if (usersVO == null) 
			response.sendRedirect("login");
		
		List<ConsumerVO> list =  consumerService.selectByUserId(usersVO.getUserid());
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("viewAppList");
		mav.addObject("list", list);
		
		return mav;
	}
	
	@GetMapping("/createApp")
	public String goCreateAppPage() throws Exception {
		
		return "createApp";
	}
	
	@PostMapping("/createApp")
	public void createApp(ConsumerVO oAuthKeyVO, HttpSession session, HttpServletResponse response) throws Exception {
		
		UsersVO usersVO  = (UsersVO)session.getAttribute("usersVO");
		oAuthKeyVO.setUserId(usersVO.getUserid());
		//이제 키를 생성해서 부여해야 함.(ConsumerKey&Secret, AccessToken & Secret)
		TokenGenerator.generateConsumerKey(oAuthKeyVO);
		//그후 데이터베이스에 등록
		consumerService.createConsumer(oAuthKeyVO);		
		
		response.sendRedirect("viewApp?consumerkey=" + oAuthKeyVO.getConsumerKey());
	}
	
	@GetMapping("/viewApp") 
	public ModelAndView viewApp(@RequestParam("consumerkey") String consumerKey) throws Exception {
		ConsumerVO oAuthKeyVO = consumerService.selectByConsumerKey(consumerKey);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("viewApp");
		mav.addObject("oAuthKeyVO", oAuthKeyVO);
		return mav;
	}
	
	@GetMapping("/deleteApp") 
	public void deleteApp(@RequestParam("consumerkey") String consumerKey,
			HttpServletResponse response) throws Exception {
		consumerService.deleteConsumer(consumerKey);
		response.sendRedirect("viewAppList");
	}
}
