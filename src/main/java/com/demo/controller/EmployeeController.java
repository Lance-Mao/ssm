package com.demo.controller;

import com.demo.bean.Employee;
import com.demo.bean.Msg;
import com.demo.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理员工CRUD请求
 * Created by admin on 2017/7/2.
 */
@Controller
public class EmployeeController {


    @Autowired
    EmployeeService employeeService;

    @ResponseBody
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.DELETE)
    public Msg deleteEmpById(@PathVariable("id") Integer id) {
        employeeService.deleteEmpById(id);
        return Msg.success();
    }

    /**
     * 员工更新方法
     * 如果直接发送ajax=PUT形式请求,获取的数据全部为空，PUT形式的请求在tomcat中不会被封装成map
     *
     * 要支持直接发送PUT之类的请求还要封装请求体中的数据
     * 在web.xml中配置上HttpPutFormContentFilter,
     * 作用：将请求体中的数据包装成一个map，
     * request被重新包装，request.getParameter()被重写，就会从自己封装的map中取数据
     * @param employee
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "emp/{empId}",method = RequestMethod.PUT)
    public Msg saveEmp(Employee employee) {
        employeeService.updateEmp(employee);
        return Msg.success();
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id) {
        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp", employee);
    }

    /**
     * 检查用户名是否可用
     *
     * @param empName
     * @return
     */
    @RequestMapping("/checkUser")
    @ResponseBody
    public Msg checkUser(@RequestParam("empName") String empName) {
        boolean b = employeeService.checkUser(empName);
        if (b) {
            return Msg.success();
        } else {
            return Msg.fail();
        }
    }

    /**
     * 员工保存
     * 1.支持JSR303校验
     * 2.导入hibernate-Validator
     *
     * @return
     * @Valid代表数据校验 BindingResult封装校验结果
     */
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result) {
        if (!result.hasErrors()) {
            //校验失败，应该返回失败，在模态框中显示校验失败的信息
            Map<String, String> map = new HashMap<String, String>();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                System.out.println("错误的字段名"+fieldError.getField());
                System.out.println("错误信息" + fieldError.getDefaultMessage());
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return Msg.fail().add("errorFields",map);
        } else {
            employeeService.saveEmp(employee);
            return Msg.success();
        }
    }

    /**
     * @ResponseBody标间需要导入jackson包
     */
    @RequestMapping("/emps")
    @ResponseBody//自动将返回对象转换为json字符串
    public Msg getEmpWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
        //在查询之前只需要 调用PageHelper.startPage(),传入页码，以及每页显示的条数
        PageHelper.startPage(pn, 5);
        //startPage后面紧跟的查询就是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用PageInfo包装查询后的结果，只需要将pageInfo交个页面就行了
        //封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(emps, 5);
        return Msg.success().add("pageInfo", pageInfo);
    }

    /**
     * 查询员工数据（分页查询）
     *
     * @return
     */
    @RequestMapping("/empsOld")//采用jstl方式完成客户端与服务器之间的交互
    public String getEmp(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
        //这不是一个分页查询
        //引入PageHelper分页查询
        //在查询之前只需要 调用PageHelper.startPage(),传入页码，以及每页显示的条数
        PageHelper.startPage(pn, 5);
        //startPage后面紧跟的查询就是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用PageInfo包装查询后的结果，只需要将pageInfo交个页面就行了
        //封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
        PageInfo pageInfo = new PageInfo(emps, 5);

        model.addAttribute("pageInfo", pageInfo);
        return "list";
    }


}
