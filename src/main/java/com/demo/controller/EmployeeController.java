package com.demo.controller;

import com.demo.bean.Employee;
import com.demo.bean.Msg;
import com.demo.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 处理员工CRUD请求
 * Created by admin on 2017/7/2.
 */
@Controller
public class EmployeeController {


    @Autowired
    EmployeeService employeeService;

    /**
     * @ResponseBody标间需要导入jackson包
     */
    @RequestMapping("/emps")
    @ResponseBody//自动将返回对象转换为json字符串
    public Msg getEmpWithJson(@RequestParam(value = "pn",defaultValue = "1") Integer pn, Model model) {
        //在查询之前只需要 调用PageHelper.startPage(),传入页码，以及每页显示的条数
        PageHelper.startPage(pn, 5);
        //startPage后面紧跟的查询就是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用PageInfo包装查询后的结果，只需要将pageInfo交个页面就行了
        //封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(emps,5);
        return Msg.success().add("pageInfo",pageInfo);
    }
    /**
     * 查询员工数据（分页查询）
     * @return
     */
    @RequestMapping("/empsOld")//采用jstl方式完成客户端与服务器之间的交互
    public String getEmp(@RequestParam(value = "pn",defaultValue = "1") Integer pn, Model model) {
        //这不是一个分页查询
        //引入PageHelper分页查询
        //在查询之前只需要 调用PageHelper.startPage(),传入页码，以及每页显示的条数
        PageHelper.startPage(pn, 5);
        //startPage后面紧跟的查询就是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用PageInfo包装查询后的结果，只需要将pageInfo交个页面就行了
        //封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(emps,5);

        model.addAttribute("pageInfo", pageInfo);
        return "list";
    }


}
