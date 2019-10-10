package com.sandeep.demoemployee;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandeep.demoemployee.entity.CrudeEmployee;
import com.sandeep.demoemployee.entity.Employee;
import com.sandeep.demoemployee.entity.NewEmployee;
import javafx.application.Application;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
@EnableWebMvc
@WebAppConfiguration
@ContextConfiguration(classes = {DemoEmployeeApplication.class})
public class PostTests extends AbstractTestNGSpringContextTests
{
    @Autowired
    WebApplicationContext context;
    private MockMvc mvc;
    @BeforeMethod
    public void setUp()
    {
        mvc= MockMvcBuilders.webAppContextSetup(context).build();
    }


    /*************** test cases for post requests ****************/

    @Test(priority = 0)
    public void createEmployeeTest() throws Exception                //post is working or not
    {
        CrudeEmployee employeePost=new CrudeEmployee(2,"wonder woman","intern");
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(employeePost);
        MvcResult result=mvc.perform(MockMvcRequestBuilders.post("/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String resultOutput=result.getResponse().getContentAsString();
        Assert.assertEquals("Employee added successfully",resultOutput);
    }

    @Test(priority = 1)
    public void directorValidationForManager() throws Exception                //Assigning director with manager
    {
        CrudeEmployee employeePost=new CrudeEmployee(2,"wonder woman","Director");
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(employeePost);
        mvc.perform(MockMvcRequestBuilders.post("/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void multipleDirector() throws Exception                             //Adding second Director
    {
        CrudeEmployee employeePost=new CrudeEmployee(null,"wonder woman","Director");
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(employeePost);
        mvc.perform(MockMvcRequestBuilders.post("/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();
    }

    @Test
    public void noData () throws Exception                //Adding employee with no data
    {
        CrudeEmployee employeePost=new CrudeEmployee();
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(employeePost);
        mvc.perform(MockMvcRequestBuilders.post("/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void partialData () throws Exception                //Adding employee with partial data
    {
        CrudeEmployee employeePost=new CrudeEmployee(2,"wonder woman");
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(employeePost);
        mvc.perform(MockMvcRequestBuilders.post("/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void invalidParentId () throws Exception                //Adding employee with non existing manager
    {
        CrudeEmployee employeePost=new CrudeEmployee(12,"wonder woman", "Lead");
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(employeePost);
        mvc.perform(MockMvcRequestBuilders.post("/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }
    @Test
    public void hierarchyViolation () throws Exception                //Adding employee with violating organisation hierarchy
    {
        CrudeEmployee employee=new CrudeEmployee(8,"wonder woman", "Lead");
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(employee);
        mvc.perform(MockMvcRequestBuilders.post("/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        employee.setDesignation("manager");
        mvc.perform(MockMvcRequestBuilders.post("/employees").content(mapper.writeValueAsString(employee)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void invalidDesignation () throws Exception                //Adding employee with non existing Designation
    {
        CrudeEmployee employeePost=new CrudeEmployee(12,"wonder woman", "Laead");
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(employeePost);
        mvc.perform(MockMvcRequestBuilders.post("/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    /*********************** test case for delete employee ********************/

    @Test
    public void delInvalidId () throws Exception                //Deleting non existing employee
    {
        mvc.perform(MockMvcRequestBuilders.delete("/employees/121"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void delDirectorWithChild () throws Exception                //Deleting director with children
    {
        mvc.perform(MockMvcRequestBuilders.delete("/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();
    }

    @Test
    public void delDirectorWithoutChild () throws Exception                //Deleting director with children
    {

        mvc.perform(MockMvcRequestBuilders.delete("/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();
    }

    @Test
    public void delEmployee() throws Exception                //Deleting director without children
    {
        for(int i=10;i>0;i--)
        {
            mvc.perform(MockMvcRequestBuilders.delete("/employees/"+i))
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        }

    }

    /******************** test cases for put requests ********************/
    @Test
    public void updateEmpDemotionIron() throws Exception {
        NewEmployee employee = new NewEmployee();
        employee.setDesignation("Lead");
        employee.setReplace(false);
        //employee.setEmpName("Iron Man");
        //employee.setManagerId(1);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(employee);
        mvc.perform(MockMvcRequestBuilders.put("/employees/2").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

}



















