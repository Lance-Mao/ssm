import com.demo.bean.Department;
import com.demo.bean.Employee;
import com.demo.dao.DepartmentMapper;
import com.demo.dao.EmployeeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.UUID;

/**
 * 测试DepartmentMapper
 * 推荐Spring的项目就可以使用Spring的单元测试,可以自动注入我们需要的文件
 * 1.导入SpringTest模块
 * 2.@ContextConfiguration指定Spring配置文件的位置
 * 3.直接
 * Created by admin on 2017/7/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ManagerTest {

    @Autowired
    DepartmentMapper departmentMappe;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;
    @Test
    public void test() {
        //1.创建SpringIOC容器
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        //2.从容器中获取mapper
//        DepartmentMapper bean = applicationContext.getBean(DepartmentMapper.class);
        //1.插入部门
//        departmentMappe.insertSelective(new Department(null, "开发部"));
//        departmentMappe.insertSelective(new Department(null, "测试部"));
        //插入员工数据
//        employeeMapper.insertSelective(new Employee(null,"Jerry","M","Jerry@demo.com",1));
        //批量插入多个员工，使用执行可以批量操作的SqlSession
//        for (){
//            employeeMapper.insertSelective(new Employee(null,"Jerry","M","Jerry@demo.com",1));
//        }

        EmployeeMapper mapper = sqlSessionTemplate.getMapper(EmployeeMapper.class);
        for (int i =0;i<1000;i++) {
            String substring = UUID.randomUUID().toString().substring(0, 5)+i;
            employeeMapper.insertSelective(new Employee(null,substring,"M",substring+"@demo.com",1));
        }
        System.out.println("批量完成");
    }

    @Test
    public void test02() {
        List<Employee> employees = employeeMapper.selectByExampleWithDept(null);
        System.out.println(employees);
    }
}
